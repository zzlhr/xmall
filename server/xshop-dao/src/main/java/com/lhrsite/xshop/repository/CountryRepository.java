package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    List<Country> findAllByCityId(String cityId);
}
