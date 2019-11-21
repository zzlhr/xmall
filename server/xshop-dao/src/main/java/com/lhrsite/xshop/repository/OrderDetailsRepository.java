package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.OrderDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends BaseRepository<OrderDetails, String> {

    List<OrderDetails>  findByOrderId(String orderId);
}
