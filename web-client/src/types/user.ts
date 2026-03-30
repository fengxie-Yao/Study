// src/types/user.ts
export interface User {
  id: number;
  username: string;
  nickname: string;
  email: string;
  enabled: boolean;
  roles: string[];
  createTime: string;
}

export interface LoginResponse {
  token: string
  user: User
}

export interface RegisterResponse {
  code: string
}

export interface UserQuery {
  pageNum: number;
  pageSize: number;
  username?: string;
  nickname?: string;
  role?: string;
}

export interface UserSaveForm {
  id?: number;
  username?: string;
  password?: string; // 选填，修改时不填则不变
  nickname?: string;
  email?: string;
  enabled: boolean;
  roles: string[];
}

// 分页响应结构 (适配 Spring Data Page)
export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number; // 当前页 (0-based)
}
