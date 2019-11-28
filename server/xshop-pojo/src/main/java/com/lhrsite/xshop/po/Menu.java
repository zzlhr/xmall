package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 菜单实体类
 */
@Entity
@Data
public class Menu implements Serializable {

    public static final Integer ENABLE = 1; // 启用
    public static final Integer DISABLE = 0; // 禁用


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;
    private String menuName;
    private String menuCode;
    private Integer menuFmid;
    private Integer menuStatus;
    private Integer menuLevel;
    private Integer updateUser;
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;
}
