<template>
  <div class="login">
    <!-- 左上角logo -->
    <div class="header-logo">
      <img src="@/assets/logo/100-logo.png" alt="" class="login-logo-img" />
    </div>

    <div class="middle-bg">
      <!-- 中间部分form -->
      <div class="middle-view">
        <div class="left-wrapper">
          <img src="@/assets/images/slog.png" alt="" />
        </div>
        <div class="right-wrapper">
          <div class="header">
            <span :class="activePhone ? 'active' : 'tab-span'" @click="handlePhone(1)">手机号登录</span>
            <span :class="!activePhone ? 'active' : 'tab-span'" @click="handlePhone(0)">账号登录</span>
          </div>
          <div class="bottom-block"></div>
          <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form" :label-position="'top'">
            <el-form-item prop="username" label="账号" v-if="!activePhone">
              <el-input v-model="loginForm.username" type="text" size="large" auto-complete="off" placeholder="账号">
              </el-input>
            </el-form-item>
            <el-form-item prop="password" label="密码" v-if="!activePhone">
              <el-input v-model="loginForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
                show-password @keyup.enter="handleLogin">
              </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaEnabled && !activePhone" label="验证码">
              <el-input v-model="loginForm.code" size="large" auto-complete="off" placeholder="验证码" style="width: 230px"
                @keyup.enter="handleLogin">
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" />
              </div>
            </el-form-item>
            <el-checkbox v-if="!activePhone" v-model="loginForm.rememberMe"
              style="margin: 0px 0px 25px 0px">记住密码</el-checkbox>

            <!-- 手机号登录 -->
            <el-form-item prop="phone" label="手机号" v-if="activePhone">
              <el-input v-model="loginForm.phone" type="text" size="large" auto-complete="off" placeholder="手机号">
              </el-input>
            </el-form-item>
            <el-form-item prop="phoneCode" v-if="activePhone" label="验证码" style="margin-top: 36px">
              <el-input v-model="loginForm.phoneCode" size="large" auto-complete="off" placeholder="验证码"
                style="width: 230px" @keyup.enter="handleLogin">
              </el-input>
              <div class="phone-code">
                <el-button :disabled="!canGetCode || countdown > 0" type="default"
                  style="margin-left: 12px; height: 38px" @click="getVerifyCode">{{ countdown > 0 ? `${countdown}s后重试` :
                    "获取验证码" }}</el-button>
              </div>
            </el-form-item>

            <div class="privacy-row" v-if="activePhone">
              <el-checkbox v-model="agreePrivacy">同意</el-checkbox>
              <span class="privacy-link" style="z-index: 1000;" @click="openPrivacyModal">《隐私协议》</span>
              <div class="wx-text" @click="handleWx" style="margin-left: auto">
                <span class="wx-text-span">联系管理员</span>

                <div class="wx-code-modal">
                  <img v-if="systemInfo.adminWechatQrCode" :src="systemInfo.adminWechatQrCode" alt="" />
                  <img v-else src="@/assets/images/adminWx.png" alt="" />
                  扫码加微 请备注 100
                </div>
              </div>
            </div>

            <el-form-item style="width: 100%">
              <el-button :loading="loading" size="large" type="primary" style="width: 100%" color="#626aef"
                :dark="isDark" @click.prevent="handleLogin" class="submit-btn">
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 隐私协议模态窗 -->
    <el-dialog v-model="showPrivacyModal" title="隐私协议" width="600px" :close-on-click-modal="false"
      :close-on-press-escape="false" :show-close="false">
      <div class="privacy-content">
        <p>欢迎使用100Charge开源充电桩运营系统（以下简称“本平台”）。我们深知个人信息对您的重要性，并会尽全力保护您的个人信息安全。请您在使用本平台前仔细阅读并充分理解本隐私协议的全部内容。</p>

        <p><strong>一、信息收集</strong><br />
          1.1 当您使用本平台时，您需要使用<strong>本人真实有效的手机号码</strong>进行登录。我们通过发送短信验证码的方式核验您的身份。<br />
          1.2 您在使用充电桩管理、订单查询、数据统计等功能时，本平台会自动收集相关的业务操作日志，以确保服务正常运行。</p>

        <p><strong>二、信息使用</strong><br />
          2.1 您的手机号码将作为您登录系统的<strong>唯一账号标识</strong>，用于区分不同的运营主体和用户。<br />
        
          2.2 <strong>销售与服务回访</strong>：我们可能会根据您提交的手机号码，在必要时通过电话、短信或微信等方式联系您，用于产品功能通知、技术支持、满意度调研及运营指导。您有权随时拒绝此类回访。</p>

        <p><strong>三、Cookie 使用</strong><br />
          我们使用 Cookie 来记忆您的登录状态，提升您在使用本平台时的体验。您可以根据浏览器的设置拒绝或管理 Cookie，但可能会影响部分功能的正常使用。</p>

        <p><strong>四、您的权利</strong><br />
          4.1 您有权在系统设置中<strong>访问和更正</strong>您绑定的手机号码。<br />
          4.2 您有权通过联系我们<strong>注销账号</strong>，账号注销后我们将删除或匿名化处理您的个人信息。<br />
          4.3 您有权随时通过联系客服或回复短信，要求停止一切非必要的<strong>销售回访</strong>。</p>

        <p><strong>五、协议变更</strong><br />
          我们可能会根据法律法规的变化或系统功能的更新对本协议进行修订。对于重大变更，我们将在系统登录页或官网发布公告，修订后的协议将在公告载明的生效日期生效。</p>

        <p><strong>六、联系我们</strong><br />
          如您对本协议有任何疑问、意见或建议，可通过以下方式联系我们：<br />
          - 联系电话:15069016292<br />
        </p>
      </div>
      <template #footer>
        <el-button @click="rejectPrivacy">拒绝</el-button>
        <el-button type="primary" @click="acceptPrivacy">同意</el-button>
      </template>
    </el-dialog>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ "Copyright © 2017-" + new Date().getFullYear() + " xingchuan All Rights Reserved." }}</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg, sendCode } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from "@/store/modules/user"
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const activePhone = ref(true)
const systemInfo1 = JSON.parse(Cookies.get("SystemInfo") || "{}")
const systemInfo = {
  ...systemInfo1,
  homeLogo: systemInfo1.homeLogo
    ? systemInfo1.homeLogo.includes("http")
      ? systemInfo1.homeLogo
      : "https://demo-ems.zhitancloud.com" + systemInfo1.homeLogo
    : "",
  adminWechatQrCode: systemInfo1.adminWechatQrCode
    ? systemInfo1.adminWechatQrCode.includes("http")
      ? systemInfo1.adminWechatQrCode
      : "https://demo-ems.zhitancloud.com" + systemInfo1.adminWechatQrCode
    : "",
}

const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
  code: "",
  uuid: "",
  phone: "",
  phoneCode: "",
})
const validatePhone = (rule, value, callback) => {
  if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error("请输入正确的手机号"))
  } else {
    callback()
  }
}
const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }],
  phoneCode: [
    { required: true, trigger: "change", message: "请输入验证码" },
  ],
  phone: [
    { required: true, trigger: "blur", message: "请输入您的手机号" },
    { validator: validatePhone, trigger: "blur" },
  ],
}

const agreePrivacy = ref(false)
const showPrivacyModal = ref(false)

function openPrivacyModal() {
  console.log("打开隐私协议模态窗")
  showPrivacyModal.value = true
}

function acceptPrivacy() {
  agreePrivacy.value = true
  showPrivacyModal.value = false
}

function rejectPrivacy() {
  agreePrivacy.value = false
  showPrivacyModal.value = false
}

const countdown = ref(0)
const canGetCode = ref(true)
const timer = ref(null)

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

const handlePhone = (type) => {
  activePhone.value = type === 1
  nextTick(() => {
    proxy.$refs.loginRef?.clearValidate()
  })
}

// 获取手机验证码
async function getVerifyCode() {
  if (!loginForm.value.phone) {
    proxy.$modal.msgError("请输入手机号")
    return
  }
  if (agreePrivacy.value == false) {
    proxy.$modal.msgError("请同意隐私协议")
    return
  }
  const res = await sendCode({ phoneNumber: loginForm.value.phone })
  if (res.code === 200) {
    proxy.$modal.msgSuccess("发送成功")
    startCountdown()
  }
}
// 60秒倒计时
function startCountdown() {
  countdown.value = 60
  timer.value = setInterval(() => {
    if (countdown.value <= 0) {
      clearInterval(timer.value)
    } else {
      countdown.value--
    }
  }, 1000)
}

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
  },
  { immediate: true }
)

function handleLogin() {
  const fields = activePhone.value
    ? ["phone", "phoneCode"]
    : captchaEnabled.value
      ? ["username", "password", "code"]
      : ["username", "password"]


  proxy.$refs.loginRef.validateField(fields, (valid) => {

    if (valid) {
      if (activePhone.value) {

        loading.value = true
        Cookies.set("phone", loginForm.value.phone, { expires: 30 })
        userStore
          .loginBySms(loginForm.value)
          .then(() => {
            const query = route.query
            const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
              if (cur !== "redirect") {
                acc[cur] = query[cur]
              }
              return acc
            }, {})
            router.push({ path: redirect.value || "/", query: otherQueryParams })
          })
          .catch(() => {
            loading.value = false
          })
      } else {
        loading.value = true
        // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
        if (loginForm.value.rememberMe) {
          Cookies.set("username", loginForm.value.username, { expires: 30 })
          Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
          Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
        } else {
          // 否则移除
          Cookies.remove("username")
          Cookies.remove("password")
          Cookies.remove("rememberMe")
        }
        // 调用action的登录方法
        userStore
          .login(loginForm.value)
          .then(() => {
            const query = route.query
            const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
              if (cur !== "redirect") {
                acc[cur] = query[cur]
              }
              return acc
            }, {})
            router.push({ path: redirect.value || "/", query: otherQueryParams })
          })
          .catch(() => {
            loading.value = false
            // 重新获取验证码
            if (captchaEnabled.value) {
              getCode()
            }
          })
      }
    }
  })
}

function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  const phone = Cookies.get("phone")
  loginForm.value = {
    phone: phone === undefined ? loginForm.value.phone : phone,
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
  }
}

getCode()
getCookie()
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  align-items: center;
  height: 100%;
  // background-image: url("../assets/images/bg_02.png");
  // background-repeat: no-repeat;
  // background-size: cover;
  flex-direction: column;
  position: relative;
  min-width: 700px;
  min-height: 700px;
  background: #fff;
}

.header-logo {
  background: #fff;
  height: 94px;
  width: 100%;
  display: flex;
  align-items: center;

  .login-logo-img {
    height: 68px;
    margin-left: 4%;
  }
}

.middle-bg {
  background-image: url("../assets/images/login-background.png");
  background-repeat: no-repeat;
  background-size: cover;
  // background: #1eb897;
  width: 100%;
  height: calc(100vh - 188px);
}

.middle-view {
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 100%;
  width: 1200px;
  // width: 70%;
  margin: 0 auto;

  .left-wrapper {

    // width: 500px;
    img {
      width: 80%;
      margin-top: -200px;
    }
  }

  .login-font {
    font-size: 32px;
    font-weight: 700;
    color: #fff;
    margin-bottom: 10px;
  }
}

.right-wrapper {
  border-radius: 23px;
  background: #ffffff;
  width: 410px;
  position: relative;

  .header {
    height: 56px;
    line-height: 56px;
    border-bottom: 1px solid #f1f1f1;
    color: #3b3b3b;
    font-size: 18px;
    margin-bottom: 22px;

    .tab-span,
    .active {
      display: inline-block;
      height: 56px;
      line-height: 62px;
      margin-left: 32px;
      cursor: pointer;
    }

    .active {
      border-bottom: 4px solid var(--el-color-primary);
    }
  }
}

:deep(.el-input__wrapper) {
  background-color: #f7f8fa !important;
  border: none;
}

:deep(.el-input__inner) {
  color: #3b3b3b;
}

:deep(.el-form-item__label) {
  color: #3b3b3b !important;
}

:deep(.el-checkbox__label) {
  color: #2e2e2e;
}

.bottom-block {
  height: 140px;
  width: 90%;
  background-color: rgba(255, 255, 255, 0.3);
  position: absolute;
  left: 5%;
  bottom: -20px;
  border-radius: 20px;
}

.login-form {
  padding: 0 32px 20px;

  .submit-btn {
    width: 360px;
    height: 44px;
    border-radius: 3px;
    font-size: 18px;
    box-shadow: 1px 2px 5px var(--el-color-primary);
    background: var(--el-color-primary);
    border: none;
    border-radius: 6px;
  }

  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  // width: 120px;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }

  .login-code-img {
    height: 40px;
    // padding-left: 12px;
  }
}

.phone-code {
  height: 40px;
  text-align: center;
  line-height: 40px;
}

.el-login-footer {
  height: 94px;
  line-height: 94px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #535353;
  font-family: Arial;
  font-size: 14px;
  letter-spacing: 1px;
}

.privacy-row {
  display: flex;
  align-items: center;
  margin-bottom: 0;
  height: 60px;

  .privacy-link {
    color: #3a83fc;
    cursor: pointer;
    font-size: 14px;
    margin-left: 2px;

    &:hover {
      text-decoration: underline;
    }
  }
}

.privacy-content {
  max-height: 360px;
  overflow-y: auto;
  line-height: 1.8;
  color: #333;
  font-size: 14px;

  p {
    margin-bottom: 12px;
  }
}

.wx-div {
  width: 100%;
  text-align: right;
  display: flex;
  justify-content: flex-end;
}

.wx-text {
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
  position: relative;

  .wx-text-span {
    color: #3a83fc;
    display: block;
  }

  .wx-code-modal {
    // 隐藏 透明度0
    opacity: 0;
    pointer-events: none;
    position: absolute;
    right: -230px;
    top: 0;
    border: 1px solid #959393;
    border-radius: 6px;
    padding: 10px;
    display: flex;
    flex-direction: column;
    color: #fff;
    text-align: center;

    img {
      width: 160px;
      height: 160px;
    }
  }
}

.wx-text:hover>.wx-code-modal {
  opacity: 1 !important;
  pointer-events: auto;
}
</style>
