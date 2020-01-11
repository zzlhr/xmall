package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.AuthCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthCodeMapper {

    List<AuthCode> getAuthCodes(Integer type, String phone);
    AuthCode getAuthCode(Integer type, String phone);
}
