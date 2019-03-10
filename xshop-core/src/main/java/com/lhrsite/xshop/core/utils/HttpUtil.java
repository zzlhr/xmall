package com.lhrsite.xshop.core.utils;


import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

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
     * @param path 上传路径
     * @param file 欲上传的文件
     * @return 文件名称错误代码
     * @throws XShopException 异常
     */
    public static JSONObject saveFile(Logger log, String path, MultipartFile file) throws XShopException {
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = null;
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        fileName = UUID.randomUUID() + suffixName;
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(path + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            int angel = PictureRotateUtil.getRotateAngleForPhoto(path + fileName);
            System.out.println(">>>>>>>>>>>" + angel);
            if (angel > 0) {
                dest = PictureRotateUtil.rotateImage(new File(path + fileName), angel);
            }
            BufferedImage bi = new BufferedImage(50, 20, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(0, 0, 50, 20);
            char[] data = "选货邦".toCharArray();
            g.drawChars(data, 0, data.length, 3, 10);
            Thumbnails.of(dest).scale(1f).outputQuality(0.25f).watermark(Positions.BOTTOM_RIGHT, bi, 0.5f).toFile(dest);
            log.info("上传成功后的文件路径未：" + path + fileName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("file", fileName);
            return jsonObject;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        throw new XShopException(ErrEumn.UPLOAD_ERROR);

    }

}
