package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.mapper.GoodsCategoryMapper;
import com.lhrsite.xshop.po.GoodsCategory;
import com.lhrsite.xshop.repository.GoodsCategoryRepository;
import com.lhrsite.xshop.service.GoodsService;
import com.lhrsite.xshop.vo.GoodsListVO;
import com.lhrsite.xshop.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {


    private final GoodsCategoryMapper goodsCategoryMapper;
    private final GoodsCategoryRepository goodsCategoryRepository;
    @Autowired
    public GoodsServiceImpl(GoodsCategoryMapper goodsCategoryMapper, GoodsCategoryRepository goodsCategoryRepository) {
        this.goodsCategoryMapper = goodsCategoryMapper;
        this.goodsCategoryRepository = goodsCategoryRepository;
    }


    @Override
    public List<GoodsCategory> getGoodsCategory(Integer fid, Integer status) {
        return goodsCategoryMapper.getGoodsCategory(fid, status);
    }

    @Override
    public GoodsCategory saveGoodsCategory(GoodsCategory goodsCategory) {
        return goodsCategoryRepository.save(goodsCategory);
    }

    @Override
    public PageVO<GoodsListVO> getGoodsList(String goodsKeyword, Integer goodsCategoryId, Integer page, Integer pageSize) {
        return null;
    }
}
