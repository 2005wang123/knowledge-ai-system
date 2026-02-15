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
