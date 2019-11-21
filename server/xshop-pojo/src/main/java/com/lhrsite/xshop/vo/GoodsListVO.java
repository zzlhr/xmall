package com.lhrsite.xshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class GoodsListVO {

    private String goodsId;
    private String title;
    private String describe;
    private String content;


    private BigDecimal originalPrice;

    private BigDecimal salePrice;

    private Integer saleStatus;

    // 促销类型
    private Integer saleType;

    private String cover;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 库存
     */
    private Integer stock;

    /**
     * 月销量
     */
    private Integer salesVolume;

    private String deliveryPlace;

    private BigDecimal despatchMoney;

    private Integer updateUser;

    private String updateUserName;


    private Timestamp createTime;
    private Timestamp updateTime;

    private Integer clId;
    private String clName;

    private Integer clFid;
    private String clFName;


}
