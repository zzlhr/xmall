package com.lhrsite.xshop.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 统计视图对象
 */
@Data
public class OrderStatisticsVO implements Serializable {

    /**
     * 时间
     */
    private String time;

    /**
     * 订单数
     */
    private Integer orderNumber;

}
