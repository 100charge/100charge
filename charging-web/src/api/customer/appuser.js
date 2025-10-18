import request from "@/utils/request"

export default {
  // 查询参数列表
  page(query) {
    return request({
      url: "/appUser/list",
      method: "get",
      params: query,
    })
  },
  add(query) {
    return request({
      url: "/appUser",
      method: "post",
      data: query,
    })
  },
  edit(query) {
    return request({
      url: "/appUser",
      method: "put",
      data: query,
    })
  },

  appUserDisableOrEnable(query) {
    return request({
      url: "/appUser/appUserDisableOrEnable",
      method: "put",
      data: query,
    })
  },

  // 用户余额变动记录 appUserBalance/list
  appUserBalance(query) {
    return request({
      url: "/appUserBalance/list",
      method: "get",
      params: query,
    })
  },
  // 导出-小程序用户
  miniProgramUserExport(query) {
    return request({
      url: "/appUser/miniProgramUserExport",
      method: "post",
      data: query,
      responseType: "blob",
    })
  },
}
