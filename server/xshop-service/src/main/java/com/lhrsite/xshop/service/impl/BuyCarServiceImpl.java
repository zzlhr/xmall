package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.BuyCarMapper;
import com.lhrsite.xshop.po.BuyCar;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.repository.BuyCarReository;
import com.lhrsite.xshop.service.BuyCarService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.BuyCarVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BuyCarServiceImpl implements BuyCarService {


    private final BuyCarReository buyCarReository;
    private final UserService userService;
    private final BuyCarMapper buyCarMapper;

    @Autowired
    public BuyCarServiceImpl(BuyCarReository buyCarReository, UserService userService, BuyCarMapper buyCarMapper) {
        this.userService = userService;
        this.buyCarMapper = buyCarMapper;
        this.buyCarReository = buyCarReository;
    }


    public BuyCar minusBuyCar(BuyCar buyCar, Integer number) {
        BuyCar buyCar1 = buyCarReository.findByUserIdAndGoodsId(buyCar.getUserId(), buyCar.getGoodsId());
        buyCar1.setNumber(buyCar1.getNumber() - number);
        if (buyCar1.getNumber() <= 0) {
            deleteBuyCar(buyCar.getId());
        }
        return buyCarReository.save(buyCar1);
    }

    @Override
    public void deleteBuyCar(String buyCarId) {
        buyCarReository.deleteById(buyCarId);
    }


    @Override
    public List<BuyCarVO> getBuyCar(String token) throws XShopException {
        User user = userService.tokenGetUser(token);

        return buyCarMapper.getBuyCar(user.getUid());

    }

    @Override
    public BuyCar addBuyCar(String token, String goodsId, Integer num, Integer standardId) throws XShopException {
        log.info("【number】number={}", standardId);
        User user = userService.tokenGetUser(token);

        BuyCar buyCar = buyCarReository.findByUserIdAndGoodsId(user.getUid(), goodsId);
        if (buyCar != null) {
            buyCar.setNumber(buyCar.getNumber() + num);

        } else {
            buyCar = new BuyCar();
            buyCar.setUserId(user.getUid());
            buyCar.setGoodsId(goodsId);
            buyCar.setNumber(num);
            buyCar.setId(createBuyCarId());
        }

        return buyCarReository.save(buyCar);

    }

    @Override
    public BuyCar minusBuyCar(String token, String goodsId, Integer number) throws XShopException {
        User user = userService.tokenGetUser(token);
        List<BuyCar> buyCars = buyCarMapper.getBuyCarByUidAndGoodsId(user.getUid(), goodsId);
        BuyCar buyCar = buyCars.get(0);

        buyCar.setNumber(buyCar.getNumber() - number);

        return buyCarReository.save(buyCar);
    }

    @Override
    public List<BuyCarVO> getBuyCarByIds(List<String> buyCarIds) {
        return buyCarMapper.getBuyCarByIds(buyCarIds);
    }

    private synchronized String createBuyCarId() {
        return String.valueOf(new Date().getTime());
    }
}
