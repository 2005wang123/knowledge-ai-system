package com.zhiyou.knowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiyou.knowledge.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档服务接口（无向量相关方法）
 */
public interface DocumentService extends IService<Document> {
    /**
     * 上传文档（仅保存文件+数据库，无向量处理）
     */
    Document upload(MultipartFile file) throws Exception;

    /**
     * 根据ID查询文档
     */
    Document getById(Long id);

    /**
     * 查询所有文档（按创建时间倒序）
     */
    List<Document> listAll();

    /**
     * 清空所有文档
     */
    void clearAllDocuments();
}