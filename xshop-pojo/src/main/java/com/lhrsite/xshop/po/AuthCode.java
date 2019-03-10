package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class AuthCode implements Serializable {
    @Id
    private String phoneNumber;

    private String code;

    @Column(insertable = false, updatable = false)
    private Date createTime;

    private Integer type;
}
