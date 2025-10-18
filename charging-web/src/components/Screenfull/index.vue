<template>
  <div class="">
    <svg-icon :icon-class="isFullscreen ? 'exit-fullscreen' : 'fullscreen'" @click="toggle" />
  </div>
</template>

<script setup>
import { useFullscreen } from "@vueuse/core"

const { isFullscreen, enter, exit, toggle } = useFullscreen()
</script>

<style lang="scss" scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
