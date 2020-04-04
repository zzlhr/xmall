package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class BuyCar {

  @Id
  private String id;
  private Integer userId;
  private String goodsId;
  private Integer number;
  @Column(updatable = false, insertable = false)
  private java.sql.Timestamp createTime;


}
