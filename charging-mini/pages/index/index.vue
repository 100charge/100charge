<template>
	<view class="index-box">
		<!-- 导航栏 -->
		<u-navbar ref="navRef" title=" " bgColor="#fff" :safeAreaInsetTop="true" :border="false" :placeholder="true"
			:autoBack="false" style="z-index: 999">
			<view slot="left">
				<view class="nav-custom-view">
					<view class="switch-display-type" @click="onSwitchPage">
						<view class="switch-display-item" v-if="pageDisplayType == 'map'">
							<image src="../../static/images/index/list.png" mode="heightFix"></image>
							<text style="color: #333333">列表</text>
						</view>
						<view class="switch-display-item" v-if="pageDisplayType == 'list'">
							<image src="../../static/images/index/map.png" mode="heightFix"></image>
							<text style="color: #333333">地图</text>
						</view>
					</view>
					<view class="header-search-view">
						<view class="linear-bg">
							<view class="white-bg-view">
								<view class="switch-city-pick-view">
									<picker @change="bindCityChange" :value="cityIndex" :range="cityNameArr">
										<view class="uni-input" style="margin-right: 6rpx">
											{{ currentCity }}
											<!-- {{ cityIndex === -1 || cityIndex === 0 ? currentCity : cityNameArr[cityIndex] }} -->
										</view>
									</picker>
									<u-icon name="arrow-down-fill" color="#A8A9AF" size="10"></u-icon>
								</view>

								<view class="line-shu"></view>

								<u-search placeholder="搜索场站" :clearabled="true" bgColor="transparent"
									:showAction="false" searchIconColor="#72727D" :searchIconSize="20"
									placeholderColor="#999"
									:inputStyle="{ fontSize: '28rpx', fontWeight: 400, color: '#333' }"
									v-model="keyword" @input="inputKeyword"></u-search>
							</view>
						</view>
					</view>
				</view>
			</view>
		</u-navbar>
		<!-- 导航栏 end -->
		<view v-if="pageDisplayType == 'list'" class="home-list-wrapper">
			<!-- 轮播图 -->
			<view class="home-swiper">
				<swiper :indicator-dots="true" :circular="true" indicator-color="transparent"
					indicator-active-color="transparent" :autoplay="true" :interval="3000" :duration="1000">
					<swiper-item v-for="(item, index) in swiperList" :key="index" @click="openUrl(item)">
						<view class="swiper-item">
							<image :src="item.imageUrl" mode="scaleToFill"></image>
						</view>
					</swiper-item>
				</swiper>
			</view>
		</view>
		<HomeStickySearch ref="stickySearchRef" @onComplete="onStickySearch"
			@getCurrentLocation="bindCityChange({ detail: { value: 0 } })" />
		<home-map v-if="pageDisplayType == 'map'" :stationList="stationList" @onChangeCenter="onChangeMapCenter" />
		<home-list v-if="pageDisplayType == 'list'" :stationList="stationList" :loadStatus="loadStatus" />

		<!-- 车辆认证 -->
		<uni-popup ref="noCarAuth" type="center" :mask-click="false">
			<view class="popup-content">
				<view class="popup-title">提示</view>
				<view class="popup-con">您还没有车辆认证，请先进行车辆认证</view>
				<view class="btns">
					<button class="cancle" @click="cancleCarAuth">取消</button>
					<button class="button" @click="toCarAuth">去认证</button>
				</view>
			</view>
		</uni-popup>

		<Tabbar :currentPage="'home'" />
	</view>
</template>
<script>
	import app from "@/static/js/app.js"
	import HomeStickySearch from "../../components/home-sticky-search/home-sticky-search.vue"
	import {
		pageStation,
		listMapStation,
		getOpenCities,
		getPendingPaymentOrder,
		getBannerList
	} from "@/config/api.js"
	import Tabbar from "../../components/tabbar/tabbar.vue"
	import homeMap from "../../components/home-map/home-map.vue"
	import homeList from "../../components/home-list/home-list.vue"
	export default {
		components: {
			Tabbar,
			homeMap,
			homeList,
			HomeStickySearch,
		},
		data() {
			return {
				latitude: "",
				longitude: "",
				cityCode: "",
				mapLatitude: "",
				mapLongitude: "",
				currentCity: uni.getStorageSync("city") || "济南市",
				navHeight: 0,
				pageDisplayType: "list", // map list
				keyword: "",
				pageInfo: {},
				cityIndex: 0,
				cityNameArr: [],
				cityList: [],
				stationList: [],
				stationTotal: 0,
				pageNum: 1,
				stickySearchParams: {},
				loadStatus: "",
				swiperList: [{
					imageUrl: "https://demofile.zhitancloud.com/demo-file/2025/08/13/27a18d4e5ed04ba46989306b1bbe8434.png"
				}, ],
				showAuthLocation: true,
			}
		},
		watch: {
			showAuthLocation: {
				handler(val, old) {
					if (!val) {
						this.bindCityChange({
							detail: {
								value: 0
							}
						})
					}
				},
				immediate: false,
				deep: true,
			},
		},
		mounted() {

			this.getBanner()

			// #ifdef MP-WEIXIN
			this.$nextTick(() => {
				this.navHeight = this.$refs.navRef.$mp.data.$root.g0
				// 确保存储的值包含 px 单位
				const navHeightValue = typeof this.navHeight === "number" ? this.navHeight + "px" : this.navHeight
				uni.setStorageSync("homeNavHeight", navHeightValue)
				// 通知子组件更新
				if (this.$refs.stickySearchRef) {
					this.$refs.stickySearchRef.updateNavHeight(navHeightValue)
				}
			})
			// #endif
			this.getCityData()

			if (this.pageDisplayType == "map") {
				this.getStationDataMap()
			} else {
				this.getStationData()
			}
		},

		onShow() {
			this.showAuthLocation = !uni.getStorageSync("hasAuthLocation")
			this.getAuthStatus()
			console.log("onShow-home", uni.getStorageSync("latitude"))
			this.latitude = uni.getStorageSync("latitude") || 36.6667
			this.longitude = uni.getStorageSync("longitude") || 116.9949
			this.mapLatitude = uni.getStorageSync("latitude") || 36.6667
			this.mapLongitude = uni.getStorageSync("longitude") || 116.9949
			this.cityCode = uni.getStorageSync("cityCode") || "370100"
			this.currentCity = uni.getStorageSync("city") || "济南市"
			this.cityIndex = 0
			this.getPendingOrder()
		},

		onPageScroll: function(e) {
			if (this.pageDisplayType == "list") {
				uni.setStorageSync("homeScrollTop", e.scrollTop)
			}
		},

		onPullDownRefresh() {
			console.log("onPullDownRefresh")
			this.pageNum = 1
			this.getStationData()
			this.getCityData()
			this.getBanner()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		onReachBottom() {
			if (this.pageNum * 10 < this.stationTotal) {
				this.loadStatus = "loading"
				this.pageNum++
				setTimeout(() => {
					this.getStationData()
				}, 100)
			} else {
				this.loadStatus = "nomore"
			}
		},
		methods: {
			getAuthStatus() {
				let that = this
				uni.getSetting({
					success: function(res) {
						console.log("getSetting", res)

						// #ifdef MP-ALIPAY
						if (!res.authSetting["location"]) {
							uni.setStorageSync("hasAuthLocation", false)
							that.showAuthLocation = true
							that.$refs.stickySearchRef.changeShowAuth(true)
						} else {
							uni.setStorageSync("hasAuthLocation", true)
							that.showAuthLocation = false
							that.$refs.stickySearchRef.changeShowAuth(false)
						}
						// #endif

						// #ifdef MP-WEIXIN
						if (!res.authSetting["scope.userLocation"]) {
							uni.setStorageSync("hasAuthLocation", false)
							that.showAuthLocation = true
							that.$refs.stickySearchRef.changeShowAuth(true)
						} else {
							uni.setStorageSync("hasAuthLocation", true)
							that.showAuthLocation = false
							that.$refs.stickySearchRef.changeShowAuth(false)
						}
						// #endif
					},
					fail: function(e) {
						console.log("getSetting-fail", e)
					},
				})
			},

			openUrl(item) {
				if (!item.link) {
					return
				}
				uni.navigateTo({
					url: item.link,
				})
			},

			getPendingOrder() {
				getPendingPaymentOrder({}).then((res) => {
					if (res && res.code === 200 && res.data.id) {
						if (res.data.orderState == 1) {
							// 充电中订单
							uni.showModal({
								title: "提示",
								content: "您当前有正在充电的订单，是否查看？",
								confirmText: "去看看",
								cancelText: "取消",
								cancelColor: "#666",
								success: (result) => {
									if (result.confirm) {
										uni.navigateTo({
											url: "/pages/stations/site/charging?orderNo=" + res
												.data.tradeNo,
										})
									}
								},
							})
						} else if (res.data.payStatus === 0 && res.data.orderState == 4) {
							// 未支付订单
							uni.showModal({
								title: "提示",
								content: "您当前有未支付的订单",
								confirmText: "去支付",
								cancelText: "取消",
								// showCancel: false,
								cancelColor: "#666",
								success: (result) => {
									if (result.confirm) {
										uni.navigateTo({
											url: "/pages/stations/orders/orderDetail?orderNo=" +
												res.data.tradeNo,
										})
									}
								},
							})
						}
					}
				})
			},
			getStationData() {
				let params = {
					chargingStationName: this.keyword,
					pageNum: this.pageNum,
					pageSize: 10,
					lat: this.latitude || 36.6667,
					lng: this.longitude || 116.9949,
					cityCode: this.cityCode ? this.cityCode.substring(0, 4) : "3702",
					recommendType: this.stickySearchParams.sortValue || "AI", // 推荐排序:枚举类型,可用值:AI,DISTANCE,PRICE
					chargingSpeed: this.stickySearchParams.speedValue ||
					"", // 枚举类型,可用值:FAST_CHARGE,SLOW_CHARGE,SUPERCHARGE
					facilityLabel: this.stickySearchParams.installationLabel ?
						this.stickySearchParams.installationLabel.join(",") :
						"", // 设施
					parkingFeeLabel: this.stickySearchParams.parkLabel ? this.stickySearchParams.parkLabel.join(",") :
						"", // 停车费
					serviceLabel: this.stickySearchParams.serviceLabel ? this.stickySearchParams.serviceLabel.join(
						",") : "", // 服务
				}
				console.log("getStationData", params)
				pageStation(params).then((res) => {
					if (res && res.code == 200) {
						this.stationTotal = res.total
						let rows = res.rows.map((item) => {
							return {
								...item,
								labelList: item.labelName ? item.labelName.split(",") : [],
								distance: item.distance > 1000 ? (item.distance / 1000).toFixed(1) +
									" km" : item.distance + " m",
								distanceNumber: +item.distance,
								starLabel: +item.starLabel || 0,
								chargingTypeList: item.chargingType ? item.chargingType.split(",") : [],
								idleFastChargeCount: item.idleFastChargeCount || 0,
								fastCharging: item.fastCharging || 0,
								idleSlowChargeCount: item.idleSlowChargeCount || 0,
								slowCharging: item.slowCharging || 0,
								latitude: item.lat,
								longitude: item.lng,
								stationId: item.id,
							}
						})
						if (this.pageNum === 1) {
							this.stationList = rows
						} else {
							this.stationList.push(...rows)
						}
						// 数据更新后，强制重新计算 sticky 布局
						this.$nextTick(() => {
							// 通过更新 key 来强制重新渲染，修复 sticky 失效问题
							if (this.$refs.stickySearchRef) {
								this.$refs.stickySearchRef.forceUpdateKey++
							}
						})
					}
				})
			},
			getStationDataMap() {
				console.log("getStationDataMap")
				listMapStation({
					chargingStationName: this.keyword,
					lat: this.latitude || 36.6667,
					lng: this.longitude || 116.9949,
					cityCode: this.cityCode ? this.cityCode.substring(0, 4) : "3702",
					mapLat: this.mapLatitude || 36.6667,
					mapLng: this.mapLongitude || 116.9949,
					recommendType: this.stickySearchParams.sortValue || "AI", // 推荐排序:枚举类型,可用值:AI,DISTANCE,PRICE
					chargingSpeed: this.stickySearchParams.speedValue ||
					"", // 枚举类型,可用值:FAST_CHARGE,SLOW_CHARGE,SUPERCHARGE
					facilityLabel: this.stickySearchParams.installationLabel ?
						this.stickySearchParams.installationLabel.join(",") :
						"", // 设施
					parkingFeeLabel: this.stickySearchParams.parkLabel ? this.stickySearchParams.parkLabel.join(
						",") : "", // 停车费
					serviceLabel: this.stickySearchParams.serviceLabel ? this.stickySearchParams.serviceLabel.join(
						",") : "", // 服务
				}).then((res) => {
					if (res && res.code == 200) {
						let rows = res.data.map((item, index) => {
							return {
								...item,
								key: new Date().getTime() + index,
								labelList: item.labelName ? item.labelName.split(",") : [],
								distance: item.distance > 1000 ? (item.distance / 1000).toFixed(1) +
									" km" : item.distance + " m",
								distanceNumber: +item.distance,
								starLabel: +item.starLabel || 0,
								chargingTypeList: item.chargingType ? item.chargingType.split(",") : [],
								idleFastChargeCount: item.idleFastChargeCount || 0,
								fastCharging: item.fastCharging || 0,
								idleSlowChargeCount: item.idleSlowChargeCount || 0,
								slowCharging: item.slowCharging || 0,
								latitude: item.lat,
								longitude: item.lng,
								stationId: item.id,
							}
						})
						this.stationList = rows
					}
				})
			},
			inputKeyword(e) {
				if (this.pageDisplayType == "list") {
					this.getStationData()
				} else {
					this.getStationDataMap()
				}
			},
			onStickySearch(e) {
				console.log("onStickySearch-home", e)
				this.pageNum = 1
				this.stickySearchParams = e
				if (this.pageDisplayType == "list") {
					this.getStationData()
				} else {
					this.getStationDataMap()
				}
			},
			onChangeMapCenter(e) {
				if (e.latitude) {
					this.mapLatitude = e.latitude
					this.mapLongitude = e.longitude
					this.getStationDataMap()
				}
			},
			getBanner() {
				getBannerList({}).then((res) => {
					if (res.rows && res.rows.length > 0) {
						this.swiperList = res.rows || []
					}
				})
			},
			bindCityChange(e) {
				let index = e.detail.value
				if (index == 0) {
					this.cityIndex = -1
					let that = this
					// 重新定位，获取当前位置
					console.log("getCurrentLocation:home.vue")
					uni.getLocation({
						type: "gcj02",
						success: function(res) {
							console.log("重新定位，获取当前位置", res)
							uni.setStorageSync("latitude", res.latitude)
							uni.setStorageSync("longitude", res.longitude)
							that.latitude = res.latitude
							that.longitude = res.longitude
							uni.request({
								url: "https://apis.map.qq.com/ws/geocoder/v1/", // 腾讯
								data: {
									extensions: "all",
									key: "JGABZ-OJUKV-G6MPB-56GIH-3O2D2-SEBUR", // 腾讯
									location: res.latitude + "," + res.longitude,
								},
								success(response) {
									let city = ""
									let cityCode = ""
									uni.setStorageSync("hasAuthLocation", true)
									that.showAuthLocation = false
									that.$refs.stickySearchRef.changeShowAuth(false)
									console.log("腾讯解析地址", response)
									city = response.data.result.address_component.city
									cityCode = response.data.result.ad_info.adcode
									uni.setStorageSync("city", city)
									uni.setStorageSync("cityCode", cityCode)
									that.currentCity = city
									that.cityCode = cityCode
								},
								fail() {
									console.log("定位失败，默认经纬度为山东济南市")
									uni.setStorageSync("city", "济南市")
									uni.setStorageSync("cityCode", "370100")
								},
							})
						},
						fail(e) {
							uni.setStorageSync("latitude", 36.6667)
							uni.setStorageSync("longitude", 116.9949)
							uni.setStorageSync("city", "济南市")
							uni.setStorageSync("cityCode", "370100")
							uni.showToast({
								title: "定位失败，已为您默认定位到 济南市",
								icon: "none",
							})
						},
						complete(c) {
							console.log("complete", c)
							setTimeout(() => {
								if (that.pageDisplayType == "map") {
									that.getStationDataMap()
								} else {
									that.getStationData()
								}
							}, 500)
						},
					})
				} else {
					this.cityIndex = index
					uni.setStorageSync("latitude", this.cityList[index].lat)
					uni.setStorageSync("longitude", this.cityList[index].lng)
					uni.setStorageSync("city", this.cityList[index].name)
					uni.setStorageSync("cityCode", this.cityList[index].code)
					this.cityCode = this.cityList[index].code
					this.latitude = this.cityList[index].lat
					this.longitude = this.cityList[index].lng
					this.currentCity = this.cityList[index].name
				}
				this.pageNum = 1
				if (this.pageDisplayType == "map") {
					this.getStationDataMap()
				} else {
					this.getStationData()
				}
			},

			onSwitchPage() {
				if (this.pageDisplayType == "map") {
					this.pageDisplayType = "list"
					uni.pageScrollTo({
						scrollTop: 1,
						duration: 10,
					})
					this.getStationData()
				} else {
					this.pageDisplayType = "map"
					this.getStationDataMap()
				}
			},
			toLoginPage() {
				uni.navigateTo({
					url: "/pages/login/login",
				})
			},

			getCityData() {
				this.cityList = [{
					name: "重新定位"
				}]
				this.cityNameArr = ["重新定位"]
				getOpenCities({}).then((res) => {
					if (res && res.code === 200) {
						this.cityList.push(...(res.data || []))
						let nameArr = res.data ?
							res.data.map((item) => {
								return item.name
							}) :
							[]
						this.cityNameArr.push(...nameArr)
					}
				})
			},
		},
	}
</script>
<style lang="scss" scoped>
	.index-box {
		width: 100%;
		background-color: #fff;
		background-size: cover;
		min-height: 100vh;
		box-sizing: border-box;

		::v-deep .u-navbar--fixed {
			z-index: 99;
		}

		.nav-custom-view {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			width: 520rpx;

			.switch-display-type {
				width: 124rpx;

				.switch-display-item {
					display: flex;
					justify-content: flex-start;
					align-items: center;
					font-family: PingFang SC, PingFang SC;
					font-weight: 400;
					font-size: 28rpx;
					color: #3d3d3d;
					font-style: normal;
					text-transform: none;

					image {
						width: 40rpx;
						height: 40rpx;
						margin-right: 10rpx;
					}
				}
			}
		}

		.header-search-view {
			display: flex;
			justify-content: space-between;
			align-items: center;
			height: 60rpx;
			flex: 1;

			.linear-bg {
				width: 100%;
				padding: 2rpx;
				border-radius: 36rpx;
			}

			.white-bg-view {
				text-align: center;
				font-weight: 400;
				// width: 100%;
				height: 60rpx;
				background: #f4f5f7;
				border-radius: 36rpx;
				border: none;
				font-size: 28rpx;
				display: flex;
				justify-content: flex-start;
				align-items: center;
				padding-left: 14rpx;
			}

			.switch-city-pick-view {
				width: 110rpx;
				height: 60rpx;
				text-align: center;
				font-weight: 400;
				font-size: 28rpx;
				color: #333;
				display: flex;
				justify-content: flex-start;
				align-items: center;
			}

			.line-shu {
				background-color: rgba(0, 0, 0, 0.08);
				width: 2rpx;
				height: 27rpx;
				// margin: 0 16rpx;
			}
		}
	}

	.home-list-wrapper {
		// min-height: 100vh;
		background-color: #f4f4f4;
		position: relative;
		/* #ifdef MP-ALIPAY */
		margin-top: 68rpx;

		/* #endif */
		.home-swiper {
			padding-top: 20rpx;
			width: 100%;
			margin: 0 auto;
			border-radius: 10rpx;
			overflow: hidden;
			background-color: #fff;

			swiper {
				width: 684rpx;
				height: 280rpx;
				margin: 0 auto;

				swiper-item {
					width: 686rpx;
					height: 280rpx;

					.swiper-item {
						width: 686rpx;
						height: 280rpx;

						image {
							width: 686rpx;
							height: 280rpx;
							opacity: 1;
							background: rgba(0, 0, 0, 0);
							border-radius: 16rpx;
							// box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(0, 0, 0, 0.1);
						}
					}
				}
			}
		}
	}

	::v-deep .u-popup__content {
		background-color: transparent !important;
	}

	::v-deep .u-search__content {
		padding: 0 10rpx !important;
	}

	.coupon-popup-content {
		width: 602rpx;
		border-radius: 32rpx !important;

		.content {
			background: #fff0e3;
			border-radius: 32rpx !important;
			position: relative;
			padding: 50rpx;
			padding-bottom: 140rpx;

			.coupon-list {
				width: 100%;
				background: linear-gradient(270deg, #ff5d10 0%, #ffa95d 100%);
				border-radius: 27rpx;
				color: #774a24;
				padding: 30rpx 24rpx;
				margin-top: 32rpx;

				.coupon-item {
					margin-bottom: 18rpx;
					background-size: 100% 100%;
					height: 132rpx;
					width: 100%;
					display: flex;
					justify-items: flex-start;
					align-items: center;

					.left {
						width: 150rpx;
						text-align: center;
						font-size: 54rpx;
						font-weight: 600;
						color: #eb6130;

						text {
							font-size: 26rpx;
							font-weight: 400;
						}
					}

					.right {
						flex: 1;
						padding-left: 32rpx;
						font-size: 34rpx;
						font-weight: 600;
					}
				}
			}
		}

		.popup-title {
			image {
				height: 38rpx;
			}
		}

		.popup-footer {
			position: absolute;
			left: 0;
			right: 0;
			bottom: 0;
			background-size: 100% 100%;
			width: 100%;
			height: 192rpx;
			display: flex;
			justify-content: center;
			align-items: center;

			.active-btn {
				width: 350rpx;
				height: 94rpx;
				background: linear-gradient(270deg, #ff4d00 0%, #ffb86c 100%);
				border-radius: 47rpx;
				margin-top: 20rpx;
				color: #fff;
				display: flex;
				justify-content: center;
				align-items: center;
				flex-direction: column;
				font-size: 30rpx;

				text {
					font-size: 18rpx;
				}
			}
		}

		.close-image {
			text-align: center;
			display: flex;
			justify-content: center;
			padding-top: 32rpx;
		}
	}

	.popup-content {
		width: 642rpx;
		height: 372rpx;
		background: #ffffff;
		border-radius: 32rpx !important;

		.popup-title {
			height: 92rpx;
			background: linear-gradient(79deg, #e5f0ff 0%, #ffffff 100%);
			border-radius: 32rpx 32rpx 0rpx 0rpx;
			font-weight: 500;
			font-size: 32rpx;
			color: #2a2a2a;
			line-height: 92rpx;
			text-align: center;
		}

		.popup-con {
			text-align: center;
			font-weight: 500;
			font-size: 32rpx;
			color: #2a2a2a;
			line-height: 45rpx;
			margin-top: 54rpx;
			margin-bottom: 57rpx;
		}

		.btns {
			display: flex;

			.cancle {
				width: 240rpx;
				height: 68rpx;
				line-height: 68rpx;
				border-radius: 45rpx;
				border: 2rpx solid #439bff;
				font-size: 28rpx;
				color: #358bff;
			}

			.button {
				width: 240rpx;
				height: 68rpx;
				line-height: 68rpx;
				background: linear-gradient(122deg, #5fbcff 0%, #3187ff 100%), #3187ff;
				border-radius: 45rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
</style>