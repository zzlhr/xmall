package com.lhrsite.xshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@RestController
public class HomeController {


    @RequestMapping("/wx")
    public String wx(String signature, String timestamp, String nonce, String echostr) throws NoSuchAlgorithmException {
        String token = "xuanhuobang";
        String[] array = new String[]{token, timestamp, nonce};
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //排序
        Arrays.sort(array);

        //生成字符串
        StringBuilder content = new StringBuilder();
        for (String anArray : array) {
            content.append(anArray);
        }

        //sha1加密
        String temp = getSha1(content.toString());

        return temp.equals(signature) ? echostr : "";
    }

    private static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

}
