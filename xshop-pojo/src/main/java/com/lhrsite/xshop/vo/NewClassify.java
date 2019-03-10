package com.lhrsite.xshop.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NewClassify implements Serializable {

    private Integer clId;
    private String clName;
    private BigDecimal max;
    private BigDecimal min;

}
