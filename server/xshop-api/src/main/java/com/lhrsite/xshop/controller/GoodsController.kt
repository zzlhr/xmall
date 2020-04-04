package com.lhrsite.xshop.controller

import com.lhrsite.xshop.core.exception.ErrEumn
import com.lhrsite.xshop.core.exception.XShopException
import com.lhrsite.xshop.core.utils.MultipartFileUtil
import com.lhrsite.xshop.core.utils.MultipartFileUtil.Companion.saveImage
import com.lhrsite.xshop.po.GoodsCategory
import com.lhrsite.xshop.po.GoodsMaster
import com.lhrsite.xshop.service.GoodsService
import com.lhrsite.xshop.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/goods")
class GoodsController @Autowired constructor(private val goodsService: GoodsService) {
    @Value("\${app.upload.shop.cover}")
    private lateinit var coverPath: String

    @Value("\${app.upload.shop.pictures}")
    private lateinit var picturePath: String

    @PostMapping("/getGoodsCategory")
    fun getGoodsCategory(fid: Int?, status: Int?): ResultVO {
        return ResultVO.create(goodsService.getGoodsCategoryTree(fid, status))
    }

    @PostMapping("/saveGoodsCategory")
    fun saveGoodsCategory(goodsCategory: GoodsCategory): ResultVO {
        return ResultVO.create(goodsService.saveGoodsCategory(goodsCategory))
    }

    @PostMapping("/getGoodsList")
    fun getGoodsList(goodsKeyword: String?, goodsCategoryId: Int?,
                     @RequestParam(defaultValue = "1") page: Int,
                     @RequestParam(defaultValue = "10") pageSize: Int): ResultVO {
        return ResultVO.create(goodsService.getGoodsList(goodsKeyword, goodsCategoryId, page, pageSize))
    }

    @PostMapping("/saveGoodsMaster")
    fun saveGoods(goods: GoodsMaster): ResultVO {
        return ResultVO.create(goodsService.saveGoods(goods))
    }

    @PostMapping("/getGoodsDetail")
    fun getGoodsDetail(goodsId: String): ResultVO {
        return ResultVO.create(goodsService.getGoodsDetail(goodsId))
    }

    /**
     * 获取商品分类下的所有attrKey和attrVal
     * @param goodsCategoryId 商品分类代号
     * @return GoodsCategoryAttrs对象包含该分类下的所有key和val
     */
    @PostMapping("/getGoodsCategoryAttrs")
    fun getGoodsCategoryAttrs(goodsCategoryId: Int): ResultVO {
        return ResultVO.create(goodsService.getGoodsCategoryAttrKeyAndVal(goodsCategoryId));
    }

    @PostMapping("/uploadGoodsCover")
    @Throws(XShopException::class)
    fun uploadGoodsCover(img: MultipartFile?): ResultVO {
        if (img == null) {
            throw XShopException(ErrEumn.UPLOAD_ERROR_FILE_NULL)
        }
        val fileName = saveImage(img, coverPath)
        val resultMap: MutableMap<String, String> = HashMap()
        resultMap["fileName"] = fileName
        return ResultVO.create(resultMap)
    }


    @GetMapping("/goodsCover/{fileName}")
    fun loadGoodsCover(@PathVariable(name = "fileName") fileName: String, resp: HttpServletResponse) {
        resp.contentType = "image/*"
        MultipartFileUtil.outPutImage(fileName, coverPath, resp.outputStream)
    }

    @PostMapping("/uploadGoodsPicture")
    @Throws(XShopException::class)
    fun uploadGoodsPicture(img: MultipartFile): ResultVO {
        val fileName = saveImage(img, picturePath)
        val resultMap: MutableMap<String, String> = HashMap()
        resultMap["fileName"] = fileName
        return ResultVO.create(resultMap)
    }


    @GetMapping("/goodsPicture/{fileName}")
    fun loadGoodsPicture(@PathVariable(name = "fileName") fileName: String, resp: HttpServletResponse) {
        resp.contentType = "image/*"
        MultipartFileUtil.outPutImage(fileName, picturePath, resp.outputStream)
    }

}