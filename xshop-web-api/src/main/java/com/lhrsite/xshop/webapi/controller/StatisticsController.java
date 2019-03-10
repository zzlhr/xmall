package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final OrderService orderService;

    ResultVO resultVO = new ResultVO();

    @Autowired
    public StatisticsController(OrderService orderService) {
        this.orderService = orderService;
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);
    }

    @GetMapping("/orderStatistics")
    public ResultVO orderStatistics(Integer type) {
        resultVO.setData(orderService.orderStatistics(type));
        return resultVO;
    }


    @GetMapping("/goodsStatistics")
    public ResultVO goodsStatistics() {
        resultVO.setData(orderService.goodsStatistics(0));
        return resultVO;
    }


}
