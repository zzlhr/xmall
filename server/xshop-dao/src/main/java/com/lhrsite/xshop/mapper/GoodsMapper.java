package com.lhrsite.xshop.mapper;

import com.github.pagehelper.Page;
import com.lhrsite.xshop.po.Goods;
import com.lhrsite.xshop.vo.GoodsListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GoodsMapper{

    Page<GoodsListVO> getGoodsList(String title, Integer cid);

}
