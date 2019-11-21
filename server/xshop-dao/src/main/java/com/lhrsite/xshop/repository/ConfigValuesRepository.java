package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.ConfigValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigValuesRepository extends JpaRepository<ConfigValues, Integer> {
}
