<template>
	<view class="charging-countdown-page">
		<!-- 自定义导航栏 -->
		<u-navbar 
			:is-back="true" 
			title="充电确认" 
			:background="{ backgroundColor: '#fff' }"
			:z-index="999"
			@leftClick="goBack"
		>
		</u-navbar>

		<!-- 页面内容 -->
		<view class="page-content">
			<!-- 上半部分：充电桩信息（半透明显示） -->
			<view class="station-info-overlay">
				<view class="station-card">
					<view class="status-circle">
						<text class="status-text">空闲</text>
					</view>
					<view class="station-info">
						<view class="station-title">{{stationInfo.name}}</view>
						<view class="station-code-row">
							<text class="station-code">充电枪号{{stationInfo.code}}</text>
							<u-icon name="copy" size="16" color="#999"></u-icon>
						</view>
						<text class="station-address">{{stationInfo.address}}</text>
					</view>
				</view>

				<view class="power-parking-info">
					<view class="info-item">
						<text class="info-label">充电桩功率</text>
						<view class="info-value-row">
							<text class="info-value">{{stationInfo.power}}</text>
							<view class="power-badge">快</view>
						</view>
					</view>
					<view class="info-item">
						<text class="info-label">停车费（临牌不减免）</text>
						<text class="parking-benefit">减免 {{stationInfo.parkingDiscount}}</text>
					</view>
				</view>

				<view class="my-car-section">
					<view class="section-title">{{carText}}</view>
					<view class="car-selector" v-if="selectedCar">
						<text class="car-number">{{selectedCar}}</text>
					</view>
					<view class="add-car-hint" v-else>
						<text class="hint-text">添加车牌，可享受停车费减免</text>
						<view class="add-icon">+</view>
					</view>
				</view>

				<view class="charging-fee-section">
					<view class="section-title">充电费用</view>
					<view class="current-period">
						<text class="period-label">当前时段</text>
					</view>
					<view class="fee-display">
						<text class="fee-symbol">¥</text>
						<text class="fee-value">{{currentFee}}</text>
						<text class="fee-unit">/度</text>
					</view>
					<view class="fee-schedule">
						<text class="schedule-info">({{currentPeriod}})</text>
					</view>
				</view>
			</view>

			<!-- 底部弹出的倒计时组件 -->
			<view class="countdown-popup" :class="{ show: showCountdown }">
				<view class="popup-content">
					<view class="countdown-circle">
						<text class="countdown-text">{{timeData}}S</text>
					</view>
					<view class="countdown-title">开始充电中...</view>
					<view class="countdown-desc">
						请勿关闭本页面，预计60秒内开始充电~<br/>
						属于高压设备出于安全考虑电压电流将逐渐增加，<br/>
						请耐心等待开始充电~
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			stationInfo: {
				name: '101号直流',
				code: '010448010400000302',
				address: '青岛劲松三路金泽供热站充电站',
				power: '180 kW',
				parkingDiscount: '2小时'
			},
			selectedCar: '',
			carText: '暂无爱车',
			currentFee: '0.68',
			currentPeriod: '17:00 - 20:00',
			timeData: 30,
			timer: null,
			showCountdown: false
		}
	},
	onLoad(options) {
		// 获取传递的参数
		if (options.stationData) {
			try {
				const stationData = JSON.parse(decodeURIComponent(options.stationData))
				this.stationInfo = Object.assign(this.stationInfo, stationData)
			} catch (e) {
				console.error('解析站点数据失败:', e)
			}
		}
		
		if (options.selectedCar) {
			this.selectedCar = decodeURIComponent(options.selectedCar)
			this.carText = '我的爱车'
		}
		
		// 页面加载后短暂延迟再显示倒计时弹窗
		setTimeout(() => {
			this.showCountdown = true
			this.startCountdown()
		}, 500)
	},
	onUnload() {
		if (this.timer) {
			clearInterval(this.timer)
		}
	},
	methods: {
		goBack() {
			if (this.timer) {
				clearInterval(this.timer)
			}
			uni.navigateBack()
		},
		startCountdown() {
			this.timer = setInterval(() => {
				this.timeData--
				if (this.timeData <= 0) {
					clearInterval(this.timer)
					this.timer = null
					// 倒计时结束，跳转到充电中页面
					this.goToChargingPage()
				}
			}, 1000)
		},
		goToChargingPage() {
			// 这里应该跳转到充电中页面
			uni.showToast({
				title: '充电启动成功',
				icon: 'success',
				duration: 2000
			})
			
			setTimeout(() => {
				// 返回首页或跳转到充电管理页面
				uni.navigateBack({
					delta: 2
				})
			}, 2000)
		}
	}
}
</script>

<style scoped>
.charging-countdown-page {
	background-color: #f5f5f5;
	min-height: 100vh;
	position: relative;
}

.page-content {
	position: relative;
	min-height: 100vh;
}

/* 正常显示的背景内容 */
.station-info-overlay {
	padding: 20rpx;
	padding-bottom: 120rpx;
}

/* 充电桩信息卡片 */
.station-card {
	
	margin-bottom: 20rpx;
	border-radius: 16rpx;
	padding: 30rpx;
	display: flex;
	align-items: flex-start;
	gap: 24rpx;
	background-image: url('/static/img/shap.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
}

.status-circle {
	width: 100rpx;
	height: 100rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, #7ED321, #4CD964);
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
}

.status-text {
	color: white;
	font-size: 28rpx;
	font-weight: bold;
}

.station-info {
	flex: 1;
}

.station-title {
	font-size: 36rpx;
	color: #333;
	font-weight: bold;
	margin-bottom: 12rpx;
}

.station-code-row {
	display: flex;
	align-items: center;
	gap: 12rpx;
	margin-bottom: 8rpx;
}

.station-code {
	font-size: 28rpx;
	color: #666;
}

.station-address {
	font-size: 26rpx;
	color: #999;
}

/* 功率和停车费信息 */
.power-parking-info {
	background-color: #fff;
	margin-bottom: 20rpx;
	border-radius: 16rpx;
	padding: 30rpx;
}

.info-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20rpx 0;
}

.info-item:first-child {
	border-bottom: 1rpx solid #f0f0f0;
}

.info-label {
	font-size: 30rpx;
	color: #333;
}

.info-value-row {
	display: flex;
	align-items: center;
	gap: 12rpx;
}

.info-value {
	font-size: 30rpx;
	color: #333;
	font-weight: bold;
}

.power-badge {
	background-color: #FF9500;
	color: white;
	padding: 4rpx 12rpx;
	border-radius: 8rpx;
	font-size: 22rpx;
	font-weight: bold;
}

.parking-benefit {
	font-size: 30rpx;
	color: #333;
	font-weight: bold;
}

/* 我的爱车部分 */
.my-car-section {
	background-color: #fff;
	margin-bottom: 20rpx;
	border-radius: 16rpx;
	padding: 0 30rpx;
	height: 86rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.section-title {
	font-size: 30rpx;
	color: #333;
	font-weight: 400;
}

.car-selector {
	display: flex;
	align-items: center;
	gap: 12rpx;
}

.car-number {
	font-size: 24rpx;
	color: #72727d;
}

.add-car-hint {
	display: flex;
	align-items: center;
	gap: 12rpx;
}

.hint-text {
	font-size: 24rpx;
	color: #72727d;
}

.add-icon {
	width: 40rpx;
	height: 40rpx;
	border-radius: 50%;
	background-color: #333;
	color: white;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 24rpx;
}

/* 充电费用部分 */
.charging-fee-section {
	background-color: #fff;
	border-radius: 16rpx;
	padding: 0 30rpx 30rpx 30rpx;
}

.charging-fee-section .section-title {
	height: 78rpx;
	line-height: 78rpx;
	border-bottom: none;
}

.current-period {
	margin-bottom: 16rpx;
}

.period-label {
	font-size: 24rpx;
	color: #333;
}

.fee-display {
	margin-bottom: 16rpx;
}

.fee-symbol,
.fee-unit {
	font-size: 24rpx;
	color: #ff6b35;
}

.fee-value {
	font-size: 48rpx;
	font-weight: 600;
	color: #ff6b35;
}

.fee-schedule {
	display: flex;
	justify-content: flex-start;
	align-items: center;
}

.schedule-info {
	font-size: 24rpx;
	color: #999;
}

/* 底部弹出的倒计时组件 */
.countdown-popup {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	height: 50vh;
	background-color: white;
	border-radius: 30rpx 30rpx 0 0;
	box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
	transform: translateY(100%);
	transition: transform 0.3s ease-in-out;
	z-index: 999;
}

.countdown-popup.show {
	transform: translateY(0);
}

.popup-content {
	text-align: center;
	padding: 75rpx 60rpx 100rpx;
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.countdown-circle {
	width: 250rpx;
	height: 250rpx;
	border-radius: 50%;
	background: conic-gradient(#4CD964 0deg, #4CD964 270deg, #e0e0e0 270deg, #e0e0e0 360deg);
	display: flex;
	align-items: center;
	justify-content: center;
	margin: 0 auto 32rpx;
	position: relative;
}

.countdown-circle::before {
	content: '';
	position: absolute;
	width: 200rpx;
	height: 200rpx;
	background: white;
	border-radius: 50%;
}

.countdown-text {
	font-size: 50rpx;
	font-weight: 600;
	color: #4CD964;
	position: relative;
	z-index: 1;
}

.countdown-title {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	margin-bottom: 60rpx;
}

.countdown-desc {
	font-size: 26rpx;
	color: #72727d;
	line-height: 50rpx;
}
</style>
