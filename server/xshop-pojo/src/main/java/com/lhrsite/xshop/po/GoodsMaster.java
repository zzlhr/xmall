package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class GoodsMaster {

  @Id
  private String goodsId;
  private String goodsTitle;
  private String goodsDescribe;
  private String goodsCover;
  private long goodsStatus;
  private String goodsContent;
  private String goodsBanner;
  private long updateUser;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
  private long categoryId;

}
