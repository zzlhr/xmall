package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.AuthGroup;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthGroupRepository
        extends BaseRepository<AuthGroup, Integer> {


    List<AuthGroup> findAllByAgStatus(Integer status);


}
