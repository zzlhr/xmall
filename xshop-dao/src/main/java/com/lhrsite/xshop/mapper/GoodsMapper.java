package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.Goods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface GoodsMapper extends Mapper<Goods> {

    void updateGoodsSalesVolumeAndStock(@Param("goods") List<Goods> goods);


    @Insert("insert into fav_goods values(null,#{userId},#{goodsId})")
    Integer insertFavoGoods(@Param("userId")Integer userId,@Param("goodsId")String goodsId);

    @Select("select * from fav_goods where user_id=#{uid} and goods_id=#{goodsId}")
    List<Map<String,Object>> selectFavo(Integer uid, String goodsId);
    @Delete("delete from fav_goods where user_id=#{uid} and goods_id=#{goodsId}")
    void deleteFavoByUserIdAndGoodsId(Integer uid, String goodsId);
    @Select("select goods_id from fav_goods where user_id=#{uid}")
    List<String> getFavoGoodsIdList(Integer uid);

    @Select("select * from goods where goods_id in (${goodsId})")
     List<Goods> selectGoodsbyIds(@Param("goodsId") String goodsId);

}
