import request from "@/utils/request"
export default {
  page(query) {
    return request({
      url: "/appUser/userCarList",
      method: "get",
      params: query,
    })
  },
}
