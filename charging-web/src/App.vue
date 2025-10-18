<template>
  <router-view />
</template>

<script setup>
import useSettingsStore from "@/store/modules/settings"
import { handleThemeStyle } from "@/utils/theme"
import systemConfig from "@/systemConfig"
onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
    document.querySelector("body").className = "themeDark"

    // 网页title
    document.title = systemConfig.defaultTitle || ""

    // 动态修改网页icon
    let logo = systemConfig.loginLogo || systemConfig.logo
    if (logo) {
      document.querySelectorAll("link[rel*='icon']").forEach((item) => {
        item.href = logo
      })
    }
  })
})
</script>
