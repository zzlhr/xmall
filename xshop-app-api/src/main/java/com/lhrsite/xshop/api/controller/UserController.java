package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */

@RestController
@RequestMapping("/user")
public class UserController {
    private ResultVO resultVO;

    @Autowired
    public UserController() {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }


    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息和登录token
     */
    @PostMapping("/login")
    public ResultVO login(String userName, String password) {
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
        return resultVO;
    }


}
