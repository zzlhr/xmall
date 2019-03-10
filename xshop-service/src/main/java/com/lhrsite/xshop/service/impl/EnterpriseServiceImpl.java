package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.vo.EnterpriseDropDownVO;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.po.Enterprise;
import com.lhrsite.xshop.po.QEnterprise;
import com.lhrsite.xshop.repository.EnterpriseRepository;
import com.lhrsite.xshop.service.EntepriseService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Service
public class EnterpriseServiceImpl extends BaseServiceImpl implements EntepriseService {

    private final EnterpriseRepository enterpriseRepository;
    private JPAQueryFactory queryFactory;



    @Autowired
    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository,
                                 EntityManager entityManager) {
        super(entityManager);
        queryFactory = getQueryFactory();
        this.enterpriseRepository = enterpriseRepository;
    }


    @Override
    public List<EnterpriseDropDownVO> getDropDown(String keyword) {
        List<Enterprise> enterprises = enterpriseRepository
                .findEnterprisesByEpNameLike("%" + keyword + "%");

        List<EnterpriseDropDownVO> enterpriseDropDownVOS = new ArrayList<>();

        enterprises.forEach(enterprise -> makeDropDownVO(enterpriseDropDownVOS, enterprise));

        return enterpriseDropDownVOS;
    }

    @Override
    public PageVO<Enterprise> selectEnterprise(String epName,
                                               Integer epType,
                                               Integer epStatus,
                                               long page,
                                               long pageSize) {
        QEnterprise qEnterprise = QEnterprise.enterprise;

        BooleanBuilder builder = new BooleanBuilder();
        if (epName != null && !"".equals(epName)){
            builder.and(qEnterprise.epName.like("%" + epName + "%"));
        }

        if (epType != null && !epType.equals("")){
            builder.and(qEnterprise.epType.eq(epType));
        }

        if (epStatus != null && !epStatus.equals("")){
            builder.and(qEnterprise.epStatus.eq(epStatus));
        }


        JPAQuery<Enterprise> enterpriseJPAQuery = queryFactory.selectFrom(qEnterprise)
                .where(builder)
                .offset((page - 1) * pageSize)
                .limit(pageSize);

        List<Enterprise> enterprises = enterpriseJPAQuery.fetch();

        PageVO<Enterprise> pageVO = new PageVO<>();

        pageVO.init(enterpriseJPAQuery.fetchCount(), page, enterprises);

        return pageVO;
    }

    @Override
    public Enterprise addEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
    }

    @Override
    public Enterprise updateEnterprise(Enterprise enterprise) {

        Enterprise enterpriseOld =
                enterpriseRepository.findById(enterprise.getEid()).get();

        if (enterprise.getEpShortName() != null
                && !"".equals(enterprise.getEpShortName())){
            enterpriseOld.setEpShortName(enterprise.getEpShortName());
        }

        if (enterprise.getEpName() != null
                && !"".equals(enterprise.getEpName())){
            enterpriseOld.setEpName(enterprise.getEpName());
        }

        if (enterprise.getEpLink() != null
                && !"".equals(enterprise.getEpLink())){
            enterpriseOld.setEpLink(enterprise.getEpLink());
        }
        if (enterprise.getEpStatus() != null
                && !"".equals(enterprise.getEpStatus())){
            enterpriseOld.setEpStatus(enterprise.getEpStatus());
        }
        if (enterprise.getEpRemark() != null
                && !"".equals(enterprise.getEpRemark())){
            enterpriseOld.setEpRemark(enterprise.getEpRemark());
        }

        return enterpriseRepository.save(enterpriseOld);
    }


    /**
     * use Enterprise obj make dropdown obj
     * @param list          store the container of the enterprise drop down
     * @param enterprise    one enterprise obj
     */
    private void makeDropDownVO(
            List<EnterpriseDropDownVO> list,
            Enterprise enterprise){
        EnterpriseDropDownVO enterpriseDropDownVO =
                new EnterpriseDropDownVO();
        BeanUtils.copyProperties(enterprise, enterpriseDropDownVO);

        list.add(enterpriseDropDownVO);

    }
}
