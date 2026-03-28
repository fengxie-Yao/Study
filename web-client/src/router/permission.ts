// src/router/permission.ts
import router from './index'
import { useUserStore } from '@/stores/user'

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (!userStore.token) {
    if (to.path !== '/login') return next('/login')
    return next()
  }

  if (to.path === '/login') return next('/')

  // RBAC
  if (to.meta.role) {
    if (!userStore.roles.includes(to.meta.role as string)) {
      return next('/403')
    }
  }

  next()
})
