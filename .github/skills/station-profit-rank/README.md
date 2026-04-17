# station-profit-rank 使用说明

当你想查询“哪个场站最赚钱”或查看场站收入排名时，使用这个 skill。

## 触发词

- 赚钱排名
- 场站赚钱排名
- 哪个场站最赚钱
- 场站收入排名
- 场站营收排行
- 场站盈利排行
- 最赚钱场站
- 场站收益排行
- 场站赚钱榜
- 收入最高的场站

## 环境要求

1. 安装 `psql` 或 PostgreSQL 客户端。`psql` 官方下载地址：https://www.postgresql.org/download/
2. 这是一个 Windows 环境 skill，但更推荐直接在对话中一次性提供数据库连接信息，而不是先配置环境变量。

## 对话中提供连接信息

你可以直接按下面这种格式发给我：

```text
DB_HOST=127.0.0.1
DB_PORT=5432
DB_NAME=charge
DB_USER=postgres
DB_PASSWORD=postgres
```

或者直接给我完整连接字符串：

```text
Host=127.0.0.1;Port=5432;Database=charge;Username=postgres;Password=postgres
```

## Windows 环境变量设置

如果你仍然想在本机设置环境变量，可以使用项目默认约定：

### PowerShell 当前会话临时设置

```powershell
$env:DB_HOST="127.0.0.1"
$env:DB_PORT="5432"
$env:DB_NAME="charge"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres"
```

### CMD 当前窗口临时设置

```bat
set DB_HOST=127.0.0.1
set DB_PORT=5432
set DB_NAME=charge
set DB_USER=postgres
set DB_PASSWORD=postgres
```

### PowerShell 永久设置

```powershell
[Environment]::SetEnvironmentVariable("DB_HOST", "127.0.0.1", "User")
[Environment]::SetEnvironmentVariable("DB_PORT", "5432", "User")
[Environment]::SetEnvironmentVariable("DB_NAME", "charge", "User")
[Environment]::SetEnvironmentVariable("DB_USER", "postgres", "User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "postgres", "User")
```

### CMD 永久设置

```bat
setx DB_HOST "127.0.0.1"
setx DB_PORT "5432"
setx DB_NAME "charge"
setx DB_USER "postgres"
setx DB_PASSWORD "postgres"
```

如果已经打开了终端，永久设置后请重新打开终端窗口再执行 skill。

## 使用步骤

1. 在对话中提供数据库连接信息，或者给出完整连接字符串。
2. 运行 skill，它会查询所有已支付订单。
3. 按场站汇总 `station_amount`，得到场站总收入。
4. 按收入从高到低输出排行榜。

## 输出格式

- 以“🏆 场站赚钱排行榜”开头。
- 每一行展示场站名称和总收入，金额保留两位小数。
- 如果没有可用数据，会提示“暂无已支付订单，无法生成场站赚钱排行榜”。

## 备注

- 这个 skill 统计的是已支付订单的场站分账金额，而不是订单总金额。
- 如果数据库连接缺失，先补充连接信息再执行。