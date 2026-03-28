// src/api/auth.ts
import { post } from '@/utils/http'
import type { LoginResponse } from '@/types/user'

export function loginApi(data: {
  username: string
  password: string
}) {
  return post<LoginResponse>('/login', data)
}
