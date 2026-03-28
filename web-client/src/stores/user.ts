// src/store/user.ts
import { defineStore } from 'pinia'
import type { User, LoginResponse } from '@/types/user'

interface UserState {
  token: string
  user: User | null
  roles: string[]
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    roles: JSON.parse(localStorage.getItem('roles') || '[]')
  }),

  actions: {
    setLogin(data: LoginResponse) {
      this.token = data.token
      this.user = data.user
      this.roles = data.user.roles || []

      localStorage.setItem('token', this.token)
      localStorage.setItem('user', JSON.stringify(this.user))
      localStorage.setItem('roles', JSON.stringify(this.roles))
    },

    logout() {
      this.token = ''
      this.user = null
      this.roles = []

      localStorage.clear()
    }
  }
})
