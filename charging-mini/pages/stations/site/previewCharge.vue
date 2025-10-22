<template>
  <view class="charging-container">
    <!-- 充电桩信息卡片 -->
    <view class="station-card">
      <view class="top">
        <!-- 充电桩状态圆圈 -->
        <view class="pile-left">
          <view class="guns-status-view" :style="'border-color:' + pileInfo.color">
            <span slot="content" class="circle-progress-percent" :style="'color:' + pileInfo.color">
              {{ pileInfo.gunsStatus || "" }}
            </span>
          </view>
        </view>

        <!-- 充电桩详情 -->
        <view class="station-info">
          <view class="station-title">{{ pileInfo.stationName }}</view>
          <view class="station-code-row">
            <text class="station-code">充电枪号{{ pileInfo.deviceNo }}</text>
            <!-- <u-icon name="copy" size="16" color="#999"></u-icon> -->
          </view>
          <text class="station-address">{{ pileInfo.address }}</text>
        </view>
      </view>
      <view class="bottom">
        <view class="bottom-left">
          <view class="info-item">
            <text class="info-label">充电桩功率</text>
            <view class="info-value-row">
              <text class="info-value">{{ pileInfo.maxPower }}kW</text>
              <image v-if="pileInfo.fastCharging" src="/static/img/kuai.png" class="badge-icon" mode="widthFix"></image>
              <image v-else src="/static/img/man.png" class="badge-icon" mode="widthFix"></image>
            </view>
          </view>
        </view>
        <view class="bottom-divider" v-if="pileInfo.parkingFeeTip"></view>
        <view class="bottom-right" v-if="pileInfo.parkingFeeTip">
          <view class="info-item">
            <view class="info-label">停车费（临牌不减免）</view>
            <view class="parking-benefit"
              ><text class="parking-benefit-text">{{ pileInfo.parkingFeeTip }}</text></view
            >
          </view>
        </view>
      </view>
    </view>

    <!-- 我的爱车选择器 -->
    <view class="my-car-section">
      <view
        class="my-car-bg"
        v-if="carList.length !== 0"
        @click="navigateToPage('/pages/wode/car/addCar?fromPage=previewCharge')"
      >
        <view class="section-title">暂无爱车</view>
        <view class="car-selector">
          <text class="car-number"></text>
          <u-icon name="plus" size="14" color="#999"></u-icon>
        </view>
      </view>
      <view class="my-car-bg" v-else @click="chooseCar">
        <view class="section-title">我的爱车</view>
        <view class="car-selector">
          <picker mode="selector" :value="carIndex" :range="carNameArray" @change="bindPickerChange">
            <text class="car-number">{{ carNameArray[carIndex] }}</text>
          </picker>
          <u-icon name="arrow-right" size="14" color="#999"></u-icon>
        </view>
      </view>
    </view>

    <!-- 充电费用 -->
    <view class="charging-fee-section">
      <view class="section-title">充电费用</view>
      <view class="current-period">
        <text class="period-label">当前时段</text>
      </view>
      <view class="fee-display">
        <text class="fee-symbol">¥</text>
        <text class="fee-value">{{ pileInfo.chargeFee }}</text>
        <text class="fee-unit">/度</text>
      </view>
      <view class="fee-schedule">
        <text class="schedule-info">({{ pileInfo.currentTime || "" }})</text>
        <view class="fee-details" @click="priceHandle">
          <text class="details-text">电价详情</text>
          <u-icon name="arrow-right" size="14" color="#999"></u-icon>
        </view>
      </view>
    </view>

    <!-- 支付方式 -->
    <view class="payment-section">
      <view class="section-title">支付方式</view>

      <!-- 微信支付分 -->
      <!-- <view class="payment-item" :class="{ active: payMode == '2' }" @click="selectPay('2')">
        <view class="payment-info">
          <image class="payment-icon" src="/static/img/weichart.png" mode="heightFix"></image>
          <view class="payment-details">
            <text class="payment-name">微信支付分</text>
            <text class="payment-desc">先充后付</text>
          </view>
        </view>
        <view class="payment-radio" :class="{ checked: payMode == '2' }">
          <image v-if="payMode == '2'" src="/static/img/checked-fill-green.png" mode="aspectFit"></image>
          <image v-else src="/static/img/check-no.png" mode="aspectFit"></image>
        </view>
      </view> -->

      <!-- 余额支付 -->
      <view class="payment-item" :class="{ active: payMode == '1' }" @click="selectPay('1')">
        <view class="payment-info">
          <image class="payment-icon" src="/static/img/yue.png" mode="heightFix"></image>
          <view class="payment-details">
            <text class="payment-name">余额支付</text>
            <view class="payment-desc-row">
              <text class="balance-amount">可用额度 {{ toDecimal2(accountBalance) }}元</text>
              <view class="recharge-link" @tap.stop="navigateToPage('/pages/wode/recharge/recharge')">
                <text class="recharge-text">充值余额</text>
                <u-icon name="arrow-right" size="10" color="#007AFF"></u-icon>
              </view>
            </view>
          </view>
        </view>
        <view class="payment-radio" :class="{ checked: payMode == '1' }">
          <image v-if="payMode == '1'" src="/static/img/checked-fill-green.png" mode="aspectFit"></image>
          <image v-else src="/static/img/check-no.png" mode="aspectFit"></image>
        </view>
      </view>
    </view>

    <!-- 底部开始充电按钮 -->
    <view class="main-confirm-bottom" style="flex-direction: column">
      <text style="color: #666; margin-bottom: 12rpx; margin-top: -10rpx; font-size: 24rpx">请插枪后下拉刷新页面</text>
      <view
        :class="pileInfo.gunsStatus == '空闲' || pileInfo.gunsStatus == '已插枪' ? 'confirm-btn' : 'disabled-btn'"
        @click="$u.throttle(startCharge, 5000)"
        >开始充电</view
      >
    </view>

    <stationPrice ref="stationPriceRef" />
    <chargingCountdown ref="chargingCountdownRef" />
  </view>
</template>
<script>
import app from "@/static/js/app.js"
import {
  getUserInfo,
  getUserCarList,
  readyChargeInfo,
  startCharging,
  getOrderInfoByTradeNo,
  onStartupSuccess,
} from "@/config/api.js"
import stationPrice from "./components/station-price.vue"
import chargingCountdown from "./components/charging-countdown.vue"
export default {
  components: {
    stationPrice,
    chargingCountdown,
  },
  data() {
    return {
      hiddenBtn: false,
      toDecimal2: app.toDecimal2,
      accountBalance: 0,
      payMode: "1",
      carList: [],
      carNameArray: [],
      carIndex: 0,
      pileInfo: {},
      tradeNo: "",
      statusInterval: null, // 定时器
      code: "",
      modalShow: false,
    }
  },
  onLoad(options) {
    // if (options.q) {
    //   var decodedUrl = decodeURIComponent(options.q)
    //   this.code = decodedUrl
    // } else {
    //   this.code = decodeURIComponent(options.code)
    // }
    this.code = "https://qr.kaichongcharge.com?code=37021400010010G01"
    console.log("this.code", this.code)
  },
  onShow() {
    this.hiddenBtn = uni.getStorageSync("userType") == "company"
    this.getPileInfo()
    this.userCarList()
  },
  onUnload() {
    clearInterval(this.statusInterval)
    this.statusInterval = null
  },
  //下拉刷新
  onPullDownRefresh() {
    this.getPileInfo()
    this.userCarList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
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
    getPileInfo() {
      readyChargeInfo({
        code: this.code,
      })
        .then((res) => {
          if (res && res.code === 200) {
            this.pileInfo = {
              ...res.data,
              color: this.getColor(res.data.gunsStatus),
            }
            console.log(res)
          } else {
            uni.showToast({
              title: res.msg,
              icon: "error",
            })
          }
        })
        .catch((e) => {
          console.log("e-catch", e)
          setTimeout(function () {
            uni.showToast({
              title: e.data.msg,
              icon: "error",
            })
          }, 600)
        })
    },
    getColor(status) {
      // 充电中-FF6B01 故障-FF484A 插枪-FF6B01 离线-999999 空闲-85E2C2
      // OFFLINE(0, "OFFLINE", "离线"),
      // FAULT(1, "FAULT", "故障"),
      // ONLINE(2, "ONLINE", "空闲"),
      // CHARGING(3, "CHARGING", "充电中");
      // CHARGING(3, "CHARGING", "已插枪");
      switch (status) {
        case "充电中":
          return "#149fff"
        case "故障":
          return "#FA5151"
        case "离线":
          return "#999999"
        case "空闲":
          return "#20d26a"
        case "已插枪":
          return "#FF6B01"
        default:
          return "#999999"
          break
      }
    },
    selectPay(type) {
      this.payMode = type
    },
    bindPickerChange(e) {
      console.log(e)
      let index = e.detail.value
      this.carIndex = index
    },

    // 余额启动充电
    startCharge() {
      // 模拟启动成功
      this.$refs.chargingCountdownRef.onOpen({
        type: "start",
      })
      setTimeout(() => {
        uni.showToast({
          title: "启动充电成功",
          icon: "none",
        })
        this.$refs.chargingCountdownRef.close()
        uni.redirectTo({
          url: "/pages/stations/site/charging?orderNo=" + this.tradeNo,
        })
      }, 20000)
      return

      // 实际逻辑
      if (this.pileInfo.gunsStatus != "空闲" && this.pileInfo.gunsStatus != "已插枪") {
        uni.showToast({
          title: "请确定是否已插枪",
          icon: "none",
        })
        return
      }
      startCharging({
        payType: "BALANCE", // 余额 BALANCE, 微信支付分PAY_POINTS, 信用分CREDIT_POINTS;
        plateNo: this.carNameArray[this.carIndex],
        chargingId: this.pileInfo.stationId,
        deviceNo: this.pileInfo.deviceNo,
        gunsNo: this.pileInfo.gunsNo,
        code: this.code,
        optionType: "WECHAT", // 操作来源 微信、阿里,可用值:ALIPAY,KEY_CARD,VIN,WECHAT
      }).then((res) => {
        this.$refs.chargingCountdownRef.onOpen({
          type: "start",
        })
        this.tradeNo = res.data

        this.statusInterval = setInterval(() => {
          this.getOrderStatus()
        }, 2000)
      })
    },

    getOrderStatus() {
      getOrderInfoByTradeNo({
        tradeNo: this.tradeNo,
      }).then((res) => {
        if (res.data.orderState == "CHARGING") {
          // NONE(-1, "未知"),
          // NO_START(0, "未开始"),
          // CHARGING(1, "充电中"),
          // GUN_NOT_DRAWN(2, "充电结束（未拔枪）"),
          // CHARGING_COMPLETED(3, "充电结束"),
          // BILL_HAS_BEEN_UPLOADED(4, "账单已上传"),
          // ABNORMAL(5, "异常");
          clearInterval(this.statusInterval)
          uni.showToast({
            title: "启动充电成功",
            icon: "none",
          })
          onStartupSuccess({ tradeNo: this.tradeNo }).then((resp) => {})
          setTimeout(() => {
            this.$refs.chargingCountdownRef.close()
            uni.redirectTo({
              url: "/pages/stations/site/charging?orderNo=" + this.tradeNo,
            })
          }, 2000)
        } else if (res.data.orderState == "ABNORMAL") {
          clearInterval(this.statusInterval)
          uni.showToast({
            title: "启动充电失败，请重试",
            icon: "none",
          })
          this.$refs.chargingCountdownRef.close()
        }
      })
    },

    priceHandle() {
      this.$refs.stationPriceRef.onOpen({
        id: this.pileInfo.stationId,
      })
    },
    chooseCar() {},
    copyCode(code) {
      uni.setClipboardData({
        data: code,
        success: () => {},
        complete: (e) => {
          console.log(e)
        },
      })
    },
    navigateToPage(url) {
      this.modalShow = false
      if (!uni.getStorageSync("token")) {
        uni.showModal({
          title: "登录提示",
          content: "您还未登录账号，体验更多功能请登录",
          confirmText: "去登录",
          cancelText: "暂不登录",
          cancelColor: "#666",
          success: (result) => {
            console.log(result)
            if (result.confirm) {
              uni.navigateTo({
                url: "/pages/login/login?fromPage=previewCharge",
              })
            }
          },
        })
        return
      }
      uni.navigateTo({
        url,
      })
    },

    userCarList() {
      getUserInfo({})
        .then((res) => {
          this.accountBalance = res.data.balance || 0
        })
        .catch((e) => {
          console.log(e)
        })
      getUserCarList({}).then((res) => {
        if (res && res.code === 200 && res.data) {
          this.carList = res.data
          this.carNameArray = res.data.map((item) => {
            return item.plateNo
          })
        }
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.charging-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 120rpx;
}

/* 充电桩信息卡片 */
.station-card {
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

  gap: 24rpx;
  background-image: url("/static/img/shap2.png");
  height: 340rpx;
  background-size: 100% 100%;
  background-position: center;
  background-repeat: no-repeat;

  .top {
    display: flex;
    align-items: flex-start;
    .pile-left {
      .guns-status-view {
        border: 14rpx solid #e9e9e9;
        border-radius: 50%;
        width: 120rpx;
        height: 120rpx;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .circle-progress-percent {
        color: $primary-color;
        font-size: 28rpx;
        font-weight: 600;
      }
    }

    .status-circle {
      width: 140rpx;
      height: 140rpx;
      border-radius: 50%;
      border: 12rpx solid #cbf3d5;
      background-color: transparent;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      position: relative;
    }

    .status-text {
      color: #4cd964;
      font-size: 28rpx;
      font-weight: bold;
      z-index: 2;
      position: relative;
    }

    .station-info {
      flex: 1;
      margin-left: 30rpx;
    }

    .station-title {
      font-size: 36rpx;
      color: #333;
      font-weight: bold;
      margin-bottom: 12rpx;
    }

    .station-code-row {
      display: flex;
      align-items: center;
      gap: 12rpx;
      margin-bottom: 8rpx;
    }

    .station-code {
      font-size: 28rpx;
      color: #666;
    }

    .station-address {
      font-size: 26rpx;
      color: #999;
    }
  }

  .bottom {
    display: flex;
    align-items: flex-start;
    margin-top: 2.5vh;

    .bottom-divider {
      width: 2rpx;
      height: 80rpx;
      background-color: #d1d1d1;
      margin-top: 20rpx;
    }

    .bottom-left,
    .bottom-right {
      flex: 1;

      .info-item {
        align-items: center;
        padding: 10rpx 0;

        .info-label {
          font-size: 24rpx;
          color: #808080;
        }

        .info-value-row {
          margin-top: 16rpx;
          display: flex;
          align-items: center;
          gap: 12rpx;

          .info-value {
            font-size: 30rpx;
            color: #333;
            font-weight: bold;
          }

          .badge-icon {
            width: 40rpx;
          }
        }

        .parking-benefit {
          margin-top: 16rpx;
          font-size: 24rpx;
          color: #333;
          font-weight: bold;

          .parking-benefit-text {
            font-size: 30rpx;
          }
        }
      }
    }

    .bottom-right {
      margin-left: 60rpx;
    }
  }
}

/* 我的爱车部分 */
.my-car-section {
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 0 30rpx;
  height: 86rpx;
  display: flex;
  align-items: center;
  .my-car-bg {
    background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 10rpx;
    margin-left: -10rpx;
    border-radius: 8px;
    padding-left: 20rpx;
    .plus-img {
      width: 43rpx;
      height: 43rpx;
    }
    .section-title {
      font-size: 30rpx;
      color: #333;
      font-weight: 400;
      background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
    }

    .car-selector {
      display: flex;
      align-items: center;
      gap: 12rpx;

      .car-number {
        font-size: 24rpx;
        color: #72727d;
      }
    }
  }
}

/* 充电费用部分 */
.charging-fee-section {
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 22rpx 30rpx 30rpx 30rpx;

  .section-title {
    margin-left: -10rpx;
    background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
    border-radius: 8px;
    height: 78rpx;
    line-height: 78rpx;
    border-bottom: none;
    padding-left: 20rpx;
    font-size: 30rpx;
    font-weight: 400;
  }

  .current-period {
    margin-bottom: 16rpx;

    .period-label {
      font-size: 24rpx;
      color: #333;
    }
  }

  .fee-display {
    margin-bottom: 16rpx;

    .fee-symbol,
    .fee-unit {
      font-size: 24rpx;
      color: #ff6b35;
    }

    .fee-value {
      font-size: 48rpx;
      font-weight: 600;
      color: #ff6b35;
    }
  }

  .fee-schedule {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
    border-radius: 8px;
    padding: 10rpx;
    margin-left: -10rpx;
    padding-left: 20rpx;
    .schedule-info {
      font-size: 24rpx;
      color: #999;
    }

    .fee-details {
      display: flex;
      align-items: center;
      gap: 8rpx;

      .details-text {
        font-size: 20rpx;
        color: #72727d;
      }
    }
  }
}

/* 支付方式部分 */
.payment-section {
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 0 30rpx 30rpx 30rpx;

  .section-title {
    height: 78rpx;
    line-height: 78rpx;
    border-bottom: none;
  }

  .payment-item {
    width: 100%;
    height: 96rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;

    &:last-child {
      margin-bottom: 0;
    }

    .payment-info {
      display: flex;
      align-items: center;
      flex: 1;

      .payment-icon {
        width: 50rpx;
        height: 50rpx;
        margin-right: 16rpx;
      }

      .payment-details {
        flex: 1;

        .payment-name {
          font-size: 28rpx;
          color: #3d3d3d;
          display: block;
          margin-bottom: 6rpx;
        }

        .payment-desc {
          font-size: 24rpx;
          color: #72727d;
        }

        .payment-desc-row {
          display: flex;
          align-items: center;
          gap: 20rpx;

          .balance-amount {
            font-size: 24rpx;
            color: #72727d;
          }

          .recharge-link {
            display: flex;
            align-items: center;
            gap: 4rpx;

            .recharge-text {
              font-size: 20rpx;
              color: #007aff;
            }
          }
        }
      }
    }

    .payment-radio {
      width: 32rpx;
      height: 32rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      image {
        width: 32rpx;
        height: 32rpx;
      }
    }
  }
}

/* 底部开始充电按钮 */
.bottom-action {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  padding: 20rpx 30rpx 40rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);

  .start-charging-btn {
    width: 100%;
    height: 92rpx;
    background-color: #333;
    color: white;
    border: none;
    border-radius: 46rpx;
    font-size: 28rpx;
    font-weight: bold;
    line-height: 92rpx;
    text-align: center;
  }
}

/* 底部弹出的倒计时组件 */
.countdown-popup {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50vh;
  background-color: white;
  border-radius: 30rpx 30rpx 0 0;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
  transform: translateY(100%);
  transition: transform 0.3s ease-in-out;
  z-index: 1000;

  &.show {
    transform: translateY(0);
  }

  .popup-content {
    text-align: center;
    padding: 75rpx 60rpx 100rpx;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .countdown-circle {
      width: 250rpx;
      height: 250rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 32rpx;
      position: relative;

      .countdown-bg {
        position: absolute;
        width: 100%;
        height: 100%;
        transition: transform 0.5s ease-in-out;
      }

      .countdown-text {
        font-size: 50rpx;
        font-weight: 600;
        color: #4cd964;
        position: relative;
        z-index: 1;
      }
    }

    .countdown-title {
      font-size: 36rpx;
      color: #333;
      font-weight: 600;
      margin-bottom: 60rpx;
    }

    .countdown-desc {
      font-size: 26rpx;
      color: #72727d;
      line-height: 50rpx;
    }
  }
}
</style>
