<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="订单号" prop="tradeNo">
        <el-input v-model="queryParams.tradeNo" clearable style="width: 260px" placeholder="订单号"></el-input>
      </el-form-item>
      <el-form-item label="用户来源" prop="userSource">
        <el-select filterable v-model="queryParams.userSource" placeholder="用户来源" style="width: 260px">
          <el-option v-for="dict in account_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="queryParams.mobile" clearable style="width: 260px" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="开始日期">
        <el-date-picker
          v-model="queryParams.startTime"
          placeholder="开始日期"
          value-format="YYYY-MM-DD"
          style="width: 260px"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期">
        <el-date-picker
          v-model="queryParams.endTime"
          placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="width: 260px"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="roleList" border stripe>
      <el-table-column type="index" width="60" label="序号" align="center" fixed="left" />

      <el-table-column label="充值订单号" prop="tradeNo" :show-overflow-tooltip="true" align="center" min-width="140" />
      <el-table-column label="用户来源" prop="userSource" min-width="140" align="center">
        <template #default="scope">
          <span>{{ selectDictLabel(account_type, scope.row.userSource) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额/元" prop="orderAmount" align="center" min-width="140" />
      <el-table-column label="用户唯一标识" prop="openId" min-width="140" align="center" />
      <el-table-column label="账户手机号" prop="phone" min-width="140" align="center"> </el-table-column>
      <el-table-column label="操作时间" prop="operateTime" align="center" min-width="140" />
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

<script setup name="AccountRecharge">
import { ref, reactive, toRefs, getCurrentInstance } from "vue"
import { useRoute, useRouter } from "vue-router"
import reportApi from "@/api/report/api"

const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const { account_type } = proxy.useDict("account_type")
const roleList = ref([])
const loading = ref(false)
const showSearch = ref(true)
const total = ref(0)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userSource: undefined,
    tradeNo: "",
    mobile: "",
    startTime: "",
    endTime: "",
  },
})

const { queryParams, form } = toRefs(data)

/** 查询列表 */
function getList() {
  loading.value = true
  reportApi.accountRechargeDetailList(queryParams.value).then((response) => {
    roleList.value = response.rows || []
    total.value = response.total
    loading.value = false
  })
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = 10
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {}
  proxy.resetForm("queryRef")
  handleQuery()
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "reportForm/accountRechargeDetailExport",
    {
      ...queryParams.value,
    },
    `账户充值明细报表_${new Date().getTime()}.xlsx`
  )
}
console.log("mounted")
getList()
</script>

<style lang="scss" scoped>
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
</style>
