<template>
<uni-shadow-root class="privacy-privacy"><view v-if="innerShow" class="privacy">
  <view class="privacy-mask"></view>
  <view class="privacy-dialog-wrap">
    <view class="privacy-dialog">
      <view class="privacy-dialog-header">用户隐私保护提示</view>
      <view class="privacy-dialog-content">感谢您使用本小程序，在使用前您应当阅读井同意<text class="privacy-link" @click="openPrivacyContract">《用户隐私保护指引》</text>，当点击同意并继续时，即表示您已理解并同意该条款内容，该条款将对您产生法律约束力；如您不同意，将无法继续使用小程序相关功能。</view>
      <view class="privacy-dialog-footer">
        <button id="btn-disagree" type="default" class="btn btn-disagree" @click="handleDisagree">不同意</button>
        <button id="agree-btn" type="default" open-type="agreePrivacyAuthorization" class="btn btn-agree" @agreeprivacyauthorization="handleAgree">同意并继续</button>
      </view>
    </view>
  </view>
</view></uni-shadow-root>
</template>

<script>

global['__wxVueOptions'] = {components:{}}

global['__wxRoute'] = 'privacy/privacy'
let privacyHandler
let privacyResolves =new Set()
let closeOtherPagePopUpHooks =new Set()

if(wx.onNeedPrivacyAuthorization){
  wx.onNeedPrivacyAuthorization(resolve=>{
    if(typeof privacyHandler ==='function'){
      privacyHandler(resolve)
    }
  })
}

const closeOtherPagePopUp=(closePopUp)=>{
  closeOtherPagePopUpHooks.forEach(hook=>{
    if(closePopUp !== hook){
      hook()
    }
  })
}

Component({
  data:{
    innerShow:false,
  },
  lifetimes:{
    attached:function(){
      const closePopUp=()=>{
        this.disPopUp()
      }
      privacyHandler=resolve=>{
        privacyResolves.add(resolve)
        this.popUp()
        // 额外逻辑：当前页面的隐私弹窗弹起的时候，关掉其他页面的隐私弹窗
        closeOtherPagePopUp(closePopUp)
      }

      closeOtherPagePopUpHooks.add(closePopUp)

      this.closePopUp = closePopUp
    },
    detached:function(){
      closeOtherPagePopUpHooks.delete(this.closePopUp)
    }
  },
  pageLifetimes:{
    show:function(){
      this.curPageShow()
    }
  },
  methods:{
    handleAgree(e){
      this.disPopUp()
      privacyResolves.forEach(resolve=>{
        resolve({
          event:'agree',
          buttonId:'agree-btn'
        })
      })
      privacyResolves.clear()
    },
    handleDisagree(e){
      this.disPopUp()
      privacyResolves.forEach(resolve=>{
        resolve({
          event:'disagree',
        })
      })
      privacyResolves.clear()
    },
    popUp(){
      if(this.data.innerShow ===false){
        this.setData({
          innerShow:true
        })
      }
    },
    disPopUp(){
      if(this.data.innerShow ===true){
        this.setData({
          innerShow:false
        })
      }
    },
    openPrivacyContract(){
      wx.openPrivacyContract({
        success:res=>{
          console.log('openPrivacyContract success')
        },
        fail:res=>{
          console.error('openPrivacyContract fail', res)
        }
      })
    },
    curPageShow(){
      if(this.closePopUp){
        privacyHandler=resolve=>{
          privacyResolves.add(resolve)
          this.popUp()
          // 额外逻辑：当前页面的隐私弹窗弹起的时候，关掉其他页面的隐私弹窗
          closeOtherPagePopUp(this.closePopUp)
        }
      }
    }
  }
})
export default global['__wxComponents']['privacy/privacy']
</script>
<style platform="mp-weixin">
.privacy-mask {
  position: fixed;
  z-index: 5000;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.2);
}

.privacy-dialog-wrap {
  position: fixed;
  z-index: 5000;
  top: 16px;
  bottom: 16px;
  left: 80rpx;
  right: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.privacy-dialog {
  background-color: #fff;
  border-radius: 32rpx;
}

.privacy-dialog-header {
  padding: 60rpx 40rpx 30rpx;
  font-weight: 700;
  font-size: 36rpx;
  text-align: center;
}

.privacy-dialog-content {
  font-size: 30rpx;
  color: #555;
  line-height: 2;
  text-align: left;
  padding: 0 40rpx;
}

.privacy-dialog-content .privacy-link {
  color: #2f80ed;
}

.privacy-dialog-footer {
  display: flex;
  padding: 20rpx 40rpx 60rpx;
}

.privacy-dialog-footer .btn {
  color: #FFF;
  font-size: 30rpx;
  font-weight: 500;
  line-height: 100rpx;
  text-align: center;
  height: 100rpx;
  border-radius: 20rpx;
  border: none;
  background: #07c160;
  flex: 1;
  margin-left: 30rpx;
  justify-content: center;
}

.privacy-dialog-footer .btn::after {
  border: none;
}

.privacy-dialog-footer .btn-disagree {
  color: #07c160;
  background: #f2f2f2;
  margin-left: 0;
}
</style>