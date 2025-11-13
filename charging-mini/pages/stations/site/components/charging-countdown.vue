<template>
  <u-action-sheet :safeAreaInsetBottom="true" :show="showPopup" :closeOnClickOverlay="false" round="30" @close="close">
    <view class="popup-content">
      <view
        class="circle"
        :style="'background-image:url(' + (type == 'start' ? startCircleBgImg : endCircleBgImg) + ')'"
      >
        <block v-if="!finished"> {{ timeData }}s </block>
        <view class="" style="font-size: 36rpx" v-else> 请等待... </view>
      </view>
      <view class="ing-text">
        {{ type == "start" ? "开始充电中..." : "" }}
      </view>
      <view class="content-info" v-if="type == 'start'">
        请勿关闭本页面，预计60秒内开始充电~<br />
        属于高压设备出于安全考虑电压电流将逐渐增加，<br />
        请耐心等待开始充电~
      </view>

      <view class="content-info" v-if="type == 'end'">
        请勿关闭本页面，预计60秒内结束电桩~ <br />
        属于高压设备出于安全考虑电压电流将逐渐降为零 <br />
        请耐心等待最终结算费用~
      </view>
    </view>
  </u-action-sheet>
</template>
<script>
export default {
  name: "charging-countdown",
  data() {
    return {
      showPopup: false,
      type: "start",
      startTimeData: 60 * 1000,
      timeData: 60,
      finished: false,
      startCircleBgImg: require("../../static/start-circle.png"),
      endCircleBgImg: require("../../static/end-circle.png"),
      orderNo: "",
      timer: null,
    }
  },

  methods: {
    onOpen(params) {
      this.type = params.type
      this.orderNo = params.orderNo || ""
      this.showPopup = true
      this.finished = false
      this.timeData = 60
      this.timer = setInterval(() => {
        this.timeData--
        if (this.timeData === 0) {
          clearInterval(this.timer)
          this.timer = null
          this.finished = true
        }
      }, 1000)
    },
    close() {
      clearInterval(this.timer)
      this.timer = null
      this.showPopup = false
      this.resetCountDown()
      this.finishCountDown()
    },
    // 启动后60秒倒计时结束
    finishCountDown() {
      this.finished = true
      this.showPopup = false
      if (this.type == "end") {
        setTimeout(() => {
          uni.redirectTo({
            url: "/pages/stations/orders/orderList?orderNo=" + this.orderNo,
          })
        }, 1000)
      }
    },
    // 重置
    resetCountDown() {
      this.timeData = 60
    },
  },
}
</script>
<style lang="scss" scoped>
.popup-content {
  text-align: center;
  padding-bottom: 100rpx;

  .circle {
    width: 250rpx;
    height: 250rpx;
    background-size: 100% 100%;
    border-radius: 50%;
    color: #333;
    margin: 75rpx auto 0;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 600;
    font-size: 50rpx;
    text {
      font-size: 66rpx;
      margin-top: -10rpx;
      margin-right: 4rpx;
    }
  }
  .ing-text {
    font-weight: normal;
    font-size: 36rpx;
    color: #333333;
    height: 40rpx;
    margin-top: 32rpx;
    font-weight: 600;
  }
  .content-info {
    font-size: 26rpx;
    color: $basic-color;
    line-height: 50rpx;
    margin-top: 60rpx;
  }
}
</style>
