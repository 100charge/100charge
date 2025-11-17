<template>
  <view class="charging-page">
    <!-- 导航栏 -->
    <u-navbar
      title="充电中"
      bgColor="transparent"
      :safeAreaInsetTop="true"
      :border="false"
      :placeholder="true"
      :autoBack="true"
      :titleStyle="{ color: '#fff' }"
      leftIconColor="#fff"
    >
    </u-navbar>
    <!-- gif 背景 -->
    <image class="bg-gif" src="https://demofile.zhitancloud.com/demo-file/2025/11/14/chongdiannew_20251114155417A009.gif" mode="widthFix"></image>

    <!-- 上半部分：时间信息 -->
    <view class="time-section" v-if="socketData.status == 'CHARGING'">
      <!-- 已充时长 -->
      <view class="charge-time">
        <text>已充时长</text>
      </view>

      <!-- 时间显示 -->
      <view class="time-display">
        <text class="time-text">{{ socketData.duration || 0 }}分钟</text>
      </view>

      <!-- 预计还有时间 -->
      <view class="estimate-time">
        <text>预计还有 </text>
        <text class="highlight">{{ socketData.timeLeft || 0 }}</text>
        <text> 分钟充满</text>
      </view>
    </view>
    <view v-else class="time-section">
      <view class="charge-time">
        <text>充电已中断</text>
      </view>
    </view>

    <!-- 中间部分：留空给背景gif显示 -->

    <!-- 底部白色卡片区域 -->
    <view class="bottom-cards">
      <!-- 充电站信息卡片 -->
      <view class="station-card">
        <view class="station-name">{{ orderInfo.stationName || "--" }}</view>
        <view class="station-details">
          <text>充电桩 {{ orderInfo.deviceNo || "--" }}</text>
          <text style="margin-left: 40rpx">充电枪号 {{ orderInfo.gunsNo || "--" }}</text>
        </view>

        <!-- 充电数据 -->
        <view class="charging-stats">
          <view class="stat-item">
            <view class="stat-value">{{ socketData.totalPower || 0 }}</view>
            <view class="stat-label">已充电量（度）</view>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <view class="stat-value">{{ socketData.totalAmount || 0 }}</view>
            <view class="stat-label">预估费用（元）</view>
          </view>
        </view>
      </view>

      <!-- 实时功率卡片 -->
      <view class="power-card">
        <view class="power-header">
          <text class="power-title">实时功率</text>
          <!-- <view class="power-link">
            <text class="link-text">充电曲线</text>
            <u-icon name="arrow-right" size="12" color="#999"></u-icon>
          </view> -->
        </view>
        <view class="power-content">
          <text class="power-main">{{ socketData.totalPower || 0 }}kW</text>
          <text class="power-sub">{{ socketData.outputVoltage || 0 }}V/{{ socketData.outputCurrent || 0 }}A</text>
        </view>
      </view>
    </view>

    <!-- 底部结束充电按钮 -->
    <view class="bottom-action">
      <view class="confirm-btn" :class="disabled ? 'disabled-btn' : 'confirm-btn'" @click="closeCharging">
        结束充电
      </view>
    </view>

    <chargingCountdown ref="chargingCountdownRef" />
  </view>
</template>
<script>
import chargingCountdown from "./components/charging-countdown.vue"
import { endCharge, getOrderInfoByTradeNo, getRealtimeData } from "@/config/api.js"
export default {
  components: {
    chargingCountdown,
  },
  data() {
    return {
      // OFFLINE(0, "OFFLINE", "离线"),
      // FAULT(1, "FAULT", "故障"),
      // ONLINE(2, "ONLINE", "在线/空闲"),
      // STARTING(3, "STARTING", "启动中"),
      // CHARGING(4, "CHARGING", "充电中");
      // CHARGING(5, "CHARGING", "插枪");
      // FINISHED 6 已结束
      orderNo: "",
      orderInfo: {},
      socketData: {},
      heartbeatInterval: null, // 定时器
      disabled: false,
    }
  },
  watch: {
    socketData: {
      handler(val, oldval) {
        if (val.status === "FINISHED") {
          uni.showToast({
            title: "充电已停止",
            icon: "none",
          })
          clearInterval(this.heartbeatInterval)
          setTimeout(() => {
            uni.redirectTo({
              // url: "/pages/stations/orders/orderDetail?orderNo=" + this.orderNo,
              url: "/pages/stations/orders/orderList?orderNo=" + this.orderNo,
            })
          }, 2000)
        }
      },
      immediate: false,
      deep: true,
    },
  },
  onLoad(option) {
    this.orderNo = option.orderNo || ""
    this.getOrderStatus()
  },
  onUnload() {
    clearInterval(this.heartbeatInterval)
    this.heartbeatInterval = null
  },
  methods: {
    getOrderStatus() {
      if (!this.orderNo) {
        return
      }
      getOrderInfoByTradeNo({
        tradeNo: this.orderNo,
      }).then((res) => {
        this.orderInfo = res.data
        // NONE(-1, "未知"),
        // NO_START(0, "未开始"),
        // CHARGING(1, "充电中"),
        // GUN_NOT_DRAWN(2, "充电结束（未拔枪）"),
        // CHARGING_COMPLETED(3, "充电结束"),
        // BILL_HAS_BEEN_UPLOADED(4, "账单已上传"),
        // ABNORMAL(5, "异常");
        this.getRealtimeDataApi()
        this.heartbeatInterval = setInterval(() => {
          // 获取实时数据
          this.getRealtimeDataApi()
        }, 7500)
      })
    },
    getRealtimeDataApi() {
      getRealtimeData({
        deviceNo: this.orderInfo.deviceNo,
        gunNo: this.orderInfo.gunsNo,
        operatorId: this.orderInfo.operatorId,
      }).then((res) => {
        console.log("getRealtimeData", res)
        this.socketData = res.data || {}
      })
    },

    closeCharging() {
      let that = this
      // 模拟结束充电
      that.$refs.chargingCountdownRef.onOpen({
        type: "end",
        orderNo: this.orderNo,
      })
      setTimeout(() => {
        that.$refs.chargingCountdownRef.close()
        uni.redirectTo({
          // url: "/pages/stations/orders/orderDetail?orderNo=" + this.orderNo,
          url: "/pages/stations/orders/orderList?orderNo=" + this.orderNo,
        })
        that.disabled = false
      }, 7500)
      return

      // 实际逻辑
      if (this.disabled) {
        return
      }
      this.disabled = true
      endCharge({
        tradeNo: this.orderNo,
      })
        .then((res) => {
          if (res.code === 200) {
            that.$refs.chargingCountdownRef.onOpen({
              type: "end",
              orderNo: this.orderNo,
            })
            setTimeout(() => {
              that.$refs.chargingCountdownRef.close()
              uni.redirectTo({
                // url: "/pages/stations/orders/orderDetail?orderNo=" + this.orderNo,
                url: "/pages/stations/orders/orderList?orderNo=" + this.orderNo,
              })
              that.disabled = false
            }, 7500)
          }
        })
        .catch((e) => {
          console.log(e)
          that.disabled = false
        })
    },
  },
}
</script>

<style scoped>
page {
  background: transparent;
}

.charging-page {
  min-height: 100vh;
  position: relative;
  padding-bottom: 200rpx;
  background: linear-gradient(180deg, #a1a4ad 0%, #ffffff 100%);
}

/* gif 动画 */
.bg-gif {
  width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
  position: absolute;
  top: 0;
}

.charging-indicator {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background-color: #4cd964;
  position: relative;
}

.charging-indicator::after {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #4cd964;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}

/* 上半部分：时间信息 */
.time-section {
  position: absolute;
  top: 180rpx;
  left: 0;
  right: 0;
  text-align: center;
  z-index: 10;
  padding: 0 40rpx;
}

.charge-time {
  color: white;
  font-size: 22rpx;
  margin-bottom: 20rpx;
}

.time-display {
  margin-bottom: 24rpx;
}

.time-text {
  font-size: 80rpx;
  font-weight: bold;
  color: white;
  letter-spacing: 8rpx;
  text-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.3);
}

.estimate-time {
  color: white;
  font-size: 26rpx;
  margin-bottom: 0;
}

.highlight {
  color: #43d0a3;
  font-weight: bold;
}

/* 中间汽车区域 */
.car-section {
  height: 400rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40rpx;
  padding: 0 40rpx;
}

/* 底部卡片区域 */
.bottom-cards {
  margin-top: 580rpx;
  padding: 20rpx;

  position: relative;
}

/* 充电站信息卡片 */
.station-card {
  background-image: url("/static/img/shap.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 16rpx;
  padding: 32rpx 24rpx;
  margin: 0 0rpx 20rpx 0rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
}

.station-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 600;
  margin-bottom: 12rpx;
  text-align: left;
  line-height: 1.4;
}

.station-details {
  font-size: 22rpx;
  color: #666;
  text-align: left;
  margin-bottom: 32rpx;
  line-height: 1.3;
  padding-bottom: 50rpx;
}

/* 充电统计数据 */
.charging-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20rpx;
}

.stat-item {
  text-align: center;
  flex: 1;
  position: relative;
}

.stat-value {
  font-size: 44rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
  letter-spacing: 1rpx;
}

.stat-label {
  font-size: 22rpx;
  color: #666;
  line-height: 1.2;
}

.stat-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: #e5e5e5;
  margin: 0 24rpx;
}

/* 实时功率卡片 */
.power-card {
  background-color: white;
  border-radius: 24rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.power-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.power-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.power-link {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.link-text {
  font-size: 24rpx;
  color: #999;
}

.power-content {
  display: flex;
  align-items: baseline;
  gap: 30rpx;
}

.power-main {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.power-sub {
  font-size: 24rpx;
  color: #666;
}

/* 底部操作按钮 */
.bottom-action {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;

  padding: 30rpx;
}

.end-charging-btn {
  width: 100%;
  height: 88rpx;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 44rpx;
  font-size: 32rpx;
  font-weight: bold;
  line-height: 88rpx;
  text-align: center;
}

/* 结束充电倒计时弹窗 */
.end-countdown-popup {
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
}

.end-countdown-popup.show {
  transform: translateY(0);
}

.end-popup-content {
  text-align: center;
  padding: 75rpx 60rpx 100rpx;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.end-countdown-circle {
  width: 250rpx;
  height: 250rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 32rpx;
  position: relative;
}

.end-countdown-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  transition: transform 0.5s ease-in-out;
}

.end-countdown-text {
  font-size: 50rpx;
  font-weight: 600;
  color: #ff6b35;
  position: relative;
  z-index: 1;
}

.end-countdown-title {
  font-size: 36rpx;
  color: #333;
  font-weight: 600;
  margin-bottom: 60rpx;
}

.end-countdown-desc {
  font-size: 26rpx;
  color: #72727d;
  line-height: 50rpx;
}
</style>
