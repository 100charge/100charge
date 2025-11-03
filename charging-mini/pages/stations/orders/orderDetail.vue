<template>
  <view class="settlement-page">
    <view class="title-box">
      <view class="font-size-40">
        <text style="color: #ff4d01" v-if="orderInfo.status == 1">订单待支付</text>
        <text v-else-if="orderInfo.status == 2">订单已完成</text>
        <text v-else>订单正在结算中</text>
      </view>
      <view class="tip" v-if="orderInfo.status == 1"> 您启动充电前，已选择{{ orderInfo.paymentDesc || "" }}支付 </view>
      <view class="tip" v-else> 感谢使用，期待再次充电 </view>
    </view>

    <template v-if="orderInfo.status != 1 && orderInfo.status != 2">
      <view class="card order-detail-box" style="padding-bottom: 24px">
        <view class="detail-title dis-flex dis-flex-between">
          订单明细
          <view class="detail-text"> </view>
        </view>
        <u-skeleton rows="4" title loading></u-skeleton>
      </view>
      <view class="card order-detail-box" style="padding-bottom: 24px">
        <view class="detail-title dis-flex dis-flex-between">
          用户评价
          <view class="detail-text"> </view>
        </view>
        <u-skeleton rows="2" title loading></u-skeleton>
      </view>
    </template>

    <!-- 订单待支付 -->
    <template v-if="orderInfo.status == 1">
      <!-- 订单明细 -->
      <view class="card order-detail-box">
        <view class="detail-title dis-flex dis-flex-between">
          订单明细
          <view class="detail-text"> {{ orderInfo.stationName || "" }} </view>
        </view>
        <view class="order-detail-cell dis-flex-between">
          <text class="text">订单编号</text>
          <view class="value size24 flex-row flex-ali-center flex-jst-end">
            <text class="w700">{{ orderInfo.tradeNo }}</text>
            <text class="copy dis-flex-ac" style="margin-right: 0" @click="onClipboard(orderInfo.tradeNo)">复制</text>
          </view>
        </view>
        <view class="order-detail-cell dis-flex-between">
          <text class="text">应付总金额</text>
          <view class="value size24">
            ¥<text class="size36 w700">{{ orderInfo.totalAmount }}</text>
          </view>
        </view>
        <template>
          <view class="line"></view>
          <view class="order-detail-cell dis-flex-between">
            <text class="text">实际充电度数</text>
            <view class="value"> {{ orderInfo.totalPower }}度 </view>
          </view>
          <block v-if="showOrderDetailAll">
            <view class="order-detail-cell dis-flex-between">
              <text class="text">电费</text>
              <view class="value">￥{{ orderInfo.chargeFee }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">服务费</text>
              <view class="value">￥{{ orderInfo.serviceFee }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">停车费</text>
              <view class="value">￥{{ orderInfo.parkingFee }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">超时占用费</text>
              <view class="value">￥{{ orderInfo.overTimeFee }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">开始充电时间</text>
              <view class="value">{{ orderInfo.startTime }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">结束充电时间</text>
              <view class="value">{{ orderInfo.endTime }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">充电时长</text>
              <view class="value">{{ orderInfo.realDuration }}</view>
            </view>
            <view class="order-detail-cell dis-flex-between">
              <text class="text">停止原因</text>
              <view class="value">{{ orderInfo.stopReason }}</view>
            </view>
          </block>
        </template>

        <view class="btn-public dis-flex-ac" @click="changeAllShow('showOrderDetailAll')">
          {{ showOrderDetailAll ? "收起" : "展开全部信息" }}
          <image v-if="!showOrderDetailAll" src="/static/images/index/down-gray.png" mode="scaleToFill"></image>
          <image v-else src="/static/images/index/up-gray.png" mode="scaleToFill"></image>
        </view>
      </view>

      <!-- 优惠券 -->
      <view class="card coupon dis-flex-between" @click="changeAllShow('showCouponList')">
        <view class="title">优惠券</view>
        <view class="value dis-flex-ac">
          <text v-if="!selectedCoupon.id">{{ couponList && couponList.length ? "选择优惠券" : "暂无可用优惠券" }}</text>
          <text v-else>减免{{ orderCouponAmount || 0 }}元</text>
          <image src="/static/images/index/right-gray.png" mode="scaleToFill"></image>
        </view>
      </view>

      <!-- 优惠券选择弹窗 -->
      <u-popup :show="showCouponList" @close="changeAllShow('showCouponList')" round="20">
        <view class="coupon-list" v-if="couponList && couponList.length">
          <scroll-view scroll-y="true" style="height: 60vh">
            <couponItem
              isOrder
              :selectedCoupon="selectedCoupon"
              :coupon-item="item"
              v-for="(item, index) in couponList"
              :key="index"
              @selectCoupon="handleSelectedCoupon"
            />
          </scroll-view>
        </view>
        <view v-else class="empty-wrapper">
          <image src="/static/images/index/empty.png" mode="aspectFit"></image>
          <view class="empty-text">暂无可用优惠券</view>
        </view>
        <view class="coupon-bottom-tip" @click="handleSelectedCoupon(null)">
          {{ couponList && couponList.length ? "暂不使用优惠券" : "关闭" }}
        </view>
      </u-popup>

      <!-- 底部支付 -->
      <view class="btn-bottom-box dis-flex-between">
        <view class="value dis-flex-ac">
          <text>应付：</text>
          <text class="unit">￥</text>
          <text class="price">{{ orderShouldAmount }}</text>
        </view>
        <view class="tbn dis-flex-ac" @click="toPay"> 确认支付 </view>
      </view>
    </template>

    <!-- 订单已完成 -->
    <template v-if="orderInfo.status == 2">
      <!-- 充电详情 -->
      <view class="card detail-box">
        <view class="detail-title dis-flex" @click="toStation">
          {{ orderInfo.stationName || "" }}
          <image src="/static/images/index/right-gray.png" mode="scaleToFill"></image>
        </view>
        <view class="all-price-box dis-flex-between">
          <text class="text">实际付款总金额</text>
          <view class="price">
            <text class="unit">￥</text>
            {{ orderInfo.realAmount }}
          </view>
        </view>
        <template v-if="showDetailAll">
          <view class="price-box dis-flex-between">
            <text class="text">电费</text>
            <view class="price">￥{{ orderInfo.chargeFee }}</view>
          </view>
          <view class="price-box dis-flex-between">
            <text class="text">服务费</text>
            <view class="price">￥{{ orderInfo.serviceFee }}</view>
          </view>
          <view class="price-box dis-flex-between">
            <text class="text">停车费</text>
            <view class="price">￥{{ orderInfo.parkingFee }}</view>
          </view>
          <view class="price-box dis-flex-between">
            <text class="text">超时占用费</text>
            <view class="price">￥{{ orderInfo.overTimeFee }}</view>
          </view>
          <view class="price-box dis-flex-between" v-if="orderInfo.couponAmount">
            <text class="text">优惠券抵扣</text>
            <view class="price" style="font-weight: 400">减￥{{ orderInfo.couponAmount }}</view>
          </view>
          <view class="line"></view>

          <view class="order-title">订单信息</view>
          <view class="order-item dis-flex-between flex-ali-center">
            <text class="text">订单编号</text>
            <view class="value dis-flex flex-ali-center">
              <text class="copy dis-flex-ac" @click="onClipboard(orderInfo.tradeNo)">复制</text>
              {{ orderInfo.tradeNo }}
            </view>
          </view>
          <view class="order-item dis-flex-between flex-ali-center">
            <text class="text">电桩编号</text>
            <view class="value dis-flex flex-ali-center">
              <text class="copy dis-flex-ac" @click="onClipboard(orderInfo.deviceNo + '-' + orderInfo.gunNo)"
                >复制</text
              >
              {{ orderInfo.deviceNo }} - {{ orderInfo.gunNo }}
            </view>
          </view>
          <view class="order-item dis-flex-between">
            <text class="text">开始充电时间</text>
            <view class="value">{{ orderInfo.startTime }}</view>
          </view>
          <view class="order-item dis-flex-between">
            <text class="text">结束充电时间</text>
            <view class="value">{{ orderInfo.endTime }}</view>
          </view>
          <view class="order-item dis-flex-between">
            <text class="text">充电度数</text>
            <view class="value">{{ orderInfo.totalPower }}度</view>
          </view>
          <view class="order-item dis-flex-between">
            <text class="text">充电时长</text>
            <view class="value">{{ orderInfo.realDuration }}</view>
          </view>
          <view class="order-item dis-flex-between">
            <text class="text">停止原因</text>
            <view class="value">{{ orderInfo.stopReason }}</view>
          </view>
        </template>

        <view class="btn-public dis-flex-ac" @click="changeAllShow('showDetailAll')">
          {{ showDetailAll ? "收起" : "展开全部" }}
          <image v-if="!showDetailAll" src="/static/images/index/down-gray.png" mode="scaleToFill"></image>
          <image v-else src="/static/images/index/up-gray.png" mode="scaleToFill"></image>
        </view>
      </view>

      <!-- 用户评价 -->
      <view class="card evaluate-box">
        <view class="title dis-flex-ac">
          用户评价
          <view class="tip dis-flex-ac"> 最多三项 </view>
        </view>

        <view class="btn-box dis-flex-ac" :class="{ showEvaluateAll: showEvaluateAll }">
          <view
            class="btn-item dis-flex-ac"
            :class="{ active: item.selected }"
            v-for="item in evaluationOptions"
            :key="item.value"
            @click="selectedEval(item)"
          >
            {{ item.label }}
          </view>
        </view>

        <view class="box">
          <view class="btn dis-flex-ac" @click="changeAllShow('showEvaluateAll')">
            {{ showEvaluateAll ? "收起" : "展开全部" }}
            <image v-if="!showEvaluateAll" src="/static/images/index/down-gray.png" mode="scaleToFill"></image>
            <image v-else src="/static/images/index/up-gray.png" mode="scaleToFill"></image>
          </view>
          <view class="line"></view>
        </view>
        <view v-if="!hideEvalButton" class="submit dis-flex-ac" @click="confirmEval"> 提交评价 </view>
      </view>
    </template>
  </view>
</template>

<script>
import {
  getOrderDetail,
  getAvailableCouponsForOrders,
  calculationRealPayAmount,
  getEvaluationByOrder,
  submitEvaluationByOrder,
  orderPayment,
  getConfigKey,
} from "@/config/api.js"
import couponItem from "../../../components/coupon-item/coupon-item.vue"
export default {
  components: {
    couponItem,
  },
  data() {
    return {
      orderId: "",
      orderNo: "",
      orderInfo: {},
      detail: {
        value: 123,
      },
      orderState: 2, // 0 已完成 1未减免 2已减免
      showDetailAll: true,
      showEvaluateAll: false,
      showOrderDetailAll: true,
      btnList: [],
      showCouponList: false,
      couponList: [],
      evaluationOptions: [],
      hideEvalButton: false, // 是否隐藏提交评价按钮
      orderShouldAmount: 0, // 实际应付
      orderCouponAmount: 0, // 减免金额
      selectedCoupon: {},
      orderStatusInterval: null, // 定时器
    }
  },
  onLoad(options) {
    this.orderNo = options.orderNo || ""
    this.getDetail()
  },
  onUnload() {
    clearInterval(this.orderStatusInterval)
    this.orderStatusInterval = null
    console.log("onUnload", this.orderStatusInterval)
  },
  //下拉刷新
  onPullDownRefresh() {
    this.getDetail()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },

  methods: {
    getDetail() {
      clearInterval(this.orderStatusInterval)
      this.orderStatusInterval = null
      getOrderDetail({ tradeNo: this.orderNo }).then((res) => {
        this.orderInfo = res.data
        this.orderShouldAmount = res.data.totalAmount || 0
        if (this.orderInfo.status == 2) {
          this.getEvaluationOptions()
        }
        if (this.orderInfo.status == 1) {
          this.getCoupon()
        }
        if (this.orderInfo.status != 2 && this.orderInfo.status != 1) {
          uni.showLoading({
            title: "订单正在结算中",
          })
          this.orderStatusInterval = setInterval(() => {
            this.getDetail()
          }, 3000)
        } else {
          clearInterval(this.orderStatusInterval)
          this.orderStatusInterval = null
          uni.hideLoading()
        }
      })
    },
    getCoupon() {
      getAvailableCouponsForOrders({
        orderNo: this.orderInfo.tradeNo,
      }).then((res) => {
        this.couponList = res.data
          ? res.data.map((item) => {
              return {
                ...item,
                status: 0,
              }
            })
          : []
      })
    },
    handleSelectedCoupon(e) {
      console.log(e)
      if (!e) {
        this.selectedCoupon = {}
        this.orderShouldAmount = this.orderInfo.realAmount || 0
        this.orderCouponAmount = 0
        this.showCouponList = false
        return
      }
      this.selectedCoupon = e
      calculationRealPayAmount({
        orderNo: this.orderInfo.tradeNo,
        promotionId: e.promotionId,
      }).then((res) => {
        this.orderShouldAmount = res.data.realAmount || 0
        this.orderCouponAmount = res.data.couponAmount || 0
        this.showCouponList = false
      })
    },
    getEvaluationOptions() {
      getEvaluationByOrder({
        orderId: this.orderInfo.id,
        stationId: this.orderInfo.stationId,
      }).then((res) => {
        this.evaluationOptions = res.data || []
        let filter = this.evaluationOptions.filter((item) => {
          return item.selected
        })
        if (filter.length !== 0) {
          this.hideEvalButton = true
        }
      })
    },
    changeAllShow(e) {
      this[e] = !this[e]
    },
    onClipboard(e) {
      uni.setClipboardData({
        data: e,
        success: function () {},
        fail: function (e) {
          console.error("复制失败", e)
        },
      })
    },
    goAddCar(e) {
      uni.navigateTo({
        url: "/pages/wode/car/addCar?type=" + e,
      })
    },
    toPay() {
      orderPayment({
        tradeNo: this.orderNo,
        promotionId: this.selectedCoupon.promotionId,
      }).then((res) => {
        uni.showToast({
          title: "支付成功",
          icon: "none",
        })
        setTimeout(() => {
          uni.redirectTo({
            url: "/pages/stations/orders/orderList",
          })
        }, 1000)
      })
    },
    toStation() {
      uni.navigateTo({
        url: "/pages/stations/site/stationDetail?id=" + this.orderInfo.stationId,
      })
    },
    selectedEval(item) {
      let filter = this.evaluationOptions.filter((f) => {
        return f.selected
      })
      if (filter.length === 3 && !item.selected) {
        uni.showToast({
          title: "最多可选择3项",
          icon: "none",
        })
      } else {
        item.selected = !item.selected
      }
    },
    confirmEval() {
      let filter = this.evaluationOptions.filter((item) => {
        return item.selected
      })
      if (filter.length === 0) {
        uni.showToast({
          title: "请至少选择一项",
          icon: "none",
        })
        return
      }
      let labelList = filter.map((item) => {
        return item.value
      })

      submitEvaluationByOrder({
        labelList,
        orderId: this.orderInfo.id,
        stationId: this.orderInfo.stationId,
      }).then((res) => {
        if (res && res.code == 200) {
          uni.showToast({
            title: "提交成功",
            icon: "none",
          })
          this.getDetail()
        }
      })
    },
    callPhone() {
      getConfigKey("customer_service_tel").then((res) => {
        let telNumber = res.msg
        uni.makePhoneCall({
          phoneNumber: telNumber, // 这里就是自己要拨打的电话号码
          success: (res) => {
            console.log("调用成功!")
          },
          fail: (res) => {
            console.log("调用失败!")
          },
        })
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.dis-flex {
  display: flex;
}

.dis-flex-ac {
  display: flex;
  align-items: center;
}

.dis-flex-between {
  @extend .dis-flex-ac;
  justify-content: space-between;
}

.dis-flex-ac-jc {
  @extend .dis-flex-ac;
  justify-content: center;
}

.copy {
  width: 64rpx;
  height: 32rpx;
  font-size: 20rpx;
  display: block;
  color: #72727d;
  background: #f8f8f8;
  line-height: 32rpx;
  text-align: center;
  border-radius: 27rpx 27rpx 27rpx 27rpx;
  margin-right: 14rpx;
}

.settlement-page {
  background: #f4f4f4;
  width: 100%;
  min-height: 100vh;
  box-sizing: border-box;
  padding: 60rpx 24rpx 180rpx;
  box-sizing: border-box;
  position: relative;

  .title-box {
    padding-left: 20rpx;

    .font-size-40 {
      font-size: 40rpx;
      color: #333333;
      padding-bottom: 16rpx;
      font-weight: 700;
      line-height: 40rpx;
    }

    .stay-state {
      color: #ff4d00;
    }

    .tip {
      font-size: 24rpx;
      color: #999999;
      line-height: 24rpx;
      padding-bottom: 32rpx;
      padding-top: 16rpx;
    }
  }

  .card {
    min-height: 193rpx;
    background: #ffffff;
    border-radius: 20rpx 20rpx 20rpx 20rpx;
    margin-bottom: 20rpx;
    box-sizing: border-box;
  }

  .btn-public {
    width: 200rpx;
    font-weight: 400;
    font-size: 24rpx;
    color: #999999;
    line-height: 24rpx;
    padding: 34rpx 5rpx 28rpx;
    margin: 0 auto;
    justify-content: center;

    image {
      width: 24rpx;
      height: 24rpx;
      margin-left: 10rpx;
    }
  }

  .stop-box {
    padding: 31rpx 24rpx 40rpx;

    .stop-content {
      image {
        width: 82rpx;
        height: 82rpx;
        margin-right: 24rpx;
        font-weight: 700;
      }

      .stop-name {
        width: 380rpx;
        height: 43rpx;
        font-size: 30rpx;
        color: #333333;
        font-weight: 700;
        line-height: 34rpx;
        padding-bottom: 9rpx;
        padding-top: 3rpx;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-all; // 数字 字母换行
      }

      .license-plate {
        height: 34rpx;
        font-size: 24rpx;
        color: #72727d;
        line-height: 34rpx;
      }
    }

    .go-box {
      font-size: 26rpx;
      color: #72727d;
      line-height: 34rpx;
      padding-bottom: 20rpx;

      image {
        width: 28rpx;
        height: 28rpx;
        margin-top: 6rpx;
        margin-left: 10rpx;
      }
    }
  }

  .stop-box2 {
    padding: 31rpx 30rpx 33rpx 24rpx;
    min-height: auto;

    .stop-content {
      image {
        width: 50rpx;
        height: 50rpx;
        margin-right: 16rpx;
      }

      .text {
        height: 34rpx;
        font-size: 34rpx;
        color: #333333;
        line-height: 34rpx;
      }
    }

    .add {
      width: 44rpx;
      height: 44rpx;
    }

    .numberplate {
      height: 34rpx;
      font-size: 24rpx;
      color: #72727d;
      line-height: 34rpx;
    }
  }

  .detail-box {
    padding: 42rpx 24rpx 0;

    .detail-title {
      max-width: 640rpx;
      min-height: 34rpx;
      font-size: 30rpx;
      color: #333333;
      line-height: 34rpx;
      font-weight: 700;

      image {
        width: 28rpx;
        height: 28rpx;
        margin-left: 10rpx;
        margin-top: 3rpx;
        flex-shrink: 0;
      }
    }

    .all-price-box {
      padding-top: 24rpx;
      justify-content: space-between;

      .text {
        padding-top: 10rpx;
        font-size: 24rpx;
        color: #333333;
        line-height: 24rpx;
      }

      .price {
        height: 34rpx;
        font-size: 44rpx;
        color: #333333;
        line-height: 34rpx;
        position: relative;
        padding-left: 18rpx;
        font-weight: 700;

        .unit {
          position: absolute;
          height: 22rpx;
          left: 0;
          bottom: 1rpx;
          font-size: 20rpx;
          font-weight: 400;
        }
      }
    }

    .price-box {
      padding-top: 30rpx;

      .text {
        height: 24rpx;
        font-size: 24rpx;
        color: #999999;
        line-height: 24rpx;
      }

      .price {
        font-weight: 700;
        font-size: 28rpx;
        color: #333333;
        line-height: 28rpx;
      }
    }

    .line {
      width: 100%;
      height: 0rpx;
      border-top: 1rpx solid rgba(0, 0, 0, 0.08);
      padding-bottom: 34rpx;
      margin-top: 34rpx;
    }

    .order-title {
      font-size: 24rpx;
      color: #333333;
      line-height: 24rpx;
      padding-bottom: 44rpx;
    }

    .order-item {
      padding-bottom: 34rpx;

      .text {
        height: 24rpx;
        font-size: 24rpx;
        color: #999999;
        line-height: 24rpx;
      }

      .value {
        height: 24rpx;
        font-size: 26rpx;
        color: #333333;
        line-height: 24rpx;

        .copy {
          width: 64rpx;
          height: 32rpx;
          font-size: 20rpx;
          display: block;
          color: #72727d;
          background: #f8f8f8;
          line-height: 32rpx;
          text-align: center;
          border-radius: 27rpx 27rpx 27rpx 27rpx;
          margin-right: 14rpx;
        }
      }
    }

    .padd-b-0 {
      padding-bottom: 0rpx !important;
    }
  }

  .evaluate-box {
    padding: 30rpx 0;

    .title {
      padding: 0 24rpx;
      height: 28rpx;
      font-size: 28rpx;
      color: #333333;
      line-height: 28rpx;
      font-weight: 700;

      .tip {
        width: 96rpx;
        height: 31rpx;
        background: #f8f8f8;
        color: #ff4d00;
        border-radius: 6rpx 6rpx 6rpx 6rpx;
        justify-content: center;
        margin-left: 10rpx;
        font-size: 20rpx;
        font-weight: 400;
      }
    }

    .btn-box {
      padding: 33rpx 14rpx 20rpx 24rpx;
      flex-wrap: wrap;
      height: 186rpx;
      overflow: hidden;
      margin-bottom: 34rpx;
      box-sizing: content-box;

      .btn-item {
        height: 54rpx;
        background: #f8f9fa;
        border-radius: 34rpx 34rpx 34rpx 34rpx;
        border: 1rpx solid #f8f9fa;
        padding: 16rpx 24rpx;
        box-sizing: border-box;
        font-size: 24rpx;
        color: #72727d;
        margin-bottom: 24rpx;
        margin-right: 22rpx;
      }

      .active {
        background: #f8f8f8;
        border: 1rpx solid #ff4d00;
        color: #ff4d00;
      }
    }

    .showEvaluateAll.btn-box {
      height: auto;
      overflow: visible;
      margin-bottom: 0rpx;
    }

    .box {
      position: relative;
      top: -16rpx;

      .btn {
        width: 148rpx;
        height: 24rpx;
        font-weight: 400;
        font-size: 24rpx;
        color: #999999;
        line-height: 24rpx;
        background: #ffffff;
        position: relative;
        margin: 0 auto;
        justify-content: center;
        z-index: 1;

        image {
          width: 24rpx;
          height: 24rpx;
          margin-left: 10rpx;
          flex-shrink: 0;
        }
      }

      .line {
        position: absolute;
        left: 0rpx;
        top: 12rpx;
        width: 702rpx;
        height: 1rpx;
        z-index: 0;
        border-top: 1rpx dashed rgba(0, 0, 0, 0.08);
      }
    }

    .submit {
      margin: 10rpx auto 0;
      width: 654rpx;
      height: 77rpx;
      border-radius: 39rpx 39rpx 39rpx 39rpx;
      border: 1rpx solid #333333;
      justify-content: center;
      font-size: 28rpx;
      color: #333333;
    }
  }

  .order-detail-box {
    padding: 24rpx 24rpx 7rpx;

    .detail-title {
      // height: 34rpx;
      font-size: 30rpx;
      color: #333333;
      line-height: 34rpx;
      padding-bottom: 30rpx;
      font-weight: 700;

      .detail-text {
        height: 24rpx;
        font-size: 24rpx;
        color: #999999;
        line-height: 24rpx;
      }
    }

    .order-detail-cell {
      padding-bottom: 28rpx;

      .text {
        height: 24rpx;
        font-size: 24rpx;
        color: #333333;
        line-height: 24rpx;
        width: 156rpx;
      }

      .value {
        height: 28rpx;
        font-weight: 400;
        font-size: 26rpx;
        color: #333333;
        line-height: 28rpx;
        flex: 1;
        text-align: right;
        white-space: break-spaces;
        word-break: break-all;
      }

      .size24 {
        font-size: 24rpx;
        color: #333333;
        line-height: 24rpx;
      }

      .size36 {
        height: 36rpx;
        line-height: 36rpx;
        font-size: 36rpx;
      }

      .size28 {
        height: 28rpx;
        line-height: 28rpx;
        font-size: 28rpx;
      }

      .w700 {
        font-weight: 700;
      }
    }

    .line {
      width: 100%;
      height: 0rpx;
      border-top: 1rpx solid rgba(0, 0, 0, 0.08);
      padding-bottom: 25rpx;
      margin-top: 19rpx;
    }
  }

  .coupon {
    padding: 40rpx 24rpx;
    min-height: auto;

    .title {
      font-size: 30rpx;
      color: #333333;
      line-height: 34rpx;
      font-weight: 700;
    }

    .value {
      font-size: 26rpx;
      color: #72727d;

      image {
        width: 24rpx;
        height: 24rpx;
        margin-left: 4rpx;
      }
    }
  }

  .coupon-list {
    padding: 40rpx 24rpx 20rpx;
  }

  .coupon-bottom-tip {
    font-size: 24rpx;
    color: #999;
    line-height: 24rpx;
    padding: 20rpx 0 60rpx;
    text-align: center;
  }

  .btn-bottom-box {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    background: #ffffff;
    padding: 30rpx 24rpx 59rpx;

    .value {
      font-size: 24rpx;
      color: #333333;
      line-height: 24rpx;
      align-items: flex-end;
      height: 56rpx;

      .unit,
      .price {
        font-weight: 700;
        font-size: 24rpx;
        color: #333333;
      }

      .price {
        font-size: 56rpx;
        height: 56rpx;
        line-height: 72rpx;
      }
    }

    .tbn {
      width: 290rpx;
      height: 88rpx;
      background: #333;
      border-radius: 46rpx 46rpx 46rpx 46rpx;
      justify-content: center;
      font-size: 32rpx;
      color: #ffffff;
      line-height: 34rpx;
    }
  }
}

::v-deep.uicon-arrow-right {
  width: 9rpx;
  height: 16rpx;
  padding-left: 13rpx;
  position: relative;
  top: 2rpx !important;
}
</style>
