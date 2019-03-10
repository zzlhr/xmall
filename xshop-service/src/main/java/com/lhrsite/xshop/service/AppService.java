package com.lhrsite.xshop.service;


import com.lhrsite.xshop.po.App;
import com.lhrsite.xshop.core.exception.XShopException;

/**
 * (App)表服务接口
 *
 * @author lhr
 * @since 2018-08-22 14:08:28
 */
public interface AppService {

    String getPicture();
    String getLink();

    void edit(App app, String token) throws XShopException;

}