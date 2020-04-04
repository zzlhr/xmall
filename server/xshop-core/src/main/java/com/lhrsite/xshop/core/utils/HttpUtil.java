package com.lhrsite.xshop.core.utils;


import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class HttpUtil {

    /**
     * 通过Response输出图片
     *
     * @param response HttpServletResponse 对象
     * @param fileName 文件名称
     * @param filePath 文件路径
     */
    public static void writeImage(HttpServletResponse response, String fileName, String filePath) {
        System.out.println(filePath + fileName);

        response.setContentType("image/*");
        up(response, fileName, filePath);
    }

    public static void up(HttpServletResponse response, String fileName, String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(filePath + fileName);
            int i = inputStream.available();
            byte[] buff = new byte[i];
            inputStream.read(buff);
            //记得关闭输入流
            inputStream.close();

            response.setContentType("image/*");
            OutputStream out = response.getOutputStream();

            out.write(buff);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单文件上传
     *
     * @param multipartFile 欲上传的文件
     * @param path          上传路径
     * @return 文件名称错误代码
     * @throws XShopException 异常
     */
    public static String saveFile(String path, MultipartFile multipartFile) throws XShopException {

        String newFileName = IdentifyUtil.getIdentify() + "." + MultipartFileUtil.Companion.getFileType(multipartFile);
        System.out.println(newFileName);
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File file = new File(path + newFileName);
        try {
            IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new XShopException(ErrEumn.UPLOAD_ERROR);
        }
        return file.getName();

    }

}
