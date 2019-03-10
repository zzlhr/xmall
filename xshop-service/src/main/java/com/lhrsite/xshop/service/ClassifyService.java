package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.vo.ClassifyVO;
import com.lhrsite.xshop.vo.NewClassify;

import java.util.List;

public interface ClassifyService {

    List<ClassifyVO> getClassifyTree();

    List<ClassifyVO> getFClassify();

    Classify add(Classify classify) throws XShopException;

    Classify update(Classify classify);

    Classify findById(Integer clId) throws XShopException;


    void del(Integer clId, Integer clFid) throws XShopException;


    /**
     * 价格区间列表
     *
     * @param fid 欲查询的价格价格区间父级id
     * @return 价格区间列表
     */
    List<NewClassify> getClassifyPriceRange(Integer fid);

}
