package com.lhrsite.xshop.core.utils;

public class SQLUtil {

    /**
     * 模糊查询处理器
     * @param val 要查询的内容
     * @return 加上%%的内容
     */
    public static String toFuzzySearchVal(String val){
        if (val == null){
            return null;
        }
        return "%" + val + "%";
    }

    /**
     * 模糊查询处理器
     * @param val 要查询的内容
     * @param onlyLeft 只加左边
     * @return 加上%%的内容
     */
    public static String toFuzzySearchVal(String val, boolean onlyLeft){
        if (val == null){
            return null;
        }
        if (onlyLeft){
            return val + "%";
        }
        return toFuzzySearchVal(val);
    }

}
