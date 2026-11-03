package com.knowhub.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/** 文件相关工具类：存储路径、文件名生成、文件保存。 */
public class FileUtil {

    /** 默认上传目录（相对于当前工作目录，运行时会自动创建）。 */
    private static final String UPLOAD_DIR = "uploads";

    /**
     * 获取上传目录的绝对路径（不存在则自动创建）。
     */
    public static String getUploadPath() {
        String base = System.getProperty("user.dir");
        Path dir = Paths.get(base, UPLOAD_DIR);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("创建上传目录失败: " + dir, e);
        }
        return dir.toAbsolutePath().toString();
    }

    /**
     * 生成一个带原始扩展名的唯一文件名，避免重名覆盖。
     */
    public static String generateFileName(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return UUID.randomUUID() + ".bin";
        }
        int dot = originalName.lastIndexOf('.');
        String ext = (dot >= 0) ? originalName.substring(dot) : "";
        return UUID.randomUUID().toString().replace("-", "") + ext;
    }

    /**
     * 根据原始文件名获取扩展名（小写，含点）。
     */
    public static String getExtension(String fileName) {
        if (fileName == null) return "";
        int dot = fileName.lastIndexOf('.');
        return (dot >= 0) ? fileName.substring(dot).toLowerCase() : "";
    }

    /**
     * 判断扩展名是否在允许列表内。
     */
    public static boolean isAllowedExtension(String fileName, String... allowed) {
        String ext = getExtension(fileName);
        for (String a : allowed) {
            if (ext.equalsIgnoreCase(a)) return true;
        }
        return false;
    }

    /**
     * 把文件保存到磁盘，返回保存后的绝对路径。
     */
    public static String saveFile(String uploadPath, String fileName, byte[] content) throws IOException {
        File dir = new File(uploadPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("无法创建目录: " + uploadPath);
        }
        File target = new File(dir, fileName);
        Files.write(target.toPath(), content);
        return target.getAbsolutePath();
    }
}
