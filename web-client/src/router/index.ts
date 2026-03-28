import { createRouter, createWebHistory } from 'vue-router'

import LoginViewView from '../views/LoginView.vue'
import RegisterViewView from '../views/RegisterView.vue'
import HomeViewView from '../views/HomeView.vue'

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
