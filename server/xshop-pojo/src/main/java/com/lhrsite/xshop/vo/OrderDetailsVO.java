package com.lhrsite.xshop.vo;

import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.po.Order;
import com.lhrsite.xshop.po.OrderDetails;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsVO {

    private String orderId;
    private Order order;
    private List<OrderDetails> orderDetails;
    private List<Goods> goods;

}
