package com.lhrsite.xshop.po;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 配置类型表
 * 存储所有配置项
 *
 * @author haoranliu
 */
@Entity
@Data
public class ConfigType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /* 配置类型id */
    private Integer ctid;
    /* 配置类型 */
    private Integer ctType;
    /* 配置类型描述 */
    private String ctDescription;

}
