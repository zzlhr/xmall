package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.mapper.ClassifyMapper;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.po.QClassify;
import com.lhrsite.xshop.repository.ClassifyRepository;
import com.lhrsite.xshop.service.ClassifyService;
import com.lhrsite.xshop.vo.ClassifyPriceRange;
import com.lhrsite.xshop.vo.ClassifyVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassifyServiceImpl extends BaseServiceImpl implements ClassifyService {

    private final ClassifyRepository classifyRepository;
    private JPAQueryFactory queryFactory;
    private final ClassifyMapper classifyMapper;

    @Autowired
    public ClassifyServiceImpl(EntityManager entityManager, ClassifyRepository classifyRepository,
                               ClassifyMapper classifyMapper) {
        super(entityManager);
        this.classifyRepository = classifyRepository;
        this.classifyMapper = classifyMapper;
        queryFactory = getQueryFactory();
    }

    @Override
    public List<ClassifyVO> getClassifyTree(Integer eid) {

        List<Classify> classifies = classifyMapper.findAllClassify(eid);

        List<ClassifyVO> classifyVOS = ClassifyVO.init(classifies);

        List<ClassifyVO> resultVO = new ArrayList<>();

        classifyToVO(classifyVOS, resultVO);

        return resultVO;
    }

    @Override
    public List<ClassifyVO> getFClassify(Integer eid) {

        List<Classify> classifies = classifyMapper.findClassifyByFid(0, eid);

        List<ClassifyVO> classifyVOS = ClassifyVO.init(classifies);

        List<ClassifyVO> resultVO = new ArrayList<>();

        classifyToVO(classifyVOS, resultVO);
        return resultVO;
    }

    private void classifyToVO(List<ClassifyVO> classifyVOS, List<ClassifyVO> resultVO) {
        classifyVOS.forEach(classifyVO -> {
            if (classifyVO.getClGrade() == 0) {
                resultVO.add(classifyVO);
            } else {
                classifyVOS.forEach(classifyVO1 -> {
                    if (classifyVO1.getClId().equals(classifyVO.getClFid())) {
                        classifyVO1.getChildren().add(classifyVO);
                    }
                });
            }

        });
    }

    @Override
    public Classify add(Classify classify) throws XShopException {
        // 判断是否存在
        QClassify qClassify = QClassify.classify;

        Classify existClassify = queryFactory.selectFrom(qClassify)
                .where(qClassify.clName.eq(classify.getClName())).fetchOne();

        if (existClassify != null) {
            throw new XShopException(ErrEumn.CLASSIFY_IS_EXIST);
        }
        classify.setClSerial(0);
        return classifyRepository.save(classify);
    }

    @Override
    public Classify update(Classify classify) {
        return classifyRepository.save(classify);
    }


    @Override
    public Classify findById(Integer clId) throws XShopException {
        Optional<Classify> classifyOptional = classifyRepository.findById(clId);
        if (!classifyOptional.isPresent()) {
            throw new XShopException(ErrEumn.CLASSIFY_IS_NOTEXIST);
        }
        return classifyOptional.get();
    }

    private List<Classify> findByFid(Integer fid) throws XShopException {
        return classifyRepository.findAllByClFid(fid);
    }

    @Override
    public void del(Integer clId, Integer eid) throws XShopException {
        classifyMapper.delClassify(clId, eid);
    }

    @Override
    public List<ClassifyPriceRange> getClassifyPriceRange(Integer fid, Integer eid) {
        return classifyMapper.getClassifyPriceRange(fid, eid);
    }
}
