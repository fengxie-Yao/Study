// src/api/request.ts
import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 定义后端基础地址
const BASE_URL = 'http://localhost:8080'

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 5000 // 请求超时时间
})

// --- 请求拦截器 ---
request.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      // 设置 Authorization 头，格式为 "Bearer <token>"
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// --- 响应拦截器 ---
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data

    // 假设后端统一返回格式：{ code: 200, message: 'success', data: ... }
    // 如果业务状态码不是 200，说明业务逻辑出错（如密码错误）
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')

      // 特殊处理：如果是 401 (未授权/Token 过期)，清除本地存储并跳转登录页
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        router.push('/login')
      }

      return Promise.reject(new Error(res.message || 'Error'))
    }

    // 成功则直接返回数据部分
    return res
  },
  (error) => {
    // 网络错误或服务器崩溃 (如 500, 404)
    ElMessage.error(error.message || '网络连接失败')
    return Promise.reject(error)
  }
)

export default request
