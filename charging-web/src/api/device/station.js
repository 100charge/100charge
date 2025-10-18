import request from "@/utils/request"

// 场站管理-新增
export function addStations(data) {
  return request({
    url: "/stations",
    method: "post",
    data: data,
  })
}
// 场站管理-查询分页
export function getStationsList(params) {
  return request({
    url: "/stations/list",
    method: "get",
    params,
  })
}
// 场站管理-查询详情
export function getStationsDetail(id) {
  return request({
    url: "/stations/" + id,
    method: "get",
  })
}

// 场站管理-修改
export function updateStations(data) {
  return request({
    url: "/stations",
    method: "put",
    data: data,
  })
}

// 场站管理-删除
export function delStations(id) {
  return request({
    url: "/stations/" + id,
    method: "delete",
  })
}

// 场站管理-是否展示状态切换
export function updateShowStatus(data) {
  return request({
    url: "/stations/updateShowStatus",
    method: "put",
    data: data,
  })
}

// 查询当前运营商场站下拉列表
export function getStationListAll(data) {
  return request({
    url: "/stations/getStationList",
    method: "get",
    params: data,
  })
}


//充电实况
export function getRealTimeStatus(data) {
  return request({
    url: "/charging/getRealTimeStatus",
    method: "get",
    params: data,
  })
}

// 场站列表导出二维码的接口
export function generateQRCodeZipForStation(params) {
  return request({
    url: "/stations/generateQRCodeZip",
    method: "get",
    params,
    responseType: "blob",
  })
}


