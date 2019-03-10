package com.lhrsite.xshop.vo;

import com.lhrsite.xshop.po.Order;
import com.lhrsite.xshop.po.OrderDetails;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    private Order order;
    private List<OrderDetails> orderDetails;



}
