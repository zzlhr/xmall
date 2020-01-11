package com.lhrsite.xshop.vo;

import lombok.Data;

@Data
public class AuthValueVO {

    private Integer agid;

    /** 权限组名称 */
    private String agName;

    private Integer mid;

    private String menuName;

    private String menuCode;

    private Integer menuFmid;

    private Integer menuStatus;

}
