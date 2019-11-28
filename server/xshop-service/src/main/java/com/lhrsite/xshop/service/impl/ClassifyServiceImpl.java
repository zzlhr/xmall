package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.core.utils.HttpUtil;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClassifyServiceImpl implements ClassifyService {

    private final ClassifyRepository classifyRepository;
    private JPAQueryFactory queryFactory;
    private final ClassifyMapper classifyMapper;

    @Value("${app.upload.classify.pictures}")
    private String uploadPicturePath;
    private final UserService userService;

    @Autowired
    public ClassifyServiceImpl(ClassifyRepository classifyRepository,
                               ClassifyMapper classifyMapper, UserService userService) {
        this.classifyRepository = classifyRepository;
        this.classifyMapper = classifyMapper;
        this.userService = userService;
    }

    @Override
    public List<ClassifyVO> getClassifyTree() throws XShopException {
        List<Classify> classifies = classifyMapper.findAllClassify();
        return classifyVOSToTree(ClassifyVO.init(classifies));
    }

    /**
     * 把classify对象封装成树结构
     *
     * @param classifyVOS classify对象
     * @return 树
     */
    private List<ClassifyVO> classifyVOSToTree(List<ClassifyVO> classifyVOS) {
        // 因为有三级分类，所以从第二级开始找
        List<ClassifyVO> classifyVOS2 = new ArrayList<>();
        for (int i = 0; i < classifyVOS.size(); i++) {
            ClassifyVO classifyItem = classifyVOS.get(i);
            if (classifyItem.getClGrade() == 1) {
                // 找二级菜单的子菜单
                for (ClassifyVO classifyVO : classifyVOS) {
                    if (classifyVO.getClFid().equals(classifyItem.getClId())) {
                        if (classifyItem.getChildren() == null) {
                            classifyItem.setChildren(new ArrayList<>());
                        }
                        // 设置三级菜单的children为null
                        if (classifyVO.getClGrade() == 2) {
                            classifyVO.setChildren(null);
                        }
                        classifyItem.getChildren().add(classifyVO);
                    }
                }
                classifyVOS2.add(classifyItem);
            }
        }
        List<ClassifyVO> classifyVOS0 = new ArrayList<>();
        // 最后所有二级菜单放入一级菜单children中
        for (ClassifyVO classifyVO : classifyVOS) {
            if (classifyVO.getClGrade() == 0) {
                for (ClassifyVO classifyVO2 : classifyVOS2) {
                    if (classifyVO.getClId().equals(classifyVO2.getClFid())) {
                        classifyVO.getChildren().add(classifyVO2);
                    }
                }
                classifyVOS0.add(classifyVO);
            }
        }

        return classifyVOS0;


    }

    @Override
    public List<ClassifyVO> getClassifyByFid(Integer fid) throws XShopException {
        List<Classify> classifies = classifyMapper.findClassifyByFid(fid);
        return ClassifyVO.init(classifies);
    }

    @Override
    public List<ClassifyVO> getFClassify() {

        List<Classify> classifies = classifyMapper.findClassifyByFid(0);

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
    public ClassifyVO add(Classify classify) throws XShopException {
        if (classify.getClName().isEmpty()) {
            throw new XShopException(ErrEumn.CLASS_NAME_CONNOT_NULL);
        }

        Classify existClassify = classifyMapper.findClassifyByClassName(classify.getClName());

        if (existClassify != null) {
            throw new XShopException(ErrEumn.CLASSIFY_IS_EXIST);
        }
        classify.setClSerial(0);
        classify.setEid(0);
        return ClassifyVO.init(classifyRepository.save(classify));
    }

    @Override
    public ClassifyVO update(Classify classify) {
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
    public void del(Integer clId) throws XShopException {

        classifyMapper.delClassify(clId);
        classifyMapper.delFoundNotFidClassify();
    }

    @Override
    public List<ClassifyPriceRange> getClassifyPriceRange(Integer fid, Integer eid) {
        return classifyMapper.getClassifyPriceRange(fid, eid);
    }

    @Override
    public String uploadClassifyPicture(MultipartFile multipartFile) throws XShopException {
        return HttpUtil.saveFile(uploadPicturePath, multipartFile);
    }

    @Override
    public void getClassifyPicture(String pictureName, HttpServletResponse response) throws IOException {
        File file = new File(uploadPicturePath + pictureName);
        response.setContentType("image/*");
        response.setCharacterEncoding("utf8");
        IOUtils.copy(new FileInputStream(file), response.getOutputStream());
    }
}
