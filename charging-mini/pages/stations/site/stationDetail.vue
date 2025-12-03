<template>
  <view class="container">
    <!-- 主要内容 -->
    <scroll-view class="main-content">
      <!-- 图片轮播 -->
      <view class="image-gallery" v-if="stationDetail.stationImageList && stationDetail.stationImageList.length !== 0">
        <scroll-view class="image-scroll" scroll-x="true" show-scrollbar="false">
          <view class="images-container">
            <view class="image-item" v-for="(image, index) in stationDetail.stationImageList" :key="index">
              <image :src="image" class="station-image" mode="aspectFill" @click="previewImage(index)"></image>
            </view>
          </view>
        </scroll-view>
      </view>

      <!-- 充电站基本信息 -->
      <view class="station-info">
        <!-- 背景图片 -->
        <image src="/static/img/shap.png" class="station-bg-image"></image>

        <!-- 内容层 -->
        <view class="station-content">
          <view class="station-header">
            <view class="station-name">{{ stationDetail.name }}</view>
            <view class="station-tags">
              <text class="tag rating">{{ stationDetail.starLabel || 5 }}分</text>

              <view v-for="(item, index) in stationDetail.labelList" :key="index">
                <text class="tag open">{{ item }}</text>
                <text
                  class="tag-line"
                  v-if="stationDetail.labelList.length && index !== stationDetail.labelList.length - 1"
                ></text>
              </view>
              <!-- <text class="tag-line"></text> <text class="tag status">{{ stationDetail.status }}</text>
              <text class="tag-line"></text> <text class="tag feature">{{ stationDetail.feature }}</text>
              <text class="tag-line"></text>
              <text class="tag open">{{ stationDetail.openStatus }}</text> -->
            </view>
          </view>

          <!-- 地址信息 -->
          <view class="address-section">
            <view class="address-info">
              <text class="address-text">{{ stationDetail.address }}</text>
              <view class="distance-nav" @click="locationHandle">
                <image src="/static/img/daohang.png" class="nav-icon"></image>
                <text class="distance-text">{{ stationDetail.distance }}</text>
              </view>
            </view>
            <view class="business-hours">
              <text class="hours-text"
                >营业时间：{{ stationDetail.perationBeginTime }} - {{ stationDetail.perationEndTime || "" }}</text
              >
            </view>
          </view>

          <!-- 服务设施 -->
        </view>
      </view>
      <view class="facilities-section">
        <view class="facilities">
          <view class="facility-item">
            <image src="/static/img/parking.png" class="facility-icon"></image>
            <text class="facility-text">充电专用车位</text>
          </view>
          <view class="facility-item">
            <image src="/static/img/washroom.png" class="facility-icon"></image>
            <text class="facility-text">洗手间</text>
          </view>
          <view class="facility-item">
            <image src="/static/img/canopy.png" class="facility-icon"></image>
            <text class="facility-text">雨棚</text>
          </view>
          <view class="facility-item">
            <image src="/static/img/lighting.png" class="facility-icon"></image>
            <text class="facility-text">场站照明</text>
            <image src="/static/img/row.png" class="arrow-right"></image>
          </view>
        </view>
      </view>

      <!-- 充电桩信息 -->
      <view class="charger-section">
        <view class="charger-row">
          <view class="charger-item">
            <view class="charger-badge fast">
              <image src="/static/img/kuai.png" class="badge-icon"></image>
            </view>
            <text class="charger-status"
              >闲{{ stationDetail.fastChargingIdle
              }}<text class="status-divider">/{{ stationDetail.fastCharging }}</text></text
            >
            <view class="detail-wrapper" @click="pileHandle('DC')">
              <text class="detail-text">详情</text>
              <uni-icons type="right" size="14" color="#999"></uni-icons>
            </view>
          </view>
          <view class="charger-item">
            <view class="charger-badge slow">
              <image src="/static/img/low.png" class="badge-icon"></image>
            </view>
            <text class="charger-status"
              >闲{{ stationDetail.slowChargingIdle
              }}<text class="status-divider">/{{ stationDetail.slowCharging }}</text></text
            >
            <view class="detail-wrapper" @click="pileHandle('AC')">
              <text class="detail-text">详情</text>
              <uni-icons type="right" size="14" color="#999"></uni-icons>
            </view>
          </view>
        </view>
      </view>

      <!-- 价格信息 -->
      <view class="price-section">
        <view class="section-title">价格信息</view>
        <view class="current-period">
          <text class="period-label">当前时段</text>
        </view>
        <view class="price-display">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ stationDetail.chargeFee }}</text>
          <text class="price-unit">/度</text>
        </view>
        <view class="price-schedule">
          <text class="schedule-text">{{ stationDetail.currentTime }}</text>
          <view class="all-periods" @click="priceHandle">
            <text class="all-text">全部时段</text>
            <uni-icons type="right" size="14" color="#999"></uni-icons>
          </view>
        </view>
        <view class="parking-all-section" v-if="stationDetail.parkingFeeTip">
          <view class="parking-header">
            <image src="/static/img/bluep.png" class="parking-icon"></image>
            <text class="parking-title">停车参考价</text>
          </view>
          <view class="parking-info">
            <text class="parking-text">{{ stationDetail.parkingFeeTip || "" }}</text>
          </view>
        </view>
      </view>

      <!-- 营业信息 -->
      <view class="business-section">
        <view class="section-title">营业信息</view>
        <view class="business-item">
          <text class="business-label">服务提供</text>
          <text class="business-value">{{ stationDetail.companyName || "" }}</text>
          <view class="business-right" @click="licenseHandle">
            <text class="detail-text">详情</text>
            <uni-icons type="right" size="14" color="#999"></uni-icons>
          </view>
        </view>
      </view>

      <!-- 评价信息 -->
      <view class="business-section" v-if="commentList && commentList.length !== 0">
        <view class="section-title">评论</view>
        <view class="comment-list">
          <view class="comment-item flex-row flex-ali-center" v-for="item in commentList" :key="item.value">
            {{ item.label }} ({{ item.qty || 0 }})
          </view>
        </view>
      </view>

      <!-- 底部空白 -->
      <view class="bottom-space"></view>
    </scroll-view>

    <!-- 底部操作栏 -->
    <view class="bottom-actions">
      <view class="price-info">
        <text class="price-symbol">¥</text>
        <text class="price-value">{{ stationDetail.chargeFee }}</text>
        <text class="price-unit">/度</text>
      </view>
      <view class="scan-charge-btn" @click="scanCodeHandle">
        <image src="/static/img/saoma.png" mode="widthFix"></image>
      </view>
    </view>

    <stationPrice ref="stationPriceRef" />
    <pileListPopup ref="pileListPopupRef" />
    <businessLicense ref="businessLicenseRef" />
  </view>
</template>
<script>
import app from "@/static/js/app.js"
import { stationDetail, getEvaluationByStationId } from "@/config/api.js"
import stationPrice from "./components/station-price.vue"
import businessLicense from "./components/business-license.vue"
import pileListPopup from "./components/pile-list.vue"
export default {
  components: {
    stationPrice,
    businessLicense,
    pileListPopup,
  },
  data() {
    return {
      stationId: "",
      isIpx: uni.getStorageSync("isIpx"),
      stationDetail: {},
      commentList: [],
    }
  },
  onLoad(options) {
    this.stationId = options.id
    this.getDetailData()
    this.getEvaluationList()
  },
  methods: {
    getEvaluationList() {
      getEvaluationByStationId({
        stationId: this.stationId,
      }).then((res) => {
        this.commentList = res.data
      })
    },
    getDetailData() {
      stationDetail({
        id: this.stationId,
        lat: uni.getStorageSync("latitude") || 36.6667,
        lng: uni.getStorageSync("longitude") || 116.9949,
      }).then((res) => {
        this.stationDetail = {
          ...res.data,
          labelList: res.data.labelName ? res.data.labelName.split(",") : [],
          distance:
            +res.data.distance > 1000 ? (+res.data.distance / 1000).toFixed(1) + " km" : +res.data.distance + " m",
          starLabel: +res.data.starLabel || 0,
          chargingTypeList: res.data.chargingType ? res.data.chargingType.split(",") : [],
        }
      })
    },
    scanCodeHandle() {
      let that = this
      uni.scanCode({
        success(res) {
          let resp = {
            result: res.result,
            scanType: res.scanType,
          }
          uni.navigateTo({
            url: "/pages/stations/site/previewCharge?code=" + encodeURIComponent(resp.result),
          })
        },
        fail(e) {
          console.log(e)
        },
      })
    },
    // 通过 URL 地址获取参数
    getUrlParam(url) {
      try {
        let href = url
        if (href.indexOf("?") > -1) {
          let paramStr = href.split("?")[1]
          let paramArr = paramStr.split("&")
          let returnObj = {}
          for (let i = 0; i < paramArr.length; i++) {
            let paramItem = paramArr[i].split("=")
            returnObj[paramItem[0]] = paramItem[1]
          }
          return returnObj
        } else {
          return {}
        }
      } catch (error) {
        return {}
      }
    },
    licenseHandle() {
      this.$refs.businessLicenseRef.onOpen(this.stationDetail)
    },
    priceHandle() {
      this.$refs.stationPriceRef.onOpen(this.stationDetail)
    },
    pileHandle(type) {
      this.$refs.pileListPopupRef.onOpen({
        ...this.stationDetail,
        type: type,
      })
    },
    commentHandle() {
      uni.navigateTo({
        url: "/pages/stations/site/stationComment?id=" + this.stationId,
      })
    },

    //预览图片
    previewImage: function (index) {
      let urls = this.stationDetail.stationImageList
      uni.previewImage({
        current: index,
        urls: urls,
      })
    },
    locationHandle() {
      uni.openLocation({
        latitude: this.stationDetail.lat,
        longitude: this.stationDetail.lng,
        name: this.stationDetail.name, // 位置名 alipay必填
        address: this.stationDetail.address, // 详细地址 alipay必填
        success() {},
        complete(e) {
          console.log(e)
        },
      })
    },
  },
}
</script>

<style scoped>
.tag-line {
  width: 1px;
  height: 20rpx;
  background-color: #888888;
  margin-top: 14rpx;
}
page {
  background-color: #f5f5f5;
}

.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 主要内容 */
.main-content {
  background-color: #f5f5f5;
}

/* 图片轮播 */
.image-gallery {
  padding: 20rpx;
}

.image-scroll {
  height: 200rpx;
}

.images-container {
  display: flex;
  gap: 16rpx;
  height: 100%;
  white-space: nowrap;
}

.image-item {
  flex-shrink: 0;
  width: 300rpx;
  border-radius: 16rpx;
  overflow: hidden;
  background-color: #f5f5f5;
}

.station-image {
  width: 100%;
  height: 100%;
  border-radius: 16rpx;
  object-fit: cover;
}

/* 充电站信息 */
.station-info {
  position: relative;
  margin: 20rpx 20rpx 0;
  margin-bottom: 20rpx;

  border-radius: 16rpx;
  overflow: hidden;
}

/* 背景图片 */
.station-bg-image {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  object-fit: cover;
  object-position: right center;
}

/* 内容层 */
.station-content {
  position: relative;
  z-index: 2;
  padding: 20rpx;
}

.station-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  padding-bottom: 10rpx;
}

.station-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-top: 20rpx;
}
.line {
  width: 100%;
  height: 1rpx;
  border-bottom: 1px dashed #888888;
  width: 90%;
  margin-left: 2vh;
  margin-top: 26rpx;
  margin-bottom: 10rpx;
  border-style: dashed;
  border-width: 0 0 1px 0;
  border-color: #888888;
  border-image: repeating-linear-gradient(to right, #888888 0, #888888 4px, transparent 4px, transparent 12px) 1;
}

.tag {
  padding: 8rpx 16rpx;
  border-radius: 12rpx;
  font-size: 24rpx;
  color: #888;
  top: 0;
  right: 0;
}

.tag.feature {
  background-color: #fff5f0;
  color: #ff6b35;
}

.address-info {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16rpx;
}
.facilities-section {
  margin: 20rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 20rpx;
}

.address-text {
  width: 76%;
  font-size: 26rpx;
  color: #333;
  line-height: 1.8;
  margin-right: 20rpx;
  margin-top: 60rpx;
}

.distance-nav {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
  width: 23%;
  margin-top: 40rpx;
}

.nav-icon {
  width: 32rpx;
  height: 32rpx;
  background: #f9f9f9;
  border-radius: 50%;
  padding: 16rpx;
  margin-top: 20rpx;
}

.distance-text {
  font-size: 24rpx;
  color: #666;
  font-weight: bold;
  margin-top: 10rpx;
}

.business-hours {
  font-size: 26rpx;
  color: #888;
  padding-bottom: 20rpx;
  background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
  padding: 10rpx;
}

/* 服务设施 */
.facilities {
  display: flex;
  align-items: center;
  padding: 20rpx 0 20rpx;
  gap: 30rpx;
}

.facility-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  flex: 1;
  justify-content: center;
}

.facility-item:first-child {
  justify-content: flex-start;
}

.facility-item:last-child {
  justify-content: flex-end;
}

.facility-icon {
  width: 32rpx;
  height: 32rpx;
  flex-shrink: 0;
}

.facility-text {
  font-size: 24rpx;
  color: #666;
  white-space: nowrap;
}

.arrow-right {
  width: 16rpx;
  height: 16rpx;
  margin-left: 4rpx;
  flex-shrink: 0;
}

/* 充电桩信息 */
.charger-section {
  background-color: white;
  margin-bottom: 20rpx;
  margin: 20rpx;
  border-radius: 20rpx;
}

.charger-row {
  display: flex;
  padding: 30rpx;
  gap: 40rpx;
}

.charger-item {
  display: flex;
  align-items: center;
  flex: 1;
  gap: 20rpx;
  background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
  padding: 8rpx;
}

.charger-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40rpx;
  height: 40rpx;
  flex-shrink: 0;
}

.badge-icon {
  width: 40rpx;
  height: 40rpx;
  object-fit: contain;
}

.charger-status {
  font-size: 28rpx;
  flex: 1;
}

.charger-item:first-child .charger-status {
  color: #ff6b35;
}

.charger-item:last-child .charger-status {
  color: #4a90e2;
}

.status-divider {
  font-size: 28rpx;
  color: #666;
}

.detail-wrapper {
  display: flex;
  align-items: center;
  gap: 4rpx;
  flex-shrink: 0;
}

.detail-text {
  font-size: 28rpx;
  color: #999;
}

.arrow-icon {
  width: 24rpx;
  height: 24rpx;
}

/* 价格信息 */
.price-section {
  background-color: white;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
  padding: 20rpx;
}

.section-titles {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.current-period {
  margin-bottom: 16rpx;

  padding-left: 20rpx;
}

.period-label {
  font-size: 28rpx;
  color: #999;
}

.price-display {
  display: flex;
  align-items: baseline;
  gap: 4rpx;
  margin-bottom: 10rpx;
  margin-left: 20rpx;
  margin-top: 20rpx;
}

.price-symbol {
  font-size: 40rpx;
  color: #ff6b35;
  font-weight: bold;
}

.price-value {
  font-size: 48rpx;
  color: #ff3b14;
  font-weight: bold;
  line-height: 1;
}

.price-unit {
  font-size: 24rpx;
  color: #ff6b35;
}

.price-schedule {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fdfdfd;
  padding: 20rpx;
}

.schedule-text {
  font-size: 28rpx;
  color: #666;
}

.all-periods {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.all-text {
  font-size: 28rpx;
  color: #666;
}

/* 停车参考价 */
.parking-section {
  background-color: white;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.parking-all-section {
  background: #f9f9f9;
  padding: 20rpx;
}

.parking-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 20rpx;
}

.parking-icon {
  width: 32rpx;
  height: 32rpx;
}

.parking-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.parking-info {
  margin-bottom: 30rpx;
}

.parking-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

/* 操作行容器，浅灰边框+圆角，内边距 */
.parking-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 20rpx;
  border: 2rpx solid rgba(20, 159, 255, 0.15);
  background-color: #ffffff;
  border-radius: 16rpx;
}

/* 左侧蓝色提示文字 */
.parking-hint {
  color: #149fff;
  font-size: 26rpx;
}

/* 右侧描边圆角按钮 */
.parking-input-btn {
  border: 2rpx solid #149fff;
  color: #149fff;
  background: #dbf0ff;
  border-radius: 12rpx;
  padding: 12rpx 24rpx;
  font-size: 26rpx;
  line-height: 1;
}

/* 营业信息 */
.business-section {
  background-color: white;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 20rpx;
}
.comment-list {
  display: flex;
  justify-content: flex-start;
  align-items: space-between;
  flex-wrap: wrap;
}
.comment-item {
  height: 60rpx;
  background: #f8f9fa;
  border-radius: 34rpx;
  font-size: 28rpx;
  padding: 0 24rpx;
  line-height: 50rpx;
  margin-top: 24rpx;
  margin-right: 32rpx;
  color: #333;
}

.business-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-top: 20rpx;
  padding-left: 20rpx;
}

.business-label {
  font-size: 28rpx;
  color: #666;
}

.business-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.business-right {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

/* 评论区 */
.comment-section {
  background-color: white;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 20rpx;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
  padding: 20rpx;
}

.view-all {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.view-all-text {
  font-size: 28rpx;
  color: #666;
}

.comment-filters {
  display: flex;
  gap: 40rpx;
  margin-bottom: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  padding-bottom: 20rpx;
}

.filter-item {
  font-size: 28rpx;
  color: #666;
  padding-bottom: 10rpx;
}

.filter-item.active {
  color: #333;
  font-weight: bold;
  border-bottom: 2rpx solid #333;
}

.comment-item {
  display: flex;
  gap: 20rpx;
}

.comment-avatar {
  width: 80rpx;
  height: 80rpx;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.comment-content {
  flex: 1;
}

.comment-user {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.username {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
}

.rating-stars {
  display: flex;
  gap: 4rpx;
}

.star {
  width: 24rpx;
  height: 24rpx;
}

.comment-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.5;
  margin-bottom: 16rpx;
  display: block;
}

.comment-time {
  font-size: 24rpx;
  color: #999;
}

/* 底部操作栏 */
.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: white;
  padding: 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.price-info {
  display: flex;
  align-items: baseline;
  gap: 4rpx;
}

.price-info .price-symbol {
  font-size: 24rpx;
  color: #ff6b35;
  font-weight: bold;
}

.price-info .price-value {
  font-size: 48rpx;
  color: #ff3b14;
  font-weight: bold;
}

.price-info .price-unit {
  font-size: 24rpx;
  color: #ff6b35;
}

.scan-charge-btn image {
  width: 350rpx;
}
.bottom-space {
  height: 200rpx;
}
</style>
