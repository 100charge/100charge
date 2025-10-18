<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="策略名称" prop="name" label-width="68px">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入策略名称"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增策略</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格数据 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @expand-change="handleExpand"
      row-key="id"
      :expand-row-keys="expandRowKeys"
      border
      stripe
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column type="expand">
        <template #default="props">
          <div v-if="props.row.timePeriodList" style="padding-left: 60px">
            <el-table :data="props.row.timePeriodList" border>
              <el-table-column label="开始日期" prop="beginTime" align="center" />
              <el-table-column label="结束日期" prop="endTime" align="center" />
              <el-table-column label="操作" prop="action" align="center">
                <template #default="timePeriod">
                  <el-button
                    link
                    type="primary"
                    @click="
                      handleUpdateTime({
                        ruleId: props.row.id,
                        ...timePeriod.row,
                      })
                    "
                    >修改</el-button
                  >
                  <el-button link type="danger" @click="handleDeleteTime(timePeriod.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="计费策略名称" prop="name" align="left" />
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link icon="Edit" type="primary" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link icon="Plus" type="primary" @click="handlePrice(scope.row)">新增时段电价</el-button>
          <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <el-dialog :title="title" v-model="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formData" label-width="150px" :rules="rules">
        <el-form-item class="nav-form-item" label="计费策略名称" prop="name">
          <el-input
            maxlength="20"
            v-model="formData.name"
            style="width: 425px"
            placeholder="请输入计费策略名称"
            clearable
          />
        </el-form-item>
        <el-form-item class="nav-form-item" label="备注信息" prop="remark">
          <el-input
            type="textarea"
            maxlength="200"
            v-model="formData.remark"
            style="width: 425px"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <ruleModal ref="ruleModalRef" @successful="getList" />
  </div>
</template>

<script setup name="PriceRule">
import { getTimePeriod } from "@/utils/index"
import { deepClone } from "@/utils/index"
import ruleApi from "@/api/rule/rule"
import ruleModal from "./ruleModal.vue"
import { reactive } from "vue"
const router = useRouter()
const { proxy } = getCurrentInstance()
const txtArray = ["", "尖", "峰", "平", "谷"]
const colorArray = ["", "#f56c6c", "#e6a23c", "#67c23a", "#909399"]
const priceTypeList = reactive([
  {
    id: "",
    priceType: 1,
    price: 1,
    servicePrice: 0.8,
    parkFee: 1,
    occupancyFee: 1,
    txt: "尖时段",
    cor: "#f56c6c",
  },
  {
    id: "",
    priceType: 2,
    price: 1,
    servicePrice: 0.8,
    parkFee: 1,
    occupancyFee: 1,
    txt: "峰时段",
    cor: "#e6a23c",
  },
  {
    id: "",
    priceType: 3,
    price: 1,
    servicePrice: 0.8,
    parkFee: 1,
    occupancyFee: 1,
    txt: "平时段",
    cor: "#67c23a",
  },
  {
    id: "",
    priceType: 4,
    price: 1,
    servicePrice: 0.8,
    parkFee: 1,
    occupancyFee: 1,
    txt: "谷时段",
    cor: "#909399",
  },
])

const tableData = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const total = ref(0)
const title = ref("")
const timePeriodList = ref([])
const formData = ref({
  chargeType: "",
  ruleType: "",
  name: "",
  priceList: [],
})
const ruleModalRef = ref()
const dialogVisible = ref(false)
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
  },
  rules: {
    name: [{ required: true, message: "计费策略名称不能为空", trigger: "blur" }],
  },
})
const { queryParams, form, rules } = toRefs(data)

const expandRowKeys = ref([])
function handleExpand(e) {
  if (expandRowKeys.value.includes(e.id)) {
    expandRowKeys.value = []
  } else {
    expandRowKeys.value = [e.id]
  }
  if (e.timePeriodList) {
    return
  }
  ruleApi
    .listRuleTimeByRuleId({
      id: e.id,
    })
    .then((res) => {
      e.timePeriodList = res.data || []
    })
}

/** 查询列表 */
function getList() {
  expandRowKeys.value = []
  loading.value = true
  ruleApi.pageRule(queryParams.value).then((response) => {
    tableData.value = response.rows
    total.value = response.total
    loading.value = false
  })
}
/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {}
  proxy.resetForm("queryRef")
  handleQuery()
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 删除按钮操作 */
function handleDelete(row) {
  const names = row.name
  proxy.$modal
    .confirm('是否确认删除"' + names + '"的数据项?')
    .then(function () {
      return ruleApi.delRule({ id: row.id })
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

/** 重置新增的表单以及其他数据  */
function reset() {
  form.value = {
    name: undefined,
  }
  proxy.resetForm("formRef")
}
// 维护时段电价
function handlePrice(row) {
  ruleModalRef.value.onOpen({
    ruleId: row.id,
  })
}
function handleUpdateTime(row) {
  ruleModalRef.value.onOpen(row)
}
function handleDeleteTime(row) {
  proxy.$modal
    .confirm("是否确认删除该数据项?")
    .then(function () {
      return ruleApi.delRuleTime({ id: row.id })
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
/** 添加 */
function handleAdd() {
  reset()
  dialogVisible.value = true
  title.value = "添加计费策略"
  formData.value = {
    name: "",
  }
}
function handleUpdate(row) {
  reset()
  ruleApi
    .getRuleDetail({
      id: row.id,
    })
    .then((res) => {
      formData.value = res.data
      title.value = "修改计费策略"
      dialogVisible.value = true
      // let ruleDetailList = res.data.ruleDetailList
      // timePeriodList.value = ruleDetailList.map((item) => {
      //   return {
      //     ...item,
      //     value: item.timePeriod,
      //     time: getTimePeriod(item.timePeriod),
      //     priceType: item.type + 1,
      //     selected: false,
      //     id: item.id,
      //   }
      // })
      // let typeNameArr = ["sharp", "peak", "flat", "valley"]
      // priceTypeList.forEach((item, index) => {
      //   item.price = res.data[typeNameArr[index] + "Fee"]
      //   item.occupancyFee = res.data[typeNameArr[index] + "OccupancyFee"]
      //   item.parkFee = res.data[typeNameArr[index] + "ParkingFee"]
      //   item.servicePrice = res.data[typeNameArr[index] + "ServiceFee"]
      // })
    })
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      let params = {
        name: formData.value.name,
        id: formData.value.id,
        tenantId: formData.value.tenantId || "",
        remark: formData.value.remark,
        flatFee: priceTypeList[2].price, //平时段
        flatOccupancyFee: priceTypeList[2].occupancyFee,
        flatParkingFee: priceTypeList[2].parkFee,
        flatServiceFee: priceTypeList[2].servicePrice,
        peakFee: priceTypeList[1].price, //峰时段
        peakOccupancyFee: priceTypeList[1].occupancyFee,
        peakParkingFee: priceTypeList[1].parkFee,
        peakServiceFee: priceTypeList[1].servicePrice,

        sharpFee: priceTypeList[0].price, //尖时段
        sharpOccupancyFee: priceTypeList[0].occupancyFee,
        sharpParkingFee: priceTypeList[0].parkFee,
        sharpServiceFee: priceTypeList[0].servicePrice,
        valleyFee: priceTypeList[3].price, //谷时段
        valleyOccupancyFee: priceTypeList[3].occupancyFee,
        valleyParkingFee: priceTypeList[3].parkFee,
        valleyServiceFee: priceTypeList[3].servicePrice,
        ruleDetailList: timePeriodList.value.map((item) => {
          return {
            timePeriod: item.value,
            type: item.priceType - 1,
            id: item.id || "",
          }
        }),
      }
      if (formData.value.id) {
        ruleApi.editRule(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          dialogVisible.value = false
          getList()
        })
      } else {
        ruleApi.addRule(params).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          dialogVisible.value = false
          getList()
        })
      }
    }
  })
}
/** 取消按钮 */
function cancel() {
  dialogVisible.value = false
  reset()
}

function setTimeType(type) {
  let arr = timePeriodList.value.filter((f) => {
    return f.selected
  })
  if (arr.length === 0) {
    proxy.$modal.msgWarning("请至少选择一个时段")
    return
  }
  arr.forEach((element) => {
    element.priceType = type
    element.selected = false
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
//:deep(.el-dialog__body) {
//  height: 75vh;
//  overflow: hidden;
//  overflow-y: auto;
//}
</style>
