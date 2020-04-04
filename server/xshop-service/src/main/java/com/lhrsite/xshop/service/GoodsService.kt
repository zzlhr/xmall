package com.lhrsite.xshop.service

import com.lhrsite.xshop.po.GoodsAttrKey
import com.lhrsite.xshop.po.GoodsCategory
import com.lhrsite.xshop.po.GoodsMaster
import com.lhrsite.xshop.vo.*

interface GoodsService {
    /**
     * 查询分类
     *
     * @param fid    分类父级id，查询顶级菜单可传0
     * @param status 状态，所有状态传null或者不传
     * @return 分类列表
     */
    fun getGoodsCategory(fid: Int?, status: Int?): List<GoodsCategory>

    /**
     * 获取分类树
     */
    fun getGoodsCategoryTree(fid: Int?, status: Int?): List<GoodsCategoryVO>


    /**
     * 保存分类
     *
     * @param goodsCategory 分类对象，如果添加不传id
     * @return 保存后的分类对象
     */
    fun saveGoodsCategory(goodsCategory: GoodsCategory): GoodsCategory

    /**
     * 商品列表
     *
     * @param goodsKeyword    商品关键字
     * @param goodsCategoryId 分类id
     * @param page            页码
     * @param pageSize        每页数量
     * @return 商品列表
     */
    fun getGoodsList(goodsKeyword: String?, goodsCategoryId: Int?, page: Int, pageSize: Int): PageVO<GoodsListVO>

    fun getGoodsDetail(goodsId: String): GoodsDetailVO

    fun saveGoods(goods: GoodsMaster): GoodsMaster
    /**
     * 获取商品分类下的所有attrKey和attrVal
     * @param goodsCategoryId 商品分类代号
     * @return GoodsCategoryAttrs 对象包含该分类下的所有key和val
     */
    fun getGoodsCategoryAttrKeyAndVal(goodsCategoryId: Int): List<GoodsCategoryAttr>
}