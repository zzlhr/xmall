package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class FavoriteGoods implements Serializable {

    /* 收藏id */
    private Integer fgId;
    /* 用户id */
    private Integer uid;
    /* 商品id */
    private String goodsId;
    /* 创建时间 */
    private Date createTime;

}
