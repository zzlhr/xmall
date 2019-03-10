package com.lhrsite.xshop.service;

import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.UserVO;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.core.exception.XShopException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户服务
 * @author lhr
 */
public interface UserService {



    UserVO bindWeChat(String code, String token) throws IOException, XShopException;

    UserVO weChatLogin(HttpServletRequest request, String openId) throws XShopException;

    UserVO login(String phoneNumber, String password, String smsCode,
                 HttpServletRequest request) throws XShopException;

    UserVO createUserLogin(HttpServletRequest request, User user) throws XShopException;

    UserVO loginAdmin(String phoneNumber, String password,
                      HttpServletRequest request) throws XShopException;
    /**
     * use token get user object
     * @param token The token obtained by the user login
     * @return      user data
     * @throws XShopException
     */
    UserVO tokenCanUse(String token) throws XShopException;


    /**
     * get user list ande select user list
     * @param user you can use username, phone, email, enterpriseId query,
     *             and username,phone, email is "like query"
     *
     * @param page page number, page size default 10
     *
     * @return     UserVO list and pagination data
     */
    PageVO<UserVO> getUser(User user, int page, int pageSize);


    UserVO findUserById(Integer userId, boolean showPhoneNumber) throws XShopException;

    User findById(Integer userId) throws XShopException;
    User findByPhone(String phone);

    User addUser(User user) throws XShopException;

    UserVO updateUser(User user) throws XShopException;

    UserVO delUser(User user) throws XShopException;

    User tokenGetUser(String token) throws XShopException;
    User upPassword(String token, String oldPassword, String newPassword) throws XShopException;

    void phoneIsExist(String phone) throws XShopException;

}
