<template>
  <view class="container">
    <view class="mine-box">
      <view class="m-moneybox-center" :style="'background-image:url(' + rechargeBgImg + ')'">
        <view class="moneybox-title"> 可用余额(元) </view>
        <view class="moneybox-info">
          <view class="info-price"> {{ toDecimal2(accountAmount || 0) }} </view>
          <view class="moneybox-info-box">
            <view class="moneybox-info-text"> 仅限充电业务使用 </view>
            <view class="flex-1 flex-row flex-jst-end" v-if="!hiddenBtn">
              <view class="info-btn" @click="navigateToPage('/pages/wode/recharge/recharge')"> 充值 </view>
              <view class="info-btn" @click="navigateToPage('/pages/wode/recharge/withdrawal')"> 提现 </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="r-box">
      <view class="ulc">
        <view class="titke">余额明细</view>
        <view class="flex-row flex-jst-btw flex-ali-center">
          <view class="titke-date">
            <picker mode="date" fields="month" :value="date" @change="bindDateChange">
              <view class="uni-input">{{ monthText }}</view>
            </picker>
            <u-icon name="arrow-down-fill" color="#666" size="10"></u-icon>
          </view>
          <view class="flex-row flex-1 flex-ali-center flex-jst-end">
            <text>共支出￥{{ toDecimal2(consumptionTotal || 0) }}</text>
            <text style="margin-left: 36rpx">共充值￥{{ toDecimal2(rechargeTotal || 0) }}</text>
          </view>
        </view>
      </view>

      <view class="czjl" v-if="detailsList && detailsList.length !== 0">
        <view v-for="(item, index) in detailsList" class="itemcc" :key="index">
          <view class="center">
            <view class="ordertype">{{ item.name }}</view>
            <view class="time">{{ item.time }} </view>
          </view>
          <view class="right" :class="item.amount > 0 ? 'right-amount' : ''">
            <view class="">{{ item.amount }}</view>
          </view>
        </view>
      </view>
      <view v-else class="empty-wrapper">
        <image src="/static/images/index/empty.png" mode="aspectFit"></image>
        <view class="empty-text">暂无数据</view>
      </view>
    </view>
  </view>
</template>

<script>
import app from "@/static/js/app.js"
import { getUserBalance } from "@/config/api.js"
export default {
  components: {},
  data() {
    return {
      toDecimal2: app.toDecimal2,
      rechargeBgImg: require("../../stations/static/chongzhi1.png"),
      accountAmount: 0.0, //账户余额
      detailsList: [],
      consumptionTotal: "",
      rechargeTotal: "",
      hiddenBtn: false,
      month:
        new Date().getFullYear() +
        "-" +
        (new Date().getMonth() + 1 < 10 ? "0" + (new Date().getMonth() + 1) : new Date().getMonth() + 1),
      monthText:
        new Date().getFullYear() +
        "年" +
        (new Date().getMonth() + 1 < 10 ? "0" + (new Date().getMonth() + 1) : new Date().getMonth() + 1) +
        "月",
    }
  },
  onShow() {
    this.hiddenBtn = uni.getStorageSync("userType") == "company"
    this.queryAccountBalance()
  },
  //下拉刷新
  onPullDownRefresh() {
    this.queryAccountBalance()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    navigateToPage(url) {
      uni.navigateTo({
        url,
      })
    },
    getDate(d) {
      const date = d ? new Date(d) : new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()

      month = month > 9 ? month : "0" + month
      day = day > 9 ? day : "0" + day
      return `${year}-${month}-${day}`
    },
    bindDateChange(e) {
      console.log(e)
      let val = e.detail.value // '2024/06'
      this.confirmDate({
        value: e.detail.value + "-01",
      })
    },
    formatter(type, value) {
      if (type === "year") {
        return `${value}年`
      }
      if (type === "month") {
        return `${value}月`
      }
      if (type === "day") {
        return `${value}日`
      }
      return value
    },
    confirmDate(e) {
      let date = e.value.replace(/-/g, "/")
      var y = new Date(date).getFullYear()
      var m = new Date(date).getMonth() + 1
      if (m * 1 < 10) {
        m = "0" + m
      }
      console.log(e, date, y, m)
      this.month = y + "-" + m
      this.monthText = y + "年" + m + "月"
      this.detailsList = []
      this.queryAccountBalance()
    },
    /**
     * 获取账户余额
     */
    queryAccountBalance() {
      getUserBalance({
        queryDate: this.month,
      }).then((res) => {
        this.accountAmount = res.data.balance
        this.detailsList = res.data.detailsResponseList
          ? res.data.detailsResponseList.map((item) => {
              return {
                ...item,
                // time: this.getDate(item.time),
              }
            })
          : []
        this.consumptionTotal = res.data.consumptionTotal || 0
        this.rechargeTotal = res.data.rechargeTotal || 0
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.container {
  position: relative;
  padding: 32rpx 24rpx;
}

.mine-box {
  .mu-tabs {
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 34rpx;
    background: #768ce8;
    color: #ffffff;
    height: 68rpx;

    .order-tabs {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;
    }

    .active-tab {
      height: 68rpx;
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;
      border-radius: 34rpx;
      background: #eaeeff;
      color: #031fa7;
    }
  }

  .m-moneybox-center {
    padding: 26rpx;
    background-size: 100% 100%;
    border-radius: 16rpx;
    color: #ffffff;

    .moneybox-title {
      padding: 0 0 20rpx 0;
      font-size: 28rpx;
    }

    .moneybox-info {
      .info-price {
        font-weight: 600;
        font-size: 64rpx;
        letter-spacing: 1rpx;
      }

      .moneybox-info-box {
        display: flex;
        align-items: flex-end;
        justify-content: space-between;

        .info-btn {
          width: 106rpx;
          height: 46rpx;
          background: rgba(255, 255, 255, 0.16);
          border-radius: 50rpx;
          font-weight: 400;
          font-size: 24rpx;
          background-color: #ffffff;
          color: $primary-color;
          line-height: 48rpx;
          text-align: center;
          margin-left: 16rpx;
        }
      }

      .moneybox-info-text {
        font-size: 24rpx;
        color: #ffffff;
        width: 230rpx;
      }
    }
  }
}

.r-box {
  margin: 30rpx auto;
  height: auto;
  background: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx 30rpx 0 30rpx;
}

.ulc {
  .titke {
    color: #333;
    font-size: 30rpx;
    margin-bottom: 16rpx;
  }

  .titke-date {
    width: 212rpx;
    height: 56rpx;
    background: #f8f8f8;
    border-radius: 29rpx;
    font-size: 28rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #333;
  }
  text {
    font-size: 24rpx;
    color: $basic-color;
  }
}

.czjl {
  border-radius: 20rpx;
  margin-top: 16rpx;

  .itemcc {
    border-top: 1rpx solid rgba(0, 0, 0, 0.08);
    height: 160rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .left {
      width: 62rpx;
      height: 62rpx;
      background: #9ed4ff;
      border-radius: 50%;
      margin-right: 30rpx;
      float: left;
    }

    .center {
      float: left;

      .ordertype {
        font-size: 28rpx;
        color: #333;
      }

      .time {
        font-size: 22rpx;
        color: $basic-color;
        margin-top: 12rpx;
      }
    }

    .right {
      font-weight: 600;
      font-size: 32rpx;
      color: #3d3d3d;
      margin-top: -28rpx;
    }

    .right-amount {
      color: $primary-color;
    }
  }
}

.nodata {
  font-size: 26rpx;
  color: #444444;
  text-align: center;
  margin-top: 100rpx;

  image {
    display: block;
    margin: 0 auto 40rpx;
    width: 300rpx;
    height: 228rpx;
  }
}
</style>
