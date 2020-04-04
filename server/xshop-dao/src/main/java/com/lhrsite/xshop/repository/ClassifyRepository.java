package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.Classify;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassifyRepository extends BaseRepository<Classify, Integer> {
    List<Classify> findAllByClFid(Integer clFid);
}
