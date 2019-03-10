package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.SupplierVO;
import com.lhrsite.xshop.po.QSupplier;
import com.lhrsite.xshop.po.QUser;
import com.lhrsite.xshop.po.Supplier;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.SupplierRepository;
import com.lhrsite.xshop.service.SupplierService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * 供货商(StSupplier)表服务实现类
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
@Service
public class SupplierServiceImpl extends BaseServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private JPAQueryFactory queryFactory;

    @Autowired
    public SupplierServiceImpl(EntityManager entityManager, SupplierRepository supplierRepository) {
        super(entityManager);
        this.supplierRepository = supplierRepository;
        queryFactory = getQueryFactory();
    }

    @Override
    public Supplier add(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public PageVO<SupplierVO> list(String supplierName, Integer status,
                                   Integer rowStatus, long page, long pageSize) {

        QSupplier qSupplier = QSupplier.supplier;
        QUser qUser = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qSupplier.rowStatus.eq(rowStatus));

        PageVO<SupplierVO> supplierVOPageVO = new PageVO<>();

        supplierVOPageVO.init(queryFactory.select(
                Projections.bean(
                        SupplierVO.class,
                        qSupplier.createTime,
                        qSupplier.rowStatus,
                        qSupplier.supplierId,
                        qSupplier.supplierAddress,
                        qSupplier.supplierDescribe,
                        qSupplier.supplierRemark,
                        qSupplier.supplierName,
                        qSupplier.createUser,
                        qSupplier.createTime,
                        qSupplier.updateTime,
                        qSupplier.updateUser,
                        qUser.username.as("updateUserName")
                )
        ).from(qSupplier)
                .leftJoin(qUser).on(qSupplier.updateUser.eq(qUser.uid))
                .where(builder)
                .offset((page - 1) * pageSize)
                .limit(pageSize), page);

        return supplierVOPageVO;
    }

    @Override
    public void del(Integer supplierId) throws XShopException {
        Optional<Supplier> supplierOptional =
                supplierRepository.findById(supplierId);
        if (supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();
            supplier.setRowStatus(1);
            supplierRepository.save(supplier);
        }
        throw new XShopException(ErrEumn.SUPPLIER_NOT_EXIST);
    }

    @Override
    public Supplier update(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
}