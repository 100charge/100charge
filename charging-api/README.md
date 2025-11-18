# Charging API - 充电管理系统

## 📋 项目简介

充电桩管理系统后端API服务，基于Spring Boot开发，支持充电桩管理、订单管理、支付集成等功能。

## 🔧 环境变量配置

所有配置项都支持通过环境变量注入，以下是完整的配置参数列表：

### 数据库配置 (Database Configuration)

| 环境变量 | 默认值 | 说明 |
|---------|--------|------|
| `DB_HOST` | `172.24.139.41` | PostgreSQL数据库主机地址 |
| `DB_PORT` | `5432` | PostgreSQL数据库端口 |
| `DB_NAME` | `charge` | 数据库名称 |
| `DB_USER` | `postgres` | 数据库用户名 |
| `DB_PASSWORD` | `postgres` | 数据库密码 |

**示例：**
```bash
-e DB_HOST=172.24.139.41 \
-e DB_PORT=5432 \
-e DB_NAME=charge \
-e DB_USER=postgres \
-e DB_PASSWORD=postgres
```

---

### Redis 配置 (Redis Configuration)

| 环境变量 | 默认值 | 说明 |
|---------|--------|------|
| `REDIS_HOST` | `172.24.139.41` | Redis服务器地址 |
| `REDIS_PORT` | `6379` | Redis服务器端口 |
| `REDIS_DATABASE` | `0` | Redis数据库索引(0-15) |
| `REDIS_PASSWORD` | _(空)_ | Redis密码，留空表示无密码 |

**示例：**
```bash
-e REDIS_HOST=172.24.139.41 \
-e REDIS_PORT=6379 \
-e REDIS_DATABASE=0 \
-e REDIS_PASSWORD=
```

---

### RocketMQ 配置 (Message Queue Configuration)

| 环境变量 | 默认值 | 说明 |
|---------|--------|------|
| `ROCKETMQ_NAME_SERVER` | `172.24.139.41:9876` | RocketMQ NameServer地址 |

**示例：**
```bash
-e ROCKETMQ_NAME_SERVER=172.24.139.41:9876
```

---

### Druid 数据库监控配置 (Druid Monitoring)

| 环境变量 | 默认值 | 说明 |
|---------|--------|------|
| `DRUID_USERNAME` | `admin` | Druid监控页面登录用户名 |
| `DRUID_PASSWORD` | `123456` | Druid监控页面登录密码 |

**访问地址：** `http://your-host:port/druid/`

**示例：**
```bash
-e DRUID_USERNAME=admin \
-e DRUID_PASSWORD=123456
```

---

### 微信小程序配置 

| 环境变量 | 默认值 | 说明 |
|---------|--------|------|
| `WX_APPID` | `appid` | 微信小程序AppID |
| `WX_SECRET` | `secret` | 微信小程序AppSecret |
| `WX_TOKEN` | _(空)_ | 消息服务器配置Token（可选） |
| `WX_AES_KEY` | _(空)_ | 消息加密密钥（可选） |
| `WX_ENV_VERSION` | `trial` | 小程序跳转版本：`dev`(开发版) / `trial`(体验版) / `release`(正式版) |
| `WX_PAGE` | `/pages/index/home` | 小程序跳转页面路径 |
| `WX_START_TEMPLATE` | `w` | 启动充电通知模板ID |
| `WX_STOP_TEMPLATE` | `x` | 停止充电通知模板ID |
| `WX_SEND_MESSAGE_URL` | `https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s` | 发送订阅消息API地址 |

**示例：**
```bash
-e WX_APPID=wx1234567890abcdef \
-e WX_SECRET=your_wechat_secret \
-e WX_ENV_VERSION=release \
-e WX_PAGE=/pages/index/home \
-e WX_START_TEMPLATE=your_start_template_id \
-e WX_STOP_TEMPLATE=your_stop_template_id
```

---

### 微信支付配置

| 环境变量 | 默认值 | 说明 |
|---------|--------|------|
| `PAY_WECHAT_APPID` | `123456789` | 微信支付AppID |
| `PAY_WECHAT_SECRET` | `123456789` | 微信支付AppSecret |
| `PAY_WECHAT_MERCHANT_ID` | `1715546065` | 微信支付商户号 |
| `PAY_WECHAT_PRIVATE_KEY_PATH` | `./apiclient_key.pem` | API证书私钥文件路径 |
| `PAY_WECHAT_SERIAL_NUMBER` | `123456789` | API证书序列号 |
| `PAY_WECHAT_API_V3_KEY` | `123456789` | APIv3密钥（32位字符串） |
| `PAY_WECHAT_PROFIT_SHARING` | `false` | 是否启用分账功能 |
| `PAY_WECHAT_EXPIRE_MINUTE` | `5` | 订单过期时间（分钟） |
| `PAY_WECHAT_RECHARGE_NOTIFY_URL` | `https://wxapi.example.com/prod-api/payNotify/recharge/wechat/` | 充值支付回调通知URL |
| `PAY_WECHAT_REFUND_NOTIFY_URL` | `https://wxapi.example.com/prod-api/payNotify/recharge/wechat/` | 退款回调通知URL |
| `PAY_WECHAT_PROXY_ENABLED` | `false` | 是否启用HTTP代理 |
| `PAY_WECHAT_PROXY_HOST` | `127.0.0.1` | 代理服务器地址 |
| `PAY_WECHAT_PROXY_PORT` | `8213` | 代理服务器端口 |
| `PAY_WECHAT_READ_TIMEOUT` | `5000` | 读取超时时间（毫秒） |
| `PAY_WECHAT_CONNECT_TIMEOUT` | `5000` | 连接超时时间（毫秒） |
| `PAY_WECHAT_WRITE_TIMEOUT` | `5000` | 写入超时时间（毫秒） |

**示例：**
```bash
-e PAY_WECHAT_APPID=wx1234567890abcdef \
-e PAY_WECHAT_SECRET=your_pay_secret \
-e PAY_WECHAT_MERCHANT_ID=1234567890 \
-e PAY_WECHAT_PRIVATE_KEY_PATH=/app/certs/apiclient_key.pem \
-e PAY_WECHAT_SERIAL_NUMBER=ABCDEF1234567890 \
-e PAY_WECHAT_API_V3_KEY=your_32_character_apiv3_key_here \
-e PAY_WECHAT_RECHARGE_NOTIFY_URL=https://yourdomain.com/prod-api/payNotify/recharge/wechat/ \
-e PAY_WECHAT_REFUND_NOTIFY_URL=https://yourdomain.com/prod-api/payNotify/refund/wechat/ \
-e PAY_WECHAT_PROXY_ENABLED=false
```

---

## 📦 完整的 Docker 运行示例

### 方式1: 直接使用 -e 参数

```bash
docker run -d \
  --name charging-api \
  -p 8080:8080 \
  \
    # JVM参数（如需自定义内存、GC等）
  -e JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
  \
  # 数据库配置
  -e DB_HOST=172.24.139.41 \
  -e DB_PORT=5432 \
  -e DB_NAME=charge \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  \
  # Redis配置
  -e REDIS_HOST=172.24.139.41 \
  -e REDIS_PORT=6379 \
  -e REDIS_DATABASE=0 \
  -e REDIS_PASSWORD= \
  \
  # RocketMQ配置
  -e ROCKETMQ_NAME_SERVER=172.24.139.41:9876 \
  \
  # Druid监控
  -e DRUID_USERNAME=admin \
  -e DRUID_PASSWORD=123456 \
  \
  # 微信小程序配置
  -e WX_APPID=wx1234567890abcdef \
  -e WX_SECRET=your_wechat_secret \
  -e WX_ENV_VERSION=release \
  -e WX_PAGE=/pages/index/home \
  -e WX_START_TEMPLATE=start_template_id \
  -e WX_STOP_TEMPLATE=stop_template_id \
  \
  # 微信支付配置
  -e PAY_WECHAT_APPID=wx1234567890abcdef \
  -e PAY_WECHAT_SECRET=your_pay_secret \
  -e PAY_WECHAT_MERCHANT_ID=1234567890 \
  -e PAY_WECHAT_PRIVATE_KEY_PATH=/app/certs/apiclient_key.pem \
  -e PAY_WECHAT_SERIAL_NUMBER=ABCDEF1234567890 \
  -e PAY_WECHAT_API_V3_KEY=your_32_character_apiv3_key \
  -e PAY_WECHAT_RECHARGE_NOTIFY_URL=https://yourdomain.com/prod-api/payNotify/recharge/wechat/ \
  -e PAY_WECHAT_REFUND_NOTIFY_URL=https://yourdomain.com/prod-api/payNotify/refund/wechat/ \
  -e PAY_WECHAT_PROXY_ENABLED=false \
  \
  # 数据卷挂载
  -v ./adminLogs:/app/adminLogs \
  -v ./uploadPath:/app/uploadPath \
  -v ./apiclient_key.pem:/app/certs/apiclient_key.pem:ro \
  \
  100charge/charging-api:latest
```

### 方式2: 使用环境变量文件（推荐）

**步骤1:** 创建 `.docker.env` 文件（参考 `.docker.env`）

**步骤2:** 运行容器
```bash
docker run -d \
  --name charging-api \
  --env-file .docker.env \
  -p 8080:8080 \
  -v ./adminLogs:/app/adminLogs \
  -v ./uploadPath:/app/uploadPath \
  -v ./apiclient_key.pem:/app/certs/apiclient_key.pem:ro \
  100charge/charging-api:latest
```