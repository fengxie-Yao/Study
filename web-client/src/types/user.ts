// src/types/user.ts
export interface User {
  id: number
  username: string
  roles: string[]
}

export interface LoginResponse {
  token: string
  user: User
}
