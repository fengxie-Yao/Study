// src/utils/request.ts
import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import type { Result } from '@/types/result'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useUserStore } from '@/stores/user'

// 基础配置
const BASE_URL = 'http://localhost:8080'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use((config) => {
  const userStore = useUserStore()
  config.headers['Content-Type'] = 'application/json'
  // console.log('Request URL:', config.url)
  if (config.url !== '/user/login') {
    const token = userStore.token || localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
  }

  return config
}, (error) => {
  return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(
  <T>(response: AxiosResponse<Result<T>>) => {
    const res = response.data

    // 业务错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')

      // 401 未授权 → 登出并跳转到登录页
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.logout()
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        router.push('/login')
      }
      return Promise.reject(res.message)
    }

    // 成功：直接返回 data（最常用）
    return res.data
  },
  (error) => {
    // 网络/服务器错误
    ElMessage.error(error.message || '网络连接失败')
    return Promise.reject(error)
  }
)

export default service
