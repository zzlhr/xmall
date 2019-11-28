package com.lhrsite.xshop.webapi.controller;

import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city")
@Slf4j
public class CityController {

    private final CityService cityService;
    private ResultVO resultVO = new ResultVO();

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);

    }

    @PostMapping("/getProvince")
    public ResultVO getProvince() {
        resultVO.setData(cityService.getProvince());
        log.info("【获取省份】result={}", resultVO);
        return resultVO;
    }

    @PostMapping("/getCity")
    public ResultVO getCity(String provinceId) {
        resultVO.setData(cityService.getCity(provinceId));
        return resultVO;
    }

    @PostMapping("/getCountry")
    public ResultVO getCountry(String cityId) {
        resultVO.setData(cityService.getCountry(cityId));
        return resultVO;
    }

    @PostMapping("/getTown")
    public ResultVO getTown(String countryId) {
        resultVO.setData(cityService.getTown(countryId));
        return resultVO;
    }

    @GetMapping("/getCityPicker")
    public ResultVO getCityPicker() {
        resultVO.setData(cityService.getCityPicker());
        return resultVO;
    }

}
