package com.lhrsite.xshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhrsite.xshop.mapper.EnterpriseMapper;
import com.lhrsite.xshop.vo.EnterpriseDropDownVO;
import com.lhrsite.xshop.vo.PageVO;
import com.lhrsite.xshop.po.Enterprise;
import com.lhrsite.xshop.repository.EnterpriseRepository;
import com.lhrsite.xshop.service.EntepriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Service
public class EnterpriseServiceImpl implements EntepriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;


    @Autowired
    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository,EnterpriseMapper enterpriseMapper) {
        this.enterpriseMapper = enterpriseMapper;
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
                                               Integer page,
                                               Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<Enterprise> enterprises = enterpriseMapper.getEnterprises(epName, epType, epStatus);
        PageInfo<Enterprise> pageInfo = new PageInfo<>(enterprises);
        PageVO<Enterprise> pageVO = new PageVO<>();
        pageVO = pageVO.init(pageInfo, page);
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
