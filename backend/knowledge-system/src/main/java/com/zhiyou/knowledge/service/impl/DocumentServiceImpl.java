package com.zhiyou.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.mapper.DocumentMapper;
import com.zhiyou.knowledge.service.DocumentService;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文档服务实现类（支持 TXT/ DOCX/ PDF 解析）
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    /** 文档上传路径（从配置文件读取） */
    @Value("${file.upload-path}")
    private String uploadPath;

    @Resource
    private DocumentMapper documentMapper;

    /**
     * 上传文档（核心方法：支持 TXT/ DOCX/ PDF 解析 + 保存文件+数据库）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Document upload(MultipartFile file) throws Exception {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        if (!originalFileName.contains(".")) {
            throw new RuntimeException("文件必须包含后缀名（如.txt/.docx/.pdf）");
        }

        // 2. 创建上传目录（不存在则自动创建）
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean isCreated = uploadDir.mkdirs();
            System.out.println("===== 上传目录创建结果：" + (isCreated ? "成功" : "失败") + " =====");
        }

        // 3. 生成唯一文件名（避免重复）
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        String fullFilePath = uploadPath + newFileName;

        // 4. 保存文件到本地
        File destFile = new File(fullFilePath);
        file.transferTo(destFile);
        System.out.println("===== 文件保存成功，路径：" + fullFilePath + " =====");

        // 5. 解析文件内容（支持 TXT/ DOCX/ PDF）
        String fileContent = "";
        try {
            if (originalFileName.toLowerCase().endsWith(".txt")) {
                // 解析 TXT 文件
                fileContent = FileUtils.readFileToString(destFile, "UTF-8");
                System.out.println("===== TXT文件解析成功，内容长度：" + fileContent.length() + " =====");
            } else if (originalFileName.toLowerCase().endsWith(".docx")) {
                // 解析 DOCX 文件（新增表格解析逻辑）
                fileContent = parseDocxFile(destFile);
                System.out.println("===== DOCX文件解析成功，内容长度：" + fileContent.length() + " =====");
            } else if (originalFileName.toLowerCase().endsWith(".pdf")) {
                // 解析 PDF 文件
                fileContent = parsePdfFile(destFile);
                System.out.println("===== PDF文件解析成功，内容长度：" + fileContent.length() + " =====");
            } else {
                fileContent = "不支持的文件格式，仅支持 TXT/ DOCX/ PDF";
                System.out.println("===== 不支持的文件格式：" + suffix + " =====");
            }
        } catch (Exception e) {
            System.out.println("===== 文件解析失败：" + e.getMessage() + " =====");
            fileContent = "文件内容解析失败：" + e.getMessage();
        }

        // 6. 保存到数据库
        Document document = new Document();
        document.setFileName(originalFileName);
        document.setFilePath(fullFilePath);
        document.setFileSize(file.getSize());
        document.setContent(fileContent);
        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        document.setIsDeleted(0); // 未删除

        int insertResult = documentMapper.insert(document);
        if (insertResult <= 0) {
            throw new RuntimeException("文档信息保存到数据库失败");
        }
        System.out.println("===== 文档信息保存成功，ID：" + document.getId() + " =====");

        // 7. 返回保存后的文档对象（包含ID）
        return document;
    }

    /**
     * 解析 DOCX 文件内容（新增表格解析，转Markdown格式）
     */
    private String parseDocxFile(File docxFile) throws IOException {
        StringBuilder content = new StringBuilder();
        try (InputStream is = new FileInputStream(docxFile);
             XWPFDocument document = new XWPFDocument(is)) {

            // 遍历文档所有元素（段落+表格）
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    // 处理普通段落
                    XWPFParagraph paragraph = (XWPFParagraph) element;
                    String paraText = paragraph.getText();
                    if (paraText != null && !paraText.trim().isEmpty()) {
                        content.append(paraText).append("\n\n");
                    }
                } else if (element instanceof XWPFTable) {
                    // 处理表格 → 转Markdown格式
                    XWPFTable table = (XWPFTable) element;
                    List<XWPFTableRow> rows = table.getRows();
                    if (rows.isEmpty()) continue;

                    // 1. 表头行
                    XWPFTableRow headerRow = rows.get(0);
                    content.append("|");
                    for (XWPFTableCell cell : headerRow.getTableCells()) {
                        content.append(cell.getText().trim()).append("|");
                    }
                    content.append("\n");

                    // 2. 分隔线行
                    content.append("|");
                    for (int i = 0; i < headerRow.getTableCells().size(); i++) {
                        content.append("---|");
                    }
                    content.append("\n");

                    // 3. 数据行
                    for (int i = 1; i < rows.size(); i++) {
                        XWPFTableRow dataRow = rows.get(i);
                        content.append("|");
                        for (XWPFTableCell cell : dataRow.getTableCells()) {
                            content.append(cell.getText().trim()).append("|");
                        }
                        content.append("\n");
                    }
                    content.append("\n\n"); // 表格后加空行，区分内容
                }
            }
        }
        return content.toString().trim();
    }

    /**
     * 解析 PDF 文件内容（解决中文乱码）
     */
    private String parsePdfFile(File pdfFile) throws IOException {
        StringBuilder content = new StringBuilder();
        try (PDDocument document = PDDocument.load(pdfFile)) {
            // 检查PDF是否加密
            if (document.isEncrypted()) {
                throw new RuntimeException("PDF文件已加密，无法解析");
            }
            // 提取文本（支持中文）
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true); // 按页面顺序提取
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());
            content.append(stripper.getText(document));
        }
        return content.toString().trim();
    }

    /**
     * 根据ID查询文档
     */
    @Override
    public Document getById(Long id) {
        return documentMapper.selectById(id);
    }

    /**
     * 查询所有文档（按创建时间倒序）
     */
    @Override
    public List<Document> listAll() {
        QueryWrapper<Document> wrapper = new QueryWrapper<Document>()
                .orderByDesc("create_time"); // 按创建时间倒序
        return documentMapper.selectList(wrapper);
    }

    /**
     * 清空所有文档
     */
    @Override
    public void clearAllDocuments() {
        // 先删除所有物理文件，再清空数据库
        List<Document> allDocs = listAll();
        for (Document doc : allDocs) {
            File file = new File(doc.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        }
        documentMapper.delete(null);
    }

    /**
     * 新增：根据ID删除单个文档
     * 包含物理文件删除 + 数据库记录删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) throws Exception {
        // 1. 查询文档信息
        Document document = documentMapper.selectById(id);
        if (document == null) {
            System.out.println("===== 文档不存在，ID：" + id + " =====");
            return false;
        }

        // 2. 删除本地物理文件
        File file = new File(document.getFilePath());
        if (file.exists()) {
            boolean isFileDeleted = file.delete();
            if (isFileDeleted) {
                System.out.println("===== 物理文件删除成功，路径：" + document.getFilePath() + " =====");
            } else {
                throw new RuntimeException("物理文件删除失败，路径：" + document.getFilePath());
            }
        } else {
            System.out.println("===== 物理文件不存在，路径：" + document.getFilePath() + " =====");
        }

        // 3. 删除数据库中的记录
        int deleteCount = documentMapper.deleteById(id);
        if (deleteCount > 0) {
            System.out.println("===== 数据库记录删除成功，ID：" + id + " =====");
            return true;
        } else {
            throw new RuntimeException("数据库记录删除失败，ID：" + id);
        }
    }
}
