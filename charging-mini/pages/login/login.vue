<template>
  <view class="main">
    <view class="main-swiper">
      <view>Hello!</view>
      <view>欢迎登录100Charge</view>
    </view>
    <view class="login-info" v-if="loginMode == 'authPhone'">
      <view class="login-info-item">
        <view>
          <button
            open-type="getPhoneNumber|agreePrivacyAuthorization"
            @getphonenumber="getPhoneNumber"
            class="confirm-btn"
          >
            手机号一键登录
          </button>
        </view>
        <view>
          <view @click="cancelLogin" class="button-else"> 取消登录 </view>
        </view>
        <view class="privacy-box" @click="checkboxValue = !checkboxValue">
          <image v-if="!checkboxValue" src="/static/images/index/check-no.png" mode="aspectFit"></image>
          <image v-else src="/static/images/index/checked-fill.png" mode="aspectFit"></image>
          <text style="margin-top: -4rpx"
            >我已阅读并同意<text class="privacy" @tap.stop="handlePrivacy">《隐私协议》</text>和<text
              class="privacy"
              @tap.stop="handleUserAgrement"
              >《用户条款》</text
            ></text
          >
        </view>
      </view>
    </view>

    <!-- 普通弹窗 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-content">
        <view class="popup-title">温馨提示</view>
        <view class="popup-con">平台将获取并验证你的手机号提供会员服务，注册即默认同意服务条款和政策协议。</view>
        <button class="button">同意</button>
        <view class="button-else"> 不同意，暂不使用 </view>
      </view>
    </uni-popup>
  </view>
</template>
<script>
import app from "@/static/js/app.js"
import { appletWeChatLogin, appletBindMobile } from "@/config/api.js"
export default {
  data() {
    return {
      phoneNumber: "",
      openid: "",
      checkboxValue: false,
      loginMode: "authPhone", // pwd / authPhone

      loginUserName: "",
      loginPassword: "",
      fromPage: "",
    }
  },
  onLoad(option) {
    this.fromPage = option.fromPage || ""
    // #ifdef MP-WEIXIN
    uni.getPrivacySetting({
      success: (res) => {
        if (res.needAuthorization) {
          // 需要弹出隐私协议
          this.showPrivacy = true
        } else {
        }
      },
      fail: () => {},
      complete: () => {},
    })
    // #endif
  },
  onHide() {},
  methods: {
    cancelLogin() {
      uni.navigateBack()
    },

    getPhoneNumber(e) {
      if (!this.checkboxValue) {
        uni.showToast({
          title: "请先阅读并同意《隐私协议》和《用户条款》",
          icon: "none",
        })
        return
      }
      if (e.detail.code) {
        var that = this
        uni.login({
          provider: "weixin",
          success: (res) => {
            console.log("loginCode:", JSON.parse(JSON.stringify(res)))
            that.doLogin(res.code, e)
          },
          fail: (e) => {
            console.log("fail", e)
          },
        })
      }
    },

    doLogin(code, e) {
      console.log("请求参数:" + JSON.stringify(code))
      let that = this
      appletWeChatLogin({ code })
        .then((res) => {
          if (res.data) {
            let userInfo = res.data
            let appUser = res.data.appUser
            uni.setStorageSync("token", userInfo.token)
            that.openid = appUser.openId
            uni.setStorageSync("openid", appUser.openId)
            if (appUser.phoneNumber) {
              uni.setStorageSync("mobile", appUser.phoneNumber)
            }
            uni.setStorageSync("nickName", appUser.nickName)
            uni.setStorageSync("userName", appUser.userName)
            uni.setStorageSync("headImage", appUser.avatar)

            appletBindMobile({
              code: e.detail.code,
              openId: that.openid,
            }).then((res) => {
              if (res && res.code == 200) {
                if (this.fromPage == "previewCharge") {
                  uni.navigateBack()
                } else {
                  uni.switchTab({
                    url: "/pages/index/index",
                  })
                }
              }
            })
          }
        })
        .catch((err) => {
          uni.showToast({
            title: err.msg,
            icon: "error",
          })
        })
    },

    handleAuthError(e) {
      console.log(e)
    },
    // 隐私政策
    handlePrivacy() {
      uni.navigateTo({
        url: `/pages/wode/extend/article?id=4`, //附带两个参数过去
      })
    },
    // 用户协议
    handleUserAgrement() {
      uni.navigateTo({
        url: `/pages/wode/extend/article?id=5`, //附带两个参数过去
      })
    },
  },
}
</script>

<style>
page {
  background: #fff !important;
}
</style>
<style lang="scss" scoped>
.main {
  // height: 100vh;
  width: 100%;
  padding: 0 60rpx;
  background: linear-gradient(0deg, #fff 0%, #f5e0d0 100%);
  .main-swiper {
    width: 100%;
    height: 24vh;
    text-align: left;
    padding-top: 180rpx;

    image {
      width: 198rpx;
      height: 198rpx;
      margin-top: 120rpx;
    }

    view {
      font-weight: bold;
      font-size: 48rpx;
      color: #333;
      margin-top: 14rpx;
    }
  }

  .fixed-bottom {
    position: fixed;
    bottom: 60rpx;
    left: 0;
    right: 0;
    width: 100%;
    text-align: center;
    font-size: 28rpx;
    color: $basic-color;
    padding: 0 60rpx;
  }
  .login-info,
  .password-login-view {
    margin-top: 170rpx;
    width: 100%;
    .privacy-box {
      margin-top: 40rpx;
      text-align: center;
      font-size: 24rpx;
      color: #333;
      display: flex;
      justify-content: center;
      align-items: center;

      image {
        width: 30rpx;
        height: 30rpx;
        margin-right: 12rpx;
      }

      .privacy {
        color: $primary-color;
      }
    }
  }
  .confirm-btn {
    width: 100%;
    height: 94rpx;
    color: #ffffff;
    background: #2a2a2a;
    font-size: 32rpx;
    margin-bottom: 14rpx;
    border-radius: 22rpx;
    border: 0;
  }
  .blue-button {
    background: #3a73e6;
    border-radius: 26rpx;
  }
  .cancel-btns {
    color: $basic-color;
    font-size: 28rpx;
    margin-top: 24rpx;
  }

  .password-login-view {
    margin-top: 160rpx;
    width: 100%;
    .input {
      text-align: left;
      height: 86rpx;
      border-bottom: 2rpx solid rgba(0, 0, 0, 0.08);
      display: flex;
      align-items: flex-end;

      input {
        // height: 80rpx;
        padding: 0 0 12rpx 0;
        background-color: transparent;
        font-size: 32rpx;
      }
      input::placeholder {
        color: #dbdbdb;
      }
    }
    .cancel-btns {
      color: $basic-color;
      font-size: 28rpx;
      margin-top: 24rpx;
    }
  }

  .popup-content {
    width: 642rpx;
    height: 480rpx;
    background: #ffffff;
    border-radius: 32rpx !important;

    .popup-title {
      height: 92rpx;
      background: linear-gradient(79deg, #e5f0ff 0%, #ffffff 100%);
      border-radius: 32rpx 32rpx 0rpx 0rpx;
      font-weight: 500;
      font-size: 32rpx;
      color: #2a2a2a;
      line-height: 92rpx;
      padding-left: 47rpx;
    }

    .popup-con {
      width: 540rpx;
      height: 96rpx;
      font-weight: 400;
      font-size: 28rpx;
      color: #515151;
      line-height: 48rpx;
      text-align: left;
      font-style: normal;
      padding-left: 47rpx;
      margin-top: 64rpx;
    }
  }

  .button-else {
    font-weight: 400;
    font-size: 24rpx;
    color: #666;
    text-align: center;
  }
}
</style>
