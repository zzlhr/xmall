package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.Enterprise;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnterpriseRepository
        extends BaseRepository<Enterprise, Integer> {

    List<Enterprise> findEnterprisesByEpNameLike(String epName);


}
