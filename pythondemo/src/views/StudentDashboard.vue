<template>
  <div class="student-dashboard min-h-screen bg-gray-50">
    <!-- 顶部导航 -->
    <div class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <h1 class="text-2xl font-bold text-gray-900">学习中心</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-gray-600">欢迎，{{ user?.nickname || '同学' }}</span>
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
              <i class="fa fa-book text-blue-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">已选课程</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.enrolledCourses }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-green-100 rounded-lg">
              <i class="fa fa-code text-green-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">已完成题目</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.completedProblems }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-yellow-100 rounded-lg">
              <i class="fa fa-clock text-yellow-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">学习时长</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.studyHours }}h</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-purple-100 rounded-lg">
              <i class="fa fa-trophy text-purple-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">平均成绩</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.avgScore }}%</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 功能模块 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- 学习管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">学习管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showMyCourses = true" class="w-full bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
                我的课程
              </button>
              <button @click="showLearningProgress = true" class="w-full bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">
                学习进度
              </button>
              <button @click="showStudyPlan = true" class="w-full bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors">
                学习计划
              </button>
            </div>
          </div>
        </div>

        <!-- 练习管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">练习管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showPracticeHistory = true" class="w-full bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors">
                练习历史
              </button>
              <button @click="showWeakPoints = true" class="w-full bg-teal-600 text-white px-4 py-2 rounded-lg hover:bg-teal-700 transition-colors">
                薄弱知识点
              </button>
              <button @click="showRecommendProblems = true" class="w-full bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition-colors">
                推荐题目
              </button>
            </div>
          </div>
        </div>

        <!-- 作业管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">作业管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showAssignmentList = true" class="w-full bg-yellow-600 text-white px-4 py-2 rounded-lg hover:bg-yellow-700 transition-colors">
                作业列表
              </button>
              <button @click="showSubmittedAssignments = true" class="w-full bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors">
                已提交作业
              </button>
              <button @click="showGrades = true" class="w-full bg-pink-600 text-white px-4 py-2 rounded-lg hover:bg-pink-700 transition-colors">
                成绩查看
              </button>
            </div>
          </div>
        </div>

        <!-- 个人设置 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">个人设置</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showProfileSettings = true" class="w-full bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors">
                个人资料
              </button>
              <button @click="showLearningPreferences = true" class="w-full bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
                学习偏好
              </button>
              <button @click="showNotificationSettings = true" class="w-full bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">
                通知设置
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 学习建议 -->
      <div class="mt-8 bg-white rounded-lg shadow">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">学习建议</h3>
        </div>
        <div class="p-6">
          <div class="space-y-4">
            <div v-for="suggestion in learningSuggestions" :key="suggestion.id" class="flex items-start space-x-4 p-4 bg-blue-50 rounded-lg border-l-4 border-blue-400">
              <div class="p-2 bg-blue-100 rounded-lg">
                <i :class="suggestion.icon" class="text-blue-600"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-900">{{ suggestion.title }}</p>
                <p class="text-sm text-gray-600 mt-1">{{ suggestion.description }}</p>
                <div class="mt-2">
                  <button class="text-xs bg-blue-600 text-white px-3 py-1 rounded-full hover:bg-blue-700 transition-colors">
                    {{ suggestion.action }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近学习 -->
      <div class="mt-8 bg-white rounded-lg shadow">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">最近学习</h3>
        </div>
        <div class="p-6">
          <div class="space-y-4">
            <div v-for="activity in recentLearning" :key="activity.id" class="flex items-center space-x-4 p-3 bg-gray-50 rounded-lg">
              <div class="p-2 bg-green-100 rounded-lg">
                <i :class="activity.icon" class="text-green-600"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-900">{{ activity.title }}</p>
                <p class="text-sm text-gray-600">{{ activity.description }}</p>
              </div>
              <span class="text-xs text-gray-500">{{ activity.time }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 模态框 -->
    <div v-if="showMyCourses" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium">我的课程</h3>
          <button @click="showMyCourses = false" class="text-gray-400 hover:text-gray-600">
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div class="text-center py-8">
          <i class="fa fa-book-open text-4xl text-gray-400 mb-4"></i>
          <p class="text-gray-600">我的课程功能开发中...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentUser, isStudent, clearLoginState } from '@/utils/auth'

export default {
  name: 'StudentDashboard',
  setup() {
    const router = useRouter()
    const user = ref(null)
    const stats = ref({
      enrolledCourses: 3,
      completedProblems: 45,
      studyHours: 28,
      avgScore: 88
    })

    const learningSuggestions = ref([
      {
        id: 1,
        title: '继续学习Python基础',
        description: '你已完成60%，建议继续完成剩余章节',
        icon: 'fa fa-book',
        action: '继续学习'
      },
      {
        id: 2,
        title: '练习薄弱知识点',
        description: '在循环结构方面需要加强练习',
        icon: 'fa fa-lightbulb',
        action: '开始练习'
      },
      {
        id: 3,
        title: '复习已学内容',
        description: '建议复习变量和数据类型章节',
        icon: 'fa fa-refresh',
        action: '开始复习'
      }
    ])

    const recentLearning = ref([
      {
        id: 1,
        title: '完成Python基础第3章',
        description: '学习了函数定义和调用',
        icon: 'fa fa-check-circle',
        time: '2小时前'
      },
      {
        id: 2,
        title: '提交作业《循环结构》',
        description: '作业已提交，等待批改',
        icon: 'fa fa-upload',
        time: '4小时前'
      },
      {
        id: 3,
        title: '练习题目完成',
        description: '完成了5道循环结构题目',
        icon: 'fa fa-code',
        time: '1天前'
      }
    ])

    const showMyCourses = ref(false)
    const showLearningProgress = ref(false)
    const showStudyPlan = ref(false)
    const showPracticeHistory = ref(false)
    const showWeakPoints = ref(false)
    const showRecommendProblems = ref(false)
    const showAssignmentList = ref(false)
    const showSubmittedAssignments = ref(false)
    const showGrades = ref(false)
    const showProfileSettings = ref(false)
    const showLearningPreferences = ref(false)
    const showNotificationSettings = ref(false)

    onMounted(() => {
      // 检查学生权限
      if (!isStudent()) {
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
      learningSuggestions,
      recentLearning,
      showMyCourses,
      showLearningProgress,
      showStudyPlan,
      showPracticeHistory,
      showWeakPoints,
      showRecommendProblems,
      showAssignmentList,
      showSubmittedAssignments,
      showGrades,
      showProfileSettings,
      showLearningPreferences,
      showNotificationSettings,
      logout
    }
  }
}
</script>

<style scoped>
.student-dashboard {
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
}
</style>

