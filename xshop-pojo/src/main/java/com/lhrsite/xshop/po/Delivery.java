package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Delivery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer did;
    private String deliveryName;
    private String deliveryPhone;
    private String deliveryPassword;
    private Integer deliveryStatus;
    @Column(insertable = false, updatable = false)
    private Date createTime;
    private String token;
}
