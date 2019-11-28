package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.BuyCarService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyCar")
public class BuyCarController {

    private final BuyCarService buyCarService;
    private ResultVO resultVO;
    @Autowired
    public BuyCarController(BuyCarService buyCarService) {
        this.buyCarService = buyCarService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    @RequestMapping("/add")
    public ResultVO add(String token, String goodsId, Integer number) throws XShopException {
        resultVO.setData(buyCarService.addBuyCar(token, goodsId, 1, number));
        return resultVO;
    }

    @RequestMapping("/getBuyCar")
    public ResultVO add(String token) throws XShopException {
        if (token == null || "".equals(token)|| "undefined".equals(token)){
            resultVO.setData(new JSONArray());
        }else {
            resultVO.setData(buyCarService.getBuyCar(token));
        }
        return resultVO;
    }

    @RequestMapping("/minusBuyCar")
    public ResultVO minusBuyCar(String token, String goodsId, Integer number) throws XShopException {
        resultVO.setData(buyCarService.minusBuyCar(token, goodsId,number));
        return resultVO;
    }

    @RequestMapping("/deleteBuyCar")
    public ResultVO deleteBuyCar(String buyCarId){
        buyCarService.deleteBuyCar(buyCarId);
        return resultVO;
    }

}
