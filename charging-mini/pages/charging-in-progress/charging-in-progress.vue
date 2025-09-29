<template>
	<view class="charging-page">
		<!-- 充电动画背景 -->
		<view class="charging-animation-bg">
			<view class="charging-pulse"></view>
			<view class="charging-circle"></view>
		</view>
		
		<!-- 状态栏 -->
		<view class="status-bar">3:24</view>
		
		<!-- 导航栏 -->
		<view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<u-icon name="arrow-left" size="18" color="white"></u-icon>
			</view>
			<view class="nav-title">充电中</view>
			<view class="nav-right">
				<u-icon name="more-dot-fill" size="18" color="white"></u-icon>
				<view class="charging-indicator"></view>
			</view>
		</view>

		<!-- 上半部分：时间信息 -->
		<view class="time-section">
			<!-- 已充时长 -->
			<view class="charge-time">
				<text>已充时长</text>
			</view>

			<!-- 时间显示 -->
			<view class="time-display">
				<text class="time-text">{{formatTime(chargingDuration)}}</text>
			</view>

			<!-- 预计还有时间 -->
			<view class="estimate-time">
				<text>预计还有 </text>
				<text class="highlight">{{estimateMinutes}}</text>
				<text> 分钟充满</text>
			</view>
		</view>

		<!-- 中间部分：留空给背景gif显示 -->


		<!-- 底部白色卡片区域 -->
		<view class="bottom-cards">
			<!-- 充电站信息卡片 -->
			<view class="station-card">
				<view class="station-name">{{stationName}}</view>
				<view class="station-details">
					<text>充电桩 {{deviceNo}}</text>
					<text style="margin-left: 40rpx;">充电枪号 {{gunNo}}</text>
				</view>
				
				<!-- 充电数据 -->
				<view class="charging-stats">
					<view class="stat-item">
						<view class="stat-value">{{chargedPower.toFixed(2)}}</view>
						<view class="stat-label">已充电量（度）</view>
					</view>
					<view class="stat-divider"></view>
					<view class="stat-item">
						<view class="stat-value">{{chargingAmount.toFixed(2)}}</view>
						<view class="stat-label">充电费用（元）</view>
					</view>
				</view>
			</view>

			<!-- 实时功率卡片 -->
			<view class="power-card">
				<view class="power-header">
					<text class="power-title">实时功率</text>
					<view class="power-link">
						<text class="link-text">充电曲线</text>
						<u-icon name="arrow-right" size="12" color="#999"></u-icon>
					</view>
				</view>
				<view class="power-content">
					<text class="power-main">{{realTimePower.toFixed(2)}}kW</text>
					<text class="power-sub">{{voltage.toFixed(2)}}V/{{current.toFixed(2)}}A</text>
				</view>
			</view>
		</view>

		<!-- 底部结束充电按钮 -->
		<view class="bottom-action">
			<view class="end-charging-btn" @click="endCharging">结束充电</view>
		</view>

		<!-- 结束充电倒计时弹窗 -->
		<view class="end-countdown-popup" :class="{ show: showEndCountdown }">
			<view class="end-popup-content">
				<view class="end-countdown-circle">
					<image class="end-countdown-bg" src="/static/img/guanbi.png" mode="aspectFit" :style="{ transform: 'rotate(' + endRotationAngle + 'deg)' }"></image>
					<text class="end-countdown-text">{{endTimeData}}S</text>
				</view>
				<view class="end-countdown-title">请勿关闭本页面，预计60秒内结束充电桩~</view>
				<view class="end-countdown-desc">
					属于高压设备出于安全考虑电压电流将逐渐降为零<br/>
					请耐心等待最终结算费用~
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			orderNo: '',
			stationName: '青岛劲松三路金泽供热站充电站',
			deviceNo: '001',
			gunNo: '370203027411O1',
			chargingDuration: 0, // 充电时长(秒)
			estimateMinutes: 5, // 预计剩余分钟
			batteryPercent: 16, // 电池百分比
			chargingAmount: 1.880, // 充电费用
			chargedPower: 8.88, // 已充电量
			realTimePower: 0, // 实时功率
			voltage: 236.30, // 电压
			current: 0, // 电流
			timer: null,
			showEndCountdown: false, // 显示结束充电倒计时
			endTimeData: 30, // 结束充电倒计时秒数
			endRotationAngle: 0, // 结束充电旋转角度
			endTimer: null // 结束充电计时器
		}
	},
	onLoad(options) {
		if (options.orderNo) {
			this.orderNo = options.orderNo
		}
		if (options.stationName) {
			this.stationName = decodeURIComponent(options.stationName)
		}
		
		// 开始计时器模拟充电过程
		this.startChargingTimer()
	},
	onUnload() {
		if (this.timer) {
			clearInterval(this.timer)
		}
		if (this.endTimer) {
			clearInterval(this.endTimer)
		}
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		// 格式化时间显示
		formatTime(seconds) {
			const hours = Math.floor(seconds / 3600)
			const minutes = Math.floor((seconds % 3600) / 60)
			const secs = seconds % 60
			return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
		},
		// 开始充电计时器
		startChargingTimer() {
			// 从8分16秒开始计时
			this.chargingDuration = 8 * 60 + 16
			
			this.timer = setInterval(() => {
				this.chargingDuration++
				
				// 模拟数据变化
				if (this.chargingDuration % 30 === 0) { // 每30秒更新一次数据
					this.chargedPower += 0.1
					this.chargingAmount += 0.05
					if (this.batteryPercent < 100) {
						this.batteryPercent += 1
					}
					if (this.estimateMinutes > 0) {
						this.estimateMinutes -= 1
					}
				}
			}, 1000)
		},
		// 结束充电
		endCharging() {
			uni.showModal({
				title: '确认结束充电',
				content: '确定要结束本次充电吗？',
				confirmText: '确认结束',
				cancelText: '继续充电',
				success: (res) => {
					if (res.confirm) {
						// 停止充电计时器
						if (this.timer) {
							clearInterval(this.timer)
						}
						
						// 显示结束充电倒计时弹窗
						this.showEndCountdown = true
						this.endTimeData = 5
						this.endRotationAngle = 0
						this.startEndCountdownTimer()
					}
				}
			})
		},
		
		// 开始结束充电倒计时
		startEndCountdownTimer() {
			this.endTimer = setInterval(() => {
				this.endTimeData--
				// 计算旋转角度：5秒完成360度旋转
				this.endRotationAngle = (5 - this.endTimeData) * 72 // 每秒旋转72度
				
				if (this.endTimeData <= 0) {
					clearInterval(this.endTimer)
					this.showEndCountdown = false
					
					uni.showToast({
						title: '充电结束成功',
						icon: 'success',
						duration: 1000
					})
					
					// 跳转到结算页面
					setTimeout(() => {
						uni.redirectTo({
							url: '/pages/charging-settlement/charging-settlement?orderNo=' + this.orderNo
						})
					}, 1000)
				}
			}, 1000)
		}
	}
}
</script>

<style scoped>
page {
	background: transparent;
}

.charging-page {
	min-height: 100vh;
	position: relative;
	padding-bottom: 200rpx;
	background: linear-gradient(180deg, #a1a4ad 0%, #FFFFFF 100%);
}

/* gif 动画 */
.bg-gif {
	width: 100%;
	height: auto;
	display: block;
	margin: 0 auto;
	
}

/* 状态栏 */
.status-bar {
	position: fixed;
	top: 0;
	left: 50%;
	transform: translateX(-50%);
	color: white;
	font-size: 28rpx;
	font-weight: bold;
	padding: 20rpx 0;
	z-index: 1000;
}

/* 导航栏 */
.nav-bar {
	position: fixed;
	top: 88rpx;
	left: 0;
	right: 0;
	height: 88rpx;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 40rpx;
	z-index: 999;
}

.nav-left {
	width: 60rpx;
	height: 60rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.nav-title {
	color: white;
	font-size: 36rpx;
	font-weight: 500;
}

.nav-right {
	display: flex;
	align-items: center;
	gap: 24rpx;
}

.charging-indicator {
	width: 20rpx;
	height: 20rpx;
	border-radius: 50%;
	background-color: #4CD964;
	position: relative;
}

.charging-indicator::after {
	content: '';
	position: absolute;
	width: 100%;
	height: 100%;
	border-radius: 50%;
	background-color: #4CD964;
	animation: pulse 1.5s infinite;
}

@keyframes pulse {
	0% {
		transform: scale(1);
		opacity: 1;
	}
	100% {
		transform: scale(2);
		opacity: 0;
	}
}

/* 上半部分：时间信息 */
.time-section {
	position: absolute;
	top: 180rpx;
	left: 0;
	right: 0;
	text-align: center;
	z-index: 10;
	padding: 0 40rpx;
}

.charge-time {
	color: white;
	font-size: 22rpx;
	margin-bottom: 20rpx;	
}

.time-display {
	margin-bottom: 24rpx;
}

.time-text {
	font-size: 80rpx;
	font-weight: bold;
	color: white;
	letter-spacing: 8rpx;
	text-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.3);
}

.estimate-time {
	color: white;
	font-size: 26rpx;
	margin-bottom: 0;
}

.highlight {
	color: #43d0a3;
	font-weight: bold;
}

/* 中间汽车区域 */
.car-section {
	height: 400rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 40rpx;
	padding: 0 40rpx;
}



/* 底部卡片区域 */
.bottom-cards {
	margin-top: 40rpx;
	padding: 20rpx;
	
	position: relative;
}

/* 充电站信息卡片 */
.station-card {
	background-color: white;
	background-image: url('/static/img/shap.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	border-radius: 16rpx;
	padding: 32rpx 24rpx;
	margin: 0 20rpx 20rpx 20rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
	position: relative;
	overflow: hidden;
}

.station-name {
	font-size: 28rpx;
	color: #333;
	font-weight: 600;
	margin-bottom: 12rpx;
	text-align: center;
	line-height: 1.4;
}

.station-details {
	font-size: 22rpx;
	color: #666;
	text-align: center;
	margin-bottom: 32rpx;
	line-height: 1.3;
}

/* 充电统计数据 */
.charging-stats {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 0 20rpx;
}

.stat-item {
	text-align: center;
	flex: 1;
	position: relative;
}

.stat-value {
	font-size: 44rpx;
	font-weight: 600;
	color: #333;
	margin-bottom: 8rpx;
	letter-spacing: 1rpx;
}

.stat-label {
	font-size: 22rpx;
	color: #666;
	line-height: 1.2;
}

.stat-divider {
	width: 1rpx;
	height: 60rpx;
	background-color: #e5e5e5;
	margin: 0 24rpx;
}

/* 实时功率卡片 */
.power-card {
	background-color: white;
	border-radius: 24rpx;
	padding: 30rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.power-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 24rpx;
}

.power-title {
	font-size: 28rpx;
	color: #333;
	font-weight: 500;
}

.power-link {
	display: flex;
	align-items: center;
	gap: 8rpx;
}

.link-text {
	font-size: 24rpx;
	color: #999;
}

.power-content {
	display: flex;
	align-items: baseline;
	gap: 30rpx;
}

.power-main {
	font-size: 48rpx;
	font-weight: bold;
	color: #333;
}

.power-sub {
	font-size: 32rpx;
	color: #666;
}

/* 底部操作按钮 */
.bottom-action {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;

	padding: 30rpx;
	
}

.end-charging-btn {
	width: 100%;
	height: 88rpx;
	background-color: #333;
	color: white;
	border: none;
	border-radius: 44rpx;
	font-size: 32rpx;
	font-weight: bold;
	line-height: 88rpx;
	text-align: center;
}

/* 结束充电倒计时弹窗 */
.end-countdown-popup {
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
	z-index: 1000;
}

.end-countdown-popup.show {
	transform: translateY(0);
}

.end-popup-content {
	text-align: center;
	padding: 75rpx 60rpx 100rpx;
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.end-countdown-circle {
	width: 250rpx;
	height: 250rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin: 0 auto 32rpx;
	position: relative;
}

.end-countdown-bg {
	position: absolute;
	width: 100%;
	height: 100%;
	transition: transform 0.5s ease-in-out;
}

.end-countdown-text {
	font-size: 50rpx;
	font-weight: 600;
	color: #ff6b35;
	position: relative;
	z-index: 1;
}

.end-countdown-title {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	margin-bottom: 60rpx;
}

.end-countdown-desc {
	font-size: 26rpx;
	color: #72727d;
	line-height: 50rpx;
}
</style>
