<template>
  <div class="app-container" style="background: transparent">
    <div class="search-wrapper">
      <div class="card-box-header">
        <div class="card-title">
          <span class="card-tag"></span>
          <span>充电站列表</span>
        </div>
      </div>
      <div class="card-cont">
        <el-input
          v-model="queryParams.stationName"
          :prefix-icon="Search"
          placeholder="请输入关键字进行搜索"
          @input="changeStation"
        />
        <div class="station-list" v-loading="loading">
          <div
            class="station-item"
            :class="{ 'station-item-active': queryParams.stationId == dict.id }"
            v-for="dict in stationOption"
            :key="dict.id"
            :label="dict.name"
            :title="dict.name"
            :value="dict.id"
            @click="selectedStation(dict)"
          >
            {{ dict.name }}
          </div>
        </div>
      </div>
    </div>

    <div class="right-box">
      <div class="card-box-header">
        <div class="card-title">
          <span class="card-tag"></span>
          <span>实时监控</span>
        </div>
      </div>
      <div class="content-view">
        <div class="station-name">{{ currentStation.name }}</div>
        <div class="btn-box">
          <div
            class="btn-item"
            :class="{ 'btn-active': item.value == tabIndex }"
            v-for="(item, index) in btnList"
            :key="index"
            @click="changeTab(item)"
          >
            <div class="item-label">{{ item.label }}</div>
            <div class="item-count">{{ item.count || 0 }}</div>
          </div>
        </div>
        <div class="empty-box" v-if="pileList.length === 0">
          <img src="../../../assets/logo/empty.png" alt="" />
          <span class="empty-text">暂无查询到充电桩</span>
        </div>
        <div class="list-wrap">
          <div class="list-card" v-for="(card, cardIndex) in pileList" :key="card.deviceNo + '-' + card.gunNo">
            <div class="card-gun">
              <!-- 默认展示模块 -->
              <div class="card-gun-basic">
                <div class="card-gun-icon">
                  <!-- // OFFLINE(0, "OFFLINE", "离线"),
                      // FAULT(1, "FAULT", "故障"),
                      // ONLINE(2, "ONLINE", "在线/空闲"),
                      // CHARGING(3, "CHARGING", "充电中");
                      // STARTING( "STARTING", "启动中");
                      // GUN_INSERTED ---插枪
                      // FINISHED ---已结束 -->
                  <el-progress
                    v-if="card.status == 'CHARGING' || card.status == 'STARTING'"
                    type="circle"
                    :percentage="card.soc"
                    width="98"
                    stroke-width="10"
                  />
                  <!-- <p class="free" v-if="card.status == 'CHARGING' && currentStation.operateStation">充电中</p> -->
                  <!-- <p class="free" v-if="card.status == 'STARTING' && currentStation.operateStation">启动中</p> -->
                  <p class="free" v-if="card.status == 'ONLINE'">空闲</p>
                  <p class="stop" v-if="card.status == 'OFFLINE'">离线</p>
                  <p class="error" v-if="card.status == 'FAULT'">故障</p>
                  <p class="insert" v-if="card.status == 'GUN_INSERTED'">插枪</p>
                  <p class="insert" v-if="card.status == 'FINISHED'">结束</p>
                </div>
                <div class="card-gun-info">
                  <div class="card-gun-info-title">
                    <img src="@/assets/images/realtime/icon1.png" alt="" />
                    <div class="card-gun-info-title-name">终端编号：{{ card.deviceNo }} - {{ card.gunNo }}</div>
                  </div>
                  <div class="card-gun-info-title">
                    <img src="@/assets/images/realtime/icon2.png" alt="" />
                    <div class="card-gun-info-title-name">电流：{{ card.outputCurrent || "-" }}</div>
                  </div>
                  <div class="card-gun-info-title">
                    <img src="@/assets/images/realtime/icon3.png" alt="" />
                    <div class="card-gun-info-title-name">电压：{{ card.outputVoltage || "-" }}</div>
                  </div>
                  <div class="card-gun-info-title" v-if="card.status == 'CHARGING' && currentStation.operateStation">
                    <img src="@/assets/images/realtime/icon4.png" alt="" />
                    <div class="card-gun-info-title-name">
                      已充
                      <span class="card-gun-info-status-label-highlight">{{ card.totalPower }}</span
                      >度，预计充满需要<span class="card-gun-info-status-label-highlight">{{ card.timeLeft }}</span
                      >分钟
                    </div>
                  </div>
                  <div
                    class="card-gun-info-status"
                    v-if="card.status == 'CHARGING' && card.tradeNo"
                    @click="goChargingOrderDetail(card)"
                  >
                    查看当前订单 >
                  </div>
                </div>
                <div
                  class="card-status-tag"
                  :class="{
                    'charging-tag': card.status == 'CHARGING' || card.status == 'STARTING',
                    'free-tag': card.status == 'ONLINE',
                    'stop-tag': card.status == 'OFFLINE',
                    'error-tag': card.status == 'FAULT',
                    'inserted-tag': card.status == 'GUN_INSERTED' || card.status == 'FINISHED',
                  }"
                >
                  <span v-if="card.status == 'CHARGING'">充电中</span>
                  <span v-if="card.status == 'STARTING'">启动中</span>
                  <span v-if="card.status == 'ONLINE'">空闲</span>
                  <span v-if="card.status == 'OFFLINE'">离线</span>
                  <span v-if="card.status == 'FAULT'">故障</span>
                  <span v-if="card.status == 'GUN_INSERTED'">插枪</span>
                  <span v-if="card.status == 'FINISHED'">结束</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getStationListAll, getRealTimeStatus } from "@/api/device/station"
import { onMounted } from "vue"
import { Search } from "@element-plus/icons-vue"
const { proxy } = getCurrentInstance()
const route = useRoute()
const router = useRouter()
const data = reactive({
  form: {},
  queryParams: {
    tenantId: undefined,
    stationId: "",
  },
})
const total = ref(10)
const tabIndex = ref("all")

const { queryParams, form, rules } = toRefs(data)
const loading = ref(true)
const stationOptionAll = ref([])
const stationOption = ref([])
const currentStation = ref({})

onMounted(() => {
  tabIndex.value = route.query.status || "all"
  getStationListAll({}).then((res) => {
    stationOptionAll.value = res.data
    stationOption.value = res.data
    if (res.data && res.data.length) {
      if (route.query.stationId) {
        queryParams.value.stationId = route.query.stationId
        setCurrentStation()
      } else {
        queryParams.value.stationId = res.data[0].id
        currentStation.value = res.data[0]
      }
      getData()
    } else {
      queryParams.value.stationId = ""
      currentStation.value = {}
    }
  })
})

function setCurrentStation() {
  for (let i = 0; i < stationOption.value.length; i++) {
    if (queryParams.value.stationId == stationOption.value[i].id) {
      currentStation.value = stationOption.value[i]
    }
  }
}

function changeStation(e) {
  if (!e) {
    stationOption.value = stationOptionAll.value
    return
  }
  stationOption.value = stationOptionAll.value.filter((item) => {
    return item.name.includes(e)
  })
}
function selectedStation(e) {
  currentStation.value = e
  queryParams.value.stationId = e.id
  getData()
}

const pileList = ref([])
function getData() {
  loading.value = true
  pileList.value = []
  getRealTimeStatus({
    stationId: queryParams.value.stationId || "-1",
  }).then((res) => {
    loading.value = false
    if (tabIndex.value == 0 || tabIndex.value == "all") {
      pileList.value = res.data.responseList || []
    } else {
      pileList.value = res.data.responseList
        ? res.data.responseList.filter((item) => {
            return item.status == tabIndex.value
          })
        : []
    }

    btnList.value.forEach((item) => {
      item.count = res.data[item.name]
    })
  })
}

const btnList = ref([
  {
    label: "全部",
    value: "all",
    name: "total",
  },
  {
    label: "空闲",
    value: "ONLINE",
    name: "online",
  },
  {
    label: "充电中",
    value: "CHARGING",
    name: "charging",
  },
  {
    label: "启动中",
    value: "STARTING",
    name: "starting",
  },
  {
    label: "已插枪",
    value: "GUN_INSERTED",
    name: "gunInserted",
  },
  {
    label: "离线",
    value: "OFFLINE",
    name: "offline",
  },
  {
    label: "故障",
    value: "FAULT",
    name: "fault",
  },
  {
    label: "已结束",
    value: "FINISHED",
    name: "finished",
  },
])

function changeTab(item) {
  pileList.value = []
  tabIndex.value = item.value
  getData()
}

function goChargingOrderDetail(row) {
  router.push({ path: "/detail/orderDetail?id=" + row.tradeNo })
}
</script>

<style lang="scss" scoped>
.empty-box {
  margin: 30px auto;
  text-align: center;
  img {
    width: 300px;
    display: block;
    margin: 0 auto;
  }
  .empty-text {
    color: #999;
  }
}
.card-box-header {
  height: 48px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  color: #242424;
  font-size: 16px;
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
.app-container {
  height: calc(100vh - 108px);
  display: flex;
  justify-content: flex-start;
  padding: 0;
  .search-wrapper {
    box-shadow: 0px 4px 20px 4px rgba(0, 0, 0, 0.09);
    border-radius: 9px;
    width: 268px;
    background: #fff;
    margin-right: 12px;
    .card-cont {
      padding: 12px;
    }
    .station-list {
      margin-top: 12px;
      overflow: hidden;
      overflow-y: auto;
      height: calc(100vh - 240px);
      .station-item,
      .station-item-active {
        height: 34px;
        line-height: 34px;
        border-radius: 3px;
        font-size: 14px;
        padding: 0 8px;
        overflow: hidden;
        cursor: pointer;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .station-item-active,
      .station-item:hover {
        background: #fcefe5;
      }
    }
  }
}

.btn-box {
  display: flex;
  text-align: center;
  .btn-item {
    height: 94px;
    width: 12%;
    background-color: rgba(255, 255, 255, 0.8);
    background-image: url("@/assets/images/home/box-bg.png");
    background-repeat: no-repeat;
    background-size: cover;
    border: 1px solid rgba(238, 220, 208, 0.3);
    border-radius: 9px;
    cursor: pointer;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
    overflow: hidden;
    position: relative;
    color: #111;
    letter-spacing: 1px;
    .item-label {
      font-size: 14px;
      line-height: 12px;
    }
    .item-count {
      font-size: 22px;
      margin-top: 8px;
    }
  }
  .btn-item:not(:last-of-type) {
    margin-right: 10px;
  }
  .btn-active {
    background: var(--el-color-primary);
    border: 1px solid var(--el-color-primary);
    color: #fff;
    font-weight: 600;
  }
}
.right-box {
  flex: 1;
  background: #fff;
  border-radius: 9px;
  .content-view {
    padding: 12px 20px;
    .station-name {
      display: inline-block;
      height: 38px;
      line-height: 38px;
      background: #fffaf6;
      border-radius: 3px;
      padding: 0 16px;
      color: #111111;
      font-size: 16px;
      margin-bottom: 17px;
    }
  }
  .title {
    font-size: 14px;
    font-weight: 600;
    color: rgb(54, 54, 70);
    padding-bottom: 16px;
  }
  .list-wrap {
    display: flex;
    justify-content: flex-start;
    align-items: flex-start;
    flex-wrap: wrap;
    gap: 12px;
    margin-top: 12px;
    height: calc(100vh - 350px);
    overflow: hidden;
    overflow-y: auto;
    .list-card {
      width: 440px;
      height: 170px;
      background: #ffffff;
      box-shadow: 0px 2px 7px 3px rgba(232, 232, 232, 0.4);
      border-radius: 9px;
      border: 1px solid #ececec;
      position: relative;
    }
    .card-status-tag {
      position: absolute;
      top: 0;
      left: 0;
      width: 64px;
      height: 30px;
      border-radius: 9px 0 9px 0;
      // background: var(--el-color-primary);
      color: #fff;
      text-align: center;
      line-height: 30px;
      font-size: 14px;
    }
    .charging-tag {
      background: #2acccc;
    }
    .free-tag {
      background: #05a7f0;
    }
    .stop-tag {
      background: #c9c9c9;
    }
    .error-tag {
      background: #f06767;
    }
    .inserted-tag {
      background: #ef6729;
    }
    .finished-tag {
      background: #c9c9c9;
    }

    .card-gun-basic {
      // width: 280px;
      height: 170px;
      padding-left: 24px;
      box-sizing: border-box;
      display: flex;
      align-items: center;

      .card-gun-info {
        position: relative;
        flex: 1;
        .card-gun-info-title {
          display: flex;
          align-items: center;
          margin-bottom: 9px;
          .card-gun-info-title-name {
            color: #111;
            font-size: 14px;
            line-height: 14px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-left: 8px;
          }
        }
        .card-gun-info-status-label-highlight {
          color: #2acccc;
          font-size: 14px;
          line-height: 14px;
          letter-spacing: 1px;
        }
        .card-gun-info-status {
          color: #05a7f0;
          font-size: 14px;
          line-height: 16px;
          margin-top: 12px;
        }
      }

      .card-gun-icon {
        width: 98px;
        margin-right: 30px;
        p {
          width: 98px;
          height: 98px;
          border-radius: 50%;
          font-weight: 700;
          color: #fff;
          margin: 0;
          background: #fff;
          text-align: center;
          line-height: 98px;
        }
        .free {
          background: var(--el-color-primary);
          background-image: url("@/assets/images/realtime/kongxian.png");
          background-size: cover;
        }
        .stop {
          background: rgba(255, 182, 64, 0.6);
          background-image: url("@/assets/images/realtime/lixian.png");
          background-size: cover;
        }
        .error {
          background: rgba(255, 0, 0, 0.6);
          background-image: url("@/assets/images/realtime/guzhang.png");
          background-size: cover;
        }
        .insert {
          background: rgba(255, 123, 0, 0.6);
          background-image: url("@/assets/images/realtime/chaqiang.png");
          background-size: cover;
        }
        .finished {
          background: #999;
          background-image: url("@/assets/images/realtime/chaqiang.png");
          background-size: cover;
        }
      }
    }

    .card-gun-detail {
      color: #9a9aa4;
      flex: 0 0 100%;
      font-size: 14px;
      line-height: 22px;
      margin-bottom: 15px;
      padding: 0 18px;
      position: absolute;
      bottom: -190px;
      background: #fff;
      left: 0;
      right: 0;
      padding-top: 12px;
      padding-bottom: 10px;
      border-radius: 0 0 12px 12px;
      z-index: 1;
      display: none;
      .kHYvDx {
        background: rgb(250, 250, 250);
        border-radius: 8px;
        padding: 16px 12px;
        margin-bottom: 6px;
      }
      .kHYvDx:last-child {
        margin-bottom: 16px;
      }
      .ebekII {
        background: rgb(250, 250, 250);
        border-radius: 8px;

        display: flex;
        .fzGKOB {
          font-size: 14px;
          letter-spacing: 0px;
          line-height: 20px;
          color: rgb(154, 154, 164);
          width: 68px;
        }
        .dgjogb {
          font-size: 14px;
          color: rgb(54, 54, 72);
          letter-spacing: 0px;
          line-height: 20px;
          flex: 1 1 0%;
          white-space: pre-line;
          display: flex;
          -webkit-box-align: center;
          align-items: center;
        }
      }
      .el-button {
        width: 100%;
      }
    }
  }
}
</style>
