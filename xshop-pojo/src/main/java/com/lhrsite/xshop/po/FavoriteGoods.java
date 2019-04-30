package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class FavoriteGoods implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /* 收藏id */
    private Integer fgId;
    /* 用户id */
    private Integer uid;
    /* 商品id */
    private String goodsId;
    /* 创建时间 */
    private Date createTime;

}
