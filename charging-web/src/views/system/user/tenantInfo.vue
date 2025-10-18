<template>
  <div class="app-container">
    <el-card>
      <template v-slot:header>
        <div class="" style="width: 100%; display: flex; justify-content: space-between">
          <span>运营商信息</span>
          <el-button circle icon="Refresh" @click="refresh()" />
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="tab1">
          <el-descriptions title="" :column="3">
            <el-descriptions-item label="运营商编码">{{ tenantInfo.code || "--" }}</el-descriptions-item>
            <el-descriptions-item label="运营商名称">{{ tenantInfo.name || "--" }}</el-descriptions-item>
            <el-descriptions-item label="运营商简称">{{ tenantInfo.shortName || "--" }}</el-descriptions-item>
            <el-descriptions-item label="英文名称">{{ tenantInfo.englishName || "--" }}</el-descriptions-item>
            <el-descriptions-item label="组织机构代码" :span="2">{{
              tenantInfo.organizationCode || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="联系人">{{ tenantInfo.contact || "--" }}</el-descriptions-item>
            <el-descriptions-item label="联系方式">{{ tenantInfo.phone || "--" }}</el-descriptions-item>
            <el-descriptions-item label="联系地址">{{ tenantInfo.address || "--" }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{ tenantInfo.companyTypeDesc || "--" }}</el-descriptions-item>
            <el-descriptions-item label="有效期"
              >{{ tenantInfo.startDate }} ~ {{ tenantInfo.endDate }}</el-descriptions-item
            >
            <el-descriptions-item label="域名">{{ tenantInfo.domain || "--" }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="企业信息" name="tab2">
          <el-descriptions title="" :column="3">
            <el-descriptions-item label="企业名称">{{ tenantInfo.companyName || "--" }}</el-descriptions-item>
            <el-descriptions-item label="账户类型">{{ tenantInfo.acctTypeDesc || "--" }}</el-descriptions-item>
            <el-descriptions-item label="企业地址">{{ tenantInfo.companyAddress || "--" }}</el-descriptions-item>
            <el-descriptions-item label="银行卡号">{{ tenantInfo.accountNo || "--" }}</el-descriptions-item>
            <el-descriptions-item label="开户银行">{{ tenantInfo.parentBankName || "--" }}</el-descriptions-item>
            <el-descriptions-item label="支行名称">{{ tenantInfo.bankName || "--" }}</el-descriptions-item>
            <el-descriptions-item label="支付行号">{{ tenantInfo.unionBank || "--" }}</el-descriptions-item>
            <el-descriptions-item label="统一社会信用">{{
              tenantInfo.unifiedSocialCredit || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="统一社会信用到期时间">{{
              tenantInfo.unifiedSocialCreditExpiraTime || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="税务登记证号">{{
              tenantInfo.taxRegistCertificate || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="营业执照类型">{{
              tenantInfo.businessLicenseTypeDesc || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="营业执照号">{{ tenantInfo.businessLicenseNo || "--" }}</el-descriptions-item>
            <el-descriptions-item label="营业执照到期时间">{{
              tenantInfo.businessLicenseExpiraTime || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="营业执照照片">
              <ImagePreview :src="tenantInfo.businessLicenseImage" height="120px" width="120px" />
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="法人信息" name="tab3">
          <el-descriptions title="" :column="3">
            <el-descriptions-item label="法人姓名">{{ tenantInfo.legalName || "--" }}</el-descriptions-item>
            <el-descriptions-item label="法人手机号">{{ tenantInfo.legalPhone || "--" }}</el-descriptions-item>
            <el-descriptions-item label="身份证号码">{{ tenantInfo.legalId || "--" }}</el-descriptions-item>
            <el-descriptions-item label="证件有效期">{{
              tenantInfo.certificateTermValidity || "--"
            }}</el-descriptions-item>
            <el-descriptions-item label="身份证正面照片">
              <ImagePreview :src="tenantInfo.idCardFrontImage" height="120px" width="120px" />
            </el-descriptions-item>
            <el-descriptions-item label="身份证反面照片">
              <ImagePreview :src="tenantInfo.idCardBackImage" height="120px" width="120px" />
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <el-card style="margin-top: 12px">
      <template v-slot:header>
        <div class="" style="width: 100%; display: flex; justify-content: space-between">
          <span>分润比例配置</span>
          <el-button circle icon="Refresh" @click="refresh()" />
        </div>
      </template>
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column label="场站名称" prop="stationName" />
        <el-table-column label="企业客户优惠比例/%" prop="companyDiscountRatio" />
        <el-table-column label="电费比例/%" prop="electricityFee" />
        <el-table-column label="服务费比例/%" prop="serviceFee" />
        <el-table-column label="停车费比例/%" prop="parkingFee" />
        <el-table-column label="超时占用费比例/%" prop="occupancyFee" />
        <el-table-column label="通联支付手续费/%" prop="unionpayServiceCharge" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime"> </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getTableData"
      />
    </el-card>
  </div>
</template>

<script setup name="Tenant">
import { getTenantInfo, sharingRatioList } from "@/api/system/user"

const activeTab = ref("tab1")
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const tenantInfo = ref({})
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

function getInfo() {
  getTenantInfo().then((res) => {
    tenantInfo.value = {
      ...res.data,
      ...res.data.companyInfo,
      status: res.data.enabled ? "0" : "1",
      acctType: res.data.companyInfo && res.data.companyInfo.acctType ? res.data.companyInfo.acctType.toString() : "1",
      businessLicenseType:
        res.data.companyInfo && res.data.companyInfo.businessLicenseType
          ? res.data.companyInfo.businessLicenseType.toString()
          : "1",
      companyType: res.data.companyType ? res.data.companyType.toString() : "0",
      daterange: [res.data.startDate, res.data.endDate],
    }
  })
}

function getTableData() {
  sharingRatioList(queryParams.value).then((res) => {
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

function refresh() {
  queryParams.value.pageNum = 1
  getInfo()
  getTableData()
}

refresh()
</script>
