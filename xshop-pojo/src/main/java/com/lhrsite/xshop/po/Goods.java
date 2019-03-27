package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
public class Goods implements Serializable {

    @Id
    /* 商品id */
    private String goodsId;
    /* 商品标题 */
    private String title;
    /* 商品描述 */
    @Column(name = "`describe`")
    private String describe;
    /* 原价 */
    private BigDecimal originalPrice;
    /* 促销价 */
    private BigDecimal salePrice;
    /* 促销状态0未促销，1促销中 */
    private Integer saleStatus;
    /* 促销类型 */
    private Integer saleType;
    /* 商品封面 */
    private String cover;
    /* 状态，0上架，1下架 */
    private Integer status;
    /* 库存 */
    private Integer stock;
    /* 月销量 */
    private Integer salesVolume;
    /* 发货地 */
    private String deliveryPlace;
    /* 快递费 */
    private BigDecimal despatchMoney;
    /* 快递费是否累加 */
    private Integer despatchIsPlus;
    /* 运费累加单价 */
    private BigDecimal despatchPlusMoney;
    /* 免邮费数量，购买数到达该数量免邮费*/
    private Integer freePostageNum;
    /* 是否执行免邮数量 */
    private Integer execFreePostageNum;
    /* 内容 */
    private String content;
    /* banner图 */
    private String pictures;
    /* 更新人id */
    private Integer updateUser;
    /* 创建时间 */
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    /* 更新时间 */
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;
    /* 所属分类 */
    private Integer clId;
    /* 所属分类父级 */
    private Integer clFid;

}
