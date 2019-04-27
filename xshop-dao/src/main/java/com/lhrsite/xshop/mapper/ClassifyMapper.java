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
     * @return 分类列表
     */
    List<Classify> findAllClassify();

    /**
     * 通过父id查询
     *
     * @param fid 父id
     * @return 分类列表
     */
    List<Classify> findClassifyByFid(Integer fid);

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
     * @param className 分类名称
     * @return 分类
     */
    Classify findClassifyByClassName(String className);

    /**
     * 删除分类，会删除id为传入id以及fid为传入id的数据
     *
     * @param id 分类id
     */
    void delClassify(Integer id);


    /**
     * 删除全部找不到父类的分类
     * <p>
     * 用于删除分类后调用删除子类。
     * </p>
     */
    void delFoundNotFidClassify();

}
