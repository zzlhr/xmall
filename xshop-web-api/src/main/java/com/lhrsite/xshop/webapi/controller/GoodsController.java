package com.lhrsite.xshop.webapi.controller;


import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.ClassifyService;
import com.lhrsite.xshop.service.GoodsService;
import com.lhrsite.xshop.core.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Value("${app.upload.shop.cover}")
    private String coverUploadDir;
    @Value("${app.upload.shop.pictures}")
    private String picturesUploadDir;
    private final GoodsService goodsService;
    private ResultVO resultVO;

    private final ClassifyService classifyService;

    @Autowired
    public GoodsController(GoodsService goodsService, ClassifyService classifyService) {
        this.goodsService = goodsService;
        this.classifyService = classifyService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
    }

    @PostMapping("/list")
    public ResultVO list(@RequestParam(required = false) String title,
                         @RequestParam(required = false) Integer cid,
                         @RequestParam(required = false) Integer saleType,
                         @RequestParam(defaultValue = "1") long page,
                         @RequestParam(defaultValue = "10") long pageSize) {
        resultVO.setData(goodsService.getGoodsList(title, cid, saleType, page, pageSize));
        return resultVO;
    }

    @PostMapping("/pullDown")
    public ResultVO pullDown(@RequestParam(required = false, defaultValue = "") String title) {
        resultVO.setData(goodsService.pullDown(title));
        return resultVO;
    }


    @PostMapping("/info")
    public ResultVO info(String goodsId) {
        resultVO.setData(goodsService.getById(goodsId));
        return resultVO;
    }

    @RequestMapping("/classifyTree")
    public ResultVO classifyTree(Integer eid) {
        resultVO.setData(classifyService.getClassifyTree(eid));
        return resultVO;
    }

    @GetMapping("/fClassify")
    public ResultVO fClassify(Integer eid) {
        resultVO.setData(classifyService.getFClassify(eid));
        return resultVO;
    }

    @PostMapping("/addClassify")
    public ResultVO add(Classify classify) throws XShopException {
        resultVO.setData(classifyService.add(classify));
        return resultVO;
    }

    @PostMapping("/updateClassify")
    public ResultVO updateClassify(Classify classify) {
        resultVO.setData(classifyService.update(classify));
        return resultVO;
    }

    @PostMapping("/delClassify")
    public ResultVO delClassify(Integer clId, Integer clFid) throws XShopException {
        classifyService.del(clId, clFid);
        return resultVO;
    }

    @PostMapping("/deleteGoods")
    public ResultVO deleteGoods(String goodsId) throws XShopException {
        goodsService.deleteGoods(goodsId);
        return resultVO;
    }

    @PostMapping("/updateGoods")
    public ResultVO updateGoods(Goods goods, @RequestParam(defaultValue = "") String saleMessage) {
        goodsService.updateGoods(goods, saleMessage);
        return resultVO;
    }

    @PostMapping("/add")
    public ResultVO add(Goods goods, @RequestParam(defaultValue = "") String saleMessage) throws XShopException {
        resultVO.setData(goodsService.addGoods(goods, saleMessage));
        return resultVO;
    }


    @GetMapping("/getMessage")
    public ResultVO getMessage(@RequestParam(defaultValue = "0") Integer type) {
        resultVO.setData(goodsService.getMessage(type));
        return resultVO;
    }

    @PostMapping("/getClassifyPriceRange")
    public ResultVO getClassifyPriceRange(Integer fid, Integer eid) {
        resultVO.setData(classifyService.getClassifyPriceRange(fid, eid));
        return resultVO;
    }


    @RequestMapping(value = "/coverUpload", method = RequestMethod.POST)
    public ResultVO coverUpload(@RequestParam(value = "file") MultipartFile file)
            throws RuntimeException, XShopException {
        if (file.isEmpty()) {
            throw new XShopException(ErrEumn.UPLOAD_ERROR_FILE_NULL);
        }
        resultVO.setData(HttpUtil.saveFile(log, coverUploadDir, file));
        return resultVO;

    }

    @RequestMapping(value = "/picturesUpload", method = RequestMethod.POST)
    public ResultVO picturesUpload(@RequestParam(value = "file") MultipartFile file)
            throws RuntimeException, XShopException {
        resultVO.setData(HttpUtil.saveFile(log, picturesUploadDir, file));
        return resultVO;
    }

    @RequestMapping(value = "/pictures/{fileName}", method = RequestMethod.GET)
    public void pictures(@PathVariable(name = "fileName") String fileName, HttpServletResponse response)
            throws RuntimeException, XShopException {
        writeImage(response, fileName, picturesUploadDir);
    }

    @RequestMapping(value = "/cover/{fileName}", method = RequestMethod.GET)
    public void cover(@PathVariable(name = "fileName") String fileName, HttpServletResponse response)
            throws RuntimeException, XShopException {
        writeImage(response, fileName, coverUploadDir);
    }

    private void writeImage(HttpServletResponse response, String fileName, String filePath) {
        log.info("【文件上传】url={}", filePath + fileName);
        response.setContentType("image/*");
        HttpUtil.up(response, fileName, filePath);
    }


}
