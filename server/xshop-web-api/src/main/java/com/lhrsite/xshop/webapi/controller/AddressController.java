package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Address;
import com.lhrsite.xshop.service.AddressService;
import com.lhrsite.xshop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

/**
 * (Address)表控制层
 *
 * @author lhr
 * @since 2018-08-22 14:08:32
 */
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {

    private final AddressService addressService;

    private ResultVO resultVO;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    @PostMapping("/add")
    public ResultVO add(Address address, String token) throws XShopException, ConstraintViolationException {
        resultVO.setData(addressService.add(address, token));
        return resultVO;
    }

    @PostMapping("/updateDefaultAddr")
    public ResultVO updateDefaultAddr(String token, Integer addrId) throws XShopException {
        resultVO.setData(addressService.updateDefaultAddr(token, addrId));
        return resultVO;
    }

    @PostMapping("/updateAddr")
    public ResultVO updateAddr(String token, Address address) throws XShopException {
        resultVO.setData(addressService.updateAddr(token, address));
        return resultVO;
    }

    @PostMapping("/delAddr")
    public ResultVO delAddr(String token, Integer addrId) throws XShopException {
        addressService.delAddr(token, addrId);
        return resultVO;
    }

    @PostMapping("/getAddress")
    public ResultVO getAddress(String token, Integer page, Integer pageSize) throws XShopException {
        resultVO.setData(addressService.getAddress(token, page, pageSize));
        return resultVO;
    }

    @PostMapping("/addressInfo")
    public ResultVO addressInfo(Integer id) throws XShopException {
        resultVO.setData(addressService.getAddressById(id));
        return resultVO;
    }

}