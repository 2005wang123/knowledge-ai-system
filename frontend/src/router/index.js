import { createRouter, createWebHistory } from 'vue-router'

// 导入你的主页面
const Index = () => import('../views/Index.vue')

// 路由规则
const routes = [
  {
    path: '/',
    name: 'Index',
    component: Index,
    meta: { title: '智能知识库问答系统' }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：自动设置页面标题
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router