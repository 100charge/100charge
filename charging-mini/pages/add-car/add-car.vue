<template>
	<view class="main">
		<view class="card plate-box">
			<view class="title dis-flex"> 
				<view class="mark">*</view>车牌号码 
			</view>
			<view class="tip"> 请正确填写车牌号码 </view>
			<view class="dis-flex-ac">
				<view
					class="plate-number-item dis-flex-ac-jc"
					:class="{ active: activeIndex == index }"
					v-for="(item, index) in plateNumber"
					:key="index"
					@click="clickPlate(index)"
				>
					{{ item.value }}
				</view>
			</view>
			
			<!-- 拍车牌识别 -->
			<view class="camera-section">
				<view class="camera-btn" @click="openCamera">
					<u-icon name="camera" color="#72727d" size="18"></u-icon>
					<text>拍车牌识别</text>
					<u-icon name="arrow-right" color="#72727d" size="14"></u-icon>
				</view>
			</view>
			
			<u-keyboard
				mode="car"
				v-model="showPlateKeyboard"
				safe-area-inset-bottom
				:mask-close-able="true"
				@change="valChange"
				@backspace="backspace"
				@confirm="closePlateKeyboard"
				@cancel="closePlateKeyboard"
			></u-keyboard>
		</view>

		<view class="card btn-list" v-if="carId == ''">
			<view class="title">车辆用途</view>
			<view class="tip">正确填写车牌用途信息，为您推荐更多优惠的场所</view>
			<view class="dis-flex-ac btn-box">
				<view
					class="btn-item dis-flex-ac-jc"
					:class="{ active: selectedType == item.value }"
					v-for="(item, index) in btnList"
					:key="index"
					@click="selectedType = item.value"
				>
					{{ item.label }}
				</view>
			</view>
		</view>

		<view class="card" style="padding: 24rpx">
			<view class="flex-row flex-jst-btw flex-ali-center">
				<text>设为默认</text>
				<u-switch space="2" v-model="defaultValue" activeColor="#FF6B01" inactiveColor="rgb(230, 230, 230)"></u-switch>
			</view>
		</view>

		<!-- 底部按钮 -->
		<view class="btn-bottom-box">
			<view class="value dis-flex-ac">
				<u-icon name="checkmark-circle-fill" :color="isRead ? '#FF6B01' : '#C2C1C6'" @click="onRead"></u-icon>
				<text class="padd-l-14" @click="onRead">我已阅读并同意</text>
				<text class="protocol" @click.stop="goArticle">《车辆信息授权协议》</text>
			</view>
			<view class="btn dis-flex-ac-jc btn-active" @click="submit"> {{ carId ? "确认修改" : "完成" }} </view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			defaultValue: false,
			selectedType: "PRIVATE_CAR",
			activeIndex: 0,
			showPlateKeyboard: false,
			carNo: "",
			carId: "",
			plateNumber: [
				{ value: "" },
				{ value: "" },
				{ value: "" },
				{ value: "" },
				{ value: "" },
				{ value: "" },
				{ value: "" },
				{ value: "" },
			],
			btnList: [
				{ value: "PRIVATE_CAR", label: "私家车" },
				{ value: "RIDE_HAILING_CAR", label: "网约车" },
				{ value: "LOGISTICS_VEHICLE", label: "物流车" },
				{ value: "TAXI", label: "出租车" },
			],
			isRead: false,
			fromPage: "",
		}
	},
	onLoad(option) {
		this.defaultValue = option.default == 1 ? true : false
		this.carId = option.carId || ""
		this.carNo = option.no || ""
		this.fromPage = option.fromPage || ""

		if (!this.carId) {
			this.clickPlate(0)
		} else {
			this.isRead = true
			this.plateNumber = []
			for (let i = 0; i < 8; i++) {
				this.plateNumber.push({
					value: option.no[i] || "",
				})
			}
		}
	},
	methods: {
		isPlateNo(number) {
			const regExp =
				/^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z](([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([0-9]{5}[DF]))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1})$/
			if (!regExp.test(number)) {
				return false
			} else {
				console.log("车牌号正确")
				return true
			}
		},
		submit() {
			let number = this.plateNumber
				.filter((f) => {
					return f.value !== ""
				})
				.map((item) => {
					return item.value
				})
			let plateNo = number.join("")
			
			if (!this.isRead) {
				uni.showToast({
					title: "请阅读并同意《车辆信息授权协议》",
					icon: "none",
				})
				return
			}
			
			if (plateNo.length < 7) {
				uni.showToast({
					title: "请输入完整的车牌号码",
					icon: "none",
				})
				return
			}
			
			// 这里应该调用API保存车牌信息
			uni.showToast({
				title: "添加成功",
				icon: "none",
			})
			
			setTimeout(() => {
				uni.navigateBack()
			}, 1000)
		},
		clickPlate(index) {
			this.activeIndex = index
			this.showPlateKeyboard = true
		},
		// 按键被点击(点击退格键不会触发此事件)
		valChange(val) {
			this.plateNumber[this.activeIndex].value = val
			this.activeIndex !== 7 && this.activeIndex++
		},
		// 退格键被点击
		backspace() {
			this.plateNumber[this.activeIndex].value = ""
			this.activeIndex !== 0 && this.activeIndex--
		},
		closePlateKeyboard() {
			this.showPlateKeyboard = false
		},
		onRead() {
			this.isRead = !this.isRead
		},
		goArticle() {
			// 跳转到协议页面
			console.log('查看车辆信息授权协议')
		},
		openCamera() {
			// 打开相机拍摄车牌
			uni.chooseImage({
				count: 1,
				sourceType: ['camera'],
				success: (res) => {
					console.log('拍摄车牌照片:', res.tempFilePaths[0])
					uni.showToast({
						title: '车牌识别功能待开发',
						icon: 'none'
					})
				}
			})
		},
	},
}
</script>

<style lang="scss" scoped>
.main {
	background: #f4f7f8;
	width: 100vw;
	min-height: 100vh;
	box-sizing: border-box;
	padding-bottom: 180rpx;
	padding-top: 24rpx;
	padding: 24rpx 24rpx 220rpx;
	position: relative;
}

.dis-flex {
	display: flex;
}

.dis-flex-ac {
	display: flex;
	align-items: center;
}

.dis-flex-between {
	@extend .dis-flex-ac;
	justify-content: space-between;
}

.dis-flex-ac-jc {
	@extend .dis-flex-ac;
	justify-content: center;
}

.flex-row {
	display: flex;
	flex-direction: row;
}

.flex-jst-btw {
	justify-content: space-between;
}

.flex-ali-center {
	align-items: center;
}

.card {
	background: #ffffff;
	border-radius: 20rpx 20rpx 20rpx 20rpx;
	margin-bottom: 24rpx;
	box-sizing: border-box;
}

.plate-box {
	padding: 30rpx 24rpx 24rpx;

	.title {
		font-size: 32rpx;
		color: #333333;
		line-height: 40rpx;
		font-weight: 700;

		.mark {
			height: 40rpx;
			font-size: 32rpx;
			color: #6b6969;
			line-height: 40rpx;
		}
	}

	.tip {
		padding: 18rpx 0 35rpx;
		font-size: 24rpx;
		color: #72727d;
		line-height: 24rpx;
	}

	.plate-number-item {
		width: 69rpx;
		height: 88rpx;
		font-size: 30rpx;
		color: #333333;
		background: #f5f5f5;
		border: 1rpx solid #e5e5e5;
		border-radius: 8rpx 8rpx 8rpx 8rpx;
	}

	.plate-number-item + .plate-number-item {
		margin-left: 10rpx;
	}

	.plate-number-item:nth-child(3) {
		margin-left: 42rpx;
		position: relative;
	}

	.plate-number-item:nth-child(3)::before {
		content: "";
		position: absolute;
		width: 8rpx;
		height: 8rpx;
		background: #333333;
		left: -25rpx;
		top: 40rpx;
		border-radius: 50%;
	}

	.active {
		background: rgba(255, 107, 53, 0.1);
		font-size: 32rpx;
		border-radius: 8rpx 8rpx 8rpx 8rpx;
		border: 1rpx solid #ff6b35;
	}
}

.camera-section {
	padding: 0rpx 24rpx 24rpx;
	
	.camera-btn {
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: #ffffff;
		border: none;
		border-radius: 0;
		padding: 32rpx 0;
		font-size: 30rpx;
		color: #333333;
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.08);
		
		text {
			flex: 1;
			margin-left: 24rpx;
		}
	}
}

.btn-list {
	padding: 35rpx 2rpx 15rpx 24rpx;

	.title {
		font-weight: 700;
		font-size: 32rpx;
		color: #333333;
		line-height: 40rpx;
		padding-bottom: 18rpx;
	}

	.tip {
		font-size: 24rpx;
		color: #72727d;
		line-height: 24rpx;
		padding-bottom: 35rpx;
	}

	.btn-box {
		flex-wrap: wrap;
	}

	.btn-item {
		width: 126rpx;
		height: 56rpx;
		background: #f5f5f5;
		border: 1rpx solid #e5e5e5;
		border-radius: 29rpx 29rpx 29rpx 29rpx;
		margin-right: 22rpx;
		font-size: 26rpx;
		color: #72727d;
		margin-bottom: 25rpx;
	}

	.active {
		background: rgba(255, 107, 53, 0.1);
		border: 2rpx solid #ff6b35;
		font-size: 26rpx;
		color: #ff6b35;
	}
}

.btn-bottom-box {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 0;
	background: #ffffff;
	padding: 30rpx 24rpx 59rpx;
	
	.value {
		padding-bottom: 26rpx;
		font-size: 24rpx;
		color: #72727d;
		line-height: 28rpx;
		
		.padd-l-14 {
			padding-left: 14rpx;
		}
		
		.protocol {
			color: #ff6b35;
		}
	}

	.btn {
		width: 702rpx;
		height: 92rpx;
		background: #c2c1c6;
		border-radius: 50rpx 50rpx 50rpx 50rpx;
		font-size: 28rpx;
		color: #ffffff;
		line-height: 28rpx;
	}
	
	.btn-active {
		background: linear-gradient(135deg, #ff6b35 0%, #ff8c42 100%);
	}
}
</style>
