package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository
        extends BaseRepository<Menu, Integer> {

    List<Menu> findAllByMenuStatus(Integer status, Sort sort);

}
