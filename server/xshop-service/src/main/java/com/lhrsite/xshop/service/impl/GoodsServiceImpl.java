package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.mapper.GoodsCategoryMapper;
import com.lhrsite.xshop.mapper.GoodsMapper;
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
    private final GoodsMapper goodsMapper;
    @Autowired
    public GoodsServiceImpl(GoodsCategoryMapper goodsCategoryMapper, GoodsCategoryRepository goodsCategoryRepository, GoodsMapper goodsMapper) {
        this.goodsCategoryMapper = goodsCategoryMapper;
        this.goodsCategoryRepository = goodsCategoryRepository;
        this.goodsMapper = goodsMapper;
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
    public PageVO<GoodsListVO> getGoodsList(String goodsKeyword, Integer goodsCategoryId,
                                            Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Page<GoodsListVO> goodsListVOPage = goodsMapper.getGoodsList(goodsKeyword, goodsCategoryId);
        PageInfo<GoodsListVO> pageInfo = new PageInfo<>(goodsListVOPage);
        PageVO<GoodsListVO> pageVO = new PageVO<>();
        pageVO.init(pageInfo, page);
        return pageVO;
    }
}
