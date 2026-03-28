// src/utils/http.ts
import request from './request'

export function get<T>(url: string, params?: any): Promise<T> {
  return request.get(url, { params })
}

export function post<T>(url: string, data?: any): Promise<T> {
  return request.post(url, data)
}
