package com.lhrsite.xshop.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 编码类
 * @author lhr
 */
public class EncryptUtil {
    /**
     * 所有密码加密都需要走该方法。
     * @param passwordSrc   为加密密码
     * @return              加密后的密码
     */
    public static String encryptPassword(String passwordSrc){
        return DigestUtils.md5Hex(
                        passwordSrc + "@!#DFRT$$%$fdsfdsrenjboq");

    }
}
