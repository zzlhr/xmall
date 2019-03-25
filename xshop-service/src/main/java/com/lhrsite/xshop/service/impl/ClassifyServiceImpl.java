package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.core.utils.IdentifyUtil;
import com.lhrsite.xshop.core.utils.MultipartFileUtil;
import com.lhrsite.xshop.mapper.ClassifyMapper;
import com.lhrsite.xshop.po.Classify;
import com.lhrsite.xshop.repository.ClassifyRepository;
import com.lhrsite.xshop.service.ClassifyService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.ClassifyPriceRange;
import com.lhrsite.xshop.vo.ClassifyVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClassifyServiceImpl extends BaseServiceImpl implements ClassifyService {

    private final ClassifyRepository classifyRepository;
    private JPAQueryFactory queryFactory;
    private final ClassifyMapper classifyMapper;

    @Value("${app.upload.classify.pictures}")
    private String uploadPicturePath;
    private final UserService userService;

    @Autowired
    public ClassifyServiceImpl(EntityManager entityManager, ClassifyRepository classifyRepository,
                               ClassifyMapper classifyMapper, UserService userService) {
        super(entityManager);
        this.classifyRepository = classifyRepository;
        this.classifyMapper = classifyMapper;
        this.userService = userService;
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
    public List<ClassifyVO> getClassifyByFid(Integer fid, String token) throws XShopException {
        Integer eid = userService.getUserEnterpriseId(token);
        List<Classify> classifies = classifyMapper.findClassifyByFid(fid, eid);
        return ClassifyVO.init(classifies);
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
    public ClassifyVO add(Classify classify, String token) throws XShopException {
        if (classify.getClName().isEmpty()) {
            throw new XShopException(ErrEumn.CLASS_NAME_CONNOT_NULL);
        }
        Integer eid = userService.getUserEnterpriseId(token);

        Classify existClassify = classifyMapper.findClassifyByClassName(eid, classify.getClName());

        if (existClassify != null) {
            throw new XShopException(ErrEumn.CLASSIFY_IS_EXIST);
        }
        classify.setClSerial(0);
        classify.setEid(eid);
        return ClassifyVO.init(classifyRepository.save(classify));
    }

    @Override
    public ClassifyVO update(Classify classify, String token) throws XShopException {
        Integer eid = userService.getUserEnterpriseId(token);
        classify.setEid(eid);
        classify.setClSerial(0);
        return ClassifyVO.init(classifyRepository.save(classify));
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
    public void del(Integer clId, String token) throws XShopException {
        Integer eid = userService.getUserEnterpriseId(token);


        classifyMapper.delClassify(clId, eid);
        classifyMapper.delFoundNotFidClassify(eid);
    }

    @Override
    public List<ClassifyPriceRange> getClassifyPriceRange(Integer fid, Integer eid) {
        return classifyMapper.getClassifyPriceRange(fid, eid);
    }

    @Override
    public String uploadClassifyPicture(MultipartFile multipartFile) throws IOException {

        String newFileName = IdentifyUtil.getIdentify() + "." + MultipartFileUtil.getFileType(multipartFile);
        System.out.println(newFileName);
        File filePath = new File(uploadPicturePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File file = new File(uploadPicturePath + newFileName);
        IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
        return file.getName();
    }

    @Override
    public void getClassifyPicture(String pictureName, HttpServletResponse response) throws IOException {
        File file = new File(uploadPicturePath + pictureName);
        response.setContentType("image/*");
        response.setCharacterEncoding("utf8");
        IOUtils.copy(new FileInputStream(file), response.getOutputStream());
    }
}
