<template>
  <div class="app-container" style="background-color: transparent; padding: 0">
    <div class="card-list">
      <div class="card-item" v-for="(item, index) in cardList" :key="index">
        <div class="card-item-title">
          <div class="title">
            <span class="point"></span>
            {{ index === 0 ? "场站数量" : item.name + "充电营收" }}
          </div>
          <div class="value">
            {{ item.value1 }}
            {{ index === 0 ? "个" : "元" }}
          </div>
        </div>
        <div class="card-item-title">
          <div class="title">
            <span class="point"></span>
            {{ index === 0 ? "充电桩数量" : item.name + "充电电量" }}
          </div>
          <div class="value">
            {{ item.value2 }}
            {{ index === 0 ? "个" : "度" }}
          </div>
        </div>
        <div class="card-item-title">
          <div class="title">
            <span class="point"></span>
            {{ index === 0 ? "充电枪数量" : item.name + "充电次数" }}
          </div>
          <div class="value">
            {{ item.value3 }}
            {{ index === 0 ? "个" : "次" }}
          </div>
        </div>
      </div>
    </div>

    <div class="card-box-custom">
      <div class="card-box-header">
        <div class="card-title">
          <span class="card-tag"></span>
          <span>场站经营</span>
        </div>
        <div class="card-operation">
          <el-select v-model="queryParams.stationId" placeholder="全部场站">
            <el-option v-for="item in stationOption" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
          <el-date-picker
            v-model="queryParams.dataTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 260px; margin: 0 12px"
          ></el-date-picker>
          <el-button type="primary" icon="Search" @click="getUpdateStations">搜索</el-button>
        </div>
      </div>
      <div class="card-box-cont">
        <div class="card-box-item">
          <div class="img-bg">
            <img src="@/assets/images/home/icon1.png" alt="" />
          </div>
          <div>
            <div>订单数量</div>
            <div class="count">{{ statistics.orderCount || 0 }}</div>
          </div>
        </div>
        <div class="card-box-item">
          <div class="img-bg">
            <img src="@/assets/images/home/icon2.png" alt="" />
          </div>
          <div>
            <div>充电电量（度）</div>
            <div class="count">{{ statistics.chargingCapacity || 0 }}</div>
          </div>
        </div>
        <div class="card-box-item">
          <div class="img-bg">
            <img src="@/assets/images/home/icon3.png" alt="" />
          </div>
          <div>
            <div>订单金额（元）</div>
            <div class="count">{{ statistics.orderTotalAmount || 0 }}</div>
          </div>
        </div>
        <div class="card-box-item">
          <div class="img-bg">
            <img src="@/assets/images/home/icon4.png" alt="" />
          </div>
          <div>
            <div>订单电费（元）</div>
            <div class="count">{{ statistics.electricityFee || 0 }}</div>
          </div>
        </div>
        <div class="card-box-item">
          <div class="img-bg">
            <img src="@/assets/images/home/icon5.png" alt="" />
          </div>
          <div>
            <div>订单服务费（元）</div>
            <div class="count">{{ statistics.serviceFee || 0 }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- echarts -->
    <div class="card-box-custom">
      <div class="card-box-header">
        <div class="card-title">
          <span class="card-tag"></span>
          <span>全站经营趋势</span>
        </div>
        <div class="card-operation btns">
          <el-button
            :class="searchDayNumber === 7 ? 'btn-active' : 'btn'"
            :type="searchDayNumber === 7 ? 'primary' : ''"
            @click="handleSearchDay(7)"
            >近7天</el-button
          >
          <el-button
            :class="searchDayNumber === 30 ? 'btn-active' : 'btn'"
            :type="searchDayNumber === 30 ? 'primary' : ''"
            @click="handleSearchDay(30)"
            >近30天</el-button
          >
          <el-button
            :class="searchDayNumber === 12 ? 'btn-active' : 'btn'"
            :type="searchDayNumber === 12 ? 'primary' : ''"
            @click="handleSearchDay(12)"
            >近12个月</el-button
          >
        </div>
      </div>
      <div class="chart-item">
        <div ref="lineChartRef" style="width: 100%; height: 100%"></div>
      </div>
    </div>
  </div>
</template>

<script setup name="Index">
import * as echarts from "echarts"
import { formatDatePoint } from "../utils/index"
import { getStationListAll } from "@/api/device/station"
import { updateStations } from "@/api/business/chargingOrder"
import homeApi from "@/api/home/index"
import { nextTick, onMounted } from "vue"
const router = useRouter()
const { proxy } = getCurrentInstance()
const data = reactive({
  form: {},
  queryParams: {
    stationId: undefined,
    dataTime: [],
  },
})
const { queryParams } = toRefs(data)
const cardList = ref([
  { name: "充电量", value1: 123, value2: 321, value3: 345 },
  { name: "今日", value1: 123, value2: 321, value3: 345 },
  { name: "本月", value1: 123, value2: 321, value3: 345 },
  { name: "今年", value1: 123, value2: 321, value3: 345 },
])
// 充电订单统计
const statistics = ref({})
function getUpdateStations() {
  updateStations(proxy.addDateRange(queryParams.value, queryParams.value.dataTime)).then((response) => {
    statistics.value = response.data
  })
}
const stationOption = ref([])
getStationListAll({}).then((res) => {
  stationOption.value = res.data
})

const chartData = ref([])
function getData() {
  queryData()
  getUpdateStations()
  handleSearchDay(7)
}

function queryData() {
  homeApi.queryOperationData().then((res) => {
    cardList.value[1].value1 = res.data.todayRevenue || 0
    cardList.value[1].value2 = res.data.todayChargingVolume || 0
    cardList.value[1].value3 = res.data.todayChargingCount || 0

    cardList.value[2].value1 = res.data.monthlyRevenue || 0
    cardList.value[2].value2 = res.data.monthlyChargingVolume || 0
    cardList.value[2].value3 = res.data.monthlyChargingCount || 0

    cardList.value[3].value1 = res.data.annualRevenue || 0
    cardList.value[3].value2 = res.data.annualChargingVolume || 0
    cardList.value[3].value3 = res.data.annualChargingCount || 0
  })
  homeApi.getDashboardOperatorInfo().then((res) => {
    cardList.value[0].value1 = res.data.stationCount || 0
    cardList.value[0].value2 = res.data.pileCount || 0
    cardList.value[0].value3 = res.data.terminalCount || 0
  })
}

const lineChartRef = ref()
const searchDayNumber = ref(7)
const handleSearchDay = (day) => {
  searchDayNumber.value = day
  homeApi
    .getStationBusinessTrend({
      stationId: queryParams.value.stationId,
      dateNumber: searchDayNumber.value,
      timeType: searchDayNumber.value === 12 ? "MONTH" : "DAY",
    })
    .then((res) => {
      chartData.value = res.data.map((item) => {
        return {
          ...item,
          chargingServiceFee: +(item.chargingServiceFee || 0),
          totalChargingAmount: +(item.totalChargingAmount || 0),
          totalOrderAmount: +(item.totalOrderAmount || 0),
          totalOrderAmountComparison: +(item.totalOrderAmountComparison || 0),
          totalChargingAmountComparison: +(item.totalChargingAmountComparison || 0),
          chargingServiceFeeComparison: +(item.chargingServiceFeeComparison || 0),
          xTime: searchDayNumber.value === 12 ? new Date(item.time).getMonth() + 1 + "月" : formatDatePoint(item.time),
          tooltipTime: searchDayNumber.value === 12 ? item.time.substring(0, 7) : item.time,
        }
      })
      nextTick(() => {
        setChart1()
      })
    })
}
function setChart1() {
  const commandstatsIntance = echarts.init(lineChartRef.value, "macarons")
  let arr1 = chartData.value.map((item) => {
    return +(item.totalChargingAmount || 0)
  })
  let arr2 = chartData.value.map((item) => {
    return +(item.totalOrderAmount || 0)
  })
  let arr3 = chartData.value.map((item) => {
    return +(item.chargingServiceFee || 0)
  })
  let option = {
    grid: {
      top: 60,
      bottom: 30,
      left: 40,
      right: 40,
    },
    tooltip: {
      trigger: "axis",
      backgroundColor: "rgba(255,255,255,0.8)",
      padding: 10,
      textStyle: {
        color: "#000",
      },
      formatter: (params) => {
        console.log(params)
        return `<div style="padding: 5px">
            <div style="width: 160px;color:#666">
              <span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:#999;"></span>
              日期：${params[0].data.tooltipTime}</div>
            <div style="margin-top:8px;display:flex;justify-content: space-between;align-items: center;">
              <div style="color:#333;margin-right:14px">
                ${params[0].marker} 总${params[0].seriesName}：${params[0].value}
              </div>
              <div style="font-weight: 600; color:${
                params[0].data.totalChargingAmountComparison > 0 ? "#43966a" : "#e24836"
              }">${params[0].data.totalChargingAmountComparison}%</div> 
            </div>
            <div style="margin-top:6px;display:flex;justify-content: space-between;align-items: center;">
              <div style="color:#333;margin-right:14px">
                ${params[1].marker} ${params[1].seriesName}：${params[1].value}
              </div> 
              <div style="font-weight: 600; color:${
                params[1].data.totalOrderAmountComparison > 0 ? "#43966a" : "#e24836"
              }">${params[1].data.totalOrderAmountComparison}%</div> 
            </div>
            <div style="margin-top:6px;display:flex;justify-content: space-between;align-items: center;">
              <div style="color:#333;margin-right:14px">
                ${params[2].marker} ${params[2].seriesName}：${params[2].value}
              </div>  
              <div style="font-weight: 600; color:${
                params[2].data.chargingServiceFeeComparison > 0 ? "#43966a" : "#e24836"
              }">${params[2].data.chargingServiceFeeComparison}%</div>
            </div>
          </div>`
      },
    },
    legend: {
      icon: "rect",
      right: 10,
      itemWidth: 14,
      itemHeight: 10,
    },
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: chartData.value.map((item) => {
        return item.xTime
      }),
      axisLine: {
        show: false, //隐藏X轴轴线
        lineStyle: {
          color: "#5A6883",
        },
      },
      axisTick: {
        show: false, //隐藏X轴刻度
      },
      axisLabel: {
        show: true,
        textStyle: {
          color: "#5A6883", //X轴文字颜色
        },
      },
    },
    yAxis: [
      {
        name: "充电量/度",
        type: "value",
      },
      {
        name: "金额/元",
        alignTicks: true,
        type: "value",
      },
    ],
    series: [
      {
        name: "充电量",
        type: "line",
        smooth: true, //平滑曲线显示
        showAllSymbol: true, //显示所有图形。
        // symbol: "circle", //标记的图形为实心圆
        symbolSize: 3, //标记的大小
        color: proxy.primaryColor,
        lineStyle: {
          width: 2,
          shadowColor: proxy.primaryColor,
          shadowBlur: 10,
          shadowOffsetY: 8,
        },
        data: chartData.value.map((item) => {
          return {
            ...item,
            name: "充电量",
            value: item.totalChargingAmount,
          }
        }),
      },
      {
        name: "订单总额",
        type: "line",
        yAxisIndex: 1,
        smooth: true, //平滑曲线显示
        showAllSymbol: true, //显示所有图形。
        // symbol: "circle", //标记的图形为实心圆
        symbolSize: 3, //标记的大小
        color: "#faad14",
        lineStyle: {
          width: 2,
          shadowColor: "rgba(250, 173, 20, 0.6)",
          shadowBlur: 10,
          shadowOffsetY: 8,
        },
        data: chartData.value.map((item) => {
          return {
            ...item,
            name: "订单总额",
            value: item.totalOrderAmount,
          }
        }),
      },
      {
        name: "服务费",
        type: "line",
        yAxisIndex: 1,
        smooth: true, //平滑曲线显示
        showAllSymbol: true, //显示所有图形。
        // symbol: "circle", //标记的图形为实心圆
        symbolSize: 3, //标记的大小
        color: "#096dd9",
        lineStyle: {
          width: 2,
          shadowColor: "rgba(9, 109, 217, 0.6)",
          shadowBlur: 10,
          shadowOffsetY: 8,
        },
        // data: chartData.value.map((item) => {
        //   return item.chargingServiceFee
        // }),
        data: chartData.value.map((item) => {
          return {
            ...item,
            name: "服务费",
            value: item.chargingServiceFee,
          }
        }),
      },
    ],
    visualMap: [
      {
        show: false,
        type: "continuous",
        seriesIndex: 0,
        min: Math.min.apply(null, arr1),
        max: Math.max.apply(null, arr1),
        color: [proxy.primaryColor, proxy.primaryColor],
      },
      {
        show: false,
        type: "continuous",
        seriesIndex: 1,
        min: Math.min.apply(null, arr2),
        max: Math.max.apply(null, arr2),
        color: ["#faad14", "#ffe58f"],
      },
      {
        show: false,
        type: "continuous",
        seriesIndex: 2,
        min: Math.min.apply(null, arr3),
        max: Math.max.apply(null, arr3),
        color: ["#096dd9", "#91d5ff"],
      },
    ],
  }
  commandstatsIntance.setOption(option)

  window.addEventListener("resize", () => {
    commandstatsIntance.resize()
  })
}

onMounted(() => {
  getData()
})
</script>

<style scoped lang="scss">
.card-list {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  .card-item {
    width: calc(25% - 10px);
    height: 166px;
    background-image: url("@/assets/images/home/box-bg.png");
    background-repeat: no-repeat;
    background-size: 100% 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 16px;
    padding: 0 32px 0 24px;
    .card-item-title {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      color: #46484b;
      font-size: 14px;
      .point {
        width: 8px;
        height: 8px;
        background: #ef6729;
        opacity: 0.53;
        border-radius: 50%;
        display: inline-block;
        margin-bottom: 2px;
        margin-right: 4px;
      }
      .value {
        font-size: 16px;
      }
    }
  }
}

.card-box-custom {
  background: #fff;
  border-radius: 9px;
  margin-top: 12px;
  .card-box-header {
    height: 48px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    color: #242424;
    font-size: 18px;
    .card-title {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      font-weight: 500;
    }
    .card-tag {
      width: 5px;
      height: 18px;
      background: #ef6729;
      border-radius: 3px;
      display: inline-block;
      margin-right: 12px;
    }
  }
  .card-box-cont {
    padding: 18px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .card-box-item {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      height: 128px;
      width: calc(20% - 10px);
      background: #f6f6f6;
      border-radius: 9px;
      padding-left: 12px;
      color: #212121;
      .img-bg {
        background: #fff;
        width: 64px;
        height: 64px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
      }
      img {
        width: 32px;
        height: 32px;
      }
      .count {
        font-size: 22px;
        font-weight: 600;
        margin-top: 12px;
        letter-spacing: 1px;
      }
    }
  }
}
.btns {
  height: 28px;
  margin-left: 10px;
  border-radius: 6px;
  border: 0.5px solid rgb(226, 230, 236);
  display: flex;
  justify-content: flex-start;
  align-items: center;
  .btn,
  .btn-active {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 12px;
    border: none;
    margin: 0;
    height: 26px;
    width: 64px;
    border-radius: 6px;
    text-align: center;
    transition: all 0.5s;
  }
  .btn-active {
    box-shadow: 0 0 2px 0 rgba(0, 0, 0, 0.12), 0 3px 6px 0 rgba(107, 117, 124, 0.35);
  }
}
.chart-item {
  width: 100%;
  height: 420px;
  padding: 16px 12px;
}
</style>
