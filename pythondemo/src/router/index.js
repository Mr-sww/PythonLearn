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

const routes = [
  { path: '/', component: HomePage },
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

export default router
