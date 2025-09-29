<template>
  <view class="invoice-header-page">
    <!-- 顶部选择区域 -->
    <view class="header-section">
      <view class="type-selector">
        <view class="selector-item" :class="{ active: selectedType === 'company' }" @click="selectType('company')">
          <text>企业</text>
          <image class="dropdown-icon" src="/static/img/down-gray.png" mode="aspectFit"></image>
        </view>
      </view>
      <view class="add-button" @click="toAddHeader">
        <view class="add-icon">+</view>
        <text>新增发票抬头</text>
      </view>
    </view>

    <!-- 发票抬头列表 -->
    <view class="invoice-list" v-if="invoiceHeaderList && invoiceHeaderList.length > 0">
      <view class="invoice-item" v-for="(item, index) in invoiceHeaderList" :key="index" @click="clickHeaderItem(item)">
        <view class="item-header">
          <view class="company-name">{{ item.taxCompany }}</view>
          <view class="default-tag" v-if="item.isDefault">默认</view>
        </view>
        <view class="item-details">
          <view class="detail-row">
            <text class="label">税号：</text>
            <text class="value">{{ item.taxNumber }}</text>
          </view>
        </view>
        <!-- <view class="item-actions">
          <view class="action-btn delete-btn" @click.stop="deleteItem(item)">
            <image class="action-icon" src="/static/img/delete.png" mode="aspectFit"></image>
          </view>
          <view class="action-btn edit-btn" @click.stop="clickHeaderItem(item)">
            <image class="action-icon" src="/static/img/edit.png" mode="aspectFit"></image>
          </view>
        </view> -->
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-else>
      <image class="empty-icon" src="/static/img/empty1.png" mode="aspectFit"></image>
      <view class="empty-text">您未设置发票抬头</view>
      <view class="empty-button" @click="toAddHeader">立即设置</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedType: 'company',
      invoiceHeaderList: [],
      openid: ""
    }
  },
  onLoad() {
    this.initMockData()
  },
  onShow() {
    this.initMockData()
  },
  methods: {
    // 初始化模拟数据
    initMockData() {
      this.invoiceHeaderList = [
        {
          id: 1,
          taxCompany: "中国农业银行股份有限公司",
          taxNumber: "728999901001001001973",
          configType: "company",
          receiveEmail: "example@abc.com",
          isDefault: true
        },
        {
          id: 2,
          taxCompany: "农业银行股份有限公司",
          taxNumber: "728999901001001001973",
          configType: "company",
          receiveEmail: "example@abc.com",
          isDefault: false
        }
      ]
    },
    
    selectType(type) {
      this.selectedType = type
    },
    
    toAddHeader() {
      uni.navigateTo({
        url: '/pages/invoice/invoice-header-add'
      })
    },
    
    clickHeaderItem(item) {
      uni.navigateTo({
        url: `/pages/invoice/invoice-header-add?id=${item.id}&fromPage=invoiceHeader`
      })
    },
    
    deleteItem(item) {
      uni.showModal({
        title: "提示",
        content: "确定要删除该发票抬头吗？",
        success: (res) => {
          if (res.confirm) {
            // 从列表中删除
            const index = this.invoiceHeaderList.findIndex(header => header.id === item.id)
            if (index > -1) {
              this.invoiceHeaderList.splice(index, 1)
            }
            uni.showToast({
              title: "删除成功",
              icon: "success"
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.invoice-header-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-top: 20rpx;
}

/* 顶部选择区域 */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: white;
  margin-bottom: 20rpx;
}

.type-selector {
  flex: 1;
}

.selector-item {
  display: flex;
  align-items: center;
  font-size: 26rpx;
  color: #333;
  font-weight: 500;
}

.selector-item.active {
  color: #000;
}

.dropdown-icon {
  width: 24rpx;
  height: 24rpx;
  margin-left: 16rpx;
}

.add-button {
  display: flex;
  align-items: center;
 
  color: #000;
  border-radius: 50rpx;
  padding: 16rpx 24rpx;
  font-size: 28rpx;
}

.add-icon {
  width: 32rpx;
  height: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12rpx;
  font-size: 24rpx;
  font-weight: 300;
  background-color: #333;
  color: white;
  border-radius: 50%;
}

/* 发票抬头列表 */
.invoice-list {
  padding: 0 30rpx;
}

.invoice-item {
  background-color: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  position: relative;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.company-name {
  font-size: 32rpx;
  color: #333;
  font-weight: 600;
  flex: 1;
  line-height: 1.4;
}

.default-tag {
  background-color: rgba(255, 107, 1, 0.1);
  color: #ff6b01;
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  margin-left: 20rpx;
}

.item-details {
  margin-bottom: 30rpx;
}

.detail-row {
  margin-bottom: 12rpx;
}

.label {
  font-size: 26rpx;
  color: #666;
}

.value {
  font-size: 26rpx;
  color: #333;
}

.item-actions {
  display: flex;
  justify-content: flex-end;
  gap: 30rpx;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
}

.action-icon {
  width: 36rpx;
  height: 36rpx;
}

.delete-btn {
  background-color: #f5f5f5;
}

.edit-btn {
  background-color: #f5f5f5;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 60rpx;
  text-align: center;
}

.empty-icon {
  width: 480rpx;
  height: 279rpx;
  margin-bottom: 40rpx;
}

.empty-text {
  font-size: 32rpx;
  color: #666;
  margin-bottom: 60rpx;
}

.empty-button {
  background-color: #ff6b01;
  color: white;
  font-size: 32rpx;
  padding: 20rpx 60rpx;
  border-radius: 50rpx;
}
</style>
