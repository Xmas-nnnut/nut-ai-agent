# AI智能助手前端项目

这是一个基于Vue3的AI智能助手前端项目，包含AI恋爱大师和AI超级智能体两个应用。

## 功能特性

### 主页
- 美观的应用选择界面
- 支持切换不同的AI应用

### AI恋爱大师应用
- 聊天室风格界面
- 实时SSE连接，流式显示AI回复
- 自动生成聊天室ID
- 专业的恋爱咨询功能

### AI超级智能体应用
- 聊天室风格界面
- 实时SSE连接，流式显示AI回复
- 多功能AI助手

## 技术栈

- **Vue 3** - 前端框架
- **Vue Router 4** - 路由管理
- **Axios** - HTTP客户端
- **Vite** - 构建工具
- **EventSource** - SSE连接

## 项目结构

```
nut-ai-agent-frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/
│   │   └── index.js       # API接口配置
│   ├── router/
│   │   └── index.js       # 路由配置
│   ├── views/
│   │   ├── Home.vue       # 主页
│   │   ├── LoveApp.vue    # AI恋爱大师页面
│   │   └── ManusApp.vue   # AI超级智能体页面
│   ├── App.vue            # 根组件
│   ├── main.js            # 入口文件
│   └── style.css          # 全局样式
├── index.html             # HTML模板
├── package.json           # 项目配置
├── vite.config.js         # Vite配置
└── README.md              # 项目说明
```

## 安装和运行

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

项目将在 http://localhost:3000 启动

### 3. 构建生产版本

```bash
npm run build
```

## 后端接口配置

项目默认连接到 `http://localhost:8123/api`，确保后端服务已启动。

### 接口说明

1. **AI恋爱大师接口**
   - 路径: `/ai/love_app/chat/sse`
   - 方法: GET
   - 参数: `message`, `chatId`
   - 类型: SSE (Server-Sent Events)

2. **AI超级智能体接口**
   - 路径: `/ai/manus/chat`
   - 方法: GET
   - 参数: `message`
   - 类型: SSE (Server-Sent Events)

## 使用说明

1. 访问主页选择要使用的AI应用
2. 在聊天界面输入消息
3. AI将通过SSE实时返回回复
4. 支持流式显示，提供良好的用户体验

## 注意事项

- 确保后端服务已启动且接口可访问
- 浏览器需要支持EventSource API（现代浏览器都支持）
- 开发环境下端口为3000，可在vite.config.js中修改

## 开发说明

- 项目使用Vue 3 Composition API
- 响应式设计，支持移动端
- 代码结构清晰，易于维护和扩展
- 包含错误处理和加载状态管理 