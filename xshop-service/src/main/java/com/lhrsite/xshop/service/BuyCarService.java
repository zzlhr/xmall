package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.BuyCar;
import com.lhrsite.xshop.vo.BuyCarVO;

import java.util.List;

public interface BuyCarService {


    void deleteBuyCar(String buyCarId);

    List<BuyCarVO> getBuyCar(String token) throws XShopException;

    BuyCar addBuyCar(String token, String goodsId, Integer num, Integer standardId) throws XShopException;

    BuyCar minusBuyCar(String token, String goodsId, Integer number) throws XShopException;

    List<BuyCarVO> getBuyCarByIds(List<String> buyCarIds);

}
