package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /* 主键 */
    private Integer id;
    /* 用户id */
    @NotNull(message = "uid不能为空")
    private Integer uid;
    /* 省份id */
    @NotBlank(message = "省份不能为空")
    private String province;
    /* 城市id */
    @NotBlank(message = "城市不能为空")
    private String city;
    /* 区域id */
    @NotBlank(message = "区域不能为空")
    private String country;
    /* 街道id */
    private String town;
    /* 地址 */
    @NotBlank(message = "地址不能为空")
    private String addr;
    /* 收货人 */
    @NotBlank(message = "收货人不能为空")
    private String consignee;
    /* 联系方式 */
    @NotBlank(message = "联系方式不能为空")
    private String linkTel;
    /* 默认状态 */
    @NotNull
    private Integer defaultStatus;

}
