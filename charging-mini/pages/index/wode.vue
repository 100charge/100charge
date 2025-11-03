<template>
  <view class="profile-page">
    <view class="container">
      <!-- 用户信息区域 -->
      <view class="my-info-box" style="padding-top: 10vh">
        <view class="my-info-content">
          <view class="content-info">
            <image class="avatat" :src="userInfo.avatar || '/static/img/head-img.png'" mode="scaleToFill"></image>
            <view class="my-phone" v-if="isLogin">
              {{ userInfo.userName || encryptPhoneNumber(userInfo.mobile) }}
            </view>
            <view v-else class="my-phone" @tap="toLoginPage()">请登录</view>
          </view>
        </view>
      </view>

      <!-- 账户余额区域 -->
      <view class="balance-box">
        <view class="balance-content">
          <image class="balance-icon" src="/static/img/mine/yue.png" mode="aspectFit"></image>
          <text class="balance-text" @click="navigateToPage('/pages/wode/recharge/balance')"
            >账户余额：{{ accountBalance }}</text
          >
          <view class="recharge-btn" @click="navigateToPage('/pages/wode/recharge/recharge')"> 去充值 </view>
        </view>
      </view>

      <!-- 常用功能 -->
      <view class="common-functions">
        <view class="function-title">常用功能</view>
        <view class="function-grid">
          <view class="function-item" @click="navigateToPage('/pages/stations/orders/orderList')">
            <view class="function-icon">
              <image src="/static/img/mine/dingdan.png" mode="heightFix"></image>
            </view>
            <text>订单</text>
          </view>
          <view class="function-item" @click="navigateToPage('')">
            <view class="function-icon">
              <image src="/static/img/mine/youhui.png" mode="heightFix"></image>
              <view class="coupon-badge" v-if="couponCount > 0">{{ couponCount }}</view>
            </view>
            <text>优惠券</text>
          </view>
          <view class="function-item" @click="navigateToPage()">
            <view class="function-icon">
              <image src="/static/img/mine/kaipiao.png" mode="heightFix"></image>
            </view>
            <text>开票</text>
          </view>
          <view class="function-item" @click="navigateToPage()">
            <view class="function-icon">
              <image src="/static/img/mine/huiyuan.png" mode="heightFix"></image>
            </view>
            <text>会员中心</text>
          </view>
        </view>

        <!-- 标签占位(按UI无则移除) -->
      </view>

      <!-- 车辆信息 -->
      <view class="car-info-box" @click="navigateToPage('/pages/wode/car/myCarList')">
        <view class="car-content" v-if="carInfo.plateNo">
          <view class="car-left">
            <view class="car-number">{{ carInfo.plateNo }}</view>
            <view class="car-desc">车辆用途：{{ carInfo.usage || "" }}</view>
          </view>
          <view class="car-right">
            <image class="car-img" src="/static/img/my-icon-car.png" mode="scaleToFill"></image>
            <u-icon name="arrow-right" size="14" color="#999"></u-icon>
          </view>
        </view>
        <view class="car-content" v-else>
          <view class="car-left">
            <text class="car-title">新增车辆</text>
          </view>
          <view class="car-right">
            <image class="car-img" src="/static/img/my-icon-car.png" mode="scaleToFill"></image>
            <u-icon name="arrow-right" size="14" color="#999"></u-icon>
          </view>
        </view>
      </view>

      <!-- 更多功能 -->
      <view class="more-functions">
        <view class="function-list">
          <view class="more-title">更多功能</view>
          <view class="function-row" @click="navigateToPage('')">
            <image class="function-image" src="/static/img/mine/yaoqing.png" mode="aspectFit"></image>
            <text>邀请有礼</text>
            <u-icon name="arrow-right" size="24" color="#999"></u-icon>
          </view>
          <view class="function-row" @click="navigateToPage('')">
            <image class="function-image" src="/static/img/mine/fuli.png" mode="aspectFit"></image>
            <text>福利兑换</text>
            <u-icon name="arrow-right" size="24" color="#999"></u-icon>
          </view>
          <view class="function-row" @click="navigateToPage('/pages/wode/extend/kefu')">
            <image class="function-image" src="/static/img/mine/kefu.png" mode="aspectFit"></image>
            <text>客服中心</text>
            <u-icon name="arrow-right" size="24" color="#999"></u-icon>
          </view>
          <view class="function-row" @click="navigateToPage('')">
            <image class="function-image" src="/static/img/mine/hezuo.png" mode="aspectFit"></image>
            <text>我要合作</text>
            <u-icon name="arrow-right" size="24" color="#999"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部导航栏组件 -->
    <Tabbar :currentPage="'mine'" />
  </view>
</template>

<script>
import Tabbar from "../../components/tabbar/tabbar.vue"
import app from "@/static/js/app.js"
import { getUserInfo } from "@/config/api"
export default {
  components: {
    Tabbar,
  },
  data() {
    return {
      isLogin: false,
      userInfo: {
        userName: "",
        mobile: "",
        avatar: "",
      },
      accountBalance: "0",
      couponCount: 0,
      carInfo: {
        plateNo: "",
        usage: "",
      },
      openid: "",
      id: "",
    }
  },
  onLoad(options) {
    // 隐藏原生tabBar，使用自定义底部导航
    uni.hideTabBar()
  },
  onShow() {
    this.checkLoginStatus()
    this.userInfo = {
      userName: uni.getStorageSync("userName") || "",
      mobile: uni.getStorageSync("mobile") || "",
      avatar: uni.getStorageSync("headImage") || "",
    }
    this.openid = uni.getStorageSync("openid")
    this.id = uni.getStorageSync("id")
    this.getPageInfo()
  },
  methods: {
    getPageInfo() {
      getUserInfo({}).then((res) => {
        this.headImage = res.data.avatar || ""
        this.accountBalance = res.data.balance || 0
        this.couponCount = res.data.coupon || 0
        this.carInfo = {
          plateNo: res.data.plateNo || "",
          usage: res.data.usage || "",
        }
        this.userInfo = {
          ...res.data,
          userName: res.data.userName || "",
          mobile: res.data.phoneNumber || "",
          avatar: res.data.headImage || "",
        }
        if (res.data.phoneNumber) {
          uni.setStorageSync("mobile", res.data.phoneNumber)
        }
        if (res.data.avatar) {
          uni.setStorageSync("headImage", res.data.avatar)
        }
        if (res.data.userName) {
          uni.setStorageSync("userName", res.data.userName)
        }
      })
    },

    checkLoginStatus() {
      // 检查登录状态
      const token = uni.getStorageSync("token")
      this.isLogin = !!token
    },

    toLoginPage() {
      uni.navigateTo({
        url: "/pages/login/login",
      })
    },
    navigateToPage(url) {
      if (!this.isLogin) {
        uni.showModal({
          title: "登录提示",
          content: "您还未登录账号，体验更多功能请登录",
          confirmText: "去登录",
          cancelText: "暂不登录",
          success: (result) => {
            if (result.confirm) {
              this.toLoginPage()
            }
          },
        })
        return
      }

      if (url) {
        uni.navigateTo({
          url: url,
        })
      } else {
        uni.showToast({
          title: "功能暂未开放",
          icon: "none",
        })
      }
    },

    encryptPhoneNumber(phone) {
      if (!phone) return ""
      const reg = /(\d{3})\d{4}(\d{4})/
      return String(phone).replace(reg, "$1****$2")
    },
  },
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #e1e5e8 0%, #f4f4f4 100%);
  padding: 0 24rpx;
  padding-bottom: 120rpx;
}

.container {
  padding-bottom: 40rpx;
}

/* 用户信息区域 */
.my-info-box {
  width: 100%;
}

.my-info-content {
  padding: 26rpx 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.content-info {
  display: flex;
  align-items: center;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.avatat {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  margin-right: 16rpx;
}

.my-phone {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.level {
  width: 48rpx;
  height: 48rpx;
  margin-left: 10rpx;
}

.setting-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60rpx;
  height: 60rpx;
}

/* 账户余额区域 */
.balance-box {
  background: #333;
  border-radius: 20rpx;
  border-radius: 20rpx 20rpx 0 0;
  padding: 14rpx;
}

.balance-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
}

.balance-icon {
  width: 30rpx;
  height: 30rpx;

  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: bold;
  margin-right: 16rpx;
  padding-left: 20rpx;
}

.balance-text {
  flex: 1;
  font-size: 24rpx;
  font-weight: bold;
  color: white;
}

.recharge-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 1rpx solid rgba(255, 255, 255, 0.3);
  border-radius: 24rpx;
  padding: 8rpx 16rpx;
  font-size: 20rpx;
  color: white;
}

.function-title {
  color: #333;
  font-size: 32rpx;
  font-weight: 400;
  margin-bottom: 40rpx;
}

/* 常用功能 */
.common-functions {
  background: #fff;
  border-radius: 0 0 20rpx 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.function-grid {
  display: flex;
  justify-content: space-around;
}

.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.function-icon {
  position: relative;
  margin-bottom: 16rpx;
}

.function-item image {
  width: 60rpx;
  height: 60rpx;
}

.icon-placeholder {
  font-size: 48rpx;
  width: 60rpx;
  height: 60rpx;
  line-height: 60rpx;
  text-align: center;
}

.function-item text {
  font-size: 26rpx;
  color: #333;
}

.coupon-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  background: #ff4757;
  color: white;
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 12rpx;
  min-width: 24rpx;
  text-align: center;
  line-height: 1;
}

.backup-tag {
  background: #007aff;
  color: white;
  padding: 8rpx 16rpx;
  border-radius: 12rpx;
  font-size: 24rpx;
  margin-top: 20rpx;
  align-self: flex-start;
}

/* 车辆信息 */
.car-info-box {
  background: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.car-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.car-left {
  flex: 1;
}

.car-number,
.car-title {
  font-size: 34rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.car-desc {
  font-size: 24rpx;
  color: #72727d;
}

.car-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.car-img {
  width: 142rpx;
  height: 93rpx;
}

/* 更多功能 */
.more-functions {
  margin-bottom: 20rpx;
}

.function-list {
  background: white;
  border-radius: 20rpx;
  overflow: hidden;
  padding: 0;
}

.more-title {
  font-size: 32rpx;
  font-weight: 400;
  color: #333;
  padding: 30rpx;
}

.function-row {
  display: flex;
  align-items: center;
  padding: 30rpx;
}

.function-row:last-child {
  border-bottom: none;
}

.function-row image {
  width: 40rpx;
  height: 40rpx;
  margin-right: 20rpx;
}

.function-image {
  width: 40rpx;
  height: 40rpx;
  margin-right: 20rpx;
}

.function-emoji {
  font-size: 36rpx;
  width: 48rpx;
  height: 48rpx;
  line-height: 48rpx;
  text-align: center;
  margin-right: 20rpx;
}

.function-row text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}
</style>
