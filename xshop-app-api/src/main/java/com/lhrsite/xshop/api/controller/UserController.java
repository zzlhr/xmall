package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.impl.UserServiceImpl;
import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 */

@RestController
@RequestMapping("/user")
public class UserController {
    private ResultVO resultVO;

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        this.userService = userService;
    }


    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息和登录token
     */
    @PostMapping("/login")
    public ResultVO login(String userName, String password, HttpServletRequest request) throws XShopException {
        UserVO userVO = userService.login(userName, password, "", request);
        resultVO.setData(userVO);
        return resultVO;
    }


    /**
     * 退出登录
     *
     * @param token 用户令牌
     * @return
     */
    @PostMapping("/loginOut")
    public ResultVO loginOut(String token) {
        userService.logOut(token);
        return resultVO;
    }

    /**
     * 修改密码
     *
     * @param token       用户令牌
     * @param oldPassword 老的密码
     * @param newPassword 新的密码
     * @return 修改结果
     * @throws XShopException
     */
    @PostMapping("/updatePassword")
    public ResultVO updatePassword(String token, String oldPassword, String newPassword) throws XShopException {
        resultVO.setData(userService.upPassword(token, oldPassword, newPassword));
        return resultVO;
    }


}
