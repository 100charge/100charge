# swagger-start 使用说明

当你需要“启动 Swagger”或“打开接口文档”时，使用这个 skill。

## 这个 skill 做什么

它会先确认 `charging-api` 后端是否已经运行，然后引导你打开当前项目的 Swagger 页面。

## 访问地址

- 后端 Swagger 地址：`http://127.0.0.1:9080/prod-api/swagger-ui/index.html`

## 常用启动命令

```powershell
cd charging-api
mvn -pl xingchuan-admin -am spring-boot:run
```

或者：

```powershell
cd charging-api
.\start.bat start
```

## 备注

- 后端默认端口是 `9080`。
- 项目中 Swagger 已开启，相关配置在 `charging-api/xingchuan-admin/src/main/resources/application.yml`。
- 如果打开失败，先检查数据库连接是否可用，再确认后端进程是否已启动。