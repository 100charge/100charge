// src/systemConfig.js
import logo from "@/assets/logo/100logo.png"

const systemName = import.meta.env.VITE_APP_SYSTEM

// 默认系统配置
const defaultConfig = {
  /**
   * logo
   */
  logo: logo,
  /**
   * 默认标题
   */
  defaultTitle: '铱佰能链充电桩管理系统',
  /**
   * login页面底部copyright
   */
  copyright: '<a href="https://beian.mps.gov.cn/#/query/websearch?code=37021302001261" rel="noreferrer" target=" blank">鲁公网安备37021302001261</a>',

  /**
   * 腾讯地图appKey
   */
  qqMapKey: '',
}

// 根据系统名称返回对应配置
const getConfig = () => {
  return defaultConfig
}

export default getConfig()
