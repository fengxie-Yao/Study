import request from '@/utils/request'
import type {
  User,
  UserQuery,
  UserSaveForm,
  PageResult,
  RegisterResponse
} from '@/types/user';
import {del, put, get, post} from "@/utils/http.ts";

export function registerApi(data: {
  username: string
  password: string
}) {
  return post<RegisterResponse>('/api/user/register', data)
}


export const userApi = {

  // 获取列表
  getList: (params: UserQuery) => {
    return get<PageResult<User>>('/api/user', params)

  },

  // 新增/修改 （查看是否存在 id)
  save: (data: UserSaveForm) => { // ✅ 统一用 data
    if (data.id) {
      return put<PageResult<User>>(`/api/user/${data.id}`, data)}
    else {
      return post<PageResult<User>>('/api/user', data)
    }
  },

  // 删除
  delete: (id: number) => {
    return del(`/api/user/${id}`)
  },

  // 获取角色字典
  getRoles: () => {
    return get<string[]>('/api/user/roles')
  },
};
