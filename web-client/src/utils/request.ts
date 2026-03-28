// src/utils/request.ts
import axios from 'axios'

import type { AxiosInstance, AxiosResponse } from 'axios'
import type { Result } from '@/types/result'
import router from '@/router'
import { useUserStore } from '@/stores/user'

const service: AxiosInstance = axios.create({
  baseURL: '/api/user',
  timeout: 5000
})


/**
 * 请求拦截器
 */
service.interceptors.request.use(config => {
  const userStore = useUserStore()

  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }

  return config
})

/**
 * 响应拦截器
 */
service.interceptors.response.use(
  <T>(response: AxiosResponse<Result<T>>) => {
    const res = response.data

    if (res.code !== 200) {
      return Promise.reject(res.message)
    }

    return res.data
  },
  error => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
    }

    return Promise.reject(error)
  }
)

export default service
