package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.FavoriteGoodsMapper;
import com.lhrsite.xshop.mapper.GoodsMapper;
import com.lhrsite.xshop.po.FavoriteGoods;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.service.FavoriteService;
import com.lhrsite.xshop.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户收藏服务实现
 *
 * @author : guandaowei
 * @date: 2019/4/28
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private UserServiceImpl userService;
    private final GoodsMapper goodsMapper;
    private final FavoriteGoodsMapper favoriteGoodsMapper;

    @Autowired
    public FavoriteServiceImpl(UserServiceImpl userService, GoodsMapper goodsMapper,
                               FavoriteGoodsMapper favoriteGoodsMapper) {
        this.userService = userService;
        this.goodsMapper = goodsMapper;
        this.favoriteGoodsMapper = favoriteGoodsMapper;
    }

    @Override
    @Transactional
    public void addFavorite(String goodsId, String token) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        // 验证是否已经添加过该收藏了
        if (isFavorite(token, goodsId)) {
            throw new XShopException(ErrEumn.ALREADY_ADD_FAVORITE);
        }

        favoriteGoodsMapper.insertFavoriteGoods(user.getUid(), goodsId);

    }

    @Override
    public boolean isFavorite(String token, String goodsId) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        FavoriteGoods favoriteGoods = favoriteGoodsMapper.getUserFavoriteByGoodsId(user.getUid(), goodsId);
        return favoriteGoods != null;
    }

    @Override
    public PageVO<Goods> getFavoriteList(String token, Integer page, Integer pageSize) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }

        PageVO<Goods> pageVO = new PageVO<>();
//        PageHelper.startPage(page, pageSize);
//        List<String> goodsIdList = favoriteGoodsMapper.getFavoriteGoodsIdList(user.getUid());
//        List<Goods> goods = goodsMapper.getGoodsByGoodsIds(goodsIdList);
//        PageInfo pageInfo = new PageInfo<>(goods);
//        pageVO.setTotalCount(pageInfo.getTotal());
//        pageVO.setTotalPage(pageInfo.getPages());
//        pageVO.setArr(goods);
//        pageVO.setPageSize(pageInfo.getPageSize());

        return pageVO;
    }


    @Override
    public void deleteFavorite(Integer fgId, String token) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        favoriteGoodsMapper.deleteFavoriteByFgId(fgId, user.getUid());
    }


}

