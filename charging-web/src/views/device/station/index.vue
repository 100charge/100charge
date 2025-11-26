<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="场站名称" prop="dictName">
        <el-input v-model="queryParams.name" placeholder="请输入场站名称" clearable style="width: 240px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="stationsList" @selection-change="handleSelectionChange" border stripe>
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="场站名称" align="center" prop="name" width="220" />
      <el-table-column label="设施标签" align="center" prop="facilityLabel">
        <template #default="scope">
          {{ getFacilityLabel(scope.row.facilityLabel) }}
        </template>
      </el-table-column>
      <!-- 	场站服务 serviceLabel station_services（卫生间、休息室、便利店） -->
      <el-table-column label="场站服务" align="center" prop="serviceLabel">
        <template #default="scope">
          {{ getServiceLabel(scope.row.serviceLabel) }}
        </template>
      </el-table-column>
      <el-table-column label="星级" align="center" prop="starLabel">
        <template #default="scope">
          {{ scope.row.starLabel == null ? "--" : starlabel[scope.row.starLabel] }}
        </template>
      </el-table-column>
      <el-table-column label="计费策略" align="center" prop="ruleName" width="220" />
      <el-table-column label="是否展示" align="center" prop="showStatus">
        <template #default="scope">
          <el-switch v-model="scope.row.showStatus" active-value="1" inactive-value="0"
            @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button v-if="!scope.row.operateStation" link type="success" icon="Download"
            @click="handleDownload(scope.row)">下载二维码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 新建场站/修改对话框 -->
    <el-drawer :title="title" v-model="open" size="900px" append-to-body :close-on-click-modal="false">
      <el-form ref="dictRef" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="场站名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="场站状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in stations_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否展示" prop="showStatus">
              <el-radio-group v-model="form.showStatus">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <div style="border-top: 1px solid #eaeaea; width: 100%; padding: 12px 0; font-weight: 600; font-size: 16px"
            class="global-color">
            场站基础信息
          </div>
          <el-col :span="12">
            <el-form-item label="计费策略" prop="ruleId">
              <el-select v-model="form.ruleId" filterable placeholder="请选择" style="width: 100%">
                <el-option v-for="item in ruleList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大功率" prop="maxPower">
              <el-input v-model="form.maxPower" placeholder="请输入" type="number"><template
                  #append>kW</template></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="营业时间" prop="perationBeginTime">
              <el-time-select v-model="form.perationBeginTime" style="width: 150px" start="00:00" end="23:59"
                placeholder="开始时间" />
              <div style="padding-left: 15px"></div>

              <el-time-select v-model="form.perationEndTime" style="width: 150px" start="00:00" end="23:59"
                placeholder="截止时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建设场所" prop="construction">
              <el-select v-model="form.construction" filterable placeholder="请选择" style="width: 100%">
                <el-option v-for="item in constructionEumn" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="即插即充" prop="plugCharge">
              <el-radio-group v-model="form.plugCharge">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否优选" prop="recommend">
              <el-radio-group v-model="form.recommend">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 设施标签（地上、地下、洗车） -->
            <el-form-item label="设施标签" prop="facilityLabel">
              <el-checkbox-group v-model="form.facilityLabel">
                <el-checkbox v-for="item in station_facilities" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- （卫生间、休息室、便利店） -->
            <el-form-item label="服务标签" prop="serviceLabel">
              <el-checkbox-group v-model="form.serviceLabel">
                <el-checkbox v-for="item in station_services" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停车费标签" prop="parkingFeeLabel">
              <el-radio-group v-model="form.parkingFeeLabel">
                <el-radio v-for="item in parking_fees" :label="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 3星 4星 5星 -->
            <el-form-item label="场站星级" prop="starLabel">
              <el-radio-group v-model="form.starLabel">
                <el-radio v-for="item in station_starlabel" :label="item.value" :value="item.value"
                  :key="item.value + 'starLabel'">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停车费描述" prop="parkingFeeTip">
              <el-input type="textarea" v-model="form.parkingFeeTip" placeholder="请输入停车费描述。如：超出2小时，每小时2元。" />
            </el-form-item>
          </el-col>

          <el-col :span="12"> </el-col>

          <el-col :span="12">
            <el-form-item label="场站logo" prop="logoImage">
              <image-upload v-model="form.logoImage" :limit="1" :isShowTip="false"></image-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="营业执照" prop="licenseImage">
              <image-upload v-model="form.licenseImage" :limit="1" :isShowTip="false"></image-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="场站图片" prop="stationImageList">
              <image-upload v-model="form.stationImageList" :limit="4" :isShowTip="false"></image-upload>
            </el-form-item>
          </el-col>

          <div style="border-top: 1px solid #eaeaea; width: 100%; padding: 12px 0; font-weight: 600; font-size: 16px"
            class="global-color">
            场站地址信息
          </div>
          <el-col :span="8">
            <el-form-item label="省" prop="province">
              <el-select v-model="form.province" filterable placeholder="请选择" @change="getCityListByProvinceIdApi">
                <el-option v-for="item in provincOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="市" prop="city">
              <el-select v-model="form.city" filterable placeholder="请选择" @change="getDistrictListByCityIdApi">
                <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区" prop="region">
              <el-select v-model="form.region" filterable placeholder="请选择">
                <el-option v-for="item in regionOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="详细地址" prop="address" class="address-input">
              <el-input v-model="form.address" placeholder="请输入详细地址"> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="lng">
              <el-input-number :precision="4" v-model="form.lng" placeholder="请输入经度，范围-180～180" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="lat">
              <el-input-number :precision="4" v-model="form.lat" placeholder="请输入纬度，范围-90～90" style="width: 100%" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="腾讯坐标拾取">
          <el-link underline href="https://lbs.qq.com/getPoint/" target="_blank">打开</el-link>
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
    </el-drawer>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router"
import {
  addStations,
  getStationsList,
  updateStations,
  updateShowStatus,
  getStationsDetail,
  delStations,
  generateQRCodeZipForStation,
} from "@/api/device/station"
import { getRuleList, getProvinceList, getCityListByProvinceId, getDistrictListByCityId } from "@/api/common/common"
import ImageUpload from "@/components/ImageUpload"
import { blobValidate } from "@/utils/ruoyi"
import download from "@/plugins/download.js"

const { proxy } = getCurrentInstance()
const { stations_status } = proxy.useDict("stations_status")
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")
const { station_facilities } = proxy.useDict("station_facilities")
const { station_services } = proxy.useDict("station_services")
const { station_starlabel } = proxy.useDict("station_starlabel")
const { parking_fees } = proxy.useDict("parking_fees")
const facilities = ref({})
const services = ref({})
const starlabel = ref({})

const route = useRoute()
const router = useRouter()
const { params, query } = route
const stationsList = ref([])
const loading = ref(true)
const ids = ref([])
const names = ref([])
const dateRange = ref([])
const multiple = ref(true)
const total = ref(0)
const single = ref(true)
// 0:关闭；1:开放；2:维护中
const statusOptions = ref([])
const open = ref(false)
const refreshTable = ref(true)
const title = ref("")
const provincOptions = ref([])
const cityOptions = ref([])
const regionOptions = ref([])
const ruleList = ref([])
const constructionEumn = [
  { id: 1, name: "居民区" },
  { id: 2, name: "公共机构" },
  { id: 3, name: "企事业单位" },
  { id: 4, name: "写字楼" },
  { id: 5, name: "工业园区" },
  { id: 6, name: "交通枢纽" },
  { id: 7, name: "大型文体设施" },
  { id: 8, name: "城市绿地" },
  { id: 9, name: "大型建筑配建停车场" },
  { id: 10, name: "路边停车位" },
  { id: 11, name: "城际高速服务区" },
  { id: 255, name: "其他" },
]
const checkLat = (rule, value, callback) => {
  if (+value < -90 || +value > 90) {
    callback(new Error("纬度范围 -90 ～ 90"))
  } else {
    callback()
  }
}
const checkLng = (rule, value, callback) => {
  if (+value < -180 || +value > 180) {
    callback(new Error("经度范围 -180 ～ 180"))
  } else {
    callback()
  }
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
    name: [{ required: true, message: "场站名称不能为空", trigger: "blur" }],
    // provider: [{ required: true, message: "公司名称不能为空", trigger: "blur" }],
    lat: [
      { required: true, message: "纬度不能为空", trigger: "blur" },
      { validator: checkLat, trigger: ["blur", "change"] },
    ],
    lng: [
      { required: true, message: "经度不能为空", trigger: "blur" },
      { validator: checkLng, trigger: ["blur", "change"] },
    ],
    starLabel: [{ required: true, message: "请选择星级", trigger: "blur" }],
    status: [{ required: true, message: "场站状态不能为空", trigger: "change" }],
    province: [{ required: true, message: "省不能为空", trigger: "change" }],
    city: [{ required: true, message: "市不能为空", trigger: "change" }],
    region: [{ required: true, message: "区不能为空", trigger: "change" }],
    address: [{ required: true, message: "详细地址不能为空", trigger: "blur" }],
    ruleId: [{ required: true, message: "计费策略不能为空", trigger: "change" }],
    plugCharge: [{ required: true, message: "是否即插即充不能为空", trigger: "change" }],
    recommend: [{ required: true, message: "是否优选不能为空", trigger: "change" }],
    showStatus: [{ required: true, message: "是否显示不能为空", trigger: "change" }],
    perationBeginTime: [{ required: true, message: "营业时间开始不能为空", trigger: "change" }],
    perationEndTime: [{ required: true, message: "营业时间结束不能为空", trigger: "change" }],
    construction: [{ required: true, message: "建设场所不能为空", trigger: "change" }],
    chargingLimitAmount: [{ required: true, message: "停止充电金额不能为空", trigger: "blur" }],
  },
})
const { queryParams, form, rules } = toRefs(data)

/** 场站列表 */
function getList() {
  loading.value = true
  getStationsList(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    //  设施标签
    !Object.keys(facilities.value).length &&
      station_facilities.value.forEach((item) => {
        facilities.value[item.value] = item.label
      })
    // 服务标签
    !Object.keys(services.value).length &&
      station_services.value.forEach((item) => {
        services.value[item.value] = item.label
      })
    // 星级
    !Object.keys(starlabel.value).length &&
      station_starlabel.value.forEach((item) => {
        starlabel.value[item.value] = item.label
      })

    stationsList.value = response.rows
      ? response.rows.map((item) => {
        return {
          ...item,
          showStatus: item.showStatus?.toString() || undefined,
        }
      })
      : []
    total.value = response.total
    loading.value = false
  })
}
/** 获取设施标签文本展示 */
function getFacilityLabel(e) {
  if (!e) {
    return "--"
  }
  let arr = e.split(",")
  let text = arr.reduce((pro, cur, index) => {
    pro = pro + (facilities.value[cur] || "") + (facilities.value[cur] ? (index + 1 == arr.length ? "" : ",") : "")
    return pro
  }, "")
  return text
}
/** 获取场站服务文本展示 */
function getServiceLabel(e) {
  if (!e) {
    return "--"
  }
  let arr = e.split(",")
  let text = arr.reduce((pro, cur, index) => {
    pro = pro + (services.value[cur] || "") + (services.value[cur] ? (index + 1 == arr.length ? "" : ",") : "")
    return pro
  }, "")
  return text
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
    lng: undefined,
    lat: undefined,
    maxPower: undefined,
    plugCharge: "1",
    ruleId: undefined,
    facilityLabel: undefined,
    serviceLabel: undefined,
    parkingFeeLabel: undefined,
    parkingFeeTip: undefined,
    starLabel: "3",
    recommend: "1",
    showStatus: "1",
    stationImageList: "",
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
  queryParams.value.name = undefined
  queryParams.value.status = undefined
  proxy.resetForm("queryRef")
  handleQuery()
}
/** 新增按钮操作 */
function handleAdd() {
  reset()
  // 获取省
  getProvinceListApi()
  // 获取计费策略
  getRuleListApi()

  open.value = true
  title.value = "新增场站"
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  // 获取省
  getProvinceListApi()
  // 获取计费策略
  getRuleListApi()
  const dictId = row.id || ids.value
  getStationsDetail(dictId).then((response) => {
    form.value = {
      ...response.data,
      logoImage: response.data.logo || "",
      name: response.data.name ?? undefined,
      province: response.data.province?.toString() || undefined,
      city: response.data.city?.toString() || undefined,
      region: response.data.region?.toString() || undefined,
      status: response.data.status?.toString() || undefined,
      plugCharge: response.data.plugCharge?.toString() || undefined,
      ruleId: response.data.ruleId?.toString() || undefined,
      facilityLabel: response.data.facilityLabel ? response.data.facilityLabel.split(",") : undefined,
      serviceLabel: response.data.serviceLabel ? response.data.serviceLabel.split(",") : undefined,
      parkingFeeLabel: response.data.parkingFeeLabel?.toString() || undefined,
      starLabel: response.data.starLabel?.toString() || undefined,
      recommend: response.data.recommend?.toString() || undefined,
      showStatus: response.data.showStatus?.toString() || undefined,
      tenantId: response.data.tenantId?.toString() || undefined,
      stationImageList: response.data.stationImageList?.length
        ? response.data.stationImageList.reduce((pro, cur, index) => {
          pro = pro + cur + (index + 1 == response.data.stationImageList.length ? "" : ",")
          return pro
        }, "")
        : "",
    }
    response.data.province && getCityListByProvinceIdApi()
    response.data.city && getDistrictListByCityIdApi()
    open.value = true
    title.value = "修改场站"
  })
}

// 下载二维码
function handleDownload(row) {
  generateQRCodeZipForStation({
    stationId: row.id,
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
/** 删除按钮操作 */
function handleDelete(row) {
  const dictIds = row.id || ids.value
  let name = row.name || names.value
  proxy.$modal
    .confirm('是否确认删除"' + name + '"场站吗？')
    .then(function () {
      return delStations(dictIds)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => { })
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id)
  names.value = selection.map((item) => item.name)
  single.value = selection.length != 1
  multiple.value = !selection.length
}
/** 场站状态修改 */
function handleStatusChange(row) {
  let text = row.showStatus == "1" ? "展示" : "隐藏"
  proxy.$modal
    .confirm("确认要" + text + '"' + row.name + '"场站吗?')
    .then(function () {
      return updateShowStatus({ id: row.id })
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功")
    })
    .catch(function () {
      row.showStatus = row.showStatus == "0" ? "1" : "0"
    })
}
function locationChange(location) {
  form.value.lat = location.latlng.lat
  form.value.lng = location.latlng.lng
  form.value.address = location.poiname + location.poiaddress
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["dictRef"].validate((valid) => {
    let params = JSON.parse(JSON.stringify(form.value))
    params?.facilityLabel &&
      (params.facilityLabel = form.value.facilityLabel.reduce((pro, cur, index) => {
        pro = pro + cur + (index + 1 == form.value.facilityLabel.length ? "" : ",")
        return pro
      }, ""))
    params?.serviceLabel &&
      (params.serviceLabel = form.value.serviceLabel.reduce((pro, cur, index) => {
        pro = pro + cur + (index + 1 == form.value.serviceLabel.length ? "" : ",")
        return pro
      }, ""))
    params?.stationImageList && (params.stationImageList = form.value.stationImageList.split(","))
    !params?.stationImageList && (params.stationImageList = [])
    params?.perationBeginTime && (params.perationBeginTime = form.value.perationBeginTime + ":00")
    params?.perationEndTime && (params.perationEndTime = form.value.perationEndTime + ":00")

    if (valid) {
      if (form.value?.id != undefined) {
        updateStations(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addStations(params).then((response) => {
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
/** 获取省下拉数据 */
function getProvinceListApi() {
  if (provincOptions?.length) return
  getProvinceList().then((res) => {
    provincOptions.value = res.data
  })
}
// 根据省级id获取市级下拉列表
function getCityListByProvinceIdApi(key) {
  if (key) {
    form.value.city = ""
    form.value.region = ""
  }

  getCityListByProvinceId({ provinceId: form.value.province }).then((res) => {
    cityOptions.value = res.data
  })
}
// 根据市级id获取区级下拉列表
function getDistrictListByCityIdApi(key) {
  key && (form.value.region = "")

  getDistrictListByCityId({ cityId: form.value.city }).then((res) => {
    regionOptions.value = res.data
  })
}
/**获取计费策略 */
function getRuleListApi() {
  if (ruleList?.length) return
  getRuleList({
    pageNum: 1,
    pageSize: 99999,
  }).then((res) => {
    ruleList.value = res.rows
  })
}
getList()
</script>

<style lang="scss" scoped>
.address-input {
  :deep(.el-input__wrapper) {
    padding-right: 0;
  }
}
</style>
