package com.lhrsite.xshop.core.utils;

import java.util.Random;

/**
 * 唯一标识生成器
 */
public class IdentifyUtil {

    public static String getIdentify() {
        return System.currentTimeMillis() + getRandoms(6);
    }

    static String getRandoms(Integer length) {
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

}
