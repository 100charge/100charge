<template>
  <view class="station-item" @click.stop="toStationDetail">
    <view class="station-header">
      <text class="station-name">{{ stationItem.name }}</text>
      <view class="station-distance" @tap.stop="locationHandle">
        <image class="distance-icon" src="/static/img/daohang.png" mode="aspectFit"></image>
        <text class="distance-text">{{ stationItem.distance }}</text>
      </view>
    </view>
    <view class="station-details">
      <text class="rating">{{ stationItem.starLabel || 5 }}分 |</text>
      <!-- <text class="last-charge">最近充电{{ stationItem.lastCharge }}分钟前 |</text> -->
      <text class="public-access">{{ stationItem.chargingType }} |</text>
      <text class="special-offer">充电特惠 |</text>
      <text class="public-access">对外开放</text>
    </view>
    <view class="charger-availability">
      <view class="charger-type">
        <view class="charger-icon-wrapper fast">
          <image src="/static/img/kuai.png" class="badge-icon"></image>
        </view>
        <text class="charger-status"
          ><text class="charger-status-text">闲{{ stationItem.idleFastChargeCount }}</text> /{{
            stationItem.fastCharging
          }}</text
        >
      </view>
      <view class="line-shu"></view>
      <view class="charger-type">
        <view class="charger-icon-wrapper slow">
          <image src="/static/img/low.png" class="badge-icon"></image>
        </view>
        <text class="charger-status"
          ><text class="charger-status-blue">闲{{ stationItem.idleSlowChargeCount }}</text> /{{
            stationItem.slowCharging
          }}</text
        >
      </view>
    </view>
    <view class="station-footer">
      <view class="price-wrapper">
        <text class="price-symbol">¥</text>
        <text class="price-value">{{ stationItem.fee }}</text>
        <text class="price-unit">/度</text>
      </view>
      <view class="navigate-btn" @tap.stop="locationHandle">
        <image src="/static/img/daohang-logo.png" class="nav-icons" mode="heightFix"></image>
      </view>
    </view>
  </view>
</template>
<script>
import app from "@/static/js/app.js"
export default {
  name: "home-station-card",
  props: {
    stationItem: {
      default() {
        return {}
      },
    },
  },

  data() {
    return {
      toDecimal2: app.toDecimal2,
    }
  },

  methods: {
    toStationDetail() {
      uni.navigateTo({
        url: "/pages/stations/site/stationDetail?id=" + this.stationItem.stationId,
      })
    },

    locationHandle() {
      uni.openLocation({
        latitude: this.stationItem.lat,
        longitude: this.stationItem.lng,
        name: this.stationItem.name, // 位置名 alipay必填
        address: this.stationItem.address, // 详细地址 alipay必填
        success: function () {},
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.station-item {
  background-color: rgba(255, 255, 255, 1);
  border-radius: 20rpx;
  padding: 25rpx 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

  .station-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15rpx;

    .station-name {
      font-size: 32rpx;
      color: rgba(26, 26, 26, 1);
      font-family: SourceHanSansSC-Medium;
      font-weight: 500;
      flex: 1;
      line-height: 47rpx;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .station-distance {
      display: flex;
      align-items: center;
      gap: 5rpx;
      background-color: #f9f9f9;
      border-radius: 24rpx;
      padding: 5rpx 10rpx;

      .distance-icon {
        width: 24rpx;
        height: 24rpx;
        margin-right: 5rpx;
      }

      .distance-text {
        font-size: 24rpx;
        color: rgb(0, 0, 0);
        font-weight: bold;
      }
    }
  }

  .station-details {
    display: flex;
    flex-wrap: wrap;
    gap: 12rpx;
    margin-bottom: 15rpx;
    align-items: center;

    .rating,
    .last-charge,
    .special-offer,
    .public-access {
      font-size: 22rpx;
      color: rgba(165, 165, 165, 1);
      font-family: SourceHanSansSC-Regular;
      font-weight: normal;
      line-height: 33rpx;
    }

    .rating {
      color: rgba(165, 165, 165, 1);
    }

    .special-offer {
      color: rgba(255, 107, 53, 1);
    }
  }

  .charger-availability {
    display: flex;
    gap: 25rpx;
    margin-bottom: 15rpx;
    background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
    border-radius: 12rpx;

    .charger-type {
      display: flex;
      align-items: center;
      gap: 8rpx;
      background-color: rgba(249, 249, 249, 1);
      border-radius: 24rpx;
      padding: 12rpx 12rpx;

      .charger-icon-wrapper {
        width: 40rpx;
        height: 40rpx;
        border-radius: 8rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 5rpx;

        &.fast {
          background-color: rgba(255, 132, 20, 1);
          width: 36rpx;
          height: 36rpx;
          image {
            width: 36rpx;
            height: 36rpx;
          }
        }

        &.slow {
          background-color: rgba(0, 151, 255, 1);
          width: 36rpx;
          height: 36rpx;
          image {
            width: 36rpx;
            height: 36rpx;
          }
        }

        .charger-icon-text {
          font-size: 22rpx;
          color: rgba(255, 255, 255, 1);
          font-family: YouSheBiaoTiHei;
          font-weight: normal;
          line-height: 29rpx;
        }
      }

      .charger-status {
        font-size: 26rpx;
        color: #4f4f4f;
        font-family: SourceHanSansSC-Medium;
        font-weight: 500;
        line-height: 33rpx;

        .charger-status-text {
          color: rgba(255, 132, 20, 1);
        }

        .charger-status-blue {
          color: rgba(0, 151, 255, 1);
        }
      }
    }
    .line-shu {
      margin-top: 20rpx;
      width: 1rpx;
      height: 20rpx;
      background-color: #d0d0d0;
    }
  }

  .station-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20rpx;

    .price-wrapper {
      display: flex;
      align-items: baseline;
      gap: 2rpx;

      .price-symbol {
        font-size: 22rpx;
        color: #ff3b14;
        font-family: SourceHanSansSC-Bold;
        font-weight: 700;
        line-height: 33rpx;
      }

      .price-value {
        font-size: 38rpx;
        color: #ff3b14;
        font-family: Arial-BoldMT;
        font-weight: bold;
        line-height: 43rpx;
      }

      .price-unit {
        font-size: 22rpx;
        color: #ff3b14;
        font-family: SourceHanSansSC-Regular;
        font-weight: normal;
        line-height: 33rpx;
      }
    }

    .navigate-btn {
      .nav-icons {
        height: 42rpx;
      }
    }
  }
}
</style>
