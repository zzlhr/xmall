package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class OrderDetails {

    @Id
    private String odId;
    private String orderId;
    private String goodsId;
    private Integer number;
    private BigDecimal transactionPrice;
    private Timestamp createTime;


}
