package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class GoodsAttrKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer goodsAttrKeyId;
  private Integer goodsCategoryId;
  private String goodsAttrKeyName;
  private Integer goodsAttrKeySort;
  private Timestamp createTime;
  private Timestamp updateTime;


}
