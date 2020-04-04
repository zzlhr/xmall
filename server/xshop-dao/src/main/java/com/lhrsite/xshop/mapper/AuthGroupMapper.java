package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.AuthValue;
import com.lhrsite.xshop.vo.AuthGroupVO;
import com.lhrsite.xshop.vo.AuthValueVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthGroupMapper {
    List<AuthGroupVO> getAuthGroupVOList(String agName, Integer agStatus);

    List<AuthValueVO> getAuthValueVOSByGroupId(Integer groupId);

    List<AuthValue> getAuthValuesByGroupId(Integer groupId);


}
