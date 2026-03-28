// src/utils/permission.ts
import { useUserStore } from '@/stores/user'

export function hasRole(role: string): boolean {
  const userStore = useUserStore()
  return userStore.roles.includes(role)
}
