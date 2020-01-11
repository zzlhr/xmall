package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.ConfigSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigSettingRepository extends JpaRepository<ConfigSetting, Integer> {
}
