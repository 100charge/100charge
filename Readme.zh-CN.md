<p align="center"><img src='https://gitee.com/ustcyc/100charge/raw/master/images/LOGO.png'  height="100" width="231"></img></p>
<p align="center" style="margin: 30px 0 30px; font-weight: bold;font-size: larger;">100Charge基于若依的充电运营平台</h1>
<p align="center">基于SpringBoot和若依框架开发</p>

<p align="center">
    <a href='https://gitee.com/ustcyc/100charge/stargazers'><img src='https://gitee.com/ustcyc/100charge/badge/star.svg?theme=dark' alt='star'></img></a>
    <a href='https://gitee.com/ustcyc/100charge/members'><img src='https://gitee.com/ustcyc/100charge/badge/fork.svg?theme=dark' alt='fork'></img></a>
</p>

# 一 项目背景

    我们团队在新能源充电领域深耕多年，深刻理解中小型充电场站在运营中面临的核心需求与实际痛点。为此，团队决定将多年积累的行业经验与技术成果开源，通过共享力量，帮助广大充电站站长降本增效，轻松应对日常运营挑战。同时也为准备进入该领域的创业者，提供一套经过实际验证过的解决方案，助力业务快速落地与商业验证。

## 1.项目价值

    针对中小充电站场站主 “数字化门槛高、落地周期长、技术门槛高” 的痛点，开源系统提供两大关键支撑：

* **快速接入充电桩**：无需技术开发即可轻松接入主流品牌充电桩，还可以实现设备数据轻松上传云端，降低场站数字化改造难度以及实施成本；
* **业务直达用户**：支持微信小程序端，让充电业务快速触达终端用户，用户可通过小程序完成找桩、预约、充电、支付全流程，助力场站主快速搭建线上服务渠道，提升用户体验与运营效率。
  
  

## 2.功能介绍

    **100Charge**充电运营系统以 “快速落地、轻松上云、轻量运营”为核心目标。系统具有四大核心功能模块，具体如下：

* **充电桩管理**：实现对充电桩设备的集中化管控，支持设备状态实时监控、远程参数配置，确保设备稳定运行，轻松管理。

* **场站管理**：覆盖场站基础信息维护、运营状态可视化展示，帮助场站主高效管理线下场地资源，提升场地利用效率。

* **计费策略**：提供灵活可配置的计费规则设置功能，支持按峰谷时段等多维度自定义计费方案，满足不同场景下的定价需求。

* **订单管理**：自动记录充电订单详情（如充电时长、电量、费用等），支持订单查询、统计与导出，实现充电业务流水的清晰化管理
  
  ## 

## 2.小程序截图

<div>
<img height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/home.jpg' alt="首页">
<img  height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/search.jpg' alt="搜索">
<img  height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/charging-confirm.jpg' alt="充电确认">
<img  height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/charging.jpg' alt="充电中">
<img  height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/my.png' alt="我的">
<img  height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/orders.jpg' alt="订单列表">
<img  height="600px" style="display: inline-block; margin: 5px;" src='https://gitee.com/ustcyc/100charge/raw/master/images/station-detail.jpg' alt="详情">
</div>

## 3.运营系统截图

### 3.1 社区版

### 3.2 商业版

# 二 版本对比

    我们提供两种版本，以满足不同阶段和规模的用户选择：

**社区版**

    如果您想快速推进业务落地，社区版是适合的选择，它包含的四项核心功能足以满足您的需求。社区版包含了稳定可靠的充电桩管理，计费策略，订单管理。助力您快速低成本开启充电场站运营。

**商业版**

    如果你要想要更精细化的运营与管理，商业版将是您的最佳选择。在**社区版**的基础上，额外提供了企业级的高阶运营功能，比如多维度的数据分析，营销工具等。并且提供官方技术保障与支持，满足复杂的商业场景。

**版本对比**

| 父模块  | 子模块     | 社区版 | 商业版 |
| ---- | ------- | --- | --- |
| 首页   | 首页      | √   | √   |
| 订单管理 | 订单管理    | √   | √   |
| 客户运营 | 个人用户    | √   | √   |
|      | 用户车辆    | √   | √   |
|      | 企业客户    | ×   | √   |
|      | 车队管理    | ×   | √   |
|      | 卡管理     | ×   | √   |
|      | 白名单管理   | ×   | √   |
| 电站运营 | 场站管理    | √   | √   |
|      | 充电桩管理   | √   | √   |
|      | 计费策略    | √   | √   |
| 财务管理 | 用户余额变动  | √   | √   |
|      | 企业余额变动  | ×   | √   |
|      | 结算单管理   | ×   | √   |
| 营销管理 | 营销策略    | ×   | √   |
|      | 优惠券管理   | ×   | √   |
|      | 券使用记录   | ×   | √   |
| 运营商  | 运营商管理   | ×   | √   |
|      | 运营商分润管理 | ×   | √   |
| 运维管理 | 运维报表    | ×   | √   |
|      | 报文管理    | ×   | √   |
| 报表   | 多维度分析   | ×   | √   |
|      | 运营报表    | ×   | √   |
|      | 电站数据报表  | ×   | √   |
|      | 订单扣费明细  | ×   | √   |
|      | 账户信息汇总  | ×   | √   |
|      | 电站运营状况  | ×   | √   |
|      | 终端利用率   | ×   | √   |
|      | 企业收退款报表 | ×   | √   |
|      | 尖峰平谷统计  | ×   | √   |
|      | 车辆峰谷统计  | ×   | √   |



# 三 演示系统

|     | 地址   |
| --- | ---- |
| 社区版 | TODO |
| 商业版 | TODO |

# 四 开发环境

## 1 项目结构



## 2 前端

### 2.1 运营系统

### 2.2 小程序

## 3 后端

# 五 运维环境

# 六 安装及部署

# 七 版权须知

    本软件项目（包括但不限于源代码、文档、图片、配置等所有构成项目的材料）的原始版权和知识产权归 **[山东行川新能源科技有限公司]** 所有。我们保留对本项目名称、品牌、Logo、专利以及商业许可的所有权利。未经明确授权，任何个人或组织不得使用与本项目相关的名称、标识进行商业性宣传或推广。



# 八 贡献代码

## 1. 如何提交代码

    良好的团队协作的前提是，有一个清晰的代码规范，便于其他人理解。在您提交代码前，请您熟悉[《阿里巴巴Java开发手册》](https://developer.aliyun.com/ebook/386)的代码规范

## 2. 如何提交缺陷反馈

* 您已搜索过现有Issues，确保没有重复问题

* 您已确认这不是我本地环境导致的问题

* 您已提供足够的信息供开发人员复现问题

* 提交时，您已正确选择标签：bug

## 3. 如何提交需求

* 您已搜索过现有需求，确保没有重复需求

* 我已详细描述需求的背景，如我作为一个运营人员，我想要看到每日订单高峰时间段，并在该时间设置合适的价格。

* 我理解开源团队资源有限，会耐心等待评估

* 提交时，您已正确选择标签：feature

# 九 开源协议

    **100Charge**是基于 [AGPL](https://www.gnu.org/licenses/agpl-3.0.en.html) 和 [XCPL](./LICENSE) 双协议的开源软件。您在使用、修改、复制、分发本项目时，仔细阅读并理解以下该协议条款。

**如果您属于以下情况，请遵循[AGPL](https://www.gnu.org/licenses/agpl-3.0.en.html)协议**

* 您是**个人开发者、研究机构或教育机构**，希望技术学习、研究或非商业性地使用本项目。
* 您希望基于本项目进行**修改或扩展，并愿意将您的修改成果同样开源**给社区。

如果您是学生或者教师，用于技术学习或教学目的，可联系我们，我们将免费提供培训与指导（远程）



**如果您属于以下情况，需要获得[XCPL](./LICENSE)协议许可**

* 您基于本项目的修改版本**用于商业目的，但不希望公开您的项目代码**。

* 您在一个**内部使用的系统**中集成了本项目，只对组织内部使用，**不**打算将其对外提供服务。

* 您计划将本项目**打包成SaaS产品或云服务进行销售**。

* 您需要在**专有/闭源产品中集成、修改或链接**本项目的代码。

* 您需要自行部署系统并运营，对外部用户提供收费服务。

**如何获得[XCPL](./LICENSE)协议许可？**

    您需要通过官方渠道，包括不限于：微信、QQ群等与我们取得联系，这样确保您秉持开源精神同时，获得商业许可的自由，并能得到我们官方的技术支持



# 十 联系我们

**QQ群**，如果您只是技术学习与咨询等，请加入我们的**QQ群**，入群请填写：100+姓名

<img src="./images/98bafeed-b960-4d95-adf5-303c13c96d48.png" title="" alt="98bafeed-b960-4d95-adf5-303c13c96d48" data-align="center">

**微信扫码**，如果您要想咨询我们的商业授权，以及培训等事宜，请优先加微信，请备注：100+姓名

<img src="./images/fae88e40-6921-4b36-a24b-ab1397ee86bc.png" title="" alt="fae88e40-6921-4b36-a24b-ab1397ee86bc" data-align="center">




