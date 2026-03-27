<template>
  <div class="register-container">
    <el-card class="box-card">
      <h2>用户注册</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="设置用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="设置密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="handleRegister" style="width: 100%">注册</el-button>
          <div style="margin-top: 10px; text-align: center;">
            <span>已有账号？</span>
            <el-link type="primary" @click="$router.push('/login')">去登录</el-link>
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
import request from '@/api/request'

const router = useRouter()

interface RegisterForm {
  username: string
  password: string
}

const form = reactive<RegisterForm>({
  username: '',
  password: ''
})

const handleRegister = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写完整信息')
    return
  }

  try {
    const res: any = await request.post('/api/user/register', form)
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    }
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.register-container {
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
