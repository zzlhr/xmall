package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.BuyCar;
import com.lhrsite.xshop.service.impl.BuyCarServiceImpl;
import com.lhrsite.xshop.vo.BuyCarVO;
import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private ResultVO resultVO;

    @Autowired
    public ShoppingCartController(BuyCarServiceImpl buyCarService) {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        this.buyCarService = buyCarService;
    }


    private BuyCarServiceImpl buyCarService;


    /**
     * 获取购物车列表
     *
     * @param token 用户令牌
     * @return 购物车列表
     */
    @PostMapping("/shoppingCartList")
    public ResultVO shoppingCartList(String token) throws XShopException {
        List<BuyCarVO> buyCars = buyCarService.getBuyCar(token);

        resultVO.setData(buyCars);

        return resultVO;
    }

    /**
     * 加入购物车
     *
     * @param token      用户token
     * @param goodsId    商品id
     * @param num        数量
     * @param standardId 规格id
     * @return resultVO
     */
    @PostMapping("/addShoppingCart")
    public ResultVO addShoppingCart(String token, String goodsId,
                                    Integer num, Integer standardId) throws XShopException {

        BuyCar buyCar = buyCarService.addBuyCar(token, goodsId, num, standardId);
        resultVO.setData(buyCar);
        return resultVO;
    }

    /**
     * 删除购物车项目
     *
     * @param token          用户令牌
     * @param shoppingCartId 购物车id
     * @return resultVO
     */
    @PostMapping("/delShoppingCart")
    public ResultVO delShoppingCart(String token, String shoppingCartId) {
        return resultVO;
    }

    /**
     * 编辑购物车
     *
     * @param token          用户令牌
     * @param shoppingCartId 购物车项目id
     * @param num            数量
     * @param standardId     规格id
     * @return resultVO
     */
    @PostMapping("/editShoppingCart")
    public ResultVO editShoppingCart(String token, String shoppingCartId,
                                     Integer num, Integer standardId) {
        return resultVO;
    }

}
