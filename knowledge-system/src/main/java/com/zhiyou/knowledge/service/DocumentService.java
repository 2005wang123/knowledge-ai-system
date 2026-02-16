package com.zhiyou.knowledge.service;

import com.zhiyou.knowledge.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档服务接口
 * 定义文档上传、查询等核心业务逻辑
 * @author 你的名字（可选）
 */
public interface DocumentService {

    /**
     * 上传文档并保存到数据库
     * @param file 前端上传的文件
     * @return 保存后的文档对象（包含ID、路径等信息）
     * @throws Exception 上传过程中的异常（文件为空、路径创建失败等）
     */
    Document upload(MultipartFile file) throws Exception;

    /**
     * 根据ID查询文档
     * @param id 文档ID
     * @return 文档详情
     */
    Document getById(Long id);

    /**
     * 查询所有未删除的文档列表
     * @return 文档列表（按上传时间倒序）
     */
    List<Document> listAll();
}