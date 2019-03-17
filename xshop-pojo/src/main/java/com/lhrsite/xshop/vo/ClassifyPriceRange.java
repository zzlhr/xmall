package com.lhrsite.xshop.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分类价格区间
 */
@Data
public class ClassifyPriceRange implements Serializable {

    private Integer clId;
    private String clName;
    private BigDecimal max;
    private BigDecimal min;

}
