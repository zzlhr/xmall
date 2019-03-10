package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.vo.ClassifyVO;
import com.lhrsite.xshop.vo.NewClassify;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.po.QClassify;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.ClassifyRepository;
import com.lhrsite.xshop.service.ClassifyService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassifyServiceImpl extends BaseServiceImpl implements ClassifyService {

    private final ClassifyRepository classifyRepository;
    private JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassifyServiceImpl(EntityManager entityManager, ClassifyRepository classifyRepository, JdbcTemplate jdbcTemplate) {
        super(entityManager);
        this.classifyRepository = classifyRepository;
        this.jdbcTemplate = jdbcTemplate;
        queryFactory = getQueryFactory();
    }

    @Override
    public List<ClassifyVO> getClassifyTree() {

        QClassify qClassify = QClassify.classify;

        List<Classify> classifies = queryFactory.selectFrom(qClassify)
                .orderBy(qClassify.clGrade.desc())
                .orderBy(qClassify.clSerial.asc())
                .where(qClassify.clDel.eq(0))
                .fetch();

        List<ClassifyVO> classifyVOS = ClassifyVO.init(classifies);

        List<ClassifyVO> resultVO = new ArrayList<>();

        classifyToVO(classifyVOS, resultVO);

        return resultVO;
    }

    @Override
    public List<ClassifyVO> getFClassify() {
        QClassify qClassify = QClassify.classify;
        List<Classify> classifies = queryFactory.selectFrom(qClassify)
                .orderBy(qClassify.clGrade.desc())
                .where(qClassify.clDel.eq(0))
                .where(qClassify.clFid.eq(0))
                .fetch();
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
                .where(
                        qClassify.clDel.eq(0)
                                .and(qClassify.clName.eq(classify.getClName()))
                ).fetchOne();
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
    public void del(Integer clId, Integer clFid) throws XShopException {
        if (clId == 0) {
            List<Classify> classifies = findByFid(clFid);

            for (Classify classify : classifies) {
                classify.setClDel(1);
            }
            Classify classify = findById(clFid);
            classify.setClDel(1);
            classifies.add(classify);
            classifyRepository.saveAll(classifies);


        } else {
            Classify classify = findById(clId);
            classify.setClDel(1);
            classifyRepository.save(classify);
        }

    }

    @Override
    public List<NewClassify> getClassifyPriceRange(Integer fid) {
        String sql = "select c.cl_id, c.cl_name, max(g.original_price) max, min(original_price) min from goods g\n" +
                "inner join classify c on g.cl_id=c.cl_id\n" +
                "where g.status=0 and  c.cl_fid=" + fid + "\n" +
                "group by c.cl_name";

        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(NewClassify.class));
    }
}
