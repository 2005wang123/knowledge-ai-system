# 个人知识库 AI 智能问答系统
一个基于 Spring Boot + Vue3 + DeepSeek 大模型构建的轻量化个人知识库 AI 智能问答系统，支持文档上传、内容解析、基于文档的精准问答，帮助你快速搭建专属的 AI 知识库。

![项目演示图](https://img.shields.io/badge/技术栈-Spring%20Boot%20%7C%20Vue3%20%7C%20DeepSeek-brightgreen)
![许可证](https://img.shields.io/badge/license-MIT-blue)

## 🌟 核心功能
- 📤 文档上传：支持 `.txt` 格式文档上传，自动存储到本地磁盘并入库
- 📚 文档管理：查询已上传文档列表，展示文档基本信息（ID/文件名/大小/上传时间）
- 🤖 AI 问答：基于 DeepSeek 大模型，精准回答与文档内容相关的问题
- 💻 可视化界面：Vue3 + Element Plus 构建的友好交互界面，支持对话式问答
- 🔌 全栈闭环：前后端分离架构，接口规范，跨域兼容

## 🛠 技术栈
### 后端
- 框架：Spring Boot 2.7.x
- ORM：MyBatis-Plus
- 数据库：MySQL 8.0+
- 文件处理：Apache Commons IO
- 接口规范：RESTful API

### 前端
- 框架：Vue3 + Vite
- UI 组件：Element Plus
- 网络请求：Axios
- 路由：Vue Router 4

### AI 能力
- 大模型：DeepSeek API（支持通用文本问答）

## 🚀 快速开始
### 环境要求
- JDK 8+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+

### 1. 后端部署
#### 1.1 克隆项目
```bash
git clone https://github.com/你的用户名/knowledge-ai-system.git
cd knowledge-ai-system/backend
```

#### 1.2 配置数据库
修改 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/knowledge?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root # 你的MySQL用户名
    password: 123456 # 你的MySQL密码
    driver-class-name: com.mysql.cj.jdbc.Driver

# 文件上传路径（请确保该目录存在且有读写权限）
file:
  upload-path: D:/knowledge_upload/

# DeepSeek API 配置（替换为你的API Key）
deepseek:
  api-key: your-deepseek-api-key
  base-url: https://api.deepseek.com/v1
```

#### 1.3 创建数据库
```sql
CREATE DATABASE knowledge DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建文档表
CREATE TABLE `document` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文档ID',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(500) DEFAULT NULL COMMENT '文件存储路径',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小（字节）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删，1-已删',
  `content` text COMMENT '文档文本内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档表';
```

#### 1.4 启动后端
```bash
mvn clean package
java -jar target/knowledge-ai-0.0.1-SNAPSHOT.jar
```
后端服务默认运行在 `http://localhost:8080/api`

### 2. 前端部署
#### 2.1 进入前端目录
```bash
cd ../frontend
```

#### 2.2 安装依赖
```bash
npm install
```

#### 2.3 启动前端
```bash
npm run dev
```
前端页面默认运行在 `http://localhost:5173`

### 3. 使用指南
1. 打开浏览器访问 `http://localhost:5173`
2. 点击「文档管理」→ 上传 `.txt` 格式的文档
3. 上传成功后，点击「AI 问答」→ 输入文档 ID 并确认
4. 输入与文档内容相关的问题，点击「发送」即可获取 AI 回答

## 📖 接口文档
### 后端核心接口
| 接口地址 | 请求方式 | 描述 | 参数 |
|----------|----------|------|------|
| `/api/document/upload` | POST | 上传文档 | `file`（MultipartFile） |
| `/api/document/list` | GET | 获取文档列表 | 无 |
| `/api/api/ai/ask` | POST | AI 问答 | `documentId`（文档ID）、`question`（问题） |

## 📌 待优化方向
- [ ] 支持 `.docx`/`.pdf` 等更多文档格式解析
- [ ] 引入 RAG 架构，优化长文档问答精准度
- [ ] 增加文档删除、重命名、分类功能
- [ ] 支持多轮对话上下文
- [ ] 部署上线，支持公网访问
- [ ] 增加用户登录与权限控制

## 📄 许可证
本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 🙏 致谢
- [Spring Boot](https://spring.io/projects/spring-boot)：后端开发框架
- [Vue3](https://vuejs.org/)：前端开发框架
- [Element Plus](https://element-plus.org/)：UI 组件库
- [DeepSeek](https://www.deepseek.com/)：大模型 API 支持

## 📞 联系我
如果有任何问题或建议，欢迎提 Issue 或联系我：
- GitHub：[你的用户名](https://github.com/你的用户名)
- 邮箱：your-email@example.com

---
⭐ 如果这个项目对你有帮助，欢迎点个 Star 支持一下！
