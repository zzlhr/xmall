package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.vo.SupplierVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SupplierMapper {

    List<SupplierVO> supplierList(String supplierName, String supplierTel, Integer rowStatus,
                                  long page, long pageSize);

}
