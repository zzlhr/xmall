package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.vo.DeliveryVO;
import com.lhrsite.xshop.po.Delivery;
import com.lhrsite.xshop.po.QDelivery;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.DeliveryRepository;
import com.lhrsite.xshop.service.DeliveryService;
import com.lhrsite.xshop.core.utils.EncryptUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeliveryServiceImpl extends BaseServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private JPAQueryFactory queryFactory;

    @Autowired
    public DeliveryServiceImpl(EntityManager entityManager, DeliveryRepository deliveryRepository) {
        super(entityManager);
        this.deliveryRepository = deliveryRepository;
        queryFactory = getQueryFactory();
    }

    @Override
    public List<DeliveryVO> getDeliveryList() {
        QDelivery qDelivery = QDelivery.delivery;

        return queryFactory.select(Projections.bean(
                DeliveryVO.class,
                qDelivery.createTime,
                qDelivery.deliveryName,
                qDelivery.deliveryPhone,
                qDelivery.deliveryStatus,
                qDelivery.did
        )).from(qDelivery)
                .where(qDelivery.deliveryStatus.eq(0)).fetch();
    }

    @Override
    public Delivery addDelivery(Delivery delivery) throws XShopException {

        if (delivery.getDeliveryName() == null || delivery.getDeliveryName().equals("")) {
            throw new XShopException(ErrEumn.DELIVERY_NAME_CONNOT_NULL);
        }

        if (delivery.getDeliveryPassword() == null || delivery.getDeliveryPassword().equals("")) {
            throw new XShopException(ErrEumn.DELIVERY_PASSWORD_CONNOT_NULL);
        }

        if (delivery.getDeliveryPhone() == null || delivery.getDeliveryPhone().equals("")) {
            throw new XShopException(ErrEumn.DELIVERY_PHONE_CONNOT_NULL);
        }

        delivery.setDeliveryStatus(0);

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery updateDelivery(Delivery delivery) throws XShopException {
        if (delivery.getDid() == null || delivery.getDid() == 0) {
            throw new XShopException(ErrEumn.DELIVERY_DID_CONNOT_NULL);
        }
        if (delivery.getDeliveryName() == null || delivery.getDeliveryName().equals("")) {
            throw new XShopException(ErrEumn.DELIVERY_NAME_CONNOT_NULL);
        }

        if (delivery.getDeliveryPassword() == null || delivery.getDeliveryPassword().equals("")) {
            throw new XShopException(ErrEumn.DELIVERY_PASSWORD_CONNOT_NULL);
        }

        if (delivery.getDeliveryPhone() == null || delivery.getDeliveryPhone().equals("")) {
            throw new XShopException(ErrEumn.DELIVERY_PHONE_CONNOT_NULL);
        }
        if (delivery.getDeliveryStatus() == null || delivery.getDeliveryStatus() == 0) {
            delivery.setDeliveryStatus(0);
        }
        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery updatePassword(Integer did, String password) throws XShopException {

        Optional<Delivery> deliveryOptional = deliveryRepository.findById(did);

        Delivery delivery = deliveryOptional.orElseThrow(
                () -> new XShopException(ErrEumn.DELIVERY_NOT_EXIST));

        delivery.setDeliveryPassword(password);

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery updatePhone(Integer did, String phone) throws XShopException {
        Optional<Delivery> deliveryOptional = deliveryRepository.findById(did);

        Delivery delivery = deliveryOptional.orElseThrow(
                () -> new XShopException(ErrEumn.DELIVERY_NOT_EXIST));

        delivery.setDeliveryPhone(phone);

        return deliveryRepository.save(delivery);
    }

    @Override
    public DeliveryVO login(String phone, String password) throws XShopException {

        Delivery delivery = deliveryRepository.findByDeliveryPhone(phone);
        if (delivery == null) {
            throw new XShopException(ErrEumn.DELIVERY_NOT_EXIST);
        }

        if (!delivery.getDeliveryPassword().equals(password)) {
            throw new XShopException(ErrEumn.LOGIN_ERR);
        }

        delivery.setToken(EncryptUtil.encryptPassword(UUID.randomUUID().toString()));

        deliveryRepository.save(delivery);

        DeliveryVO deliveryVO = new DeliveryVO();
        BeanUtils.copyProperties(delivery, deliveryVO);

        return deliveryVO;
    }
}
