package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Address;
import com.lhrsite.xshop.service.AddressService;
import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    private ResultVO resultVO;
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);
    }

    /**
     * 获取收货地址
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 收货地址集合
     */
    @PostMapping("/getAddress")
    public ResultVO getAddress(String token, Integer page, Integer pageSize) throws XShopException {
        resultVO.setData(addressService.getAddress(token, page, pageSize));
        return resultVO;
    }

    /**
     * 编辑收货地址
     *
     * @param address 收货地址对象
     * @param token   用户令牌
     * @return
     */
    @PostMapping("/editAddress")
    public ResultVO editAddress(Address address, String token) {
        return resultVO;
    }

    /**
     * 编辑收货地址
     *
     * @param address 收货地址对象
     * @param token   用户令牌
     * @return
     */
    @PostMapping("/addAddress")
    public ResultVO addAddress(Address address, String token) {
        return resultVO;
    }

    /**
     * 删除收货地址
     *
     * @param addressId 收货地址id
     * @param token     用户令牌
     * @return
     */
    @PostMapping("/delAddress")
    public ResultVO addAddress(Integer addressId, String token) {
        return resultVO;
    }


}
