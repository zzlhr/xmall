package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.service.StorageInDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * 入库单详情(StStorageInDetails)表服务实现类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Service
public class StorageInDetailsServiceImpl extends BaseServiceImpl implements StorageInDetailsService {

    @Autowired
    public StorageInDetailsServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }
}