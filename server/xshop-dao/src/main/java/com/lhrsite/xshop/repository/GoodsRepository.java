package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository
        extends JpaRepository<Goods, String> {
    @Query(value = "insert into fav_goods values(null,?1,?2)",nativeQuery = true)
    @Modifying
    void insertFavo(Integer uid, String goodsId);
}
