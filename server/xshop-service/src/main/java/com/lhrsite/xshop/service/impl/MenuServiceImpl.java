package com.lhrsite.xshop.service.impl;


import com.lhrsite.xshop.po.Menu;
import com.lhrsite.xshop.vo.MenuListVO;
import com.lhrsite.xshop.repository.MenuRepository;
import com.lhrsite.xshop.service.MenuService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository,
                           EntityManager entityManager) {
        this.menuRepository = menuRepository;
    }


    @Override
    public List<MenuListVO> getMenuTree() {

        List<Menu> menus = this.getMenus(Menu.ENABLE);

        List<MenuListVO> menuListVOS = new ArrayList<>();

        menus.forEach(menu -> {
            menuListVOS.add(makeMenuListVO(menu));
        });
        List<MenuListVO> result = new ArrayList<>();

        menuListVOS.forEach(menuListVO -> {
            if (menuListVO.getFid() == 0) {
                result.add(menuListVO);
            } else {
                // Client
                menuListVOS.forEach(m -> {
                    if (m.getId() == menuListVO.getFid()) {
                        m.getChildren().add(menuListVO);
                    }
                });
            }
        });

        return result;
    }

    @Override
    public List<Menu> getMenus(Integer status) {
        return menuRepository.findAllByMenuStatus(status, Sort.by(Sort.Direction.DESC, "menuLevel"));
    }


    @Override
    public Menu getMenuById(Integer mid) {
        return menuRepository.findById(mid).orElse(null);
    }

    @Override
    public Menu saveMenu(Menu menu) {
        if (menu.getUpdateUser() == null) {
            menu.setUpdateUser(1);
        }


        return menuRepository.save(menu);
    }

    private void addTopMenu(List<MenuListVO> menuListVOS, Menu menu) {
        menuListVOS.add(makeMenuListVO(menu));
    }

    private MenuListVO makeMenuListVO(Menu menu) {
        MenuListVO menuListVO = new MenuListVO();
        menuListVO.setId(menu.getMid());
        menuListVO.setLabel(menu.getMenuName());
        menuListVO.setChildren(new ArrayList<>());
        menuListVO.setFid(menu.getMenuFmid());
        menuListVO.setLevel(menu.getMenuLevel());
        return menuListVO;
    }

    private List<MenuListVO> getClient(List<Menu> menus, Integer fid, int addTime) {
        List<MenuListVO> menuListVOS = new ArrayList<>();


        menus.forEach(menu -> {
            if (fid.equals(menu.getMenuFmid())) {
                menuListVOS.add(makeMenuListVO(menu));
            }
        });
        return menuListVOS;
    }

}
