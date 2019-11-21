package com.lhrsite.xshop.vo;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class DeliveryVO implements Serializable {
    private Integer did;
    private String deliveryName;
    private String deliveryPhone;
    private Integer deliveryStatus;
    private String token;
    private Timestamp createTime;
}
