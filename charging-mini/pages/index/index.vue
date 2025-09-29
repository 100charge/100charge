<template>
  <view class="container">
    <!-- 自定义导航栏 -->
    <u-navbar :is-back="false" title="" :background="{ backgroundColor: '#fff' }" :z-index="999">
      <view class="nav-custom-view">
        <view class="switch-display-type" @click="onSwitchPage">
          <view class="switch-display-item" v-if="pageDisplayType == 'map'">
            <text class="icon-text" style="color: #333333">☰</text>
            <text style="color: #333333">列表</text>
          </view>
          <view class="switch-display-item" v-if="pageDisplayType == 'list'">
            <image src="/static/img/map.png" mode="heightFix"></image>
            <text style="color: #333333">地图</text>
          </view>
        </view>
        <view class="header-search-view">
          <view class="linear-bg">
            <view class="white-bg-view">
              <view class="switch-city-pick-view">
                <picker @change="bindCityChange" :value="cityIndex" :range="cityNameArr">
                  <view class="uni-input" style="margin-right: 6rpx">
                    {{ currentCity }}
                  </view>
                </picker>
                <u-icon name="arrow-down-fill" color="#A8A9AF" size="10"></u-icon>
              </view>

              <view class="line-shu"></view>

              <u-search
                placeholder="搜索场站"
                :clearabled="true"
                bgColor="transparent"
                :showAction="false"
                searchIconColor="#72727D"
                :searchIconSize="20"
                placeholderColor="#999"
                :inputStyle="{ fontSize: '28rpx', fontWeight: 400, color: '#333' }"
                v-model="keyword"
                @input="inputKeyword"
              ></u-search>
            </view>
          </view>
        </view>
      </view>
    </u-navbar>

    <!-- 地图视图 - 全屏显示 -->
    <view class="map-view-fullscreen" v-if="pageDisplayType === 'map'">
      <MapView :stationList="stations" @onChangeCenter="onChangeMapCenter" />
    </view>

    <!-- 列表视图 - 带固定区域 -->
    <view v-if="pageDisplayType === 'list'">
      <!-- 固定区域 -->
      <view class="fixed-area">
        <!-- 促销横幅 -->
        <view class="promo-banner" @click="handleClickOutside">
          <image class="banner-image" src="../../static/img/sales-banner.png" mode="aspectFill"></image>
        </view>

        <!-- 筛选排序区域 -->
        <view class="filter-section">
          <view class="filter-row">
            <view class="filter-item" @click="handleClickOutside">
              <text class="filter-text">综合排序</text>
              <text class="filter-arrow">▼</text>
            </view>
            <view class="filter-item" @click="showDistanceFilter" :class="{ 'filter-item-active': showFilterModal }">
              <text class="filter-text">距离/区域</text>
              <text class="filter-arrow" :class="{ 'filter-arrow-active': showFilterModal }">{{
                showFilterModal ? "▲" : "▼"
              }}</text>
            </view>
            <view class="filter-item" @click="handleClickOutside">
              <text class="filter-text">充电站</text>
              <text class="filter-arrow">▼</text>
            </view>
            <view class="filter-item" @click="handleClickOutside">
              <text class="filter-text">筛选</text>
              <text class="filter-arrow">▼</text>
            </view>
          </view>
          <view class="filter-tags" @click="handleClickOutside">
            <text class="tag">限时特价</text>
            <text class="tag">免费停车</text>
            <text class="tag">快充</text>
            <text class="tag">慢充</text>
            <text class="tag">地下</text>
            <text class="tag">可用充</text>
          </view>
        </view>
      </view>

      <!-- 可滚动的充电站列表 -->
      <scroll-view
        class="station-list"
        scroll-y="true"
        :style="{ height: listHeight + 'rpx' }"
        @click="handleClickOutside"
      >
        <view
          class="station-item"
          v-for="(station, index) in stations"
          :key="index"
          @click.stop="goToStationDetail(station)"
        >
          <view class="station-header">
            <text class="station-name">{{ station.name }}</text>
            <view class="station-distance">
              <image class="distance-icon" src="/static/img/daohang.png" mode="aspectFit"></image>
              <text class="distance-text">{{ station.distance }}</text>
            </view>
          </view>
          <view class="station-details">
            <text class="rating">{{ station.rating }}分 |</text>
            <text class="last-charge">最近充电{{ station.lastCharge }}分钟前 |</text>
            <text class="special-offer">充电特惠 |</text>
            <text class="public-access">对外开放</text>
          </view>
          <view class="charger-availability">
            <view class="charger-type">
              <view class="charger-icon-wrapper fast">
                <image src="/static/img/kuai.png" class="badge-icon"></image>
              </view>
              <text class="charger-status"
                ><text class="charger-status-text">闲{{ station.fastAvailable }}</text> /{{ station.fastTotal }}</text
              >
            </view>
            <view class="line-shu"></view>
            <view class="charger-type">
              <view class="charger-icon-wrapper slow">
                <image src="/static/img/low.png" class="badge-icon"></image>
              </view>
              <text class="charger-status"
                ><text class="charger-status-blue">闲{{ station.slowAvailable }}</text> /{{ station.slowTotal }}</text
              >
            </view>
          </view>
          <view class="station-footer">
            <view class="price-wrapper">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ station.price }}</text>
              <text class="price-unit">/度</text>
            </view>
            <view class="navigate-btn" @click.stop="navigateToStation(station)">
              <image src="/static/img/daohang-logo.png" class="nav-icons" mode="heightFix"></image>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 筛选弹窗 -->
    <view
      class="filter-modal"
      v-if="showFilterModal"
      @click="hideFilterModal"
      @touchmove.stop.prevent="preventMove"
      :style="{ top: fixedAreaHeight + 'rpx' }"
    >
      <view class="filter-content" @click.stop @tap.stop :style="{ height: 'calc(90vh - ' + fixedAreaHeight + 'rpx)' }">
        <!-- 距离筛选 -->
        <view class="filter-section-modal">
          <text class="filter-title">距离我</text>
          <view class="distance-options">
            <view class="distance-row">
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '1km' }"
                @click="selectDistance('1km')"
              >
                <text>1km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '2km' }"
                @click="selectDistance('2km')"
              >
                <text>2km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '3km' }"
                @click="selectDistance('3km')"
              >
                <text>3km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '5km' }"
                @click="selectDistance('5km')"
              >
                <text>5km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '10km' }"
                @click="selectDistance('10km')"
              >
                <text>10km</text>
              </view>
            </view>
            <view class="distance-row">
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '20km' }"
                @click="selectDistance('20km')"
              >
                <text>20km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '30km' }"
                @click="selectDistance('30km')"
              >
                <text>30km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '50km' }"
                @click="selectDistance('50km')"
              >
                <text>50km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '100km' }"
                @click="selectDistance('100km')"
              >
                <text>100km</text>
              </view>
              <view
                class="distance-item"
                :class="{ active: selectedDistance === '200km' }"
                @click="selectDistance('200km')"
              >
                <text>200km</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 行政区筛选 -->
        <view class="filter-section-modal-area">
          <text class="filter-title">行政区</text>
          <view class="area-options">
            <view class="area-item" :class="{ active: selectedAreas.includes('市中区') }" @click="toggleArea('市中区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('市中区') }"></view>
              <text>市中区</text>
            </view>
            <view class="area-item" :class="{ active: selectedAreas.includes('历下区') }" @click="toggleArea('历下区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('历下区') }"></view>
              <text>历下区</text>
            </view>
            <view class="area-item" :class="{ active: selectedAreas.includes('天桥区') }" @click="toggleArea('天桥区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('天桥区') }"></view>
              <text>天桥区</text>
            </view>
            <view class="area-item" :class="{ active: selectedAreas.includes('槐荫区') }" @click="toggleArea('槐荫区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('槐荫区') }"></view>
              <text>槐荫区</text>
            </view>
            <view class="area-item" :class="{ active: selectedAreas.includes('历城区') }" @click="toggleArea('历城区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('历城区') }"></view>
              <text>历城区</text>
            </view>
            <view class="area-item" :class="{ active: selectedAreas.includes('长清区') }" @click="toggleArea('长清区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('长清区') }"></view>
              <text>长清区</text>
            </view>
            <view class="area-item" :class="{ active: selectedAreas.includes('莱芜区') }" @click="toggleArea('莱芜区')">
              <view class="checkbox" :class="{ checked: selectedAreas.includes('莱芜区') }"></view>
              <text>莱芜区</text>
            </view>
          </view>
        </view>

        <!-- 底部按钮 -->
        <view class="filter-buttons">
          <button class="filter-btn clear-btn" @click="clearFilter">清空</button>
          <button class="filter-btn confirm-btn" @click="confirmFilter">确定</button>
        </view>
      </view>
    </view>

    <!-- 底部导航栏组件 -->
    <BottomTabBar :currentPage="'home'" v-if="!showFilterModal" />
  </view>
</template>

<script>
import MapView from "../../components/MapView/MapView.vue"
import BottomTabBar from "../../components/BottomTabBar/BottomTabBar.vue"

export default {
  components: {
    MapView,
    BottomTabBar,
  },
  data() {
    return {
      listHeight: 0,
      showFilterModal: false,
      selectedDistance: "",
      selectedAreas: [],
      fixedAreaHeight: 0, // 动态计算的固定区域高度
      pageDisplayType: "list", // 页面显示类型：list 或 map
      currentCity: "济南市", // 当前城市
      keyword: "", // 搜索关键词
      cityIndex: -1, // 城市选择索引
      cityNameArr: ["当前位置", "济南市", "青岛市", "烟台市", "威海市"], // 城市列表
      stations: [
        {
          id: 1,
          name: "济南金泽供热站充电站",
          distance: "1.1km",
          rating: "4.4",
          lastCharge: "7",
          fastAvailable: 2,
          fastTotal: 16,
          slowAvailable: 2,
          slowTotal: 2,
          price: "0.55",
          latitude: 36.566964,
          longitude: 118.382681,
        },
        {
          id: 2,
          name: "济南市100停车场充电站",
          distance: "1.3km",
          rating: "4.2",
          lastCharge: "15",
          fastAvailable: 1,
          fastTotal: 8,
          slowAvailable: 1,
          slowTotal: 4,
          price: "0.75",
          latitude: 36.470964,
          longitude: 117.390681,
        },
        {
          id: 3,
          name: "济南CBD充电站",
          distance: "1.3km",
          rating: "4.2",
          lastCharge: "15",
          fastAvailable: 1,
          fastTotal: 8,
          slowAvailable: 1,
          slowTotal: 4,
          price: "0.68",
          latitude: 36.562964,
          longitude: 117.375681,
        },
        {
          id: 4,
          name: "济南万象城充电站",
          distance: "1.5km",
          rating: "4.6",
          lastCharge: "3",
          fastAvailable: 3,
          fastTotal: 12,
          slowAvailable: 2,
          slowTotal: 6,
          price: "0.62",
          latitude: 36.675964,
          longitude: 117.388681,
        },
        {
          id: 5,
          name: "济南海信广场充电站",
          distance: "1.8km",
          rating: "4.3",
          lastCharge: "12",
          fastAvailable: 1,
          fastTotal: 10,
          slowAvailable: 3,
          slowTotal: 4,
          price: "0.58",
          latitude: 36.658964,
          longitude: 118.395681,
        },
        {
          id: 6,
          name: "济南大明湖中心充电站",
          distance: "2.1km",
          rating: "4.7",
          lastCharge: "5",
          fastAvailable: 4,
          fastTotal: 16,
          slowAvailable: 2,
          slowTotal: 8,
          price: "0.65",
          latitude: 36.680964,
          longitude: 117.378681,
        },
      ],
    }
  },
  onLoad() {
    // 隐藏原生tabBar，使用自定义底部导航
    uni.hideTabBar()

    // 使用 vk-uview 导航栏自动处理状态栏和胶囊按钮的适配
    // 导航栏会自动占位，无需手动计算高度

    // 计算列表高度
    uni.getSystemInfo({
      success: (res) => {
        const windowHeight = res.windowHeight
        // vk-uview 导航栏自动占位，简化列表高度计算
        const customBottomNavHeight = 160 // 自定义底部导航栏高度 (rpx)
        const bannerAndFilterHeight = 400 // 横幅和筛选区域高度 (rpx)
        const listHeight = windowHeight * 2 - bannerAndFilterHeight + customBottomNavHeight

        this.listHeight = listHeight
      },
    })

    // 延迟计算固定区域高度，确保DOM渲染完成
    this.$nextTick(() => {
      this.calculateFixedAreaHeight()
    })
  },
  methods: {
    // 动态计算到filter-row底部的高度
    calculateFixedAreaHeight() {
      const query = uni.createSelectorQuery().in(this)
      query
        .select(".filter-row")
        .boundingClientRect((data) => {
          if (data) {
            // data.bottom 是filter-row底部相对于视口的位置
            // 将px转换为rpx (1px = 2rpx)
            this.fixedAreaHeight = data.bottom * 2
            console.log("filter-row底部位置(rpx):", this.fixedAreaHeight)
            console.log("filter-row原始数据:", data)
          }
        })
        .exec()
    },
    // 搜索方法
    onSearch() {
      // 搜索逻辑
    },
    // 切换页面显示类型
    onSwitchPage() {
      this.pageDisplayType = this.pageDisplayType === "list" ? "map" : "list"
      console.log("切换到:", this.pageDisplayType)
    },
    // 搜索输入处理
    inputKeyword(e) {
      this.keyword = e.detail.value
      // 这里可以添加搜索逻辑
      console.log("搜索关键词:", this.keyword)
    },
    // 城市选择变化
    bindCityChange(e) {
      let index = e.detail.value
      if (index == 0) {
        this.cityIndex = -1
        this.currentCity = "当前位置"
        // 这里可以添加重新定位逻辑
        console.log("重新定位")
      } else {
        this.cityIndex = index
        this.currentCity = this.cityNameArr[index]
        console.log("选择城市:", this.currentCity)
      }
    },
    // 导航方法
    onNavigate(station) {
      // 导航逻辑
    },
    // 筛选方法
    onFilter(type) {
      // 筛选逻辑
    },
    // 导航到充电站
    navigateToStation(station) {
      uni.navigateTo({
        url: `/pages/charging/charging?code=${station.name}`,
      })
    },

    // 筛选相关方法
    showDistanceFilter() {
      this.showFilterModal = true
    },
    hideFilterModal() {
      this.showFilterModal = false
    },
    // 处理点击外部区域关闭弹出框
    handleClickOutside() {
      if (this.showFilterModal) {
        this.hideFilterModal()
      }
    },
    selectDistance(distance) {
      this.selectedDistance = distance
    },
    toggleArea(area) {
      const index = this.selectedAreas.indexOf(area)
      if (index > -1) {
        this.selectedAreas.splice(index, 1)
      } else {
        this.selectedAreas.push(area)
      }
    },
    clearFilter() {
      this.selectedDistance = ""
      this.selectedAreas = []
    },
    confirmFilter() {
      // 应用筛选条件
      console.log("选择的距离:", this.selectedDistance)
      console.log("选择的区域:", this.selectedAreas)
      this.showFilterModal = false
      // 这里可以添加筛选逻辑
    },
    preventMove() {
      // 防止弹出框背景滚动
      return false
    },
    goToStationDetail(station) {
      uni.navigateTo({
        url: "/pages/station-detail/station-detail?stationData=" + encodeURIComponent(JSON.stringify(station)),
      })
    },
    // 地图中心位置变化处理
    onChangeMapCenter(e) {
      if (e.latitude && e.longitude) {
        // 地图中心位置变化时，可以重新获取该区域的充电站数据
        console.log("地图中心变化:", e.latitude, e.longitude)
        // 这里可以调用API获取新区域的充电站数据
        // this.getStationDataForMap(e.latitude, e.longitude)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
/* 隐藏页面滚动条 */
page {
  overflow: hidden;
  height: 100vh;
}

.container {
  background-color: #f5f5f5;
  height: 100vh;
  overflow: hidden;
  position: relative;
}

/* 固定区域 */
.fixed-area {
  background-color: #f5f5f5;
  position: relative;
  z-index: 50;
  overflow: hidden;
}

/* 导航栏自定义内容 */
.nav-custom-view {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  padding: 0 30rpx;
  box-sizing: border-box;

  .switch-display-type {
    width: 124rpx;

    .switch-display-item {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 28rpx;
      color: #ffffff;
      font-style: normal;
      text-transform: none;

      image {
        width: 40rpx;
        height: 40rpx;
        margin-right: 10rpx;
      }

      .icon-text {
        font-size: 32rpx;
        margin-right: 10rpx;
      }
    }
  }

  .header-search-view {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 60rpx;
    flex: 1;

    .linear-bg {
      width: 100%;
      padding: 2rpx;
      border-radius: 36rpx;

      .white-bg-view {
        text-align: center;
        font-weight: 400;
        height: 60rpx;
        background: #f4f5f7;
        border-radius: 36rpx;
        border: none;
        font-size: 28rpx;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        padding-left: 14rpx;

        .switch-city-pick-view {
          width: 160rpx;
          height: 60rpx;
          text-align: center;
          font-weight: 400;
          font-size: 28rpx;
          color: #333;
          display: flex;
          justify-content: flex-start;
          align-items: center;
        }

        .line-shu {
          background-color: rgba(0, 0, 0, 0.08);
          width: 2rpx;
          height: 27rpx;
        }
      }
    }
  }
}

/* 促销横幅 */
.promo-banner {
  margin: 15rpx 25rpx 0 24rpx;
  border-radius: 20rpx;
  overflow: hidden;
  position: relative;

  .banner-image {
    width: 100%;
    height: 280rpx;
    border-radius: 20rpx;
  }
}

/* 筛选排序区域 */
.filter-section {
  position: relative;
  margin: 15rpx 25rpx 0 24rpx;
  border-radius: 15rpx;
  padding: 20rpx;
  padding-left: 0;
  padding-right: 0;
  margin-top: 0;

  .filter-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;

    .filter-item {
      display: flex;
      align-items: center;
      gap: 10rpx;

      &.filter-item-active {
        .filter-text {
          color: #ff6b35 !important;
        }

        .filter-arrow {
          color: #ff6b35 !important;
        }
      }

      .filter-text {
        font-size: 28rpx;
        font-weight: 500;
        color: #000;
      }

      .filter-arrow {
        font-size: 16rpx;
        color: #999;
        transition: all 0.3s ease;

        &.filter-arrow-active {
          color: #ff6b35 !important;
          transform: rotate(180deg);
        }
      }
    }
  }

  .filter-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 15rpx;
    margin-top: 15rpx;
    justify-content: space-between;

    .tag {
      background-color: rgba(240, 240, 240, 1);
      color: rgba(102, 102, 102, 1);
      padding: 8rpx 16rpx;
      border-radius: 6rpx;
      font-size: 24rpx;
      font-family: SourceHanSansSC-Regular;
      font-weight: normal;
      white-space: nowrap;

      &:first-child {
        background-color: #fff;
        color: #000;
        font-family: SourceHanSansSC-Medium;
        font-weight: 500;
      }
    }
  }
}

/* 充电站卡片优化 */
.station-item:first-child {
  margin-top: 0;
}

/* 自定义底部导航 */
.custom-bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 200;

  .bottom-nav-bg {
    width: 100%;
    display: block;
  }

  .nav-items {
    position: absolute;
    top: 20rpx;
    left: 0;
    right: 0;
    height: calc(100% - 20rpx);
    display: flex;
    align-items: flex-start;
    justify-content: space-around;
    padding: 40rpx 0 30rpx;
    box-sizing: border-box;

    .nav-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10rpx;

      .nav-icon {
        width: 48rpx;
        height: 48rpx;
      }

      /* 扫码图标特殊样式 */
      &:nth-child(2) {
        transform: translateY(-8rpx);

        .nav-icon {
          width: 72rpx;
          height: 72rpx;
          margin-top: -15rpx;
        }
      }

      .nav-text {
        font-size: 22rpx;
        color: #666;
      }

      &.active .nav-text {
        color: #ff6b35;
      }
    }
  }
}

/* 筛选弹窗样式 */
.filter-modal {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  transition-duration: 0.3s;
  transition-timing-function: ease-out;

  .filter-content {
    background-color: #fff;
    border-radius: 0 0 30rpx 30rpx;
    padding: 40rpx 30rpx 10rpx 30rpx;
    width: 100%;
    overflow-y: auto;
    margin-top: 0;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
    animation: slideDown 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    transition: all 0.3s ease-out;

    @keyframes slideDown {
      0% {
        transform: translateY(-100%);
        opacity: 0;
      }
      100% {
        transform: translateY(0);
        opacity: 1;
      }
    }

    .filter-section-modal {
      margin-bottom: 40rpx;

      .filter-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 30rpx;
        display: block;
      }

      .distance-options {
        margin-bottom: 20rpx;

        .distance-row {
          display: flex;
          justify-content: space-between;
          margin-bottom: 16rpx;

          .distance-item {
            flex: 1;
            height: 60rpx;
            background-color: #f5f5f5;
            border-radius: 10rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 10rpx;
            font-size: 28rpx;
            color: #1a1a1a;

            &.active {
              background-color: #ff6b35;
              color: white;
            }
          }
        }
      }
    }

    .filter-section-modal-area {
      margin-bottom: 40rpx;
      display: flex;
      flex-direction: row;
      gap: 20rpx;

      .filter-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #1a1a1a;
        width: 180rpx;
        padding-top: 20rpx;
        padding-left: 20rpx;

        display: block;
        background: #f9f9f9;
      }

      .area-options {
        display: flex;
        flex-direction: column;
        gap: 20rpx;

        .area-item {
          display: flex;
          align-items: center;
          padding: 10rpx 0;

          &:last-child {
            border-bottom: none;
          }

          .checkbox {
            width: 40rpx;
            height: 40rpx;
            border: 2rpx solid #ddd;
            border-radius: 6rpx;
            margin-right: 20rpx;
            display: flex;
            align-items: center;
            justify-content: center;

            &.checked {
              background-color: #ff6b35;
              border-color: #ff6b35;

              &::after {
                content: "✓";
                color: white;
                font-size: 24rpx;
              }
            }
          }

          text {
            font-size: 26rpx;
            color: #000000;
          }
        }
      }
    }

    .filter-buttons {
      display: flex;
      gap: 20rpx;
      margin-top: 40rpx;
      padding: 20rpx 0 40rpx 0;
      border-top: 1rpx solid #f0f0f0;
      position: sticky;
      bottom: 0;
      background-color: #fff;

      .filter-btn {
        flex: 1;
        height: 80rpx;
        border: none;
        border-radius: 10rpx;
        font-size: 30rpx;

        &.clear-btn {
          background: #f5f6f8;
          color: #000000;
          border-radius: 38rpx;
        }

        &.confirm-btn {
          background-color: #000;
          color: white;
          border-radius: 38rpx;
        }
      }
    }
  }
}

/* 全屏地图视图 */
.map-view-fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 10;
  background-color: #f5f5f5;
}

/* 原地图视图样式保留（如果需要） */
.map-view {
  background-color: #f5f5f5;
  position: relative;
}

/* 充电站列表 */
.station-list {
  padding: 0rpx 20rpx 160rpx;
  background-color: #f5f5f5;
  box-sizing: border-box;

  .station-item {
    background-color: rgba(255, 255, 255, 1);
    border-radius: 20rpx;
    padding: 25rpx 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

    .station-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15rpx;

      .station-name {
        font-size: 32rpx;
        color: rgba(26, 26, 26, 1);
        font-family: SourceHanSansSC-Medium;
        font-weight: 500;
        flex: 1;
        line-height: 47rpx;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .station-distance {
        display: flex;
        align-items: center;
        gap: 5rpx;
        background-color: #f9f9f9;
        border-radius: 24rpx;
        padding: 5rpx 10rpx;

        .distance-icon {
          width: 24rpx;
          height: 24rpx;
          margin-right: 5rpx;
        }

        .distance-text {
          font-size: 24rpx;
          color: rgb(0, 0, 0);
          font-weight: bold;
        }
      }
    }

    .station-details {
      display: flex;
      flex-wrap: wrap;
      gap: 12rpx;
      margin-bottom: 15rpx;
      align-items: center;

      .rating,
      .last-charge,
      .special-offer,
      .public-access {
        font-size: 22rpx;
        color: rgba(165, 165, 165, 1);
        font-family: SourceHanSansSC-Regular;
        font-weight: normal;
        line-height: 33rpx;
      }

      .rating {
        color: rgba(165, 165, 165, 1);
      }

      .special-offer {
        color: rgba(255, 107, 53, 1);
      }
    }

    .charger-availability {
      display: flex;
      gap: 25rpx;
      margin-bottom: 15rpx;
      background: linear-gradient(270deg, #ffffff 0%, #f9f9f9 100%);
      border-radius: 12rpx;

      .charger-type {
        display: flex;
        align-items: center;
        gap: 8rpx;
        background-color: rgba(249, 249, 249, 1);
        border-radius: 24rpx;
        padding: 12rpx 12rpx;

        .charger-icon-wrapper {
          width: 40rpx;
          height: 40rpx;
          border-radius: 8rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 5rpx;

          &.fast {
            background-color: rgba(255, 132, 20, 1);
            width: 36rpx;
            height: 36rpx;
            image {
              width: 36rpx;
              height: 36rpx;
            }
          }

          &.slow {
            background-color: rgba(0, 151, 255, 1);
            width: 36rpx;
            height: 36rpx;
            image {
              width: 36rpx;
              height: 36rpx;
            }
          }

          .charger-icon-text {
            font-size: 22rpx;
            color: rgba(255, 255, 255, 1);
            font-family: YouSheBiaoTiHei;
            font-weight: normal;
            line-height: 29rpx;
          }
        }

        .charger-status {
          font-size: 26rpx;
          color: #4f4f4f;
          font-family: SourceHanSansSC-Medium;
          font-weight: 500;
          line-height: 33rpx;

          .charger-status-text {
            color: rgba(255, 132, 20, 1);
          }

          .charger-status-blue {
            color: rgba(0, 151, 255, 1);
          }
        }
      }
      .line-shu {
        margin-top: 20rpx;
        width: 1rpx;
        height: 20rpx;
        background-color: #d0d0d0;
      }
    }

    .station-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 20rpx;

      .price-wrapper {
        display: flex;
        align-items: baseline;
        gap: 2rpx;

        .price-symbol {
          font-size: 22rpx;
          color: rgba(255, 59, 20, 1);
          font-family: SourceHanSansSC-Bold;
          font-weight: 700;
          line-height: 33rpx;
        }

        .price-value {
          font-size: 38rpx;
          color: rgba(255, 59, 20, 1);
          font-family: Arial-BoldMT;
          font-weight: bold;
          line-height: 43rpx;
        }

        .price-unit {
          font-size: 22rpx;
          color: rgba(255, 59, 20, 1);
          font-family: SourceHanSansSC-Regular;
          font-weight: normal;
          line-height: 33rpx;
        }
      }

      .navigate-btn {
        .nav-icons {
          height: 42rpx;
        }
      }
    }
  }
}
</style>
