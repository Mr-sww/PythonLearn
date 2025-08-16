<template>
  <div class="admin-dashboard min-h-screen bg-gray-50">
    <!-- 顶部导航 -->
    <div class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <h1 class="text-2xl font-bold text-gray-900">管理员后台</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-gray-600">欢迎，{{ user?.nickname || '管理员' }}</span>
            <button @click="logout" class="text-red-600 hover:text-red-800">退出</button>
          </div>
        </div>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- 统计卡片 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-blue-100 rounded-lg">
              <i class="fa fa-users text-blue-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总用户数</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.totalUsers }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-green-100 rounded-lg">
              <i class="fa fa-book text-green-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总课程数</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.totalCourses }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-yellow-100 rounded-lg">
              <i class="fa fa-code text-yellow-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总题目数</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.totalProblems }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-purple-100 rounded-lg">
              <i class="fa fa-chart-line text-purple-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">今日活跃</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.activeToday }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 功能模块 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- 用户管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">用户管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showUserManagement = true" class="w-full bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
                管理用户账户
              </button>
              <button @click="showRoleManagement = true" class="w-full bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">
                角色权限管理
              </button>
            </div>
          </div>
        </div>

        <!-- 内容管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">内容管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showCourseManagement = true" class="w-full bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors">
                课程内容审核
              </button>
              <button @click="showProblemManagement = true" class="w-full bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition-colors">
                题目内容审核
              </button>
            </div>
          </div>
        </div>

        <!-- 系统监控 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">系统监控</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showSystemMonitor = true" class="w-full bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors">
                系统性能监控
              </button>
              <button @click="showLogs = true" class="w-full bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors">
                系统日志查看
              </button>
            </div>
          </div>
        </div>

        <!-- 数据统计 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">数据统计</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showUserAnalytics = true" class="w-full bg-teal-600 text-white px-4 py-2 rounded-lg hover:bg-teal-700 transition-colors">
                用户行为分析
              </button>
              <button @click="showCourseAnalytics = true" class="w-full bg-pink-600 text-white px-4 py-2 rounded-lg hover:bg-pink-700 transition-colors">
                课程学习统计
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 模态框 -->
    <div v-if="showUserManagement" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium">用户管理</h3>
          <button @click="showUserManagement = false" class="text-gray-400 hover:text-gray-600">
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div class="text-center py-8">
          <i class="fa fa-users text-4xl text-gray-400 mb-4"></i>
          <p class="text-gray-600">用户管理功能开发中...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentUser, isAdmin, clearLoginState } from '@/utils/auth'

export default {
  name: 'AdminDashboard',
  setup() {
    const router = useRouter()
    const user = ref(null)
    const stats = ref({
      totalUsers: 1250,
      totalCourses: 89,
      totalProblems: 456,
      activeToday: 234
    })

    const showUserManagement = ref(false)
    const showRoleManagement = ref(false)
    const showCourseManagement = ref(false)
    const showProblemManagement = ref(false)
    const showSystemMonitor = ref(false)
    const showLogs = ref(false)
    const showUserAnalytics = ref(false)
    const showCourseAnalytics = ref(false)

    onMounted(() => {
      // 检查管理员权限
      if (!isAdmin()) {
        router.push('/')
        return
      }
      
      user.value = getCurrentUser()
    })

    const logout = () => {
      clearLoginState()
      router.push('/auth')
    }

    return {
      user,
      stats,
      showUserManagement,
      showRoleManagement,
      showCourseManagement,
      showProblemManagement,
      showSystemMonitor,
      showLogs,
      showUserAnalytics,
      showCourseAnalytics,
      logout
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
</style>

