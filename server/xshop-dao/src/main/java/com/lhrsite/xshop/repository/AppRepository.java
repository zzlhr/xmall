package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.App;
import org.springframework.stereotype.Repository;

/**
 * (App)表数据库访问层
 *
 * @author lhr
 * @since 2018-08-22 14:08:32
 */
@Repository
public interface AppRepository extends BaseRepository<App, Integer> {

}