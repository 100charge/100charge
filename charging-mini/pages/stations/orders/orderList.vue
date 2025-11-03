<template>
  <view class="main">
    <view class="mu-tabs">
      <view class="mu-tabs-box">
        <view class="order-tabs" :class="status == 'all' ? 'active-tab' : ''" @click="sTab('all')">全部 </view>
        <view class="order-tabs" :class="status == 0 ? 'active-tab' : ''" @click="sTab(0)">进行中 </view>
        <view class="order-tabs" :class="status == 1 ? 'active-tab' : ''" @click="sTab(1)">待支付 </view>
        <view class="order-tabs" :class="status == 2 ? 'active-tab' : ''" @click="sTab(2)">已完成 </view>
      </view>
    </view>
    <!-- 订单列表 -->
    <view class="order-nlist">
      <view v-for="item in list" :key="item.id" @click="handleDetail(item)">
        <u-transition :show="list && list.length > 0" :mode="modeFade">
          <view class="order-content">
            <view class="content-title">
              <view class="title-box">
                <image class="icon-title" src="/static/images/index/station-vip.png" mode="scaleToFill" />
                <text class="title">{{ item.stationName || "" }}</text>
              </view>
              <view class="state-box">
                <text :class="item.status == 1 || item.status == 3 ? 'status-nopay' : 'status'">{{
                  item.statusDesc
                }}</text>
                <image class="icon-right" src="/static/images/index/right-gray.png" mode="scaleToFill" />
              </view>
            </view>
            <view class="time"> {{ item.startTime }} </view>
            <view class="price-box">
              <view class="unit">￥</view>
              <view class="price">{{ item.realAmount || 0 }}</view>
            </view>
            <view class="content"> 充电{{ item.totalPower || 0 }}度·用时{{ item.realDuration }} </view>
            <view class="btn-box">
              <button v-if="item.status == 0">去查看</button>
              <button class="evaluate" v-else-if="item.status == 1">去付款</button>
              <button v-if="item.status == 2 && !item.reviewed">去评价</button>
            </view>
          </view>
        </u-transition>
      </view>
    </view>
    <!-- 加载更多 -->
    <view>
      <u-loadmore :status="loadStatus" v-if="list && list.length > 0" />
    </view>
    <!-- 空状态 -->
    <view class="nodata" v-if="list && list.length === 0">
      <image src="/static/images/index/empty.png" mode="scaleToFill"></image>
      <view class="nodata-text">
        <span>暂无订单</span>
      </view>
    </view>
  </view>
</template>

<script>
import { getChargingOrderList } from "@/config/api.js"
export default {
  components: {},
  data() {
    return {
      loadStatus: "nomore",
      openid: "",
      status: "all",
      list: [],
      pageNum: 1,
      listCode: null,
      totalRows: 0,
      modeFade: "fade-down",
    }
  },
  onShow() {
    this.sTab("all")
  },
  watch: {},
  //下拉刷新
  onPullDownRefresh() {
    this.sTab(this.status)
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onReachBottom() {
    if (this.pageNum * 10 < this.totalRows) {
      this.loadStatus = "loading"
      this.pageNum++
      setTimeout(() => {
        this.orderList()
      }, 100)
    } else {
      this.loadStatus = "nomore"
    }
  },
  methods: {
    handleDetail(item) {
      if (item.status == 3) {
        uni.showToast({
          title: "订单正在结算中，请稍候",
          icon: "none",
        })
        return
      }
      if (item.status == 0) {
        uni.navigateTo({
          url: "/pages/stations/site/charging?orderNo=" + item.tradeNo,
        })
      }
      if (item.status == 1 || item.status == 2) {
        uni.navigateTo({
          url: "/pages/stations/orders/orderDetail?orderNo=" + item.tradeNo,
        })
      }
    },
    sTab(status) {
      this.status = status
      this.list = []
      this.pageNum = 1
      this.orderList()
    },
    /**
     * 获取订单列表
     */
    orderList() {
      var that = this
      getChargingOrderList({
        status: this.status == "all" ? null : this.status,
        pageNum: this.pageNum,
      }).then((res) => {
        if (res && res.code == 200) {
          that.totalRows = res.total
          that.list.push(...res.rows)
        }
      })
    },

    //计算两个时间之间的时间差 多少天时分秒
    intervalTime(begin_time, end_time) {
      //年月日时分秒转换为时间戳
      let beginTime = new Date(begin_time).getTime() / 1000
      let endTime = new Date(end_time).getTime() / 1000
      var starttime = ""
      var endtime = ""
      if (beginTime < endTime) {
        starttime = beginTime
        endtime = endTime
      } else {
        starttime = endTime
        endtime = beginTime
      }
      //计算天数
      var timediff = endtime - starttime
      var days = parseInt(timediff / 86400)
      //计算小时数
      var remain = timediff % 86400
      var hours = parseInt(remain / 3600)
      //计算分钟数
      var remain = remain % 3600
      var mins = parseInt(remain / 60)
      var res = (days ? days + "天" : "") + (hours ? hours + "小时" : "") + (mins ? mins + "分" : "--")
      return res
    },
  },
}
</script>

<style lang="scss" scoped>
image {
  width: auto;
  height: auto;
}

.main {
  background: #f4f7f8;
  width: 100vw;
  min-height: 100vh;
  box-sizing: border-box;
  padding-bottom: 180rpx;
  padding-top: 88rpx;
}

.mu-tabs {
  padding: 20rpx 96rpx 0;
  background-color: #fff;
  position: fixed;
  width: 100%;
  box-sizing: border-box;
  z-index: 999;
  top: 0;

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
      font-weight: 700;
      font-size: 30rpx;
      color: #333333;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .active-tab::after {
      content: "";
      position: absolute;
      bottom: 0rpx;
      width: 26rpx;
      height: 5rpx;
      background: $primary-color;
      border-radius: 54rpx 54rpx 54rpx 54rpx;
    }
  }
}

.main .mu-tabs .item {
  padding: 5px 10px;
  line-height: 35px;
  text-align: center;
  border-bottom: 1px solid #010c3d;
}

.main .mu-tabs .active {
  color: #1ec5f1;
  border-bottom: 1px solid #1ec5f1;
}

.order-nlist {
  margin: 24rpx;

  .order-content {
    margin-bottom: 24rpx;
    width: 702rpx;
    min-height: 254rpx;
    box-sizing: border-box;
    padding: 28rpx 24rpx 36rpx 24rpx;
    background: #ffffff;
    border-radius: 20rpx 20rpx 20rpx 20rpx;
    position: relative;

    .content-title {
      display: flex;
      justify-content: space-between;

      .title-box {
        display: flex;
        align-items: center;
      }

      .icon-title {
        width: 28rpx;
        height: 28rpx;
        margin-right: 10rpx;
      }

      .title {
        width: 450rpx;
        height: 34rpx;
        font-weight: 400;
        font-size: 28rpx;
        color: #333333;
        line-height: 34rpx;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-all; // 数字 字母换行
      }

      .state-box {
        display: flex;
        align-content: center;

        text {
          min-width: 78rpx;
          height: 34rpx;
          font-size: 26rpx;
          line-height: 34rpx;
        }

        .status {
          color: #72727d;
        }

        .status-nopay {
          color: $primary-color;
        }

        .icon-right {
          width: 28rpx;
          height: 28rpx;
          position: relative;
          top: 5rpx;
        }
      }
    }

    .time {
      height: 24rpx;
      font-weight: 400;
      font-size: 22rpx;
      color: #999999;
      line-height: 24rpx;
      padding: 16rpx 0 36rpx;
    }

    .price-box {
      padding-left: 16rpx;
      color: $primary-color;
      position: relative;

      .unit {
        font-size: 20rpx;
        position: absolute;
        left: 0;
        bottom: 8rpx;
      }

      .price {
        font-size: 44rpx;
        font-weight: 600;
      }
    }

    .content {
      width: 362rpx;
      min-height: 28rpx;
      font-size: 24rpx;
      color: #999999;
      line-height: 28rpx;
      padding-top: 18rpx;
    }

    .btn-box {
      position: absolute;
      right: 23rpx;
      bottom: 32rpx;

      button {
        min-width: 118rpx;
        height: 54rpx;
        font-size: 24rpx;
        color: #ffffff;
        line-height: 54rpx;
        background: #333;
        border-radius: 27rpx 27rpx 27rpx 27rpx;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      button.evaluate {
        border: 1rpx solid #333;
        height: 52rpx;
        min-width: 116rpx;
        background: transparent;
        color: #333;
      }
    }
  }
}

.nodata {
  font-size: 28rpx;
  color: #494e61;
  margin-top: 35%;
  position: relative;

  image {
    display: block;
    margin: 0 auto 40rpx;
    height: 279rpx;
    width: 480rpx;
  }

  .nodata-text {
    margin-top: 46rpx;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>
