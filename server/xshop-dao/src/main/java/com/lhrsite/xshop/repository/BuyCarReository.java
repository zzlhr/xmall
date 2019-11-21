package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.BuyCar;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyCarReository extends BaseRepository<BuyCar, String> {

    BuyCar findByUserIdAndGoodsId(Integer userId, String goodId);

}
