package com.knowhub.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/** POI / PDFBox 解析工具：把 docx / pdf 解析成纯文本。 */
public class PoiUtil {

    /**
     * 根据文件名后缀选择解析策略。
     *
     * @param localFilePath 文件在服务器上的绝对路径
     * @return 解析出的纯文本；不支持的类型返回空串
     */
    public static String extractText(String localFilePath) {
        if (localFilePath == null) return "";
        String lower = localFilePath.toLowerCase();
        try {
            if (lower.endsWith(".docx")) {
                return extractDocx(localFilePath);
            } else if (lower.endsWith(".pdf")) {
                return extractPdf(localFilePath);
            } else {
                return "";
            }
        } catch (Exception e) {
            System.err.println("[PoiUtil] 解析失败: " + e.getMessage());
            return "";
        }
    }

    public static String extractDocx(String filePath) throws IOException {
        try (InputStream in = Files.newInputStream(Path.of(filePath));
             XWPFDocument doc = new XWPFDocument(in)) {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph p : doc.getParagraphs()) {
                for (XWPFRun run : p.getRuns()) {
                    if (run.getText(0) != null) {
                        sb.append(run.getText(0));
                    }
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    public static String extractPdf(String filePath) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }
}
