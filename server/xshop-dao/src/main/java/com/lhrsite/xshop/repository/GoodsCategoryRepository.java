package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory, Integer> {
}
