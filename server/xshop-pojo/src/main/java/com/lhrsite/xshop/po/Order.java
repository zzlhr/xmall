package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "order_master")
public class Order implements Serializable {

    @Id
    private String orderId;
    private Integer userId;
    private BigDecimal orderAmount;
    private BigDecimal despatchMoney;
    // 优惠
    @Column(name = "`offer`")
    private BigDecimal offer;
    // -1删除
    private Integer status;
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    private Integer addrId;


}
