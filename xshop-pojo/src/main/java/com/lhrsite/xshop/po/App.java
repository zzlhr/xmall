package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;

/**
 * (Address)表实体类
 *
 * @author lhr
 * @since 2018-08-22 14:08:28
 */
@Entity
@Data
@Table(name = "`app`")
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //联系方式
    private String link;
    //首页轮播图
    private String picture;

}