import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomePage.vue'
import CourseDetailPage from '../views/CourseDetailPage.vue'
import ProfilePage from '../views/ProfilePage.vue'
import ExerciseDetailPage from '../views/ExerciseDetailPage.vue'
import AIChatPage from '../views/AIChatPage.vue'
import CourseCenter from '../views/CourseCenter.vue'
import LearningCenter from '../views/LearningCenter.vue'
import PracticeCenter from '../views/PracticeCenter.vue'
import CodePractice from '../views/CodePractice.vue'
import ProblemList from '../views/ProblemList.vue'
import AuthPage from '../views/AuthPage.vue'
import PracticeRecords from '../views/PracticeRecords.vue'

const routes = [
  { path: '/', component: HomePage },
  { path: '/auth', component: AuthPage },
  { path: '/courses', component: CourseCenter },
  { path: '/learning', component: LearningCenter },
  { path: '/practice', component: PracticeCenter },
  { path: '/problems', component: ProblemList },
  { path: '/code-practice', component: CodePractice },
  { path: '/problem/:id', component: CodePractice },
  { path: '/course/:id', component: CourseDetailPage },
  { path: '/profile', component: ProfilePage },
  { path: '/exercise/:id', component: ExerciseDetailPage },
  { path: '/ai', component: AIChatPage },
  { path: '/practice-records', component: PracticeRecords },
  {
    path: '/favorites',
    name: 'FavoriteCourses',
    component: () => import('@/views/FavoriteCourses.vue')
  },
  {
    path: '/learn-detail',
    name: 'LearnDetial',
    component: () => import('@/views/LearnDetial.vue')
  },
  {
    path: '/favorite-problems',
    name: 'FavoriteProblems',
    component: () => import('@/views/FavoriteProblems.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  // 不需要登录检查的路径
  const publicPaths = ['/auth']
  
  // 检查是否访问的是公开路径
  if (publicPaths.includes(to.path)) {
    next()
    return
  }
  
  // 检查用户是否已登录
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  const isLoggedIn = user && (user.userId || user.user_id)
  
  if (!isLoggedIn) {
    // 未登录，保存当前路径并跳转到登录页
    localStorage.setItem('redirectPath', to.fullPath)
    next('/auth')
  } else {
    // 已登录，允许访问
    next()
  }
})

export default router
