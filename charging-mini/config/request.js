import app from "@/static/js/app.js"
import md5Utils from "@/config/md5Utils.js"
// 此vm参数为页面的实例，可以通过它引用vuex中的变量
module.exports = (vm) => {
  // 初始化请求配置
  uni.$u.http.setConfig((config) => {
    /* config 为默认全局配置*/
    config.baseURL = app.api /* 根域名 */
    return config
  })

  // 请求拦截
  uni.$u.http.interceptors.request.use(
    (config) => {
      // 可使用async await 做异步操作
      // 初始化请求拦截器时，会执行此方法，此时data为undefined，赋予默认{}
      let noLoadingUrls = [
        "/chargingOrder/getOrderInfoByTradeNo",
        "/chargingOrder/orderDetail",
        "/realtime-data/status",
      ]
      if (!noLoadingUrls.includes(config.url)) {
        uni.showLoading({
          title: "加载中",
        })
      }

      // #ifdef  APP-PLUS
      config.header.platform = "APP"
      // #endif
      // #ifdef  MP-WEIXIN
      config.header.platform = "MP-WEIXIN"
      // #endif
      const timestamp = new Date().getTime()
      config.params.timestamp = timestamp
      const sign = md5Utils.getSign(config.url, config.params)
      config.header.sign = sign
      config.header.timestamp = timestamp
      config.header.Authorization = "Bearer " + uni.getStorageSync("token")
      config.data = config.data || {}
      // // 根据custom参数中配置的是否需要token，添加对应的请求头
      // if (config?.custom?.auth) {
      //   // 可以在此通过vm引用vuex中的变量，具体值在vm.$store.state中
      //   config.header.token = vm.$store.state.userInfo.token
      // }
      return config
    },
    (config) => {
      // 可使用async await 做异步操作
      return Promise.reject(config)
    }
  )

  // 响应拦截
  uni.$u.http.interceptors.response.use(
    (response) => {
      /* 对响应成功做点什么 可使用async await 做异步操作*/
      const data = response.data
      let apiUrl = response.config.url
      console.log("apiUrl", apiUrl)
      if (data.code !== 200) {
        if (data.code === 401) {
          uni.clearStorageSync()
          let openId = uni.getStorageSync("openid") || ""
          uni.setStorageSync("token", "")
          uni.hideLoading()
          let noLoginTipUrls = [
            "/area/getOpenCities",
            "/dictData/getDictDataByType",
            "/banner/getBannerList",
            "/chargingOrder/getPendingPaymentOrder",
            "/marketingStrategy/getUserMarketingStrategy",
            "/appUser/listAppUserCar",
            "/stations/readyCharge",
          ]
          if (!noLoginTipUrls.includes(apiUrl)) {
            uni.showModal({
              title: openId ? "提示" : "登录提示",
              content: openId ? "认证失败，请重新登录" : "您还未登录账号，体验更多功能请登录",
              confirmText: "去登录",
              cancelText: "暂不登录",
              cancelColor: "#666",
              success: (result) => {
                if (result.confirm) {
                  // 清除openId
                  uni.setStorageSync("openid", "")
                  uni.clearStorageSync()
                  uni.navigateTo({
                    url: "/pages/login/login?fromPage=" + (apiUrl == "/appUser/getUserInfo" ? "previewCharge" : ""),
                  })
                }
              },
            })
          }
        } else {
          if (apiUrl != "/stations/readyCharge") {
            uni.showToast({
              title: data.msg,
              icon: "none",
            })
          }
        }
        return Promise.reject(response)
      } else {
        uni.hideLoading()
        return data === undefined ? {} : data
      }
    },
    (resp) => {
      // 对响应错误做点什么 （statusCode !== 200）
      // if (resp.statusCode === 401) {
      //   uni.showToast({
      //     title: "登录失效，请重新登录",
      //     icon: "none",
      //   })
      //   setTimeout(() => {
      //     uni.navigateTo({
      //       url: "/pages/login/login",
      //     })
      //   }, 1000)
      //   return false
      // } else {
      //   return Promise.reject(resp)
      // }
      if (resp.statusCode === 401) {
        // 清除openId
        uni.setStorageSync("openid", "")
        uni.clearStorageSync()
      } else {
        uni.showToast({
          title: resp.data.msg || '',
          icon: "none",
        })
      }
      console.log("resp", resp)
      setTimeout(function () {
        uni.hideLoading()
      }, 2000);
      return Promise.reject(resp)
    }
  )
}
