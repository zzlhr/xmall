package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private ResultVO resultVO;

    @Autowired
    public FavoriteController() {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    /**
     * 添加收藏
     *
     * @param goodsId 商品id
     * @param token   用户token
     * @return resultVO
     */
    @PostMapping("/addFavorite")
    public ResultVO addFavorite(String goodsId, String token) {
        return resultVO;
    }

    /**
     * 删除收藏
     *
     * @param goodsId 商品id
     * @param token   用户令牌
     * @return 结果vo resultVO
     */
    @PostMapping("/delFavorite")
    public ResultVO delFavorite(String goodsId, String token) {
        return resultVO;
    }


    /**
     * 获取收藏列表
     *
     * @param token 用户令牌
     * @return 收藏列表
     */
    @PostMapping("/favoriteList")
    public ResultVO favoriteList(String token) {
        return resultVO;
    }


}
