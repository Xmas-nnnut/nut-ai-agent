# Nut AI Agent 🤖

> 基于 Spring AI 的智能代理平台，集成多种AI工具和功能模块

## 📋 项目简介

Nut AI Agent 是一个功能强大的AI智能代理平台，基于 Spring Boot 3.4.5 和 Spring AI 框架构建。该平台集成了阿里云通义千问大语言模型，提供多种智能代理服务和工具调用能力，支持RAG（检索增强生成）、工具链调用、实时流式响应等功能。

## ✨ 核心功能

### 🎯 智能代理

- **🤖 AI 超级智能体**
  - 全能AI助手，具备强大的工具调用能力
  - 支持复杂任务的分解和执行
  - 可调用多种外部工具完成用户需求

- **💝 AI 恋爱大师**
  - 专业的情感咨询服务
  - 基于恋爱知识库的RAG检索
  - 提供个性化的恋爱建议和技巧

### 🛠️ 工具集成

| 工具 | 功能描述                    |
|------|-------------------------|
| PDF生成工具 | 将内容转换为PDF文档(支持markdown) |
| 文件操作工具 | 文件读写、管理操作               |
| 终端操作工具 | 执行系统命令                  |
| 网页抓取工具 | 抓取网页内容                  |
| 网络搜索工具 | 在线搜索功能                  |
| 资源下载工具 | 下载网络资源                  |

### 🚀 技术特性

- **实时流式响应** - 基于SSE的实时对话体验
- **向量数据库** - 使用PGVector支持RAG功能
- **MCP协议** - 支持Model Context Protocol
- **会话持久化** - 文件基础的聊天记录存储
- **API文档** - 集成Knife4j/Swagger文档

## 🏗️ 技术架构

### 后端技术栈

- **Spring Boot 3.4.5** - 主框架
- **Spring AI 1.0.0-M6** - AI集成框架
- **Java 21** - 编程语言
- **阿里云通义千问** - 大语言模型
- **PostgreSQL + PGVector** - 向量数据库
- **Maven** - 项目管理

### 前端技术栈

- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Vue Router** - 路由管理
- **Axios** - HTTP客户端
- **Markdown-it** - Markdown渲染

### 项目结构

```
nut-ai-agent/
├── src/main/java/com/xqj/nutaiagent/
│   ├── agent/              # AI代理实现
│   ├── tools/              # 工具集合
│   ├── rag/               # RAG相关配置
│   ├── controller/        # REST控制器
│   └── config/            # 配置类
├── nut-ai-agent-frontend/  # Vue前端应用
├── image-search-mcp-server/ # MCP图像搜索服务
└── src/main/resources/
    └── document/          # RAG知识库文档
```

## 🚀 快速开始

### 环境要求

- Java 21+
- Node.js 16+
- PostgreSQL 12+ (支持vector扩展)
- Maven 3.6+

### 1. 克隆项目

```bash
git clone https://github.com/your-username/nut-ai-agent.git
cd nut-ai-agent
```

### 2. 配置数据库

创建PostgreSQL数据库并安装vector扩展：

### 3. 配置应用

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-dashscope-api-key  # 替换为你的API密钥
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_agent
    username: your-username
    password: your-password  # 替换为你的向量数据库配置

search-api:
  api-key: your-search-api-key  # 替换为搜索API密钥
```

### 4. 启动后端服务

后端服务将在 `http://localhost:8123` 启动。

### 5. 启动前端应用

```bash
cd nut-ai-agent-frontend
npm install
npm run dev
```

前端应用将在 `http://localhost:5173` 启动。

### 6. 访问应用

- **前端界面**: http://localhost:5173
- **API文档**: http://localhost:8123/api/swagger-ui.html

## 📖 使用指南

### AI恋爱大师

1. 在主页选择 " 💝 AI恋爱大师 "
2. 输入您的情感问题或恋爱困惑
3. AI将基于专业知识库提供个性化建议

### AI超级智能体

1. 在主页选择 " 🤖 AI超级智能体 "
2. 描述您需要完成的任务
3. 智能体将自动选择合适的工具来解决问题

### API调用示例

```javascript
// 同步调用恋爱大师
const response = await fetch('/api/ai/love_app/chat/sync?message=如何提升约会成功率&chatId=123');

// 流式调用超级智能体
const eventSource = new EventSource('/api/ai/manus/chat?message=帮我生成一份报告');
eventSource.onmessage = function(event) {
  console.log(event.data);
};
```

## 🔧 开发指南

### 添加新工具

1. 在 `tools` 包下创建新的工具类
2. 实现 `@Tool` 注解标记的方法
3. 在 `ToolRegistration` 中注册工具

示例：

```java
@Component
public class CustomTool {
    
    @Tool("自定义工具描述")
    public String customFunction(String input) {
        // 工具逻辑实现
        return "处理结果";
    }
}
```

### 创建新代理

1. 继承 `BaseAgent` 或相关基类
2. 配置系统提示词和工具集
3. 在控制器中添加对应接口

### 扩展RAG功能

1. 在 `resources/document` 目录添加知识库文档
2. 配置文档加载器和向量存储
3. 自定义查询增强器

## 🌟 主要特色

- **🎯 多代理架构** - 支持不同领域的专业化AI代理
- **🔧 丰富工具链** - 内置多种实用工具，支持自定义扩展
- **💬 实时交互** - 基于SSE的流式对话体验
- **📚 知识检索** - RAG技术实现知识库问答
- **🌐 现代化UI** - Vue 3构建的响应式前端界面
- **📋 完整文档** - 集成API文档和使用指南
