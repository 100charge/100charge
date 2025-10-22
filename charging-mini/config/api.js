const http = uni.$u.http;
// 微信登录
export const appletWeChatLogin = (params) => http.post("/wxQuickLogin", params);
// 支付宝登录
export const appletAlipayLogin = (params) =>
    http.post("/aLiQuickLogin", params);
// 绑定手机号
export const appletBindMobile = (params) =>
    http.get("/appletBindMobile", { params });
export const alipayBindMobile = (params) =>
    http.get("/alipayBindMobile", { params });
// 账号密码登录
export const accountLogin = (params) => http.post("/accountLogin", params);
// 获取字典值
export const getDictDataByType = (params) =>
    http.get("/dictData/getDictDataByType", { params });
// 首页-获取开放城市
export const getOpenCities = (params) =>
    http.get("/area/getOpenCities", { params });
// 首页-获取banner
export const getBannerList = (params) =>
    http.get("/banner/getBannerList", { params });
// 首页-获取未支付订单
export const getPendingPaymentOrder = (params) =>
    http.get("/chargingOrder/getPendingPaymentOrder", { params });
// 分页场站
export const pageStation = (params) => http.get("/stations/list", { params });
export const listMapStation = (params) =>
    http.get("/stations/listMapStation", { params });
// 场站管理-查询详情
export const stationDetail = (params) =>
    http.get("/stations/getDetailById", { params });
// 场站管理-根据场站id查询电桩信息
export const getStationDevice = (params) =>
    http.get("/stations/getStationDevice", { params });
// 场站管理-根据场站id查询计费费用列表
export const getStationRuleById = (params) =>
    http.get("/stations/getStationRuleById", { params });
// 场站管理-扫码确定充电枪
export const readyChargeInfo = (params) =>
    http.get("/stations/readyCharge", { params });
// 获取订单可用优惠券
export const getAvailableCouponsForOrders = (params) =>
    http.get("/appUserPromotion/getAvailableCouponsForOrders", { params });
// 计算使用优惠券后的订单金额
export const calculationRealPayAmount = (params) =>
    http.get("/chargingOrder/calculationRealPayAmount", { params });
// 充电
// 启动充电
export const startCharging = (params) =>
    http.get("/charging/startCharging", { params });
// 发送启动成功消息
export const onStartupSuccess = (params) =>
    http.get("/charging/onStartupSuccess", { params });
// 启动-轮训查询是否启动成功
export const getOrderInfoByTradeNo = (params) =>
    http.get("/chargingOrder/getOrderInfoByTradeNo", { params });
// /realtime-data/{operatorId}/{deviceNo}/{gunNo}
export const getRealtimeData = (params) =>
    http.get("/realtime-data/status", { params });
// 结束充电
export const endCharge = (params) =>
    http.get("/charging/endCharge", { params });
// 支付
export const orderPayment = (params) =>
    http.get("/charging/orderPayment", { params });
// ---------------------------------------------以上------------
// 我的
// 我的-新增车辆
export const addCar = (params) => http.post("/appUser/addCar", params);
// 我的-修改车辆
export const editPlateNo = (params) =>
    http.post("/appUser/editPlateNo", params);
export const deleteCar = (params) =>
    http.get("/appUser/deleteById", { params });
// 我的-车列表
export const getUserCarList = (params) =>
    http.get("/appUser/listAppUserCar", { params });
// 我的-车辆认证
export const vehicleCertification = (params) =>
    http.post("/appUser/vehicleCertification", params);
// 我的-车辆ocr
export const ocrRecognition = (params) =>
    http.post("/appUser/ocrRecognition", params);
// 我的-获取用户余额
export const getUserBalance = (params) =>
    http.get("/appUser/getUserBalance", { params });
// 我的-获取用户信息
export const getUserInfo = (params) =>
    http.get("/appUser/getUserInfo", { params });
// 我的-订单列表
export const getChargingOrderList = (params) =>
    http.get("/chargingOrder/list", { params });
export const getOrderDetail = (params) =>
    http.get("/chargingOrder/orderDetail", { params });
// 评价-根据订单查看评价
export const getEvaluationByOrder = (params) =>
    http.get("/stationEvaluation/getEvaluationByOrder", { params });
// 评价-提交评价
export const submitEvaluationByOrder = (params) =>
    http.post("/stationEvaluation/evaluation", params);
// 评价-查看场站评价
export const getEvaluationByStationId = (params) =>
    http.get("/stationEvaluation/getEvaluation", { params });
// 系统参数-根据key查询配置信息
export const getConfigKey = (params) =>
    http.get("/system/config/configKey/" + params);
// 我的-优惠券列表
export const appUserPromotionList = (params) =>
    http.get("/appUserPromotion/list", { params });
// 通过充值金额获取优惠信息
export const showPromotionInfo = (params) =>
    http.get("/appUser/showPromotionInfo", { params });
export const preRechargeAlipay = (params) =>
    http.get("/appUser/aLiUserRecharge", { params });
export const preRechargeWX = (params) =>
    http.get("/appUser/wxUserRecharge", { params });
// 提现申请
export const userWithdrawal = (params) =>
    http.get("/appUser/userWithdrawal", { params });
// 提现申请记录
export const getWithdrawalApplicationPage = (params) =>
    http.get("/appUser/getWithdrawalApplicationPage", { params });
// 获取文章
export const getNoticeDetail = (params) =>
    http.get("/banner/getNoticeDetail", { params });
// 获取活动
export const getUserMarketingStrategy = (params) =>
    http.get("/marketingStrategy/getUserMarketingStrategy", { params });
export const getUserMarketingStrategyById = (params) =>
    http.get("/marketingStrategy/getUserMarketingStrategyById", { params });
export const claimCoupon = (params) =>
    http.get("/marketingStrategy/claimCoupon", { params });
