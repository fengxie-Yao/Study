<template>
  <div class="common-layout">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <h2 class="logo">🚀 MySystem</h2>
        </div>

        <div class="header-right">
          <!-- 情况 A: 未登录 -->
          <template v-if="!isLoggedIn">
            <el-button type="info" plain @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" @click="$router.push('/register')">注册</el-button>
          </template>

          <!-- 情况 B: 已登录 -->
          <template v-else>
            <span class="welcome-text">欢迎，<strong>{{ username }}</strong></span>
            <el-button type="danger" plain @click="handleLogout">退出登录</el-button>
          </template>
        </div>
      </el-header>

      <!-- 主要内容区 -->
      <el-main class="main-content">
        <div class="welcome-banner">
          <h1>欢迎来到智能管理系统</h1>
          <p>高效、安全、便捷的用户认证解决方案</p>

          <div class="action-buttons" v-if="!isLoggedIn">
            <el-button type="primary" size="large" @click="$router.push('/register')">立即开始</el-button>
          </div>
        </div>

        <!-- 特性卡片展示 -->
        <el-row :gutter="20" class="feature-cards">
          <el-col :span="8" v-for="item in features" :key="item.title">
            <el-card shadow="hover" class="feature-card">
              <div class="card-icon">{{ item.icon }}</div>
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
            </el-card>
          </el-col>
        </el-row>
      </el-main>

      <el-footer class="footer">
        © 2024 MySystem. All rights reserved.
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const isLoggedIn = ref(false)
const username = ref<string>('')

// 特性数据
const features = [
  { icon: '🔒', title: '安全认证', desc: '基于 Spring Security 6 + JWT 的企业级安全架构' },
  { icon: '⚡', title: '高性能', desc: '前后端分离，响应迅速，支持高并发访问' },
  { icon: '🎨', title: '现代 UI', desc: 'Vue 3 + Element Plus 打造的极致用户体验' }
]

// 检查登录状态
onMounted(() => {
  const token = localStorage.getItem('token')
  const user = localStorage.getItem('username')

  if (token && user) {
    isLoggedIn.value = true
    username.value = user
  }
})

// 退出登录
const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('role')

  isLoggedIn.value = false
  username.value = ''

  ElMessage.success('已安全退出')
  // 可选：刷新页面或跳转回首页
  // window.location.reload()
}
</script>

<style scoped>
.common-layout {
  height: 100vh;
  background-color: #f5f7fa;
}

/* 头部样式 */
.header {
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
}

.logo {
  margin: 0;
  color: #409EFF;
  font-weight: bold;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome-text {
  color: #606266;
  margin-right: 10px;
}

/* 主体内容样式 */
.main-content {
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.welcome-banner {
  text-align: center;
  margin-bottom: 60px;
  padding: 60px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.welcome-banner h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.welcome-banner p {
  font-size: 1.2rem;
  opacity: 0.9;
  margin-bottom: 30px;
}

/* 卡片样式 */
.feature-cards {
  margin-top: 20px;
}

.feature-card {
  text-align: center;
  border: none;
  border-radius: 8px;
  transition: transform 0.3s;
  height: 100%;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.card-icon {
  font-size: 3rem;
  margin-bottom: 15px;
}

.feature-card h3 {
  color: #303133;
  margin-bottom: 10px;
}

.feature-card p {
  color: #909399;
  line-height: 1.6;
}

/* 底部样式 */
.footer {
  text-align: center;
  color: #909399;
  font-size: 14px;
  padding: 20px;
  background-color: #fff;
}
</style>
