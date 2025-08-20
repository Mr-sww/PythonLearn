<template>
  <nav ref="navBar" class="global-navbar bg-gradient-to-r from-blue-600 to-blue-400 shadow-lg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- Logo区域 -->
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <h1 class="text-white text-xl font-bold">Python学习平台</h1>
          </div>
        </div>

        <!-- 导航菜单 -->
        <div class="hidden md:block">
          <div class="ml-10 flex items-baseline space-x-4">
            <router-link 
              v-for="item in navItems" 
              :key="item.path"
              :to="item.path"
              class="global-nav-link px-4 py-3 rounded-md text-lg font-semibold text-white hover:text-white hover:bg-blue-700 transition-all duration-200 no-underline"
              :class="{ 'is-active': $route.path === item.path }"
            >
              <span class="emoji">{{ item.emoji }}</span>
              {{ item.name }}
            </router-link>
          </div>
        </div>

        <!-- 用户区 -->
        <div class="flex items-center space-x-4">
          <!-- 用户菜单 -->
          <div class="relative">
            <button @click="showUserMenu = !showUserMenu" class="flex items-center space-x-2 text-white hover:text-blue-200 transition-colors">
              <img v-if="user?.avatar" :src="user.avatar" alt="头像" class="w-8 h-8 rounded-full">
              <div v-else class="w-8 h-8 bg-blue-300 rounded-full flex items-center justify-center">
                <i class="fa fa-user text-blue-600"></i>
              </div>
              <span class="text-base">{{ user?.nickname || '用户' }}</span>
              <i class="fa fa-chevron-down text-sm"></i>
            </button>
            
            <!-- 用户下拉菜单 -->
            <div v-if="showUserMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50">
              <router-link to="/profile" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                <i class="fa fa-user-circle mr-2"></i>个人资料
              </router-link>
              
              <router-link to="/practice-records" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                <i class="fa fa-code mr-2"></i>练习记录
              </router-link>
              
              <router-link to="/learning-records" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                <i class="fa fa-book-open mr-2"></i>学习记录
              </router-link>
              
              <!-- 角色相关菜单 -->
              <div v-if="isAdmin" class="border-t border-gray-100">
                <router-link to="/admin" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                  <i class="fa fa-shield-alt mr-2"></i>管理员后台
                </router-link>
              </div>
              
              <div v-if="isTeacher" class="border-t border-gray-100">
                <router-link to="/teacher" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                  <i class="fa fa-chalkboard-teacher mr-2"></i>教师工作台
                </router-link>
                <router-link to="/teacher-courses" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                  <i class="fa fa-chalkboard mr-2"></i>我的课程
                </router-link>
              </div>
              
              <div v-if="isStudent" class="border-t border-gray-100">
                <router-link to="/my-courses" class="block px-4 py-2 text-base text-gray-700 hover:bg-gray-100">
                  <i class="fa fa-bookmark mr-2"></i>我的课程
                </router-link>
              </div>
              
              <div class="border-t border-gray-100">
                <button @click="logout" class="block w-full text-left px-4 py-2 text-base text-red-600 hover:bg-gray-100">
                  <i class="fa fa-power-off mr-2"></i>退出登录
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 移动端菜单按钮 -->
    <div class="md:hidden">
      <button @click="showMobileMenu = !showMobileMenu" class="text-white hover:text-blue-200 transition-colors">
        <i class="fa fa-bars text-xl"></i>
      </button>
    </div>

    <!-- 移动端菜单 -->
    <div v-if="showMobileMenu" class="md:hidden bg-blue-700">
      <div class="px-2 pt-2 pb-3 space-y-1">
        <router-link 
          v-for="item in navItems" 
          :key="item.path"
          :to="item.path"
          class="global-nav-link block px-4 py-3 rounded-md text-lg font-semibold text-white hover:text-white hover:bg-blue-600 transition-all duration-200 no-underline"
          :class="{ 'is-active': $route.path === item.path }"
          @click="showMobileMenu = false"
        >
          <span class="emoji">{{ item.emoji }}</span>
          {{ item.name }}
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentUser, getCurrentUserRole, isAdmin, isTeacher, isStudent, clearLoginState } from '@/utils/auth'

export default {
  name: 'GlobalNavbar',
  setup() {
    const router = useRouter()
    const navBar = ref(null)
    const showUserMenu = ref(false)
    const showMobileMenu = ref(false)
    const user = ref(null)

    // 导航菜单项
    const navItems = ref([
      { name: '首页', path: '/', emoji: '🏠' },
      { name: '课程中心', path: '/courses', emoji: '📚' },
      { name: '学习中心', path: '/learning', emoji: '🎓' },
      { name: '练习中心', path: '/practice', emoji: '💻' },
      { name: 'AI助手', path: '/ai', emoji: '🤖' }
    ])

    // 计算属性
    const userRole = computed(() => getCurrentUserRole())
    const isAdminUser = computed(() => isAdmin())
    const isTeacherUser = computed(() => isTeacher())
    const isStudentUser = computed(() => isStudent())

    const userRoleDisplay = computed(() => {
      switch (userRole.value) {
        case 'admin': return '管理员'
        case 'teacher': return '教师'
        case 'student': return '学生'
        default: return '用户'
      }
    })

    const roleIndicatorClass = computed(() => {
      switch (userRole.value) {
        case 'admin': return 'bg-red-400'
        case 'teacher': return 'bg-yellow-400'
        case 'student': return 'bg-green-400'
        default: return 'bg-gray-400'
      }
    })

    // 方法
    const logout = () => {
      clearLoginState()
      router.push('/auth')
    }

    // 生命周期
    onMounted(() => {
      user.value = getCurrentUser()
      
      // 点击外部关闭用户菜单
      document.addEventListener('click', handleClickOutside)
    })

    onUnmounted(() => {
      document.removeEventListener('click', handleClickOutside)
    })

    const handleClickOutside = (event) => {
      if (showUserMenu.value && !event.target.closest('.relative')) {
        showUserMenu.value = false
      }
    }

    // 监听路由变化，触发跳跃动画
    const watchRoute = () => {
      if (navBar.value) {
        const activeLink = navBar.value.querySelector('.global-nav-link.is-active')
        if (activeLink) {
          // 移除之前的动画类
          activeLink.classList.remove('jump-animation')
          // 强制重绘
          void activeLink.offsetWidth
          // 添加跳跃动画
          activeLink.classList.add('jump-animation')
        }
      }
    }

    // 监听路由变化
    router.afterEach(watchRoute)

    return {
      navBar,
      showUserMenu,
      showMobileMenu,
      user,
      navItems,
      userRole,
      isAdmin: isAdminUser,
      isTeacher: isTeacherUser,
      isStudent: isStudentUser,
      userRoleDisplay,
      roleIndicatorClass,
      logout
    }
  }
}
</script>

<style scoped>
.global-navbar {
  position: sticky;
  top: 0;
  z-index: 40;
}

.global-nav-link {
  position: relative;
  transition: all 0.3s ease;
  text-decoration: none !important;
}

.global-nav-link.is-active {
  background: linear-gradient(135deg, #fef3c7 0%, #fde68a 50%, #fcd34d 100%);
  color: #374151 !important;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(255,255,255,0.9);
  box-shadow: 0 4px 20px rgba(254, 243, 199, 0.4), 0 0 0 1px rgba(254, 243, 199, 0.3);
}

.global-nav-link .emoji {
  margin-right: 10px;
  font-size: 1.5em;
  filter: drop-shadow(0 2px 2px rgba(0,0,0,0.15));
  transition: transform 0.2s ease;
}

.global-nav-link:hover .emoji {
  transform: scale(1.1);
}

.jump-animation .emoji {
  animation: emojiJump 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

@keyframes emojiJump {
  0% { transform: translateY(0) scale(1); }
  25% { transform: translateY(-8px) scale(1.1); }
  50% { transform: translateY(-12px) scale(1.2); }
  75% { transform: translateY(-6px) scale(1.1); }
  100% { transform: translateY(0) scale(1); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .global-nav-link {
    padding: 1rem 1.25rem;
    font-size: 1.125rem;
  }
  
  .global-nav-link .emoji {
    margin-right: 0.75rem;
    font-size: 1.4em;
  }
}
</style>
