<template>
  <view>
    <view id="tabbar-container" class="tabbar-container" :class="isIpx ? 'IpxBot' : ''">
      <view
        class="tabbar-item"
        v-for="(item, index) in tabList"
        :class="item.centerItem ? 'center-item' : ''"
        @click="changeItem(item)"
        :key="index"
      >
        <view class="item-top" :style="{ padding: item.centerItem ? 0 : '4rpx' }">
          <image
            style="width: 48rpx; height: 48rpx"
            :src="tabId == item.id ? item.selectIcon : item.icon"
            v-if="!item.centerItem"
          >
          </image>
          <view class="item-top_center" v-else>
            <image :src="item.selectIcon"> </image>
          </view>
        </view>
        <view class="item-bottom" :class="[tabId == item.id ? 'item-active' : '']">
          <text>{{ item.text }}</text>
        </view>
      </view>
    </view>
    <u-popup :show="modalShow" @close="modalShow = false" mode="center" :safeAreaInsetBottom="false" :round="10">
      <view class="infobox">
        <view class="info-txt" style="color: #eb3713"> 用户未登录 </view>
        <view class="info-txt"> 您还未登录，无法使用此功能。 </view>
        <view class="info-login">
          <image src="/static/images/index/empty2.png" mode="heightFix"></image>
        </view>
        <view class="info-btn-box">
          <view class="btn-cancel" @click="modalShow = false"> 我在看看 </view>
          <view class="btn-submit" @click="modalConfirm()"> 立即登录 </view>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import app from "@/static/js/app.js"
export default {
  props: {
    currentPage: {
      type: String,
      default: "home",
    },
  },
  data() {
    return {
      //适配IPhoneX
      isIpx: false,
      //底部Tab
      tabId: "home",
      tabList: [
        {
          id: "home",
          path: "/pages/index/home",
          icon: require("./tabbar/index-no.png"),
          selectIcon: require("./tabbar/index.png"),
          text: "首页",
          centerItem: false,
        },
        {
          id: 1,
          path: "/pages/index/map",
          icon: require("./tabbar/mine-no.png"),
          selectIcon: require("./tabbar/sao.png"),
          text: "扫码",
          centerItem: true,
        },
        {
          id: "mine",
          path: "/pages/index/wode",
          icon: require("./tabbar/mine-no.png"),
          selectIcon: require("./tabbar/mine.png"),
          text: "我的",
          centerItem: false,
        },
      ],
      password: null,
      modalShow: false,
      openid: null,
    }
  },
  computed: {},
  mounted() {
    this.tabId = this.currentPage
    //隐藏原生tab
    uni.hideTabBar()
  },
  created() {
    //判断为 iPhone给予底部距离
    let that = this
    uni.getSystemInfo({
      success: function (res) {
        if (res.model.indexOf("iPhone") !== -1) {
          that.isIpx = true
          uni.setStorageSync("isIpx", true)
        }
      },
    })
  },
  methods: {
    // tab 切换
    changeItem(item) {
      if (item.centerItem) {
        // if (!uni.getStorageSync("token")) {
        //   uni.showModal({
        //     title: "登录提示",
        //     content: "您还未登录账号，体验更多功能请登录",
        //     confirmText: "去登录",
        //     cancelText: "暂不登录",
        //     cancelColor: "#666",
        //     success: (result) => {
        //       if (result.confirm) {
        //         // 清除openId
        //         uni.setStorageSync("openid", "")
        //         uni.clearStorageSync()
        //         uni.navigateTo({
        //           url: "/pages/login/login",
        //         })
        //       }
        //     },
        //   })
        //   return
        // }
        this.scanCodeHandle()
        return
      }
      uni.switchTab({
        url: item.path,
      })
    },
    scanCodeHandle() {
      let that = this
      uni.scanCode({
        success(res) {
          let resp = {
            result: res.result,
            scanType: res.scanType,
          }
          uni.navigateTo({
            url: "/pages/stations/site/previewCharge?code=" + encodeURIComponent(resp.result),
          })
        },
        fail(e) {
          console.log(e)
        },
      })
    },
    // 通过 URL 地址获取参数
    getUrlParam(url) {
      let href = url
      try {
        if (href.indexOf("?") > -1) {
          let paramStr = href.split("?")[1]
          let paramArr = paramStr.split("&")
          let returnObj = {}
          for (let i = 0; i < paramArr.length; i++) {
            let paramItem = paramArr[i].split("=")
            returnObj[paramItem[0]] = paramItem[1]
          }
          return returnObj
        } else {
          return {}
        }
      } catch (error) {
        return {}
      }
    },
    modalConfirm() {
      this.modalShow = false
      uni.navigateTo({
        url: "/pages/login/login",
      })
    },
  },
}
</script>
<style scoped lang="scss">
view {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.tabbar-container {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding-top: 6rpx;
  font-size: 28rpx;
  color: #999;
  background-color: #ffffff;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(254, 161, 80, 0.1216);
  border-radius: 30rpx 30rpx 0rpx 0rpx;
}

.tabbar-container .tabbar-item {
  // flex: 1;
  min-width: 112rpx;
  height: 112rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.tabbar-container .item-active {
  color: $primary-color;
}

.tabbar-container .center-item {
  margin-top: -38rpx;
  width: 148rpx;
  height: 148rpx;
  background: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(254, 161, 80, 0.1216);
}

.tabbar-container .tabbar-item .item-top {
  padding: 0;
}

.tabbar-container .center-item .item-top .item-top_center {
  width: 115rpx;
  height: 115rpx;
  // background: linear-gradient(180deg, #ffb86c 0%, #ff4d00 100%);
  // box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(255, 89, 0, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  image {
    width: 60rpx;
    height: 60rpx;
  }
}

.tabbar-container .tabbar-item .item-top image {
  width: 100%;
  height: 100%;
}

.tabbar-container .tabbar-item .item-bottom {
  font-size: 22rpx;
  width: 100%;
}

.tabbar-container .center-item .item-bottom {
  position: absolute;
  bottom: 42rpx;
}

/* 适配iPhone X */
.IpxBot {
  padding-bottom: 40rpx !important;
}

.infobox {
  width: 640rpx;
  background: #fff;
  border-radius: 20rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 56rpx 52rpx 44rpx;
  box-sizing: border-box;

  .info-login {
    margin-top: 64rpx;

    image {
      width: 250rpx;
      height: 324rpx;
    }
  }

  .info-txt {
    text-align: center;
    font-size: 28rpx;
    margin-top: 20rpx;
    color: #000000;

    .other {
      margin-top: 12rpx;
      color: $primary-color;
    }
  }

  .info-btn-box {
    width: 100%;
    font-size: 28rpx;
    margin-top: 36rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .btn-cancel {
      color: #666666;
      width: 240rpx;
      height: 84rpx;
      line-height: 84rpx;
      text-align: center;
      border: 2rpx solid #a6a8ba;
      border-radius: 14rpx;
    }

    .btn-submit {
      color: #ffffff;
      width: 240rpx;
      height: 84rpx;
      line-height: 84rpx;
      text-align: center;
      background-color: #ff6b01;
      border: 2rpx solid #ff6b01;
      border-radius: 14rpx;
    }
  }
}
</style>
