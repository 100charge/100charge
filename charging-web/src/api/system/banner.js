import request from "@/utils/request"

export default {
  // 查询参数列表
  page(query) {
    return request({
      url: "/banner/getBannerPage",
      method: "get",
      params: query,
    })
  },
  add(query) {
    return request({
      url: "/banner/addBanner",
      method: "post",
      data: query,
    })
  },
  edit(query) {
    return request({
      url: "/banner/updateBanner",
      method: "post",
      data: query,
    })
  },
  changeStatus(query) {
    return request({
      url: "/banner/openBanner",
      method: "post",
      data: query,
    })
  },
  delete(query) {
    return request({
      url: "/banner/delBanner/" + query.id,
      method: "delete",
    })
  },
  detail(query) {
    return request({
      url: "/banner/" + query.id,
      method: "get",
      params: query,
    })
  },
}
