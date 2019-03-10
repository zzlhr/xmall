package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * 菜单实体类
 */
@Entity
@Data
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;
    private String menuName;
    private String menuUrl;
    private String menuApi;
    private Integer menuFmid;
    private Integer menuStatus;
    private Integer menuLevel;
    private Integer enterprise;
    private Integer project;
    private Integer updateUser;
    @Column(insertable = false, updatable = false)
    private Date createTime;
    @Column(insertable = false, updatable = false)
    private Date updateTime;
}
