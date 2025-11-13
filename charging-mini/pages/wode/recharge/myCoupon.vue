<template>
  <view class="coupon-page main">
    <view class="mu-tabs">
      <view class="mu-tabs-box">
        <view class="order-tabs" :class="tabIndex === 0 ? 'active-tab' : ''" @click="sTab(0)">未使用 </view>
        <view class="order-tabs" :class="tabIndex === 1 ? 'active-tab' : ''" @click="sTab(1)">已使用 </view>
        <view class="order-tabs" :class="tabIndex === 2 ? 'active-tab' : ''" @click="sTab(2)">已过期 </view>
      </view>
    </view>
    <view class="coupon-list" v-if="list && list.length !== 0">
      <view v-for="(item, index) in list" :key="index">
        <couponItem :coupon-item="item" />
      </view>
      <u-loadmore :status="loadStatus" />
    </view>

    <view class="empty-wrapper" style="padding-top: 200rpx" v-if="list.length === 0">
      <image src="/static/images/index/empty.png" mode="aspectFit"></image>
      <view class="empty-text">暂无优惠券</view>
    </view>
  </view>
</template>

<script>
import { appUserPromotionList } from "@/config/api.js"
import couponItem from "@/components/coupon-item/coupon-item.vue"
export default {
  components: {
    couponItem,
  },
  data() {
    return {
      loadStatus: "loadmore",
      list: [],
      tabIndex: 0,
      totalRows: 0,
      modeFade: "fade-right",
      pageNum: 1,
    }
  },
  onReady() {
    this.sTab(0)
  },
  watch: {
    tabIndex: function (newValue, oldValue) {
      if (newValue > oldValue) {
        this.modeFade = "fade-left"
      } else {
        this.modeFade = "fade-right"
      }
    },
  },
  //下拉刷新
  onPullDownRefresh() {
    this.sTab(this.tabIndex)
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onReachBottom() {
    if (this.pageNum * 10 < this.totalRows) {
      this.loadStatus = "loading"
      this.pageNum++
      setTimeout(() => {
        this.couponList()
      }, 100)
    } else {
      this.loadStatus = "nomore"
    }
  },
  methods: {
    sTab(index) {
      this.pageNum = 1
      this.tabIndex = index
      this.couponList()
    },
    /**
     * 获取优惠券列表
     */
    couponList() {
      appUserPromotionList({
        status: this.tabIndex,
        pageNum: this.pageNum,
        pageSize: 10,
      }).then((res) => {
        if (res.code == 200) {
          this.totalRows = res.total
          let arr = res.rows.map((item) => {
            return {
              ...item,
              // discountPercentage:
              //   item.couponType == 1 ? (item.discountPercentage * 1) / 10 + "折" : item.discountPercentage,
              station: item.stationList
                ? item.stationList
                    .map((item) => {
                      return item.name
                    })
                    .join(",")
                : "",
            }
          })
          if (this.pageNum === 1) {
            this.list = arr || []
          } else {
            this.list.push(...arr)
          }
          console.log(this.list)
        }
      })
    },
    goHome() {
      uni.switchTab({
        url: "/pages/index/index",
      })
    },
  },
}
</script>

<style>
page {
  background-color: #fff;
}
</style>
<style lang="scss" scoped>
.coupon-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  .coupon-list {
    padding: 0 24rpx;
    padding-top: 120rpx;
    padding-bottom: 80rpx;
  }
}

.mu-tabs {
  padding: 20rpx 96rpx 0;
  background-color: #fff;
  position: fixed;
  // top: 0;
  right: 0;
  left: 0;

  .mu-tabs-box {
    box-sizing: border-box;
    padding: 6rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 32rpx;

    .order-tabs {
      text-align: center;
      color: #444444;
      padding-bottom: 16rpx;
      font-size: 26rpx;
    }

    .active-tab {
      border-bottom: 2px solid $primary-color;
      font-weight: 600;
      font-size: 28rpx;
      color: $primary-color;
    }
  }
}

.coupon-page .mu-tabs .item {
  padding: 5px 10px;
  line-height: 35px;
  text-align: center;
  border-bottom: 1px solid $primary-color;
}

.coupon-page .mu-tabs .active {
  color: #1ec5f1;
  border-bottom: 1px solid #1ec5f1;
}

.nodata {
  font-size: 28rpx;
  color: #494e61;
  margin-top: 35%;
  position: relative;

  image {
    display: block;
    margin: 0 auto 40rpx;
    height: 167rpx;
  }

  .nodata-text {
    // position: absolute;
    // top: 46rpx;
    // left: 0;
    margin-top: 46rpx;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>
