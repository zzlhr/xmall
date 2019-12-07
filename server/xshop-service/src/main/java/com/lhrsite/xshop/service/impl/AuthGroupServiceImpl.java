package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.core.enums.AuthGroupEnums;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.AuthGroupMapper;
import com.lhrsite.xshop.po.*;
import com.lhrsite.xshop.repository.AuthGroupRepository;
import com.lhrsite.xshop.repository.AuthValueRepository;
import com.lhrsite.xshop.repository.MenuRepository;
import com.lhrsite.xshop.service.AuthGroupService;
import com.lhrsite.xshop.service.MenuService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.AuthGroupDropDownVO;
import com.lhrsite.xshop.vo.AuthGroupVO;
import com.lhrsite.xshop.vo.AuthValueVO;
import com.lhrsite.xshop.vo.PageVO;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class AuthGroupServiceImpl implements AuthGroupService {

    private final AuthGroupRepository authGroupRepository;

    private final AuthValueRepository authValueRepository;

    private final MenuRepository menuRepository;
    private final AuthGroupMapper authGroupMapper;
    private final UserService userService;
    private final MenuService menuService;


    @Autowired
    public AuthGroupServiceImpl(AuthGroupRepository authGroupRepository,
                                AuthValueRepository authValueRepository, MenuRepository menuRepository,
                                AuthGroupMapper authGroupMapper, UserService userService, MenuService menuService) {
        this.authGroupRepository = authGroupRepository;
        this.authValueRepository = authValueRepository;
        this.menuRepository = menuRepository;
        this.authGroupMapper = authGroupMapper;
        this.userService = userService;
        this.menuService = menuService;
    }

    @Override
    public List<AuthGroupDropDownVO> getAuthGroupDropDown() {

        List<AuthGroupDropDownVO> authGroupDropDownVOS = new ArrayList<>();

        List<AuthGroup> authGroups = authGroupRepository.findAllByAgStatus(1);


        authGroups.forEach(authGroup ->
                makeDropDown(authGroupDropDownVOS, authGroup));


        return authGroupDropDownVOS;
    }

    @Override
    public PageVO<AuthGroupVO> selectAuthGroup(Integer page, Integer pageSize, String agName, Integer agStatus) {
        PageHelper.startPage(page, pageSize);

        List<AuthGroupVO> authGroupList = authGroupMapper.getAuthGroupVOList(agName, agStatus);

        PageInfo<AuthGroupVO> pageInfo = new PageInfo<>(authGroupList);
        PageVO<AuthGroupVO> authGroupVOPageVO = new PageVO<>();
        authGroupVOPageVO = authGroupVOPageVO.init(pageInfo, page);
        return authGroupVOPageVO;
    }

    @Override
    @Transactional
    public AuthGroup editAuthGroup(AuthGroup authGroup)
            throws XShopException {

        AuthGroup authGroup1;
        try {
            authGroup1 = authGroupRepository.save(authGroup);
            if (authGroup.getAgid() == null ||
                    "".equals(authGroup.getAgid()) ||
                    0 == authGroup.getAgid()) {
                List<Menu> menus = menuRepository.findAll();

                List<AuthValue> initAuthValues = new ArrayList<>();
                for (Menu menu : menus) {
                    AuthValue authValue = new AuthValue();
                    authValue.setGroupId(authGroup1.getAgid());
                    authValue.setMenuId(menu.getMenuFmid());
                    authGroup.setUpdateUser(authGroup1.getUpdateUser());
                    authValue.setValue(0);
                    initAuthValues.add(authValue);
                }

                authValueRepository.saveAll(initAuthValues);
            }
        } catch (Exception e) {
            throw new XShopException(ErrEumn.EDIT_AUTH_GROUP_ERROR);
        }

        return authGroup1;

    }

    @Override
    public List<AuthGroup> getAuthGroup() {

        return authGroupRepository
                .findAllByAgStatus(AuthGroupEnums.USE.getCode());
    }

    @Override
    public List<AuthValueVO> getAuthValue(Integer groupId) {
        return authGroupMapper.getAuthValueVOSByGroupId(groupId);
    }

    @Override
    @Transactional
    public List<AuthValue> saveAuthValue(List<Integer> menuIds,
                                         Integer groupId, String token) throws XShopException {

        User user = userService.tokenGetUser(token);

        List<AuthValue> avs = new ArrayList<>();
        List<Menu> menuList =
                menuService.getMenus(
                        authGroupRepository
                                .findById(groupId)
                                .orElseThrow(() -> new XShopException(ErrEumn.MENU_NOT_FIND))
                                .getAgid()
                );
        menuList.forEach(menuListVO -> {
            AuthValue authValue = new AuthValue();
            authValue.setMenuId(menuListVO.getMid());
            authValue.setGroupId(groupId);
            authValue.setValue(0);
            authValue.setUpdateUser(user.getUid());
            avs.add(authValue);
        });

        authValueRepository.saveAll(avs);

        return authGroupMapper.getAuthValuesByGroupId(groupId);
    }


    private void makeDropDown(
            List<AuthGroupDropDownVO> result, AuthGroup authGroup) {
        AuthGroupDropDownVO authGroupDropDownVO = new AuthGroupDropDownVO();
        BeanUtils.copyProperties(authGroup, authGroupDropDownVO);
        result.add(authGroupDropDownVO);
    }
}
