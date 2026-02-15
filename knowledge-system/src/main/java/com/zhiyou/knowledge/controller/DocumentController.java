package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.common.Result;
import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.service.DocumentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 文档控制器
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    /**
     * 上传文档
     */
    @PostMapping("/upload")
    public Result<String> uploadDocument(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 简化：固定用户ID=1，真实场景从登录态解析
            Long userId = 1L;
            String result = documentService.uploadDocument(title, file, userId);
            if (result.contains("成功")) {
                return Result.success(result);
            } else {
                return Result.error(result);
            }
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 查询用户文档列表
     */
    @GetMapping("/list")
    public Result<List<Document>> getDocumentList() {
        // 简化：固定用户ID=1
        Long userId = 1L;
        List<Document> documentList = documentService.getDocumentList(userId);
        return Result.success(documentList);
    }
}