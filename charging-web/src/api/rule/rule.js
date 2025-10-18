import request from "@/utils/request"

export default {
  // 查询参数列表
  pageRule(query) {
    return request({
      url: "/rule/list",
      method: "get",
      params: query,
    })
  },
  addRule(query) {
    return request({
      url: "/rule/addRule",
      method: "post",
      data: query,
    })
  },
  editRule(query) {
    return request({
      url: "/rule/updateRule",
      method: "post",
      data: query,
    })
  },
  delRule(query) {
    return request({
      url: "/rule/delRule/" + query.id,
      method: "delete",
      params: query,
    })
  },
  getRuleDetail(query) {
    return request({
      url: "/rule/getRuleDetail",
      method: "get",
      params: query,
    })
  },
  // 新增计费策略时段
  addRuleTime(query) {
    return request({
      url: "/rule/addRuleTime",
      method: "post",
      data: query,
    })
  },
  // 删除计费策略时段
  delRuleTime(query) {
    return request({
      url: "/rule/delRuleTime/" + query.id,
      method: "delete",
      params: query,
    })
  },
  // 获取计费规则时段详情
  getRuleTimeDetail(query) {
    return request({
      url: "/rule/getRuleTimeDetail",
      method: "get",
      params: query,
    })
  },
  // 查询计费规则时段列表
  listRuleTimeByRuleId(query) {
    return request({
      url: "/rule/listRuleTimeByRuleId",
      method: "get",
      params: query,
    })
  },
  // 修改计费策略时段
  updateRuleTime(query) {
    return request({
      url: "/rule/updateRuleTime",
      method: "post",
      data: query,
    })
  },
}
