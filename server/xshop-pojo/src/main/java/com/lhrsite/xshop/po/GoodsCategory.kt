package com.lhrsite.xshop.po

import java.sql.Timestamp
import javax.persistence.*

@Entity
open class GoodsCategory(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        open var categoryId: Int?,
        open var categoryFid: Int,
        open var categoryName: String,
        open var categoryStatus: Int,
        open var categorySort: Int,
        @Column(insertable = false, updatable = false)
        open var createTime: Timestamp?,
        @Column(insertable = false, updatable = false)
        open var updateTime: Timestamp?
) {
    constructor() : this(null, 0, "", 0, 0,
            null, null)

    override fun toString(): String {
        return "GoodsCategory(categoryId=$categoryId, categoryFid=$categoryFid, categoryName='$categoryName', categoryStatus=$categoryStatus, categorySort=$categorySort, createTime=$createTime, updateTime=$updateTime)"
    }


}
