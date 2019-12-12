package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class GoodsCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer categoryId;
  private Integer categoryFid;
  private String categoryName;
  private Integer categoryStatus;
  private Integer categorySort;
  private Timestamp createTime;
  private Timestamp updateTime;


}
