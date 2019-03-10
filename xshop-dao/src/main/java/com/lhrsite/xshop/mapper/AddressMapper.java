package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.vo.AddressVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AddressMapper {
    List<AddressVO> getAddress(Integer uid);
}
