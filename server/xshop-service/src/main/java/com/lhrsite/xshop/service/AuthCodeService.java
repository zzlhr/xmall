package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;

public interface AuthCodeService {


    void sendMessage(String phone, Integer type) throws XShopException;

    void sendUpPwdMessage(String token) throws XShopException;
}
