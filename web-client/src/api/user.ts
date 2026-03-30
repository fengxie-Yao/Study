import request from '@/utils/request'
import type {
  User,
  UserQuery,
  UserSaveForm,
  PageResult,
  RegisterResponse
} from '@/types/user';
import {del, get, post} from "@/utils/http.ts";

export function registerApi(data: {
  username: string
  password: string
}) {
  return post<RegisterResponse>('/api/user/register', data)
}


export const userApi = {

  // 获取列表
  getList: (params: UserQuery) => {
    return get<PageResult<User>>('/api/users', params)

  },

  // 新增/修改 (通过是否有 id 区分，或者分开两个方法)
  save: (data: UserSaveForm) => { // ✅ 统一用 data
    if (data.id) {
      return get<PageResult<User>>(`/api/users/${data.id}`, data)}
    else {
      return get<PageResult<User>>('/api/users', data)
    }
  },

  // 删除
  delete: (id: number) => {
    return del(`/api/users/${id}`)
  },

  // 获取角色字典
  getRoles: () => {
    return get<string[]>('/api/users/roles')
  },
};
