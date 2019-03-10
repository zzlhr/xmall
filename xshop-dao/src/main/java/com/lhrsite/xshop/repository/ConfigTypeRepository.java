package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigTypeRepository extends JpaRepository<ConfigType, Integer> {
}
