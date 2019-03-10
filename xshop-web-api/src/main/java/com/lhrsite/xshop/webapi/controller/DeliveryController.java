package com.lhrsite.xshop.webapi.controller;



import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    private ResultVO resultVO = new ResultVO();

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);
    }

    @PostMapping("/list")
    public ResultVO list() {
        resultVO.setData(deliveryService.getDeliveryList());
        return resultVO;
    }

    @PostMapping("/login")
    public ResultVO login(String phone, String password) throws XShopException {
        resultVO.setData(deliveryService.login(phone, password));
        return resultVO;
    }

}
