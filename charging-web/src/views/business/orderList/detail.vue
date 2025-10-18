<template>
  <div class="app-container" style="padding-top: 8px">
    <el-card class="card">
      <el-descriptions title="订单详情" :column="3">
        <el-descriptions-item label="订单编号：">{{ dataDetail.tradeNo || "--" }}</el-descriptions-item>
        <el-descriptions-item label="订单状态：">{{ dataDetail.orderStateDesc || "--" }}</el-descriptions-item>
        <el-descriptions-item label="充电时长：">{{ dataDetail.realDuration || "--" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    <!-- 客户下单：{{dataDetail.startTime}} -->
    <el-card class="card">
      <el-steps :active="dataDetail?.endTime ? 2 : 1" align-center finish-status="success">
        <el-step title="开始充电" :description="dataDetail.startTime" />
        <el-step title="完成时间" :description="dataDetail.endTime" />
        <!-- <el-step title="Step 3" description="Some description" /> -->
      </el-steps>
    </el-card>

    <el-card class="card">
      <el-descriptions title="费用信息" style="padding-top: 15px" :column="3">
        <el-descriptions-item label="订单金额：" width="150px">{{
          dataDetail.totalAmount || "0"
        }}</el-descriptions-item>
        <el-descriptions-item label="实际支付金额：" width="150px">{{
          dataDetail.realAmount || "0"
        }}</el-descriptions-item>
        <el-descriptions-item label="优惠券抵扣金额：" width="150px">{{
          dataDetail.couponAmount || "0"
        }}</el-descriptions-item>
        <el-descriptions-item label="充电费用：" width="150px">{{ dataDetail.chargeFee || "0" }}</el-descriptions-item>
        <el-descriptions-item label="服务费：" width="150px">{{ dataDetail.serviceFee || "0" }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="支付信息" style="padding-top: 15px">
        <el-descriptions-item label="支付方式：" width="150px">{{
          dataDetail.paymentDesc || "--"
        }}</el-descriptions-item>
        <el-descriptions-item label="支付状态：" width="150px">{{
          dataDetail.payStatusDesc || "--"
        }}</el-descriptions-item>
        <el-descriptions-item label="支付时间：" width="150px">{{ dataDetail.payTime || "--" }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="充电信息" style="padding-top: 15px">
        <el-descriptions-item label="场站：" width="150px">{{ dataDetail.stationName || "--" }}</el-descriptions-item>
        <el-descriptions-item label="桩编号：" width="150px">{{ dataDetail.deviceNo || "--" }}</el-descriptions-item>
        <el-descriptions-item label="枪编号：" width="150px">{{ dataDetail.gunNo || "--" }}</el-descriptions-item>

        <el-descriptions-item label="充电时长：" width="150px">{{
          dataDetail.realDuration || "--"
        }}</el-descriptions-item>
        <el-descriptions-item label="充电度数：" width="150px">{{
          dataDetail.totalPower || "--"
        }}</el-descriptions-item>
        <el-descriptions-item label="结束原因：" width="150px">{{
          dataDetail.stopReason || "--"
        }}</el-descriptions-item>
        <el-descriptions-item label="设备来源：" width="150px">{{
          dataDetail.platequipmentSourceOperatorform || "--"
        }}</el-descriptions-item>
        <el-descriptions-item label="三方互联订单号：" width="150px">{{
          dataDetail.resaleOrderNo || "--"
        }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="用户信息" style="padding-top: 15px" :column="3">
        <!-- <el-descriptions-item label="用户名：" width="150px">{{ dataDetail.deviceNo || "--" }}</el-descriptions-item> -->
        <el-descriptions-item label="手机号：" width="150px">{{ dataDetail.mobile || "--" }}</el-descriptions-item>
        <el-descriptions-item label="车牌号：" width="150px">{{ dataDetail.plateNo || "--" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { getOrderDetail } from "@/api/business/chargingOrder"

const route = useRoute()
const { query } = route
const dataDetail = ref([])
const loading = ref(false)

function getData() {
  loading.value = true
  getOrderDetail(query.id).then((response) => {
    dataDetail.value = response.data
    loading.value = false
  })
}
getData()
</script>

<style lang="scss" scoped>
.card {
  margin-top: 12px;
}
</style>
