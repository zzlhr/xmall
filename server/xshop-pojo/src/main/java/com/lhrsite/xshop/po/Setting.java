package com.lhrsite.xshop.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统设置表
 */
@Data
public class Setting implements Serializable {

    /* 设置id */
    private Integer settingId;
    /* 设置代号 */
    private String settingCode;
    /* 配置项值 */
    private String settingValue;
    /* 企业id */
    private Integer eid;

}
