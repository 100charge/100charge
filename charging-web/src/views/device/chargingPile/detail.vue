<template>
  <div class="app-container">
    <el-card>
      <el-descriptions title="桩详情">
        <el-descriptions-item label="桩编号：">{{ dataDetail.deviceNo || "--" }}</el-descriptions-item>
        <el-descriptions-item label="桩名称：">{{ dataDetail.name || "--" }}</el-descriptions-item>
        <el-descriptions-item label="所属场站：">{{ dataDetail.stationName || "--" }}</el-descriptions-item>
        <el-descriptions-item label="生产厂商：">{{ dataDetail.supplier || "--" }}</el-descriptions-item>
        <el-descriptions-item label="充电类型：">{{ dataDetail.pileTypeDesc || "--" }}</el-descriptions-item>
        <el-descriptions-item label="设备接口类型：">{{ dataDetail.connectorTypeDesc || "--" }}</el-descriptions-item>
        <el-descriptions-item label="国家标准：">{{ dataDetail.nationalStandardDesc || "--" }}</el-descriptions-item>
        <el-descriptions-item label="桩状态：">{{ dataDetail.pileStatusDesc || "--" }}</el-descriptions-item>
        <el-descriptions-item label="枪数量：">{{ dataDetail.gunNumber || "--" }}</el-descriptions-item>
        <el-descriptions-item label="硬件版本：">{{ dataDetail.hardwareVersion || "--" }}</el-descriptions-item>
        <el-descriptions-item label="最大输出电流：">{{ dataDetail.maxCurrent || "--" }}</el-descriptions-item>
        <el-descriptions-item label="最小输出电流：">{{ dataDetail.minCurrent || "--" }}</el-descriptions-item>
        <el-descriptions-item label="最大输出功率：">{{ dataDetail.maxPower || "--" }}</el-descriptions-item>
        <el-descriptions-item label="最小输出功率：">{{ dataDetail.minPower || "--" }}</el-descriptions-item>
        <el-descriptions-item label="最大输出电压：">{{ dataDetail.maxVoltage || "--" }}</el-descriptions-item>
        <el-descriptions-item label="最小输出电压：">{{ dataDetail.minVoltage || "--" }}</el-descriptions-item>
        <el-descriptions-item label="协议版本：">
          {{ dataDetail.protocolVersionName }}
        </el-descriptions-item>
        <el-descriptions-item label="备注：">{{ dataDetail.remark || "--" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-button type="primary" class="add" plain icon="Plus" @click="handleAdd">新增</el-button>
    <el-button type="success" class="add" style="margin-left: 12px" plain icon="Download" @click="handleDownload"
      >批量下载</el-button
    >
    <el-table :data="dataDetail.gunsList" border stripe>
      <el-table-column label="枪编号" align="center" prop="no" :show-overflow-tooltip="true" />
      <el-table-column label="枪ID" align="center" prop="id" :show-overflow-tooltip="true" />
      <el-table-column label="枪状态" align="center" prop="gunStatus" :show-overflow-tooltip="true" />
      <el-table-column label="SOC" align="center" prop="soc" :show-overflow-tooltip="true" />
      <el-table-column label="电压" align="center" prop="voltage" :show-overflow-tooltip="true" />
      <el-table-column label="电流" align="center" prop="current" :show-overflow-tooltip="true" />
      <el-table-column
        label="充电电量"
        align="center"
        prop="accumulatedChargingCapacity"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" /> -->

      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="success" icon="Download" @click="handleQRCode(scope.row)">下载二维码</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus"
import { blobValidate } from "@/utils/ruoyi"
import download from "@/plugins/download.js"
import {
  getChargingPileDetail,
  addChargingGun,
  delChargingGun,
  generateQRCode,
  generateQRCodeZip,
} from "@/api/device/chargingPile"

const { proxy } = getCurrentInstance()
const route = useRoute()
const router = useRouter()
const { params, query } = route
const dataDetail = ref([])
const loading = ref(false)

function getData() {
  loading.value = true
  getChargingPileDetail(query.id).then((response) => {
    dataDetail.value = response.data

    loading.value = false
  })
}

/** 新增按钮操作 */
function handleAdd() {
  proxy.$modal
    .confirm("是否确认新增电枪？")
    .then(function () {
      return addChargingGun(query.id)
    })
    .then(() => {
      getData()
      proxy.$modal.msgSuccess("新增成功")
    })
    .catch(() => {})
}
/** 删除按钮操作 */
function handleDelete(row) {
  const dictIds = row.id
  proxy.$modal
    .confirm("是否确认删除该电枪吗？")
    .then(function () {
      return delChargingGun(dictIds)
    })
    .then(() => {
      getData()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

function handleQRCode(row) {
  let params = {
    gunNo: row.no,
    pileNo: dataDetail.value.deviceNo,
  }
  generateQRCode(params).then((response) => {
    var blob = response
    if (blob.type.indexOf("application/json") > -1) {
      blob.text().then((text) => {
        if (typeof text == "string") {
          //服务端响应信息就在 errInfo
          let errInfo = JSON.parse(text)
          //然后弹出报错信息
          ElMessage.error(errInfo?.msg || "下载失败！")
        }
      })
      return
    }
    uploadimage(response, row)
  })
}

function handleDownload() {
  generateQRCodeZip({
    pileNo: dataDetail.value.deviceNo,
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
function uploadimage(data, row) {
  var a = document.createElement("a")
  document.body.appendChild(a)
  a.style = "display: none"
  let blob = new Blob([data], {
    type: "image/png",
  })
  let url = window.URL.createObjectURL(blob)
  a.href = url
  a.download = row.no
  a.click()
  a.remove()
  window.URL.revokeObjectURL(url) // 释放url
}

getData()
</script>
<style lang="scss" scoped>
.page-box {
  padding: 20px 15px;
}
.add {
  margin: 20px 0;
}
</style>
