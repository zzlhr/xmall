package com.lhrsite.xshop.core.utils

import com.lhrsite.xshop.core.exception.ErrEumn
import com.lhrsite.xshop.core.exception.XShopException
import lombok.extern.slf4j.Slf4j
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MultipartFileUtil() {

    companion object {
        fun getFileType(multipartFile: MultipartFile): String {
            val fileName = multipartFile.originalFilename ?: throw RuntimeException("文件名为空！")
            val fileNameSplit = fileName.split("\\.".toRegex()).toTypedArray()
            return fileNameSplit[fileNameSplit.size - 1]
        }

        // 检测目录是否存在不存在进行创建
        private fun checkDirIsExist(path: String) {

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
        }

        fun saveImage(file: MultipartFile, path: String): String {
            checkDirIsExist(path)
            /*
            文件后缀名
             */
            val suffix = when (file.contentType) {
                "image/png" -> ".png"
                "image/jpeg" -> ".jpg"
                else -> throw XShopException(ErrEumn.UPLOAD_FILE_TYPE_ERROR)
            }
            val fileNewName = makeFileCode() + suffix
            val newPath = if ("/" == path[path.lastIndex].toString()) path else "$path/"
            val imgFile = File(newPath + fileNewName)
            FileUtils.copyInputStreamToFile(file.inputStream, imgFile)

            return fileNewName
        }

        fun outPutImage(fileName: String, filePath: String, out: OutputStream) {
            val newFilePath = if (filePath[filePath.lastIndex].toString() == "/") filePath else "$filePath/"
            val file = File(newFilePath + fileName)
            try {
                FileUtils.copyFile(file, out)
            } catch (e: IOException) {
                throw XShopException(ErrEumn.LOAD_FILE_FAILED)
            }
        }

        @Synchronized
        fun makeFileCode(): String {
            var random = "${System.currentTimeMillis()}"
            var i = 0
            val rand = Random()
            while (i < 3) {
                random += rand.nextInt(10)
                i++
            }
            return random
        }
    }


}

