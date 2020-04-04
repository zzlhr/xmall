package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class GoodsMaster {

    @Id
    private String goodsId;
    private String goodsTitle;
    private String goodsDescribe;
    private String goodsCover;
    private Integer goodsStatus;
    private String goodsContent;
    private String goodsBanner;
    private Integer updateUser;
    @Column(updatable = false, insertable = false)
    private java.sql.Timestamp createTime;
    @Column(updatable = false, insertable = false)
    private java.sql.Timestamp updateTime;
    private Integer category1;
    private Integer category2;

}
