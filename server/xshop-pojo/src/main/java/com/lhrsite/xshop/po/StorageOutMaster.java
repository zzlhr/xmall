package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 出库单主表(StStorageOutMaster)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
@Entity
@Table(name = "st_storage_out_master")
public class StorageOutMaster implements Serializable {
    @Id
    //出库单id
    private String soId;
    //出库单代号
    private String soCode;
    //订单id
    private String orderId;
    //出库单备注
    private String soRemark;
    //购买用户id
    private Integer userId;
    //出库单状态
    private Integer soStatus;
    //出库时间
    private Timestamp soTime;
    //创建时间
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    //创建用户
    private Integer createUser;
    //更新时间
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;
    //更新用户
    private Integer updateUser;
    //审核状态，0未审核，1审核通过，2审核未通过
    private Integer examineStatus;
    //审核用户
    private Integer examineUser;
    //审核备注
    private String examineRemark;
    //审核时间
    private Timestamp examineTime;


}