<template>
  <block>
    <view
      class="home-sticky-search-location"
      :style="'top:' + (homeNavHeight || '38px')"
      :key="'sticky-' + forceUpdateKey"
    >
      <view class="home-sticky-wrapper">
        <view
          class="search-item"
          @click="openPopupHandle(1)"
          :class="activeType === 1 || !!sortValue ? 'search-item-active' : ''"
        >
          <text>{{ sortValue ? sortLabel : "综合排序" }}</text>
          <image v-if="activeType === 1" src="/static/images/index/down-primary-fill.png" mode="heightFix"></image>
          <image v-else src="/static/images/index/down-gray-fill.png" mode="heightFix"></image>
        </view>
        <view
          class="search-item"
          @click="openPopupHandle(2)"
          :class="activeType === 2 || !!speedValue ? 'search-item-active' : ''"
        >
          <text>{{ speedValue ? speedLabel : "速度" }}</text>
          <image v-if="activeType === 2" src="/static/images/index/down-primary-fill.png" mode="heightFix"></image>
          <image v-else src="/static/images/index/down-gray-fill.png" mode="heightFix"></image>
        </view>
        <view
          class="search-item"
          @click="openPopupHandle(3)"
          :class="activeType === 3 || serviceValue.length ? 'search-item-active' : ''"
        >
          <text>{{ serviceValue.length ? "服务" : "服务" }}</text>
          <image v-if="activeType === 3" src="/static/images/index/down-primary-fill.png" mode="heightFix"></image>
          <image v-else src="/static/images/index/down-gray-fill.png" mode="heightFix"></image>
        </view>
        <view class="search-last" @click="openPopupHandle(4)" :class="activeType === 4 ? 'search-last-active' : ''">
          <text>筛选</text>
          <image v-if="activeType === 4" src="/static/images/index/choose-primary.png" mode="heightFix"></image>
          <image v-else src="/static/images/index/choose-gray.png" mode="heightFix"></image>
        </view>
      </view>

      <view class="home-location-wrapper" v-show="showAuthLocation && !showOverlay">
        <view class="border">
          <view class="home-location-item"> 当前<text>定位权限未开启</text>，为您默认定位到济南市 </view>
          <view class="location-btn" @click="getSetting"
            >去授权
            <u-icon name="arrow-right" color="#fff" size="12"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- <u-overlay :show="showOverlay" :duration="480" :z-index="98" :opacity="0.3" @click="close" @touchmove.stop.prevent="moveHandle"> -->
    <view v-if="showOverlay" class="overlay-out-view" @click="close" @touchmove.stop.prevent="moveHandle">
      <view class="overlay-content-wrapper" @tap.stop="">
        <!-- 推荐排序 -->
        <view class="flex-row flex-jst-start flex-ali-center flex-wrap" v-if="activeType === 1">
          <view
            v-for="item in sortList"
            :key="item.value"
            :class="item.value == sortValue ? 'active-item' : 'item'"
            @tap.stop="selectItem(item, 1)"
          >
            {{ item.label }}
          </view>
        </view>
        <!-- 速度 -->
        <view class="flex-row flex-jst-start flex-ali-center flex-wrap" v-if="activeType === 2">
          <view
            v-for="item in speedList"
            :key="item.value"
            :class="item.value == speedValue ? 'active-item' : 'item'"
            @tap.stop="selectItem(item, 2)"
          >
            {{ item.label }}
          </view>
        </view>
        <!-- 服务-->
        <view class="flex-column" v-if="activeType === 3">
          <view class="flex-row flex-jst-start flex-ali-center flex-wrap">
            <view
              v-for="item in serviceList"
              :key="item.value"
              :class="serviceValue.includes(item.value) ? 'active-item' : 'item'"
              @tap.stop="selectItem(item, 3)"
            >
              {{ item.label }}
            </view>
          </view>
          <view class="btns flex-row flex-jst-btw flex-ali-center">
            <!-- <view class="cancel-btn" @tap.stop="resetAll">重置</view> -->
            <view class="confirm-btn" style="height: 70rpx; width: 290rpx" @tap.stop="confirmData">搜索</view>
          </view>
        </view>
        <!-- 筛选 -->
        <view class="flex-column" v-if="activeType === 4">
          <view class="sx-title">充电速度</view>
          <view class="flex-row flex-jst-start flex-ali-center flex-wrap">
            <view
              v-for="item in speedList"
              :key="item.value"
              :class="item.value == speedValue ? 'active-item' : 'item'"
              @tap.stop="selectItem(item, 2)"
            >
              {{ item.label }}
            </view>
          </view>
          <view class="sx-title">场站服务</view>
          <view class="flex-row flex-jst-start flex-ali-center flex-wrap">
            <view
              v-for="item in serviceList"
              :key="item.value"
              :class="serviceValue.includes(item.value) ? 'active-item' : 'item'"
              @tap.stop="selectItem(item, 3)"
            >
              {{ item.label }}
            </view>
          </view>
          <view class="sx-title">场站设施</view>
          <view class="flex-row flex-jst-start flex-ali-center flex-wrap">
            <view
              v-for="item in installationList"
              :key="item.value"
              :class="installationValue.includes(item.value) ? 'active-item' : 'item'"
              @tap.stop="selectItem(item, 4)"
            >
              {{ item.label }}
            </view>
          </view>
          <view class="sx-title">停车费</view>
          <view class="flex-row flex-jst-start flex-ali-center flex-wrap">
            <view
              v-for="item in parkList"
              :key="item.value"
              :class="parkValue.includes(item.value) ? 'active-item' : 'item'"
              @tap.stop="selectItem(item, 5)"
            >
              {{ item.label }}
            </view>
          </view>

          <view class="btns flex-row flex-jst-btw flex-ali-center">
            <view class="cancel-btn" @tap.stop="resetAll">重置</view>
            <view class="confirm-btn" @tap.stop="confirmData">完成</view>
          </view>
        </view>
      </view>
    </view>
    <!-- </u-overlay> -->
  </block>
</template>

<script>
import { getDictDataByType } from "@/config/api.js"
export default {
  components: {},
  // props: {
  //   showAuthLocation: {
  //     type: Boolean,
  //     default: false,
  //   },
  // },
  data() {
    return {
      homeNavHeight: "38px",
      activeType: 0,
      showOverlay: false,
      sortList: [
        { label: "综合排序", value: "AI" },
        { label: "距离最近", value: "DISTANCE" },
        { label: "价格最低", value: "PRICE" },
      ],
      speedList: [
        { label: "超充", value: "SUPERCHARGE" },
        { label: "快充", value: "FAST_CHARGE" },
        { label: "慢充", value: "SLOW_CHARGE" },
      ],
      serviceList: [
        { label: "卫生间", value: "1" },
        { label: "休息室", value: "2" },
        { label: "便利店", value: "3" },
      ],
      installationList: [
        { label: "地上", value: "1" },
        { label: "地下", value: "2" },
        { label: "洗车", value: "3" },
      ],
      parkList: [
        { label: "停车免费", value: "1" },
        { label: "限时收费", value: "2" },
        { label: "停车收费", value: "3" },
      ],
      sortValue: "",
      speedValue: "",
      serviceValue: [],
      installationValue: [],
      parkValue: [],
      // label
      sortLabel: "",
      speedLabel: "",
      serviceLabel: [],
      installationLabel: [],
      parkLabel: [],
      // scrollTop
      scrollTop: uni.getStorageSync("homeScrollTop"),
      showAuthLocation: !uni.getStorageSync("hasAuthLocation"),
      // 用于强制重新渲染以修复 sticky
      forceUpdateKey: 0,
    }
  },

  mounted() {
    // #ifdef MP-WEIXIN
    // 使用 $nextTick 确保父组件的 navHeight 已经设置
    this.$nextTick(() => {
      const navHeight = uni.getStorageSync("homeNavHeight")
      if (navHeight) {
        // 确保有 px 单位
        this.homeNavHeight = typeof navHeight === "number" ? navHeight + "px" : navHeight
      } else {
        // 如果还没有设置，延迟获取
        setTimeout(() => {
          const navHeight = uni.getStorageSync("homeNavHeight")
          if (navHeight) {
            this.homeNavHeight = typeof navHeight === "number" ? navHeight + "px" : navHeight
          }
        }, 100)
      }
    })
    // #endif

    // #ifdef MP-ALIPAY
    this.homeNavHeight = "38px"
    // #endif
    this.getDict()
  },

  onShow() {
    // 页面显示时检查并更新导航栏高度
    // #ifdef MP-WEIXIN
    const navHeight = uni.getStorageSync("homeNavHeight")
    if (navHeight) {
      this.homeNavHeight = typeof navHeight === "number" ? navHeight + "px" : navHeight
    }
    // #endif
  },

  watch: {
    scrollTop(oldVal, newVal) {
      if (this.showOverlay) {
        if (newVal < 100) {
          console.log('uni.getStorageSync("homeScrollTop") < 100')
          uni.pageScrollTo({
            scrollTop: 210,
            duration: 300,
          })
        }
      }
    },
  },

  methods: {
    updateNavHeight(height) {
      // 确保有 px 单位
      this.homeNavHeight = typeof height === "number" ? height + "px" : height
    },
    changeShowAuth(show) {
      this.showAuthLocation = show
    },

    getSetting() {
      let that = this
      uni.getSetting({
        success: function (res) {
          // #ifdef MP-ALIPAY
          that.getCurrentLocation()
          // #endif

          // #ifdef MP-WEIXIN
          if (!res.authSetting["scope.userLocation"]) {
            uni.authorize({
              scope: "scope.userLocation",
              success() {
                that.getCurrentLocation()
              },
              fail() {
                console.log("用户拒绝授权获取位置")
                // 用户拒绝授权获取位置
                uni.showModal({
                  title: "提示",
                  content: "需要获取您的位置信息，请确认授权，否则将无法使用部分功能",
                  success: (modalRes) => {
                    if (modalRes.confirm) {
                      uni.openSetting() // 引导用户去设置中开启位置权限
                    }
                  },
                })
              },
              complete: function (c) {
                console.log("complete", c)
              },
            })
          } else {
            that.getCurrentLocation()
          }
          // #endif
        },
      })
    },
    getCurrentLocation() {
      this.$emit("getCurrentLocation")
    },
    getDict() {
      if (!uni.getStorageSync("token")) {
        return
      }
      // sort
      getDictDataByType({
        type: "station_sort_type",
      }).then((res) => {
        this.sortList = res.data.map((item) => {
          return {
            label: item.dictLabel,
            value: item.dictValue,
          }
        })
      })
      // 速度
      getDictDataByType({
        type: "charging_speed",
      }).then((res) => {
        this.speedList = res.data.map((item) => {
          return {
            label: item.dictLabel,
            value: item.dictValue,
          }
        })
      })
      // 服务
      getDictDataByType({
        type: "station_services",
      }).then((res) => {
        this.serviceList = res.data.map((item) => {
          return {
            label: item.dictLabel,
            value: item.dictValue,
          }
        })
      })
      // 设施
      getDictDataByType({
        type: "station_facilities",
      }).then((res) => {
        this.installationList = res.data.map((item) => {
          return {
            label: item.dictLabel,
            value: item.dictValue,
          }
        })
      })
      // 停车费
      getDictDataByType({
        type: "parking_fees",
      }).then((res) => {
        this.parkList = res.data.map((item) => {
          return {
            label: item.dictLabel,
            value: item.dictValue,
          }
        })
      })
    },
    moveHandle() {
      console.log("moveHandle")
    },
    openPopupHandle(type) {
      if (this.showOverlay && this.activeType == type) {
        this.close()
      } else {
        if (uni.getStorageSync("homeScrollTop") < 100) {
          uni.pageScrollTo({
            scrollTop: 210,
            duration: 300,
          })
        }
        this.activeType = type
        this.showOverlay = true
      }
    },
    close() {
      this.activeType = 0
      this.showOverlay = false
    },
    resetAll() {
      this.sortValue = ""
      this.speedValue = ""
      this.serviceValue = []
      this.installationValue = []
      this.parkValue = []
      this.sortLabel = ""
      this.speedLabel = ""
      this.serviceLabel = []
      this.installationLabel = []
      this.parkLabel = []
      this.confirmData()
    },
    confirmData() {
      let searchParams = {
        speedLabel: this.speedLabel,
        speedValue: this.speedValue,
        sortLabel: this.sortLabel,
        sortValue: this.sortValue,
        serviceValue: this.serviceValue,
        serviceLabel: this.serviceLabel,
        parkValue: this.parkValue,
        parkLabel: this.parkLabel,
        installationValue: this.installationValue,
        installationLabel: this.installationLabel,
      }
      this.$emit("onComplete", searchParams)
      this.close()
    },
    selectItem(item, e) {
      if (e === 1) {
        if (this.sortValue == item.value) {
          this.sortValue = ""
          this.sortLabel = ""
        } else {
          this.sortValue = item.value
          this.sortLabel = item.label
        }
      } else if (e === 2) {
        if (this.speedValue == item.value) {
          this.speedValue = ""
          this.speedLabel = ""
        } else {
          this.speedValue = item.value
          this.speedLabel = item.label
        }
      } else if (e === 3) {
        // 多选-服务
        if (this.serviceValue.includes(item.value)) {
          let index = this.serviceValue.indexOf(item.value)
          this.serviceValue.splice(index, 1)
          this.serviceLabel.splice(index, 1)
        } else {
          this.serviceValue.push(item.value)
          this.serviceLabel.push(item.label)
        }
      } else if (e === 4) {
        // 多选-设施
        if (this.installationValue.includes(item.value)) {
          let index = this.installationValue.indexOf(item.value)
          this.installationValue.splice(index, 1)
          this.installationLabel.splice(index, 1)
        } else {
          this.installationValue.push(item.value)
          this.installationLabel.push(item.label)
        }
      } else if (e === 5) {
        // 多选- 停车费
        if (this.parkValue.includes(item.value)) {
          let index = this.parkValue.indexOf(item.value)
          this.parkValue.splice(index, 1)
          this.parkLabel.splice(index, 1)
        } else {
          this.parkValue.push(item.value)
          this.parkLabel.push(item.label)
        }
      }

      if (this.activeType == 1 || this.activeType == 2) {
        this.confirmData()
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.home-sticky-search-location {
  background: #fff;
  /* #ifdef MP-ALIPAY */
  top: 38px;
  /* #endif */
  position: sticky;
  z-index: 99;
  /* 确保 sticky 生效 */
  will-change: transform;
  /* 确保在滚动容器内正确工作 */
  left: 0;
  right: 0;
  .home-location-wrapper {
    height: 80rpx;
    background: #f0f4f5;
    padding: 8rpx 32rpx;
    font-size: 24rpx;
    .border {
      height: 64rpx;
      border: 1rpx solid $primary-color;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-radius: 12rpx;
      padding: 0 12rpx;
      background: #ffeede;
    }
    .location-btn {
      width: 126rpx;
      height: 44rpx;
      font-size: 24rpx;
      border-radius: 22rpx;
      background: #333;
      color: #fff;
      display: flex;
      justify-content: center;
      align-items: center;
      letter-spacing: 1rpx;
      font-weight: 400;
    }
    text {
      color: #ff4d00;
    }
  }
}
.home-sticky-wrapper {
  padding: 24rpx 32rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  // 加个下阴影
  box-shadow: 0rpx 3rpx 9rpx 0rpx rgba(0, 0, 0, 0.12);
  .search-item,
  .search-item-active {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    color: #333;
    font-size: 30rpx;
    font-weight: 500;
    width: 25%;
    image {
      width: 14rpx;
      height: 8rpx;
      margin-left: 8rpx;
    }
  }
  .search-item-active {
    color: #ff4d00;
  }
  .search-last,
  .search-last-active {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    color: #333;
    font-size: 30rpx;
    font-weight: 500;

    image {
      width: 13rpx;
      height: 13rpx;
      margin-left: 8rpx;
    }
  }
  .search-last-active {
    color: #ff4d00;
  }
}
.overlay-out-view {
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 98;
  // heigh: 100vh;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  transition-duration: 480ms;
  transition-timing-function: ease-out;
  // overflow: hidden;
}

.overlay-content-wrapper {
  background: #fff;
  padding: 32rpx;
  padding-bottom: 10rpx;
  /* #ifdef MP-WEIXIN */
  padding-top: 280rpx;
  /* #endif */

  /* #ifdef MP-ALIPAY */
  padding-top: 200rpx;
  /* #endif */
  border-radius: 0 0 30rpx 30rpx;

  .item,
  .active-item {
    width: 200rpx;
    height: 68rpx;
    background: #f3f3f3;
    border-radius: 50rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 400;
    font-size: 26rpx;
    color: #72727d;
    margin-right: 28rpx;
    margin-bottom: 40rpx;
    border: 2rpx solid #f3f3f3;
  }
  .active-item {
    width: 200rpx;
    height: 68rpx;
    background: #fff;
    border: 2rpx solid #000;
    color: #000;
  }

  .sx-title {
    font-weight: 400;
    font-size: 28rpx;
    color: #3d3d3d;
    line-height: 28rpx;
    text-align: left;
    margin-bottom: 32rpx;
  }

  .btns {
    margin-bottom: 30rpx;
    .cancel-btn {
      width: 296rpx;
      height: 84rpx;
      border-radius: 50rpx;
      border: 2rpx solid #999999;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .confirm-btn {
      width: 300rpx;
      height: 88rpx;
    }
  }
}
</style>
