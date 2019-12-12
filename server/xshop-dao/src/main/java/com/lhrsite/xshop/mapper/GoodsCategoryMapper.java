package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsCategoryMapper {

    List<GoodsCategory> getGoodsCategory(Integer fid, Integer status);

}
