<template>
  <view
    class="coupon-item flex-row flex-ali-center"
    :class="couponItem.status != 0 ? 'coupon-item-disabled' : ''"
    :style="'background-image:url(' + (couponItem.status != 0 ? couponBgImgDisabled : couponBgImg) + ')'"
  >
    <view class="coupon-left">
      <view class="price flex-row">
        <text class="unit" v-if="couponItem.couponType != 1">￥</text>
        {{ couponItem.couponType == 1 ? couponItem.discountPercentage : couponItem.giftAmount }}
        <text class="unit" v-if="couponItem.couponType == 1">%</text>
      </view>
      <view class="coupon-tip" v-if="couponItem.couponType == 0"> 满{{ couponItem.totalAmount }}可用 </view>
    </view>

    <view class="coupon-rigth flex-row flex-jst-btw flex-ali-center">
      <view class="coupon-info">
        <view class="name">
          {{ couponItem.name }}
        </view>
        <view class="time-tip">{{ couponItem.applicableScopeDesc }}</view>
        <view class="text-time"> {{ couponItem.expireTime }} 23:59:59前到期 </view>
        <view
          class="text-station"
          v-if="couponItem.status == 0 && couponItem.stationList && couponItem.stationList.length"
        >
          可用场站：{{ couponItem.station }}
        </view>
      </view>
      <view class="coupon-status">
        <view class="" v-if="isOrder">
          <view
            class="coupon-btn"
            :class="{ 'coupon-active': selectedCoupon.id == couponItem.id }"
            @click="handleCoupon"
          >
            {{ selectedCoupon.id == couponItem.id ? "已选中" : "选择" }}
          </view>
        </view>
        <view class="" v-else>
          <view @click="toHome" class="coupon-btn coupon-active" v-if="couponItem.status == 0"> 去使用 </view>
          <view class="coupon-btn" v-if="couponItem.status == 1"> 已使用 </view>
          <view class="coupon-btn coupon-expire" v-if="couponItem.status == 2"> 已过期 </view>
        </view>

        <!-- <view class="end-tip-expire" v-if="couponItem.status == 2"> 已过期 </view> -->
        <!-- <view class="end-tip" v-else> {{couponItem.stackable ? '可叠加使用' : '不可叠加'}} </view> -->
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "couponItemPage",
  props: {
    couponItem: {
      default() {
        return {}
      },
    },
    isOrder: {
      default: false,
      type: Boolean,
    },
    selectedCoupon: {
      default: () => {},
      type: Object,
    },
  },

  data() {
    return {
      couponBgImg: require("../../static/img/youhui.png"),
      couponBgImgDisabled: require("../../static/images/mine/coupon-bg-disabled.png"),
    }
  },

  methods: {
    toHome() {
      uni.switchTab({
        url: "/pages/index/index",
      })
    },
    handleCoupon() {
      this.$emit("selectCoupon", this.couponItem)
    },
  },
}
</script>
<style lang="scss" scoped>
.coupon-item,
.coupon-item-disabled {
  min-height: 152rpx;
  padding: 16rpx 0;
  background-size: 100% 100%;
  color: #ff3b14;
  margin-bottom: 20rpx;
  .coupon-left {
    width: 162rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;

    .price {
      font-weight: 700;
      font-size: 50rpx;
      line-height: 38rpx;
      display: flex;
      align-items: flex-end;

      .unit {
        font-size: 24rpx;
        margin-bottom: -6rpx;
      }
    }

    .coupon-tip {
      font-size: 20rpx;
      line-height: 20rpx;
      padding-top: 14rpx;
    }
  }

  .coupon-rigth {
    // padding: 22rpx 24rpx 20rpx 36rpx;
    margin-left: 36rpx;
    // padding-right: 24rpx;
    flex: 1;

    .coupon-info {
      flex: 1;
      .name {
        font-size: 28rpx;
        color: #333;
        line-height: 28rpx;
        margin-bottom: 12rpx;
        max-width: 360rpx;
        font-weight: 700;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-all; // 数字 字母换行
      }
      .time-tip {
        font-size: 20rpx;
        color: $basic-color;
        line-height: 21rpx;
        margin-top: 16rpx;
      }
      .text-time {
        font-size: 20rpx;
        color: #999999;
        line-height: 20rpx;
        margin-top: 16rpx;
      }
      .text-station {
        font-size: 18rpx;
        color: #999999;
        line-height: 24rpx;
        margin-top: 16rpx;
      }
    }

    .coupon-status {
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      margin-top: 24rpx;
      width: 110rpx;
      margin-right: 20rpx;
      .coupon-btn {
        width: 96rpx;
        height: 46rpx;
        border-radius: 12rpx;
        line-height: 46rpx;
        text-align: center;
        font-size: 22rpx;
        color: $basic-color;
        background: #ffffff;
        margin-bottom: 8rpx;
      }

      .coupon-active {
        color: #ffffff;
        background: #ff6b01;
      }

      .coupon-expire {
        color: #999;
        border: 1rpx solid #f0f0f0;
        background-color: #f0f0f0;
      }

      .end-tip {
        font-size: 20rpx;
        color: $primary-color;
        line-height: 20rpx;
      }
      .end-tip-expire {
        font-size: 20rpx;
        color: $basic-color;
        line-height: 20rpx;
      }
    }
  }
}
.coupon-item-disabled {
  color: $basic-color;
  .coupon-rigth .coupon-info .name {
    color: $basic-color;
  }
}
</style>
