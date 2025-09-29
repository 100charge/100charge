<template>
  <view class="invoice-page">
    <!-- 顶部工具栏 -->
    <view class="header-section">
      <view class="header-left">
        <text class="page-title">开具发票</text>
      </view>

      <view class="header-right" @click="showFilter">
       
        <text>抬头管理</text>
      </view>
    </view>

    <!-- 订单列表 -->
    <view class="order-list" v-if="orderList && orderList.length > 0">
      <view class="order-item" v-for="(item, index) in orderList" :key="item.id" @click="toggleSelection(item)">
        <view class="order-content">
          <!-- 选择框 -->
          <view class="select-box">
            <image 
              class="select-icon" 
              :src="item.isSelect ? '/static/img/checked-fill.png' : '/static/img/check-no.png'" 
              mode="aspectFit"
            ></image>
          </view>
          
          <!-- 订单信息 -->
          <view class="order-info">
            <view class="order-header">
              <view class="station-name">{{ item.stationName }}</view>
              <view class="order-price">
                <text class="currency">¥</text>
                <text class="amount">{{ item.realAmount }}</text>
              </view>
            </view>
            
            <view class="order-details">
              <text class="detail-item">充电量：{{ item.totalPower }}度</text>
              <view class="divider"></view>
              <text class="detail-item">充电时长：{{ item.realDuration || '--' }}</text>
            </view>
            
            <view class="order-footer">
              <text class="order-time">充电时间：{{ item.createTime }}</text>
              <text class="order-status completed">充电完成</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-else>
      <image class="empty-icon" src="/static/img/empty1.png" mode="aspectFit"></image>
      <text class="empty-text">暂无可开票订单</text>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar" v-if="orderList && orderList.length > 0">
      <view class="selection-info">
        <view class="selection-summary">
          <text class="summary-text">已选</text>
          <text class="summary-count">{{ selectedOrderCount || 0 }}</text>
          <text class="summary-text">个订单</text>
          <text class="summary-text">共计</text>
          <text class="summary-amount">{{ selectedOrderPrice || 0 }}</text>
          <text class="summary-text">元</text>
        </view>
        <view class="select-all-wrapper" @click="handleSelectAll">
          <image 
            class="select-all-icon" 
            :src="isSelectAll ? '/static/img/checked-fill.png' : '/static/img/check-no.png'" 
            mode="aspectFit"
          ></image>
          <text class="select-all-text">全选</text>
        </view>
      </view>
      <view class="apply-button" @click="handleApplyInvoice">申请开票</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderList: [],
      selectedOrders: [],
      isSelectAll: false,
      beginTime: "",
      endTime: ""
    }
  },
  computed: {
    selectedOrderCount() {
      return this.selectedOrders.length
    },
    selectedOrderPrice() {
      let total = 0
      this.selectedOrders.forEach(item => {
        total += parseFloat(item.realAmount || 0)
      })
      return total.toFixed(2)
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
      this.orderList = [
        {
          id: 1,
          stationName: "青岛劲松三路金泽供热站充电站",
          realAmount: "8.34",
          totalPower: "6",
          realDuration: "2分钟31秒",
          createTime: "2025-07-17 14:23",
          isSelect: false
        },
        {
          id: 2,
          stationName: "青岛劲松三路金泽供热站充电站",
          realAmount: "12.50",
          totalPower: "8",
          realDuration: "3分钟45秒",
          createTime: "2025-07-16 10:15",
          isSelect: false
        },
        {
          id: 3,
          stationName: "青岛市南区充电站",
          realAmount: "15.80",
          totalPower: "10",
          realDuration: "4分钟20秒",
          createTime: "2025-07-15 16:30",
          isSelect: false
        }
      ]
    },
    
    // 切换选择状态
    toggleSelection(item) {
      item.isSelect = !item.isSelect
      
      if (item.isSelect) {
        this.selectedOrders.push(item)
      } else {
        this.selectedOrders = this.selectedOrders.filter(order => order.id !== item.id)
      }
      
      // 更新全选状态
      this.isSelectAll = this.selectedOrders.length === this.orderList.length
    },
    
    // 全选/取消全选
    handleSelectAll() {
      this.isSelectAll = !this.isSelectAll
      
      if (this.isSelectAll) {
        this.selectedOrders = [...this.orderList]
        this.orderList.forEach(item => {
          item.isSelect = true
        })
      } else {
        this.selectedOrders = []
        this.orderList.forEach(item => {
          item.isSelect = false
        })
      }
    },
    
    // 跳转到抬头管理
    showFilter() {
      uni.navigateTo({
        url: '/pages/invoice/invoice-header'
      })
    },
    
    // 申请开票
    handleApplyInvoice() {
      if (this.selectedOrders.length === 0) {
        uni.showToast({
          title: "您还没选择订单哦！",
          icon: "none"
        })
        return
      }
      
      // 跳转到开票申请页面
      const orderIds = this.selectedOrders.map(item => item.id).join(',')
      uni.navigateTo({
        url: `/pages/invoice/invoice-create?orderPrice=${this.selectedOrderPrice}&selectedOrderIds=${orderIds}`
      })
    }
  }
}
</script>

<style scoped>
.invoice-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 200rpx;
}

/* 顶部工具栏 */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: white;
  border-bottom: 1rpx solid #f2f6f9;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
}

.header-left .page-title {
  font-size: 28rpx;
  color: #ff6b01;
 
}

.header-right {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #ff6b01;
}

.filter-icon {
  width: 32rpx;
  height: 32rpx;
  margin-right: 12rpx;
}

/* 订单列表 */
.order-list {
  padding: 120rpx 24rpx 0;
}

.order-item {
  margin-bottom: 20rpx;
}

.order-content {
  display: flex;
  align-items: flex-start;
  background-color: white;
  border-radius: 12rpx;
  padding: 24rpx;
}

.select-box {
  margin-right: 20rpx;
  padding-top: 8rpx;
}

.select-icon {
  width: 32rpx;
  height: 32rpx;
}

.order-info {
  flex: 1;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12rpx;
}

.station-name {
  font-size: 24rpx;
  font-weight: 600;
  color: #17141b;
  flex: 1;
  line-height: 1.4;
}

.order-price {
  color: #ff6b01;
  font-weight: 600;
  margin-left: 24rpx;
}

.currency {
  font-size: 26rpx;
  margin-right: 6rpx;
}

.amount {
  font-size: 30rpx;
}

.order-details {
  display: flex;
  align-items: center;
  margin: 12rpx 0;
  font-size: 26rpx;
  color: #666;
}

.detail-item {
  white-space: nowrap;
}

.divider {
  width: 2rpx;
  height: 28rpx;
  background-color: #666;
  margin: 0 24rpx;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 26rpx;
  color: #666;
}

.order-time {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-status {
  margin-left: 24rpx;
}

.order-status.completed {
  color: #ff6b01;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 60rpx;
}

.empty-icon {
  width: 480rpx;
  height: 280rpx;
  margin-bottom: 40rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #494e61;
}

/* 底部操作栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: white;
  padding: 20rpx 30rpx;
  box-shadow: 0px -2rpx 10rpx rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.selection-info {
  flex: 1;
}

.selection-summary {
  margin-bottom: 10rpx;
}

.summary-text {
  font-size: 25rpx;
  color: #999;
  margin-right: 8rpx;
}

.summary-count,
.summary-amount {
  font-size: 25rpx;
  color: #ff6b01;
  margin-right: 8rpx;
}

.select-all-wrapper {
  display: flex;
  align-items: center;
}

.select-all-icon {
  width: 32rpx;
  height: 32rpx;
  margin-right: 8rpx;
}

.select-all-text {
  font-size: 28rpx;
  color: #333;
}

.apply-button {
  width: 200rpx;
  height: 60rpx;
  background: linear-gradient(90deg, #ffb86c 0%, #ff6b01 100%);
  border-radius: 30rpx;
  color: white;
  font-size: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>