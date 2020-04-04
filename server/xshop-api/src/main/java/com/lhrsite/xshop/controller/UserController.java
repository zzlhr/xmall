package com.lhrsite.xshop.controller;


import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.core.utils.Captcha;
import com.lhrsite.xshop.core.utils.EncryptUtil;
import com.lhrsite.xshop.core.utils.WeChatUtil;
import com.lhrsite.xshop.po.AuthCode;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.repository.AuthCodeRepository;
import com.lhrsite.xshop.service.AuthCodeService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthCodeService authCodeService;

    @Autowired
    public UserController(UserService userService, AuthCodeService authCodeService) {
        this.authCodeService = authCodeService;
        this.userService = userService;
    }


    @PostMapping("/sendCode")
    public ResultVO sendCode(String phoneNumber) throws XShopException {
        log.info(phoneNumber);
        authCodeService.sendMessage(phoneNumber, 0);
        return ResultVO.create();
    }

    @PostMapping("/sendUpPwdCode")
    public ResultVO sendUpPwdCode(String token) throws XShopException {
        authCodeService.sendUpPwdMessage(token);
        return ResultVO.create();
    }


    @PostMapping("/sendLoginCode")
    public ResultVO sendLoginCode(String phoneNumber) throws XShopException {
        authCodeService.sendMessage(phoneNumber, 1);
        return ResultVO.create();
    }


    @PostMapping("/login")
    public ResultVO login(String phone,
                          @RequestParam(defaultValue = "") String password,
                          @RequestParam(defaultValue = "") String smsCode,
                          HttpServletRequest request)
            throws XShopException {
        log.info("【登录】phone={}, password={}", phone, password);
        return ResultVO.create(userService.login(phone, password, smsCode, request));
    }


    @PostMapping("/loginAdmin")
    public ResultVO loginAdmin(String phone,
                               String password,
                               HttpServletRequest request)
            throws XShopException {
        log.info("【登录】phone={}, password={}", phone, password);
        return ResultVO.create(userService.loginAdmin(phone, password, request));
    }

    @PostMapping("/tokenUse")
    public ResultVO tokenUse(String token) throws XShopException {
        log.info("【验证token是否可用】token={}", token);
        return ResultVO.create(userService.tokenCanUse(token));
    }

    @PostMapping("/userList")
    public ResultVO userList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {

        return ResultVO.create(userService.getUsers(username, phone, email, page, pageSize));
    }

    @PostMapping("/getUser")
    public ResultVO getUser(Integer uid,
                            @RequestParam(defaultValue = "0") int showPhone)
            throws XShopException {
        log.info("【获取用户】uid={}", uid);
        if (uid == null) {
            throw new XShopException(ErrEumn.PARAM_IS_NULL);
        }
        return ResultVO.create(userService.findUserById(uid, showPhone == 1));
    }


    /**
     * 管理员添加用户
     * @param user 用户对象
     * @return 返回对象
     * @throws XShopException xshop exception
     */
    @PostMapping("/addUser")
    public ResultVO addUser(User user) throws XShopException {

        if (user == null) {
            throw new XShopException(ErrEumn.ADD_USER_IS_NULL);
        }
        if (user.getUsername() == null) {
            throw new XShopException(ErrEumn.ADD_USER_USERNAME_IS_NULL);
        }
        if (user.getPassword() == null) {
            throw new XShopException(ErrEumn.ADD_USER_PASSWORD_IS_NULL);
        }
        if (user.getPhone() == null) {
            throw new XShopException(ErrEumn.ADD_USER_PHONE_IS_NULL);
        }
        if (user.getStatus() == null) {
            user.setStatus(0);
        }
        if (user.getHeader() == null) {
            user.setHeader("");
        }
        if (user.getEmail() == null) {
            user.setEmail("");
        }
        if (user.getAuthGroup() == null) {
            user.setAuthGroup("");
        }
        if (user.getAdmin() == null) {
            user.setAdmin(0);
        }

        return ResultVO.create(userService.addUser(user));
    }

    @PostMapping("/upPassword")
    public ResultVO upPassword(String token, String smsCode, String newPassword) throws XShopException {
        return ResultVO.create(userService.upPasswordBySms(token, smsCode, newPassword));
    }

    @PostMapping("/editUser")
    public ResultVO editUser(User user) throws XShopException {
        log.info("【修改用户】user={}", user);
        if (user == null) {
            throw new XShopException(ErrEumn.ADD_USER_IS_NULL);
        }
        return ResultVO.create(userService.updateUser(user));
    }

    @PostMapping("/phoneIsExist")
    public ResultVO phoneIsExist(String phone) throws XShopException {
        userService.phoneIsExist(phone);
        return ResultVO.create();
    }

    @GetMapping("/authCode")
    public void authCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        OutputStream os = response.getOutputStream();
        //返回验证码和图片的map
        Map<String, Object> map = Captcha.getImageCode(86, 37, os);
        String simpleCaptcha = "simpleCaptcha";
        request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        request.getSession().setAttribute("codeTime", new Date().getTime());
        try {
            ImageIO.write((BufferedImage) map.get("image"), "jpg", os);
        } catch (IOException e) {
            log.error("生成验证码失败");
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }

}
