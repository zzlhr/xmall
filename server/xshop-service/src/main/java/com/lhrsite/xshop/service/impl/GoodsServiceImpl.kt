package com.lhrsite.xshop.service.impl

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lhrsite.xshop.mapper.GoodsAttrMapper
import com.lhrsite.xshop.mapper.GoodsCategoryMapper
import com.lhrsite.xshop.mapper.GoodsMapper
import com.lhrsite.xshop.po.GoodsAttrKey
import com.lhrsite.xshop.po.GoodsAttrVal
import com.lhrsite.xshop.po.GoodsCategory
import com.lhrsite.xshop.po.GoodsMaster
import com.lhrsite.xshop.repository.GoodsCategoryRepository
import com.lhrsite.xshop.repository.GoodsMasterRepository
import com.lhrsite.xshop.service.GoodsService
import com.lhrsite.xshop.vo.*
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class GoodsServiceImpl
@Autowired constructor(private val goodsCategoryMapper: GoodsCategoryMapper,
                       private val goodsCategoryRepository: GoodsCategoryRepository,
                       private val goodsMapper: GoodsMapper,
                       private val goodsMasterRepository: GoodsMasterRepository,
                       private val goodsAttrMapper: GoodsAttrMapper
) : GoodsService {


    override fun getGoodsCategory(fid: Int?, status: Int?): List<GoodsCategory> {
        return goodsCategoryMapper.getGoodsCategory(fid, status)
    }

    override fun getGoodsCategoryTree(): List<GoodsCategoryVO> {
        val goodsCategories: List<GoodsCategory> = this.getGoodsCategory(null, null)
        val goodsCategoryVOS: ArrayList<GoodsCategoryVO> = ArrayList()
        val goodsCategoryResult: ArrayList<GoodsCategoryVO> = ArrayList()
        for (goodsCategory in goodsCategories) {
            val goodsCategoryVO = GoodsCategoryVO()
            BeanUtils.copyProperties(goodsCategory, goodsCategoryVO);
            goodsCategoryVOS.add(goodsCategoryVO)
        }
        for (goodsCategoryVO in goodsCategoryVOS) {
            val goodsCategoryChildren = goodsCategoryVOS.filter { it.categoryFid == goodsCategoryVO.categoryId }
            goodsCategoryVO.children = goodsCategoryChildren
        }
        goodsCategoryVOS.map { if (it.categoryFid == 0) goodsCategoryResult.add(it) }

        return goodsCategoryResult
    }

    override fun saveGoodsCategory(goodsCategory: GoodsCategory): GoodsCategory {
        return goodsCategoryRepository.save(goodsCategory)
    }

    override fun getGoodsList(goodsKeyword: String?, goodsCategoryId: Int?, page: Int, pageSize: Int): PageVO<GoodsListVO> {
        PageHelper.startPage<Any>(page, pageSize)
        val goodsListVOPage = goodsMapper.getGoodsList(goodsKeyword, goodsCategoryId)
        val pageInfo = PageInfo(goodsListVOPage)
        val pageVO = PageVO<GoodsListVO>()
        pageVO.init(pageInfo, page)
        return pageVO
    }

    override fun getGoodsDetail(goodsId: String): GoodsDetailVO {
        return goodsMapper.getGoodsDetail(goodsId)
    }

    override fun saveGoods(goods: GoodsMaster): GoodsMaster {
        if (goods.goodsId == null || "".equals(goods.goodsId)) {
            goods.goodsId = UUID.randomUUID().toString()
        }
        //TODO: 默认更新人,后续权限拦截后通过token获取更新用户
        if (goods.updateUser == null){
            goods.updateUser = 1
        }
        return goodsMasterRepository.save(goods)
    }

    override fun getGoodsCategoryAttrKeyAndVal(goodsCategoryId: Int): List<GoodsCategoryAttr> {
        val keys: List<GoodsAttrKey> = goodsAttrMapper.getGoodsAttrKeys(goodsCategoryId)
        val vals: List<GoodsAttrVal> = goodsAttrMapper.getGoodsAttrVals(goodsCategoryId)

        val goodsCategoryAttrs = ArrayList<GoodsCategoryAttr>()
        for (key in keys) {
            val goodsCategoryAttr: GoodsCategoryAttr = GoodsCategoryAttr()
            BeanUtils.copyProperties(key, goodsCategoryAttr)
            goodsCategoryAttr.vals = vals.filter { it.goodsAttrKeyId == key.goodsAttrKeyId }
            goodsCategoryAttrs.add(goodsCategoryAttr)
        }
        return goodsCategoryAttrs
    }

}