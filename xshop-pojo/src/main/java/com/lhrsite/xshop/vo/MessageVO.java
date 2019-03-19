package com.lhrsite.xshop.vo;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class MessageVO implements Serializable {

    private String msgId;
    private Integer messageType;
    private String messageValue;
    // 消息状态 0未读，1已读，2删除
    private Integer messageStatus;
    // 发送人
    private Integer sendUser;
    // 接收人
    private Integer inceptUser;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String sendUserName;


}
