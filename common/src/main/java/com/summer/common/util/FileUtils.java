package com.summer.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/03/23 11:18
 */
public class FileUtils {
    public static void multipartToFile(MultipartFile multipartFile, String targetPath) throws IOException {
        // 获取文件原名称
        String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            throw new IOException("文件名称为空");
        }
        // 创建目标目录（如果不存在）
        File targetDirectory = new File(targetPath);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }
        // 创建File对象，指定文件保存的完整路径
        File destinationFile = new File(targetDirectory, originalFilename);
        // 将MultipartFile的内容转移到File对象
        multipartFile.transferTo(destinationFile);
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
