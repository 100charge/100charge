---
name: station-profit-rank
description: 所有场站赚钱排名。按场站总收入从高到低排序。触发词：赚钱排名、场站赚钱排名、哪个场站最赚钱、场站收入排名、场站营收排行、场站盈利排行、最赚钱场站、场站收益排行
---

# 所有场站赚钱排名

## 执行步骤

### 步骤1：获取数据库连接

优先让用户在对话中一次性提供数据库连接信息（host、port、dbname、user、password），或者直接提供完整连接字符串。如果用户未提供,则从环境变量中读取连接信息。如果连接信息不完整，先向用户补齐后再继续执行。
### 步骤2：执行SQL查询

使用 PostgreSQL 语法：

```sql
SELECT
        co.station_id,
        co.station_name,
        COALESCE(ROUND(SUM(co.total_amount)::numeric, 2), 0) AS total_revenue
FROM charging_order co
WHERE co.pay_status = 1
    AND co.del_flag = '0'
GROUP BY co.station_id, co.station_name
ORDER BY total_revenue DESC;
```

如果查询结果为空，输出“暂无已支付订单，无法生成场站赚钱排行榜”。

### 步骤3：输出排名报告

按以下格式输出：

🏆 场站赚钱排行榜

1. {第一名场站} - {总收入}元
2. {第二名场站} - {总收入}元
3. {第三名场站} - {总收入}元

...（列出所有）

💡 冠军：{第一名场站}，总收入{总收入}元

金额统一保留两位小数。