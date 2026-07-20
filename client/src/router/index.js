import Main from '@/components/Main.vue'
import MemberCreate from '@/components/MemberCreate.vue'
import MemberLogin from '@/components/MemberLogin.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routers = [
  {
    path: '/',
    component: Main
  },
  {
    path: '/member/create',
    component: MemberCreate
  },
  {
    path: '/member/login',
    component: MemberLogin
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routers,
})

export default router
