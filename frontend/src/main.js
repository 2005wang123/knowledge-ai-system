import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'
// 导入marked库
import { marked } from 'marked'
// 【关键缺失的一行】导入路由实例，必须加这一行！
import router from './router'

const app = createApp(App)

// 全局配置axios
axios.defaults.baseURL = 'http://localhost:8080/api'
axios.defaults.timeout = 120000
axios.defaults.withCredentials = true
app.config.globalProperties.$axios = axios

// 全局挂载marked，模板里就能用$marked
app.config.globalProperties.$marked = marked

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 核心：注册路由，这里的router必须是上面导入的变量
app.use(router)
app.use(ElementPlus)

// 挂载应用
app.mount('#app')