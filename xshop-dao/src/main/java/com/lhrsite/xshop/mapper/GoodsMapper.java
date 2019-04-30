package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.Goods;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GoodsMapper extends Mapper<Goods> {

    /**
     * 更新库存
     *
     * @param goods 商品列表
     */
    void updateGoodsSalesVolumeAndStock(@Param("goods") List<Goods> goods);

    List<Goods> getGoodsByGoodsIds(List<String> goodsIds);

}
