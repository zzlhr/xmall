package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 用户登录记录
 */
@Data
@Entity
public class UserLogin implements Serializable {

    @Id
    private String id;
    private Integer userId;
    private String userToken;
    private String loginIp = "未知";
    private String loginAddress = "未知";
    private String loginApp = "未知";
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    private Timestamp expireTime = new Timestamp(0);


}
