package com.lhrsite.xshop.api.controller;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.ClassifyService;
import com.lhrsite.xshop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/classify")
public class ClassifyController {

    private final ClassifyService classifyService;

    @Autowired
    public ClassifyController(ClassifyService classifyService) {
        this.classifyService = classifyService;
    }

    /**
     * 获取分类列表
     *
     * @return 分类列表
     */
    @GetMapping("/classifyList")
    public ResultVO classifyList(Integer fid) throws XShopException {
        return ResultVO.create(classifyService.getClassifyByFid(fid));
    }
}
