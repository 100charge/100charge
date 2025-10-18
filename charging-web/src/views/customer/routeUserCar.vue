<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="车牌号" prop="plateNo">
        <el-input
          v-model="queryParams.plateNo"
          placeholder="请输入车牌号"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="认证状态" prop="verified">
        <el-select v-model="queryParams.verified" placeholder="认证状态" clearable style="width: 240px">
          <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="tableData" border stripe>
      <el-table-column type="index" width="80" label="序号" align="center" />
      <el-table-column label="车牌号" prop="plateNo" align="center" />
      <el-table-column label="车辆用途" prop="usage" align="center" />
      <el-table-column label="VIN" prop="vin" align="center" />
      <el-table-column label="认证状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.verified == 2 ? 'success' : scope.row.verified == 1 ? 'primary' : 'info'">{{
            scope.row.verifiedDesc
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="用户手机号" prop="phone" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" @click="handleDetail(scope.row)">详情</el-button>
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

    <el-dialog v-model="dialogVisible" width="900px">
      <el-descriptions title="车辆信息" :column="3" border>
        <el-descriptions-item label="车牌号：">{{ formData.plateNo || "--" }}</el-descriptions-item>
        <el-descriptions-item label="车辆用途：">{{ formData.usage || "--" }}</el-descriptions-item>
        <el-descriptions-item label="VIN：">{{ formData.vin || "--" }}</el-descriptions-item>
        <el-descriptions-item label="是否默认车辆：">{{ formData.defaultVehicle || "--" }}</el-descriptions-item>
        <el-descriptions-item label="照片信息：">
          <ImagePreview v-if="formData.image" :src="formData.image" height="50px" width="50px" />
          <span v-else>--</span>
        </el-descriptions-item>
        <el-descriptions-item label="用户手机号：">{{ formData.phone || "--" }}</el-descriptions-item>
        <el-descriptions-item label="用户openID：">{{ formData.openId || "--" }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PriceRule">
import appCarApi from "@/api/customer/appcar"
import { reactive } from "vue"
// 0:未认证，1：审核中，2：已认证，3：认证失败
const sys_normal_disable = [
  { label: "未认证", value: "0" },
  { label: "审核中", value: "1" },
  { label: "已认证", value: "2" },
  { label: "认证失败", value: "3" },
]
const router = useRouter()
const { proxy } = getCurrentInstance()

const tableData = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    status: undefined,
    type: undefined,
    phoneNumber: undefined,
  },
})
const { queryParams } = toRefs(data)
const formData = ref({})
const dialogVisible = ref(false)

function handleDetail(row) {
  formData.value = row
  dialogVisible.value = true
}

/** 取消按钮 */
function cancel() {
  dialogVisible.value = false
}

/** 查询列表 */
function getList() {
  loading.value = true
  appCarApi.page(queryParams.value).then((response) => {
    tableData.value = response.rows
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
