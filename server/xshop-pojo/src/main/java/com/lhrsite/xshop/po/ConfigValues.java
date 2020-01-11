package com.lhrsite.xshop.po;

import java.io.Serializable;

import lombok.Data;

import javax.persistence.*;

/**
 * 存储每个企业所有配置项的值
 *
 * @author haoranliu
 */
@Entity
@Data
public class ConfigValues implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /* 配置值id */
    private Integer cvsid;
    /* 配置类型 */
    private Integer ctType;
    /* 配置值 */
    private String cvsValue;
    /* 配置值描述 */
    private String cvsDescription;
    /* 所属企业 */
    private Integer cvsEnterprise;

}
