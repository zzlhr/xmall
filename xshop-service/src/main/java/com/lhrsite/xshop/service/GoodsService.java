package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.po.Message;
import com.lhrsite.xshop.vo.GoodsListVO;
import com.lhrsite.xshop.vo.GoodsPullDown;
import com.lhrsite.xshop.vo.PageVO;

import java.util.List;

public interface GoodsService {


    PageVO<GoodsListVO> getGoodsList(String title, Integer cid, Integer saleType, long page, long pageSize);

    Goods addGoods(Goods goods, String msg) throws XShopException;

    Goods updateGoods(Goods goods, String msg);

    Goods getById(String goodsId);

    List<GoodsPullDown> pullDown(String title);

    List<Message> getMessage(Integer type);

    void deleteGoods(String goodsId);
}
