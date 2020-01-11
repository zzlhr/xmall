package com.lhrsite.xshop.vo

import com.lhrsite.xshop.po.GoodsCategory

class GoodsCategoryVO: GoodsCategory() {

    var children: MutableList<GoodsCategoryVO> = ArrayList()
    override fun toString(): String {
        return "${super.categoryName}:(children=$children)"
    }


}