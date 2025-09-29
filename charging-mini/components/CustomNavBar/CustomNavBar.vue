<template>
	<view class="navbar-wrap">
		<!-- 占位区域，防止内容被遮挡 -->
		<view :style="{ height: barHeight }"></view>
		
		<!-- 导航栏 -->
		<view class="nav-bar-wrap" :style="{ background: backgroundColor }">
			<!-- 状态栏占位 -->
			<view class="status-bar" :style="{ height: statusHeight }"></view>
			
			<!-- 导航栏内容 -->
			<view class="nav-bar" :style="{ padding: menuGap, gap: menuGap, height: menuHeight, paddingTop: '0' }">
				<!-- 左侧返回按钮 -->
				<view class="left" v-if="showBack" :style="{ width: menuWidth, height: menuHeight }" @click="handleBack">
					<uni-icons type="left" size="20" :color="backColor"></uni-icons>
				</view>
				
				<!-- 中间内容区域 -->
				<view class="center-content">
					<!-- 标题模式 -->
					<text class="nav-title" v-if="type === 'title'" :style="{ color: titleColor, fontSize: titleFontSize }">
						{{ title }}
					</text>
					
					<!-- 搜索模式 -->
					<view class="search-bar" v-if="type === 'search'" 
						:style="{ height: menuHeight, borderRadius: borderRadius, background: searchBackground }"
						@click="handleSearch('to')">
						<text class="search-icon">🔍</text>
						<input type="text" :placeholder="placeholder" :placeholder-style="placeholderStyle"
							:style="{ color: searchColor }" :disabled="disabled" v-model="keyWord" @confirm="handleSearch">
						<text class="clear-icon" v-show="keyWord" @click.stop="handleSearch('clear')">×</text>
					</view>
					
					<!-- 自定义插槽 -->
					<slot v-if="type === 'slot'"></slot>
				</view>
				
				<!-- 右侧分段控制器 -->
		
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'CustomNavBar',
		props: {
			title: {
				type: String,
				default: ''
			},
			backgroundColor: {
				type: String,
				default: 'white'
			},
			titleColor: {
				type: String,
				default: 'black'
			},
			titleFontSize: {
				type: String,
				default: '36rpx'
			},
			showBack: {
				type: Boolean,
				default: true
			},
			showHome: {
				type: Boolean,
				default: false
			},
			type: {
				type: String,
				default: 'title' // title, search, slot
			},
			rightActions: {
				type: Array,
				default: () => []
			},
			// 搜索相关props
			disabled: {
				type: Boolean,
				default: false
			},
			searchBackground: {
				type: String,
				default: '#fff'
			},
			searchColor: {
				type: String,
				default: '#000'
			},
			placeholder: {
				type: String,
				default: '关键字快速搜索'
			},
			placeholderStyle: {
				type: String,
				default: 'color: #999;'
			},
			searchValue: {
				type: String,
				default: ''
			},
			backColor: {
				type: String,
				default: 'black'
			},
			segmentColor: {
				type: String,
				default: 'black'
			}
		},
		data() {
			return {
				barHeight: '200rpx',
				statusHeight: '44rpx',
				menuGap: '14rpx',
				menuWidth: '170rpx',
				menuHeight: '64rpx',
				borderRadius: '32rpx',
				keyWord: ''
			}
		},
		watch: {
			searchValue(val) {
				this.keyWord = val
			}
		},
		created() {
			this.initNavBar()
		},
		methods: {
			initNavBar() {
				const menuButtonInfo = uni.getMenuButtonBoundingClientRect()
				uni.getSystemInfo({
					success: (res) => {
						const statusBarHeight = res.statusBarHeight
						
						// 计算各种尺寸
						this.statusHeight = statusBarHeight * 2 - 4 + 'rpx'
						this.menuGap = (menuButtonInfo.top - statusBarHeight) * 2 + 'rpx'
						this.menuWidth = menuButtonInfo.width * 2 + 'rpx'
						this.menuHeight = menuButtonInfo.height * 2 + 'rpx'
						this.borderRadius = menuButtonInfo.height + 'rpx'
						this.barHeight = (statusBarHeight + menuButtonInfo.height + (menuButtonInfo.top - statusBarHeight) * 2) * 2 + 'rpx'
					}
				})
			},
			handleBack() {
				this.$emit('back')
				const hasBackListener = (this.$listeners && this.$listeners.back) || (this.$attrs && (this.$attrs.onBack || this.$attrs['onBack']))
				if (!hasBackListener) {
					if (typeof uni !== 'undefined' && typeof uni.navigateBack === 'function') {
						uni.navigateBack()
					} else if (typeof history !== 'undefined' && history.length > 1) {
						history.back()
					}
				}
			},
			handleHome() {
				this.$emit('home')
				// 可以在这里添加默认的首页跳转逻辑
			},
			handleAction(action) {
				this.$emit('action', action)
			},
			handleSegmentClick(type) {
				this.$emit('segmentClick', type)
			},
			// 处理搜索
			handleSearch(event) {
				if (event === 'to') {
					this.disabled && this.$emit('search')
				} else if (event === 'search') {
					this.$emit('search', this.keyWord)
				} else if (event === 'clear') {
					this.keyWord = ''
					this.$emit('search', this.keyWord)
				} else if (typeof event === 'object') {
					this.keyWord = event.detail.value.trim()
					this.$emit('search', this.keyWord)
				}
			}
		}
	}
</script>

<style scoped>
	.navbar-wrap {
		position: relative;
	}

	.nav-bar-wrap {
		position: fixed;
		top: 0;
		z-index: 999;
		width: 100%;
		left: 0;
		right: 0;
	}

	.status-bar {
		width: 100%;
	}

	.nav-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-sizing: content-box;
	}

	.left {
		flex-shrink: 0;
		display: flex;
		justify-content: flex-start;
		align-items: center;
		padding: 0 20rpx;
		box-sizing: border-box;
	}

	.back-icon {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		line-height: 1;
	}

	.home-icon {
		font-size: 32rpx;
		color: #333;
		line-height: 1;
	}

	.center-content {
		flex: 1;
		display: flex;
		justify-content: center;
		align-items: center;
		margin: 0 20rpx;
	}

	.nav-title {
		
		text-align: center;
		max-width: 400rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
	}

	.search-bar {
		display: flex;
		align-items: center;
		box-sizing: border-box;
		padding: 0 24rpx;
		gap: 14rpx;
		font-size: 28rpx;
		color: #999;
		flex: 1;
		background-color: rgba(255, 255, 255, 0.9);
		backdrop-filter: blur(20rpx);
	}

	.search-bar input {
		flex: 1;
		font-size: 28rpx;
		background: transparent;
		border: none;
		outline: none;
	}

	.search-icon, .clear-icon {
		font-size: 28rpx;
		color: #999;
	}

	.clear-icon {
		font-size: 36rpx;
		color: #ccc;
	}

	.right {
		flex-shrink: 0;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		gap: 20rpx;
	}

	.segmented-control {
		display: flex;
		align-items: center;
		border: 1rpx solid rgba(200, 200, 200, 0.3);
		border-radius: 32rpx;
		background-color: rgba(255, 255, 255, 0.9);
		backdrop-filter: blur(20rpx);
		overflow: hidden;
	}

	.segment {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 12rpx 16rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.left-segment {
		border-right: 1rpx solid rgba(200, 200, 200, 0.3);
	}

	.segment-divider {
		width: 1rpx;
		height: 24rpx;
		background-color: rgba(200, 200, 200, 0.3);
	}

	.right-segment {
		background-color: rgba(0, 0, 0, 0.05);
	}

	.nav-action {
		padding: 8rpx;
	}

	.action-icon {
		width: 44rpx;
		height: 44rpx;
	}

	.action-text {
		font-size: 28rpx;
	}
</style> 