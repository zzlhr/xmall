package com.lhrsite.xshop.core.utils;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUtil {
    public static String getFileType(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null) {
            throw new RuntimeException("文件名为空！");
        }
        String[] fileNameSplit = fileName.split("\\.");
        return fileNameSplit[fileNameSplit.length - 1];
    }
}
