<template>
  <view class="home-list-comp">
    <view class="content">
      <view v-if="stationList && stationList.length !== 0">
        <home-station-card v-for="(item, index) in stationList" :key="index" :stationItem="item" />
        <u-loadmore :status="loadStatus" />
      </view>
      <view v-else class="empty-wrapper">
        <image src="/static/images/index/empty1.png" mode="aspectFit"></image>
        <view class="empty-text">暂无场站</view>
      </view>
    </view>
  </view>
</template>

<script>
import HomeStationCard from "../home-station-card/home-station-card.vue"
import HomeStickySearch from "../home-sticky-search/home-sticky-search.vue"
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
    loadStatus: {
      default: "",
      type: String,
    },
  },
  data() {
    return {}
  },

  onReady() {
    uni.pageScrollTo({
      scrollTop: 1,
      duration: 10,
    })
  },
  methods: {
    openUrl(item) {
      if (!item.link) {
        return
      }
      uni.navigateTo({
        url: link,
      })
    },

    onStickySearch(e) {
      this.$emit("onComplete", e)
    },
  },
}
</script>

<style lang="scss" scoped>
.home-list-comp {
  min-height: 100vh;
  background-color: #f4f4f4;
  position: relative;
  padding-top: 2rpx;
}
.home-swiper {
  padding-top: 20rpx;
  width: 100%;
  margin: 0 auto;
  border-radius: 10rpx;
  overflow: hidden;
  background-color: #fff;

  swiper {
    width: 684rpx;
    height: 167rpx;
    margin: 0 auto;

    swiper-item {
      width: 686rpx;
      height: 167rpx;

      .swiper-item {
        width: 686rpx;
        height: 167rpx;

        image {
          width: 686rpx;
          height: 167rpx;
          opacity: 1;
          background: rgba(0, 0, 0, 0);
          border-radius: 16rpx;
          // box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(0, 0, 0, 0.1);
        }
      }
    }
  }
}
.content {
  flex: 1;
  width: 100%;
  /* #ifdef MP-ALIPAY */
  height: 90vh;
  /* #endif */
  padding: 24rpx;
  padding-bottom: 210rpx;
}
</style>
