# 100Charge — 项目概览 (AI / Developer Guide)

## 关于本项目

100Charge 是基于 **Spring Boot + 若依 (RuoYi) 框架** 开发的开源充电桩运营平台，由「山东行川新能源科技有限公司」维护。项目为中小型充电场站提供：充电桩管理、场站运营、计费策略、订单管理、微信小程序用户端等核心能力。

- **开源协议**：AGPL v3 + XCPL 双协议
- **仓库地址**：https://gitee.com/ustcyc/100charge
- **演示站点**：https://demo-admin.100charge.cn 

---

## 项目结构与技术栈总览

```
100charge/
├── charging-api/     # 后端 API 服务 (Spring Boot 2.6.x + Maven 多模块)
├── charging-mini/    # 微信小程序用户端 (uni-app, Vue 2)
├── charging-web/     # 管理后台前端 (Vue 3 + Vite + Element Plus)
└── agents.md         # 本文件
```

### 系统依赖关系

```
┌──────────────┐     ┌──────────────┐
│ charging-web │     │ charging-mini│
│ (Vue3 管理端)│     │ (uni-app 小程序)│
└──────┬───────┘     └──────┬───────┘
       │ HTTP/REST          │ HTTP/REST
       └────────┬───────────┘
                ▼
┌───────────────────────────────┐
│      charging-api (后端)       │
│   Spring Boot 2.6.15 + Java 8  │
│   端口 9080, context /prod-api │
└───┬───────┬───────┬───────────┘
    │       │       │
    ▼       ▼       ▼
┌────────┐ ┌─────┐ ┌──────────┐
│PostgreSQL│ │Redis│ │RocketMQ  │
│ 14+     │ │6.2+ │ │4.9.4     │
└────────┘ └─────┘ └──────────┘
```

---

## 1. charging-api — 后端 API

### 1.1 基本信息

| 项目 | 说明 |
|------|------|
| 构建工具 | Maven 多模块聚合工程 |
| GroupId / ArtifactId | `com.xingchuan` / `charging-api` |
| Java 版本 | JDK 1.8 |
| Spring Boot 版本 | 2.6.15 |
| 启动类 | `xingchuan-admin/.../AdminApplication.java` |
| 服务端口 | 9080 |
| 上下文路径 | `/prod-api` |
| 数据库初始化脚本 | `charging-api/sql/postgresql/db1.sql`, `db2.sql` (按顺序执行) |
| Dockerfile | 多阶段构建 (maven:3.9.11 → eclipse-temurin:8-jre) |

### 1.2 Maven 模块结构

```
charging-api/
├── pom.xml                     # 父 POM，统一依赖和版本管理
├── xingchuan-admin/            # 【入口模块】Spring Boot 启动 + 所有 Controller
│   └── src/main/java/com/xingchuan/
│       ├── AdminApplication.java          # @SpringBootApplication
│       └── web/controller/
│           ├── charging/       # 充电业务控制器 (18 个)
│           ├── system/         # 系统管理控制器 (14 个)
│           ├── monitor/        # 监控控制器
│           └── common/         # 通用控制器
├── xingchuan-framework/        # 【框架核心】安全/过滤器/拦截器/AOP/配置
├── xingchuan-system/           # 【业务模块】Service + Mapper + Entity + Domain
├── xingchuan-common/           # 【通用模块】注解/枚举/工具类/异常/基类
├── xingchuan-quartz/           # 【定时任务】Quartz 调度模块
└── xingchuan-generator/        # 【代码生成】Velocity 模板代码生成器
```

**模块依赖关系**（自上而下）：
`admin` → `framework` → `system` → `common`
`admin` → `quartz` → `common`
`admin` → `generator` → `common`

### 1.3 技术栈

| 类别 | 技术 | 版本 / 说明 |
|------|------|------------|
| Web 容器 | Undertow | 替代 Tomcat |
| 安全框架 | Spring Security + JWT (jjwt) + BCrypt | Bearer Token 认证 |
| ORM | MyBatis-Plus | 3.5.6 |
| 分页 | PageHelper | PostgreSQL 方言 |
| 数据库 | PostgreSQL + Druid 连接池 | 支持主从模式（从库默认关闭） |
| 缓存 | Redis (Lettuce 客户端) | 会话缓存/Token/验证码 |
| 消息队列 | RocketMQ | rocketmq-spring-boot-starter 2.2.3 |
| 定时任务 | Quartz Scheduler | |
| API 文档 | Swagger 3 (springfox) | 3.0.0 |
| JSON | Fastjson2 | 2.0.43 |
| 工具库 | Hutool | 5.8.16 |
| 微信生态 | weixin-java-miniapp + wechatpay-java | 小程序登录/支付/退款/分账 |
| 短信 | 阿里云 Dysmsapi | 可选，默认关闭 |
| 模板引擎 | Apache Velocity | 用于代码生成 |
| 二维码 | Google ZXing | 充电桩二维码 |
| 验证码 | Google Kaptcha | 数学计算型验证码 |
| 系统监控 | Oshi | CPU/内存/JVM 信息 |
| 代码简化 | Lombok | |

### 1.4 核心业务域

充电业务相关代码位于 `xingchuan-system` 和 `xingchuan-admin/web/controller/charging/`：

| 业务模块 | 核心类 | 说明 |
|----------|--------|------|
| 充电站管理 | `ChargingStationsController/Service/Entity` | 场站 CRUD、位置、标签 |
| 充电桩管理 | `ChargingPileController/Service/Entity` | 充电桩/枪配置与状态 |
| 充电订单 | `ChargingOrderController/Service/Entity` | 订单全生命周期（创建→充电中→结束→支付→退款） |
| 计费规则 | `RuleController/Service/Entity` | 灵活峰谷电价/服务费规则 |
| 用户管理 | `AppUserController/Service/Entity` | 小程序用户、车辆、余额 |
| 支付集成 | `PayController`, `PayNotifyController` | 微信支付 APIv3 回调 |
| 首页仪表盘 | `HomeController` | 运营数据统计 |
| 评价系统 | `StationEvaluationController` | 充电站评价 |
| 报表 | `ReportFormController` | 数据导出 |
| 实时数据 | `RealtimeDataController` | 充电实时状态 |

### 1.5 框架关键设计

- **分层架构**：Controller → Service (接口+实现) → Mapper (MyBatis-Plus) → Entity
- **Request/Response 分离**：每个接口使用独立的 Req/Resp DTO (`domain/req/`, `domain/resp/`)
- **自定义注解**：`@Log`(操作日志)、`@DataScope`(数据权限)、`@RateLimiter`(限流)、`@RepeatSubmit`(防重复提交)、`@Anonymous`(免鉴权)
- **BaseController**：提供通用分页、Excel 导出的基类
- **BaseEntity**：统一创建人/创建时间等公共字段
- **数据传输签名**：小程序端请求携带 timestamp + MD5 签名验证

---

## 2. charging-mini — 微信小程序用户端

### 2.1 基本信息

| 项目 | 说明 |
|------|------|
| 框架 | uni-app (Vue 2.0) |
| 开发工具 | HBuilderX |
| UI 库 | uview-ui ^2.0.36 (通过 easycom 自动注册) |
| 编译目标 | 微信小程序（主目标），同时支持支付宝/百度/抖音/H5/App |
| 微信小程序 AppID | `wx361b3b951a7d5f54` |
| NPM 依赖 | uview-ui, echarts, js-md5, sass |

### 2.2 目录结构

```
charging-mini/
├── pages.json                  # 页面路由、分包、tabBar 配置
├── manifest.json               # uni-app 应用配置 (AppID/权限/地图 SDK)
├── main.js                     # 入口 (注册 Vue/uView/mixin/request)
├── App.vue                     # 根组件 (定位/版本检查)
├── uni.scss / common.scss      # 全局样式/主题
│
├── config/
│   ├── api.js                  # 40+ API 接口定义（按业务模块分类）
│   ├── request.js              # HTTP 拦截器 (Token/MD5签名/Loading)
│   └── md5Utils.js             # 请求签名工具
│
├── components/                 # 全局复用组件
│   ├── home-map/               # 地图模式组件 (核心地图交互)
│   ├── home-sticky-search/     # 吸顶搜索 (含筛选逻辑)
│   ├── home-station-card/      # 场站信息卡片
│   ├── home-list/              # 场站列表
│   ├── coupon-item/            # 优惠券卡片
│   └── tabbar/                 # 自定义底部导航
│
├── pages/
│   ├── index/                  # 首页/启动页/个人中心 (主包)
│   │   ├── index.vue           # 首页 (地图+列表双模式)
│   │   ├── start.vue           # 启动/扫码入口
│   │   └── wode.vue            # 个人中心 (余额/订单/车辆/优惠券)
│   ├── login/                  # 登录 (微信手机号一键登录)
│   ├── scan/                   # 扫码充电
│   ├── stations/               # 【分包】场站+充电+订单
│   │   ├── site/               # 电站详情/充电确认/充电中
│   │   └── orders/             # 订单列表/详情
│   └── wode/                   # 【分包】车辆/充值/提现/优惠券/客服
│
├── utils/mixin.js              # 全局混入 (微信分享)
├── wxcomponents/               # 微信原生组件 (隐私弹窗)
└── static/                     # 静态资源
```

### 2.3 技术要点

- **跨端条件编译**：使用 `#ifdef MP-WEIXIN` / `#ifdef APP-PLUS` 处理平台差异
- **地图方案**：小程序端使用腾讯地图，App 端使用高德地图
- **状态管理**：未使用 Vuex，通过 `uni.setStorageSync/getStorageSync` 管理本地状态（token/openid/经纬度/城市）
- **API 签名**：请求时自动附加 timestamp + 参数按键排序后 MD5 签名
- **充电实时轮询**：充电中页面通过轮询获取充电进度/电量/费用
- **分包**：`stations` + `wode` 两个分包，启用预加载规则

### 2.4 核心用户流程

1. 定位获取城市 → 展示开放城市充电站
2. 地图/列表浏览充电站 → 查看详情/计费规则
3. 扫码充电桩 → 确认充电（选择优惠券）→ 启动充电
4. 实时监控充电状态（轮询电量/费用/时长）
5. 结束充电 → 支付订单
6. 个人中心：余额充值/提现、车辆管理、优惠券、历史订单

---

## 3. charging-web — 管理后台前端

### 3.1 基本信息

| 项目 | 说明 |
|------|------|
| 框架 | Vue 3 (Composition API, `<script setup>`) |
| 构建工具 | Vite 5 |
| UI 库 | Element Plus 2.4.3 |
| 状态管理 | Pinia 2.1.7 |
| 路由 | Vue Router 4 (History 模式) |
| HTTP 客户端 | Axios 0.27.2 |
| 图表 | ECharts 5.4.3 |
| 富文本编辑器 | @vueup/vue-quill |
| 开发端口 | 8081 (代理 `/dev-api` → 后端 9080/prod-api) |
| 默认账号 | admin / admin123 |

### 3.2 目录结构

```
charging-web/src/
├── main.js                      # 入口 (注册 Vue/ElementPlus/Pinia/Router/全局组件/指令)
├── App.vue                      # 根组件
├── permission.js                # 路由守卫 (Token 鉴权 + 动态路由注册)
├── settings.js                  # 布局配置 (主题/侧边栏/tagsView等)
├── systemConfig.js              # 系统名 "铱佰能链充电桩运营系统"
│
├── api/                         # 接口封装 (按业务模块分文件)
│   ├── business/                # 业务: 充电订单
│   ├── customer/                # 客户: 用户/车辆
│   ├── device/                  # 设备: 充电桩/充电站
│   ├── home/                    # 首页仪表盘
│   ├── monitor/                 # 监控: 缓存/任务/在线用户/日志
│   ├── system/                  # 系统管理: 用户/角色/菜单/部门/字典/Banner
│   ├── report/                  # 报表
│   ├── rule/                    # 计费规则
│   └── tool/                    # 代码生成/Swagger
│
├── store/modules/               # Pinia 状态
│   ├── user.js                  # 用户信息/Token/角色
│   ├── permission.js            # 动态路由/权限
│   ├── settings.js              # 系统设置
│   ├── tagsView.js              # 标签页
│   └── dict.js                  # 数据字典
│
├── router/index.js              # 静态路由 + 动态路由模板
│
├── layout/                      # 布局框架 (侧边栏/顶栏/Navbar/TagsView)
│
├── components/                  # 公共组件 (19 个)
│   ├── FileUpload/              # 文件上传
│   ├── ImageUpload/             # 图片上传
│   ├── Editor/                  # 富文本编辑器
│   ├── Pagination/              # 分页
│   ├── RightToolbar/            # 右侧工具栏
│   ├── TreeSelect/              # 树形选择器
│   └── ...                      # 其他通用组件
│
├── directive/                   # 自定义指令
│   └── permission/              # v-hasPermi 按钮权限指令
│
├── utils/
│   ├── request.js               # Axios 封装 (Token/tenant_id/防重复提交)
│   ├── auth.js                  # Token 读写 (js-cookie)
│   ├── ruoyi.js                 # 框架工具函数
│   └── validate.js              # 表单验证
│
└── views/                       # 业务页面 (54 个 .vue)
    ├── index.vue                # 首页仪表盘
    ├── login.vue                # 登录
    ├── business/                # 订单管理
    ├── customer/                # 客户管理
    ├── device/                  # 设备管理 (充电站/桩/实时监控)
    ├── priceRule/               # 计费规则
    ├── report/                  # 报表
    ├── system/                  # 系统管理 (用户/角色/菜单/部门/Banner/字典)
    ├── monitor/                 # 系统监控
    └── tool/                    # 开发工具 (代码生成)
```

### 3.3 架构特点

- **基于 RuoYi-Vue（Vue3 版本）深度定制**
- **动态路由**：登录后调用 `/getRouters` 接口获取菜单树，`store/modules/permission.js` 动态注册路由
- **按钮级权限**：通过 `v-hasPermi` 指令控制按钮显示
- **自动导入**：`unplugin-auto-import` 自动导入 Vue/Router/Pinia API，无需手动 import
- **环境配置**：3 套环境变量，生产构建启用 gzip 压缩
- **请求防重**：POST/PUT 请求 1 秒内不重复提交
- **租户隔离**：请求头注入 `tenant_id`

---

## 开发指南

### 本地开发环境搭建

1. **基础设施**
   ```bash
   # PostgreSQL 14+ (数据库 charge)
   # Redis 6.2+
   # RocketMQ 4.9.4+
   ```

2. **数据库初始化**
   ```bash
   # 按顺序执行
   charging-api/sql/postgresql/db1.sql   # 表结构和初始数据
   charging-api/sql/postgresql/db2.sql   # 重置主键序列
   ```

3. **启动后端 (charging-api)**
   ```bash
   cd charging-api
   # 修改 application.yml 中的数据库/Redis/RocketMQ 连接配置
   # 启动 AdminApplication.java
   ```

4. **启动管理后台 (charging-web)**
   ```bash
   cd charging-web
   npm install  # 或 yarn
   npm run dev  # 开发模式，端口 8081
   ```

5. **启动小程序 (charging-mini)**
   ```bash
   # 用 HBuilderX 打开 charging-mini 目录
   # 修改 static/js/app.js 中的 api 地址指向本地后端
   # 运行 → 微信开发者工具
   ```

### 关键文件速查

| 场景 | 文件 |
|------|------|
| 修改后端端口/数据库 | `charging-api/xingchuan-admin/src/main/resources/application.yml` |
| 新增后端 API | 在 `xingchuan-admin/web/controller/` 创建 Controller → `xingchuan-system/` 创建 Service/Mapper |
| 新增管理后台页面 | `charging-web/src/views/` 创建页面 → `api/` 创建接口 → `router/` 添加路由 |
| 新增小程序页面 | `charging-mini/pages/` 创建页面 → `pages.json` 注册 → `config/api.js` 添加接口 |
| 后端接口签名逻辑 | `charging-mini/config/request.js` + `md5Utils.js` |
| 权限/菜单配置 | 管理后台 → 系统管理 → 菜单管理（在线配置） |
| 微信支付配置 | `charging-api` 环境变量，见 README 部署章节 |

### 代码规范

- Java 代码遵循《阿里巴巴 Java 开发手册》
- 前端代码保持 RuoYi 框架约定命名
- 小程序使用 uni-app 组件规范

---

## 版本说明

- **社区版**：含充电桩管理、计费策略、订单管理、场站管理、用户管理等核心功能
- **标准版**：在社区版基础上增加数据分析、营销工具、运营商管理、运维报表等企业功能
