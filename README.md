# 智能知识库问答系统

## 项目简介

这是一个基于 **Spring Boot + DeepSeek API** 构建的智能知识库问答系统，支持文档上传、内容存储和基于文档内容的 AI 智能问答。用户可以上传本地文档（.txt/.docx/.pdf），系统会将文档内容持久化到 MySQL 数据库，并通过 DeepSeek 大语言模型实现基于文档内容的精准问答。

## 功能特性

- ✅ **文档上传**：支持 .txt/.docx/.pdf 格式文档上传，单个文件最大 10MB
- ✅ **文档管理**：文档列表展示、按创建时间倒序排列
- ✅ **内容解析**：自动解析 TXT 文档文本内容并存储
- ✅ **AI 问答**：基于 DeepSeek API 实现文档内容的智能问答
- ✅ **跨域支持**：后端配置全局 CORS，支持前后端分离部署
- ✅ **数据持久化**：使用 MySQL + MyBatis-Plus 实现数据持久化
- ✅ **简洁 UI**：基于 Vue 3 + Element Plus 的现代化前端界面

## 技术栈

### 后端
- **核心框架**：Spring Boot 2.7.18
- **ORM 框架**：MyBatis-Plus 3.5.3.1
- **数据库**：MySQL 8.0.33
- **AI 服务**：DeepSeek API
- **HTTP 客户端**：OkHttp 4.10.0
- **JSON 解析**：Fastjson2 2.0.52
- **文件处理**：Apache Commons IO 2.11.0

### 前端
- **核心框架**：Vue 3
- **构建工具**：Vite
- **UI 组件库**：Element Plus
- **HTTP 客户端**：Axios

## 快速开始

### 环境准备

在开始之前，请确保你的电脑已安装以下软件：
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+ / 8.0+
- Node.js 16+（前端开发）
- Git

### 后端启动

1. **克隆项目**
   ```bash
   git clone https://github.com/2005wang123/knowledge-ai-system.git
   cd knowledge-ai-system
   ```

2. **创建数据库**
   在 MySQL 中执行以下 SQL 创建数据库：
   ```sql
   CREATE DATABASE knowledge_ai DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **修改配置文件**
   打开 `src/main/resources/application.yml`，修改以下配置：
   ```yaml
   # 数据库配置
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/knowledge_ai?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
       username: root  # 替换为你的MySQL用户名
       password: 123456  # 替换为你的MySQL密码

   # DeepSeek API 配置
   deepseek:
     api-key: sk-你的DeepSeekAPIKey  # 替换为你的有效API Key
     base-url: https://api.deepseek.com
   ```

4. **创建上传目录**
   手动创建本地上传目录（或启动项目自动创建）：
   ```
   D:/knowledge_ai/upload/
   ```

5. **启动后端服务**
   在 IDEA 中运行 `KnowledgeAiApplication.java`，或在终端执行：
   ```bash
   mvn spring-boot:run
   ```
   看到以下日志说明启动成功：
   ```
   ===== 文档上传系统启动成功 =====
   ===== 接口地址：http://localhost:8080/api/document =====
   ```

### 前端启动

1. **进入前端目录**
   ```bash
   cd knowledge-ai-frontend  # 替换为你的前端目录名
   ```

2. **安装依赖**
   ```bash
   npm install
   ```

3. **启动前端服务**
   ```bash
   npm run dev
   ```
   看到以下日志说明启动成功：
   ```
   Local: http://localhost:5173/
   ```

### 访问系统

- **前端地址**：http://localhost:5173
- **后端接口地址**：http://localhost:8080/api

## 使用指南

### 1. 上传文档
- 点击左侧「点击或拖拽上传文档」区域
- 选择本地 .txt/.docx/.pdf 文件
- 上传成功后，文档会显示在左侧列表中

### 2. 查看文档
- 点击左侧文档列表中的文件
- 右侧会显示文档的基本信息（文件名、大小、上传时间、字数）
- 点击「文档内容」标签可查看文档原文

### 3. AI 问答
- 确保已选中左侧的文档
- 在底部输入框中输入问题（如「这首诗的作者是谁？」）
- 点击「发送」按钮，AI 会基于文档内容生成回答

## 项目结构

```
knowledge-ai/
├── pom.xml                                    # Maven依赖配置
├── src/
│   ├── main/
│   │   ├── java/com/zhiyou/knowledge/
│   │   │   ├── KnowledgeAiApplication.java   # 启动类
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java           # 全局跨域配置
│   │   │   ├── controller/
│   │   │   │   ├── DocumentController.java   # 文档接口
│   │   │   │   └── KnowledgeController.java  # AI问答接口
│   │   │   ├── entity/
│   │   │   │   └── Document.java             # 文档实体
│   │   │   ├── mapper/
│   │   │   │   └── DocumentMapper.java       # 文档Mapper
│   │   │   ├── service/
│   │   │   │   ├── DocumentService.java      # 文档服务接口
│   │   │   │   ├── KnowledgeService.java     # AI问答服务接口
│   │   │   │   └── impl/
│   │   │   │       ├── DocumentServiceImpl.java
│   │   │   │       └── KnowledgeServiceImpl.java
│   │   │   └── util/
│   │   │       └── DeepSeekUtils.java        # DeepSeek API工具类
│   │   └── resources/
│   │       └── application.yml                # 配置文件
│   └── test/                                  # 测试目录
└── README.md                                   # 项目说明文档
```

## 📸 效果截图
![截图](images/screenshot.png)

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

- GitHub Issues：https://github.com/2005wang123/knowledge-ai-system/issues

---
