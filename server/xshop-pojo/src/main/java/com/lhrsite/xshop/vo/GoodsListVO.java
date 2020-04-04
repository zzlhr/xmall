package com.lhrsite.xshop.vo;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class GoodsListVO {

    private String goodsId;
    private String goodsTitle;
    private String goodsCover;
    private String goodsStatus;

    private Integer updateUser;

    private String updateUserName;

    private Timestamp createTime;
    private Timestamp updateTime;

    private Integer categoryId;

    private String categoryName;

}
