package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
public class GoodsSpec {

  
  private String goodsId;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer goodsSpecId;
  private String goodsSpecVal;
  private Integer goodsSpecSort;
  private Integer goodsSpecStock;
  private BigDecimal goodsSpecPrice;
  private Timestamp createTime;
  private Timestamp updateTime;

}
