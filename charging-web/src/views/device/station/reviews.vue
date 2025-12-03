<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="所属场站">
        <el-select v-model="queryParams.stationId" filterable placeholder="所属场站" clearable style="width: 240px">
          <el-option v-for="dict in stationsList" :key="dict.value" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单编号">
        <el-input v-model="queryParams.tradeNo" placeholder="请输入订单编号" clearable style="width: 240px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker v-model="queryParams.startTime" type="datetime" placeholder="开始时间"
          value-format="YYYY-MM-DD HH:mm:ss" style="width: 240px"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker v-model="queryParams.endTime" type="datetime" placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss" style="width: 240px"></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="reviewsList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column label="场站名称" align="center" prop="chargingStationName" width="220" />
      <el-table-column label="订单号" align="center" prop="orderId" />
      <el-table-column label="用户" align="center" prop="appUser" />
      <el-table-column label="标签" align="center" prop="ratingString" />
      <el-table-column label="评价时间" align="center" prop="createTime" />
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router"
import {
  getStationsList,
  getStationsReviewsList,
} from "@/api/device/station"

import { getRuleList, getProvinceList, getCityListByProvinceId, getDistrictListByCityId } from "@/api/common/common"
import ImageUpload from "@/components/ImageUpload"
import { blobValidate } from "@/utils/ruoyi"
import download from "@/plugins/download.js"

const { proxy } = getCurrentInstance()

const route = useRoute()
const router = useRouter()
const { params, query } = route
const stationsList = ref([])
const reviewsList = ref([])
const loading = ref(true)

const dateRange = ref([])

const total = ref(0)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    status: undefined,
  }
})
const { queryParams, form, rules } = toRefs(data)

/**评价列表 */
function getList() {
  loading.value = true
  getStationsReviewsList(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    reviewsList.value = response.rows;
    total.value = response.total
    loading.value = false
  })
}

// 获取场站
function getStations() {
  getStationsList({ pageSize: 9999, pageNum: 1 }).then((response) => {
    stationsList.value = response.rows
  })
}
/** 表单重置 */
function reset() {
  form.value = {
    name: undefined,
    // provider: undefined,
    status: "1",
    logoImage: undefined,
    licenseImage: undefined,
    province: undefined,
    city: undefined,
    region: undefined,
    address: undefined,
  }
  proxy.resetForm("dictRef")
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

getList()
getStations()
</script>

<style lang="scss" scoped>
.address-input {
  :deep(.el-input__wrapper) {
    padding-right: 0;
  }
}
</style>
