package com.lhrsite.xshop.vo;

import com.github.pagehelper.PageInfo;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    // 总页数
    private long totalPage;
    // 每页数量
    private long pageSize = 10;
    // 当前页码
    private long currentPage;
    // 总条数
    private long totalCount;
    // 本页数量
    private long currentNumber;

    private List<T> arr;

    public void init(long totalCount, long currentPage, List<T> arr){
        this.currentPage = currentPage;
        this.arr = arr;
        this.totalCount = totalCount;
        this.totalPage = totalCount / pageSize;
        this.currentNumber = arr.size();
        if (totalCount % pageSize > 0){
            this.totalPage += 1;
        }
    }

    public void init(JPAQuery<T> tjpaQuery, long currentPage){
        arr = tjpaQuery.fetch();
        totalCount = tjpaQuery.fetchCount();
        this.currentPage = currentPage;
        this.totalPage = totalCount / pageSize;
        this.currentNumber = arr.size();
        if (totalCount % pageSize > 0){
            this.totalPage += 1;
        }
    }

    public PageVO<T> init(PageInfo<T> pageInfo){
        this.setTotalCount(pageInfo.getTotal());
        this.setTotalPage(pageInfo.getPages());
        this.setArr(pageInfo.getList());
        this.setPageSize(pageInfo.getPageSize());
        return this;
    }


}
