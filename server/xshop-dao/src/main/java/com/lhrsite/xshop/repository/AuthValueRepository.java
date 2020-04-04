package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.AuthValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AuthValueRepository
        extends JpaRepository<AuthValue, Integer> {

    List<AuthValue> findAllByGroupId(Integer groupId);

    @Transactional
    @Modifying
    @Query(value = "update AuthValue set value=0 where group_id=:groupId")
    void updateAllValueByGroupId(@Param("groupId") Integer groupId);

}
