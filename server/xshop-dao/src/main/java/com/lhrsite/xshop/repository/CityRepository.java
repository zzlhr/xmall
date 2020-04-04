package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findAllByProvinceId(String provinceId);
}
