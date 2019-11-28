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
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private ResultVO resultVO;
    private final AuthCodeService authCodeService;
    private AuthCodeRepository authCodeRepository;

    @Autowired
    public UserController(UserService userService, AuthCodeService authCodeService, AuthCodeRepository authCodeRepository) {
        this.authCodeService = authCodeService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);
        this.userService = userService;
        this.authCodeRepository = authCodeRepository;
    }



    @PostMapping("/sendCode")
    public ResultVO sendCode(String phoneNumber) throws XShopException {
        log.info(phoneNumber);
        authCodeService.sendMessage(phoneNumber, 0);
        return resultVO;
    }

    @PostMapping("/sendUpPwdCode")
    public ResultVO sendUpPwdCode(String token) throws XShopException {
        authCodeService.sendUpPwdMessage(token);
        return resultVO;
    }


    @PostMapping("/sendLoginCode")
    public ResultVO sendLoginCode(String phoneNumber) throws XShopException {
        authCodeService.sendMessage(phoneNumber, 1);
        return resultVO;
    }


    @PostMapping("/login")
    public ResultVO login(String phone,
                          @RequestParam(defaultValue = "") String password,
                          @RequestParam(defaultValue = "") String smsCode,
                          HttpServletRequest request)
            throws XShopException {
        log.info("【登录】phone={}, password={}", phone, password);
        resultVO.setData(userService.login(phone, password, smsCode, request));
        return resultVO;
    }


    @PostMapping("/loginAdmin")
    public ResultVO loginAdmin(String phone,
                               String password,
                               HttpServletRequest request)
            throws XShopException {
        log.info("【登录】phone={}, password={}", phone, password);
        resultVO.setData(userService.loginAdmin(phone, password, request));
        return resultVO;
    }

    @PostMapping("/tokenUse")
    public ResultVO tokenUse(String token) throws XShopException {
        log.info("【验证token是否可用】token={}", token);

        resultVO.setData(userService.tokenCanUse(token));
        return resultVO;
    }

    @PostMapping("/userList")
    public ResultVO userList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phoneNum,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        log.info("【查询用户】params={}", request.getParameterMap());
        User user = new User();
        user.setUsername(username);
        user.setPhone(phoneNum);
        user.setEmail(email);

        resultVO.setData(userService.getUser(user, page, pageSize));

        return resultVO;
    }

    @PostMapping("/getUser")
    public ResultVO getUser(Integer uid,
                            @RequestParam(defaultValue = "0") int showPhone)
            throws XShopException {
        log.info("【获取用户】uid={}", uid);
        if (uid == null) {
            throw new XShopException(ErrEumn.PARAM_IS_NULL);
        }
        resultVO.setData(userService.findUserById(uid, showPhone == 1));

        return resultVO;
    }


    @PostMapping("/addUser")
    public ResultVO addUser(HttpServletRequest request, User user, String code) throws XShopException {

        log.info("【添加用户】user={},code={}", user, code);
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
            user.setAuthGroup(0);
        }
        if (user.getAdmin() == null) {
            user.setAdmin(0);
        }
        Optional<AuthCode> authCodeOptional = authCodeRepository.findById(user.getPhone());
        AuthCode authCode;
        if (authCodeOptional.isPresent()) {
            authCode = authCodeOptional.get();
        } else {
            throw new XShopException(ErrEumn.AUTH_CODE_ERROR);
        }

        if (!authCode.getCode().equals(code)) {
            throw new XShopException(ErrEumn.AUTH_CODE_ERROR);
        }

        resultVO.setData(userService.addUser(user));
        authCodeRepository.delete(authCode);
        UserVO userVO = userService.createUserLogin(request, user);
        resultVO.setData(userVO);
        return resultVO;
    }

    @PostMapping("/upPassword")
    public ResultVO upPassword(String token, String smsCode, String newPassword) throws XShopException {
        resultVO.setData(userService.upPasswordBySms(token, smsCode, newPassword));
        return resultVO;
    }

    @PostMapping("/editUser")
    public ResultVO editUser(User user) throws XShopException {
        log.info("【修改用户】user={}", user);
        if (user == null) {
            throw new XShopException(ErrEumn.ADD_USER_IS_NULL);
        }
        if (user.getUid() == null || user.getUid() == 0) {
            throw new XShopException(ErrEumn.ADD_USER_UID_IS_NULL);
        }
        User oldUser = userService.findById(user.getUid());
        if (user.getUsername() != null) {
            oldUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            oldUser.setPassword(EncryptUtil.encryptPassword(user.getPassword()));
        }
        if (user.getPhone() != null) {
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
        resultVO.setData(userService.updateUser(user));
        return resultVO;
    }

    @PostMapping("/phoneIsExist")
    public ResultVO phoneIsExist(String phone) throws XShopException {
        userService.phoneIsExist(phone);
        return resultVO;
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
