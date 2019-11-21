package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出库单详情(StStorageOutDetails)表实体类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Data
@Entity
@Table(name = "st_storage_out_details")
public class StorageOutDetails implements Serializable {
    @Id
    //出库单详情id
    private String sodId;
    //出库主表id
    private String soId;
    //商品id
    private String goodsId;
    //出库商品数量
    private BigDecimal goodsNumber;
    //仓库id
    private Integer storageId;


}