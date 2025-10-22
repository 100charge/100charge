<template>
  <view class="page-view">
    <view class="box flex-row flex-jst-start flex-ali-center">
      <view class="container flex-row flex-jst-start flex-ali-center">
        <image src="../static/icon-tel.png" mode="aspectFit"></image>
        <text @click="callPhone">{{ telNumber }}</text>
      </view>
      <view class="container-time flex-row flex-jst-start flex-ali-center">
        <image src="../static/icon-time.png" mode="aspectFit"></image>
        <text>00:00 - 24:00</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getConfigKey } from "@/config/api.js"
export default {
  data() {
    return {
      telNumber: "",
    }
  },
  onLoad() {
    getConfigKey("customer_service_tel").then((res) => {
      this.telNumber = res.msg
    })
  },
  methods: {
    callPhone() {
      uni.makePhoneCall({
        phoneNumber: this.telNumber, // 这里就是自己要拨打的电话号码
        success: (res) => {
          console.log("调用成功!")
        },
        fail: (res) => {
          console.log("调用失败!")
        },
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.page-view {
  padding: 24rpx;
}
.box {
  background: #fff;
  box-shadow: 0rpx 1rpx 2rpx 0rpx rgba(0, 0, 0, 0.08);
  border-radius: 20rpx;
  padding: 30rpx 0 30rpx 40rpx;
  .container {
    font-size: 24rpx;
    color: #3d3d3d;
    image {
      width: 44rpx;
      height: 44rpx;
      margin-right: 12rpx;
    }
  }

  .container-time {
    font-size: 24rpx;
    color: #999;
    margin-left: 48rpx;
    image {
      width: 32rpx;
      height: 32rpx;
      margin-right: 12rpx;
    }
  }
}
</style>
