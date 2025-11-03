<template>
  <view class="main-box">
    <view class="cash-content-box">
      <view class="content-title">
        <view class="title">到账方式</view>
      </view>
      <!-- #ifdef MP-WEIXIN -->
      <view class="flex-row flex-ali-center">
        <image :src="require('../static/icon-wchart.png')" mode="heightFix"></image>
        <view class="title-type">微信原路退回 </view>
      </view>
      <!-- #endif -->
      <!-- #ifdef MP-ALIPAY -->
      <view class="flex-row flex-ali-center">
        <image :src="require('../static/icon-alipay.png')" mode="heightFix"></image>
        <view class="title-type">支付宝原路退回 </view>
      </view>
      <!-- #endif -->
      <view class="box">
        <view class="content-price flex-row flex-jst-btw flex-ali-center">
          <text>提现金额</text>
          <view class="input-all" @click="getAll"> 全部提现 </view>
        </view>
        <view class="content-input">
          <view class="input">
            <text class="rmb">￥</text>
            <input v-model="extract" @input="updateInput" type="digit" maxlength="9" class="t-input" />
          </view>
          <view class="balance-text"> 可用余额:￥{{ amount }} </view>
        </view>
        <view class="content-info"> 注：提现金额不得超出可用余额范围~ </view>
      </view>
      <view :class="[isDisabled ? 'disabled-btn confirm-btn' : 'confirm-btn']" @click="handleConfirm"> 提现 </view>
      <view @click="handleDetail" class="record-text"> 提现记录 </view>
    </view>
    <!-- <view class="cash-tips">
      温馨提示
      <br />
      1、用户申请提现后，约1~3个工作日到账 <br />
      2、如果有正在充电的订单时，不可申请提现 <br />
      3、提现依据用户充值记录进行逐笔 <br />
      4、参与活动的充值满送部分不可用于提现，如提现，满送额度将作废 注意:如果手动输入金额无法输入小数时，可选择全部提现
    </view> -->
  </view>
</template>

<script>
import app from "@/static/js/app.js"
import { getUserInfo, userWithdrawal } from "@/config/api.js"
export default {
  components: {},
  data() {
    return {
      amount: 0,
      extract: "",
      isDisabled: false,
    }
  },
  onLoad() {
    getUserInfo({}).then((res) => {
      this.amount = res.data.balance || 0
    })
  },
  methods: {
    updateInput(event) {
      this.checkMoney()
    },
    getAll() {
      this.extract = parseFloat(this.amount)
      this.checkMoney()
    },
    checkMoney() {
      if (this.extract && parseFloat(this.extract).toString() == "NaN") {
        uni.showToast({
          title: "输入金额不合法！",
          icon: "none",
        })
        this.isDisabled = true
        return false
      }
			console.log('this.extract', this.extract)
			this.extract = Number(this.extract)
      if (this.extract > this.amount) {
        uni.showToast({
          title: "可提现金额不足",
          icon: "none",
        })
        this.isDisabled = true
        return false
      }
      if (!this.extract || this.extract <= 0) {
        uni.showToast({
          title: "提现金额需大于0",
          icon: "none",
        })
        this.isDisabled = true
        return false
      }
      this.isDisabled = false
      return true
    },
    handleConfirm() {
      if (!this.checkMoney()) {
        return
      }
      let that = this
			let money = parseFloat(this.extract.toFixed(2));
			that.extract = money
      uni.showModal({
        title: "提示",
        content: "您要申请提现的金额为" + money + "元",
        confirmText: "确定",
        cancelText: "取消",
        cancelColor: "#666",
        success: (result) => {
          if (result.confirm) {
            userWithdrawal({
              amount: money,
            }).then((res) => {
              if (res && res.code === 200) {
                that.extract = ""
                uni.showToast({
                  title: "提现申请已提交，请等待审核",
                  icon: "none",
                })
                getUserInfo({}).then((resp) => {
                  that.amount = resp.data.balance || 0
                })
                setTimeout(() => {
                  uni.navigateBack({
                    delta: 1, // 回退前 delta(默认为1) 页面
                  })
                }, 2000)
              }
            })
          }
        },
      })
    },

    handleDetail() {
      uni.navigateTo({
        url: "/pages/wode/recharge/withdrawalResult",
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.main-box {
  padding: 24rpx;
  .cash-content-box {
    .content-title {
      margin-bottom: 30rpx;
      display: flex;
      align-items: center;
      font-size: 28rpx;
      color: #333;
    }
    image {
      height: 44rpx;
      width: 44rpx;
    }
    .title-type {
      color: #333;
      font-size: 36rpx;
      margin-left: 12rpx;
    }

    .content-price {
      font-size: 28rpx;
      margin-bottom: 50rpx;
      color: #2d2d2d;
      font-weight: 500;
      .input-all {
        font-size: 24rpx;
        color: #3b93ff;
        margin-left: 36rpx;
      }
    }

    .content-input {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 6rpx;
      border-bottom: 2rpx solid rgba(0, 0, 0, 0.08);
      height: 80rpx;

      .rmb {
        font-size: 36rpx;
        margin-top: 8rpx;
      }

      .balance-text {
        font-weight: 400;
        font-size: 24rpx;
        color: #999;
      }

      .input {
        flex: 1;
        display: flex;
        align-items: center;

        .t-input {
          height: 74rpx;
          flex: 1;
          font-size: 56rpx;
          color: #3d3d3d;
          font-weight: 600;
        }
      }
    }

    .box {
      background-color: #fff;
      border-radius: 20rpx;
      padding: 30rpx 24rpx;
      margin: 30rpx auto 60rpx;
    }

    .content-info {
      font-size: 24rpx;
      color: $basic-color;
      margin: 24rpx 0 0;
    }
  }

  .cash-tips {
    font-size: 26rpx;
    margin: 39rpx 24rpx;
    color: #7e7e7e;
    line-height: 42rpx;
  }
  .record-text {
    font-size: 30rpx;
    color: #3b93ff;
    width: 100%;
    text-align: center;
    margin-top: 32rpx;
  }
}
</style>
