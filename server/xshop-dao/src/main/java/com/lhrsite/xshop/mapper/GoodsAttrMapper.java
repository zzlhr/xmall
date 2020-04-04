package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.GoodsAttrKey;
import com.lhrsite.xshop.po.GoodsAttrVal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsAttrMapper {

    List<GoodsAttrKey> getGoodsAttrKeys(Integer goodsCategoryId);
    List<GoodsAttrVal> getGoodsAttrVals(Integer goodsCategoryId);

}
