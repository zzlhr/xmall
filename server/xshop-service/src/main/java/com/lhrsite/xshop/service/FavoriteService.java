package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.vo.PageVO;

/**
 * 用户收藏服务
 *
 * @author : guandaowei
 * @date: 2019/4/28
 */
public interface FavoriteService {
    void addFavorite(String goodsId, String token) throws XShopException;

    boolean isFavorite(String token, String goodsId) throws XShopException;

    PageVO<Goods> getFavoriteList(String token, Integer page, Integer pageSize) throws XShopException;


    void deleteFavorite(Integer fgId, String token) throws XShopException;
}
