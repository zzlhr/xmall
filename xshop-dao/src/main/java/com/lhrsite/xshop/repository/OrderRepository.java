package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, String> {
}
