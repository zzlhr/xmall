package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<MessageVO> getMessageList(Integer uid, Integer messageType);
    Integer getNoReadMessageCount(Integer uid, Integer messageStatus);

}
