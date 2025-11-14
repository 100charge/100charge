<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="所属场站">
        <el-select v-model="queryParams.stationId" filterable placeholder="所属场站" clearable style="width: 240px">
          <el-option v-for="dict in stationsList" :key="dict.value" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单编号">
        <el-input
          v-model="queryParams.tradeNo"
          placeholder="请输入订单编号"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="queryParams.startTime"
          type="datetime"
          placeholder="开始时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 240px"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
          v-model="queryParams.endTime"
          type="datetime"
          placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 240px"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select v-model="queryParams.orderState" placeholder="订单状态" clearable style="width: 240px">
          <el-option v-for="dict in order_state" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态">
        <el-select v-model="queryParams.payStatus" placeholder="支付状态" clearable style="width: 240px">
          <el-option v-for="dict in pay_state" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单来源">
        <el-select v-model="queryParams.orderSource" placeholder="订单来源" clearable style="width: 240px">
          <el-option v-for="dict in order_source" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <div class="bcukdi">
      <div class="cell">
        <div>
          <div class="dhcTPV">订单量(单)</div>
          <div class="bMyACg">{{ statistics.orderCount || 0 }}</div>
        </div>
      </div>
      <div class="cell" style="background-color: #fff0e5; padding: 20px 12px; border-radius: 6px">
        <div style="padding-right: 24px; border-right: 1px solid #dfdfdf">
          <div class="dhcTPV">
            <span class="anticon jgBmSv">
              <el-tooltip placement="top">
                <template #content>
                  订单总额：筛选时间内所有订单的“订单金额之和”，未扣除技术服务费、营销活动等费用。<br />
                  充电电费：筛选时间内所有的订单“充电电费之和”。<br />
                  充电服务费：筛选时间内所有订单的“充电服务费之和”，未扣除技术服务费、营销活动等费用。</template
                >
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
              订单总额(元)</span
            >
          </div>
          <div class="bMyACg">{{ statistics.orderTotalAmount || 0 }}</div>
        </div>
        <div style="display: flex; padding-top: 2px">
          <div class="YBYzT">
            <div class="cdwtFp">充电电费(元)</div>
            <div class="laFOyh">{{ statistics.electricityFee || 0 }}</div>
          </div>
          <div class="YBYzT">
            <div class="cdwtFp">充电服务费(元)</div>
            <div class="laFOyh">{{ statistics.serviceFee || 0 }}</div>
          </div>
          <div class="YBYzT">
            <div class="cdwtFp">第三方订单总额(元)</div>
            <div class="laFOyh">{{ statistics.thirdPartyOrderTotalAmount || 0 }}</div>
          </div>
          <div class="YBYzT">
            <div class="cdwtFp">第三方订单电费(元)</div>
            <div class="laFOyh">{{ statistics.thirdPartyElectricityFee || 0 }}</div>
          </div>
          <div class="YBYzT">
            <div class="cdwtFp">第三方订单服务费(元)</div>
            <div class="laFOyh">{{ statistics.thirdPartyServiceFee || 0 }}</div>
          </div>
        </div>
      </div>
      <div class="cell">
        <div>
          <div class="dhcTPV">充电量(度)</div>
          <div class="bMyACg">{{ statistics.chargingCapacity || 0 }}</div>
        </div>
      </div>
      <div class="cell">
        <div>
          <div class="dhcTPV">充电时长</div>
          <div class="bMyACg">{{ statistics.chargingDuration || 0 }}</div>
        </div>
      </div>
    </div>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column label="订单编号" align="center" prop="tradeNo" min-width="210" />
      <el-table-column label="场站" align="center" prop="stationName" min-width="200" />
      <el-table-column
        label="充电状态"
        align="center"
        prop="orderStateDesc"
        :show-overflow-tooltip="true"
        min-width="140"
      />
      <el-table-column
        label="支付状态"
        align="center"
        prop="payStatusDesc"
        :show-overflow-tooltip="true"
        min-width="140"
      />
      <el-table-column label="充电量/度" align="center" prop="totalPower" min-width="130" />
      <el-table-column label="订单费用" align="center" prop="totalAmount" min-width="130" />
      <el-table-column label="电费" align="center" prop="chargeFee" min-width="130" />
      <el-table-column label="服务费" align="center" prop="serviceFee" min-width="130" />
      <el-table-column label="充电开始时间" align="center" prop="startTime" min-width="160" />
      <el-table-column label="充电结束时间" align="center" prop="endTime" min-width="160" />
      <el-table-column label="支付时间" align="center" prop="payTime" min-width="160" />
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="goDetail(scope.row)">订单详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="ChargingOrder/index">
import { useRoute, useRouter } from "vue-router"
import { getChargingOrderList, updateStations } from "@/api/business/chargingOrder"
import { getStationsList } from "@/api/device/station"
import download from "@/plugins/download.js"
const { proxy } = getCurrentInstance()
const { order_state } = proxy.useDict("order_state")
const { order_source } = proxy.useDict("order_source")
const pay_state = [
  // NOT_PAY（未支付）、PAY_SUCCESS（支付成功）、PAYMENT_SETTLEMENT（支付结算）
  {
    label: "未支付",
    value: "NOT_PAY",
  },
  {
    label: "支付成功",
    value: "PAY_SUCCESS",
  },
  {
    label: "支付结算",
    value: "PAYMENT_SETTLEMENT",
  },
]

const route = useRoute()
const router = useRouter()
const { params, query } = route
const showSearch = ref(true)
const dataList = ref([])
const statistics = ref({})
const loading = ref(true)
const stationsList = ref([])

const dateRange = ref([])
const multiple = ref(true)
const total = ref(0)
const single = ref(true)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    status: undefined,
    platform: undefined,
    outTradeNo: "",
  },
  rules: {},
})
const { queryParams, form, rules } = toRefs(data)

/** 列表 */
function getList() {
  loading.value = true
  getChargingOrderList(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    dataList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}
// 充电订单统计
function getUpdateStations() {
  updateStations(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    statistics.value = response.data
  })
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = 10
  getList()
  getUpdateStations()
}
/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {}
  dateRange.value = []
  queryParams.value.orderState = undefined
  queryParams.value.offline = undefined
  queryParams.value.startTime = undefined
  queryParams.value.endTime = undefined
  queryParams.value.orderSource = undefined
  queryParams.value.stationId = undefined
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id)
  names.value = selection.map((item) => item.name)
  single.value = selection.length != 1
  multiple.value = !selection.length
}
function goDetail(row) {
  router.push({ path: "/detail/orderDetail", query: { id: row.id } })
}

// 获取场站
function getStations() {
  getStationsList({ pageSize: 9999, pageNum: 1 }).then((response) => {
    stationsList.value = response.rows
  })
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "chargingOrder/export",
    {
      ...queryParams.value,
    },
    `充电订单_${new Date().getTime()}.xlsx`
  )
}
getList()
getUpdateStations()
getStations()
</script>
<style lang="scss" scoped>
@import "@/assets/styles/variables.module.scss";
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
.bcukdi {
  padding: 12px 0px;
  color: rgb(54, 54, 70);
  border-radius: 8px;
  border: 1px solid var(--el-color-primary);
  // background: rgba(64, 158, 255, 0.15);
  background: $--color-primary-bg-opacity;
  margin: 0px 0px 14px;
  @extend .dis-flex-ac;
  // .cell:not(:first-of-type) {
  //   border-left: 1px solid var(--el-color-primary);
  // }
  .cell {
    -webkit-box-flex: 1;
    flex-grow: 1;
    box-sizing: border-box;
    padding: 0px 32px;
    display: flex;
    .dhcTPV {
      position: relative;
      text-align: left;
      font-size: 14px;
      line-height: 14px;
    }
    .bMyACg {
      font-size: 20px;
      line-height: 20px;
      margin-top: 8px;
    }
    .laFOyh {
      font-size: 16px;
      color: rgb(54, 54, 70);
      line-height: 16px;
      margin-top: 10px;
    }
    .YBYzT {
      margin-left: 20px;
      margin-right: 16px;
      padding-left: 10px;
      text-align: center;
    }
    .cdwtFp {
      font-size: 12px;
      color: rgb(114, 114, 125);
      line-height: 14px;
    }
    .laFOyh {
      font-size: 16px;
      color: rgb(54, 54, 70);
      line-height: 16px;
      margin-top: 10px;
    }
  }
}
</style>
