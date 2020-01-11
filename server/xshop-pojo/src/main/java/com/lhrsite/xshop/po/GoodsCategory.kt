package com.lhrsite.xshop.po

import java.sql.Timestamp
import javax.persistence.*

@Entity
open class GoodsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var categoryId: Int? = null
    var categoryFid: Int? = null
    var categoryName: String? = null
    var categoryStatus: Int? = null
    var categorySort: Int? = null
    @Column(insertable = false, updatable = false)
    var createTime: Timestamp? = null
    @Column(insertable = false, updatable = false)
    var updateTime: Timestamp? = null

    override fun toString(): String {
        return "GoodsCategory(categoryId=$categoryId, categoryFid=$categoryFid, categoryName=$categoryName, categoryStatus=$categoryStatus, categorySort=$categorySort, createTime=$createTime, updateTime=$updateTime)"
    }


}