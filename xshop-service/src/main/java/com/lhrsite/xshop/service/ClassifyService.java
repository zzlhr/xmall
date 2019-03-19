package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.vo.ClassifyPriceRange;
import com.lhrsite.xshop.vo.ClassifyVO;

import java.util.List;

public interface ClassifyService {

    List<ClassifyVO> getClassifyTree(Integer eid);

    /**
     * 通过fid获取菜单
     *
     * @param fid fid
     * @param eid 企业id
     * @return 分类列表
     */
    List<ClassifyVO> getFClassify(Integer fid, Integer eid);

    Classify add(Classify classify) throws XShopException;

    Classify update(Classify classify);

    Classify findById(Integer clId) throws XShopException;


    void del(Integer clId, Integer eid) throws XShopException;


    /**
     * 价格区间列表
     *
     * @param fid 欲查询的价格价格区间父级id
     * @return 价格区间列表
     */
    List<ClassifyPriceRange> getClassifyPriceRange(Integer fid, Integer eid);

}
