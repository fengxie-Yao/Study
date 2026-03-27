<template>
  <div class="home">
    <h1>欢迎回来，{{ username }}</h1>
    <el-button type="danger" @click="logout">退出登录</el-button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref<string>('用户')

onMounted(() => {
  username.value = localStorage.getItem('username') || '未知用户'
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>
