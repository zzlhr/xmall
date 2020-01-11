package com.lhrsite.xshop.vo;

import com.lhrsite.xshop.po.Goods;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderInfoVO {
    private String odId;

    private String orderId;
    private Goods goods;

    private Integer number;

    private BigDecimal transactionPrice;

}
