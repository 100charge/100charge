# package-project 使用说明

当你需要“打包项目”或“生成 jar 包”时，使用这个 skill。

## 这个 skill 做什么

它会执行项目打包命令，并明确告诉你最终生成的 jar 文件位置。

## 打包命令

```powershell
cd charging-api
mvn clean package "-Dmaven.test.skip=true"
```

> 在 Windows PowerShell 中，测试跳过参数建议加引号传递，避免被解析器误拆。

或者：

```powershell
cd charging-api\bin
.\package.bat
```

## 构建后 jar 地址

- `charging-api/xingchuan-admin/target/xingchuan-admin.jar`

构建成功后，Maven 会把重打包后的可执行 jar 放在这个位置。

## 备注

- `bin/package.bat` 会回到 `charging-api` 根目录执行 Maven 打包。
- 本次已验证：`cd charging-api; mvn clean package "-Dmaven.test.skip=true"` 能成功完成构建。
- 如果需要启动接口文档，先确认后端已经运行，再打开 Swagger 页面。