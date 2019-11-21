package com.lhrsite.xshop.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 供货商(StSupplier)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
public class SupplierVO implements Serializable {

    private Integer supplierId;
    /* 供应商名称 */
    private String supplierName;
    /* 供应商联系电话 */
    private String supplierTel;
    /* 供应商地址 */
    private String supplierAddress;
    /* 供应商描述 */
    private String supplierDescribe;
    /* 供应商描述 */
    private String supplierRemark;
    /* 创建人id */
    private Integer createUser;
    private String createUserName;
    /* 修改人id */
    private Integer updateUser;
    private String updateUserName;
    /* 创建时间 */
    private Timestamp createTime;
    /* 更新时间 */
    private Timestamp updateTime;
    /* 所属企业 */
    private Integer enterprise;
    /* 删除否 */
    private Integer rowStatus;


}