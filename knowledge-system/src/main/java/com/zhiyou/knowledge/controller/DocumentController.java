package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.service.DocumentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    // 上传文档接口
    @PostMapping("/upload")
    public Document upload(@RequestParam("file") MultipartFile file) throws Exception {
        return documentService.upload(file);
    }

    // 新增：查询所有文档列表接口
    @GetMapping("/list")
    public List<Document> getDocList() {
        return documentService.listAll();
    }
}