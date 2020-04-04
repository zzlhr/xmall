package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.service.StorageOutDetailsService;

import javax.persistence.EntityManager;

/**
 * 出库单详情(StStorageOutDetails)表服务实现类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
public class StorageOutDetailsServiceImpl extends BaseServiceImpl implements StorageOutDetailsService {

    public StorageOutDetailsServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }
}