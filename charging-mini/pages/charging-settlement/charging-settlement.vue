<template>
	<view class="settlement-page">
		<!-- 自定义导航栏 -->
		<u-navbar 
			:is-back="true" 
			title="支付账单" 
			:background="{ backgroundColor: '#f4f4f4' }"
			:z-index="999"
			@leftClick="goBack"
		>
		</u-navbar>

		<!-- 标题状态 -->
		<view class="title-box">
			<view class="status-title" :class="{ completed: orderInfo.status == 2 }">
				<text v-if="orderInfo.status == 2">订单已完成</text>
				<text v-else>订单待支付</text>
			</view>
			<view class="tip">
				<text v-if="orderInfo.status == 2">感谢使用，期待再次充电</text>
				<text v-else>您启动充电前，已选择{{orderInfo.paymentDesc || '微信支付分'}}支付</text>
			</view>
		</view>

		<!-- 停车费提示 -->
		<view class="parking-notice" v-if="orderInfo.status == 2">
			<view class="notice-content">
				<image class="parking-icon" src="/static/img/blueP.png" mode="heightFix"></image>
				<view class="parking-info">
					<text class="parking-title">停车费减免2小时</text>
					<text class="car-number">鲁B·8888888</text>
				</view>
			</view>
			<view class="parking-actions">
				<text class="detail-text">详情</text>
				<u-icon name="arrow-right" size="12" color="#999"></u-icon>
			</view>
		</view>
		<view class="parking-notice" v-else>
			<view class="notice-content">
				<image class="parking-icon" src="/static/img/blueP.png" mode="heightFix"></image>
				<text class="notice-text">停车费未减免</text>
			</view>
			<u-icon name="plus" size="20" color="#333"></u-icon>
		</view>

		<!-- 修改车牌提示 -->
		<view class="parking-tip" v-if="orderInfo.status == 2">
			<text class="tip-text">* 如果修改车牌，需重新启动充电才能减免</text>
			<view class="modify-car">
				<text class="modify-text">修改车牌</text>
				<u-icon name="arrow-right" size="10" color="#007AFF"></u-icon>
			</view>
		</view>

		<!-- 订单明细 - 订单完成状态 -->
		<view class="detail-card" v-if="orderInfo.status == 2">
			<view class="detail-header">
				<view class="station-title" @click="toStation">
					<text class="station-name">{{orderInfo.stationName}}</text>
					<u-icon name="arrow-right" size="14" color="#999"></u-icon>
				</view>
			</view>

			<view class="detail-item main-amount">
				<text class="item-label">实付总金额</text>
				<view class="item-value-price">
					<text class="price-symbol">¥</text>
					<text class="price-amount">{{orderInfo.realAmount || finalAmount}}</text>
				</view>
			</view>

			<template v-if="showDetailAll">
				<view class="detail-item">
					<text class="item-label">电费</text>
					<text class="item-value">¥{{orderInfo.chargeFee}}</text>
				</view>

				<view class="detail-item">
					<text class="item-label">服务费</text>
					<text class="item-value">¥{{orderInfo.serviceFee}}</text>
				</view>

				<view class="divider"></view>

				<view class="order-info-title">订单信息</view>

				<view class="detail-item order-item">
					<text class="item-label">订单编号</text>
					<view class="item-value-with-copy">
						<text class="copy-btn" @click="copyText(orderInfo.tradeNo)">复制</text>
						<text class="item-value">{{orderInfo.tradeNo}}</text>
					</view>
				</view>

				<view class="detail-item order-item">
					<text class="item-label">电桩编号</text>
					<view class="item-value-with-copy">
						<text class="copy-btn" @click="copyText(orderInfo.deviceNo + '-' + orderInfo.gunNo)">复制</text>
						<text class="item-value">{{orderInfo.deviceNo}}-{{orderInfo.gunNo}}</text>
					</view>
				</view>

				<view class="detail-item order-item">
					<text class="item-label">开始充电时间</text>
					<text class="item-value">{{orderInfo.startTime}}</text>
				</view>

				<view class="detail-item order-item">
					<text class="item-label">结束充电时间</text>
					<text class="item-value">{{orderInfo.endTime}}</text>
				</view>

				<view class="detail-item order-item">
					<text class="item-label">充电度数</text>
					<text class="item-value">{{orderInfo.totalPower}}度</text>
				</view>

				<view class="detail-item order-item">
					<text class="item-label">充电方式</text>
					<text class="item-value">快充</text>
				</view>
			</template>

			<view class="expand-btn" @click="showDetailAll = !showDetailAll">
				<text class="expand-text">{{showDetailAll ? '收起' : '展开全部'}}</text>
				<u-icon :name="showDetailAll ? 'arrow-up' : 'arrow-down'" size="12" color="#999"></u-icon>
			</view>
		</view>

		<!-- 订单明细 - 待支付状态 -->
		<view class="detail-card" v-else>
			<view class="detail-header">
				<text class="detail-title">订单明细</text>
				<text class="station-name">{{orderInfo.stationName}}</text>
			</view>

			<view class="detail-item">
				<text class="item-label">充电度数</text>
				<text class="item-value">{{orderInfo.totalPower}} 度</text>
			</view>

			<view class="detail-item">
				<text class="item-label">电费</text>
				<text class="item-value">¥{{orderInfo.chargeFee}}</text>
			</view>

			<view class="detail-item">
				<text class="item-label">服务费</text>
				<text class="item-value">¥{{orderInfo.serviceFee}}</text>
			</view>

			<view class="detail-item">
				<text class="item-label">应付金额</text>
				<view class="item-value-price">
					<text class="price-symbol">¥</text>
					<text class="price-amount">{{orderInfo.totalAmount}}</text>
				</view>
			</view>

			<view class="divider"></view>

			<view class="detail-item">
				<text class="item-label">充电时段</text>
				<text class="item-value">{{chargingPeriod}}</text>
			</view>

			<view class="detail-item">
				<text class="item-label">充电方式</text>
				<text class="item-value">快充</text>
			</view>

			<view class="expand-btn" @click="showDetailAll = !showDetailAll">
				<text class="expand-text">{{showDetailAll ? '收起' : '展开全部'}}</text>
				<u-icon :name="showDetailAll ? 'arrow-up' : 'arrow-down'" size="12" color="#999"></u-icon>
			</view>
		</view>

		<!-- 优惠券 - 仅在待支付状态显示 -->
		<view class="coupon-card" v-if="orderInfo.status != 2" @click="handleCouponClick">
			<text class="coupon-title">优惠券</text>
			<view class="coupon-value">
				<text class="coupon-text">{{selectedCoupon.id ? '减免' + couponAmount + '元' : '减免2元'}}</text>
				<u-icon name="arrow-right" size="14" color="#999"></u-icon>
			</view>
		</view>

		<!-- 底部支付区域 - 仅在待支付状态显示 -->
		<view class="bottom-action" v-if="orderInfo.status != 2">
			<view class="payment-amount">
				<text class="amount-label">应付</text>
				<text class="amount-symbol">¥</text>
				<text class="amount-value">{{finalAmount}}</text>
			</view>
			<view class="pay-btn" @click="confirmPayment">确认支付</view>
		</view>

		<!-- 优惠券选择弹窗 -->
		<view class="popup-mask" v-if="showCouponList" @click="showCouponList = false">
			<view class="popup-container" @click.stop>
			<view class="coupon-popup">
				<view class="coupon-header">
					<text class="popup-title">选择优惠券</text>
					<u-icon name="close" size="20" color="#999" @click="showCouponList = false"></u-icon>
				</view>
				<view class="coupon-list">
					<!-- 可用优惠券 -->
					<view class="coupon-item-card" 
						v-for="(coupon, index) in availableCoupons" 
						:key="coupon.id"
						:class="{ selected: selectedCoupon.id === coupon.id }"
						@click="selectCoupon(coupon)">
						<view class="coupon-item-bg">
							<view class="coupon-left">
								<view class="price-section">
									<text class="currency" v-if="coupon.couponType !== 1">¥</text>
									<text class="amount">{{ coupon.couponType === 1 ? coupon.discountPercentage : coupon.giftAmount }}</text>
									<text class="currency" v-if="coupon.couponType === 1">折</text>
								</view>
								<view class="condition" v-if="coupon.couponType === 0">
									满{{ coupon.totalAmount }}可用
								</view>
								<view class="condition" v-else>
									无门槛
								</view>
							</view>
							<view class="coupon-right">
								<view class="coupon-info">
									<view class="coupon-title">{{ coupon.name }}</view>
									<view class="coupon-desc">{{ coupon.applicableScopeDesc }}</view>
									<view class="coupon-time">{{ coupon.expireTime }} 前到期</view>
								</view>
								<view class="coupon-status">
									<u-icon name="checkmark-circle-fill" 
										:color="selectedCoupon.id === coupon.id ? '#ff6b01' : '#ddd'" 
										size="24"></u-icon>
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="coupon-footer">
					<view class="footer-btn" @click="clearCoupon">
						暂不使用优惠券
					</view>
					<view class="footer-btn primary" @click="confirmCoupon">
						确认使用
					</view>
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
			orderNo: '',
			orderInfo: {
				stationName: '青岛劲松三路金泽供热站充电站',
				totalPower: '18.88',
				chargeFee: '0.34',
				serviceFee: '0.00',
				totalAmount: '0.34',
				paymentDesc: '微信支付分',
				status: 1 // 添加这一行：1表示待支付，2表示已完成
			},
			chargingPeriod: '13:00-14:00',
			showDetailAll: false,
			showCouponList: false,
			selectedCoupon: {},
			couponAmount: '2',
			finalAmount: '0.34',
			availableCoupons: [
				{
					id: 1,
					name: '充电专享券',
					couponType: 0, // 0: 满减券, 1: 折扣券
					giftAmount: 2.1,
					totalAmount: 0,
					applicableScopeDesc: '仅限新用户专享',
					expireTime: '2024-12-31 23:59:59',
					status: 0 // 0: 未使用, 1: 已使用, 2: 已过期
				},
				{
					id: 2,
					name: '充电专享券',
					couponType: 0,
					giftAmount: 1.5,
					totalAmount: 5,
					applicableScopeDesc: '限时充电专享',
					expireTime: '2024-12-25 23:59:59',
					status: 0
				},
				{
					id: 3,
					name: '新用户优惠券',
					couponType: 0,
					giftAmount: 5,
					totalAmount: 10,
					applicableScopeDesc: '新用户注册专享',
					expireTime: '2024-12-20 23:59:59',
					status: 0
				}
			]
		}
	},
	onLoad(options) {
		if (options.orderNo) {
			this.orderNo = options.orderNo
		}
		this.getOrderDetail()
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		toStation() {
			// 跳转到充电站详情
			uni.showToast({
				title: '跳转到充电站详情',
				icon: 'none'
			})
		},
		copyText(text) {
			uni.setClipboardData({
				data: text,
				success: () => {
					uni.showToast({
						title: '复制成功',
						icon: 'success'
					})
				}
			})
		},
		getOrderDetail() {
			// 这里应该调用API获取订单详情
			// 模拟数据已在data中设置
			this.calculateFinalAmount()
		},
		calculateFinalAmount() {
			// 计算最终支付金额
			let amount = parseFloat(this.orderInfo.totalAmount)
			if (this.selectedCoupon.id) {
				amount = Math.max(0, amount - parseFloat(this.couponAmount))
			}
			this.finalAmount = amount.toFixed(2)
		},
		confirmPayment() {
			uni.showModal({
				title: '确认支付',
				content: `确认支付 ¥${this.finalAmount} 吗？`,
				confirmText: '确认支付',
				cancelText: '取消',
				success: (res) => {
					if (res.confirm) {
						this.processPayment()
					}
				}
			})
		},
		processPayment() {
			uni.showLoading({
				title: '支付中...'
			})
			
			// 模拟支付过程
			setTimeout(() => {
				uni.hideLoading()
				uni.showToast({
					title: '支付成功',
					icon: 'success',
					duration: 1000
				})
				
				// 支付成功后切换到订单完成状态
				setTimeout(() => {
					this.orderInfo.status = 2 // 设置为已完成状态
					this.orderInfo.realAmount = this.finalAmount
					this.orderInfo.tradeNo = '578830928409238090-48'
					this.orderInfo.deviceNo = '578809238090'
					this.orderInfo.gunNo = '48'
					this.orderInfo.startTime = '2022-09-08 08:43:40'
					this.orderInfo.endTime = '2022-10-07 15:21:36'
					this.orderInfo.totalPower = '1.45'
					this.orderInfo.realDuration = '1小时38分钟'
					this.orderInfo.stopReason = '用户主动停止'
				}, 1000)
			}, 2000)
		},
		selectCoupon(coupon) {
			// 选择优惠券
			this.selectedCoupon = coupon
			this.couponAmount = coupon.giftAmount
		},
		clearCoupon() {
			// 清除选择的优惠券
			this.selectedCoupon = {}
			this.couponAmount = '0'
			this.calculateFinalAmount()
			this.showCouponList = false
		},
		confirmCoupon() {
			// 确认使用优惠券
			this.calculateFinalAmount()
			this.showCouponList = false
			uni.showToast({
				title: this.selectedCoupon.id ? '优惠券使用成功' : '已取消使用优惠券',
				icon: 'success'
			})
		},
		handleCouponClick() {
			console.log('优惠券点击事件触发')
			console.log('当前showCouponList值:', this.showCouponList)
			console.log('orderInfo.status:', this.orderInfo.status)
			this.showCouponList = true
			console.log('设置后showCouponList值:', this.showCouponList)
		}
	}
}
</script>

<style scoped>
.settlement-page {
	min-height: 100vh;
	background-color: #f4f4f4;
	padding-bottom: 200rpx;
}

/* 标题状态 */
.title-box {
	padding: 60rpx 40rpx 32rpx;
}

.status-title {
	font-size: 40rpx;
	color: #ff6b35;
	font-weight: bold;
	margin-bottom: 16rpx;
}

.status-title.completed {
	color: #333;
}

.tip {
	font-size: 24rpx;
	color: #999;
	line-height: 32rpx;
}

/* 停车费提示 */
.parking-notice {
	background-color: white;
	margin: 0 20rpx 20rpx;
	border-radius: 16rpx;
	padding: 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.notice-content {
	display: flex;
	align-items: center;
	gap: 16rpx;
}

.parking-icon {
	width: 40rpx;
	height: 40rpx;
}

.notice-text {
	font-size: 28rpx;
	color: #333;
}

.parking-info {
	display: flex;
	flex-direction: column;
}

.parking-title {
	font-size: 30rpx;
	color: #333;
	font-weight: bold;
	margin-bottom: 8rpx;
}

.car-number {
	font-size: 24rpx;
	color: #72727d;
}

.parking-actions {
	display: flex;
	align-items: center;
	gap: 8rpx;
}

.detail-text {
	font-size: 26rpx;
	color: #72727d;
}

/* 修改车牌提示 */
.parking-tip {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 0 50rpx 28rpx;
}

.tip-text {
	font-size: 24rpx;
	color: #ff6b35;
	flex: 1;
}

.modify-car {
	display: flex;
	align-items: center;
	gap: 8rpx;
	background-color: #fff0e6;
	padding: 8rpx 16rpx;
	border-radius: 10rpx;
}

.modify-text {
	font-size: 24rpx;
	color: #007AFF;
}

/* 订单明细卡片 */
.detail-card {
	background-color: white;
	margin: 0 20rpx 20rpx;
	border-radius: 16rpx;
	padding: 30rpx;
}

.detail-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 30rpx;
}

.detail-title {
	font-size: 30rpx;
	color: #333;
	font-weight: bold;
}

.station-name {
	font-size: 24rpx;
	color: #999;
}

.detail-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 28rpx;
}

.item-label {
	font-size: 24rpx;
	color: #333;
}

.item-value {
	font-size: 26rpx;
	color: #333;
}

.item-value-price {
	display: flex;
	align-items: baseline;
}

.price-symbol {
	font-size: 24rpx;
	color: #333;
	font-weight: bold;
}

.price-amount {
	font-size: 36rpx;
	color: #333;
	font-weight: bold;
	margin-left: 4rpx;
}

.divider {
	width: 100%;
	height: 1rpx;
	background-color: rgba(0, 0, 0, 0.08);
	margin: 20rpx 0;
}

.expand-btn {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 10rpx;
	padding: 20rpx 0;
	margin-top: 20rpx;
}

.expand-text {
	font-size: 24rpx;
	color: #999;
}

/* 订单完成状态特殊样式 */
.station-title {
	display: flex;
	align-items: center;
	justify-content: space-between;
	width: 100%;
}

.main-amount {
	margin-top: 24rpx;
}

.order-info-title {
	font-size: 24rpx;
	color: #333;
	margin: 20rpx 0;
	font-weight: bold;
}

.order-item {
	margin-bottom: 20rpx;
}

.item-value-with-copy {
	display: flex;
	align-items: center;
	gap: 14rpx;
}

.copy-btn {
	width: 64rpx;
	height: 32rpx;
	font-size: 20rpx;
	color: #72727d;
	background: #f8f8f8;
	line-height: 32rpx;
	text-align: center;
	border-radius: 27rpx;
}

/* 优惠券卡片 */
.coupon-card {
	background-color: white;
	margin: 0 20rpx 20rpx;
	border-radius: 16rpx;
	padding: 40rpx 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.coupon-title {
	font-size: 30rpx;
	color: #333;
	font-weight: bold;
}

.coupon-value {
	display: flex;
	align-items: center;
	gap: 8rpx;
}

.coupon-text {
	font-size: 26rpx;
	color: #72727d;
}

/* 弹窗遮罩和容器 */
.popup-mask {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.5);
	z-index: 9999;
	display: flex;
	align-items: flex-end;
}

.popup-container {
	width: 100%;
	background-color: white;
	border-radius: 32rpx 32rpx 0 0;
	animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
	from {
		transform: translateY(100%);
	}
	to {
		transform: translateY(0);
	}
}

/* 优惠券弹窗 */
.coupon-popup {
	padding: 40rpx 24rpx 60rpx;
	max-height: 80vh;
}

.coupon-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 40rpx;
	padding: 0 16rpx;
}

.popup-title {
	font-size: 32rpx;
	color: #333;
	font-weight: bold;
}

.coupon-list {
	margin-bottom: 40rpx;
	max-height: 60vh;
	overflow-y: auto;
}

/* 优惠券卡片样式 */
.coupon-item-card {
	margin-bottom: 20rpx;
	position: relative;
}

.coupon-item-card.selected .coupon-item-bg {
	border: 2rpx solid #ff6b01;
}

.coupon-item-bg {
	display: flex;
	background-image: url('/static/img/youhui.png');
	background-size: 100% 100%;
	background-position: center;
	background-repeat: no-repeat;
	height: 160rpx;
	position: relative;
	border-radius: 16rpx;
	overflow: hidden;
}

.coupon-item-card .coupon-left {
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

.coupon-item-card .price-section {
	display: flex;
	align-items: baseline;
	margin-bottom: 8rpx;
}

.coupon-item-card .currency {
	font-size: 24rpx;
	font-weight: 500;
}

.coupon-item-card .amount {
	font-size: 52rpx;
	font-weight: bold;
	margin: 0 4rpx;
}

.coupon-item-card .condition {
	font-size: 20rpx;
	opacity: 0.9;
	text-align: center;
}

.coupon-item-card .coupon-right {
	flex: 1;
	padding: 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	background: transparent;
}

.coupon-item-card .coupon-info {
	flex: 1;
}

.coupon-item-card .coupon-title {
	font-size: 32rpx;
	color: #222;
	font-weight: 600;
	margin-bottom: 8rpx;
}

.coupon-item-card .coupon-desc {
	font-size: 24rpx;
	color: #888;
	margin-bottom: 8rpx;
}

.coupon-item-card .coupon-time {
	font-size: 22rpx;
	color: #aaa;
}

.coupon-item-card .coupon-status {
	margin-left: 20rpx;
}

.coupon-footer {
	display: flex;
	gap: 20rpx;
	padding: 20rpx 16rpx 0;
}

.footer-btn {
	flex: 1;
	height: 88rpx;
	border-radius: 44rpx;
	font-size: 28rpx;
	line-height: 88rpx;
	text-align: center;
	border: 1rpx solid #ddd;
	color: #666;
	background-color: #f8f8f8;
}

.footer-btn.primary {
	background-color: #ff6b01;
	color: white;
	border-color: #ff6b01;
}

/* 底部支付区域 */
.bottom-action {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background-color: white;
	padding: 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
}

.payment-amount {
	display: flex;
	align-items: baseline;
	gap: 8rpx;
}

.amount-label {
	font-size: 24rpx;
	color: #333;
}

.amount-symbol {
	font-size: 24rpx;
	color: #ff6b35;
	font-weight: bold;
}

.amount-value {
	font-size: 56rpx;
	color: #ff6b35;
	font-weight: bold;
}

.pay-btn {
	width: 290rpx;
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
</style>
