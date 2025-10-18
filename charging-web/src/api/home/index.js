import request from "@/utils/request"

export default {
  getStationBusinessTrend(query) {
    return request({
      url: "/home/getStationBusinessTrend",
      method: "get",
      params: query,
    })
  },
  // /queryOperationData
  queryOperationData(query) {
    return request({
      url: "/home/queryOperationData",
      method: "get",
      params: query,
    })
  },
  // /getDashboardOperatorInfo
  getDashboardOperatorInfo(query) {
    return request({
      url: "/home/getDashboardOperatorInfo",
      method: "get",
      params: query,
    })
  },
}
