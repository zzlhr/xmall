package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {
    List<Town> findAllByCountryId(String countryId);
}
