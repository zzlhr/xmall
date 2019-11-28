package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.po.Enterprise;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.EntepriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/enterprise")
@Slf4j
public class EnterpriseController {

    private final EntepriseService entepriseService;
    private ResultVO resultVO;

    @Autowired
    public EnterpriseController(EntepriseService entepriseService) {
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        this.entepriseService = entepriseService;
    }

    @GetMapping("/getEnterpriseDropDown")
    public ResultVO getEnterpriseDropDown(String keyword){
        log.info("【获取企业下拉】keyword={}", keyword);

        resultVO.setData(entepriseService.getDropDown(keyword));
        return resultVO;
    }


    @PostMapping("/selectEnterprise")
    public ResultVO selectEnterprise(String epName, Integer epType, Integer epStatus,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   HttpServletRequest request){
        log.info("【查询企业】epName={},epType={},epStatus={}", epName, epType, epStatus);
        return ResultVO.create(entepriseService.selectEnterprise(epName, epType, epStatus, page, pageSize));
    }


    @PostMapping("/addEnterprise")
    public ResultVO addEnterprise(Enterprise enterprise) throws XShopException {
        log.info("【添加企业】enterprise={}", enterprise);
        if (enterprise == null){
            throw new XShopException(ErrEumn.ADD_ENTERPRISE_NULL);
        }
        enterprise.setEid(null);
        if (enterprise.getEpShortName() == null ||
                "".equals(enterprise.getEpShortName())){
            throw new XShopException(ErrEumn.ADD_ENTERPRISE_SHORT_NAME_NULL);
        }
        if (enterprise.getEpName() == null || "".equals(enterprise.getEpName())){
            throw new XShopException(ErrEumn.ADD_ENTERPRISE_NAME_NULL);
        }
        if (enterprise.getEpShortName() == null || "".equals(enterprise.getEpStatus())){
            enterprise.setEpStatus(0);
        }

        resultVO.setData(entepriseService.addEnterprise(enterprise));
        return resultVO;

    }

    @PostMapping("/updateEnterprise")
    public ResultVO updateEnterprise(Enterprise enterprise) throws XShopException {
        log.info("【修改企业】enterprise={}", enterprise);
        if (enterprise == null
                ||enterprise.getEid() == null
                || "".equals(enterprise.getEid())
                || enterprise.getEid() == 0){
            throw new XShopException(ErrEumn.ADD_ENTERPRISE_NULL);
        }
        resultVO.setData(entepriseService.updateEnterprise(enterprise));
        return resultVO;

    }

}
