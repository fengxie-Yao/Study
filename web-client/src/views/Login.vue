<template>
  <div>
    <h2>登录</h2>
    <input v-model="username" placeholder="用户名" />
    <input v-model="password" type="password" placeholder="密码" />
    <button @click="handleLogin">登录</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '@/api/user'

const username = ref('')
const password = ref('')

const handleLogin = async () => {
  const res = await login({
    username: username.value,
    password: password.value
  })

  if (res.data.code === 200) {
    // 保存 token
    localStorage.setItem('token', res.data.data.token)

    // 保存用户信息
    localStorage.setItem('user', JSON.stringify(res.data.data.user))

    alert('登录成功')
  } else {
    alert(res.data.message)
  }
}
</script>
