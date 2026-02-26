Security Policy

Supported Versions

当前项目处于初始版本阶段，以下版本会得到安全更新支持：

表格

Version	Supported

1.0.x	✅

< 1.0	❌

Reporting a Vulnerability

如果你发现了安全漏洞，请通过以下方式联系我：

提交 GitHub Issue，标签选择 security

或直接发送邮件至：2470939152@qq.com

我会在 48 小时内 确认收到漏洞报告，并在 7 天内 给出初步评估和修复计划。如果漏洞被确认，我会尽快发布修复版本，并在安全公告中感谢你的贡献。

Security Best Practices

为了保障你的部署安全，请遵循以下建议：

不要提交敏感信息：application.yml 中的数据库密码、DeepSeek API Key 等敏感信息，切勿提交到公开仓库。

使用环境变量：在生产环境中，建议通过环境变量或配置中心管理敏感配置，而非硬编码在配置文件中。

定期更新依赖：及时更新项目依赖，修复已知的安全漏洞。

限制访问权限：生产环境中，仅开放必要的端口和接口，避免不必要的暴露。
