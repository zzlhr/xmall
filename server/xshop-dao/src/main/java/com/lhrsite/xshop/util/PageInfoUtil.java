package com.lhrsite.xshop.util;

import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.vo.PageVO;

public class PageInfoUtil<T> {

    public PageVO<T> init(PageInfo<T> pageInfo) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.init(pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getList());
        return pageVO;
    }


}
