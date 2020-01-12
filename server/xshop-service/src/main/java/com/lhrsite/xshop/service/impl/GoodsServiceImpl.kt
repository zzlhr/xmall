package com.lhrsite.xshop.service.impl

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lhrsite.xshop.mapper.GoodsCategoryMapper
import com.lhrsite.xshop.mapper.GoodsMapper
import com.lhrsite.xshop.po.GoodsCategory
import com.lhrsite.xshop.po.GoodsMaster
import com.lhrsite.xshop.repository.GoodsCategoryRepository
import com.lhrsite.xshop.repository.GoodsMasterRepository
import com.lhrsite.xshop.service.GoodsService
import com.lhrsite.xshop.vo.GoodsCategoryVO
import com.lhrsite.xshop.vo.GoodsListVO
import com.lhrsite.xshop.vo.PageVO
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GoodsServiceImpl
@Autowired constructor(private val goodsCategoryMapper: GoodsCategoryMapper,
                       private val goodsCategoryRepository: GoodsCategoryRepository,
                       private val goodsMapper: GoodsMapper,
                       private val goodsMasterRepository: GoodsMasterRepository
) : GoodsService {


    override fun getGoodsCategory(fid: Int?, status: Int?): List<GoodsCategory> {
        return goodsCategoryMapper.getGoodsCategory(fid, status)
    }

    override fun getGoodsCategoryTree(fid: Int?, status: Int?): List<GoodsCategoryVO> {
        val goodsCategorys: List<GoodsCategory> = this.getGoodsCategory(null, null)
        val goodsCategoryVOS: MutableList<GoodsCategoryVO> = ArrayList()
        for (goodsCategory in goodsCategorys) {
            val goodsCategoryVO = GoodsCategoryVO()
            BeanUtils.copyProperties(goodsCategory, goodsCategoryVO);
            goodsCategoryVOS.add(goodsCategoryVO)
        }
        for (goodsCategoryVO in goodsCategoryVOS) {
            val goodsCategoryChildren = goodsCategoryVOS.filter { it.categoryFid == goodsCategoryVO.categoryId }
            goodsCategoryVO.children = goodsCategoryChildren as MutableList<GoodsCategoryVO>
        }

        return goodsCategoryVOS.filter { it.categoryFid == 0 }
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

    override fun saveGoods(goods: GoodsMaster): GoodsMaster {
        return goodsMasterRepository.save(goods)
    }

}