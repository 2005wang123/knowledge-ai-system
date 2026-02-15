package com.zhiyou.knowledge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.mapper.DocumentMapper;
import com.zhiyou.knowledge.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文档服务实现类
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    /**
     * 文件上传根目录
     */
    private static final String UPLOAD_DIR = "D:/ai-knowledge-doc/";

    @Override
    public String uploadDocument(String title, MultipartFile file, Long userId) throws IOException {
        // 1. 校验文件类型（仅支持TXT）
        if (file == null || file.isEmpty()) {
            return "上传文件不能为空";
        }
        if (!"text/plain".equals(file.getContentType())) {
            return "仅支持上传TXT格式文件";
        }

        // 2. 创建上传目录
        File uploadPath = new File(UPLOAD_DIR);
        if (!uploadPath.exists()) {
            boolean mkdirs = uploadPath.mkdirs();
            if (!mkdirs) {
                return "创建上传目录失败";
            }
        }

        // 3. 生成唯一文件名（避免覆盖）
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".txt";
        String newFileName = UUID.randomUUID() + suffix;
        String filePath = UPLOAD_DIR + newFileName;

        // 4. 保存文件到本地
        File destFile = new File(filePath);
        file.transferTo(destFile);

        // 5. 保存文档信息到数据库
        Document document = new Document();
        document.setTitle(title);
        document.setFilePath(filePath);
        document.setUserId(userId);
        document.setCreateTime(new Date());

        boolean save = this.save(document);

        if (save) {
            return "文档上传成功，ID：" + document.getId();
        } else {
            // 数据库保存失败，删除本地文件
            destFile.delete();
            return "文档信息保存失败";
        }
    }

    @Override
    public List<Document> getDocumentList(Long userId) {
        // 根据用户ID查询文档列表
        return this.lambdaQuery()
                .eq(Document::getUserId, userId)
                .orderByDesc(Document::getCreateTime)
                .list();
    }
}