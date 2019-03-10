package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.vo.AddressVO;
import com.lhrsite.xshop.po.Address;
import com.lhrsite.xshop.po.QAddress;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.AddressMapper;
import com.lhrsite.xshop.repository.AddressRepository;
import com.lhrsite.xshop.service.AddressService;
import com.lhrsite.xshop.service.UserService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
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
public class AddressServiceImpl extends BaseServiceImpl implements AddressService {

    private final UserService userService;

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryFactory;

    public AddressServiceImpl(EntityManager entityManager,
                              UserService userService,
                              AddressRepository addressRepository, AddressMapper addressMapper) {
        super(entityManager);
        this.userService = userService;
        this.addressRepository = addressRepository;
        this.entityManager = entityManager;
        this.addressMapper = addressMapper;
        queryFactory = getQueryFactory();

    }

    @Override
    public Address add(Address address, String token) throws XShopException, ConstraintViolationException {
        User user = userService.tokenGetUser(token);
        address.setUid(user.getUid());
        if (this.getAddress(token).size() == 0) {
            address.setDefaultStatus(1);
        } else {
            address.setDefaultStatus(0);
        }
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
        // 取消该用户现有默认地址
        QAddress qAddress = QAddress.address;
        List<Address> addresses =
                queryFactory.selectFrom(qAddress)
                        .where(qAddress.uid.eq(user.getUid())
                                .and(qAddress.defaultStatus.eq(1)))
                        .fetch();

        List<Address> addressesUpDefault = new ArrayList<>();
        addresses.forEach(address -> {
            address.setDefaultStatus(0);
            addressesUpDefault.add(address);
        });
        if (addressesUpDefault.size() > 0) {
            addressRepository.saveAll(addressesUpDefault);
        }

        // 修改欲要修改的地址
        Optional<Address> addressOptional = addressRepository.findById(addrId);
        if (addressOptional.isPresent()) {
            throw new XShopException(ErrEumn.ADDRESS_NOT_EXIST);
        }
        Address address = addressOptional.get();
        address.setDefaultStatus(1);
        addressRepository.save(address);
        return address;
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
        if (addressOptional.isPresent()) {
            throw new XShopException(ErrEumn.ADDRESS_NOT_EXIST);
        }
        if (!user.getUid().equals(addressOptional.get().getUid())) {
            throw new XShopException(ErrEumn.ONLY_DELETE_YOUERSELF_ADDRESS);
        }
        addressRepository.deleteById(addrId);
    }

    @Override
    public List<AddressVO> getAddress(String token) throws XShopException {
        User user = userService.tokenGetUser(token);
        return addressMapper.getAddress(user.getUid());
    }

    @Override
    public Address getDefaultAddress(String token) throws XShopException {

        User user = userService.tokenGetUser(token);
        QAddress qAddress = QAddress.address;

        return queryFactory.selectFrom(qAddress)
                .where(
                        qAddress.uid.eq(user.getUid())
                                .and(qAddress.defaultStatus.eq(1))
                )
                .fetchOne();
    }

    @Override
    public Address getAddressById(Integer addressId) throws XShopException {
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        return addressOptional.orElseThrow(() -> new XShopException(ErrEumn.ADDRESS_NOT_EXIST));
    }
}