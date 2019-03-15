package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    void updateGoodsSalesVolumeAndStock(@Param("goods") List<Goods> goods);

}
