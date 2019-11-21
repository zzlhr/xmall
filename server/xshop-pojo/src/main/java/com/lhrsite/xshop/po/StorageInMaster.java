package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 入库单主表(StStorageInMaster)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
@Entity
@Table(name = "st_storage_in_master")
public class StorageInMaster implements Serializable {
    @Id
    //入库单id
    private String siId;
    //入库单号
    private String siCode;
    //供应商id
    private Integer supplierId;
    //审核状态，0未审核，1审核通过，2不通过
    private Integer examineStatus;
    //审核人
    private Integer examineUser;
    //审核意见
    private String examineRemark;
    //审核时间
    private Timestamp examineTime;
    //入库时间
    private Timestamp siTime;
    //入库单状态
    private Integer siStatus;
    //入库单总金额
    private BigDecimal siTotalPrice;
    //入库单总运费
    private BigDecimal siTotalDespatch;
    //入库单商品总价
    private BigDecimal siTotalGoods;
    //删除状态0为删除，1已删除
    private Integer siDel;
    //创建人
    private Integer createUser;
    //创建时间
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    //修改用户
    private Integer updateUser;
    //修改时间
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;


}