package com.lhrsite.xshop.service;


import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.vo.SupplierVO;
import com.lhrsite.xshop.po.Supplier;
import com.lhrsite.xshop.core.exception.XShopException;

/**
 * 供货商(StSupplier)表服务接口
 *
 * @author lhr
 * @since 2018-09-03 21:56:33
 */
public interface SupplierService {

    /**
     * 添加供货商
     *
     * @param supplier 供货商对象
     * @return 添加后的供货商
     */
    Supplier add(Supplier supplier);

    /**
     * 查询供应商
     *
     * @param supplierName 供应商名称
     * @param status       供应商状态
     * @param rowStatus    删除否
     * @return 供应商分页对象
     */
    PageVO<SupplierVO> list(String supplierName,
                            Integer status,
                            Integer rowStatus,
                            long page,
                            long pageSize);

    /**
     * 删除供应商
     *
     * @param supplierId 供应商id
     */
    void del(Integer supplierId) throws XShopException;

    /**
     * 修改供应商
     *
     * @param supplier 供应商对象
     * @return 修改后的对象
     */
    Supplier update(Supplier supplier);

}