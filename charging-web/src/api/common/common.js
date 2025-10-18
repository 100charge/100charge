import request from '@/utils/request'

// 获取省市区下拉列表
export function getAllAreaList() {
  return request({
    url: '/area/getDropdownList',
    method: 'get'
  })
}

// 获取省下拉列表
export function getProvinceList() {
  return request({
    url: '/area/provinceList',
    method: 'get'
  })
}

// 根据省级id获取市级下拉列表
export function getCityListByProvinceId(params) {
  return request({
    url: '/area/getCityListByProvinceId',
    method: 'get',
    params
  })
}

// 根据市级id获取区级下拉列表
export function getDistrictListByCityId(params) {
  return request({
    url: '/area/getDistrictListByCityId',
    method: 'get',
    params
  })
}

// 查询计费规则列表
export function getRuleList(params) {
  return request({
    url: '/rule/list',
    method: 'get',
    params
  })
}




