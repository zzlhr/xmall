package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository
        extends BaseRepository<Menu, Integer> {


    List<Menu> findAllByMenuStatusAndEnterpriseIn(Integer status, List<Integer> enterprises, Sort sort);

    List<Menu> findAllByEnterpriseIn(List<Integer> enterprises);

    List<Menu> findAllByEnterpriseIn(List<Integer> enterprises, Sort sort);




}
