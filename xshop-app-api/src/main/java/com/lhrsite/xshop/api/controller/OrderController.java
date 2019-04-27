package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private ResultVO resultVO;

    @Autowired
    public OrderController() {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    /**
     * 获取订单列表
     *
     * @param token       用户token
     * @param orderStatus 订单状态
     * @return 订单列表
     */
    @PostMapping("/orderList")
    public ResultVO orderList(String token, Integer orderStatus) {
        return resultVO;
    }

    /**
     * 添加订单
     *
     * @param token     用户令牌
     * @param orderDate 订单数量 json形式 包含商品id，商品规格，商品数量，购物券信息等 暂未确定格式
     */
    @PostMapping("/addOrder")
    public ResultVO addOrder(String token, String orderDate) {
        return resultVO;
    }


    /**
     * 删除订单
     * 只有订单完成或者取消时才能
     *
     * @param token   用户令牌
     * @param orderId 订单id
     * @return resultVO
     */
    @PostMapping("/delOrder")
    public ResultVO delOrder(String token, String orderId) {
        return resultVO;
    }

    /**
     * 确认收货
     *
     * @param token   用户令牌
     * @param orderId 订单id
     * @return resultVO
     */
    @PostMapping("/receipt")
    public ResultVO receipt(String token, String orderId) {
        return resultVO;
    }


    /**
     * 退货
     * @param orderId 订单id
     * @param token token
     * @param msg 原因
     * @return resultVO
     */
    @PostMapping("/returnOrder")
    public ResultVO returnOrder(String orderId, String token, String msg){
        return resultVO;
    }

}
