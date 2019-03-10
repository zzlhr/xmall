package com.lhrsite.xshop.po;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 配置设置表
 * 存储每个企业的配置
 *
 * @author haoranliu
 */
@Entity
@Data
public class ConfigSetting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /* 配置设置id */
    private Integer csid;
    /* 配置项类型 */
    private Integer ctType;
    /* 配置项值 */
    private Integer configValue;
    /* 配置值所属企业 */
    private Integer configEnterprise;

}
