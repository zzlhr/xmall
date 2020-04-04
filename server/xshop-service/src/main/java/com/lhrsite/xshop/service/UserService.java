package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户服务
 *
 * @author lhr
 */
public interface UserService {


    UserVO login(String phoneNumber, String password, String smsCode,
                 HttpServletRequest request) throws XShopException;

    UserVO createUserLogin(HttpServletRequest request, User user) throws XShopException;

    UserVO loginAdmin(String phoneNumber, String password,
                      HttpServletRequest request) throws XShopException;

    /**
     * use token get user object
     *
     * @param token The token obtained by the user login
     * @return user data
     * @throws XShopException
     */
    UserVO tokenCanUse(String token) throws XShopException;


    /**
     * get user list ande select user list
     *
     * @param username search username
     * @param email search email
     * @param phone search phone
     * @param page page number, page size default 10
     * @return UserVO list and pagination data
     */
    PageVO<UserVO> getUsers(String username, String phone, String email, Integer page, Integer pageSize);

    UserVO findUserById(Integer userId, boolean showPhoneNumber) throws XShopException;

    User findById(Integer userId) throws XShopException;

    User findByPhone(String phone);

    User addUser(User user) throws XShopException;

    UserVO updateUser(User user) throws XShopException;

    UserVO delUser(User user) throws XShopException;

    User tokenGetUser(String token) throws XShopException;

    /**
     * 通过验证码修改密码
     *
     * @param token       用户令牌
     * @param smsCode     验证码
     * @param newPassword 新密码
     * @return 修改后的用户
     * @throws XShopException
     */
    User upPasswordBySms(String token, String smsCode, String newPassword) throws XShopException;

    User updatePasswordByOldPassword(String token, String oldPassword, String newPassword) throws XShopException;

    void phoneIsExist(String phone) throws XShopException;


    void logOut(String token);
}
