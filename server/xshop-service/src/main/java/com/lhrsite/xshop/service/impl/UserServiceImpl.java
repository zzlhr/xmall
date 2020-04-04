package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.core.enums.UserStatusEnums;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.core.utils.*;
import com.lhrsite.xshop.mapper.AuthCodeMapper;
import com.lhrsite.xshop.mapper.UserMapper;
import com.lhrsite.xshop.po.*;
import com.lhrsite.xshop.repository.AuthGroupRepository;
import com.lhrsite.xshop.repository.UserLoginRepository;
import com.lhrsite.xshop.repository.UserRepository;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    private final AuthGroupRepository authGroupRepository;

    private final UserLoginRepository userLoginRepository;
    private final AuthCodeMapper authCodeMapper;
    private final RedisUtil<String, User> redisUtil;

    //JPA查询工厂
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthGroupRepository authGroupRepository,
                           UserLoginRepository userLoginRepository,
                           AuthCodeMapper authCodeMapper, RedisUtil<String, User> redisUtil, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authGroupRepository = authGroupRepository;
        this.userLoginRepository = userLoginRepository;
        this.authCodeMapper = authCodeMapper;
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
    }


    @Override
    public UserVO login(String phoneNumber, String password, String smsCode,
                        HttpServletRequest request) throws XShopException {
        log.info("【用户登录】phoneNumber={}, password={}", phoneNumber, password);
        User user = userRepository.findByPhone(phoneNumber);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        if (!"".equals(smsCode)) {
            // 短信登录
            PageHelper.orderBy("createTime desc");
            AuthCode authCode = authCodeMapper.getAuthCode(1, phoneNumber);
            if (authCode == null) {
                throw new XShopException(ErrEumn.AUTH_CODE_ERROR);
            }
            if (authCode.getCode().equals(smsCode)) {
                return createUserLogin(request, user);
            } else {
                throw new XShopException(ErrEumn.AUTH_CODE_ERROR);
            }
        }
        if (user.getPassword().equals(EncryptUtil.encryptPassword(password))) {
            return createUserLogin(request, user);
        } else {
            throw new XShopException(ErrEumn.LOGIN_ERR);
        }
    }

    @Override
    public UserVO createUserLogin(HttpServletRequest request, User user) throws XShopException {
        UserLogin userLogin = new UserLogin();
        userLogin.setId(EncryptUtil.encryptPassword(UUID.randomUUID().toString()));
        userLogin.setLoginIp(IpUtil.getIpAddress(request));
        userLogin.setUserToken(
                EncryptUtil.encryptPassword(
                        UUID.randomUUID()
                                .toString()));
        userLogin.setUserId(user.getUid());

        userLogin.setExpireTime(new Timestamp(
                TimeUtil.addDay(new Date(), 30).getTime()
        ));

        userLogin = userLoginRepository.save(userLogin);

        UserVO userVO = userToUserVO(user, false);
        userVO.setToken(userLogin.getUserToken());
        // redis缓存
        redisUtil.hashPut("login", userVO.getToken(), user);
        return userVO;
    }

    @Override
    public UserVO loginAdmin(String phoneNumber, String password, HttpServletRequest request) throws XShopException {
        User user = userMapper.getAdmin(phoneNumber, EncryptUtil.encryptPassword(password));
        UserVO userVO = userToUserVO(user, false);
        if (userVO == null) {
            throw new XShopException(ErrEumn.LOGIN_ERR);
        }
        if (user.getAdmin() == 0) {
            throw new XShopException(ErrEumn.NOT_ADMIN);
        }
        return userVO;
    }

    @Override
    public UserVO tokenCanUse(String token) throws XShopException {

        User user = redisUtil.hashGet("login", token);

        if (user != null) {
            UserVO userVO = userToUserVO(user, true);
            userVO.setToken(token);
            return userVO;
        }
        throw new XShopException(ErrEumn.EXPIRE_TOKEN);
    }

    @Override
    public PageVO<UserVO> getUsers(String username, String phone, String email, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize, "create_time asc");
        List<UserVO> userList = userMapper.getUsers(SQLUtil.toFuzzySearchVal(email), SQLUtil.toFuzzySearchVal(phone),
                SQLUtil.toFuzzySearchVal(username), null);

        PageVO<UserVO> pageVO = new PageVO<>();
        pageVO = pageVO.init(new PageInfo<>(userList), page);
        pageVO.setArr(userList);
        return pageVO;
    }

    @Override
    public UserVO findUserById(Integer userId,
                               boolean showPhoneNumber) throws XShopException {

        User user = userRepository.getOne(userId);

        return userToUserVO(user, true);

    }

    @Override
    public User findById(Integer userId) throws XShopException {
        return userRepository.findById(userId).orElseThrow(() -> new XShopException(ErrEumn.USER_NO_EXIST));
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User addUser(User user) throws XShopException {

        phoneIsExist(user.getPhone());

        user.setPassword(
                EncryptUtil.encryptPassword(user.getPassword()));


        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new XShopException(ErrEumn.ADD_USER_ERR);
        }

    }

    @Override
    public UserVO updateUser(User user) throws XShopException {

        if (user == null || user.getUid() == null || user.getUid() <= 0) {
            //当修改用户或找不到该用户的id时
            throw new XShopException(ErrEumn.UPDATE_USER_PARAMS_ERR);
        }

        User oldUser = userRepository.findById(user.getUid()).orElseThrow(() -> new XShopException(ErrEumn.USER_NO_EXIST));

        /* 允许修改的字段才进行修改 */
        if (user.getUsername() != null) {
            oldUser.setUsername(user.getUsername());
        }
//        if (user.getPassword() != null) {
//            oldUser.setPassword(EncryptUtil.encryptPassword(user.getPassword()));
//        }
        if (user.getPhone() != null && !user.getPhone().equals(oldUser.getPhone())) {
            // 当修改手机号时判断手机号是否存在
            phoneIsExist(user.getPhone());
            oldUser.setPhone(user.getPhone());
        }
        if (user.getHeader() != null) {
            oldUser.setHeader(user.getHeader());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getAuthGroup() != null) {
            oldUser.setAuthGroup(user.getAuthGroup());
        }


        try {
            return userToUserVO(userRepository.save(oldUser),
                    true);

        } catch (Exception e) {
            // 处理入库失败情况
            throw new XShopException(ErrEumn.UPDATE_USER_ERR);
        }


    }

    @Override
    public UserVO delUser(User user) throws XShopException {
        // 伪删除

        if (user == null || user.getUid() <= 0) {
            throw new XShopException(ErrEumn.DELECT_USER_ERR);
        }
        user = userRepository.findById(user.getUid()).orElseThrow(() -> new XShopException(ErrEumn.USER_NO_EXIST));

        user.setStatus(UserStatusEnums.NOUSE.getCode());

        try {
            return userToUserVO(userRepository.save(user),
                    true);
        } catch (Exception e) {
            throw new XShopException(ErrEumn.DELECT_USER_ERR);
        }

    }

    @Override
    public User tokenGetUser(String token) throws XShopException {

        User user = redisUtil.hashGet("login", token);

        if (user == null) {
            throw new XShopException(ErrEumn.EXPIRE_TOKEN);
        }
        return user;

    }


    @Override
    public User upPasswordBySms(String token, String smsCode, String newPassword) throws XShopException {
        if (newPassword.length() < 6) {
            throw new XShopException(ErrEumn.PASSWORD_NOT6);
        }
        User user = this.tokenGetUser(token);

        log.info("【userPhone】userPhone={}", user.getPhone());
        AuthCode authCode = authCodeMapper.getAuthCode(2, user.getPhone());
        log.info("【authCode】authCode={}", authCode);
        if (authCode == null) {
            throw new XShopException(ErrEumn.AUTH_CODE_ERROR);
        }
        log.info(smsCode);
        if (authCode.getCode().equals(smsCode)) {
            user.setPassword(EncryptUtil.encryptPassword(newPassword));
        } else {
            throw new XShopException(ErrEumn.AUTH_CODE_ERROR);
        }
        return userRepository.save(user);
    }

    @Override
    public User updatePasswordByOldPassword(String token, String oldPassword, String newPassword) throws XShopException {
        if (newPassword.length() < 6) {
            throw new XShopException(ErrEumn.PASSWORD_NOT6);
        }
        User user = this.tokenGetUser(token);
        log.info("【修改前的用户】userPassword={}, oldPassword={}",
                user.getPassword(), EncryptUtil.encryptPassword(oldPassword));

        if (!user.getPassword().equals(EncryptUtil.encryptPassword(oldPassword))) {
            throw new XShopException(ErrEumn.PASSWORD_ERROR);
        }

        user.setPassword(EncryptUtil.encryptPassword(newPassword));
        log.info("【修改后的用户】user={}", user);
        redisUtil.hashPut("login", token, user);
        return userRepository.save(user);
    }


    /**
     * User对象转uservo对象
     *
     * @param userList        user集合
     * @param showPhoneNumber 是否显示手机号:true为显示
     * @return userVO集合
     */
    private List<UserVO> userToUserVO(List<User> userList, boolean showPhoneNumber) {

        List<UserVO> userVOS = new ArrayList<>();

        List<AuthGroup> authGroups = authGroupRepository.findAll();

        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);

            // 处理用户权限组
            String authGroupIds = user.getAuthGroup();
            String[] ags = authGroupIds.split(","); // 分割获取用户权限组id
            List<AuthGroup> userAuthGroup = new ArrayList<>();
            // 循环将权限组加入用户权限集合
            for (String ag : ags) {
                for (AuthGroup authGroup : authGroups) {
                    if (ag.equals(authGroup.getAgid().toString())) {
                        userAuthGroup.add(authGroup);
                    }
                }
            }
            userVO.setAuthGroups(userAuthGroup);


            // 当不显示手机号时隐藏中间几位手机号
            if (!showPhoneNumber){
                userVO.setPhone(userVO.getPhone()
                        .replaceAll("(\\d{3})\\d{4}(\\d{4})",
                                "$1****$2"));

            }

            userVOS.add(userVO);
        }



        return userVOS;
    }


    /**
     * 同上处理单个
     *
     * @param user            单个用户
     * @param showPhoneNumber 是否显示手机号：true为显示
     * @return 单个用户的uservo
     * @throws XShopException erp exception
     */
    private UserVO userToUserVO(User user, boolean showPhoneNumber) throws XShopException {
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        List<User> users = new ArrayList<>();
        users.add(user);
        List<UserVO> userVOS = userToUserVO(users, showPhoneNumber);
        return userVOS.get(0);
    }


    /**
     * 判断手机号是否存在
     *
     * @param phone 手机号
     * @throws XShopException 如果存在抛出异常
     */
    @Override
    public void phoneIsExist(String phone) throws XShopException {
        User user = userRepository.findByPhone(phone);
        log.info("【检测用户是否存在】user={}", user);
        if (user != null) {
            throw new XShopException(ErrEumn.ADD_USER_PHONE_EXIST);
        }
    }

    @Override
    @Transactional
    public void logOut(String token) {
        //此操作为先查询再根据id删除,需要加事务
        userLoginRepository.removeByUserToken(token);
        redisUtil.hashRemove("login", token);

    }

}
