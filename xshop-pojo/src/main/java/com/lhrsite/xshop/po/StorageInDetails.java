package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * 入库单详情(StStorageInDetails)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
@Entity
@Table(name = "st_storage_in_details")
public class StorageInDetails implements Serializable {
    @Id
    //入库单详情id
    private String sidId;
    //入库单主表id
    private String siId;
    //商品id
    private String goodsId;
    //商品数量
    private Double goodsNumber;
    //商品单位
    private String goodsUnit;
    //入库单价
    private BigDecimal goodsInUnitPrice;
    //商品总运费
    private BigDecimal goodsTotalDespatch;
    //商品总价
    private BigDecimal goodsTotalPrice;
    //创建时间
    @Column(insertable = false, updatable = false)
    private Date createTime;
    //创建用户
    private Integer createUser;
    //更新时间
    @Column(insertable = false, updatable = false)
    private Date updateTime;
    //更新人
    private Integer updateUser;
    //备注
    private String sidRemark;


}