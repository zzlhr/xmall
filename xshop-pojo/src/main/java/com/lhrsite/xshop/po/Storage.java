package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 仓库表(StStorage)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
@Entity
@Table(name = "st_storage")
public class Storage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //仓库id
    private Integer storageId;
    //仓库名称
    private String storageName;
    //仓库地址
    private String storageAddress;
    //仓库备注
    private String storageRemark;
    //仓库状态，0启用，1禁用
    private Integer storageStatus;
    //删除否，0未删除，1删除
    private Integer storageDel;
    //创建时间
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    //更新时间
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;
    //更新人
    private Integer updateUser;


}