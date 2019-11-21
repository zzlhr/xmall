package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.service.StorageInMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * 入库单主表(StStorageInMaster)表服务实现类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Service
public class StorageInMasterServiceImpl extends BaseServiceImpl implements StorageInMasterService {

    @Autowired
    public StorageInMasterServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }
}