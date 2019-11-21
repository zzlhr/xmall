package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.core.enums.AuthGroupEnums;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.AuthGroup;
import com.lhrsite.xshop.po.Menu;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.service.AuthGroupService;
import com.lhrsite.xshop.service.MenuService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthGroupController {


    private final AuthGroupService authGroupService;
    private final MenuService menuService;
    private ResultVO resultVO;
    private final UserService userService;

    @Autowired
    public AuthGroupController(AuthGroupService authGroupService, MenuService menuService, UserService userService) {
        this.menuService = menuService;
        this.userService = userService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        this.authGroupService = authGroupService;
    }

    @GetMapping("/getAuthGroupDropDown")
    public ResultVO getAuthGroupDropDown(Integer enterprise){
        resultVO.setData(authGroupService.getAuthGroupDropDown(enterprise));
        return resultVO;
    }

    @GetMapping("/getMenuList")
    public ResultVO getMenuList(Integer enterprise){
        resultVO.setData(menuService.getMenuTree(enterprise));
        return resultVO;
    }

    @PostMapping("/getMenu")
    public ResultVO getMenu(Integer mid){
        resultVO.setData(menuService.getMenuById(mid));
        return resultVO;
    }


    @PostMapping("/saveMenu")
    public ResultVO saveMenu(Menu menu){
        menu.setCreateTime(null);
        menu.setUpdateTime(null);
        log.info("【保存菜单】menu={}", menu);
        resultVO.setData(menuService.saveMenu(menu));
        return resultVO;
    }

    @PostMapping("/getAuthGroup")
    public ResultVO getAuthGroup(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String agName,
            @RequestParam(required = false, defaultValue = "0") Integer agStatus,
            @RequestParam Integer enterpriseId){
        resultVO.setData(authGroupService.selectAuthGroup(page, pageSize, agName, agStatus, enterpriseId));
        return resultVO;
    }

    @PostMapping("/editAuthGroup")
    public ResultVO editAuthGroup(AuthGroup authGroup)
            throws XShopException {

        if (authGroup == null){
            throw new XShopException(ErrEumn.AUTH_GROUP_NULL);
        }
        if (authGroup.getAgName() == null ||
                "".equals(authGroup.getAgName())){
            throw new XShopException(ErrEumn.AUTH_GROUP_NAME_NULL);
        }
        if (authGroup.getAgStatus() == null){
            authGroup.setAgStatus(AuthGroupEnums.USE.getCode());
        }
        if (authGroup.getEnterprise() == null){
            throw new XShopException(ErrEumn.ENTERPRISE_NULL);
        }
        if (authGroup.getUpdateUser() == null){
            throw new XShopException(ErrEumn.UPDATEUSER_NULL);
        }


        resultVO.setData(authGroupService.editAuthGroup(authGroup));
        return resultVO;
    }


    @PostMapping("/getAuthGroups")
    public ResultVO getAuthGroups(String token) throws XShopException {

        if (token == null || token.equals("")){
            throw new XShopException(ErrEumn.TOKEN_IS_NULL);
        }

        User user = userService.tokenGetUser(token);
        List<AuthGroup> authGroupList =
                authGroupService.getAuthGroup(user.getEnterprise());
        resultVO.setData(authGroupList);
        return resultVO;
    }


    @PostMapping("/getAuthValue")
    public ResultVO getAuthValue(Integer groupId){
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("openAuth", authGroupService.getOpenAuth(groupId));
        mapData.put("authValue", authGroupService.getAuthValue(groupId));
        resultVO.setData(mapData);
        return resultVO;
    }


    @PostMapping("/openAuth")
    public ResultVO getOpenAuth(Integer groupId){
        resultVO.setData(Arrays.toString(authGroupService.getOpenAuth(groupId)));
        return resultVO;
    }

    @PostMapping("/saveAuthValue")
    public ResultVO saveAuthValue(String authValues, Integer groupId, String token) throws XShopException {
        String[] avs;
        if (!(authValues == null || "".equals(authValues))){
            avs = authValues.split("\\|");
            List<Integer> menuIds = Arrays.asList(new Integer[avs.length]);
            int i = 0;
            for (String authValueId : avs){
                if (!authValueId.equals("")){
                    menuIds.set(i, Integer.valueOf(authValueId));
                    i++;
                }
            }
            Map<String, Object> mapData = new HashMap<>();

            mapData.put("openAuth", authGroupService.getOpenAuth(groupId));
            mapData.put("authValue", authGroupService.saveAuthValue(menuIds, groupId, token));

        }else {
            resultVO.setData(new ArrayList<>());
        }

        return resultVO;



    }




}
