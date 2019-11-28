package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserVO> getUsers(String email, String phone, String userName, Integer userStatus);

}
