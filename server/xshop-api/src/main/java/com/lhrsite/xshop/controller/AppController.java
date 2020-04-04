package com.lhrsite.xshop.controller;



import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.po.App;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.AppService;
import com.lhrsite.xshop.core.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/app")
@Slf4j
public class AppController {

    private final AppService appService;
    private ResultVO resultVO;
    @Value("${app.upload.home.pictures}")
    private String picturesUploadDir;
    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    @GetMapping("/picture")
    public ResultVO getPicture(){
        resultVO.setData(appService.getPicture());
        return resultVO;
    }

    @GetMapping("/link")
    public ResultVO getLink(){
        resultVO.setData(appService.getLink());
        return resultVO;
    }
    @PostMapping("/save")
    public ResultVO edit(String link, String picture, String token) throws XShopException {
        App app = new App();
        app.setId(1);
        app.setLink(link);
        app.setPicture(picture);
        appService.edit(app, token);
        return resultVO;
    }

    @RequestMapping(value = "/picturesUpload", method = RequestMethod.POST)
    public ResultVO picturesUpload(@RequestParam(value = "file") MultipartFile file)
            throws RuntimeException, XShopException {
        resultVO.setData(HttpUtil.saveFile(picturesUploadDir, file));
        return resultVO;
    }

    @RequestMapping(value = "/pictures/{fileName}", method = RequestMethod.GET)
    public void pictures(@PathVariable(name = "fileName") String fileName, HttpServletResponse response)
            throws RuntimeException, XShopException {
        HttpUtil.writeImage(response, fileName, picturesUploadDir);
    }









}
