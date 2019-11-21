package com.lhrsite.xshop.mapper;

import com.lhrsite.xshop.vo.AddressVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AddressMapper {
    List<AddressVO> getAddresses(Integer uid);
    List<AddressVO> getAddresses(Integer uid, Integer defaultStatus);


    void updateUserAddressNotDefault(Integer uid);

    /**
     * 设置默认收货地址，无需修改其他记录状态，该方法自动修改其他地址defalut_status=0
     * @param uid       用户id
     * @param addrId    地址id
     */
    void setDefaultAddress(Integer uid, Integer addrId);
}
