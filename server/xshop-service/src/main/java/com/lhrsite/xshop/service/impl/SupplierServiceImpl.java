package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.SupplierMapper;
import com.lhrsite.xshop.po.Supplier;
import com.lhrsite.xshop.repository.SupplierRepository;
import com.lhrsite.xshop.service.SupplierService;
import com.lhrsite.xshop.util.PageInfoUtil;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.SupplierVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
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
    private final SupplierMapper supplierMapper;

    @Autowired
    public SupplierServiceImpl(EntityManager entityManager, SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        super(entityManager);
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        queryFactory = getQueryFactory();
    }

    @Override
    public Supplier add(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public PageVO<SupplierVO> list(String supplierName, String supplierTel, Integer rowStatus,
                                   Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<SupplierVO> supplierVOList = supplierMapper.supplierList(supplierName,
                supplierTel, rowStatus, page, pageSize);

        PageInfo<SupplierVO> pageInfo = new PageInfo<>(supplierVOList);

        PageInfoUtil<SupplierVO> pageInfoUtil = new PageInfoUtil<>();

        return pageInfoUtil.init(pageInfo);
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