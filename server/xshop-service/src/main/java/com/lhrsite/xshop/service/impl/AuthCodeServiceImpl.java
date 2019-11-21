package com.lhrsite.xshop.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.lhrsite.xshop.mapper.AuthCodeMapper;
import com.lhrsite.xshop.po.AuthCode;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.AuthCodeRepository;
import com.lhrsite.xshop.service.AuthCodeService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.core.utils.AliMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class AuthCodeServiceImpl extends BaseServiceImpl implements AuthCodeService {

    private final AuthCodeRepository authCodeRepository;
    private JPAQueryFactory queryFactory;
    private final UserService userService;
    private final AuthCodeMapper authCodeMapper;

    @Autowired
    public AuthCodeServiceImpl(AuthCodeRepository authCodeRepository, UserService userService,
                               EntityManager entityManager, AuthCodeMapper authCodeMapper) {
        super(entityManager);
        this.authCodeRepository = authCodeRepository;
        this.userService = userService;
        this.authCodeMapper = authCodeMapper;
        this.queryFactory = getQueryFactory();
    }

    @Override
    public void sendMessage(String phone, Integer type) throws XShopException {
        // 发送前首先删除所有需要发送类型的验证码
        List<AuthCode> authCodes = authCodeMapper.getAuthCodes(type, phone);
        try {
            authCodeRepository.deleteAll(authCodes);
        } catch (Exception e) {
            throw new XShopException(ErrEumn.SEND_MESSAGE_ERROR);
        }
        AuthCode authCode = new AuthCode();
        // 如果是注册判断手机号是否存在
        if (type == 0 && userService.findByPhone(phone) != null) {
            throw new XShopException(ErrEumn.ADD_USER_PHONE_EXIST);
        }
        String code = randomNumber();
        authCode.setCode(code);
        authCode.setPhoneNumber(phone);
        AliMessage aliMessage = new AliMessage();
        authCode.setType(1);
        sendSMS(phone, code, aliMessage, type == 1 ? "SMS_148593213" : "SMS_143700003");
        authCodeRepository.save(authCode);
    }

    @Override
    public void sendUpPwdMessage(String token) throws XShopException {
        String phone = userService.tokenGetUser(token).getPhone();
        int type = 2;
        // 发送前首先删除所有需要发送类型的验证码
        List<AuthCode> authCodes = authCodeMapper.getAuthCodes(type, phone);
        try {
            authCodeRepository.deleteAll(authCodes);
        } catch (Exception e) {
            throw new XShopException(ErrEumn.SEND_MESSAGE_ERROR);
        }
        AuthCode authCode = new AuthCode();
        String code = randomNumber();
        authCode.setCode(code);
        authCode.setPhoneNumber(phone);
        AliMessage aliMessage = new AliMessage();
        authCode.setType(2);
        sendSMS(phone, code, aliMessage, "SMS_150180006");
        authCodeRepository.save(authCode);
    }

    private void sendSMS(String phoneNumber, String code, AliMessage aliMessage, String modelCode) throws XShopException {
        try {
            if (!aliMessage.sendMessage(phoneNumber, code, modelCode)) {
                throw new XShopException(ErrEumn.SEND_MESSAGE_ERROR);
            }
        } catch (ClientException e) {
            e.printStackTrace();
            log.info(phoneNumber);
            log.error(e.getMessage());
            throw new XShopException(ErrEumn.SEND_MESSAGE_ERROR);
        }
    }

    private String randomNumber() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(9));
        }
        return code.toString();
    }

}
