package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.GoodsMapper;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.service.FavoriteService;
import com.lhrsite.xshop.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Auther: guandaowei
 * @Date: 2019/4/28
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private UserServiceImpl userService;
    private GoodsMapper goodsMapper;

    @Autowired
    public FavoriteServiceImpl(UserServiceImpl userService, GoodsMapper goodsMapper) {
        this.userService = userService;
        this.goodsMapper = goodsMapper;
    }

    @Override
    @Transactional
    public Boolean addFavorite(String goodsId, String token) throws XShopException {

       User user = userService.tokenGetUser(token);
        if (user == null) {
            throw  new XShopException(ErrEumn.USER_NO_EXIST);
        }
        List<Map<String, Object>> map = goodsMapper.selectFavo(user.getUid(), goodsId);
        if (map == null) {
            Integer integer = goodsMapper.insertFavoGoods(user.getUid(), goodsId);
            return integer > 0;
        }
        return false;


    }

    @Override
    public PageVO queryFavoList(String token, Integer page, Integer pageSize) throws XShopException {
         User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        List<String> GoodsIdList = getFavoGoodsIdList(user.getUid());
        PageVO pageVO = new PageVO();
        PageHelper.startPage(page, pageSize);
        List<Goods> goodsList = goodsMapper.selectGoodsbyIds(listToString(GoodsIdList,','));

        PageInfo pageInfo = new PageInfo<Goods>(goodsList);
        pageVO.setTotalCount(pageInfo.getTotal());
        pageVO.setTotalPage(pageInfo.getPages());
        pageVO.setArr(pageInfo.getList());
        pageVO.setPageSize(pageInfo.getPageSize());

        return pageVO;
    }

    public List<String> getFavoGoodsIdList(Integer uid) {
        List<String> goodsIdList = goodsMapper.getFavoGoodsIdList(uid);
        return goodsIdList;
    }

    @Override
    public void deleteFavo(String token, String goodsId) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        goodsMapper.deleteFavoByUserIdAndGoodsId(user.getUid(), goodsId);
    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            sb.append("'");
            sb.append(list.get(i));
            sb.append("'");
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        System.out.println(sb.toString());
        return sb.toString() ;
    }

}

