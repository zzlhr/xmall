package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.vo.ClassifyPriceRange;
import com.lhrsite.xshop.vo.ClassifyVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ClassifyService {

    List<ClassifyVO> getClassifyTree() throws XShopException;

    List<ClassifyVO> getClassifyByFid(Integer fid) throws XShopException;

    List<ClassifyVO> getFClassify();

    ClassifyVO add(Classify classify) throws XShopException;

    ClassifyVO update(Classify classify) throws XShopException;

    Classify findById(Integer clId) throws XShopException;


    void del(Integer clId) throws XShopException;


    /**
     * 价格区间列表
     *
     * @param fid 欲查询的价格价格区间父级id
     * @return 价格区间列表
     */
    List<ClassifyPriceRange> getClassifyPriceRange(Integer fid, Integer eid);

    /**
     * 上传分类图片
     *
     * @param multipartFile 图片文件
     * @return 图片名
     */
    String uploadClassifyPicture(MultipartFile multipartFile) throws XShopException;

    /**
     * 获取分类图片文件
     *
     * @param pictureName 图片文件名
     * @param response    response
     */
    void getClassifyPicture(String pictureName, HttpServletResponse response) throws IOException;
}
