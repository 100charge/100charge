<template>
  <view class="chonzhi-box">
    <view class="recharge-content">
      <view class="invest-box" :style="'background-image:url(' + rechargeBgImg + ')'">
        <view class="residue-box">
          <view class="residue-title">当前余额(元)</view>
          <view class="residue-res">
            {{ toDecimal2(accountAmount) }}
          </view>
        </view>
      </view>
      <view class="box-info">
        <view class="title-box">
          <view class="title-box-text"> 选择您的充值金额 </view>
        </view>
        <view class="price-row">
          <view
            v-for="(item, index) in preCountList"
            class="price-row-item"
            :class="totalAmountIndex == index ? 'active' : ''"
            @click="selectedAmount(item, index)"
          >
            <view class="flex-row flex-ali-end">
              {{ item.amount }}<text style="font-size: 24rpx; margin-bottom: 10rpx; font-weight: 600">元</text>
            </view>
            <!-- <text class="" style="font-size: 24rpx"> 售价 50元 </text> -->
          </view>
          <view class="price-row-item" :class="totalAmountIndex == -1 ? 'active' : ''" @click="selectedAmount({}, -1)">
            <text style="font-size: 30rpx; margin-bottom: 8rpx; font-weight: 600">自定义</text>
            <text class="" style="font-size: 24rpx"> 自定义充值金额 </text>
          </view>
        </view>
        <view class="price-row" v-if="totalAmountIndex === -1">
          <view class="price-input">
            <view class="price-input-text"> 自定义金额 </view>
            <input
              class="input"
              type="digit"
              @blur="inputBlur"
              placeholder="请输入充值金额"
              maxlength="5"
              v-model="inputAmount"
            />
          </view>
        </view>

        <view
          class="gift-box"
          :style="'background-image:url(' + couponBgImg + ')'"
          v-for="(item, index) in giftList"
          :key="index"
          v-show="giftList && giftList.length"
        >
          <!-- 0:满减券,1:折扣券（%）,2:直减券 -->
          <view class="gift-left" v-if="item.couponType == 1"> {{ item.discountPercentage / 10 }}<text>折</text> </view>
          <view class="gift-left" v-else> <text>¥</text>{{ item.amount }} </view>
          <view class="gift-right">
            <view class="gift">{{ item.promotionName }}</view>
            <view class="total" v-if="item.couponType == 0">满{{ item.totalAmount }}元可用</view>
            <view class="total" v-else>无门槛</view>
          </view>
          <view class="gift-num"> {{ item.qty }}张 </view>
        </view>
      </view>
      <view class="main-confirm-bottom" style="">
        <view class="confirm-btn" @click="confirmPay"> 确认充值 </view>
      </view>
    </view>
  </view>
</template>

<script>
import app from "@/static/js/app.js"
import { getUserInfo, preRecharge } from "@/config/api.js"
export default {
  data() {
    return {
      rechargeBgImg: require("../../stations/static/chongzhi2.png"),
      couponBgImg: require("../static/quan-bg.png"),
      toDecimal2: app.toDecimal2,
      accountAmount: 0.0, //账户余额

      preCountList: [
        { id: 1, amount: 15, price: 15 },
        { id: 2, amount: 20, price: 20 },
        { id: 3, amount: 30, price: 30 },
        { id: 4, amount: 100, price: 100 },
        { id: 5, amount: 200, price: 200 },
      ],

      giftAmount: 0, //优惠金额
      name: "", //优惠标题
      totalAmount: 15, //充值金额
      totalAmountIndex: 0,

      giftList: [],

      inputAmount: null,
      key: "",
      checkForm: false,
    }
  },
  onShow(option) {
    this.queryAccountBalance()
  },
  methods: {
    selectedAmount(item, index) {
      this.giftList = []
      this.totalAmountIndex = index
      this.inputAmount = ""
      if (index !== -1) {
        this.totalAmount = item.amount
      }
    },
    inputBlur(e) {},
    queryAccountBalance() {
      getUserInfo({}).then((res) => {
        if (res && res.code == 200) {
          this.accountAmount = res.data.balance
        }
      })
    },
    rechargewx() {
      var _this = this
      console.log(this.inputAmount || this.totalAmount)
      preRecharge({
        amount: this.inputAmount || this.totalAmount,
        payment: "WECHAT_PAY",
      })
        .then((res) => {
          if (res.code == 200 && res.data) {
            uni.requestPayment({
              appId: res.data.appId,
              provider: "wxpay", //支付类型-固定值
              timeStamp: res.data.timeStamp, // 时间戳（单位：秒）
              nonceStr: res.data.nonceStr, // 随机字符串
              package: res.data.package, // 固定值
              signType: res.data.signType, //固定值 MD5
              paySign: res.data.paySign, //签名

              success: function (res) {
                uni.showToast({
                  icon: "success",
                  title: "支付成功",
                })
                _this.queryAccountBalance()
                setTimeout(() => {
                  uni.navigateBack({
                    delta: 1,
                  })
                }, 1500)
              },

              fail: function (err) {
                uni.showToast({
                  icon: "none",
                  title: "支付失败",
                })
                _this.queryAccountBalance()
              },
            })
          } else {
            this.giftAmount = 0
            this.name = ""
            uni.showToast({
              title: res.msg,
              icon: "none",
            })
          }
        })
        .catch((err) => {
          uni.showToast({
            title: err.msg,
            icon: "none",
          })
        })
    },
    confirmPay() {
      if (!this.totalAmount && !this.inputAmount) {
        uni.showToast({
          icon: "none",
          title: "请填写充值金额",
        })
        return
      }
      uni.showToast({
        title: "功能暂未开放",
        icon: "none",
      })
      return
      this.rechargewx()
    },
  },
}
</script>

<style lang="scss">
.chonzhi-box {
  padding: 30rpx 0;
  padding-bottom: 180rpx;
}

.recharge-content {
  .invest-box {
    height: 212rpx;
    background-size: 100% 100%;
    display: flex;
    padding: 24rpx;
    margin: 0 24rpx;
    color: #ffffff;

    .residue-box {
      padding-top: 16rpx;
      height: 100%;

      .residue-title {
        font-weight: 500;
        font-size: 28rpx;
      }

      .residue-res {
        font-weight: 600;
        font-size: 64rpx;
        margin-top: 12rpx;
      }
    }
  }

  .box-info {
    background: #ffffff;
    padding: 24rpx;
    margin: -20rpx auto;
    box-sizing: border-box;

    .title-box {
      margin-bottom: 36rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .title-box-text {
        font-size: 28rpx;
        color: $basic-color;
      }
    }
  }
}

.price-row {
  display: flex;
  justify-content: first baseline;
  align-items: center;
  flex-wrap: wrap;

  .price-input {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0rpx 26rpx;
    height: 92rpx;
    background: #f7f7f7;
    border-radius: 20rpx;

    .price-input-text {
      font-size: 28rpx;
      color: #333;
    }

    .input {
      color: #333;
      flex: 1;
      margin-left: 100rpx;
      background: transparent;
      font-size: 30rpx;
      text-align: right;
    }
  }

  .price-tips {
    margin-top: 28rpx;
    display: flex;
    align-items: flex-start;

    .price-tips-text {
      margin-top: -4rpx;
      margin-left: 0rpx;
      font-size: 26rpx;
      color: #7e7e7e;
      line-height: 50rpx;
    }
  }

  .btn {
    width: 100%;
    margin: 8px;
    background: #eaeeff;
    color: #fff;
    font-size: 16px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    border-radius: 6px;
    margin-top: 40px;
    padding-top: 4px;
    padding-bottom: 4px;
  }

  .price-row-item {
    width: 220rpx;
    height: 138rpx;
    background: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    margin-bottom: 24rpx;
    border-radius: 20rpx;
    border: 2rpx solid #eaeaea;
    color: #333;
    margin-right: 16rpx;
    font-size: 50rpx;
    font-weight: 600;

    text {
      font-weight: 400;
    }

    &:nth-child(3n) {
      margin-right: 0;
    }
  }

  .active {
    background: #ffe9d9;
    border: 2rpx solid $primary-color;
    color: $primary-color;
  }
}

.price-btn-box {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;

  .btn-check-box {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 90rpx;
    background: #effbff;

    image {
      width: 30rpx;
      height: 30rpx;
      margin-right: 12rpx;
    }

    .check-text {
      font-size: 26rpx;
      color: #17141b;

      span {
        color: #3899f3;
      }
    }
  }
}

.gift-box {
  height: 189rpx;
  // width: 100%;
  background-size: 100% auto;
  color: #afbdd1;
  overflow: hidden;
  margin: 20rpx 0 0;
  display: flex;
  align-items: center;
  padding: 10rpx 76rpx;
  box-sizing: border-box;
  position: relative;

  .gift-left {
    // width: 100rpx;
    font-size: 70rpx;
    font-weight: bold;
    color: $primary-color;

    text {
      font-size: 38rpx;
      font-weight: 400;
    }
  }

  .gift-right {
    margin-left: 26rpx;
    text-align: left;
    width: 260rpx;
    font-size: 28rpx;
    font-weight: 400;
    color: $primary-color;

    .gift {
      margin-bottom: 4rpx;
      font-size: 30rpx;
      font-weight: 500;
    }
  }

  .gift-num {
    position: absolute;
    right: 200rpx;
    top: 38%;
    font-weight: 500;
    font-size: 30rpx;
    color: $primary-color;
  }
}

.uni-input-input:focus {
  border: 1px solid #1ec5f1;
}

.infobox {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  box-sizing: border-box;
  padding: 46rpx 44rpx 44rpx;
  box-sizing: border-box;

  .top-infod {
    text-align: center;
  }

  .top-infod image {
    width: 132rpx;
    height: 132rpx;
  }

  .info-txt {
    font-size: 28rpx;
    color: #444444;
    text-align: center;
    line-height: 50rpx;
  }

  .info-btn-box {
    width: 100%;
    font-size: 28rpx;
    margin-top: 52rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .btn-cancel {
      color: #666666;
      width: 240rpx;
      height: 84rpx;
      line-height: 84rpx;
      text-align: center;
      border: 2rpx solid #a6a8ba;
      border-radius: 14rpx;
    }

    .btn-submit {
      color: #ffffff;
      width: 240rpx;
      height: 84rpx;
      line-height: 84rpx;
      text-align: center;
      background-color: #134fc5;
      border: 2rpx solid #134fc5;
      border-radius: 14rpx;
    }
  }
}
</style>
