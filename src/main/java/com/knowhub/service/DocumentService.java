package com.knowhub.service;

import com.knowhub.dao.CategoryDAO;
import com.knowhub.dao.DocumentDAO;
import com.knowhub.entity.Category;
import com.knowhub.entity.Document;
import com.knowhub.util.EsUtil;
import com.knowhub.util.FileUtil;
import com.knowhub.util.PoiUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/** 文档业务层：校验 → 保存 → 解析 → 入库 → ES 推送。 */
public class DocumentService {

    /** 单个文档最大 20 MB。 */
    public static final long MAX_SIZE = 20 * 1024 * 1024L;
    /** 允许的后缀。 */
    public static final String[] ALLOWED_EXT = {".pdf", ".docx"};

    private final DocumentDAO documentDAO;
    private final CategoryDAO categoryDAO;

    public DocumentService(DocumentDAO documentDAO, CategoryDAO categoryDAO) {
        this.documentDAO = documentDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
     * 校验上传参数。返回 null 表示校验通过；否则返回错误信息。
     */
    public String validate(String fileName, long size) {
        if (fileName == null || fileName.isBlank()) return "文件名不能为空";
        if (!FileUtil.isAllowedExtension(fileName, ALLOWED_EXT)) return "只支持 pdf / docx";
        if (size <= 0) return "文件为空";
        if (size > MAX_SIZE) return "文件过大（上限 20MB）";
        return null;
    }

    /**
     * 完整的上传流程。
     *
     * @return 上传成功的 Document；失败抛 RuntimeException
     */
    public Document upload(String originalName, byte[] content, String title, Integer categoryId) throws IOException {
        String err = validate(originalName, content == null ? 0 : content.length);
        if (err != null) throw new IllegalArgumentException(err);

        // 1. 保存到磁盘
        String uploadDir = FileUtil.getUploadPath();
        String storedName = FileUtil.generateFileName(originalName);
        String localPath = FileUtil.saveFile(uploadDir, storedName, content);

        // 2. POI 提取文本
        String text = PoiUtil.extractText(localPath);

        // 3. 组装 Document
        Document doc = new Document();
        doc.setTitle(title == null || title.isBlank() ? originalName : title);
        doc.setFileName(originalName);
        doc.setFilePath(localPath);
        doc.setContentType(FileUtil.getExtension(originalName).replace(".", ""));
        doc.setFileSize((long) content.length);
        doc.setCategoryId(categoryId);
        Category cat = categoryDAO.findById(categoryId);
        doc.setCategoryName(cat == null ? "未分类" : cat.getName());
        doc.setContent(text);
        doc.setUploadTime(LocalDateTime.now());
        documentDAO.insert(doc);

        // 4. 推送到 ES（失败不阻塞主流程）
        if (text != null && !text.isBlank()) {
            boolean ok = EsUtil.indexDocument(doc.getId(), doc.getTitle(), text);
            if (!ok) System.err.println("[DocumentService] ES 索引失败，docId=" + doc.getId());
        }

        return doc;
    }

    public List<Document> list() { return documentDAO.findAll(); }

    public Document getById(Integer id) { return documentDAO.findById(id); }

    public boolean delete(Integer id) {
        if (id == null) return false;
        Document doc = documentDAO.findById(id);
        boolean ok = documentDAO.delete(id);
        if (ok && doc != null && doc.getFilePath() != null) {
            // 尝试删磁盘文件，失败不影响
            try { Files.deleteIfExists(Path.of(doc.getFilePath())); } catch (Exception e) { /* ignore */ }
        }
        return ok;
    }
}
