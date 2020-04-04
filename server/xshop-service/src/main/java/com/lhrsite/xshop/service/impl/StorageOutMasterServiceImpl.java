package com.lhrsite.xshop.service.impl;
import com.lhrsite.xshop.service.StorageOutMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * 出库单主表(StStorageOutMaster)表服务实现类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */

@Service
public class StorageOutMasterServiceImpl extends BaseServiceImpl implements StorageOutMasterService {

    @Autowired
    public StorageOutMasterServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }
}