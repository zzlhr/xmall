package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@IdClass(AuthValuePK.class)
public class AuthValue implements Serializable {

    @Id
    private Integer groupId;

    @Id
    private Integer menuId;
    private Integer value;
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;
    private Integer updateUser;


}
