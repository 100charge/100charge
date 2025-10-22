<template>
  <view class="my-car-box">
    <view class="my-c-box" v-if="carList && carList.length > 0">
      <view class="m-item" v-for="car in carList" :key="car.appCarId" @click="toCarInfo(car)">
        <view class="default-view" v-if="car.isDefault == 1"> 默认 </view>
        <view class="m-title">
          <view>{{ car.plateNo }}</view>
          <view class="default-view-btn" v-if="car.isDefault == 0" @tap.stop="tapDefault(car)"> 设为默认 </view>
        </view>
        <view class="m-con">
          <view class="m-info">
            <view v-if="!hiddenBtn" class="name">车辆用途：{{ car.carType || "" }}</view>
            <view v-if="car.vin" class="status">车辆VIN码：{{ car.vin }}</view>
            <view v-if="car.remark && car.verified == 3" class="status">驳回原因：{{ car.remark }}</view>
          </view>
        </view>
        <view class="m-btn" v-if="!hiddenBtn">
          <view class="delete-text" @tap.stop="handleDelete(car)"> 删除 </view>
        </view>
      </view>
    </view>
    <view class="add-c-box" v-else>
      <view class="empty-wrapper">
        <image src="/static/images/index/empty1.png" mode="aspectFit"></image>
        <view class="empty-text">暂未添加车辆</view>
      </view>
    </view>
    <view class="main-confirm-bottom" v-if="!hiddenBtn">
      <view class="confirm-btn" @click="addCar">添加车辆</view>
    </view>
  </view>
</template>

<script>
import { getUserCarList, deleteCar, editPlateNo } from "@/config/api.js"
export default {
  data() {
    return {
      carList: [],
      hiddenBtn: false,
    }
  },
  onLoad(options) {
    this.userCarList()
  },
  methods: {
    addCar() {
      uni.navigateTo({
        url: "/pages/wode/car/addCar",
      })
    },
    toCarInfo(car) {
      uni.navigateTo({
        url: "/pages/wode/car/addCar?no=" + car.plateNo + "&carId=" + car.appCarId + "&default=" + car.isDefault,
      })
    },
    //获取我的爱车列表
    userCarList() {
      getUserCarList({}).then((res) => {
        this.carList = res.data
      })
    },
    // 设置默认
    tapDefault(car) {
      editPlateNo({
        appCarId: car.appCarId,
        isDefault: true,
        plateNo: car.plateNo,
      }).then((res) => {
        uni.showToast({
          title: "设置成功",
          icon: "none",
        })
        setTimeout(() => {
          this.userCarList()
        }, 1000)
      })
    },
    handleDelete(car) {
      let that = this
      uni.showModal({
        title: "提示",
        content: "确定要删除该车辆吗？",
        showCancel: true,
        confirmColor: "#ff6b01",
        success(res) {
          if (res.confirm) {
            deleteCar({
              carId: car.appCarId,
            }).then((res) => {
              uni.showToast({
                title: "删除成功",
                icon: "none",
              })
              that.userCarList()
            })
          }
        },
      })
    },
  },
  //下拉刷新
  onPullDownRefresh() {
    this.userCarList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
}
</script>

<style scoped lang="scss">
.my-car-box {
  padding: 24rpx 24rpx 150rpx 24rpx;
  .my-c-box {
    .m-item {
      width: 100%;
      text-align: left;
      background: #ffffff;
      border-radius: 20rpx;
      margin-bottom: 24rpx;
      padding: 30rpx;
      position: relative;

      .default-view {
        background-color: #f5f4f4;
        width: 72rpx;
        height: 38rpx;
        border-radius: 0rpx 19rpx 0rpx 14rpx;
        text-align: center;
        line-height: 38rpx;
        position: absolute;
        right: 0;
        top: 0;
        font-size: 22rpx;
        color: $primary-color;
      }
      .m-title {
        width: 100%;
        height: 78rpx;
        font-size: 40rpx;
        color: #333;
        font-weight: 600;
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        .default-view-btn {
          color: $primary-color;
          font-size: 26rpx;
          font-weight: 400;
        }
      }
      .m-con {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        .m-info {
          width: 100%;
          font-size: 26rpx;
          color: $basic-color;
          margin-top: 6rpx;
          .status {
            margin-top: 12rpx;
          }
        }
        image {
          width: 229rpx;
          height: 150rpx;
          margin-top: -30rpx;
        }
      }
      .m-btn {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 24rpx;
        .delete-text {
          font-size: 24rpx;
          color: $uni-color-error;
        }
        .auth-button,
        .auth-button-finished {
          display: flex;
          justify-content: center;
          align-items: center;
          // width: 130rpx;
          padding: 0 12rpx;
          height: 44rpx;
          line-height: 40rpx;
          background-color: $primary-bg-color;
          color: $primary-color;
          border-radius: 10rpx;
          font-weight: 400;
          font-size: 24rpx;
        }
        .auth-button-finished {
          color: #999;
          background-color: #dddddd;
        }
      }
    }
  }
  .add-c-box {
    position: relative;
  }
}
</style>
