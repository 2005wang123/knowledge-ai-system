package com.zhiyou.knowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiyou.knowledge.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文档服务接口
 */
public interface DocumentService extends IService<Document> {
    /**
     * 上传文档
     * @param title 文档标题
     * @param file 上传的文件
     * @param userId 上传用户ID
     * @return 上传结果
     */
    String uploadDocument(String title, MultipartFile file, Long userId) throws IOException;

    /**
     * 根据用户ID查询文档列表
     * @param userId 用户ID
     * @return 文档列表
     */
    List<Document> getDocumentList(Long userId);
}