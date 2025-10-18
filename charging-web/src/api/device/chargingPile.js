import request from "@/utils/request"

// 新增充电枪
export function addChargingGun(id) {
  return request({
    url: "/chargingPile/addChargingGun/" + id,
    method: "post",
  })
}
// 新增充电桩
export function addChargingPile(data) {
  return request({
    url: "/chargingPile/addChargingPile",
    method: "post",
    data: data,
  })
}
// 分页查询充电桩列表
export function getChargingPileList(params) {
  return request({
    url: "/chargingPile/list",
    method: "get",
    params,
  })
}
// 获取充电桩详情
export function getChargingPileDetail(id) {
  return request({
    url: "/chargingPile/getChargingPileDetail?id=" + id,
    method: "get",
  })
}

// 充电桩-修改启用、停用
export function updatePileStatus(id) {
  return request({
    url: "/chargingPile/updatePileStatus/" + id,
    method: "get",
  })
}

// 修改充电桩
export function updateChargingPile(data) {
  return request({
    url: "/chargingPile/updateChargingPile",
    method: "post",
    data: data,
  })
}

// 删除充电枪
export function delChargingGun(id) {
  return request({
    url: "/chargingPile/delChargingGun/" + id,
    method: "delete",
  })
}
// 删除充电桩
export function delChargingPile(id) {
  return request({
    url: "/chargingPile/delChargingPile/" + id,
    method: "delete",
  })
}

// 生成二维码 ”桩号_枪号“拼接
export function generateQRCode(params) {
  return request({
    url: "/chargingPile/generateQRCode",
    method: "get",
    params,
    responseType: "blob",
  })
}

// 传入桩号：下载所有枪号二维码
export function generateQRCodeZip(params) {
  return request({
    url: "/chargingPile/generateQRCodeZip",
    method: "get",
    params,
    responseType: "blob",
  })
}
