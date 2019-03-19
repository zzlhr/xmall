package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.vo.ClassifyPriceRange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassifyMapper {

    /**
     * 查询所有分类
     *
     * @param eid 企业id
     * @return 分类列表
     */
    List<Classify> findAllClassify(Integer eid);

    /**
     * 通过父id查询
     *
     * @param fid 父id
     * @param eid 企业id
     * @return 分类列表
     */
    List<Classify> findClassifyByFid(Integer fid, Integer eid);

    /**
     * 获取某个分类的价格区间
     *
     * @param eid 企业id
     * @param fid 分类id（父级分类代号）
     * @return 分类价格区间
     */
    List<ClassifyPriceRange> getClassifyPriceRange(Integer fid, Integer eid);


    /**
     * 通过分类名称查询分类
     *
     * @param eid       企业id
     * @param className 分类名称
     * @return 分类
     */
    Classify findClassifyByClassName(Integer eid, String className);

    /**
     * 删除分类，会删除id为传入id以及fid为传入id的数据
     *
     * @param id  分类id
     * @param eid 企业id
     */
    void delClassify(Integer id, Integer eid);


}
