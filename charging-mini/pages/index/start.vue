<template>
  <view class="find-box"> </view>
</template>

<script>
export default {
  data() {
    return {
      timeData: "",
      backgroundImageUrl: "",
    }
  },

  onLoad() {
    let a = Math.random()
  },

  onHide() {},
  methods: {
    // 启动后30秒倒计时结束
    finishCountDown() {
      uni.setStorageSync("hasShowFirstScreen", true)
      uni.switchTab({
        url: "/pages/index/index",
      })
    },
    handleFinish() {
      uni.setStorageSync("hasShowFirstScreen", true)
      uni.switchTab({
        url: "/pages/index/index",
      })
    },
    onChangeCountDown(e) {
      this.timeData = e.seconds
    },
  },
}
</script>

<style lang="scss" scoped>
.find-box {
  min-height: 100vh;
  box-sizing: border-box;
  padding-top: 110rpx;
  background-size: 100% 100%;
  position: relative;
  text-align: center;
  .countDown-view {
    // width: 88rpx;
    padding: 0 12rpx;
    height: 40rpx;
    text-align: center;
    float: right;
    margin-top: 84rpx;
    margin-right: 18rpx;
  }
  ::v-deep.u-count-down {
    background-color: rgba(0, 0, 0, 0.2);
    color: #fff;
    font-size: 28rpx;
    text-align: center;
    width: 98rpx;
    padding: 5rpx 20rpx;
    border-radius: 26rpx;
    color: #fff;
  }
}
</style>
