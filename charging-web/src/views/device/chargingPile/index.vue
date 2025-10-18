<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="桩名称">
        <el-input
          v-model="queryParams.chargingPileName"
          placeholder="请输入桩名称"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="桩编号">
        <el-input
          v-model="queryParams.deviceNo"
          placeholder="请输入桩编号"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属场站">
        <el-select v-model="queryParams.stationId" placeholder="请选择" clearable style="width: 240px">
          <el-option v-for="item in stationsList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态">
        <el-select v-model="queryParams.pileStatus" placeholder="设备状态" clearable style="width: 240px">
          <el-option value="1" label="启用" />
          <el-option value="0" label="停用" />
        </el-select>
      </el-form-item>
      <el-form-item label="充电类型">
        <el-select v-model="queryParams.pileType" placeholder="充电桩类型" clearable style="width: 240px">
          <el-option value="1" label="直流" />
          <el-option value="0" label="交流" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" border stripe>
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="桩名称" align="center" min-width="120" prop="chargingPileName" />
      <el-table-column label="桩编号" min-width="120" align="center" prop="deviceNo" />
      <el-table-column label="所属场站" align="center" prop="stationName" />
      <el-table-column label="枪数量" align="center" prop="gunNumber" />
      <el-table-column label="最大输出功率" align="center" prop="maxPower" />
      <el-table-column label="最小输出功率" align="center" prop="minPower" />
      <el-table-column label="充电类型" align="center" prop="pileTypeDesc" />
      <el-table-column label="设备状态" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.pileStatus"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" @click="goDetail(scope.row)">详情</el-button>
          <el-button link type="success" icon="Download" @click="handleDownload(scope.row)">下载二维码</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 新建场站/修改对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="1000px"
      append-to-body
      :close-on-click-modal="false"
      style="padding-right: 48px"
    >
      <el-form ref="dictRef" :model="form" :rules="rules" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属场站" prop="stationId">
              <el-select v-model="form.stationId" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in stationsList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="桩名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="充电桩类型" prop="pileType">
              <el-radio-group v-model="form.pileType">
                <el-radio label="1">直流</el-radio>
                <el-radio label="0">交流</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="桩编号" prop="deviceNo">
              <el-input v-model="form.deviceNo" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备接口类型" prop="connectorType">
              <el-select v-model="form.connectorType" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in connectorTypeEumn" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家标准" prop="nationalStandard">
              <el-select v-model="form.nationalStandard" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in nationalStandardEumn" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="!form.id">
            <el-form-item label="枪数量" prop="gunNumber">
              <el-input v-model="form.gunNumber" placeholder="请输入" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="硬件版本" prop="hardwareVersion">
              <el-input v-model="form.hardwareVersion" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大输出电流" prop="maxCurrent">
              <el-input v-model="form.maxCurrent" type="number" placeholder="请输入" :min="0" :max="900" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小输出电流" prop="minCurrent">
              <el-input v-model="form.minCurrent" type="number" placeholder="请输入" :min="0" :max="900" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大输出功率" prop="maxPower">
              <el-input
                style="width: 90%; margin-right: 10px"
                v-model="form.maxPower"
                type="number"
                placeholder="请输入"
                :min="0"
                :max="360"
              />kW
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小输出功率" prop="minPower">
              <el-input
                style="width: 90%; margin-right: 10px"
                v-model="form.minPower"
                type="number"
                placeholder="请输入"
                :min="0"
                :max="360"
              />
              kW
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大输出电压" prop="maxVoltage">
              <el-input v-model="form.maxVoltage" type="number" placeholder="请输入" :min="0" :max="1000" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小输出电压" prop="minVoltage">
              <el-input v-model="form.minVoltage" type="number" placeholder="请输入" :min="0" :max="1000" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协议版本" prop="protocolVersion">
              <el-select v-model="form.protocolVersion" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="item in pile_protocol_version"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产厂商" prop="supplier">
              <el-input v-model="form.supplier" placeholder="请输入" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="form.remark" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router"
import { getStationListAll } from "@/api/device/station"
import {
  addChargingPile,
  getChargingPileList,
  getChargingPileDetail,
  updateChargingPile,
  delChargingPile,
  updatePileStatus,
  generateQRCodeZip,
} from "@/api/device/chargingPile"
import { ElMessage } from "element-plus"
import { blobValidate } from "@/utils/ruoyi"
import download from "@/plugins/download.js"

const { proxy } = getCurrentInstance()

const route = useRoute()
const router = useRouter()
const { params } = route
const { path } = params
const dataList = ref([])
const loading = ref(true)
const ids = ref([])
const names = ref([])
const dateRange = ref([])
const multiple = ref(true)
const total = ref(0)
const single = ref(true)
const open = ref(false)
const title = ref("")
const stationsList = ref([])
// 协议版本
const { pile_protocol_version } = proxy.useDict("pile_protocol_version")
const connectorTypeEumn = [
  { id: 1, name: "家用插座（模式2）" },
  { id: 2, name: "交流接口插座（模式3，连接方式B ）" },
  { id: 3, name: "交流接口插头（带枪线，模式3，连接方式C）" },
  { id: 4, name: "直流接口枪头（带枪线，模式4）" },
  { id: 5, name: "无线充电座" },
  { id: 6, name: "其他" },
]
const nationalStandardEumn = [
  { id: 1, name: "2011" },
  { id: 2, name: "2015" },
]

function handleDownload(row) {
  generateQRCodeZip({
    pileNo: row.deviceNo,
  })
    .then((res) => {
      const isBlob = blobValidate(res)
      if (isBlob) {
        const blob = new Blob([res], { type: "application/zip" })
        download.saveAs(blob, name)
      } else {
        res.text().then((text) => {
          const rspObj = JSON.parse(text)
          const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode["default"]
          ElMessage.error(errMsg || "下载失败！")
        })
      }
    })
    .catch((e) => {
      ElMessage.error(e.data.msg || "下载失败！")
    })
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    status: undefined,
  },
  rules: {
    name: [{ required: true, message: "桩名称不能为空", trigger: "blur" }],
    protocolVersion: [{ required: true, message: "协议版本不能为空", trigger: "blur" }],
    gunNumber: [{ required: true, message: "枪数量不能为空", trigger: "blur" }],
    pileType: [{ required: true, message: "充电桩类型不能为空", trigger: "change" }],
    deviceNo: [
      { required: true, message: "桩编号不能为空", trigger: "blur" },
      { min: 8, message: "桩编号最少8位" },
    ],
    hardwareVersion: [{ required: true, message: "硬件版本不能为空", trigger: "blur" }],
    maxCurrent: [{ required: true, message: "最大输出电流不能为空", trigger: "blur" }],
    minCurrent: [{ required: true, message: "最小输出电流不能为空", trigger: "blur" }],
    maxPower: [{ required: true, message: "最大输出功率不能为空", trigger: "blur" }],
    minPower: [{ required: true, message: "最小输出功率不能为空", trigger: "blur" }],
    maxVoltage: [{ required: true, message: "最大输出电压不能为空", trigger: "blur" }],
    minVoltage: [{ required: true, message: "最小输出电压不能为空", trigger: "blur" }],
    stationId: [{ required: true, message: "所属场站不能为空", trigger: "change" }],
    supplier: [{ required: true, message: "生产厂商不能为空", trigger: "blur" }],
    connectorType: [{ required: true, message: "设备接口类型", trigger: "blur" }],
    nationalStandard: [{ required: true, message: "国家标准不能为空", trigger: "blur" }],
  },
})
const { queryParams, form, rules } = toRefs(data)

/** 表格列表 */
function getList() {
  loading.value = true
  getChargingPileList(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    dataList.value = response.rows
      ? response.rows.map((item) => {
          return {
            ...item,
            pileStatus: item.pileStatus.toString(),
          }
        })
      : []
    // pileStatus
    total.value = response.total
    loading.value = false
  })
}

/** 表单重置 */
function reset() {
  form.value = {
    name: undefined,
    pileType: undefined,
    deviceNo: undefined,
    gunNumber: undefined,
    hardwareVersion: undefined,
    maxCurrent: undefined,
    minCurrent: undefined,
    maxPower: undefined,
    minPower: undefined,
    maxVoltage: undefined,
    minVoltage: undefined,
    protocolVersion: undefined,
    supplier: undefined,
    stationId: undefined,
    remark: undefined,
  }
  proxy.resetForm("dictRef")
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  queryParams.value.chargingPileName = undefined
  queryParams.value.deviceNo = undefined
  queryParams.value.pileStatus = undefined
  queryParams.value.pileType = undefined
  queryParams.value.stationName = undefined
  queryParams.value.stationId = undefined
  proxy.resetForm("queryRef")
  handleQuery()
}
/** 新增按钮操作 */
function handleAdd() {
  reset()
  // 获取场站
  // getStationsListApi()

  open.value = true
  title.value = "新增充电桩"
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  // 获取场站
  // getStationsListApi()
  const dictId = row.id || ids.value
  getChargingPileDetail(dictId).then((response) => {
    form.value = {
      ...response.data,
      pileType: response.data.pileType?.toString() || undefined,
      stationId: response.data.stationId?.toString() || undefined,
      pileStatus: response.data.pileStatus?.toString() || undefined,
      protocolVersion: response.data.protocolVersion?.toString() || undefined,
    }
    open.value = true
    title.value = "修改充电桩"
  })
}
/** 删除按钮操作 */
function handleDelete(row) {
  const dictIds = row.id || ids.value
  let name = row.chargingPileName || names.value
  proxy.$modal
    .confirm('是否确认删除"' + name + '充电桩"吗？')
    .then(function () {
      return delChargingPile(dictIds)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
/** 跳转详情 **/
function goDetail(row) {
  router.push({ path: "/detail/chargingPile", query: { id: row.id } })
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id)
  names.value = selection.map((item) => item.chargingPileName)
  single.value = selection.length != 1
  multiple.value = !selection.length
}
/** 状态修改 */
function handleStatusChange(row) {
  let text = row.pileStatus == "1" ? "启用" : "停用"
  proxy.$modal
    .confirm("确认要" + text + '"' + row.chargingPileName + '"充电桩吗?')
    .then(function () {
      return updatePileStatus(row.id)
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功")
      getList()
    })
    .catch(function () {
      row.pileStatus = row.pileStatus === "0" ? "1" : "0"
      getList()
    })
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["dictRef"].validate((valid) => {
    let params = JSON.parse(JSON.stringify(form.value))
    delete params.gunsList
    if (valid) {
      if (form.value?.id != undefined) {
        delete params.gunNumber
        updateChargingPile(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addChargingPile(params).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}
/** 获取场站下拉数据 */
function getStationsListApi() {
  if (stationsList?.length) return
  getStationListAll().then((res) => {
    stationsList.value = res.data
  })
}

getStationsListApi()
getList()
</script>
