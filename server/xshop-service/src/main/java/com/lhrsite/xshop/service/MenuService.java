package com.lhrsite.xshop.service;


import com.lhrsite.xshop.vo.MenuListVO;
import com.lhrsite.xshop.po.Menu;

import java.util.List;

public interface MenuService {

    List<MenuListVO> getMenuTree();

    List<Menu> getMenus(Integer status);


    Menu getMenuById(Integer mid);

    Menu saveMenu(Menu menu);



}
