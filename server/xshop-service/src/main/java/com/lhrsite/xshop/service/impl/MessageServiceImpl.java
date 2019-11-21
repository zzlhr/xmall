package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.mapper.MessageMapper;
import com.lhrsite.xshop.vo.MessageVO;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.po.Message;
import com.lhrsite.xshop.po.QMessage;
import com.lhrsite.xshop.po.QUser;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.MessageRepositroy;
import com.lhrsite.xshop.service.MessageService;
import com.lhrsite.xshop.service.UserService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepositroy messageRepositroy;
    private MessageMapper messageMapper;
    private final UserService userService;

    public MessageServiceImpl(MessageRepositroy messageRepositroy, UserService userService) {
        this.messageRepositroy = messageRepositroy;
        this.userService = userService;
    }

    @Override
    public void sendMessage(Message message) {
        if (message.getMsgId() == null || "".equals(message.getMsgId())) {
            message.setMsgId(UUID.randomUUID().toString());
        }
        messageRepositroy.save(message);
    }

    @Override
    public MessageVO readMessage(String msgId) throws XShopException {
        Message message = null;
        Optional<Message> optionalMessage = messageRepositroy.findById(msgId);
        if (optionalMessage.isPresent()) {
            message = optionalMessage.get();
        } else {
            throw new XShopException(ErrEumn.MESSAGE_NOT_EXIST);
        }

        MessageVO messageVO = new MessageVO();
        // 设置消息为已读
        message.setMessageStatus(1);
        messageRepositroy.save(message);
        BeanUtils.copyProperties(message, messageVO);

        if (message.getSendUser() == 0) {
            messageVO.setSendUserName("系统消息");
        } else {
            User user = userService.findById(message.getSendUser());

            messageVO.setSendUserName(user.getUsername());
        }

        return messageVO;
    }

    @Override
    public PageVO<MessageVO> getMessage(String token, Integer messageType,
                                        Integer page, Integer pageSize) throws XShopException {
        User user = userService.tokenGetUser(token);
        PageHelper.startPage(page, pageSize);
        List<MessageVO> messageVOs = messageMapper.getMessageList(user.getUid(), messageType);
        PageInfo<MessageVO> info = new PageInfo<>(messageVOs);

        PageVO<MessageVO> result = PageVO.init(info, messageVOs);

        for (MessageVO messageVO : result.getArr()) {
            messageVO.setSendUserName("系统消息");
        }
        return result;
    }

    @Override
    public void delMessage(String msgId) throws XShopException {
        try {
            messageRepositroy.deleteById(msgId);
        } catch (Exception e) {
            log.error("【删除日志失败】errMsg={}", e.getMessage());
            throw new XShopException(ErrEumn.DEL_MESSAGE_ERROR);
        }
    }

    @Override
    public long getMessageNoReadCount(String token, Integer messageType) throws XShopException {
        User user = userService.tokenGetUser(token);
        QMessage qMessage = QMessage.message;

        return queryFactory.selectFrom(qMessage)
                .where(qMessage.messageStatus.eq(0))
                .where(qMessage.inceptUser.eq(user.getUid()))
                .fetchCount();
    }
}
