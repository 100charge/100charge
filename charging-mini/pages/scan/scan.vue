<template>
	<view class="scan-container">
		<!-- 状态栏 -->
		<view class="status-bar">
			<view class="status-left">
				<text class="time">10:30</text>
				<text class="icon">✈️</text>
			</view>
			<view class="status-right">
				<text class="signal">📶</text>
				<text class="wifi">📶</text>
				<text class="battery">🔋</text>
			</view>
		</view>

		<!-- 头部 -->
		<view class="header">
			<view class="back-btn" @click="goBack">
				<text class="back-icon">←</text>
			</view>
			<text class="header-title">扫码充电</text>
			<view class="header-right">
				<text class="help-icon">?</text>
			</view>
		</view>

		<!-- 扫码区域 -->
		<view class="scan-area">
			<view class="scan-frame">
				<view class="scan-corner top-left"></view>
				<view class="scan-corner top-right"></view>
				<view class="scan-corner bottom-left"></view>
				<view class="scan-corner bottom-right"></view>
				<view class="scan-line"></view>
			</view>
			<text class="scan-tip">将二维码放入框内，即可自动扫描</text>
		</view>

		<!-- 手动输入区域 -->
		<view class="manual-input">
			<text class="manual-title">手动输入充电桩编号</text>
			<view class="input-group">
				<input class="station-input" placeholder="请输入充电桩编号" v-model="stationCode" />
				<button class="confirm-btn" @click="confirmStation">确认</button>
			</view>
		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				stationCode: ''
			}
		},
		onLoad() {
			// 隐藏原生tabBar，使用自定义底部导航
			uni.hideTabBar()
			// 页面加载时启动扫码
			this.startScan()
		},
		methods: {
			// 返回上一页
			goBack() {
				uni.navigateBack()
			},
			// 开始扫码
			startScan() {
				// 这里可以调用扫码API
				console.log('开始扫码')
			},
			// 确认充电桩
			confirmStation() {
				if (!this.stationCode) {
					uni.showToast({
						title: '请输入充电桩编号',
						icon: 'none'
					})
					return
				}
				
				// 跳转到充电详情页
				uni.navigateTo({
					url: `/pages/charging/charging?code=${this.stationCode}`
				})
			}
		}
	}
</script>

<style scoped>
	.scan-container {
		background-color: #000;
		min-height: 100vh;
		position: relative;
	}

	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 30rpx;
		background-color: transparent;
		font-size: 24rpx;
		color: white;
	}

	.status-left, .status-right {
		display: flex;
		align-items: center;
		gap: 10rpx;
	}

	/* 头部 */
	.header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx 30rpx;
		background-color: transparent;
	}

	.back-btn {
		width: 60rpx;
		height: 60rpx;
		background-color: rgba(255, 255, 255, 0.2);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.back-icon {
		font-size: 32rpx;
		color: white;
	}

	.header-title {
		font-size: 32rpx;
		color: white;
		font-weight: bold;
	}

	.header-right {
		width: 60rpx;
		height: 60rpx;
		background-color: rgba(255, 255, 255, 0.2);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.help-icon {
		font-size: 32rpx;
		color: white;
	}

	/* 扫码区域 */
	.scan-area {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		flex: 1;
		padding: 100rpx 0;
	}

	.scan-frame {
		width: 500rpx;
		height: 500rpx;
		position: relative;
		margin-bottom: 60rpx;
	}

	.scan-corner {
		position: absolute;
		width: 60rpx;
		height: 60rpx;
		border: 6rpx solid #4a90e2;
	}

	.top-left {
		top: 0;
		left: 0;
		border-right: none;
		border-bottom: none;
	}

	.top-right {
		top: 0;
		right: 0;
		border-left: none;
		border-bottom: none;
	}

	.bottom-left {
		bottom: 0;
		left: 0;
		border-right: none;
		border-top: none;
	}

	.bottom-right {
		bottom: 0;
		right: 0;
		border-left: none;
		border-top: none;
	}

	.scan-line {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 4rpx;
		background: linear-gradient(90deg, transparent, #4a90e2, transparent);
		animation: scan 2s linear infinite;
	}

	@keyframes scan {
		0% {
			top: 0;
		}
		100% {
			top: 100%;
		}
	}

	.scan-tip {
		color: white;
		font-size: 28rpx;
		text-align: center;
	}

	/* 手动输入区域 */
	.manual-input {
		background-color: white;
		margin: 40rpx 30rpx;
		border-radius: 20rpx;
		padding: 40rpx 30rpx;
	}

	.manual-title {
		display: block;
		font-size: 32rpx;
		color: #333;
		font-weight: bold;
		margin-bottom: 30rpx;
		text-align: center;
	}

	.input-group {
		display: flex;
		gap: 20rpx;
	}

	.station-input {
		flex: 1;
		height: 80rpx;
		border: 2rpx solid #eee;
		border-radius: 10rpx;
		padding: 0 20rpx;
		font-size: 28rpx;
	}

	.confirm-btn {
		background-color: #4a90e2;
		color: white;
		border: none;
		padding: 0 40rpx;
		border-radius: 10rpx;
		font-size: 28rpx;
	}

</style> 