package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class GoodsAttrVal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer goodsAttrValId;
  private Integer goodsAttrKeyId;
  private String goodsAttrVal;
  private Integer goodsAttrValSort;
  private Timestamp createTime;
  private Timestamp updateTime;

}
