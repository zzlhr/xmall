package com.lhrsite.xshop.service;

import com.lhrsite.xshop.vo.MessageVO;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.po.Message;
import com.lhrsite.xshop.core.exception.XShopException;

public interface MessageService {


    void sendMessage(Message message);


    MessageVO readMessage(String msgId) throws XShopException;



    PageVO<MessageVO> getMessage(String token, Integer messageType,
                                 Integer page, Integer pageSize) throws XShopException;

    void delMessage(String msgId) throws XShopException;

    long getMessageNoReadCount(String token) throws XShopException;
}
