package com.lhrsite.xshop.po;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * 供应商表
 *
 * @author haoranliu
 */
@Entity
@Data
public class Supplier implements Serializable{

    /* 供应商id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    /* 修改人id */
    private Integer updateUser;
    /* 创建时间 */
    private Date createTime;
    /* 更新时间 */
    private Date updateTime;
    /* 所属企业 */
    private Integer enterprise;
    /* 删除否 */
    private Integer rowStatus;

}
