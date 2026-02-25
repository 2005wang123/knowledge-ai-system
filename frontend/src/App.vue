<template>
  <div id="app">
    <!-- 路由出口，页面会渲染在这里 -->
    <router-view></router-view>
  </div>
</template>

<script setup>
// 根组件，用于承载整个项目的路由页面
</script>

<style>
/* 全局样式重置，避免页面错乱 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}
</style>