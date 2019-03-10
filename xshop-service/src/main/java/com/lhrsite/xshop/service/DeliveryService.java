package com.lhrsite.xshop.service;

import com.lhrsite.xshop.vo.DeliveryVO;
import com.lhrsite.xshop.po.Delivery;
import com.lhrsite.xshop.core.exception.XShopException;

import java.util.List;

public interface DeliveryService {


    List<DeliveryVO> getDeliveryList();

    Delivery addDelivery(Delivery delivery) throws XShopException;

    Delivery updateDelivery(Delivery delivery) throws XShopException;

    Delivery updatePassword(Integer did, String password) throws XShopException;

    Delivery updatePhone(Integer did, String phone) throws XShopException;

    DeliveryVO login(String phone, String password) throws XShopException;

}
