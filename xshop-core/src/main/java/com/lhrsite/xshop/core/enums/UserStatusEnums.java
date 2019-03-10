package com.lhrsite.xshop.core.enums;

public enum UserStatusEnums {

    USE(0),
    NOUSE(1)
    ;


    private int code;

    public int getCode() {
        return code;
    }

    UserStatusEnums(int code){
        this.code = code;
    }




}
