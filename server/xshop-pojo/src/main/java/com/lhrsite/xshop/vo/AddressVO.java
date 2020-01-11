package com.lhrsite.xshop.vo;

import lombok.Data;

@Data
public class AddressVO {

    private Integer id;
    /* 用户id */
    private Integer uid;
    private String username;
    /* 省份id */
    private String province;
    private String provinceName;

    /* 城市id */
    private String city;
    private String cityName;

    /* 区域id */
    private String country;
    private String countryName;

    /* 街道id */
    private String town;
    private String townName;

    /* 地址 */
    private String addr;
    private String addrName;

    /* 收货人 */
    private String consignee;

    /* 联系方式 */
    private String linkTel;
    /* 默认状态 */
    private Integer defaultStatus;
}
