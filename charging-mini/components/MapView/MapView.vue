<template>
  <view class="map-container">
    <!-- 顶部浮动筛选按钮 -->
    <view class="map-filter-bar">
      <view class="filter-item-map">
        <text class="filter-text-map">综合排序</text>
        <text class="filter-arrow-map">▼</text>
      </view>
      <view class="filter-item-map">
        <text class="filter-text-map">距离/区域</text>
        <text class="filter-arrow-map">▼</text>
      </view>
      <view class="filter-item-map">
        <text class="filter-text-map">充电站</text>
        <text class="filter-arrow-map">▼</text>
      </view>
      <view class="filter-item-map">
        <text class="filter-text-map">筛选</text>
        <text class="filter-arrow-map">▼</text>
      </view>
    </view>

    <map
      id="map"
      ref="map"
      class="map"
      :scale="mapScale"
      show-location
      :latitude="latitude"
      :longitude="longitude"
      :markers="markers"
      @callouttap="calloutTap"
      @markertap="markerTap"
      @regionchange="regionChange"
    >
      <!-- 自定义标注点信息 -->
      <!-- #ifdef MP-WEIXIN -->
      <cover-view slot="callout">
        <cover-view
          v-for="(item, index) in markers"
          :key="item.stationId"
          :markerId="item.id"
          class="custom-callout-item"
        >
          <cover-view :class="selectedMarkId == item.stationId ? 'callout-box-active' : 'callout-box'">
            <cover-view class="price" :class="selectedMarkId == item.stationId ? 'active-price' : ''">
              <cover-view class="unit">¥</cover-view>
              <cover-view class="price-text">{{ item.fee || "0.00" }}</cover-view>
            </cover-view>
            <cover-view class="count">
              <cover-image
                class="speed-image"
                :src="item.type == 'fast' ? '/static/img/icon-fast.png' : '/static/img/icon-slow.png'"
                mode="scaleToFill"
              ></cover-image>
              <cover-view :class="item.type == 'fast' ? 'parallelogram-fast' : 'parallelogram-slow'">
                <cover-view class="parallelogram-text">
                  <cover-view class="parallelogram-text-idle"
                    >闲 {{ item.type == "fast" ? item.idleFastChargeCount : item.idleSlowChargeCount }}/</cover-view
                  >
                  <cover-view class="parallelogram-text-all">{{
                    item.type == "fast" ? item.fastCharging : item.slowCharging
                  }}</cover-view>
                </cover-view>
              </cover-view>
            </cover-view>
          </cover-view>
          <cover-image
            class="callout-triangle-active-image"
            :src="
              selectedMarkId == item.stationId ? '/static/img/icon-down-ff6b01.png' : '/static/img/icon-down-fff.png'
            "
            mode="scaleToFill"
          ></cover-image>
        </cover-view>
      </cover-view>
      <!-- #endif -->
    </map>

    <!-- 底部充电站卡片（选中时显示） -->
    <view class="station-card" v-if="selectedStation">
      <view class="station-header">
        <text class="station-name">{{ selectedStation.name }}</text>
        <view class="station-distance">
          <image class="distance-icon" src="/static/img/daohang.png" mode="aspectFit"></image>
          <text class="distance-text">{{ selectedStation.distance }}</text>
        </view>
      </view>
      <view class="station-details">
        <text class="rating">{{ selectedStation.rating }}分</text>
        <text class="last-charge">最近充电{{ selectedStation.lastCharge }}分钟前</text>
        <text class="special-offer">充电特惠</text>
        <text class="public-access">对外开放</text>
      </view>
      <view class="charger-availability">
        <view class="charger-type">
          <image class="charger-icon fast" src="/static/img/kuai.png" mode="aspectFit"></image>
          <text class="charger-label">快</text>
          <text class="charger-status">闲{{ selectedStation.fastAvailable }}/{{ selectedStation.fastTotal }}</text>
        </view>
        <view class="charger-type">
          <image class="charger-icon slow" src="/static/img/low.png" mode="aspectFit"></image>
          <text class="charger-label">慢</text>
          <text class="charger-status">闲{{ selectedStation.slowAvailable }}/{{ selectedStation.slowTotal }}</text>
        </view>
      </view>
      <view class="station-footer">
        <text class="price">¥ {{ selectedStation.price }} /度</text>
        <view class="navigate-btn" @click="navigateToStation(selectedStation)">
          <image mode="widthFix" src="/static/img/daohang-logo.png"></image>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "MapView",
  props: {
    stationList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      mapScale: 15,
      latitude: 36.666964,
      longitude: 117.382681,
      markers: [],
      selectedMarkId: null,
      selectedStation: null,
    }
  },
  watch: {
    stationList: {
      handler(newList) {
        this.updateMarkers(newList)
      },
      immediate: true,
    },
  },
  mounted() {
    // 获取当前位置
    this.getCurrentLocation()
  },
  methods: {
    getCurrentLocation() {
      uni.getLocation({
        type: "gcj02",
        success: (res) => {
          this.latitude = res.latitude
          this.longitude = res.longitude
        },
        fail: (err) => {
          console.log("获取位置失败:", err)
        },
      })
    },

    updateMarkers(stationList) {
      this.markers = stationList.map((station, index) => {
        return {
          id: index + 1,
          stationId: station.id || index,
          latitude: station.latitude || 36.6667 + Math.random() * 0.01,
          longitude: station.longitude || 116.9949 + Math.random() * 0.01,
          title: station.name,
          fee: station.price,
          type: station.fastTotal > 0 ? "fast" : "slow",
          idleFastChargeCount: station.fastAvailable || 0,
          idleSlowChargeCount: station.slowAvailable || 0,
          fastCharging: station.fastTotal || 0,
          slowCharging: station.slowTotal || 0,
          iconPath: "/static/img/icon-dingwei.png",
          width: 1,
          height: 1,
          joinCluster: true,
          customCallout: {
            display: "ALWAYS",
            anchorX: 0,
            anchorY: 10,
          },
          callout: {
            content: station.name,
            display: "ALWAYS",
          },
        }
      })
    },

    markerTap(e) {
      const markerId = e.detail.markerId
      const marker = this.markers[markerId]
      if (marker) {
        this.selectedMarkId = marker.stationId
        this.selectedStation = this.stationList[markerId]
      }
    },

    calloutTap(e) {
      const markerId = e.detail.markerId
      const marker = this.markers[markerId]
      if (marker) {
        this.selectedMarkId = marker.stationId
        this.selectedStation = this.stationList[markerId]
      }
    },

    regionChange(e) {
      if (e.type === "end") {
        this.$emit("onChangeCenter", {
          latitude: e.detail.centerLocation.latitude,
          longitude: e.detail.centerLocation.longitude,
        })
      }
    },

    navigateToStation(station) {
      uni.navigateTo({
        url: `/pages/station-detail/station-detail?stationData=${encodeURIComponent(JSON.stringify(station))}`,
      })
    },
  },
}
</script>

<style scoped>
.map-container {
  position: relative;
  height: 100%;
}

/* 地图顶部筛选条 */
.map-filter-bar {
  position: absolute;
  top: 20rpx;
  left: 20rpx;
  right: 20rpx;
  display: flex;
  justify-content: space-between;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15rpx;
  padding: 15rpx 20rpx;
  z-index: 100;
  backdrop-filter: blur(10rpx);
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
}

.filter-item-map {
  display: flex;
  align-items: center;
  gap: 8rpx;
  flex: 1;
  justify-content: center;
}

.filter-text-map {
  font-size: 24rpx;
  color: #333;
  font-weight: 500;
}

.filter-arrow-map {
  font-size: 18rpx;
  color: #999;
}

.map {
  width: 100%;
  height: 100%;
}

/* 自定义标注点样式 */
.custom-callout-item {
  width: 160rpx;
  height: 125rpx;
}

.callout-box-active {
  width: 160rpx;
  height: 100rpx;
  background: #ffffff;
  box-shadow: 0rpx 3rpx 9rpx 0rpx rgba(0, 0, 0, 0.1216);
  border-radius: 14rpx;
  border: 4rpx solid #ff6b35;
}

.callout-box {
  width: 160rpx;
  height: 100rpx;
  background: #ffffff;
  box-shadow: 0rpx 3rpx 9rpx 0rpx rgba(0, 0, 0, 0.1216);
  border-radius: 14rpx;
  border: 2rpx solid #fff;
}

.callout-triangle-active-image {
  margin: 0 auto;
  width: 40rpx;
  height: 20rpx;
  margin-top: -4rpx;
}

.price {
  color: #333;
  font-weight: 500;
  font-size: 30rpx;
  padding: 12rpx 14rpx 6rpx;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.active-price {
  color: #ff6b35;
}

.unit {
  font-weight: 400;
  font-size: 22rpx;
  margin-right: 4rpx;
  width: 20rpx;
}

.price-text {
  width: 140rpx;
}

.count {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 0 8rpx;
}

.speed-image {
  width: 44rpx;
  height: 36rpx;
  margin-right: 0rpx;
}

.parallelogram-fast,
.parallelogram-slow {
  width: 120rpx;
  height: 36rpx;
  background: rgba(255, 107, 1, 0.1);
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #333;
  font-size: 22rpx;
  border-radius: 6rpx;
  z-index: 9;
}

.parallelogram-slow {
  background: rgba(118, 177, 255, 0.15);
}

.parallelogram-text {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 36rpx;
  line-height: 36rpx;
  width: 120rpx;
}

.parallelogram-text-all {
  color: #72727d;
  height: 36rpx;
  line-height: 36rpx;
  width: 40rpx;
  text-align: left;
}

.parallelogram-text-idle {
  line-height: 36rpx;
  height: 36rpx;
  width: 80rpx;
  text-align: right;
}

/* 底部充电站卡片 */
.station-card {
  position: absolute;
  bottom: 14vh;
  left: 0;
  right: 0;
  background: #ffffff;
  border-radius: 20rpx 20rpx 0 0;
  padding: 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.station-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.station-name {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  flex: 1;
  line-height: 1.4;
}

.station-distance {
  display: flex;
  align-items: center;
  gap: 5rpx;
}

.distance-icon {
  width: 24rpx;
  height: 24rpx;
  margin-right: 5rpx;
}

.distance-text {
  font-size: 24rpx;
  color: #999;
}

.station-details {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 15rpx;
}

.rating,
.last-charge,
.special-offer,
.public-access {
  font-size: 22rpx;
  color: #666;
}

.charger-availability {
  display: flex;
  gap: 25rpx;
  margin-bottom: 15rpx;
}

.charger-type {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.charger-icon {
  width: 24rpx;
  height: 24rpx;
  margin-right: 5rpx;
}

.charger-label {
  font-size: 22rpx;
  color: #333;
}

.charger-status {
  font-size: 22rpx;
  color: #666;
}

.station-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 32rpx;
  color: #ff4757;
  font-weight: bold;
}

.navigate-btn {
  color: white;
  border: none;
  font-size: 26rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.navigate-btn image {
  width: 102rpx;
}
</style>
