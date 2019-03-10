package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Message implements Serializable {

    @Id
    private String msgId;
    private Integer messageType;
    private String messageValue;
    // 消息状态 0未读，1已读，2删除
    private Integer messageStatus;
    // 发送人
    private Integer sendUser;
    // 接收人
    private Integer inceptUser;
    @Column(insertable = false, updatable = false)
    private Date createTime;
    @Column(insertable = false, updatable = false)
    private Date updateTime;
}
