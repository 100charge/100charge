<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="手机号码" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="手机号码"></el-input>
      </el-form-item>
      <el-form-item label="操作类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="操作类型" clearable style="width: 240px">
          <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <span style="font-size: 14px; color: #f56c6c">提示：小程序用户余额变动记录</span>
      </el-col>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="roleList" border stripe>
      <el-table-column label="单号" prop="tradeNo" />
      <!-- <el-table-column label="openId" prop="openId" width="180" /> -->
      <!-- <el-table-column label="用户名称" prop="createBy">
        <template #default="scope">
          <span>{{ scope.row.createBy || "--" }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="用户手机号" prop="phone" />

      <el-table-column label="用户类型" prop="userTypeDesc" />
      <el-table-column label="场站名称" prop="stationName" />
      <el-table-column label="余额" prop="lastAmount" />
      <el-table-column label="变动金额" prop="amount" />
      <el-table-column label="操作类型" prop="typeDesc" />
      <el-table-column label="操作时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
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

<script setup name="AppUserBalance">
import appUserApi from "@/api/customer/appuser"
const router = useRouter()
const { proxy } = getCurrentInstance()
const sys_normal_disable = [
  { label: "充值", value: "0" },
  { label: "消费", value: "1" },
  { label: "提现", value: "2" },
  { label: "订单退款", value: "3" },
]

const roleList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    phone: undefined,
    type: undefined,
  },
})

const { queryParams } = toRefs(data)

/** 查询车队列表 */
function getList() {
  loading.value = true
  appUserApi.appUserBalance(queryParams.value).then((response) => {
    roleList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

getList()
</script>
