package com.lhrsite.xshop.core.utils;

import org.junit.Test;

public class IdentifyUtilTest {

    @Test
    public void idTest() {
        int i = 0;
        while (i < 1000) {
            System.out.println(IdentifyUtil.getRandoms(1));
            i++;
        }
    }

}
