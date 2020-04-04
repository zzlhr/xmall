package com.lhrsite.xshop.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单结算对象
 */
@Data
public class OrderSettleAccounts {
    private BigDecimal goodsNumber;
    private BigDecimal despatchNumber;
    private List<OrderDetails> orderDetailsList;
}
