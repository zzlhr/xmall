package com.lhrsite.xshop.vo;

import com.lhrsite.xshop.po.Goods;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BuyCarVO {

    private String id;
    private Integer userId;
    private Goods goods;

    private Integer number;
    private Timestamp createTime;
}
