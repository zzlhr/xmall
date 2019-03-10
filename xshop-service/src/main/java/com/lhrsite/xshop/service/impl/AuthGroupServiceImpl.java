package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.core.enums.AuthGroupEnums;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
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
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class AuthGroupServiceImpl extends BaseServiceImpl implements AuthGroupService {

    private final AuthGroupRepository authGroupRepository;

    private final AuthValueRepository authValueRepository;

    private final MenuRepository menuRepository;
    //实体管理者
    private final EntityManager entityManager;

    private final UserService userService;
    private final MenuService menuService;

    @Autowired
    public AuthGroupServiceImpl(AuthGroupRepository authGroupRepository,
                                AuthValueRepository authValueRepository, MenuRepository menuRepository, EntityManager entityManager, UserService userService, MenuService menuService) {
        super(entityManager);
        this.authGroupRepository = authGroupRepository;
        this.authValueRepository = authValueRepository;
        this.menuRepository = menuRepository;
        this.entityManager = entityManager;
        this.userService = userService;
        this.menuService = menuService;
    }

    @Override
    public List<AuthGroupDropDownVO> getAuthGroupDropDown(Integer enterprise) {

        List<AuthGroupDropDownVO> authGroupDropDownVOS = new ArrayList<>();

        List<AuthGroup> authGroups = authGroupRepository
                .findAllByEnterpriseAndAgStatus(enterprise, 0);


        authGroups.forEach(authGroup ->
                makeDropDown(authGroupDropDownVOS, authGroup));


        return authGroupDropDownVOS;
    }

    @Override
    public PageVO<AuthGroupVO> selectAuthGroup(
            long page, long pageSize,
            String agName, Integer agStatus,
            Integer enterprise) {
        QAuthGroup qAuthGroup = QAuthGroup.authGroup;
        QUser qUser = QUser.user;
        QEnterprise qEnterprise = QEnterprise.enterprise;

        BooleanBuilder builder = new BooleanBuilder();

        if (agName != null && !"".equals(agName)) {
            builder.and(qAuthGroup.agName.like("%" + agName + "%"));
        }
        if (agStatus != null) {
            builder.and(qAuthGroup.agStatus.eq(agStatus));
        }
        if (enterprise != null) {
            builder.and(qAuthGroup.enterprise.eq(enterprise));
        }

        JPAQuery<AuthGroupVO> select = getQueryFactory()
                .select(
                        Projections.bean(
                                AuthGroupVO.class,
                                qAuthGroup.agid,
                                qAuthGroup.agName,
                                qAuthGroup.enterprise,
                                qAuthGroup.agStatus,
                                qAuthGroup.createTime,
                                qAuthGroup.updateTime,
                                qAuthGroup.project,
                                qAuthGroup.updateUser,
                                qUser.username,
                                qEnterprise.epShortName
                        )
                ).from(qAuthGroup)
                .innerJoin(qEnterprise)
                .on(qAuthGroup.enterprise.eq(qEnterprise.eid))
                .innerJoin(qUser)
                .on(qAuthGroup.updateUser.eq(qUser.uid))
                .where(builder)
                .offset((page - 1) * pageSize)
                .limit(pageSize);

        List<AuthGroupVO> authGroupList =
                select.fetch();


        PageVO<AuthGroupVO> authGroupVOPageVO = new PageVO<>();
        authGroupVOPageVO.init(select.fetchCount(), page, authGroupList);


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
                List<Menu> menus = menuRepository
                        .findAllByEnterpriseIn(Arrays.asList(authGroup1.getEnterprise(), 0));

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
    public List<AuthGroup> getAuthGroup(Integer enterpriseId) {

        return authGroupRepository
                .findAllByEnterpriseAndAgStatus(enterpriseId,
                        AuthGroupEnums.USE.getCode());
    }

    @Override
    public List<AuthValueVO> getAuthValue(Integer groupId) {
        QAuthValue qAuthValue = QAuthValue.authValue;
        QAuthGroup qAuthGroup = QAuthGroup.authGroup;
        QMenu qMenu = QMenu.menu;


        return getQueryFactory()
                .select(
                        Projections.bean(
                                AuthValueVO.class,
                                qAuthGroup.agid,
                                qAuthGroup.agName,
                                qAuthGroup.enterprise,
                                qMenu.mid,
                                qMenu.menuName,
                                qMenu.menuUrl,
                                qMenu.menuApi,
                                qMenu.menuFmid,
                                qMenu.menuStatus
                        )
                ).from(qAuthValue)
                .innerJoin(qAuthGroup)
                .on(qAuthValue.groupId.eq(qAuthGroup.agid))
                .innerJoin(qMenu)
                .on(qAuthValue.menuId.eq(qMenu.mid))
                .where(qAuthValue.groupId.eq(groupId))
                .fetch();
    }

    @Override
    @Transactional
    public List<AuthValue> saveAuthValue(List<Integer> menuIds,
                                         Integer groupId, String token) throws XShopException {

        User user = userService.tokenGetUser(token);
        QAuthValue qAuthValue = QAuthValue.authValue;

        List<AuthValue> avs = new ArrayList<>();
        List<Menu> menuList =
                menuService.getMenusByEnterprise(
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


//        getQueryFactory()
//                .update(qAuthValue)
//                .set(qAuthValue.value, 0)
//                .where(qAuthValue.groupId.eq(groupId))
//                .execute();


        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAuthValue.groupId.eq(groupId));
        BooleanBuilder builder1 = new BooleanBuilder();
        for (Integer menu : menuIds) {
            builder1.or(qAuthValue.menuId.eq(menu));
        }
        builder.and(builder1);


        getQueryFactory().update(qAuthValue)
                .set(qAuthValue.value, 1)
                .set(qAuthValue.updateUser, user.getUid())
                .where(builder)
                .execute();


        return authValueRepository.findAllByGroupId(groupId);
    }

    @Override
    public Integer[] getOpenAuth(Integer groupId) {
        QAuthValue qAuthValue = QAuthValue.authValue;
        List<AuthValue> authValues = getQueryFactory().selectFrom(qAuthValue)
                .where(qAuthValue.groupId.eq(groupId))
                .where(qAuthValue.value.eq(1))
                .fetch();
        Integer[] resultIds = new Integer[authValues.size()];
        final int[] i = {0};
        authValues.forEach(authValue -> {
            resultIds[i[0]] = authValue.getMenuId();
            i[0]++;
        });

        return resultIds;
    }

    private void makeDropDown(
            List<AuthGroupDropDownVO> result, AuthGroup authGroup) {
        AuthGroupDropDownVO authGroupDropDownVO = new AuthGroupDropDownVO();
        BeanUtils.copyProperties(authGroup, authGroupDropDownVO);
        result.add(authGroupDropDownVO);
    }
}
