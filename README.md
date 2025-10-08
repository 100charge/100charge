<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/LOGO.png'></img></p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">100Charge基于若依的充电运营平台</h1>
<p align="center">基于SpringBoot和若依框架开发</p>
<p align="center">充电管理平台，小程序和API接口已上传，其余代码持续上传中</p>
<p align="center"><span style="color: red;">通过本项目，学习者可以掌握充电桩充电行业的功能和业务，以及技术架构。</span></p>

<p align="center">
    <a href='https://gitee.com/ustcyc/100charge/stargazers'><img src='https://gitee.com/ustcyc/100charge/badge/star.svg?theme=dark' alt='star'></img></a>
    <a href='https://gitee.com/ustcyc/100charge/members'><img src='https://gitee.com/ustcyc/100charge/badge/fork.svg?theme=dark' alt='fork'></img></a>
</p>

## 介绍
本充电管理系统聚焦中小充电站场站主核心需求，以 “快速上云、轻量运营” 为核心目标，助力场站主高效开启数字化充电业务。系统整合四大核心功能模块，同时提供充电桩云端接入与微信小程序业务落地的一体化解决方案，具体如下：

##### 一、核心功能模块
1. 充电桩管理：实现对充电桩设备的集中化管控，支持设备状态实时监控、远程参数配置，确保设备稳定运行。
2. 场站管理：覆盖场站基础信息维护、运营状态可视化展示，帮助场站主高效管理线下场地资源，提升场地利用效率。
3. 计费策略：提供灵活可配置的计费规则设置功能，支持按峰谷时段等多维度自定义计费方案，满足不同场景下的定价需求。
4. 订单管理：自动记录充电订单详情（如充电时长、电量、费用等），支持订单查询、统计与导出，实现充电业务流水的清晰化管理。

##### 二、核心服务价值
针对中小充电站场站主 “数字化门槛高、落地周期长” 的痛点，系统提供两大关键支撑：
- 快速设备上云：简化充电桩接入流程，无需复杂技术开发，即可实现设备数据实时上传云端，降低场站数字化改造难度；
- 业务直达用户：同步打通微信小程序端，让充电业务快速触达终端用户，用户可通过小程序完成找桩、预约、充电、支付全流程，助力场站主快速搭建线上服务渠道，提升用户体验与运营效率。

## 开源协议
100Charge是基于 [AGPL](https://www.gnu.org/licenses/agpl-3.0.en.html) 和 [ZTPL](./LICENSE) 双协议的开源软件。  

另，学生学习、老师用于教学，可免费指导

## 不同版本功能对比

| 父模块 | 子模块 | 开源版 | 增强版本 |
|--------------------------|---|---|---|
|首页|首页|√|√|
|订单管理|订单管理|√|√|
|客户运营|个人用户|√|√|
||用户车辆	|√|√|
||企业客户	|×|√|
||车队管理	|×|√|
||卡管理	|×|√|
||白名单管理	|×|√|
|电站运营	|场站管理	|√	|√|
|	|充电桩管理|√|√|
|	|计费策略|√|√|
|财务管理|用户余额变动|√|	√|
|	|企业余额变动|×|√|
|	|结算单管理	|×|√|
|	营销管理	|营销策略	|×	|√|
||优惠券管理|	×|	√|
||券使用记录|	×|	√|
|运营商	|运营商管理|	×|	√|
||运营商分润管理|	×|	√|
|运维管理|	运维报表|	×|	√
||报文管理|	×|	√|
|报表	|多维度分析|	×|	√|
|	|运营报表      |	×|	√|
|	|电站数据报表  |	×|	√|
|	|订单扣费明细  |	×|	√|
|	|账户信息汇总  |	×|	√|
|	|电站运营状况  |	×|	√|
|	|终端利用率	  |	×|	√|
|	|企业收退款报表|	×|	√|
|	|尖峰平谷统计  |	×|	√|
|	|车辆峰谷统计  |	×|	√|

## 软件架构
基于若依Spring Boot单体版本修改的后端架构，若依框架开发文档齐全，社区活跃；
- 数据库： PGSQL 14.10及以上
- 网络框架： Netty
- Redis: Redis6.0以上
- ES（增强版）: 大数据存储
- Minio: 文件存储
- 其他中间件等

#### 项目结构
`
![输入图片说明](image.png)
`

## 小程序截图
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E9%A6%96%E9%A1%B5.jpg'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E9%A6%96%E9%A1%B5-%E6%90%9C%E7%B4%A2.jpg'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E7%94%B5%E7%AB%99%E8%AF%A6%E6%83%85.jpg'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E5%85%85%E7%94%B5%E7%A1%AE%E8%AE%A4.jpg'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E5%85%85%E7%94%B5%E4%B8%AD.jpg'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E5%85%85%E7%94%B5%E4%B8%AD2.jpg'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E6%88%91%E7%9A%84.png'></img></p>
<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/%E8%AE%A2%E5%8D%95%E5%88%97%E8%A1%A8.jpg'></img></p>

## 安装教程
`TODO: 后续完善`

## 使用说明
`TODO: 后续完善`

## 参与贡献

`TODO: 后续完善`
