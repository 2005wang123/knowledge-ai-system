package com.zhiyou.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.mapper.DocumentMapper;
import com.zhiyou.knowledge.service.DocumentService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    // 从配置文件读取文件上传路径
    @Value("${file.upload-path}")
    private String uploadPath;

    @Resource
    private DocumentMapper documentMapper;

    @Override
    public Document upload(MultipartFile file) throws Exception {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 2. 获取并校验原始文件名（Java 8 兼容写法）
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        // 校验文件是否有后缀
        if (!originalFileName.contains(".")) {
            throw new RuntimeException("文件必须包含后缀名");
        }

        // 3. 创建上传目录，处理创建失败的场景
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean mkdirSuccess = uploadDir.mkdirs();
            if (!mkdirSuccess) {
                throw new RuntimeException("文件存储目录创建失败，请检查路径权限");
            }
        }

        // 4. 生成唯一文件名（避免重名覆盖）
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        // 完整的文件存储路径
        String fullFilePath = uploadPath + newFileName;

        // 5. 把文件保存到磁盘
        File destFile = new File(fullFilePath);
        file.transferTo(destFile);

        // 6. 读取txt文件的文本内容（兼容低版本commons-io）
        String fileContent = "";
        if (originalFileName.toLowerCase().endsWith(".txt")) {
            fileContent = FileUtils.readFileToString(destFile, "UTF-8");
        }

        // 7. 封装文档对象，写入数据库
        Document document = new Document();
        document.setFileName(originalFileName);
        document.setFilePath(fullFilePath);
        document.setFileSize(file.getSize());
        document.setContent(fileContent);
        document.setCreateTime(new Date());
        document.setIsDeleted(0);

        // 保存到数据库
        documentMapper.insert(document);

        // 8. 返回完整的文档信息
        return document;
    }

    @Override
    public Document getById(Long id) {
        return documentMapper.selectById(id);
    }

    // 新增：查询所有文档列表的实现
    @Override
    public List<Document> listAll() {
        // 查询所有未删除的文档，按上传时间倒序排列
        return documentMapper.selectList(
                new LambdaQueryWrapper<Document>()
                        .eq(Document::getIsDeleted, 0)
                        .orderByDesc(Document::getCreateTime)
        );
    }
}