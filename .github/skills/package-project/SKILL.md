---
name: package-project
description: 打包 100charge 项目并输出构建后的 jar 包地址。触发词：打包项目、构建项目、生成jar包、package、build jar。
---

# 打包项目

当用户想要打包项目、生成 jar 包，或者查看构建后的产物位置时，使用这个 skill。

## 触发词

- 打包项目
- 构建项目
- 生成jar包
- package
- build jar
- 导出jar包

## 适用范围

适用于当前仓库的 `charging-api` 后端项目。

## 打包步骤

1. 进入 `charging-api` 目录。
2. 执行 Maven 打包命令。
3. 构建完成后，输出 runnable jar 包地址。

## 推荐命令

Windows PowerShell：

```powershell
cd charging-api
mvn clean package "-Dmaven.test.skip=true"
```

> 说明：在 Windows PowerShell 中，`-Dmaven.test.skip=true` 建议加引号传递，避免被错误拆分成独立生命周期参数。

如果你想直接用仓库自带的 Windows 打包脚本，也可以运行：

```powershell
cd charging-api\bin
.\package.bat
```

## 构建产物

构建后的可运行 jar 包地址是：

```text
charging-api/xingchuan-admin/target/xingchuan-admin.jar
```

实际构建后，Maven 会在 `charging-api/xingchuan-admin/target/` 下生成并重打包这个 jar。

## 备注

- `charging-api/bin/package.bat` 的默认行为就是在 `charging-api` 根目录执行 `mvn clean package -Dmaven.test.skip=true`。
- 这个项目的主运行模块是 `xingchuan-admin`，所以最终 jar 包位于该模块的 `target` 目录。
- 本次已验证：在 `charging-api` 根目录执行 `mvn clean package "-Dmaven.test.skip=true"` 可以成功打包。
- 如果构建失败，优先检查数据库连接、依赖下载和 Java/Maven 环境。