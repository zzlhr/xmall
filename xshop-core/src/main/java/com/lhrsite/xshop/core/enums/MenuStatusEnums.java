package com.lhrsite.xshop.core.enums;

public enum MenuStatusEnums {
    USE(0),
    NOUSE(1)
    ;


    private int code;

    public int getCode() {
        return code;
    }

    MenuStatusEnums(int code){
        this.code = code;
    }
}
