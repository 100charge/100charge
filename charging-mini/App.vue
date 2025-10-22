<script>
import app from "@/static/js/app.js"
import icon from "uview-ui/libs/config/props/icon"
export default {
  data() {
    return {
      showPrivacy: false,
    }
  },

  onLaunch: function () {
    uni.hideTabBar()
    // 获取当前定位
    console.log("getCurrentLocation：APP.vue")
    uni.getLocation({
      // #ifdef MP-WEIXIN
      type: "gcj02",
      // #endif

      // type: "wgs84",
      success: function (res) {
        console.log("当前位置的经度：" + res.longitude)
        console.log("当前位置的纬度：" + res.latitude)
        uni.setStorageSync("latitude", res.latitude)
        uni.setStorageSync("longitude", res.longitude)
        uni.request({
          url: "https://apis.map.qq.com/ws/geocoder/v1/", // 腾讯
          data: {
            extensions: "all",
            key: "JGABZ-OJUKV-G6MPB-56GIH-3O2D2-SEBUR", // 腾讯
            location: res.latitude + "," + res.longitude,
          },
          success(response) {
            let cityA = response.data.result.address_component.city
            let cityCodeA = response.data.result.ad_info.adcode
            uni.setStorageSync("city", cityA)
            uni.setStorageSync("cityCode", cityCodeA)
          },
          fail(e) {
            console.log("定位失败，默认经纬度为山东济南市", e)
            uni.setStorageSync("latitude", 36.6667)
            uni.setStorageSync("longitude", 116.9949)
            uni.setStorageSync("city", "济南市")
            uni.setStorageSync("cityCode", "370100")
            uni.showToast({
              title: "定位失败，已为您默认定位到 济南市",
              icon: "none",
            })
          },
        })
      },
      fail(e) {
        console.log("当前位置fail：", JSON.stringify(e))
        uni.setStorageSync("latitude", 36.6667)
        uni.setStorageSync("longitude", 116.9949)
        uni.setStorageSync("city", "济南市")
        uni.setStorageSync("cityCode", "370100")
        uni.showToast({
          title: "定位失败，已为您默认定位到 济南市",
          icon: "none",
        })
      },
    })

    // 获取小程序版本更新信息
    const updateManager = wx.getUpdateManager()
    updateManager.onCheckForUpdate(function (res) {
      // 请求完新版本信息的回调
      console.log("hasUpdate", res.hasUpdate)
    })
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: "更新提示",
        content: "新版本已经准备好，是否重启应用？",
        showCancel: false,
        success: function (res) {
          if (res.confirm) {
            // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
            updateManager.applyUpdate()
          }
        },
      })
    })
    updateManager.onUpdateFailed(function () {
      // 新版本下载失败
    })
  },
  onShow: function () {
    console.log("App Show")
  },
  onHide: function () {
    console.log("App Hide")
  },
}
</script>

<style lang="scss">
@import "/uni.scss";
image {
  height: auto;
  width: auto;
}

page {
  background-color: #f4f4f4;
}

view {
  box-sizing: border-box !important;
}
/*  #ifdef MP-WEIXIN  */
[v-cloak] {
  display: none;
}

/*  #endif  */
</style>
