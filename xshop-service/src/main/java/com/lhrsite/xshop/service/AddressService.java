package com.lhrsite.xshop.service;


import com.lhrsite.xshop.vo.AddressVO;
import com.lhrsite.xshop.po.Address;
import com.lhrsite.xshop.core.exception.XShopException;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * (Address)表服务接口
 *
 * @author lhr
 * @since 2018-08-22 14:08:28
 */
public interface AddressService {

    /**
     * 添加收货地址
     *
     * @param token   用户登录令牌
     * @param address 用户地址
     * @return 添加的收货地址
     */
    Address add(Address address, String token) throws XShopException, ConstraintViolationException;

    /**
     * 更新默认收货地址
     *
     * @param token  用户token
     * @param addrId 欲设为默认地址的地址id
     * @return 设置后的对象
     */
    Address updateDefaultAddr(String token, Integer addrId) throws XShopException;

    /**
     * 更新收货地址
     *
     * @param token   用户令牌
     * @param address 收货地址
     * @return 设置后的对象
     */
    Address updateAddr(String token, Address address) throws XShopException;

    /**
     * 删除收货地址
     *
     * @param token  用户token
     * @param addrId 地址id
     */
    void delAddr(String token, Integer addrId) throws XShopException;

    List<AddressVO> getAddress(String token) throws XShopException;

    Address getDefaultAddress(String token) throws XShopException;

    Address getAddressById(Integer addressId) throws XShopException;


}