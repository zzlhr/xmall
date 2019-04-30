package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.impl.FavoriteServiceImpl;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private ResultVO resultVO;

    private FavoriteServiceImpl favoriteService;
    @Autowired
    public FavoriteController(FavoriteServiceImpl favoriteService) {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        this.favoriteService=favoriteService;
    }

    /**
     * 添加收藏
     *
     * @param goodsId 商品id
     * @param token   用户token
     * @return resultVO
     */
    @PostMapping("/addFavorite")
    public ResultVO addFavorite(String goodsId, String token) throws XShopException {
        Boolean aBoolean = favoriteService.addFavorite(goodsId, token);
        if (aBoolean) {
            resultVO.setMsg("添加收藏成功");
        }else {
            resultVO.setCode(-1);
            resultVO.setMsg("收藏失败,您已经收藏过");
        }

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
    public ResultVO delFavorite(String goodsId, String token) throws XShopException {

        favoriteService.deleteFavo(token,goodsId);

        return resultVO;
    }


    /**
     * 获取收藏列表
     *
     * @param token 用户令牌
     * @return 收藏列表
     */
    @PostMapping("/favoriteList")
    public ResultVO favoriteList(String token, @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize) throws XShopException {
        PageVO pageVO = favoriteService.queryFavoList(token, page, pageSize);
        resultVO.setData(pageVO);

        return resultVO;
    }


}
