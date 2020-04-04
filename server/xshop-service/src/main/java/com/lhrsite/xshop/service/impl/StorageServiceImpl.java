package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * 仓库表(StStorage)表服务实现类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Service
public class StorageServiceImpl extends BaseServiceImpl implements StorageService {

    @Autowired
    public StorageServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }
}