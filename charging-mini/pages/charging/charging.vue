<template>
	<view class="charging-container">
		<!-- 自定义导航栏 -->
		<u-navbar :is-back="true" title="充电确认" :background="{ backgroundColor: '#fff' }" :z-index="999"
			@leftClick="goBack">
		</u-navbar>

		<!-- 充电桩信息卡片 -->
		<view class="station-card">
			<view class="top">
				<!-- 充电桩状态圆圈 -->
				<view class="status-circle">
					<text class="status-text">空闲</text>
				</view>

				<!-- 充电桩详情 -->
				<view class="station-info">
					<view class="station-title">{{ stationInfo.name }}</view>
					<view class="station-code-row">
						<text class="station-code">充电枪号{{ stationInfo.code }}</text>
						<u-icon name="copy" size="16" color="#999"></u-icon>
					</view>
					<text class="station-address">{{ stationInfo.address }}</text>
				</view>
			</view>
			<view class="bottom">
				<view class="bottom-left">
					<view class="info-item">
						<text class="info-label">充电桩功率</text>
						<view class="info-value-row">
							<text class="info-value">{{ stationInfo.power }}</text>
							<image src="/static/img/kuai.png" class="badge-icon" mode="widthFix"></image>
						</view>
					</view>
				</view>
				<view class="bottom-divider"></view>
				<view class="bottom-right">
					<view class="info-item">
						<view class="info-label">停车费（临牌不减免）</view>
						<view class="parking-benefit">减免 <text class="parking-benefit-text">{{
							stationInfo.parkingDiscount }}</text></view>
					</view>
				</view>
			</view>
		</view>



		<!-- 我的爱车选择器 -->
		<view class="my-car-section">
			<view class="my-car-bg">
				<view class="section-title">我的爱车</view>
				<view class="car-selector" @click="selectCar">
					<text class="car-number">{{ selectedCar }}</text>
					<u-icon name="arrow-right" size="14" color="#999"></u-icon>
				</view>
			</view>
		</view>

		<!-- 充电费用 -->
		<view class="charging-fee-section">
			<view class="section-title">充电费用</view>
			<view class="current-period">
				<text class="period-label">当前时段</text>
			</view>
			<view class="fee-display">
				<text class="fee-symbol">¥</text>
				<text class="fee-value">{{ currentFee }}</text>
				<text class="fee-unit">/度</text>
			</view>
			<view class="fee-schedule">
				<text class="schedule-info">({{ currentPeriod }})</text>
				<view class="fee-details" @click="showFeeDetails">
					<text class="details-text">电价详情</text>
					<u-icon name="arrow-right" size="14" color="#999"></u-icon>
				</view>
			</view>
		</view>

		<!-- 支付方式 -->
		<view class="payment-section">
			<view class="section-title">支付方式</view>

			<!-- 微信支付分 -->
			<view class="payment-item" :class="{ active: selectedPayment === 'wechat' }"
				@click="selectPayment('wechat')">
				<view class="payment-info">
					<image class="payment-icon" src="/static/img/weichart.png" mode="heightFix"></image>
					<view class="payment-details">
						<text class="payment-name">微信支付分</text>
						<text class="payment-desc">先充后付</text>
					</view>
				</view>
				<view class="payment-radio" :class="{ checked: selectedPayment === 'wechat' }">
					<image v-if="selectedPayment === 'wechat'" src="/static/img/checked-fill-green.png"
						mode="aspectFit"></image>
					<image v-else src="/static/img/check-no.png" mode="aspectFit"></image>
				</view>
			</view>

			<!-- 余额支付 -->
			<view class="payment-item" :class="{ active: selectedPayment === 'balance' }"
				@click="selectPayment('balance')">
				<view class="payment-info">
					<image class="payment-icon" src="/static/img/yue.png" mode="heightFix"></image>
					<view class="payment-details">
						<text class="payment-name">余额支付</text>
						<view class="payment-desc-row">
							<text class="balance-amount">可用额度 {{ accountBalance }}元</text>
							<view class="recharge-link" @click.stop="recharge">
								<text class="recharge-text">充值余额</text>
								<u-icon name="arrow-right" size="10" color="#007AFF"></u-icon>
							</view>
						</view>
					</view>
				</view>
				<view class="payment-radio" :class="{ checked: selectedPayment === 'balance' }">
					<image v-if="selectedPayment === 'balance'" src="/static/img/checked-fill-green.png"
						mode="aspectFit"></image>
					<image v-else src="/static/img/check-no.png" mode="aspectFit"></image>
				</view>
			</view>
		</view>

		<!-- 底部开始充电按钮 -->
		<view class="bottom-action">
			<button class="start-charging-btn" @click="startCharging">开始充电</button>
		</view>

		<!-- 底部弹出的倒计时组件 -->
		<view class="countdown-popup" :class="{ show: showCountdown }">
			<view class="popup-content">
				<view class="countdown-circle">
					<image class="countdown-bg" src="/static/img/huan.png" mode="aspectFit"
						:style="{ transform: 'rotate(' + rotationAngle + 'deg)' }"></image>
					<text class="countdown-text">{{ timeData }}S</text>
				</view>
				<view class="countdown-title">开始充电中...</view>
				<view class="countdown-desc">
					请勿关闭本页面，预计60秒内开始充电~<br />
					属于高压设备出于安全考虑电压电流将逐渐增加，<br />
					请耐心等待开始充电~
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
			selectedCar: '鲁B·88888',
			currentFee: '0.68',
			currentPeriod: '17:00 - 20:00',
			selectedPayment: 'wechat',
			accountBalance: '0.00',
			isCharging: false,
			timer: null,
			showCountdown: false,
			timeData: 30,
			rotationAngle: 0
		}
	},
	onLoad(options) {
		// 获取充电桩编号
		if (options.code) {
			this.stationInfo.code = options.code
		}
	},
	onUnload() {
		// 清除定时器
		if (this.timer) {
			clearInterval(this.timer)
		}
	},
	methods: {
		// 返回上一页
		goBack() {
			uni.navigateBack()
		},
		// 选择车辆
		selectCar() {
			uni.navigateTo({
				url: '/pages/add-car/add-car'
			})
		},
		// 显示电价详情
		showFeeDetails() {
			uni.showToast({
				title: '电价详情功能待开发',
				icon: 'none'
			})
		},
		// 选择支付方式
		selectPayment(type) {
			this.selectedPayment = type
		},
		// 充值余额
		recharge() {
			uni.showToast({
				title: '充值功能待开发',
				icon: 'none'
			})
		},
		// 开始充电
		startCharging() {
			if (this.selectedPayment === 'balance' && parseFloat(this.accountBalance) <= 0) {
				uni.showModal({
					title: '余额不足',
					content: '当前余额不足，请充值后再试',
					confirmText: '去充值',
					success: (res) => {
						if (res.confirm) {
							this.recharge()
						}
					}
				})
				return
			}

			// 显示倒计时弹窗
			this.showCountdown = true
			this.timeData = 30
			this.rotationAngle = 0
			this.startCountdownTimer()
		},

		// 开始倒计时
		startCountdownTimer() {
			this.timer = setInterval(() => {
				this.timeData--
				// 计算旋转角度：30秒完成360度旋转
				this.rotationAngle = (30 - this.timeData) * 12 // 每秒旋转12度

				// 5秒后跳转到充电进行中页面
				if (this.timeData <= 25) { // 30-5=25，即5秒后
					clearInterval(this.timer)
					this.showCountdown = false
					uni.showToast({
						title: '充电启动成功',
						icon: 'success',
						duration: 1000
					})

					// 跳转到充电进行中页面
					setTimeout(() => {
						uni.redirectTo({
							url: '/subpkg/charging/charging-in-progress/charging-in-progress?orderNo=TEST123456&stationName=' + encodeURIComponent(this.stationInfo.name)
						})
					}, 1000)
				}
			}, 1000)
		}
	}
}
</script>

<style scoped lang="scss">
.charging-container {
	background-color: #f5f5f5;
	min-height: 100vh;
	padding-bottom: 120rpx;
}

/* 充电桩信息卡片 */
.station-card {
	margin: 20rpx;
	border-radius: 16rpx;
	padding: 30rpx;

	gap: 24rpx;
	background-image: url('/static/img/shap2.png');
	height: 340rpx;
	background-size: 100% 100%;
	background-position: center;
	background-repeat: no-repeat;

	.top {
		display: flex;
		align-items: flex-start;

		.status-circle {
			width: 140rpx;
			height: 140rpx;
			border-radius: 50%;
			border: 12rpx solid #CBF3D5;
			background-color: transparent;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-shrink: 0;
			position: relative;
		}

		.status-text {
			color: #4CD964;
			font-size: 28rpx;
			font-weight: bold;
			z-index: 2;
			position: relative;
		}

		.station-info {
			flex: 1;
			margin-left: 30rpx;
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



	}

	.bottom {
		display: flex;
		align-items: flex-start;
		margin-top: 2.5vh;

		.bottom-divider {
			width: 2rpx;
			height: 80rpx;
			background-color: #D1D1D1;
			margin-top: 20rpx;

		}

		.bottom-left,
		.bottom-right {
			flex: 1;

			.info-item {

				align-items: center;
				padding: 10rpx 0;

				.info-label {
					font-size: 24rpx;
					color: #808080;

				}

				.info-value-row {
					margin-top: 16rpx;
					display: flex;
					align-items: center;
					gap: 12rpx;

					.info-value {
						font-size: 30rpx;
						color: #333;
						font-weight: bold;
					}

					.badge-icon {
						width: 40rpx;
						
					}
				}

				.parking-benefit {
					margin-top: 16rpx;
					font-size: 24rpx;
					color: #333;
					font-weight: bold;

					.parking-benefit-text {
						font-size: 30rpx;
					}
				}
			}
		}

		.bottom-right {
			margin-left: 60rpx;
		}
	}
}





/* 我的爱车部分 */
.my-car-section {
	background-color: #fff;
	margin: 0 20rpx 20rpx;
	border-radius: 16rpx;
	padding: 0 30rpx;
	height: 86rpx;
	display: flex;
    align-items: center;
	.my-car-bg {
		background: linear-gradient(270deg, #FFFFFF 0%, #F9F9F9 100%);
		
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 100%;
		padding: 10rpx;
		margin-left: -10rpx;
		border-radius: 8px;
		padding-left: 20rpx;
		.section-title {
			
			font-size: 30rpx;
			color: #333;
			font-weight: 400;
			background: linear-gradient( 270deg, #FFFFFF 0%, #F9F9F9 100%);
			
		
		}

		.car-selector {
			display: flex;
			align-items: center;
			gap: 12rpx;

			.car-number {
				font-size: 24rpx;
				color: #72727d;
			}
		}
	}

}

/* 充电费用部分 */
.charging-fee-section {
	background-color: #fff;
	margin: 0 20rpx 20rpx;
	border-radius: 16rpx;
	padding: 22rpx 30rpx 30rpx 30rpx;

	.section-title {
		margin-left: -10rpx;
		background: linear-gradient( 270deg, #FFFFFF 0%, #F9F9F9 100%);
		border-radius: 8px;
		height: 78rpx;
		line-height: 78rpx;
		border-bottom: none;
		padding-left: 20rpx;
		font-size: 30rpx;
		font-weight: 400;
	}

	.current-period {
		margin-bottom: 16rpx;

		.period-label {
			font-size: 24rpx;
			color: #333;
		}
	}

	.fee-display {
		margin-bottom: 16rpx;

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
	}

	.fee-schedule {
		display: flex;
		justify-content: space-between;
		align-items: center;
		background: linear-gradient( 270deg, #FFFFFF 0%, #F9F9F9 100%);
			border-radius: 8px;
			padding: 10rpx;
			margin-left: -10rpx;
			padding-left: 20rpx;
		.schedule-info {
			font-size: 24rpx;
			color: #999;
		}

		.fee-details {
			display: flex;
			align-items: center;
			gap: 8rpx;
			
			.details-text {
				font-size: 20rpx;
				color: #72727d;
			}
		}
	}
}

/* 支付方式部分 */
.payment-section {
	background-color: #fff;
	margin: 0 20rpx 20rpx;
	border-radius: 16rpx;
	padding: 0 30rpx 30rpx 30rpx;

	.section-title {
		height: 78rpx;
		line-height: 78rpx;
		border-bottom: none;
	}

	.payment-item {
		width: 100%;
		height: 96rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16rpx;

		&:last-child {
			margin-bottom: 0;
		}

		.payment-info {
			display: flex;
			align-items: center;
			flex: 1;

			.payment-icon {
				width: 50rpx;
				height: 50rpx;
				margin-right: 16rpx;
			}

			.payment-details {
				flex: 1;

				.payment-name {
					font-size: 28rpx;
					color: #3d3d3d;
					display: block;
					margin-bottom: 6rpx;
				}

				.payment-desc {
					font-size: 24rpx;
					color: #72727d;
				}

				.payment-desc-row {
					display: flex;
					align-items: center;
					gap: 20rpx;

					.balance-amount {
						font-size: 24rpx;
						color: #72727d;
					}

					.recharge-link {
						display: flex;
						align-items: center;
						gap: 4rpx;

						.recharge-text {
							font-size: 20rpx;
							color: #007AFF;
						}
					}
				}
			}
		}

		.payment-radio {
			width: 32rpx;
			height: 32rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				width: 32rpx;
				height: 32rpx;
			}
		}
	}
}

/* 底部开始充电按钮 */
.bottom-action {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background-color: #fff;
	padding: 20rpx 30rpx 40rpx;
	box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);

	.start-charging-btn {
		width: 100%;
		height: 92rpx;
		background-color: #333;
		color: white;
		border: none;
		border-radius: 46rpx;
		font-size: 28rpx;
		font-weight: bold;
		line-height: 92rpx;
		text-align: center;
	}
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
	z-index: 1000;

	&.show {
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

		.countdown-circle {
			width: 250rpx;
			height: 250rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 0 auto 32rpx;
			position: relative;

			.countdown-bg {
				position: absolute;
				width: 100%;
				height: 100%;
				transition: transform 0.5s ease-in-out;
			}

			.countdown-text {
				font-size: 50rpx;
				font-weight: 600;
				color: #4CD964;
				position: relative;
				z-index: 1;
			}
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
	}
}
</style>