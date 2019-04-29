package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.vo.PageVO;

/**
 * @Description: TODO
 * @Auther: guandaowei
 * @Date: 2019/4/28
 */
public interface FavoriteService {
    Boolean addFavorite(String goodsId, String token) throws XShopException;

    void deleteFavo(String token, String goodsId) throws XShopException;

    PageVO queryFavoList(String token, Integer page, Integer pageSize) throws XShopException;
}
