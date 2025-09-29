<template>
	<view class="coupon-page">
	  <!-- 顶部Tab栏 -->
	  <view class="tab-bar">
		<view class="tab-item" :class="{ active: activeTab === 0 }" @click="switchTab(0)">未使用</view>
		<view class="tab-item" :class="{ active: activeTab === 1 }" @click="switchTab(1)">已使用</view>
		<view class="tab-item" :class="{ active: activeTab === 2 }" @click="switchTab(2)">已过期</view>
	  </view>
  
	  <!-- 优惠券列表 -->
	  <view class="coupon-list" v-if="couponList && couponList.length > 0">
		<view class="coupon-item" v-for="(item, index) in couponList" :key="index">
		  <!-- 优惠券左侧金额区域 -->
		  <view class="coupon-left" :class="{ disabled: item.status !== 0 }">
			<view class="price-section">
			  <text class="currency" v-if="item.couponType !== 1">¥</text>
			  <text class="amount">{{ item.couponType === 1 ? item.discountPercentage : item.giftAmount }}</text>
			  <text class="currency" v-if="item.couponType === 1">折</text>
			</view>
			<view class="condition" v-if="item.couponType === 0">
			  满{{ item.totalAmount }}可用
			</view>
			<view class="condition" v-else>
			  无门槛
			</view>
		  </view>
  
		  <!-- 优惠券右侧信息区域 -->
		  <view class="coupon-right">
			<view class="coupon-info">
			  <view class="coupon-title">{{ item.name }}</view>
			  <view class="coupon-desc">{{ item.applicableScopeDesc }}</view>
			  <view class="coupon-time">{{ item.expireTime }} 前到期</view>
			</view>
			
			<!-- 状态按钮 -->
			<view class="coupon-status">
			  <view class="status-btn active" v-if="item.status === 0" @click="useCoupon(item)">
				去使用
			  </view>
			  <view class="status-btn used" v-else-if="item.status === 1">
				已使用
			  </view>
			  <view class="status-btn expired" v-else-if="item.status === 2">
				已过期
			  </view>
			  <view class="status-btn expired" v-else>
				即将到期
			  </view>
			</view>
		  </view>
		</view>
	  </view>
  
	  <!-- 空状态 -->
	  <view class="empty-state" v-else>
		<image class="empty-icon" src="/static/img/empty1.png" mode="aspectFit"></image>
		<text class="empty-text">暂无优惠券</text>
	  </view>
	</view>
  </template>
  
  <script>
  export default {
	data() {
	  return {
		activeTab: 0, // 0: 未使用, 1: 已使用, 2: 已过期
		couponList: [],
		pageNum: 1,
		totalRows: 0,
		loadStatus: 'loadmore'
	  }
	},
	onLoad() {
	  this.initMockData()
	},
	onShow() {
	  this.loadCouponList()
	},
	onPullDownRefresh() {
	  this.loadCouponList()
	  setTimeout(() => {
		uni.stopPullDownRefresh()
	  }, 1000)
	},
	onReachBottom() {
	  if (this.pageNum * 10 < this.totalRows) {
		this.loadStatus = 'loading'
		this.pageNum++
		this.loadCouponList()
	  } else {
		this.loadStatus = 'nomore'
	  }
	},
	methods: {
	  switchTab(index) {
		this.activeTab = index
		this.pageNum = 1
		this.loadCouponList()
	  },
	  
	  initMockData() {
		this.couponList = [
		  {
			id: 1,
			name: '充电专享券',
			couponType: 0, // 0: 满减券, 1: 折扣券
			giftAmount: 2.1,
			totalAmount: 0,
			applicableScopeDesc: '仅限新用户专享',
			expireTime: '2052-08-26 11:03:30',
			status: 0 // 0: 未使用, 1: 已使用, 2: 已过期
		  },
		  {
			id: 2,
			name: '充电专享券',
			couponType: 0,
			giftAmount: 2.1,
			totalAmount: 0,
			applicableScopeDesc: '仅限新用户专享',
			expireTime: '2052-08-26 11:03:30',
			status: 0
		  },
		  {
			id: 3,
			name: '充电专享券',
			couponType: 0,
			giftAmount: 2.1,
			totalAmount: 0,
			applicableScopeDesc: '仅限新用户专享',
			expireTime: '2052-08-26 11:03:30',
			status: 0
		  },
		  {
			id: 4,
			name: '限时专享券',
			couponType: 0,
			giftAmount: 2,
			totalAmount: 10,
			applicableScopeDesc: '仅限12:00-13:00时段使用',
			expireTime: '2025-07-26 11:03:30',
			status: 2
		  }
		]
	  },
	  
	  loadCouponList() {
		// 根据activeTab过滤数据
		this.initMockData()
		if (this.activeTab !== 0) {
		  this.couponList = this.couponList.filter(item => item.status === this.activeTab)
		} else {
		  this.couponList = this.couponList.filter(item => item.status === 0)
		}
		
		// 实际项目中这里会调用API
		// appUserPromotionList({
		//   status: this.activeTab,
		//   pageNum: this.pageNum,
		//   pageSize: 10
		// }).then((res) => {
		//   if (res.code === 200) {
		//     this.totalRows = res.total
		//     if (this.pageNum === 1) {
		//       this.couponList = res.rows || []
		//     } else {
		//       this.couponList.push(...res.rows)
		//     }
		//   }
		// })
	  },
	  
	  useCoupon(item) {
		// 跳转到首页使用优惠券
		uni.switchTab({
		  url: '/pages/index/index'
		})
	  }
	}
  }
  </script>
  
  <style scoped>
  .coupon-page {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding-bottom: 120rpx;
  }
  
  /* 顶部Tab栏 */
  .tab-bar {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	background-color: white;
	display: flex;
	align-items: center;
	z-index: 999;
	padding: 20rpx 96rpx;
  }
  
  .tab-item {
	flex: 1;
	text-align: center;
	font-size: 28rpx;
	color: #666;
	padding: 20rpx 0;
	position: relative;
  }
  
  .tab-item.active {
	color: #ff6b01;
	font-weight: 600;
  }
  
  .tab-item.active::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	width: 40rpx;
	height: 4rpx;
	background-color: #ff6b01;
	border-radius: 2rpx;
  }
  
  /* 优惠券列表 */
  .coupon-list {
	padding: 140rpx 30rpx 0;
  }
  
  .coupon-item {
	display: flex;
	background-image: url('/static/img/youhui.png');
	background-size: 100% 100%;
	background-position: center;
	background-repeat: no-repeat;
	margin-bottom: 20rpx;
	height: 160rpx;
	position: relative;
}
  
  /* 优惠券左侧 */
.coupon-left {
	width: 200rpx;
	background: transparent;
	color: #FF3B14;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 30rpx 20rpx;
	position: relative;
}

.coupon-left.disabled {
	background: transparent;
	opacity: 0.6;
}
  
  .price-section {
	display: flex;
	align-items: baseline;
	margin-bottom: 8rpx;
  }
  
  .currency {
	font-size: 24rpx;
	font-weight: 500;
  }
  
  .amount {
	font-size: 52rpx;
	font-weight: bold;
	margin: 0 4rpx;
}
  
  .condition {
	font-size: 20rpx;
	opacity: 0.9;
	text-align: center;
  }
  
  /* 优惠券右侧 */
.coupon-right {
	flex: 1;
	padding: 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	background: transparent;
}
  
  .coupon-info {
	flex: 1;
  }
  
  .coupon-title {
	font-size: 32rpx;
	color: #222;
	font-weight: 600;
	margin-bottom: 8rpx;
}
  
  .coupon-desc {
	font-size: 24rpx;
	color: #888;
	margin-bottom: 8rpx;
}
  
  .coupon-time {
	font-size: 22rpx;
	color: #aaa;
}
  
  /* 状态按钮 */
  .coupon-status {
	margin-left: 20rpx;
  }
  
  .status-btn {
	padding: 12rpx 24rpx;
	border-radius: 30rpx;
	font-size: 24rpx;
	text-align: center;
	min-width: 120rpx;
  }
  
  .status-btn.active {
	background-color: #ff6b01;
	color: white;
	border-radius: 20rpx;
}
  
  .status-btn.used {
	background-color: #f0f0f0;
	color: #999;
  }
  
  .status-btn.expired {
	background-color: #f0f0f0;
	color: #999;
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
	color: #999;
  }
  </style>
  