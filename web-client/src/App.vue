<template>
  <!-- router-view 是路由出口，LoginView 等组件会渲染在这里 -->
  <router-view />
</template>

<script setup lang="ts">
// 这里不需要写任何逻辑，除非你有全局布局需求
</script>

<style>
/* 全局样式重置 */
body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  background-color: #f5f7fa; /* 给整个页面一个淡淡的背景色 */
}

/* 移除默认链接下划线 */
a {
  text-decoration: none;
  color: inherit;
}
#app {
  height: 100%;
}
</style>
