<template>
  <view class="instructionMain">
    <view class="container">
      <view class="content-box">
        <rich-text style="width: 100%" space="nbsp" :nodes="content | formatRichText"></rich-text>
      </view>
    </view>
  </view>
</template>

<script>
import app from "@/static/js/app.js"
import { getNoticeDetail } from "@/config/api.js"
export default {
  components: {},
  data() {
    return {
      content: "",
      id: 0,
    }
  },
  filters: {
    formatRichText(html) {
      //控制小程序中图片大小
      let newContent = html.replace(/<img[^>]*>/gi, (match, capture) => {
        match = match.replace(/style=""/gi, "").replace(/style=''+'/gi, "")
        return match
      })
      newContent = newContent.replace(/style="[^"]+"/gi, (match, capture) => {
        match = match.replace(/width:[^;]+;/gi, "max-width:100%;").replace(/width:[^;]+;/gi, "max-width:100%;")
        return match
      })
      newContent = newContent.replace(/<br[^>]*\/>/gi, "")
      newContent = newContent.replace(/\<img/gi, '<img style="max-width:100%;height:auto;display:inline-block;" ')
      return newContent
    },
  },
  onLoad(options) {
    this.id = options.id
    this.getData(options.id)
  },

  methods: {
    getData(value) {
      getNoticeDetail({
        noticeId: value,
      }).then((res) => {
        if (res.code == 200) {
          this.content = res.data.noticeContent
          this.title = res.data.noticeTitle || "文章"
          uni.setNavigationBarTitle({
            title: res.data.noticeTitle,
          })
        }
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.instructionMain {
  background: #fff;
  width: 100vw;
  min-height: 100vh;
  overflow: hidden;

  .head {
    width: 100%;
    height: 100%;

    image {
      margin-bottom: 20rpx;
    }
  }

  .container {
    .container-tabs {
      height: 94rpx;
      background-color: #fff;
    }

    .content-box {
      padding: 20rpx;
    }
  }
}

::v-deep .u-collapse-item {
  border-radius: 10rpx;
  background-color: #0f1f43;
  margin: 20rpx 0;

  .u-cell__body {
    padding: 15rpx !important;

    // border-bottom: 1px solid #9CAEDB;
    .u-cell__title-text {
      color: #9caedb !important;
    }
  }
}

::v-deep .u-line {
  display: none;
}

::v-deep .u-icon__icon--info {
  color: #9caedb !important;
}

::v-deep .u-collapse-item__content__text {
  padding: 15rpx !important;
  color: #95abc7 !important;
}

::v-deep .u-cell--clickable {
  background-color: transparent !important;
}
</style>
