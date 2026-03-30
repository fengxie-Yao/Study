// src/utils/http.ts
import request from './request'

export function get<T>(url: string, params?: any): Promise<T> {
  return request.get(url, { params })
}

export function post<T>(url: string, data?: any): Promise<T> {
  return request.post(url, data)
}

export function put<T>(url: string, data?: any): Promise<T> {
  return request.put(url, data)
}


export function del<T>(url: string): Promise<T> {
  return request.delete(url)
}



