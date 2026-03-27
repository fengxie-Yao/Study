// src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus' // 1. 引入 Element Plus
import 'element-plus/dist/index.css'   // 2. 引入 CSS 样式 (必须！)

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus) // 3. 注册插件

app.mount('#app')
