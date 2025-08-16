<template>
  <div class="teacher-dashboard min-h-screen bg-gray-50">
    <!-- 顶部导航 -->
    <div class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <h1 class="text-2xl font-bold text-gray-900">教师工作台</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-gray-600">欢迎，{{ user?.nickname || '教师' }}</span>
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
              <p class="text-sm font-medium text-gray-600">我的课程</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.myCourses }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-green-100 rounded-lg">
              <i class="fa fa-users text-green-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">学生总数</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.totalStudents }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-2 bg-yellow-100 rounded-lg">
              <i class="fa fa-code text-yellow-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">题目数量</p>
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
              <p class="text-sm font-medium text-gray-600">平均成绩</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.avgScore }}%</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 功能模块 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- 课程管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">课程管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showCourseManagement = true" class="w-full bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
                管理我的课程
              </button>
              <button @click="showCreateCourse = true" class="w-full bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">
                创建新课程
              </button>
              <button @click="showCourseAnalytics = true" class="w-full bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors">
                课程数据分析
              </button>
            </div>
          </div>
        </div>

        <!-- 学生管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">学生管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showStudentList = true" class="w-full bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors">
                查看学生列表
              </button>
              <button @click="showStudentProgress = true" class="w-full bg-teal-600 text-white px-4 py-2 rounded-lg hover:bg-teal-700 transition-colors">
                学生进度跟踪
              </button>
              <button @click="showGradeManagement = true" class="w-full bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition-colors">
                成绩管理
              </button>
            </div>
          </div>
        </div>

        <!-- 题目管理 -->
        <div class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">题目管理</h3>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <button @click="showProblemList = true" class="w-full bg-yellow-600 text-white px-4 py-2 rounded-lg hover:bg-yellow-700 transition-colors">
                管理题目库
              </button>
              <button @click="showCreateProblem = true" class="w-full bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors">
                创建新题目
              </button>
              <button @click="showProblemAnalytics = true" class="w-full bg-pink-600 text-white px-4 py-2 rounded-lg hover:bg-pink-700 transition-colors">
                题目难度分析
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
              <button @click="showAssignmentList = true" class="w-full bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors">
                查看作业列表
              </button>
              <button @click="showCreateAssignment = true" class="w-full bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
                布置新作业
              </button>
              <button @click="showAssignmentReview = true" class="w-full bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">
                作业批改
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近活动 -->
      <div class="mt-8 bg-white rounded-lg shadow">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">最近活动</h3>
        </div>
        <div class="p-6">
          <div class="space-y-4">
            <div v-for="activity in recentActivities" :key="activity.id" class="flex items-center space-x-4 p-3 bg-gray-50 rounded-lg">
              <div class="p-2 bg-blue-100 rounded-lg">
                <i :class="activity.icon" class="text-blue-600"></i>
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
    <div v-if="showCourseManagement" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium">课程管理</h3>
          <button @click="showCourseManagement = false" class="text-gray-400 hover:text-gray-600">
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div class="text-center py-8">
          <i class="fa fa-book text-4xl text-gray-400 mb-4"></i>
          <p class="text-gray-600">课程管理功能开发中...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentUser, isTeacher, clearLoginState } from '@/utils/auth'

export default {
  name: 'TeacherDashboard',
  setup() {
    const router = useRouter()
    const user = ref(null)
    const stats = ref({
      myCourses: 5,
      totalStudents: 128,
      totalProblems: 45,
      avgScore: 85
    })

    const recentActivities = ref([
      {
        id: 1,
        title: '新学生加入课程',
        description: '张三加入了《Python基础》课程',
        icon: 'fa fa-user-plus',
        time: '2小时前'
      },
      {
        id: 2,
        title: '作业提交提醒',
        description: '《Python基础》课程有15份作业待批改',
        icon: 'fa fa-file-text',
        time: '4小时前'
      },
      {
        id: 3,
        title: '课程更新',
        description: '更新了《Python基础》第3章内容',
        icon: 'fa fa-edit',
        time: '1天前'
      }
    ])

    const showCourseManagement = ref(false)
    const showCreateCourse = ref(false)
    const showCourseAnalytics = ref(false)
    const showStudentList = ref(false)
    const showStudentProgress = ref(false)
    const showGradeManagement = ref(false)
    const showProblemList = ref(false)
    const showCreateProblem = ref(false)
    const showProblemAnalytics = ref(false)
    const showAssignmentList = ref(false)
    const showCreateAssignment = ref(false)
    const showAssignmentReview = ref(false)

    onMounted(() => {
      // 检查教师权限
      if (!isTeacher()) {
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
      recentActivities,
      showCourseManagement,
      showCreateCourse,
      showCourseAnalytics,
      showStudentList,
      showStudentProgress,
      showGradeManagement,
      showProblemList,
      showCreateProblem,
      showProblemAnalytics,
      showAssignmentList,
      showCreateAssignment,
      showAssignmentReview,
      logout
    }
  }
}
</script>

<style scoped>
.teacher-dashboard {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
}
</style>

