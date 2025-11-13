<template>
  <u-action-sheet
    :safeAreaInsetBottom="true"
    :title="title"
    :show="showPopup"
    round="30"
    bgColor="#F4F4F4"
    @close="close"
  >
    <view class="popup-content">
      <scroll-view scroll-y="true" style="height: 60vh" v-if="pileList.length !== 0">
        <view v-for="(item, index) in pileList" :key="item.id" class="pile-item">
          <view class="pile-left">
            <view
              class="circle-status"
              :style="{
                color: item.color,
                borderColor: item.circleColor,
                background: item.deviceStatus == 4 ? '#fff' : '',
              }"
            >
              {{ item.deviceStatus == 4 ? item.ratio + "%\n充电中" : item.deviceStatusName }}
            </view>
          </view>
          <view class="pile-right">
            <view class="info-item">
              <text class="label">终端编号：</text>
              <text class="value">{{ item.deviceNo }}</text>
            </view>
            <view class="info-item">
              <text class="label">最大功率：</text>
              <text class="value">{{ item.maxPower }} kW</text>
            </view>
            <view class="info-item">
              <text class="value">{{ item.pileType }}</text>
            </view>
          </view>
        </view>
      </scroll-view>
      <view v-else class="empty-wrapper">
        <image src="/static/images/index/empty.png" mode="aspectFit"></image>
        <view class="empty-text">暂无电桩</view>
      </view>
    </view>
  </u-action-sheet>
</template>
<script>
import { getStationDevice } from "@/config/api.js"

export default {
  name: "pile-list",
  components: {},
  data() {
    return {
      title: "电桩信息",
      showPopup: false,
      pileList: [],
    }
  },

  methods: {
    onOpen(params) {
      // this.title = params.type == "AC" ? "慢充电桩" : "快充电桩"
      getStationDevice({
        stationId: params.id,
        type: params.type, // AC是慢充、DC是快充
      }).then((res) => {
        this.showPopup = true
        this.title = "充电终端（" + res.data.length + "）"
        this.pileList = res.data
          ? res.data.map((item) => {
              return {
                ...item,
                deviceStatus: item.deviceStatus || 0,
                deviceStatusName: item.deviceStatusName || "离线",
                color: this.getColor(item.deviceStatus || 0),
                circleColor: item.deviceStatus ? this.getColor(item.deviceStatus) : "#d8d8d8",
                ratio: item.ratio || 0,
              }
            })
          : []
      })
    },
    getColor(status) {
      // 0 离线-999999
      // 1 故障-FA5151
      // 2 空闲-85E2C2
      // 3 启动中-FF6B01
      // 4 充电中-FF6B01
      // 5 已插枪-FF6B01
      switch (status) {
        case 0:
          return "#999999"
        case 1:
          return "#FA5151"
        case 2:
          return "#20d26a"
        case 3:
          return "#FF6B01"
        case 4:
          return "#149fff"
        case 5:
          return "#FF6B01"
        default:
          break
      }
    },
    close() {
      this.showPopup = false
    },
    onClipboard(index) {
      console.log("code", index)
      let code = this.pileList[index].deviceNo
      uni.setClipboardData({
        data: code,
        success: () => {},
        complete: (e) => {
          console.log(e)
        },
      })
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep .u-popup__content {
  background-color: #fff;
}
.popup-content {
  // height: 70vh;
  background-color: #fff;
  padding: 24rpx;
  padding-bottom: 48rpx;
  .pile-item {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 20rpx;
    // background-color: #fff;
    margin-bottom: 24rpx;
    border-radius: 20rpx;
  }
  .pile-left {
    .circle-progress-percent {
      color: $primary-color;
      font-size: 24rpx;
    }

    .circle-status {
      height: 120rpx;
      width: 120rpx;
      border-radius: 50%;
      border: 10rpx solid #d8d8d8;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 24rpx;
    }
  }
  .pile-right {
    margin-left: 24rpx;
    font-weight: 400;
    font-size: 24rpx;

    .info-item {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      line-height: 40rpx;
    }

    .label {
      width: 120rpx;
      color: $basic-color;
      text-align: left;
    }
    .value {
      color: #333;
      flex: 1;
      text-align: left;
    }
  }
}
</style>
