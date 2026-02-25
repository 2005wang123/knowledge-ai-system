package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.service.DocumentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文档控制器（仅处理文档上传/查询，无向量/问答接口）
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    /**
     * 上传文档接口
     */
    @PostMapping("/upload")
    public Document upload(@RequestParam("file") MultipartFile file) throws Exception {
        return documentService.upload(file);
    }

    /**
     * 根据ID查询文档
     */
    @GetMapping("/getById/{id}")
    public Document getById(@PathVariable Long id) {
        return documentService.getById(id);
    }

    /**
     * 查询所有文档
     */
    @GetMapping("/listAll")
    public List<Document> listAll() {
        return documentService.listAll();
    }

    /**
     * 清空所有文档
     */
    @DeleteMapping("/clearAll")
    public void clearAllDocuments() {
        documentService.clearAllDocuments();
    }
}