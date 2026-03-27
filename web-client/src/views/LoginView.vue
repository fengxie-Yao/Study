<template>
  <div class="login-container">
    <el-card class="box-card">
      <h2>用户登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
          <div style="margin-top: 10px; text-align: center;">
            <span>没有账号？</span>
            <!-- 跳转到注册页 -->
            <el-link type="primary" @click="$router.push('/register')">去注册</el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 引入你的 axios 实例 (假设你创建了 src/api/request.ts)
import request from '@/api/request'

const router = useRouter()
const loading = ref(false)

// 定义表单数据类型
interface LoginForm {
  username: string
  password: string
}

const form = reactive<LoginForm>({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写完整信息')
    return
  }

  loading.value = true
  try {
    // 调用后端接口
    const res: any = await request.post('/api/user/login', form)

    // 假设后端返回结构 { code: 200, data: { token, ... } }
    if (res.code === 200) {
      const { token, username, role } = res.data
      // 保存 Token
      localStorage.setItem('token', token)
      localStorage.setItem('username', username)

      ElMessage.success('登录成功')
      // 跳转首页 (或者你希望的受保护页面)
      router.push('/')
    }
  } catch (error) {
    console.error(error)
    // 错误拦截器里已经处理了 message 提示
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.box-card {
  width: 400px;
}
</style>
