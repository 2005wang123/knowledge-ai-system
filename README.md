```markdown
# 个人知识库 AI 智能问答系统
![Java](https://img.shields.io/badge/Java-8+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange.svg)
![ERNIE 5.0](https://img.shields.io/badge/ERNIE-5.0%20Turbo-purple.svg)

基于百度千帆 ERNIE 5.0 大模型构建的私有化知识库智能问答系统，支持本地文档解析、精准问答，杜绝 AI 编造信息，满足个人/小团队的知识库管理与智能问答需求。

## 🌟 核心特性
- 📄 **多格式文档支持**：兼容 TXT/MD 等文本格式文档上传与解析（可扩展至 PDF/Word）；
- 🧠 **精准问答能力**：AI 严格基于文档内容回答问题，避免“幻觉”；
- 🔐 **安全认证**：基于 JWT 实现用户登录与接口鉴权；
- 📝 **灵活配置**：支持自定义 AI 提示词、回答长度、温度等参数；
- 🚀 **轻量部署**：Spring Boot 单体架构，本地/服务器均可快速部署。

## 🛠 技术栈
### 后端核心
| 技术/框架         | 版本       | 用途                     |
|-------------------|------------|--------------------------|
| Spring Boot       | 2.7.18     | 后端开发框架             |
| MyBatis-Plus      | 3.5.3.1    | 数据库ORM框架            |
| MySQL             | 8.0.30     | 关系型数据库             |
| JWT (JJWT)        | 0.11.5     | 用户认证与授权           |
| 百度千帆 SDK      | 0.3.1      | ERNIE 5.0 模型调用       |
| Commons-fileupload| 1.4        | 文件上传处理             |
| FastJSON/JSON     | 2.0.43/20240303 | JSON 解析         |

### 开发/构建工具
- Maven 3.6+：项目构建与依赖管理
- Git：版本控制
- IDEA/Eclipse：开发 IDE

## 🚀 快速开始
### 1. 环境准备
- 操作系统：Windows/Linux/MacOS
- JDK：8 及以上版本
- MySQL：8.0 及以上版本
- Maven：3.6 及以上版本
- 百度千帆账号：完成实名认证，开通 ERNIE 5.0 Turbo 模型权限

### 2. 源码获取
```bash
# 克隆仓库
git clone https://github.com/2005wang123/knowledge-ai-system.git
cd knowledge-ai-system
```

### 3. 配置修改
#### 3.1 数据库配置
在 `src/main/resources` 目录下创建 `application.yml` 文件（已加入.gitignore，避免敏感信息泄露），配置数据库连接：
```yaml
spring:
  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/knowledge_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root  # 你的MySQL用户名
    password: 123456  # 你的MySQL密码
  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 10MB  # 单个文件最大大小
      max-request-size: 50MB  # 单次请求最大文件大小

# MyBatis-Plus配置
mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.zhiyou.knowledge.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# 百度千帆配置
qianfan:
  api-key: your-api-key  # 你的千帆API Key（bce-v3/ALTAK-开头）
  secret-key: your-secret-key  # 你的千帆Secret Key
  model: ERNIE-5.0-Turbo
  temperature: 0.7  # AI回答温度，0-1之间，越小越精准
  max-tokens: 2000  # 最大回答字符数
```

#### 3.2 数据库初始化
执行以下 SQL 创建数据库（如需自定义表结构，可补充脚本）：
```sql
CREATE DATABASE IF NOT EXISTS knowledge_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 示例：文档表（可根据实际需求调整）
CREATE TABLE IF NOT EXISTS document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文档ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_path VARCHAR(512) NOT NULL COMMENT '文件存储路径',
    file_size BIGINT COMMENT '文件大小（字节）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除（0-未删，1-已删）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档存储表';
```

### 4. 项目启动
#### 4.1 依赖安装
```bash
# 安装依赖
mvn clean install -Dmaven.test.skip=true
```

#### 4.2 启动项目
```bash
# 方式1：Maven启动
mvn spring-boot:run

# 方式2：打包后启动
mvn package -Dmaven.test.skip=true
java -jar target/knowledge-system-0.0.1-SNAPSHOT.jar
```

启动成功后，服务默认运行在 `http://localhost:8080`（可在application.yml中修改端口）。

### 5. 接口使用示例
#### 5.1 文档上传
- 请求方式：POST
- 请求地址：`/api/document/upload`
- 请求参数：form-data 格式，key 为 `file`，值为本地文档文件
- 响应示例：
```json
{
  "code": 200,
  "msg": "上传成功",
  "data": {
    "documentId": 1,
    "fileName": "test.txt",
    "filePath": "D:/upload/test.txt"
  }
}
```

#### 5.2 AI 问答
- 请求方式：POST
- 请求地址：`/api/ai/ask`
- 请求体（JSON）：
```json
{
  "documentId": 1,
  "question": "张三的手机号是多少？"
}
```
- 响应示例：
```json
{
  "code": 200,
  "msg": "success",
  "data": "张三的手机号是13800138000。"
}
```

## 📁 项目结构
```
knowledge-ai-system/
├── src/
│   ├── main/
│   │   ├── java/com/zhiyou/knowledge/
│   │   │   ├── controller/       # 接口控制层：处理HTTP请求
│   │   │   ├── service/          # 业务逻辑层：核心功能实现（AI调用、文档处理）
│   │   │   ├── mapper/           # 数据访问层：MyBatis-Plus Mapper接口
│   │   │   ├── entity/           # 实体类：对应数据库表/返回DTO
│   │   │   ├── config/           # 配置类：JWT、文件上传、AI客户端配置
│   │   │   ├── exception/        # 全局异常处理
│   │   │   └── KnowledgeApplication.java  # 项目启动类
│   │   └── resources/
│   │       ├── application.yml   # 应用配置（需自行创建）
│   │       ├── mapper/           # MyBatis XML映射文件
│   │       └── static/           # 静态资源（如前端页面，可选）
│   └── test/                     # 单元测试目录
├── pom.xml                       # Maven依赖配置
├── .gitignore                    # Git忽略文件配置
└── README.md                     # 项目说明文档
```

## ⚠️ 注意事项
1. **敏感信息保护**：
   - 切勿将 `application.yml` 提交到Git仓库（已加入.gitignore）；
   - 百度千帆API Key/Secret Key需妥善保管，避免泄露。
2. **模型权限**：
   - 需在百度千帆控制台开通 ERNIE 5.0 Turbo 模型权限，否则接口调用会失败；
   - 个人账号有免费调用额度，超出后需按需计费。
3. **文件路径**：
   - 文档存储路径建议使用绝对路径，确保程序有文件读取/写入权限；
   - 长文档会自动截断前2000字符，可在 `AIServiceImpl.java` 中调整截断逻辑。
4. **网络问题**：
   - 确保服务器/本地网络能访问百度千帆接口（`https://qianfan.baidubce.com`），避免代理/防火墙拦截。

## 📌 常见问题
### Q1：AI调用返回401/403错误？
A1：
- 401：API Key/Secret Key错误，或认证方式不正确；
- 403：未开通ERNIE 5.0 Turbo模型权限，或账号未实名认证。

### Q2：文档上传后无法读取？
A2：
- 检查文件路径是否正确，程序是否有文件读取权限；
- 检查文件编码格式（建议UTF-8）。

### Q3：Maven依赖下载失败？
A3：
- 确认pom.xml中已添加百度Maven仓库；
- 清理本地Maven缓存（删除 `.m2/repository/com/baidubce` 目录）后重新下载。

## 📋 后续规划
- [ ] 扩展文档格式：支持PDF、Word、Excel等格式解析；
- [ ] 向量检索优化：接入向量数据库，实现文档内容精准检索；
- [ ] 前端界面开发：基于Vue/React开发可视化操作界面；
- [ ] 多轮对话：支持基于上下文的多轮问答；
- [ ] 多模型支持：扩展接入GLM、DeepSeek等大模型；
- [ ] 权限细化：实现文档级别的权限控制。

## 🧑‍💻 贡献说明
本项目为个人学习/毕设项目，欢迎提交Issue反馈问题，或通过PR提交优化建议。

## 📄 许可证
本项目仅供学习、交流使用，未经授权不得用于商业用途。
```

### 核心特点说明
1. **完整性**：覆盖项目介绍、环境准备、配置、启动、使用、结构、问题排查等全流程，新手也能快速上手；
2. **实用性**：包含真实可运行的配置示例、SQL脚本、接口示例，直接复用即可；
3. **规范性**：采用开源项目通用的README结构，搭配徽章、代码块、分级标题，可读性强；
4. **适配性**：完全贴合你的技术栈（Spring Boot 2.7.18、MySQL 8.0、千帆ERNIE 5.0），无多余内容；
5. **避坑指南**：专门增加「注意事项」「常见问题」模块，提前规避开发/部署中的核心问题。

你可直接复制这份代码到项目根目录的 `README.md` 文件中，也可根据实际开发进度调整「后续规划」「接口示例」等内容。
