import request from "@/utils/request"

// 充电订单列表分页查询
export function getChargingOrderList(params) {
  return request({
    url: "/chargingOrder/list",
    method: "get",
    params,
  })
}
// 充电订单详情查询
export function getOrderDetail(id) {
  return request({
    url: "/chargingOrder/orderDetail?id=" + id,
    method: "get",
  })
}

// 充电订单统计
export function updateStations(params) {
  return request({
    url: "/chargingOrder/orderStatistics",
    method: "get",
    params,
  })
}

// 充电订单列表导出
export function chargingOrderExport(query) {
  return request({
    url: "/chargingOrder/export",
    method: "post",
    data: query,
    responseType: "blob",
  })
}

// 模拟
// 开始充电：
// /chargingOrder/simulateChargingStart?openId=oNqka7V3BB-txArgyUt4NesVBLFA
export function simulateChargingStart(params) {
  return request({
    url: "/chargingOrder/simulateChargingStart?openId=oNqka7V3BB-txArgyUt4NesVBLFA",
    method: "get",
    params,
  })
}
// 停止充电：
// /chargingOrder/simulateChargingStop?openId=oNqka7V3BB-txArgyUt4NesVBLFA
export function simulateChargingStop(params) {
  return request({
    url: "/chargingOrder/simulateChargingStop?openId=oNqka7V3BB-txArgyUt4NesVBLFA",
    method: "get",
    params,
  })
}
// 上传结算单：
// /chargingOrder/simulateUploadInvoice?openId=oNqka7V3BB-txArgyUt4NesVBLFA
export function simulateUploadInvoice(params) {
  return request({
    url: "/chargingOrder/simulateUploadInvoice?openId=oNqka7V3BB-txArgyUt4NesVBLFA",
    method: "get",
    params,
  })
}

// 设置异常订单
export function markOrderAsAbnormal(params) {
  return request({
    url: "/chargingOrder/markOrderAsAbnormal",
    method: "get",
    params,
  })
}

// 手动结算订单
export function manualSettlement(params) {
  return request({
    url: "/chargingOrder/manualSettlement",
    method: "post",
    data: params,
  })
}