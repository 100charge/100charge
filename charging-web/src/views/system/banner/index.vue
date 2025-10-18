<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="标题" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入标题"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="展示状态" prop="open">
        <el-select v-model="queryParams.open" placeholder="展示状态" clearable style="width: 200px">
          <el-option v-for="dict in sys_open_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="200" />
      <el-table-column label="标题" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="展示图片" align="center" prop="imageUrl" min-width="200">
        <template #default="scope">
          <ImagePreview :src="scope.row.imageUrl" height="60px" width="200px" />
        </template>
      </el-table-column>
      <el-table-column label="展示状态" min-width="100" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.open"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" v-model="open" width="780px" append-to-body>
      <el-form ref="noticeRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="标题" prop="name">
              <el-input v-model="form.name" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="跳转地址" prop="link">
              <el-input v-model="form.link" placeholder="请输入跳转地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="展示状态">
              <el-radio-group v-model="form.open">
                <el-radio v-for="dict in sys_open_type" :key="dict.value" :label="dict.value">{{
                  dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="图片" prop="imageUrl">
              <ImageUpload :limit="1" v-model="form.imageUrl" customTipText="上传图片尺寸建议700*170" />
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

<script setup name="Banner">
import bannerApi from "@/api/system/banner"
const { proxy } = getCurrentInstance()
const sys_open_type = [
  { label: "启用", value: "1" },
  { label: "停用", value: "0" },
]
const noticeList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    open: undefined,
  },
  rules: {
    name: [{ required: true, message: "标题不能为空", trigger: "blur" }],
    open: [{ required: true, message: "状态不能为空", trigger: "change" }],
    imageUrl: [{ required: true, message: "图片不能为空", trigger: "change" }],
  },
})

const { queryParams, form, rules } = toRefs(data)

/** 查询公告列表 */
function getList() {
  loading.value = true
  bannerApi.page(queryParams.value).then((response) => {
    noticeList.value = response.rows.map((item) => {
      return {
        ...item,
        open: item.open.toString(),
      }
    })
    total.value = response.total
    loading.value = false
  })
}
/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}
/** 表单重置 */
function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    open: "1",
    imageUrl: undefined,
  }
  proxy.resetForm("noticeRef")
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
/** 状态修改 */
function handleStatusChange(row) {
  let text = row.open === "1" ? "启用" : "停用"
  proxy.$modal
    .confirm('确认要"' + text + '""' + row.name + '"吗?')
    .then(function () {
      return bannerApi.changeStatus({
        id: row.id,
        open: row.open,
      })
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功")
    })
    .catch(function () {
      row.status = row.status === "1" ? "1" : "0"
    })
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.noticeId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}
/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加轮播图"
}
/**修改按钮操作 */
function handleUpdate(row) {
  reset()
  form.value = row
  open.value = true
  title.value = "修改轮播图"
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["noticeRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        bannerApi.edit(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        bannerApi.add(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
/** 删除按钮操作 */
function handleDelete(row) {
  const noticeId = row.id
  proxy.$modal
    .confirm('是否确认删除编号为"' + noticeId + '"的数据项？')
    .then(function () {
      return bannerApi.delete({ id: noticeId })
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

getList()
</script>
