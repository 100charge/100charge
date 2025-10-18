<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="systemConfig.logo" :src="systemConfig.logo" class="sidebar-logo" />
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img src="@/assets/logo/100-logo.png" class="sidebar-logo-long" />
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import systemConfig from "@/systemConfig"
import useSettingsStore from "@/store/modules/settings"
const { proxy } = getCurrentInstance()

defineProps({
  collapse: {
    type: Boolean,
    required: true,
  },
})
const title = "铱佰能链"
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  // line-height: 50px;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex !important;
    align-items: center;
    justify-content: center;

    & .sidebar-logo {
      width: 32px;
      height: 32px;
      // vertical-align: middle;
      margin-right: 12px;
    }

    & .sidebar-logo-long {
      height: 42px;
      vertical-align: middle;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: var(--el-color-primary);
      font-weight: 600;
      line-height: 20px;
      font-size: 14px;
      // text-align: left;
      // font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      // vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
