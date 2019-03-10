package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.Delivery;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends BaseRepository<Delivery, Integer> {
    Delivery findByDeliveryPhone(String phone);
}
