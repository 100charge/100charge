<template>
	<view class="custom-bottom-nav">
		<image class="bottom-nav-bg" src="/static/img/bottom.png" mode="widthFix"></image>
		<view class="nav-items">
			<view class="nav-item" :class="{ active: currentPage === 'home' }" @click="goToHome">
				<image 
					class="nav-icon" 
					:src="getTabIcon('home')" 
					mode="aspectFit"
				></image>
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item" @click="goToScan" style="margin-bottom: 30rpx;">
				<image class="nav-icon" src="/static/img/sao.png" mode="aspectFit" style="width: 60rpx;height: 60rpx;"></image>
				<text class="nav-text">扫码</text>
			</view>
			<view class="nav-item" :class="{ active: currentPage === 'profile' }" @click="goToProfile">
				<image 
					class="nav-icon" 
					:src="getTabIcon('profile')" 
					mode="aspectFit"
				></image>
				<text class="nav-text">我的</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'BottomTabBar',
	props: {
		currentPage: {
			type: String,
			default: 'home'
		}
	},
	methods: {
		getTabIcon(tabName) {
			// 根据当前页面和tab名称返回对应的图标
			const isActive = this.currentPage === tabName
			
			switch(tabName) {
				case 'home':
					return isActive ? '/static/img/index-ture.png' : '/static/img/index.png'
				case 'profile':
					return isActive ? '/static/img/mine.png' : '/static/img/mine-no.png'
				default:
					return '/static/img/sao.png'
			}
		},
		goToHome() {
			if (this.currentPage !== 'home') {
				uni.switchTab({
					url: '/pages/index/index'
				})
			}
		},
		goToScan() {
			// 直接调用扫码功能
			uni.scanCode({
				success: (res) => {
					console.log('扫码结果:', res.result)
					// 处理扫码结果，跳转到相应页面
					uni.navigateTo({
						url: `/pages/charging/charging?code=${encodeURIComponent(res.result)}`
					})
				},
				fail: (err) => {
					console.log('扫码失败:', err)
					uni.showToast({
						title: '扫码失败',
						icon: 'none'
					})
				}
			})
		},
		goToProfile() {
			if (this.currentPage !== 'profile') {
				uni.switchTab({
					url: '/pages/profile/profile'
				})
			}
		}
	}
}
</script>

<style scoped>
/* 底部导航样式 */
.custom-bottom-nav {
	position: fixed;
	bottom:-6rpx;
	left: 0;
	right: 0;
	z-index: 1000;
}

.bottom-nav-bg {
	width: 100%;
	height: auto;
}

.nav-items {
	position: absolute;
	bottom: 14rpx;
	left: 0;
	right: 0;
	display: flex;
	height: 120rpx;
	align-items: center;
	justify-content: space-around;
	padding-bottom: 40rpx;
}

.nav-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	gap: 8rpx;
}

.nav-icon {
	width: 48rpx;
	height: 48rpx;
}

.nav-text {
	font-size: 20rpx;
	color: #999;
}

.nav-item.active .nav-text {
	color: #FF6B01;
}
</style>
