package com.lhrsite.xshop.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsStatisticsVO implements Serializable {

    private String goodsId;
    private String goodsName;
    private long saleNumber;

}
