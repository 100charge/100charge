<template>
  <u-action-sheet :safeAreaInsetBottom="true" :title="title" :show="showPopup" round="30" @close="close">
    <view class="popup-content">
      <view class="table-view">
        <view class="th-header flex-row flex-jst-btw flex-ali-center">
          <text style="width: 150rpx">
            <text style="margin-left: 10rpx">充电时段</text>
          </text>
          <text style="width: 140rpx">
            <text style="margin-left: 10rpx">类型</text>
          </text>
          <text style="width: 80rpx">价格(元/度)</text>
          <text>=</text>
          <text style="width: 80rpx">电费(元/度)</text>
          <text>+</text>
          <text style="width: 80rpx">服务费(元/度)</text>
        </view>
        <view class="table-cell-list">
          <scroll-view scroll-y="true" style="height: 50vh" :scroll-into-view="toView" scroll-with-animation="true">
            <view
              class="table-cell-item flex-row flex-jst-btw flex-ali-center"
              v-for="(item, index) in timeList"
              :key="index"
            >
              <text style="width: 140rpx">{{ item.timePeriod }}</text>
              <text style="width: 75rpx">市场价</text>
              <text class="price" style="width: 90rpx">{{ item.totalAmount || 0 }}</text>
              <text style="width: 90rpx">{{ item.electricityFee || 0 }}</text>
              <text style="width: 90rpx">{{ item.serviceFee || 0 }}</text>

              <view v-if="item.currentTimePeriod" class="tag-current" id="currentTimeId"> 当前 </view>
              <view v-else-if="item.lowestPrice" class="tag-low"> 最低 </view>
            </view>
          </scroll-view>
        </view>
      </view>
      <view class="tip-text flex-row flex-jst-start flex-ali-start">
        <u-icon name="error-circle-fill" size="12"></u-icon>
        <view class="" style="margin-top: -6rpx; margin-left: 12rpx">
          由于服务运营成本等综合影响，平台浮动收费服务费采用时段计费，不同站内暂存在在价差异。
        </view>
      </view>
    </view>
  </u-action-sheet>
</template>
<script>
import { getStationRuleById } from "@/config/api.js"
export default {
  name: "station-price",
  data() {
    return {
      title: "价格详情",
      showPopup: false,
      timeList: [],
      toView: "",
    }
  },

  methods: {
    onOpen(params) {
      console.log(params)
      getStationRuleById({ stationId: params.id }).then((res) => {
        this.showPopup = true
        this.timeList = res.data
        this.$nextTick(() => {
          console.log("nextTick")
          this.toView = "currentTimeId"
        })
      })
    },
    close() {
      this.toView = ""
      this.showPopup = false
    },
  },
}
</script>
<style lang="scss" scoped>
.popup-content {
  // height: 70vh;
  padding: 24rpx;
  .tip-text {
    font-weight: 400;
    font-size: 22rpx;
    color: $basic-color;
    line-height: 36rpx;
    text-align: left;
    margin-top: 32rpx;
    margin-bottom: 24rpx;
  }
}
.table-view {
  .th-header {
    color: $basic-color;
    font-size: 24rpx;
    text-align: center;
    white-space: normal;
    word-break: break-all;
    padding-right: 14rpx;
    padding-bottom: 20rpx;
  }
  .table-cell-list {
    text-align: center;
    .table-cell-item {
      height: 98rpx;
      background: #f8f8f8;
      border-radius: 8rpx;
      margin-top: 26rpx;
      padding: 0 16rpx;
      font-size: 22rpx;
      color: #333;
      position: relative;
      .tag-current {
        position: absolute;
        top: 0;
        right: 0;
        width: 88rpx;
        height: 36rpx;
        font-size: 20rpx;
        color: #fff;
        background: #000;
        border-radius: 0 16rpx 0 16rpx;
      }
      .tag-low {
        position: absolute;
        top: 0;
        right: 0;
        width: 88rpx;
        height: 36rpx;
        font-size: 20rpx;
        color: #fff;
        background: #19a0ff;
        border-radius: 0 16rpx 0 16rpx;
      }
      .price {
        font-size: 34rpx;
        font-weight: 600;
      }
    }
  }
}
</style>
