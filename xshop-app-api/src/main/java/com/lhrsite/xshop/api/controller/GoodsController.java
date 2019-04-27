package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商品控制器
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private ResultVO resultVO;

    @Autowired
    public GoodsController() {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    /**
     * 获取商品列表
     *
     * @param goodsName     商品名称
     * @param goodsClassify 商品分类
     * @param orderBy       排序 （格式为：『字段名|desc/asc』）
     * @return 商品列表
     */
    @PostMapping("/goodsList")
    public ResultVO goodsList(String goodsName, Integer goodsClassify, String orderBy) {
        return resultVO;
    }


    /**
     * 获取商品详情
     *
     * @param token   用户令牌
     * @param goodsId 商品id
     * @return 商品详情
     */
    @PostMapping("/goodsInfo")
    public ResultVO goodsInfo(String token, String goodsId) {
        return resultVO;
    }

}
