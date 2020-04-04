package com.lhrsite.xshop.service;


import com.lhrsite.xshop.po.City;
import com.lhrsite.xshop.po.Country;
import com.lhrsite.xshop.po.Province;
import com.lhrsite.xshop.po.Town;

import java.util.List;

/**
 * 城市服务
 */
public interface CityService {

    /**
     * 获取省份
     *
     * @return 省份列表
     */
    List<Province> getProvince();


    /**
     * 获取城市列表
     *
     * @param provinceId 省份id
     * @return 城市列表
     */
    List<City> getCity(String provinceId);

    /**
     * 获取区列表
     *
     * @param cityId 城市id
     * @return 区列表
     */
    List<Country> getCountry(String cityId);

    /**
     * 获取街道列表
     *
     * @param countryId 区id
     * @return 街道列表
     */
    List<Town> getTown(String countryId);


    /**
     * 适配vux数据显示picker组件
     * 参照 <link>https://doc.vux.li/zh-CN/components/popup-picker.html</link>
     * @return 全部城市列表
     */
    List<Object> getCityPicker();

}
