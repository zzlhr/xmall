package com.lhrsite.xshop.vo

import com.lhrsite.xshop.po.GoodsAttrVal
import java.sql.Timestamp

class GoodsCategoryAttr {
    val goodsAttrKeyId: Int = 0
    val goodsCategoryId: Int = 0
    var goodsAttrKeyName: String = ""
    var goodsAttrKeySort: Int = 0
    lateinit var createTime: Timestamp
    lateinit var updateTime: Timestamp
    var vals: List<GoodsAttrVal> = ArrayList()

    override fun toString(): String {
        return "GoodsCategoryAttr(goodsAttrKeyId=$goodsAttrKeyId, goodsCategoryId=$goodsCategoryId, goodsAttrKeyName='$goodsAttrKeyName', goodsAttrKeySort=$goodsAttrKeySort, createTime=$createTime, updateTime=$updateTime, vals=$vals)"
    }


}