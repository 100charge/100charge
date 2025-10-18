<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="手机号码" prop="phoneNumber">
        <el-input
          v-model="queryParams.phoneNumber"
          placeholder="请输入手机号码"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 240px">
          <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="用户来源" prop="type">
        <el-select v-model="queryParams.type" placeholder="用户来源" clearable style="width: 240px">
          <el-option v-for="dict in app_user_category" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <span style="font-size: 14px; color: #f56c6c">提示：小程序注册用户列表</span>
      </el-col>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <!-- <el-table-column type="selection" width="50" align="center" /> -->
      <el-table-column type="index" width="80" label="序号" align="center" />
      <el-table-column label="手机号" prop="phoneNumber" align="center" />
      <el-table-column label="昵称" prop="nickName" align="center" />
      <el-table-column label="用户来源" prop="typeDesc" align="center" />
      <el-table-column label="账户余额" prop="userBalance" align="center" />
      <el-table-column label="状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status == 0 ? 'success' : 'info'">{{ scope.row.statusDesc }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="注册时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button v-if="scope.row.status == 0" link type="primary" @click="handleStatusChange(scope.row)"
            >冻结</el-button
          >
          <el-button v-else link type="danger" @click="handleStatusChange(scope.row)">解冻</el-button>
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

<script setup name="PriceRule">
import appUserApi from "@/api/customer/appuser"
import { reactive } from "vue"
const sys_normal_disable = [
  { label: "正常", value: "NORMAL" },
  { label: "停用", value: "DISABLED" },
]
const app_user_category = [
  { label: "支付宝", value: "ALIPAY" },
  { label: "微信", value: "WECHAT" },
]
const router = useRouter()
const { proxy } = getCurrentInstance()

const tableData = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    status: undefined,
    type: undefined,
    phoneNumber: undefined,
  },
})
const { queryParams, form } = toRefs(data)

/** 选择条数  */
const ids = ref([])
const userNames = ref([])
const single = ref(true)
const multiple = ref(true)
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id)
  userNames.value = selection.map((item) => item.phoneNumber)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "appUser/miniProgramUserExport",
    {
      ...queryParams.value,
    },
    `小程序用户_${new Date().getTime()}.xlsx`
  )
}
/** 查询列表 */
function getList() {
  loading.value = true
  appUserApi.page(queryParams.value).then((response) => {
    tableData.value = response.rows.map((item) => {
      return {
        ...item,
        status: item.status.toString(),
      }
    })
    total.value = response.total
    loading.value = false
  })
}
function resetQuery() {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    status: undefined,
    type: undefined,
    phoneNumber: undefined,
  }
  handleQuery()
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 状态修改 */
function handleStatusChange(row) {
  let text = row.status == "0" ? "冻结" : "解冻"
  proxy.$modal
    .confirm('确认要"' + text + '""' + row.nickName + '"吗?')
    .then(function () {
      return appUserApi.appUserDisableOrEnable({
        id: row.id,
      })
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功")
      getList()
    })
    .catch(function () {
      row.status = row.status == "0" ? "1" : "0"
    })
}

getList()
</script>

<style lang="scss" scoped>
.time-item {
  margin-right: 12px;
  border: 1px solid #eaeaea;
  border-radius: 4px;
  text-align: center;
  margin-bottom: 12px;
  width: 100px;
  position: relative;
  cursor: pointer;
  .time-type-div {
    border-radius: 4px 4px 0 0;
  }
  .sel-img {
    position: absolute;
    width: 15px;
    height: 15px;
    background: #fff;
    border-radius: 2px;
    left: 6px;
    top: 6px;
  }
}
</style>
