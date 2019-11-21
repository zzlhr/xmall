package com.lhrsite.xshop.service;


import com.lhrsite.xshop.vo.MenuListVO;
import com.lhrsite.xshop.po.Menu;

import java.util.List;

public interface MenuService {



    List<MenuListVO> getMenuTree(Integer enterprise);

    List<Menu> getMenusByEnterprise(Integer eid, Integer status);

    List<Menu> getMenusByEnterprise(Integer eid);

    Menu getMenuById(Integer mid);

    Menu saveMenu(Menu menu);



}
