package com.lhrsite.xshop.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 供货商(StSupplier)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
public class SupplierVO implements Serializable {

    //供货商id
    private Integer supplierId;
    //供货商名称
    private String supplierName;
    //供货商联系人
    private String supplierLinkMan;
    //供货商联系电话
    private String supplierLinkTel;
    //状态，0启用，1禁用
    private Integer supplierStatus;
    //删除否，0未删除，1删除
    private Integer supplierDel;
    //创建时间
    private Date createTime;
    //更新人
    private Integer updateUser;
    private String updateUserName;
    //更新时间
    private Date updateTime;


}