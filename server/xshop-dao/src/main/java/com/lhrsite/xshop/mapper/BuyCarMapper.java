package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.BuyCar;
import com.lhrsite.xshop.vo.BuyCarVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BuyCarMapper {

    List<BuyCarVO> getBuyCar(Integer uid);

    List<BuyCarVO> getBuyCarByIds(@Param("ids") List<String> ids);

    List<BuyCar> getBuyCarByUidAndGoodsId(Integer uid, String goodsId);
}
