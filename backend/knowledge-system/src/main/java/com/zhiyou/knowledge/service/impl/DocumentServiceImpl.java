package com.zhiyou.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.mapper.DocumentMapper;
import com.zhiyou.knowledge.service.DocumentService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文档服务实现类（完全移除向量相关逻辑）
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    /** 文档上传路径（从配置文件读取） */
    @Value("${file.upload-path}")
    private String uploadPath;

    @Resource
    private DocumentMapper documentMapper;

    /**
     * 上传文档（核心方法：仅保存文件+数据库）
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

        // 5. 解析TXT文件内容（仅读取，无向量处理）
        String fileContent = "";
        if (originalFileName.toLowerCase().endsWith(".txt")) {
            try {
                fileContent = FileUtils.readFileToString(destFile, "UTF-8");
                System.out.println("===== TXT文件解析成功，内容长度：" + fileContent.length() + " =====");
            } catch (Exception e) {
                System.out.println("===== TXT文件解析失败：" + e.getMessage() + " =====");
                fileContent = "文件内容解析失败";
            }
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
        documentMapper.delete(null);
    }
}