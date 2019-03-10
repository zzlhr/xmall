package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository
        extends JpaRepository<Goods, String> {

}
