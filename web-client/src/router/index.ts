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
      // 2. 关键：访问根路径时，直接重定向到登录页
      redirect: '/login'
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
      path: '/home',
      name: 'home',
      component: HomeViewView
    }
  ]
})

export default router
