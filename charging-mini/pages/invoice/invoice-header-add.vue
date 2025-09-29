<template>
    <view class="add-header-page">
      <!-- 发票详情部分 -->
      <view class="section">
        <view class="section-title">发票详情</view>
        
        <!-- 抬头类型选择 -->
        <view class="form-item">
          <view class="form-label required">抬头类型</view>
          <view class="radio-group">
            <view class="radio-item" :class="{ active: selectOption === 'option1' }" @click="selectHeaderType('company')">
              <view class="radio-circle" :class="{ checked: selectOption === 'option1' }">
                <view class="radio-dot" v-if="selectOption === 'option1'"></view>
              </view>
              <text>企业单位</text>
            </view>
            <view class="radio-item" :class="{ active: selectOption === 'option2' }" @click="selectHeaderType('personal')">
              <view class="radio-circle" :class="{ checked: selectOption === 'option2' }">
                <view class="radio-dot" v-if="selectOption === 'option2'"></view>
              </view>
              <text>个人/非企业单位</text>
            </view>
          </view>
        </view>
        
        <!-- 公司名称/抬头名称 -->
        <view class="form-item" v-if="selectOption === 'option1'">
          <view class="form-label required">公司名称</view>
          <input 
            class="form-input" 
            type="text" 
            placeholder="请填写公司名称"
            v-model="taxCompany"
          />
        </view>
        
        <view class="form-item" v-if="selectOption === 'option2'">
          <view class="form-label required">抬头名称</view>
          <input 
            class="form-input" 
            type="text" 
            placeholder="请填写抬头名称"
            v-model="personHeader"
          />
        </view>
        
        <!-- 税号 -->
        <view class="form-item" v-if="selectOption === 'option1'">
          <view class="form-label required">税号</view>
          <input 
            class="form-input" 
            type="text" 
            placeholder="请填写纳税人识别号"
            v-model="taxNumber"
          />
        </view>
        
        <!-- 更多信息展开 -->
        <view class="more-info-toggle" @click="toggleMoreInfo">
          <text>更多信息（填写地址等选填项）</text>
          <image 
            class="toggle-icon" 
            :class="{ rotated: showMoreInfo }"
            src="/static/img/down-gray.png" 
            mode="aspectFit"
          ></image>
        </view>
        
        <!-- 更多信息内容 -->
        <view class="more-info-content" v-if="showMoreInfo && selectOption === 'option1'">
          <view class="form-item">
            <view class="form-label">注册地址</view>
            <input 
              class="form-input" 
              type="text" 
              placeholder="请填写公司注册地址"
              v-model="taxAddress"
            />
          </view>
          <view class="form-item">
            <view class="form-label">注册电话</view>
            <input 
              class="form-input" 
              type="text" 
              placeholder="请填写公司注册电话"
              v-model="phone"
            />
          </view>
          <view class="form-item">
            <view class="form-label">开户银行</view>
            <input 
              class="form-input" 
              type="text" 
              placeholder="请填写公司开户银行"
              v-model="openBankName"
            />
          </view>
          <view class="form-item">
            <view class="form-label">银行账号</view>
            <input 
              class="form-input" 
              type="text" 
              placeholder="请填写银行账号"
              v-model="openBankAccount"
            />
          </view>
        </view>
      </view>
      
      <!-- 接收信息部分 -->
      <view class="section">
        <view class="section-title">接收信息</view>
        
        <!-- 电子邮箱 -->
        <view class="form-item">
          <view class="form-label required">电子邮箱</view>
          <input 
            class="form-input" 
            type="text" 
            placeholder="请填写电子邮箱"
            v-model="receiveEmail"
          />
        </view>
      </view>
      
      <!-- 设置为默认发票抬头 -->
      <view class="default-setting">
        <text>设置为默认发票抬头</text>
        <view class="switch-wrapper" @click="toggleDefault">
          <view class="switch" :class="{ active: isMoren }">
            <view class="switch-dot" :class="{ active: isMoren }"></view>
          </view>
        </view>
      </view>
      
      <!-- 底部保存按钮 -->
      <view class="bottom-action">
        <view class="save-button" @click="saveHeader">保存</view>
      </view>
    </view>
  </template>
  
  <script>
  export default {
    data() {
      return {
        selectOption: 'option1', // option1: 企业单位, option2: 个人/非企业单位
        showMoreInfo: false,
        isMoren: false,
        taxCompany: '',
        taxNumber: '',
        taxAddress: '',
        phone: '',
        openBankName: '',
        openBankAccount: '',
        personHeader: '',
        receiveEmail: '',
        headerId: 0, // 0 添加 不为0 修改
        openid: '',
        fromPage: ''
      }
    },
    onLoad(options) {
      console.log(options)
      this.openid = uni.getStorageSync('openid')
      
      // 如果是编辑模式，加载数据
      if (options.id) {
        this.loadHeaderData(options.id)
      }
      
      if (options.fromPage) {
        this.fromPage = options.fromPage
      }
    },
    methods: {
      selectHeaderType(type) {
        this.selectOption = type === 'company' ? 'option1' : 'option2'
        this.clearFormData()
      },
      
      toggleMoreInfo() {
        this.showMoreInfo = !this.showMoreInfo
      },
      
      toggleDefault() {
        this.isMoren = !this.isMoren
      },
      
      clearFormData() {
        this.taxCompany = ''
        this.taxNumber = ''
        this.taxAddress = ''
        this.phone = ''
        this.openBankName = ''
        this.openBankAccount = ''
        this.personHeader = ''
      },
      
      loadHeaderData(id) {
        // 模拟加载数据，实际项目中这里会调用API
        // getInvoiceHeaderDetail({ configId: id }).then((res) => {
        //   const data = res.data
        //   this.taxCompany = data.taxCompany
        //   this.taxNumber = data.taxNumber
        //   this.taxAddress = data.taxAddress
        //   this.phone = data.phone
        //   this.openBankName = data.openBankName
        //   this.openBankAccount = data.openBankAccount
        //   this.selectOption = data.configType === 'company' ? 'option1' : 'option2'
        //   this.isMoren = data.isDefault
        //   this.personHeader = data.configType === 'company' ? '' : data.taxCompany
        //   this.headerId = data.id
        //   this.receiveEmail = data.receiveEmail
        // })
      },
      
      saveHeader() {
        if (!this.validateForm()) {
          return
        }
        
        const headerContent = this.selectOption === 'option1' ? this.taxCompany : this.personHeader
        const params = {
          isDefault: this.isMoren,
          taxCompany: headerContent,
          taxNumber: this.taxNumber,
          taxAddress: this.taxAddress,
          phone: this.phone,
          openBankName: this.openBankName,
          openBankAccount: this.openBankAccount,
          configType: this.selectOption === 'option1' ? 'company' : 'personal',
          id: this.headerId,
          receiveEmail: this.receiveEmail,
          remark: ''
        }
        
        // 实际项目中这里会调用API
        // const apiUrl = this.headerId ? apiGetInvoiceHeaderEdit : apiGetInvoiceHeaderAdd
        // apiUrl(params).then((result) => {
        //   uni.showToast({
        //     title: '保存成功',
        //     icon: 'success'
        //   })
        //   setTimeout(() => {
        //     uni.navigateBack()
        //   }, 2000)
        // })
        
        // 模拟保存成功
        uni.showToast({
          title: '保存成功',
          icon: 'success'
        })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1000)
      },
      
      validateForm() {
        if (this.selectOption === 'option1') {
          if (!this.taxCompany) {
            uni.showToast({
              title: '您还没有输入公司名称哦！',
              icon: 'none'
            })
            return false
          }
          if (!this.taxNumber) {
            uni.showToast({
              title: '您还没有输入公司税号哦！',
              icon: 'none'
            })
            return false
          }
          if (this.taxNumber.length < 6 || this.taxNumber.length > 20) {
            uni.showToast({
              title: '公司税号为6到20位哦！',
              icon: 'none'
            })
            return false
          }
          if (this.openBankAccount && (this.openBankAccount.length < 15 || this.openBankAccount.length > 18)) {
            uni.showToast({
              title: '银行账号为15-18位！',
              icon: 'none'
            })
            return false
          }
        } else {
          if (!this.personHeader) {
            uni.showToast({
              title: '您还没有输入抬头名称哦！',
              icon: 'none'
            })
            return false
          }
        }
        
        if (!this.receiveEmail) {
          uni.showToast({
            title: '您还没有输入电子邮箱号哦！',
            icon: 'none'
          })
          return false
        }
        
        if (!this.isValidEmail(this.receiveEmail)) {
          uni.showToast({
            title: '您输入的邮箱号有误！',
            icon: 'none'
          })
          return false
        }
        
        return true
      },
      
      isValidEmail(email) {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        return emailPattern.test(email)
      }
    }
  }
  </script>
  
  <style scoped>
  .add-header-page {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding-bottom: 200rpx;
  }
  
  .section {
    background-color: white;
    margin-bottom: 20rpx;
    padding: 40rpx 30rpx;
  }
  
  .section-title {
    font-size: 28rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 40rpx;
  }
  
     .form-item {
     margin-bottom: 40rpx;
     display: flex;
     align-items: center;
     border-bottom: 1rpx solid #f0f0f0;
   }
   
   .form-label {
     font-size: 24rpx;
     color: #333;
     position: relative;
     width: 200rpx;
     flex-shrink: 0;
   }
  
  .form-label.required::before {
    content: '*';
    color: #ff6b01;
    margin-right: 8rpx;
  }
  
     .form-input {
     flex: 1;
     height: 80rpx;
     border-radius: 8rpx;
     padding: 0 20rpx;
     font-size: 24rpx;
     color: #333;
     box-sizing: border-box;
   }
  
  .form-input::placeholder {
    color: #999;
  }
  
     .radio-group {
     display: flex;
     gap: 60rpx;
     flex: 1;
   }
  
  .radio-item {
    display: flex;
    align-items: center;
    gap: 16rpx;
    font-size: 24rpx;
    color: #333;
  }
  
  .radio-circle {
    width: 36rpx;
    height: 36rpx;
    border: 2rpx solid #ddd;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .radio-circle.checked {
    border-color: #ff6b01;
  }
  
  .radio-dot {
    width: 20rpx;
    height: 20rpx;
    background-color: #ff6b01;
    border-radius: 50%;
  }
  
  .more-info-toggle {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 16rpx;
    font-size: 24rpx;
    color: #999;
    padding: 20rpx 0;
    margin-top: 20rpx;
  }
  
  .toggle-icon {
    width: 24rpx;
    height: 24rpx;
    transition: transform 0.3s;
  }
  
  .toggle-icon.rotated {
    transform: rotate(180deg);
  }
  
  .more-info-content {
    margin-top: 20rpx;
  }
  
  .default-setting {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: white;
    padding: 40rpx 30rpx;
    margin-bottom: 20rpx;
  }
  
  .default-setting text {
    font-size: 24rpx;
    color: #333;
  }
  
  .switch-wrapper {
    padding: 10rpx;
  }
  
  .switch {
    width: 100rpx;
    height: 60rpx;
    background-color: #ddd;
    border-radius: 30rpx;
    position: relative;
    transition: background-color 0.3s;
  }
  
  .switch.active {
    background-color: #ff6b01;
  }
  
  .switch-dot {
    width: 52rpx;
    height: 52rpx;
    background-color: white;
    border-radius: 50%;
    position: absolute;
    top: 4rpx;
    left: 4rpx;
    transition: transform 0.3s;
  }
  
  .switch-dot.active {
    transform: translateX(40rpx);
  }
  
  .bottom-action {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 30rpx;
    background-color: white;
  }
  
  .save-button {
    width: 100%;
    height: 88rpx;
    background-color: #333;
    color: white;
    border-radius: 44rpx;
    font-size: 28rpx;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  </style>
  