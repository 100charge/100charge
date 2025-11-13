<template>
  <view class="content">
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
      <!-- 			:include-points="includePoints" -->
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
                :src="item.type == 'fast' ? '/static/images/index/icon-fast.png' : '/static/images/index/icon-slow.png'"
                mode="scaleToFill"
              ></cover-image>
              <!-- <cover-image
                class="speed-image"
                v-if="item.type == 'fast'"
                src="/static/images/index/icon-fast.png"
                mode="scaleToFill"
              ></cover-image>
              <cover-image
                class="speed-image"
                v-if="item.type == 'slow'"
                src="/static/images/index/icon-slow.png"
                mode="scaleToFill"
              ></cover-image> -->
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
              selectedMarkId == item.stationId
                ? 'https://kc-ch-files.kaichongcharge.com/charging/wx/icon-down-ff6b01.png'
                : 'https://kc-ch-files.kaichongcharge.com/charging/wx/icon-down-fff.png'
            "
            mode="scaleToFill"
          ></cover-image>
          <!-- <cover-image
            class="callout-triangle-active-image"
            v-if="selectedMarkId == item.stationId"
            src="/static/images/index/icon-down-ff6b01.png"
            mode="scaleToFill"
          ></cover-image> -->
          <!-- <cover-image
            class="callout-triangle-image"
            v-if="selectedMarkId != item.stationId"
            src="/static/images/index/icon-down-fff.png"
            mode="scaleToFill"
          ></cover-image> -->
          <!-- <cover-view :class="selectedMarkId == item.id ? 'callout-triangle-active' : 'callout-triangle'"> </cover-view> -->
        </cover-view>
      </cover-view>
      <!-- #endif -->
    </map>

    <view class="station-card-cover" v-if="selectedMark && selectedMark.stationId">
      <home-station-card :stationItem="selectedMark" />
    </view>
  </view>
</template>

<script>
import app from "@/static/js/app.js"
import HomeStationCard from "../home-station-card/home-station-card.vue"
import HomeStickySearch from "../home-sticky-search/home-sticky-search.vue"
import { listMapStation } from "@/config/api.js"
export default {
  components: {
    HomeStationCard,
    HomeStickySearch,
  },
  props: {
    stationList: {
      default: () => [],
      type: Array,
    },
  },
  data() {
    return {
      toDecimal2: app.toDecimal2,
      mapScale: 13, // 缩放级别，取值范围为3-20
      latitude: uni.getStorageSync("latitude") || 36.6667,
      longitude: uni.getStorageSync("longitude") || 116.9949,
      _mapContext: null,
      markers: [],
      includePoints: [],
      selectedMark: {},
      selectedMarkId: "",
      stickySearchParams: {},
      mapLocation: {
        latitude: "",
        longitude: "",
      },
      // stationList: [],
      noFatch: false,
    }
  },

  onReady() {
    // this.getCurrentLocation()
    this.latitude = uni.getStorageSync("latitude") || 36.6667
    this.longitude = uni.getStorageSync("longitude") || 116.9949
    this._mapContext = uni.createMapContext("map", this)
    this.mapScale = 13.01

    // 仅调用初始化，才会触发 on.("markerClusterCreate", (e) => {})

    // #ifdef MP-WEIXIN
    this._mapContext.initMarkerCluster({
      enableDefaultStyle: false,
      zoomOnClick: true,
      gridSize: 60,
      complete(res) {
        console.log("initMarkerCluster", res)
      },
    })
    // this._mapContext.on("markerClusterCreate", (e) => {
    //   console.log("markerClusterCreate", e)
    // })
    // #endif
    this._mapContext.getCenterLocation({
      success: function (res) {
        console.log("getCenterLocation=" + JSON.stringify(res))
      },
    })
    this.moveToLocation()
    this.addMarkers()
  },
  watch: {
    stationList: {
      handler(val, old) {
        this.$nextTick(() => {
          this.mapScale = 13.01
          console.log("watch-addMarkers")
          this.addMarkers()
        })
      },
      immediate: false,
      deep: true,
    },
  },
  methods: {
    getCurrentLocation() {
      console.log("getCurrentLocation：")
      let that = this
      uni.getLocation({
        // #ifdef MP-WEIXIN
        type: "gcj02",
        // #endif
        // type: "wgs84", // wgs84
        success: function (res) {
          that.latitude = res.latitude
          that.longitude = res.longitude
          console.log("当前位置的经度：" + res.longitude)
          console.log("当前位置的纬度：" + res.latitude)
          that.moveToLocation()
        },
        fail(e) {
          console.log("当前位置fail：" + JSON.stringify(e))
          uni.setStorageSync("latitude", 36.6667)
          uni.setStorageSync("longitude", 116.9949)
          that.latitude = 36.6667
          that.longitude = 116.9949
          uni.setStorageSync("city", "济南市")
          uni.showToast({
            title: "定位失败，已为您默认定位到 济南市",
            icon: "none",
          })
        },
      })
    },
    // 把中心坐标移动到地图中心
    moveToLocation() {
      console.log("把中心坐标移动到地图中心")
      let that = this
      this._mapContext.moveToLocation({
        // longitude: that.longitude || 116.9949,
        // latitude: that.latitude || 36.6667,
        longitude: uni.getStorageSync("longitude"),
        latitude: uni.getStorageSync("latitude"),
        success: (res) => {
          console.log(res)
        },
      })
    },
    addMarkers() {
      this.noFatch = false
      console.log("addMarkers-addMarkers")
      // #ifdef MP-WEIXIN
      let markers = []
      this.stationList.forEach((p, i) => {
        markers.push({
          id: i + 1,
          markerId: i + 1,
          iconPath: "/static/images/index/icon-dingwei.png",
          width: 1,
          height: 1,
          // bgColor: "transparent",
          joinCluster: true, // 指定了该参数才会参与聚合
          callout: {
            id: i + 1,
            width: "160rpx",
            height: "125rpx",
            padding: 3,
            content: `label ${i + 1}`,
            color: "#333",
            fontSize: 15,
            borderRadius: 14,
            display: "ALWAYS",
            bgColor: "#ff6b01",
          },
          customCallout: {
            display: "ALWAYS", //'BYCLICK':点击显示; 'ALWAYS':常显
            anchorX: 0,
            anchorY: 10,
          },
          latitude: p.lat,
          longitude: p.lng,
          stationId: p.id,
          ...p,
          id: i + 1,
          type: p.fastCharging > 0 ? "fast" : "slow",
        })
      })

      this.markers = markers
      if (this.markers && this.markers.length && this.selectedMarkId == "") {
        this.selectedMark = this.markers[0]
        this.selectedMarkId = this.markers[0].stationId
      }
      // let that = this
      // this._mapContext.addMarkers({
      //   markers: that.markers,
      //   clear: false,
      //   complete(res) {
      //     console.log("addMarkers", res)
      //   },
      // })
      // #endif

      // #ifdef MP-ALIPAY
      let customCalloutMarker = []
      this.stationList.forEach((p, i) => {
        let countLabel = ""
        if (p.fastCharging > 0) {
          countLabel = "闲 " + p.idleFastChargeCount + "/" + p.fastCharging
        } else {
          countLabel = "闲 " + p.idleSlowChargeCount + "/" + p.slowCharging
        }
        customCalloutMarker.push({
          ...p,
          id: i + 1,
          stationId: p.id,
          clusterId: i + 1,
          latitude: p.lat,
          longitude: p.lng,
          alpha: 1, //透明度 0-1
          width: 20,
          height: 20,
          iconPath: "/static/images/index/icon-dingwei.png",
          iconLayout: {
            params: {
              borderColor: this.selectedMarkId == p.stationId ? "#ff6b01" : "#fff",
              price: p.fee || "0.00",
              count: countLabel,
              width: "46",
              height: "46",
              iconPath:
                p.fastCharging > 0 ? "/static/images/index/icon-fast.png" : "/static/images/index/icon-slow.png",
              downImg:
                this.selectedMarkId == p.stationId
                  ? "/static/images/index/icon-down-ff6b01.png"
                  : "/static/images/index/icon-down-fff.png",
              type: p.fastCharging > 0 ? "fast" : "slow",
              idelBgColor: p.fastCharging > 0 ? "#FFF1E6" : "#EBF4FF",
            },
            src: "/layout/customcallout.xml",
          },
          // 自定义窗口
          customCallout: {
            canShowOnTap: true,
            layout: {
              params: {
                borderColor: this.selectedMarkId == p.stationId ? "#ff6b01" : "#fff",
                price: p.fee || "0.00",
                count: countLabel,
                width: "46",
                height: "46",
                iconPath:
                  p.fastCharging > 0 ? "/static/images/index/icon-fast.png" : "/static/images/index/icon-slow.png",
                downImg:
                  this.selectedMarkId == p.stationId
                    ? "/static/images/index/icon-down-ff6b01.png"
                    : "/static/images/index/icon-down-fff.png",
                type: p.fastCharging > 0 ? "fast" : "slow",
                idelBgColor: p.fastCharging > 0 ? "#FFF1E6" : "#EBF4FF",
              },
              src: "/layout/customcallout.xml",
            },
          },
          canShowOnTap: true,
          layoutBubble: {
            style: "bubble", // none隐藏背景 bubble默认
            downImg: this.selectedMarkId == p.stationId ? "#ff6b01" : "#fff",
            bgColor: "#fff",
            borderRadius: 7,
          },
        })
      })
      this.markers = customCalloutMarker
      if (this.markers && this.markers.length && this.selectedMarkId == "") {
        this.selectedMark = this.markers[0]
        this.selectedMarkId = this.markers[0].stationId
      }
      // #endif

      console.log("this.markers", this.markers)
      this.includePoints = this.stationList.map((item) => {
        return {
          longitude: item.lng,
          latitude: item.lat,
        }
      })
      let dist = this.markers && this.markers.length > 0 ? this.markers[0].distanceNumber : 0
      if (dist < 800) {
        this.mapScale = 16.01
      } else {
        this.mapScale = 13.01
      }
      // this._mapContext.includePoints({
      //     // points: this.includePoints,
      //     padding: [ 170, 170, 170, 170 ]
      // });

      // this._mapContext.getScale({
      //   success(res) {
      //     console.log("getScale", res)
      //   },
      // })
    },
    markerTap(e) {
      console.log("markerTap", e)
      // #ifdef MP-ALIPAY
      let markId = e.markerId
      let station = this.markers.filter((item) => {
        return item.id == markId
      })
      if (station && station.length !== 0) {
        this.selectedMark = station[0]
        this.selectedMarkId = station[0].stationId
      }
      this.addMarkers()
      // #endif
    },
    calloutTap(e) {
      console.log("calloutTap", e)
      let markId = e.markerId
      let station = this.markers.filter((item) => {
        return item.id == markId
      })
      if (station && station.length !== 0) {
        this.selectedMark = station[0]
        this.selectedMarkId = station[0].stationId
      }
    },
    // 地图滑动事件
    regionChange(e) {
      console.log("regionchange_type=", e)
      if (e.type == "end") {
        // #ifdef MP-WEIXIN
        this.mapLocation.latitude = e.target.centerLocation.latitude || 36.6667
        this.mapLocation.longitude = e.target.centerLocation.longitude || 116.9949
        // #endif

        // #ifdef MP-ALIPAY
        this.mapLocation.latitude = e.latitude || 36.6667
        this.mapLocation.longitude = e.longitude || 116.9949
        // #endif

        // this.latitude = e.target.centerLocation.latitude || 36.6667
        // this.longitude = e.target.centerLocation.longitude || 116.9949
        let centerMark = [
          {
            id: 0,
            markerId: 0,
            iconPath: "/static/images/index/icon-dingwei.png",
            width: 22,
            height: 22,
            // bgColor: "transparent",
            joinCluster: true, // 指定了该参数才会参与聚合
            callout: {
              id: 0,
              fontSize: 15,
              borderRadius: 14,
              display: "ALWAYS",
            },
            latitude: this.mapLocation.latitude,
            longitude: this.mapLocation.longitude,
          },
        ]
        // this._mapContext.addMarkers({
        //   markers: centerMark,
        //   clear: true,
        //   complete(res) {
        //     console.log("addMarkers", res)
        //   },
        // })
        // this.changeCenterLocation(e.target.centerLocation)
        if (!this.noFatch) {
          this.noFatch = true
          this.changeCenterLocation(this.mapLocation)
        }
      }
    },

    changeCenterLocation(e) {
      this.$emit("onChangeCenter", e)
    },
    toJSON() {},
  },
}
</script>

<style lang="scss" scoped>
.page-view {
  min-height: 100%;
}
cover-view {
  box-sizing: border-box;
}

.station-card-wx {
  // width: 88%;
  background: #ffffff;
  border-radius: 20rpx;
  padding: 29rpx 22rpx 0 22rpx;
  margin: 0 auto;
  margin-top: 22rpx;
  .station-name {
    color: #333;
    font-size: 28rpx;
    line-height: 38rpx;
    flex: 1;
    cover-image {
      width: 28rpx;
      height: 28rpx;
      margin-right: 12rpx;
    }
  }
  .distance {
    width: 143rpx;
    height: 50rpx;
    border-radius: 100rpx;
    border: 2rpx solid rgba(0, 0, 0, 0.08);
    font-weight: 400;
    font-size: 26rpx;
    color: #72727d;
    margin-left: 32rpx;
    cover-image {
      height: 24rpx;
      width: 24rpx;
      margin-right: 8rpx;
    }
  }

  .score {
    color: #72727d;
    font-weight: 400;
    font-size: 20rpx;
    margin-top: 18rpx;
    .score-box {
      width: 118rpx;
      height: 33rpx;
      background-color: rgba(255, 233, 217, 0.5);
      background-image: linear-gradient(90deg, #ffe9d9 0%, rgba(255, 249, 224, 0.2) 100%);
      border-radius: 20rpx;
      margin-right: 10rpx;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      padding-left: 10rpx;
      .star-image {
        width: 14rpx;
        height: 14rpx;
        margin-right: 6rpx;
      }
    }
    .line-shu {
      height: 12rpx;
      width: 2rpx;
      background: #dddddd;
      margin: 0 8rpx;
      display: inline-block;
    }
  }

  .price {
    margin-top: 24rpx;
    .price-left {
      width: 300rpx;
      color: $primary-color;
      .unit {
        font-size: 20rpx;
        font-weight: 400;
      }
      .blod {
        font-size: 44rpx;
        font-weight: 700;
        margin-bottom: -6rpx;
      }
    }
    .price-right {
      display: flex;
      justify-content: flex-start;
      align-items: center;

      cover-image {
        width: 48rpx;
        height: 44rpx;
        margin-right: 6rpx;
      }

      .right-text {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        color: #333;
        font-size: 24rpx;

        .count-all {
          color: #72727d;
        }
      }
    }
  }

  .types {
    margin-top: 24rpx;
    .type-item {
      padding: 8rpx 15rpx;
      color: $primary-color;
      border: 2rpx solid #ffe9d9;
      font-size: 18rpx;
      font-weight: 400;
      border-radius: 50rpx;
      margin-right: 12rpx;
    }
  }

  .car-view {
    height: 73rpx;
    width: 100%;
    border-top: 1rpx solid #eeeeee;
    margin-top: 20rpx;
    font-weight: 400;
    font-size: 22rpx;
    color: #72727d;
    cover-image {
      width: 34rpx;
      height: 34rpx;
      margin-right: 12rpx;
    }
    .park-text {
      flex: 1;
    }
  }
}
.content {
  flex: 1;
  width: 100%;
  height: calc(100vh - 77px);
  /* #ifdef MP-ALIPAY */
  padding-top: 80rpx;
  /* #endif */
}

.map {
  width: 750rpx;
  height: calc(100vh - 77px - 44px);
  overflow: hidden;
}
.custom-callout-item {
  width: 160rpx;
  height: 125rpx;
  .callout-box-active {
    width: 160rpx;
    height: 100rpx;
    background: #ffffff;
    box-shadow: 0rpx 3rpx 9rpx 0rpx rgba(0, 0, 0, 0.1216);
    border-radius: 14rpx;
    border: 4rpx solid $primary-color;
  }
  .callout-box {
    width: 160rpx;
    height: 100rpx;
    background: #ffffff;
    box-shadow: 0rpx 3rpx 9rpx 0rpx rgba(0, 0, 0, 0.1216);
    border-radius: 14rpx;
    border: 2rpx solid #fff;
  }

  .callout-triangle-active-image,
  .callout-triangle-image {
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
    .unit {
      font-weight: 400;
      font-size: 22rpx;
      margin-right: 4rpx;
      width: 20rpx;
    }
    .price-text {
      width: 140rpx;
    }
  }
  .active-price {
    color: $primary-color;
  }
  .count {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 0 8rpx;

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
    }
    .parallelogram-slow {
      background: rgba(118, 177, 255, 0.15);
    }
  }
}

.station-card-cover {
  position: fixed;
  bottom: 190rpx;
  left: 0;
  right: 0;
  width: 100%;
  padding: 0 24rpx;
  z-index: 97;
  background: transparent;
  box-sizing: border-box;
}
</style>
