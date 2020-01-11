package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.po.City;
import com.lhrsite.xshop.po.Country;
import com.lhrsite.xshop.po.Province;
import com.lhrsite.xshop.po.Town;
import com.lhrsite.xshop.repository.CityRepository;
import com.lhrsite.xshop.repository.CountryRepository;
import com.lhrsite.xshop.repository.ProvinceRepository;
import com.lhrsite.xshop.repository.TownRepository;
import com.lhrsite.xshop.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CityServiceImpl implements CityService {


    private final CityRepository cityRepository;
    private final ProvinceRepository provinceRepository;
    private final CountryRepository countryRepository;
    private final TownRepository townRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, ProvinceRepository provinceRepository
            , CountryRepository countryRepository, TownRepository townRepository) {
        this.cityRepository = cityRepository;
        this.provinceRepository = provinceRepository;
        this.countryRepository = countryRepository;
        this.townRepository = townRepository;
    }

    @Override
    public List<Province> getProvince() {
        List<Province> result = provinceRepository.findAll();
        log.info("【获取省份】result={}", result);
        return result;
    }

    @Override
    public List<City> getCity(String provinceId) {
        return cityRepository.findAllByProvinceId(provinceId);
    }

    @Override
    public List<Country> getCountry(String cityId) {
        return countryRepository.findAllByCityId(cityId);
    }

    @Override
    public List<Town> getTown(String countryId) {
        return townRepository.findAllByCountryId(countryId);
    }

    @Override
    public List<Object> getCityPicker() {
        List<Object> result = new ArrayList<>();
        result.addAll(provinceRepository.findAll());
        result.addAll(cityRepository.findAll());
        result.addAll(countryRepository.findAll());
//        result.addAll(townRepository.findAll());
        return result;
    }
}
