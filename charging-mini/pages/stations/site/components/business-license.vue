<template>
  <u-action-sheet :safeAreaInsetBottom="true" :title="title" :show="showPopup" round="30" @close="close">
    <view class="popup-content">
      <image v-if="licenseImg" @click="previewImage" :src="licenseImg" mode="aspectFit"></image>
      <view v-else class="empty-wrapper">
        <image src="/static/images/index/empty.png" mode="aspectFit"></image>
        <view class="empty-text">暂未上传执照照片</view>
      </view>
    </view>
  </u-action-sheet>
</template>
<script>
export default {
  name: "business-license",
  data() {
    return {
      title: "营业执照",
      licenseImg: "",
      showPopup: false,
    }
  },

  methods: {
    onOpen(params) {
      console.log(params)
      this.licenseImg = params.licenseImage || ""
      this.showPopup = true
    },
    close() {
      this.showPopup = false
    },
    //预览图片
    previewImage() {
      this.showPopup = false
      let urls = [this.licenseImg]
      uni.previewImage({
        current: 0,
        urls: urls,
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.popup-content {
  // height: 70vh;
  padding: 32rpx;
  padding-bottom: 150rpx;
  .tip-text {
    font-weight: 400;
    font-size: 22rpx;
    color: $basic-color;
    line-height: 36rpx;
    text-align: left;
    margin-top: 32rpx;
    margin-bottom: 24rpx;
  }
  image {
    width: 100%;
    height: 300rpx;
    border-radius: 12rpx;
  }
}
</style>
