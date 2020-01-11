package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.po.FavoriteGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteGoodsMapper {

    /**
     * 添加收藏
     *
     * @param uid     用户id
     * @param goodsId 商品id
     * @return 收藏id
     */
    Integer insertFavoriteGoods(Integer uid, String goodsId);

    /**
     * 获取收藏商品id列表
     *
     * @param uid 用户id
     * @return 收藏商品id列表
     */
    List<String> getFavoriteGoodsIdList(Integer uid);

    /**
     * 通过收藏id删除收藏
     *
     * @param fgId 收藏id
     * @param uid  用户id 用于验证该收藏是否属于该用户
     */
    void deleteFavoriteByFgId(Integer fgId, Integer uid);


    FavoriteGoods getUserFavoriteByGoodsId(Integer uid, String goodsId);

}
