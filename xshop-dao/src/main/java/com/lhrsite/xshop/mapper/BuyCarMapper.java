package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.vo.BuyCarVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BuyCarMapper {

    List<BuyCarVO> getBuyCar(Integer uid);

    List<BuyCarVO> getBuyCarByIds(List<String> ids);
}
