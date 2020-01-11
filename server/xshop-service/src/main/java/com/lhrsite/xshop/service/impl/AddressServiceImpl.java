package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.AddressMapper;
import com.lhrsite.xshop.po.Address;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.repository.AddressRepository;
import com.lhrsite.xshop.service.AddressService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.AddressVO;
import com.lhrsite.xshop.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * (Address)表服务实现类
 *
 * @author lhr
 * @since 2018-08-22 14:08:29
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final UserService userService;

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(UserService userService,
                              AddressRepository addressRepository, AddressMapper addressMapper) {
        this.userService = userService;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;

    }

    @Override
    public Address add(Address address, String token) throws XShopException, ConstraintViolationException {
        User user = userService.tokenGetUser(token);
        address.setUid(user.getUid());
        log.info("【save address】addr={}", address);
        Address address1;
        try {
            address1 = addressRepository.save(address);
        } catch (ConstraintViolationException e) {
            throw e;
        }
        // 如果收货地址为默认地址更新现有所有收货地址
        if (address.getDefaultStatus() == 1) {
            address1 = updateDefaultAddr(token, address1.getId());
        }
        return address1;
    }

    @Override
    public Address updateDefaultAddr(String token, Integer addrId) throws XShopException {
        User user = userService.tokenGetUser(token);
        addressMapper.setDefaultAddress(user.getUid(), addrId);
        return addressRepository.getOne(addrId);
    }

    @Override
    public Address updateAddr(String token, Address address) throws XShopException {

        if (address.getId() == null) {
            throw new XShopException(ErrEumn.ADDRESS_ID_CONNOT_NULL);
        }


        return addressRepository.save(address);
    }

    @Override
    public void delAddr(String token, Integer addrId) throws XShopException {
        User user = userService.tokenGetUser(token);
        Optional<Address> addressOptional = addressRepository.findById(addrId);
        if (!user.getUid().equals(
                addressOptional.orElseThrow(() -> new XShopException(ErrEumn.ADDRESS_NOT_EXIST)).getUid())) {
            throw new XShopException(ErrEumn.ONLY_DELETE_YOUERSELF_ADDRESS);
        }
        addressRepository.deleteById(addrId);
    }

    @Override
    public PageVO<AddressVO> getAddress(String token, Integer page, Integer pageSize) throws XShopException {
        User user = userService.tokenGetUser(token);
        PageHelper.startPage(page, pageSize);
        PageInfo<AddressVO> pageInfo = new PageInfo<>(addressMapper.getAddresses(user.getUid()));
        PageVO<AddressVO> pageVO = new PageVO<>();
        pageVO.setTotalCount(pageInfo.getTotal());
        pageVO.setTotalPage(pageInfo.getPages());
        pageVO.setArr(pageInfo.getList());
        pageVO.setPageSize(pageInfo.getPageSize());
        log.info("【获取收货地址】page={}, pageVO={}", page, pageVO);
        return pageVO;
    }

    @Override
    public List<AddressVO> getDefaultAddress(String token) throws XShopException {
        User user = userService.tokenGetUser(token);
        return addressMapper.getAddresses(user.getUid(), 1);
    }

    @Override
    public Address getAddressById(Integer addressId) throws XShopException {
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        return addressOptional.orElseThrow(() -> new XShopException(ErrEumn.ADDRESS_NOT_EXIST));
    }
}