# 个人知识库 + AI 智能问答系统 (Knowledge AI System)

> 基于百度千帆 ERNIE 5.0 构建的智能知识库问答系统，支持文档上传、内容检索与 AI 智能问答。

## 项目简介

本项目是一个 **Spring Boot 后端服务**，核心能力是：
- 上传本地文档（如 TXT、MD、PDF 等），系统自动解析内容；
- 用户基于文档内容提问，AI 严格依据文档信息给出精准回答；
- 避免 AI 编造信息，实现“可信知识库问答”。

## 技术栈

- **后端框架**：Spring Boot 2.7.18
- **AI 能力**：百度千帆 ERNIE 5.0 Turbo 大模型
- **数据库**：MySQL 8.0 + MyBatis-Plus
- **认证授权**：JWT（JJWT）
- **构建工具**：Maven
- **核心依赖**：
  - `org.json` / `FastJSON` / `Jackson`：JSON 解析
  - `commons-fileupload`：文件上传处理
  - `百度千帆 API`：AI 模型调用

## 核心功能

1.  **文档管理**
    - 支持文档上传与存储
    - 自动读取文档内容，支持长文本截断处理

2.  **AI 智能问答**
    - 基于上传文档进行精准问答
    - 严格约束 AI 仅依据文档内容回答，避免幻觉
    - 支持自定义提示词，提升回答准确性

3.  **用户认证与权限**
    - 基于 JWT 的用户登录与鉴权
    - 接口安全防护

## 快速开始

### 1. 环境要求
- JDK 8+
- MySQL 8.0+
- Maven 3.6+

### 2. 配置说明
1.  复制 `application-dev.yml.example` 为 `application-dev.yml`，并配置：
    - 数据库连接信息（URL、用户名、密码）
    - 百度千帆 API Key / Secret Key（从千帆控制台获取）

2.  初始化数据库：
    ```sql
    CREATE DATABASE knowledge_system DEFAULT CHARACTER SET utf8mb4;
