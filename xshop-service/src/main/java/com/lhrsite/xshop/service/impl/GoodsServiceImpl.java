package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.vo.GoodsListVO;
import com.lhrsite.xshop.vo.GoodsPullDown;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.po.*;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.GoodsRepository;
import com.lhrsite.xshop.repository.MessageRepositroy;
import com.lhrsite.xshop.service.GoodsService;
import com.lhrsite.xshop.core.utils.EncryptUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
@Slf4j
public class GoodsServiceImpl extends BaseServiceImpl implements GoodsService {


    private final GoodsRepository goodsRepository;
    private JPAQueryFactory queryFactory;
    private final MessageRepositroy messageRepositroy;

    @Autowired
    public GoodsServiceImpl(EntityManager entityManager, GoodsRepository goodsRepository, MessageRepositroy messageRepositroy) {
        super(entityManager);
        this.goodsRepository = goodsRepository;
        this.messageRepositroy = messageRepositroy;
        this.queryFactory = getQueryFactory();
    }

    @Override
    public PageVO<GoodsListVO> getGoodsList(String title, Integer cid, Integer saleType, long page, long pageSize) {

        QGoods qGoods = QGoods.goods;
        QUser quser = QUser.user;
        QClassify qClassify = QClassify.classify;
        BooleanBuilder builder = new BooleanBuilder();
        if (title != null && !title.equals("")) {
            builder.and(qGoods.title.like("%" + title + "%"));
        }
        // 判断如果saleType不为0或者null说明查询了某个促销类型
        if (saleType != null && saleType != 0) {
            builder.and(qGoods.saleType.eq(saleType));
        }
        if (cid != null) {
            builder.and(qGoods.clId.eq(cid));
        }
        log.info("【分页】page={}, pageSize={}", page, pageSize);

        JPAQuery<GoodsListVO> jpaQuery = queryFactory
                .select(
                        Projections.bean(
                                GoodsListVO.class,
                                qGoods.goodsId,
                                qGoods.title,
                                qGoods.content,
                                qGoods.describe,
                                qGoods.originalPrice,
                                qGoods.salePrice,
                                qGoods.saleStatus,
                                qGoods.cover,
                                qGoods.status,
                                qGoods.stock,
                                qGoods.salesVolume,
                                qGoods.deliveryPlace,
                                qGoods.despatchMoney,
                                qGoods.updateUser,
                                qGoods.createTime,
                                qGoods.updateTime,
                                quser.username,
                                qClassify.clId,
                                qClassify.clName,
                                qClassify.clFid
                        )
                )
                .from(qGoods)
                .innerJoin(quser).on(qGoods.updateUser.eq(quser.uid))
                .innerJoin(qClassify).on(qClassify.clId.eq(qGoods.clId))
                .offset((page - 1) * pageSize)
                .limit(pageSize)
                .where(builder)
                .orderBy(qGoods.salesVolume.asc());


        PageVO<GoodsListVO> pageVO = new PageVO<>();

        pageVO.init(jpaQuery, page);

        return pageVO;
    }


    @Override
    public Goods addGoods(Goods goods, String msg) throws XShopException {
        //月销量0
        goods.setSalesVolume(0);
        if (goods.getGoodsId() == null || "".equals(goods.getGoodsId())) {
            goods.setGoodsId(EncryptUtil.encryptPassword(UUID.randomUUID().toString()));
        }
        // 如果促销进行推送
        sendMessage(goods, msg);
        QGoods qGoods = QGoods.goods;
        if (queryFactory.selectFrom(qGoods)
                .where(qGoods.title.eq(goods.getTitle()))
                .fetchCount() > 0) {
            throw new XShopException(ErrEumn.GOODS_EXIST);
        }
        return goodsRepository.save(goods);
    }

    @Override
    public Goods updateGoods(Goods goods, String msg) {
        sendMessage(goods, msg);
        return goodsRepository.save(goods);
    }

    private void sendMessage(Goods goods, String msg) {
        log.info("【发送消息】msg={}", msg);
        if (goods.getSaleStatus() == 1) {
            Message message = new Message();
            message.setMsgId(UUID.randomUUID().toString());
            message.setMessageType(2);
            message.setMessageValue("".equals(msg) ? goods.getTitle() + goods.getSalePrice() + "厂家直销|" + goods.getGoodsId() : msg);
            message.setSendUser(0);
            message.setInceptUser(0);
            message.setMessageStatus(0);
            messageRepositroy.save(message);
        }
    }


    @Override
    public Goods getById(String goodsId) {
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);
        return goodsOptional.orElse(null);
    }

    @Override
    public List<GoodsPullDown> pullDown(String title) {
        QGoods qGoods = QGoods.goods;

        return queryFactory.select(
                Projections.bean(
                        GoodsPullDown.class,
                        qGoods.goodsId,
                        qGoods.title
                )
        ).from(qGoods)
                .where(qGoods.title.like("%" + title + "%"))
                .fetch();
    }

    @Override
    public List<Message> getMessage(Integer type) {

        QMessage qMessage = QMessage.message;
        long time = 10 * 60 * 1000;
        Date afterDate = new Date(new Date().getTime() - time);//30分钟后的时间

        return queryFactory.selectFrom(qMessage)
                .where(qMessage.messageType.eq(2))
                .where(qMessage.createTime.between(type == 1 ? getStartTimeOfDay(System.currentTimeMillis()) : afterDate, new Date()))
                .fetch();
    }

    //获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
    private Date getStartTimeOfDay(long now) {
        String tz = "GMT+8";
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    @Override
    public void deleteGoods(String goodsId) {
        goodsRepository.deleteById(goodsId);

    }
}
