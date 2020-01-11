package com.lhrsite.xshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


@Data
public class OrderListVO {

    private String orderId;
    private Integer userId;
    private BigDecimal orderMoney;
    private BigDecimal orderAmount;
    private BigDecimal despatchMoney;
    // 优惠
    private BigDecimal offer;
    private Integer status;
    private Timestamp createTime;
    private List<OrderInfoVO> orderInfoVOS;
    private String username;
    private String phone;
    private String email;
    private String addr;
    private Integer deliveryId;

}
