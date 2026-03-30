import { createRouter, createWebHistory } from 'vue-router'

import LoginViewView from '../views/LoginView.vue'
import RegisterViewView from '../views/RegisterView.vue'
import HomeViewView from '../views/HomeView.vue'
import UserManageView from '../views/UserManageView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeViewView,
      meta: { requiresAuth: false }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginViewView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterViewView
    },
    {
      path: '/system/users',
      name: 'UserManage',
      component: UserManageView,
      meta: {
        title: '用户管理',
        requiresAuth: true,
        roles: ['ROLE_ADMIN'] // 只有管理员可访问
      }
    }

  ]
})
// 全局前置守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    // 如果需要登录但没 Token，跳回登录页
    next('/login')
  } else {
    next()
  }
})
export default router
