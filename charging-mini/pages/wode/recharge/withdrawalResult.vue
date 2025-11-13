<template>
  <view class="detail-box">
    <view class="detail-content" v-if="list.length">
      <view class="content-list">
        <view class="content-item" v-for="(item, index) in list" :key="index">
          <view class="center">
            <view class="ordertype">提现申请</view>
            <view class="time">{{ item.createTime }}</view>
          </view>
          <view class="right">
            <view class="price">{{ item.amount }}元</view>
          </view>
          <view class="right2">
            <view class="status" v-if="item.status == 0">{{ item.statusDesc }}</view>
            <view class="status-green" v-if="item.status == 1">{{ item.statusDesc }}</view>
            <view class="status-red" v-if="item.status == 2">{{ item.statusDesc }}</view>
          </view>
        </view>
      </view>
    </view>
    <view v-else class="empty-wrapper">
      <image src="/static/images/index/empty.png" mode="aspectFit"></image>
      <view class="empty-text">暂无数据</view>
    </view>

    <u-picker
      :show="showStatus"
      :columns="columns"
      keyName="label"
      @cancel="showStatus = false"
      @close="showStatus = false"
      @confirm="confirmStatus"
    ></u-picker>
    <u-calendar
      :show="showTime"
      :maxDate="maxDate"
      :minDate="minDate"
      :defaultDate="defaultDate"
      color="#5F7DF9"
      mode="range"
      :monthNum="12"
      @close="showTime = false"
      @confirm="confirmTime"
      closeOnClickOverlay
    ></u-calendar>
  </view>
</template>

<script>
import { getWithdrawalApplicationPage } from "@/config/api.js"
export default {
  data() {
    return {
      list: [],
      startTime: null,
      endTime: null,
      status: null,
      showTime: false,
      showStatus: false,
      columns: [
        [
          {
            label: "全部",
            id: "",
          },
          {
            label: "待审核",
            id: "0",
          },
          {
            label: "审核中",
            id: "1",
          },
          {
            label: "提现成功",
            id: "2",
          },
          {
            label: "提现失败",
            id: "3",
          },
        ],
      ],
      pages: {
        pageNo: 1,
        pageSize: 10,
      },
      total: 0,
      minDate: "",
      maxDate: "",
      defaultDate: [],
      defaultStartDate: "",
      defaultEndDate: "",
    }
  },
  onLoad(options) {
    // this.chooseTimed()
    this.getRefundList()
  },
  onReachBottom() {
    if (this.pages.pageNo * this.pages.pageSize < this.total) {
      this.pages.pageNo++
      this.getRefundList()
    }
  },
  //下拉刷新
  onPullDownRefresh() {
    this.pages.pageNo = 1
    this.list = []
    this.getRefundList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    chooseTimed() {
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 2
      let day = date.getDate()
      if (month <= 9) {
        month = "0" + month
      }
      if (day <= 9) {
        day = "0" + day
      }
      let minyear = year - 1
      this.minDate = `${minyear}-${month}-${day}`
      this.maxDate = `${year}-${month}-${day}`
      this.defaultDate = [this.maxDate, this.maxDate]
    },
    getRefundList() {
      getWithdrawalApplicationPage({
        pageNum: this.pages.pageNo,
        pageSize: this.pages.pageSize,
      }).then((res) => {
        if (res && res.code === 200) {
          this.total = res.total
          let data = res.rows
          this.list = this.list.concat(data)
          console.log(this.list, "this.list")
        }
      })
    },
    handleSelectTime() {
      this.showTime = true
    },
    handleSelectStatus() {
      this.showStatus = true
    },
    confirmTime(e) {
      let time = e
      this.defaultDate = e
      this.startTime = time[0]
      this.endTime = time[time.length - 1]
      this.showTime = false
      this.pages.pageNo = 1
      this.list = []
      this.getRefundList()
    },
    confirmStatus(e) {
      this.status = e.value[0].id
      this.showStatus = false
      this.pages.pageNo = 1
      this.list = []
      this.getRefundList()
    },
    getTime(val) {
      return this.$u.timeFormat(val, "yyyy-mm-dd hh:MM:ss")
    },
  },
}
</script>

<style lang="scss" scoped>
.detail-box {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 24rpx 32rpx 24rpx 28rpx;

  .detail-top-box {
    .top-box {
      height: 93rpx;
      line-height: 93rpx;
      background: #ffffff;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-left: 34rpx;
      .time-box {
        border-radius: 6rpx;
        box-sizing: border-box;
        width: 65%;
        height: 66rpx;
        // border-radius: 6rpx;
        padding: 0 16rpx;
        display: flex;
        align-items: center;
        font-size: 24rpx;
        color: #444444;

        image {
          height: 22rpx;
          margin-right: 12rpx;
        }
      }

      .status-box {
        text-align: right;
        box-sizing: border-box;
        width: 35%;
        height: 66rpx;
        border-radius: 6rpx;
        padding: 0 16rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 24rpx;
        color: #444444;
      }
    }
  }

  .detail-content {
    margin-top: 24rpx;
    background: #ffffff;
    border-radius: 20rpx;
    .content-list {
      .content-item {
        overflow: hidden;
        padding: 24rpx 28rpx 24rpx 20rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1rpx dashed #d9d9d9;
        .left {
          width: 62rpx;
          height: 62rpx;
          background: #9ed4ff;
          border-radius: 50%;
          margin-right: -22rpx;
        }

        .center {
          text-align: left;
          .ordertype {
            font-size: 26rpx;
            color: #15161d;
            line-height: 37rpx;
          }
          .time {
            font-size: 24rpx;
            color: #878997;
            line-height: 33rpx;
          }
        }

        .right {
          font-weight: 400;
          font-size: 32rpx;
          color: #626262;
        }
        .right2 {
          font-size: 26rpx;
          color: #15161d;
          .status-green {
            color: #529b2e;
          }
          .status-red {
            color: #f56c6c;
          }
        }
      }
    }
  }
  .nodata {
    font-size: 26rpx;
    color: #444444;
    text-align: center;
    margin-top: 200rpx;

    image {
      display: block;
      margin: 40rpx auto 40rpx;
      width: 300rpx;
      height: 228rpx;
    }
  }
}
</style>
