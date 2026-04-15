# 100Charge 充电小程序

> 基于 **uni-app (Vue 2.0)** 开发的微信小程序，提供电动汽车充电桩查找、扫码充电、订单管理等一站式服务。

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | uni-app (Vue 2.0) |
| UI 组件库 | uview-ui ^2.0.36 |
| 图表 | echarts ^4.2.1 + echarts-liquidfill ^2.0.6 |
| 工具 | js-md5 ^0.8.3 |
| 样式预处理 | Sass |
| 目标平台 | 微信小程序 |

## 功能模块

### 🏠 首页
- 地图模式展示附近充电站（`home-map` 组件）
- 列表模式浏览场站卡片，支持吸顶搜索筛选
- Banner 轮播展示充电优惠活动
- 显示未支付待处理订单提醒

### 📷 扫码充电
- 扫描充电桩二维码，快速识别充电枪信息
- 手动输入设备编号作为备用方式
- 跳转充电确认页，支持选择优惠券并计算实付金额

### ⚡ 充电流程
- **充电确认**：展示计费规则、可用优惠券、预估费用
- **充电中**：实时轮询充电状态与用电数据，倒计时显示
- **结束充电**：生成订单并跳转支付

### 📋 订单管理
- 充电订单列表（分页加载，支持下拉刷新）
- 订单详情：用电量、费用明细、充电时长

### 👤 个人中心
- 微信一键授权登录 / 手机号绑定
- 账户余额查询、余额充值、余额提现
- 优惠券列表管理
- 我的车辆：新增 / 修改 / 删除车牌信息
- 客服与帮助文章入口

## 项目结构

```
charging-mini/
├── config/
│   ├── api.js              # 全部 API 接口定义
│   ├── request.js          # uview-ui HTTP 拦截器（Token / Loading）
│   └── md5Utils.js         # 请求签名工具
├── components/
│   ├── coupon-item/        # 优惠券卡片组件
│   ├── home-list/          # 首页场站列表组件
│   ├── home-map/           # 首页地图组件
│   ├── home-station-card/  # 场站信息卡片
│   ├── home-sticky-search/ # 吸顶搜索栏组件
│   └── tabbar/             # 自定义底部导航
├── pages/
│   ├── index/
│   │   ├── index.vue       # 首页（地图 + 列表）
│   │   ├── start.vue       # 启动页
│   │   └── wode.vue        # 个人中心主页
│   ├── login/
│   │   └── login.vue       # 登录页
│   ├── scan/
│   │   └── scan.vue        # 扫码页
│   ├── stations/           # 分包：场站相关
│   │   ├── orders/
│   │   │   ├── orderList.vue    # 订单列表
│   │   │   └── orderDetail.vue  # 订单详情
│   │   └── site/
│   │       ├── stationDetail.vue   # 电站详情
│   │       ├── previewCharge.vue   # 充电确认
│   │       └── charging.vue        # 充电中
│   └── wode/               # 分包：个人中心子页
│       ├── car/            # 车辆管理
│       ├── recharge/       # 充值 / 提现 / 优惠券
│       └── extend/         # 客服 / 文章
├── static/
│   └── js/app.js           # 全局配置（API BaseURL 等）
├── utils/
│   └── mixin.js            # 全局混入
├── wxcomponents/
│   └── privacy/            # 微信隐私协议组件
├── App.vue                 # 应用根组件
├── main.js                 # 入口文件
├── manifest.json           # 应用配置（AppID、权限等）
├── pages.json              # 页面路由与分包配置
└── uni.scss                # 全局样式变量
```

## 快速开始

### 前置条件

- [HBuilderX](https://www.dcloud.io/hbuilderx.html) 最新稳定版
- [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html) 最新版
- 在[微信公众平台](https://mp.weixin.qq.com/)注册小程序并获取 AppID
- Node.js >= 14

### 安装依赖

```shell
npm install
```

> uview-ui 的使用方式及详细配置请参考：[uview-ui 安装文档](https://www.uviewui.com/components/install.html)

### 配置项目

1. 打开 `manifest.json`，将 `appid` 替换为自己的微信小程序 AppID
2. 修改 `static/js/app.js` 中的后端 API 地址：

```js
// static/js/app.js
const app = {
  api: 'https://your-api-domain.com'  // 替换为实际后端地址
}
export default app
```

### 运行

在 HBuilderX 中：

```
运行 → 运行到小程序模拟器 → 微信开发者工具
```

如遇微信开发者工具无法自动启动，请在微信开发者工具中手动选择 **导入项目**，目录指向本项目根目录。

### 发布

```
发行 → 小程序-微信（仅微信）
```

## 注意事项

1. **扫码功能**：需在真机上测试，模拟器不支持摄像头调用
2. **地图功能**：依赖微信内置地图，需在 `manifest.json` 中确认地图相关权限
3. **支付功能**：需在微信公众平台完成支付开通并配置支付密钥
4. **网络请求**：后端接口域名需在微信公众平台 **服务器域名** 中添加白名单

## 许可证

© 2024 100Charge充电. All rights reserved.
