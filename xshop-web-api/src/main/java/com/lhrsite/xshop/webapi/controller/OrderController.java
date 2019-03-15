package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Order;
import com.lhrsite.xshop.service.OrderService;
import com.lhrsite.xshop.vo.OrderListVO;
import com.lhrsite.xshop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private ResultVO resultVO;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);
    }


    @PostMapping("/createOrder")
    public ResultVO createOrder(String token, Integer addressId, String buyCarIds) throws XShopException {
        resultVO.setData(orderService.createOrder(token, addressId, buyCarIds));
        return resultVO;
    }

    @ResponseBody
    @GetMapping("/print.html")
    public ModelAndView print(String orderId)
            throws XShopException {
        OrderListVO order = orderService.order(orderId);
        System.out.println(order);
        ModelAndView modelAndView = new ModelAndView("print");
        modelAndView.addObject("order", order);
        return modelAndView;
    }


    @PostMapping("/settleAccounts")
    public ResultVO settleAccounts(String token) throws XShopException {
        resultVO.setData(orderService.settleAccounts(token));
        return resultVO;
    }

    @PostMapping("/updateOrder")
    public ResultVO updateOrder(String token, Order order) throws XShopException {
        resultVO.setData(orderService.settleAccounts(token));
        return resultVO;
    }


    @PostMapping("/orderList")
    public ResultVO getOrderList(String token,
                                 @RequestParam(defaultValue = "") String orderId,
                                 @RequestParam(defaultValue = "0") Integer orderStatus,
                                 @RequestParam(defaultValue = "1") long page,
                                 @RequestParam(defaultValue = "5") long pageSize) throws XShopException {
        resultVO.setData(orderService.orderListByUser(token, orderId, orderStatus, page, pageSize));
        return resultVO;
    }

    @PostMapping("/orderListByUserAdmin")
    public ResultVO orderListByUserAdmin(String token,
                                         @RequestParam(defaultValue = "") String orderId,
                                         @RequestParam(defaultValue = "1") long page,
                                         @RequestParam(defaultValue = "5") long pageSize) throws XShopException {
        resultVO.setData(orderService.orderListByUserAdmin(token, orderId, page, pageSize));
        return resultVO;
    }

    @PostMapping("/receipt")
    public ResultVO receipt(String token, String orderId) {
        resultVO.setData(orderService.receipt(token, orderId));
        return resultVO;
    }

    @PostMapping("/list")
    public ResultVO list(String token,
                         @RequestParam(defaultValue = "") String orderId,
                         @RequestParam(defaultValue = "1") long page,
                         @RequestParam(defaultValue = "5") long pageSize) throws XShopException {
        resultVO.setData(orderService.list(token, orderId, page, pageSize));
        return resultVO;
    }

    /**
     * 计算总价
     *
     * @param buyCarIds 购物车ids
     * @return 总价
     */
    @PostMapping("/getTotalPrice")
    public ResultVO getTotalPrice(String buyCarIds) {
        String[] buyCarIdList = buyCarIds.replaceAll(" ", "").split(",");
        Map<String, BigDecimal> totalPrice = new HashMap<>();
        totalPrice.put("totalPrice", orderService.getTotalPrice(buyCarIdList));
        resultVO.setData(totalPrice);
        return resultVO;
    }
}
