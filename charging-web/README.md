# charging-web

> 基于 Vue 3 + Vite + Element Plus + Pinia 搭建的充电桩运营管理前端项目。

## 目录

1. [环境要求](#1-环境要求)
2. [安装依赖](#2-安装依赖)
3. [启动开发服务](#3-启动开发服务)
4. [构建与本地预览](#4-构建与本地预览)
5. [配置说明](#5-配置说明接口地址等)
6. [项目结构说明](#6-项目结构说明)
7. [请求流程详解](#7-请求流程详解)
8. [路由与权限机制](#8-路由与权限机制)
9. [新增业务页面指南](#9-新增业务页面指南)
10. [Docker 构建](#10-docker-构建可选)
11. [常见问题](#11-常见问题)

---

## 1. 环境要求

- Node.js：**>= 18**（推荐与 Docker 构建保持一致）
- 包管理器：建议使用 **Yarn**（请勿同时混用 npm/yarn 以避免 lockfile 不一致）

> Windows 建议使用 PowerShell / Git Bash 执行下述命令。

## 2. 安装依赖

请在 **charding-web** 目录中执行：

### 2.1 安装 Yarn（如已安装可跳过）

```bash
npm install -g yarn
```

### 2.2 安装依赖

```bash
yarn install
```

## 3. 启动开发服务

```bash
yarn dev
```

- 默认地址：`http://localhost:5173/`（如有变更以控制台输出为准）

## 4. 构建与本地预览

### 4.1 构建生产包

```bash
yarn build
```

- 构建产物：`dist/`

### 4.2 本地预览（模拟生产静态服务）

```bash
yarn preview
```

## 5. 配置说明（接口地址等）

如果项目使用 Vite 环境变量，请在以下文件中配置（按项目实际情况调整）：

- `.env.development`
- `.env.production`

示例（如项目使用 VITE_ 前缀）：

```ini
VITE_API_BASE_URL=/prod-api
```

> 如使用 Docker/Nginx 运行，请确认与容器环境变量注入逻辑一致（例如 API_BASE_URL、BACKEND_URL 等）。

## 6. 项目结构说明

```
charging-web/
├── public/                   # 静态资源（不会被 Vite 处理）
├── src/
│   ├── api/                  # 所有接口请求，按业务模块分类
│   │   ├── login.js          # 登录/注销接口
│   │   ├── menu.js           # 动态路由（菜单）接口
│   │   └── business/         # 业务模块接口（充电订单等）
│   ├── assets/               # 图片、图标、全局样式
│   │   └── styles/           # SCSS 全局样式（index.scss 为入口）
│   ├── components/           # 全局公共组件（分页、上传、富文本等）
│   ├── directive/            # 自定义指令（如按钮权限 v-hasPermi）
│   ├── layout/               # 整体布局框架（侧边栏、顶栏、标签页）
│   ├── plugins/              # 全局插件（弹框、下载、缓存等工具）
│   ├── router/               # 路由配置（静态路由在此定义，动态路由由后端下发）
│   ├── store/                # Pinia 状态管理
│   │   └── modules/
│   │       ├── user.js       # 用户信息、Token、角色
│   │       ├── permission.js # 路由权限生成
│   │       ├── settings.js   # 系统设置
│   │       └── tagsView.js   # 页签缓存
│   ├── utils/                # 工具函数
│   │   ├── request.js        # Axios 封装（拦截器）
│   │   ├── auth.js           # Token 读写（基于 Cookie）
│   │   ├── env.js            # 运行时环境配置读取
│   │   └── dict.js           # 数据字典工具
│   ├── views/                # 业务页面，与路由一一对应
│   ├── permission.js         # 全局路由守卫（鉴权逻辑）
│   └── main.js               # 应用入口
├── vite/
│   └── plugins/              # Vite 插件配置（SVG、压缩、自动导入等）
├── vite.config.js            # Vite 主配置（代理、别名等）
└── Dockerfile                # 容器化构建文件
```

---

## 7. 请求流程详解

### 7.1 开发环境代理

本地开发时，浏览器发出的请求不直接打到后端，而是经过 Vite Dev Server 转发：

```
浏览器
  └─► Vite Dev Server (localhost:8081)
        └─► /dev-api/* 被代理到 target（后端地址）
              └─► rewrite: 去掉 /dev-api 前缀，转发给真实后端
```

代理配置位于 `vite.config.js`：

```javascript
proxy: {
  "/dev-api": {
    target: "http://API地址与端口/prod-api", // 后端地址
    changeOrigin: true,
    rewrite: (p) => p.replace(/^\/dev-api/, ""),
  },
}
```

> 修改后端地址只需改 `target`，无需动其他代码。

### 7.2 Axios 封装（`src/utils/request.js`）

所有业务接口统一通过封装好的 `request` 实例发起，已内置：

| 能力 | 说明 |
|---|---|
| `baseURL` | 读取 `APP_CONFIG.API_BASE_URL`（见 §7.3），本地开发为 `/dev-api` |
| 请求拦截 | 自动在 Header 中附加 `Authorization: Bearer <token>` 和 `tenant_id` |
| 防重复提交 | POST/PUT 请求 1 秒内相同数据会被拦截，避免重复提交 |
| 响应拦截 | 统一处理 `code=401`（重新登录弹窗）、`500`、`601` 等业务错误码 |
| 文件下载 | 提供 `download()` 方法，自动处理 Blob 响应并触发浏览器下载 |

### 7.3 运行时配置（`src/utils/env.js`）

`baseURL` 的优先级为：**Docker 注入 > `.env` 环境变量**

- **本地开发**：读取 `.env.development` 的 `VITE_APP_BASE_API`（通常为 `/dev-api`，经 Vite 代理转发）
- **Docker 部署**：容器启动时通过 `window.APP_CONFIG` 注入 `API_BASE_URL`，直接指向后端

### 7.4 完整请求链路示意

```
业务组件 (views/*.vue)
  │  调用 api/ 下的函数
  ▼
API 函数 (src/api/business/chargingOrder.js)
  │  return request({ url: '/chargingOrder/list', method: 'get', params })
  ▼
Axios 实例 (src/utils/request.js)
  │  请求拦截器：附加 Token、tenant_id、防重复提交
  ▼
【开发环境】Vite Dev Server 代理 → 后端
【生产环境】Nginx 反向代理 → 后端
  ▼
响应拦截器：统一处理错误码、401 自动弹登录框
  ▼
业务组件拿到 res.data 处理页面逻辑
```

---

## 8. 路由与权限机制

### 8.1 路由分类

| 类型 | 定义位置 | 说明 |
|---|---|---|
| 静态路由（`constantRoutes`） | `src/router/index.js` | 登录页、404、个人中心等所有人可访问的页面 |
| 动态路由 | 后端接口 `/getRouters` 下发 | 根据当前用户角色从后端获取，登录后动态注册 |

### 8.2 路由守卫流程（`src/permission.js`）

```
用户访问任意页面
  │
  ├─ 有 Token？
  │    ├─ 是 → 用户信息已加载？
  │    │         ├─ 否 → 调用 store/user.getInfo() 获取用户信息
  │    │         │          └─ 调用 store/permission.generateRoutes()
  │    │         │               └─ 请求后端 /getRouters，动态注册路由
  │    │         └─ 是 → 直接放行
  │    └─ 否 → 是白名单（/login、/register）？
  │              ├─ 是 → 放行
  │              └─ 否 → 重定向到 /login
```

### 8.3 按钮级权限

使用自定义指令 `v-hasPermi`（定义在 `src/directive/permission/`）：

```html
<!-- 只有拥有 'chargingOrder:list' 权限的用户才能看到此按钮 -->
<el-button v-hasPermi="['chargingOrder:list']">查询</el-button>
```

---

## 9. 新增业务页面指南

以新增"活动管理"模块为例，完整步骤如下：

**第一步：新建接口文件** `src/api/business/activity.js`

```javascript
import request from '@/utils/request'

export function getActivityList(params) {
  return request({ url: '/activity/list', method: 'get', params })
}

export function addActivity(data) {
  return request({ url: '/activity', method: 'post', data })
}
```

**第二步：新建页面文件** `src/views/business/activity/index.vue`

```vue
<template>
  <div class="app-container">
    <!-- 列表、搜索、表单等 -->
  </div>
</template>

<script setup>
import { getActivityList } from '@/api/business/activity'
// ...
</script>
```

**第三步：配置路由**

静态路由直接在 `src/router/index.js` 中添加；若菜单由后端管理（推荐），则在后端的菜单管理中新增对应路由，前端 `component` 字段填写相对 `src/views/` 的路径，如 `business/activity/index`。

**第四步：添加字典（可选）**

若页面用到数据字典，在 `<script setup>` 中引入：

```javascript
import { useDict } from '@/utils/dict'
const { activity_status } = useDict('activity_status')
```

---

## 10. Docker 构建（可选）

本项目已支持容器化构建以及部署，在 `charding-web` 目录执行：

```bash
docker build -t charding-web .
```

## 11. 常见问题

### 11.1 如何更改请求的后端域名/地址?

在当前根目录中的`vite.config.js`文件中的`server`节点

```javascript
    // vite 相关配置
    server: {
      port: 8081,
      host: true,
      open: true,
      proxy: {
        "/dev-api": {
          target: "http://localhost:8080/prod-api", // 接口地址
          changeOrigin: true,
        },
      },
    },
```


### 11.2 依赖安装慢/失败（国内网络）

可配置 registry：

```bash
npm config set registry https://registry.npmmirror.com
# 或 yarn
yarn config set registry https://registry.npmmirror.com
```

### 11.3 Linux 下构建报 "Could not resolve / Could not load"

请检查 `src/views/**.vue` 中 import 路径与真实文件名是否 **大小写一致**（Linux 文件系统大小写敏感）。