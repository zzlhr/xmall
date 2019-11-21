package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.po.Message;
import com.lhrsite.xshop.vo.GoodsListVO;
import com.lhrsite.xshop.vo.GoodsPullDown;
import com.lhrsite.xshop.vo.PageVO;

import java.util.List;

public interface GoodsService {


    /**
     * 获取商品列表
     *
     * @param title    标题 模糊查询
     * @param cid      分类id
     * @param orderBy  排序方式 使用 字段名|desc/asc形式
     * @param saleType 促销否
     * @param page     页码
     * @param pageSize 每页数量
     * @return 商品列表
     */
    PageVO<GoodsListVO> getGoodsList(String title, Integer cid, String orderBy,
                                     Integer saleType, Integer page, Integer pageSize);

    Goods addGoods(Goods goods, String msg) throws XShopException;

    Goods updateGoods(Goods goods, String msg);

    Goods getById(String goodsId);

    List<GoodsPullDown> pullDown(String title);

    List<Message> getMessage(Integer type);

    void deleteGoods(String goodsId);
}
