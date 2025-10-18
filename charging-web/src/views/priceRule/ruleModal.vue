<template>
  <el-drawer title="维护时段电价" v-model="dialogVisible" size="930px" append-to-body :close-on-click-modal="false">
    <el-form ref="formRef" :model="formData" label-width="80px" :rules="rules">
      <el-form-item label="生效开始日期" prop="startTime" label-width="160px">
        <el-date-picker
          v-model="formData.startTime"
          placeholder="开始日期"
          value-format="YYYY-MM-DD"
          style="width: 240px"
          :disabled-date="disabledDate1"
          clearable
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="生效结束日期" prop="endTime" label-width="160px">
        <el-date-picker
          v-model="formData.endTime"
          placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="width: 240px"
          :disabled-date="disabledDate2"
          clearable
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="电价设置">
        <div class="tipmincc">（峰、平、谷时段价格均为必填项。因各省市电价不同，请根据当地实际电价填写。）</div>
        <br />
        <el-row
          v-for="(rule, index) in ruleDetailList"
          :key="index"
          style="width: 100%; margin-top: 15px; margin-left: -60px"
        >
          <el-col :span="1.5">
            <el-form-item label=" " prop="type" label-width="0px">
              <el-select v-model="rule.type" placeholder="时段类型" style="width: 120px">
                <el-option v-for="item in typeOption" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="1.5">
            <el-form-item label=" " prop="startTime" label-width="0px">
              <el-time-select
                v-model="rule.startTime"
                style="width: 125px"
                :max-time="rule.endTime"
                placeholder="开始时间"
                start="00:00"
                step="00:30"
                end="24:00"
                :disabled="index === 0"
              />
            </el-form-item>
          </el-col>
          <span style="margin-left: 10px">至</span>
          <el-col :span="1.5">
            <el-form-item label=" " prop="endTime" label-width="0px">
              <el-time-select
                v-model="rule.endTime"
                style="width: 125px"
                :min-time="rule.startTime"
                placeholder="结束时间"
                start="00:00"
                step="00:30"
                end="23:59"
                :disabled="index === ruleDetailList.length - 1"
              />
            </el-form-item>
          </el-col>
          <el-col :span="1.5">
            <el-form-item label="电费" prop="chargeFee" label-width="54px">
              <el-input-number
                v-model="rule.chargeFee"
                controls-position="right"
                :min="0.0"
                :max="10"
                :step="0.1"
                style="width: 100px; margin-right: 10px"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="1.5">
            <el-form-item label="服务费" prop="serviceFee" label-width="54px">
              <el-input-number
                v-model="rule.serviceFee"
                controls-position="right"
                :min="0.0"
                :max="10"
                :step="0.1"
                style="width: 100px; margin-right: 10px"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-if="index !== 0 && index !== ruleDetailList.length - 1"
              type="danger"
              icon="Delete"
              @click="removeDom(index)"
            ></el-button>
          </el-col>
        </el-row>
        <div style="width: 100%"></div>
        <el-button type="primary" icon="Plus" style="margin: 16px 0 0 -68px" @click="addDom">添加时段</el-button>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-drawer>
</template>
<script setup name="refundModal">
import ruleApi from "@/api/rule/rule"
import dayjs from "dayjs"
const emit = defineEmits(["successful"])
const { proxy } = getCurrentInstance()
const typeOption = [
  { label: "尖", value: "0", color: "#f56c6c" },
  { label: "峰", value: "1", color: "#e6a23c" },
  { label: "平", value: "2", color: "#67c23a" },
  { label: "谷", value: "3", color: "#909399" },
]
const dialogVisible = ref(false)
const ruleId = ref("")
const formData = ref({
  id: undefined,
  dateRange: [],
  startTime: "",
  endTime: "",
})
const ruleDetailList = ref([
  {
    type: "0",
    startTime: "00:00",
    endTime: "23:59",
    chargeFee: 0,
    occupancyFee: 0,
    parkingFee: 0,
    serviceFee: 0,
    remark: "",
  },
  // {
  //   type: "0",
  //   startTime: "",
  //   endTime: "23:59",
  //   chargeFee: 0,
  //   occupancyFee: 0,
  //   parkingFee: 0,
  //   serviceFee: 0,
  //   remark: "",
  // },
])
const rules = {
  name: [{ required: true, message: "计费策略名称不能为空", trigger: "blur" }],
  tenantId: [{ required: true, message: "所属运营商不能为空", trigger: "blur" }],
  dateRange: [{ required: true, message: "必填项", trigger: "blur" }],
  startTime: [{ required: true, message: "开始时间不能为空", trigger: "blur" }],
  endTime: [{ required: true, message: "结束时间不能为空", trigger: "blur" }],
  // chargeFee: [{ required: true, message: "必填项", trigger: "blur" }],
  // occupancyFee: [{ required: true, message: "必填项", trigger: "blur" }],
  // parkingFee: [{ required: true, message: "必填项", trigger: "blur" }],
  // serviceFee: [{ required: true, message: "必填项", trigger: "blur" }],
}
const dropdownList = ref([])

const cancel = () => {
  dialogVisible.value = false
}

function disabledDate1(current) {
  if (formData.value.endTime) {
    return (
      // 小于或等于结束日期，并且大于结束年1月1日
      current.valueOf() > dayjs(formData.value.endTime) ||
      current.valueOf() < dayjs(formData.value.endTime.substring(0, 4) + "-01-01")
    )
  }
  return false
}
function disabledDate2(current) {
  if (formData.value.startTime) {
    let year = formData.value.startTime.substring(0, 4)
    return current.valueOf() < dayjs(formData.value.startTime) || current.valueOf() > dayjs(year + "-12-31")
  }
  return false
}

const onOpen = (row) => {
  ruleId.value = row.ruleId
  dialogVisible.value = true
  ruleDetailList.value = [
    {
      type: "0",
      startTime: "00:00",
      endTime: "23:59",
      chargeFee: 0,
      occupancyFee: 0,
      parkingFee: 0,
      serviceFee: 0,
      remark: "",
    },
    // {
    //   type: "0",
    //   startTime: "",
    //   endTime: "23:59",
    //   chargeFee: 0,
    //   occupancyFee: 0,
    //   parkingFee: 0,
    //   serviceFee: 0,
    //   remark: "",
    // },
  ]
  formData.value = row || {}
  if (row.id) {
    ruleApi
      .getRuleTimeDetail({
        id: row.id, // 计费规则时段id
      })
      .then((res) => {
        formData.value = res.data
        let year = new Date().getFullYear()
        formData.value.dateRange = [year + "-" + res.data.beginTime, year + "-" + res.data.endTime]
        formData.value.startTime = year + "-" + res.data.beginTime
        formData.value.endTime = year + "-" + res.data.endTime
        ruleDetailList.value = res.data.ruleDetailList.map((item) => {
          return {
            ...item,
            startTime: item.startTime.substring(0, 5),
            endTime: item.endTime.substring(0, 5),
            type: item.type.toString(),
          }
        })
      })
  }
}
function addDom() {
  if (ruleDetailList.value.length === 1) {
    ruleDetailList.value = [
      {
        type: "0",
        startTime: "00:00",
        endTime: "",
        chargeFee: 0,
        occupancyFee: 0,
        parkingFee: 0,
        serviceFee: 0,
        remark: "",
      },
      {
        type: "0",
        startTime: "",
        endTime: "23:59",
        chargeFee: 0,
        occupancyFee: 0,
        parkingFee: 0,
        serviceFee: 0,
        remark: "",
      },
    ]
  } else {
    let lastElement = ruleDetailList.value[ruleDetailList.value.length - 2]
    // 在ruleDetailList的倒数第二个位置插入元素
    ruleDetailList.value.splice(ruleDetailList.value.length - 1, 0, {
      type: "0",
      startTime: lastElement.endTime,
      endTime: "",
      chargeFee: 0,
      occupancyFee: 0,
      parkingFee: 0,
      serviceFee: 0,
      remark: "",
    })
  }
}
function removeDom(index) {
  ruleDetailList.value.splice(index, 1)
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      let params = {
        ruleId: ruleId.value,
        id: formData.value.id,
        remark: formData.value.remark,
        beginTime: formData.value.startTime || "",
        endTime: formData.value.endTime || "",
        ruleDetailList: ruleDetailList.value.map((item) => {
          return {
            ...item,
            startTime: item.startTime + ":00",
            endTime: item.endTime + ":00",
          }
        }),
      }
      if (formData.value.id) {
        ruleApi.updateRuleTime(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          dialogVisible.value = false
          emit("successful")
        })
      } else {
        ruleApi.addRuleTime(params).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          dialogVisible.value = false
          emit("successful")
        })
      }
    }
  })
}

defineExpose({
  onOpen,
})
</script>
<style scoped lang="scss">
.data {
  margin-left: 20px;
  margin-top: 20px;
  margin-right: 10px;
}

.order {
  display: flex;
  flex-direction: column;
  color: #333;

  .order-1 {
    padding: 11px 0px;
    display: flex;

    .text {
      color: #606266;
      font-weight: 600;
      text-align: right;
      padding-right: 12px;
      width: 100px;
    }
  }
}
</style>
