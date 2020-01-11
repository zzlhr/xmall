package com.lhrsite.xshop.controller;

import com.lhrsite.xshop.po.GoodsCategory;
import com.lhrsite.xshop.po.GoodsMaster;
import com.lhrsite.xshop.service.GoodsService;
import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/getGoodsCategory")
    public ResultVO getGoodsCategory(Integer fid, Integer status) {
        return ResultVO.create(goodsService.getGoodsCategoryTree(fid, status));
    }

    @PostMapping("/saveGoodsCategory")
    public ResultVO saveGoodsCategory(GoodsCategory goodsCategory) {
        return ResultVO.create(goodsService.saveGoodsCategory(goodsCategory));
    }

    @PostMapping("/getGoodsList")
    public ResultVO getGoodsList(String goodsKeyword, Integer goodsCategoryId,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResultVO.create(goodsService.getGoodsList(goodsKeyword, goodsCategoryId, page, pageSize));
    }

    @PostMapping("/saveGoods")
    public ResultVO saveGoods(GoodsMaster goods) {
        return ResultVO.create(goodsService.saveGoods(goods));
    }

}
