package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.Enterprise;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EnterpriseMapper {
    List<Enterprise> getEnterprises(String epName, Integer type, Integer status);
}
