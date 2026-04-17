---
name: swagger-start
description: 启动并打开 100charge 项目的 Swagger 接口文档。触发词：启动swagger、打开接口文档、查看swagger、系统接口、API文档。
---

# 启动 Swagger

当用户想要启动 Swagger 或打开接口文档时，使用这个 skill。

## 触发词

- 启动swagger
- 打开接口文档
- 查看swagger
- 系统接口
- API文档

## 适用范围

适用于当前仓库的 `charging-api` 后端和 `charging-web` 前端。

## 启动步骤

1. 优先确认后端服务是否已启动。
2. 如果未启动，在 `charging-api` 目录下启动后端服务。
3. 打开 Swagger 地址：`http://127.0.0.1:9080/prod-api/swagger-ui/index.html`
4. 如果前端开发服务已启动，也可以从前端菜单进入“系统接口”。

## 推荐启动方式

Windows PowerShell：

```powershell
cd charging-api
mvn -pl xingchuan-admin -am spring-boot:run
```

如果项目已经打包过，也可以直接运行：

```powershell
cd charging-api
.\start.bat start
```

## 环境要求

- PostgreSQL 可用。
- 至少设置数据库连接信息：`DB_HOST`、`DB_PORT`、`DB_NAME`、`DB_USER`、`DB_PASSWORD`。

## 关键配置

- 后端端口：`9080`
- 后端 context-path：`/prod-api`
- Swagger 开关：`swagger.enabled=true`
- Swagger 前缀：`swagger.pathMapping=/dev-api`

## 输出要求

启动成功后，明确告诉用户 Swagger 的访问地址，并说明如果页面无法打开，优先检查后端是否已运行。