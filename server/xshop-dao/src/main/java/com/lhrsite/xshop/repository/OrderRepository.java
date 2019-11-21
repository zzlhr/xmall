package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order, String> {


   List<Order> findByUserIdAndStatus(Integer userId, Integer status, Pageable pageable);
}
