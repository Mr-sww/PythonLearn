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
import LearningRecords from '../views/LearningRecords.vue'
import LoginTest from '../views/LoginTest.vue'
import TestLearningRecord from '../views/TestLearningRecord.vue'
import SessionTest from '../views/SessionTest.vue'
import CourseVideo from '../views/CourseVideo.vue'
import TestVideoRecords from '../views/TestVideoRecords.vue'
import TestVideoClick from '../views/TestVideoClick.vue'
// import AdminDashboard from '../views/AdminDashboard.vue'
import AdminShell from '../views/admin/AdminShell.vue'
import AdminUsers from '../views/admin/AdminUsers.vue'
import AdminSystemLogs from '../views/admin/AdminSystemLogs.vue'
import StudentDashboard from '../views/StudentDashboard.vue'
import MyCourses from '../views/MyCourses.vue'
import TeacherCourses from '../views/TeacherCourses.vue'
// 新增页面（懒加载占位亦可）
const TeacherClasses = () => import('@/views/teacher/TeacherClasses.vue')
const TeacherCourseRequests = () => import('@/views/teacher/TeacherCourseRequests.vue')
const AdminCourseApprovals = () => import('@/views/admin/AdminCourseApprovals.vue')
const StudentMyClasses = () => import('@/views/student/MyClasses.vue')

const routes = [
  { path: '/', component: HomePage },
  { path: '/auth', component: AuthPage },
  { path: '/login-test', component: LoginTest },
  { path: '/test-learning', component: TestLearningRecord },
  { path: '/test-video', component: TestVideoRecords },
  { path: '/test-video-click', component: TestVideoClick },
  { path: '/session-test', component: SessionTest },
  { path: '/courses', component: CourseCenter },
  { path: '/learning', component: LearningCenter },
  { path: '/practice', component: PracticeCenter },
  { path: '/problems', component: ProblemList },
  { path: '/code-practice', component: CodePractice },
  { path: '/problem/:id', component: CodePractice },
  { path: '/course/:id', component: CourseDetailPage },
  { path: '/courses/:courseId/videos/:videoId', component: CourseVideo },
  { path: '/profile', component: ProfilePage },
  { path: '/exercise/:id', component: ExerciseDetailPage },
  { path: '/ai', component: AIChatPage },
  { path: '/practice-records', component: PracticeRecords },
  { path: '/learning-records', component: LearningRecords },
  
  // 身份管理路由
  {
    path: '/admin',
    component: AdminShell,
    meta: { requiresAuth: true, requiresRole: 'admin' },
    children: [
      { path: '', redirect: '/admin/users' },
      { path: 'users', component: AdminUsers },
      { path: 'logs', component: AdminSystemLogs },
      { path: 'course-approvals', component: () => import('@/views/admin/AdminCourseApprovals.vue') },
      { path: 'courses', component: () => import('@/views/admin/AdminCourses.vue') },
      { path: 'problems', component: () => import('@/views/admin/AdminProblems.vue') },
      { path: 'knowledges', component: () => import('@/views/admin/AdminKnowledges.vue') }
    ]
  },

  { path: '/teacher/courses', component: TeacherClasses, meta: { requiresAuth: true, requiresRole: 'teacher' } },
  { path: '/teacher/course-requests', component: TeacherCourseRequests, meta: { requiresAuth: true, requiresRole: 'teacher' } },
  { 
    path: '/student', 
    component: StudentDashboard,
    meta: { requiresAuth: true, requiresRole: 'student' }
  },
  { path: '/student/my-classes', component: StudentMyClasses, meta: { requiresAuth: true, requiresRole: 'student' } },
  { path: '/admin/course-approvals', component: AdminCourseApprovals, meta: { requiresAuth: true, requiresRole: 'admin' } },
  
  // 课程相关路由
  {
    path: '/my-courses',
    name: 'MyCourses',
    component: MyCourses,
    meta: { requiresAuth: true, requiresRole: 'student' }
  },
  {
    path: '/teacher-courses',
    name: 'TeacherCourses',
    component: TeacherCourses,
    meta: { requiresAuth: true, requiresRole: 'teacher' }
  },
  
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

// 全局路由守卫 - 检查登录状态和角色权限
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
    return
  }
  
  // 检查角色权限
  if (to.meta.requiresRole) {
    // 从localStorage获取用户角色，如果没有则从用户信息中计算
    let userRole = localStorage.getItem('userRole')
    
    if (!userRole && user.groupType) {
      // 根据group_type计算角色
      if (user.groupType >= 1 && user.groupType <= 6) {
        userRole = 'student'
      } else if (user.groupType === 7) {
        userRole = 'teacher'
      } else if (user.groupType === 8) {
        userRole = 'admin'
      } else {
        userRole = 'student'
      }
      // 保存到localStorage
      localStorage.setItem('userRole', userRole)
    }
    
    if (userRole !== to.meta.requiresRole) {
      // 角色不匹配，根据用户角色跳转到对应的仪表板
      switch (userRole) {
        case 'admin':
          next('/admin')
          break
        case 'teacher':
          next('/teacher')
          break
        case 'student':
          next('/student')
          break
        default:
          next('/')
      }
      return
    }
  }
  
  // 权限检查通过，允许访问
  next()
})

export default router
