package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 用户实体类
 */
@Entity
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer enterprise;
    private String header;
    private Integer authGroup;
    private Integer status;
    @Column(insertable = false, updatable = false)
    private java.sql.Timestamp createTime;
    @Column(insertable = false, updatable = false)
    private java.sql.Timestamp updateTime;
    private Integer admin = 0;

    private String wechatOpenid;

}
