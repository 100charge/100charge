import request from "@/utils/request"

export default {
  // 账户充值明细
  accountRechargeDetailList(query) {
    return request({
      url: "/reportForm/accountRechargeDetailList",
      method: "get",
      params: query,
    })
  },
}
