<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="container mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">我的课程</h1>
        <p class="text-gray-600">查看您已选择的所有课程</p>
      </div>

      <!-- 课程统计 -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center">
            <div class="p-3 bg-blue-100 rounded-full">
              <i class="fa fa-book text-blue-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总课程数</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.totalCourses }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center">
            <div class="p-3 bg-green-100 rounded-full">
              <i class="fa fa-play-circle text-green-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">进行中</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.inProgress }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center">
            <div class="p-3 bg-yellow-100 rounded-full">
              <i class="fa fa-clock text-yellow-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">待开始</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.pending }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center">
            <div class="p-3 bg-purple-100 rounded-full">
              <i class="fa fa-check-circle text-purple-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">已完成</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.completed }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 课程列表 -->
      <div class="bg-white rounded-xl shadow-lg">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-900">课程列表</h2>
        </div>
        
        <div class="p-6">
          <div v-if="loading" class="text-center py-8">
            <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            <p class="mt-2 text-gray-600">加载中...</p>
          </div>
          
          <div v-else-if="courses.length === 0" class="text-center py-8">
            <i class="fa fa-book text-4xl text-gray-400 mb-4"></i>
            <p class="text-gray-600">您还没有选择任何课程</p>
            <router-link to="/courses" class="mt-4 inline-block px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
              去选择课程
            </router-link>
          </div>
          
          <div v-else class="space-y-4">
            <div v-for="course in courses" :key="course.id" class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-4">
                  <img :src="getCourseImage(course.image)" :alt="course.title" class="w-16 h-16 rounded-lg object-cover">
                  <div>
                    <h3 class="text-lg font-semibold text-gray-900">{{ course.title }}</h3>
                    <p class="text-gray-600">{{ course.description }}</p>
                    <div class="flex items-center space-x-4 mt-2 text-sm text-gray-500">
                      <span><i class="fa fa-user mr-1"></i>{{ course.teacherName || '未知教师' }}</span>
                      <span><i class="fa fa-clock mr-1"></i>{{ course.duration || '未知时长' }}</span>
                      <span><i class="fa fa-star mr-1 text-yellow-500"></i>{{ course.rating || '暂无评分' }}</span>
                    </div>
                  </div>
                </div>
                
                <div class="flex items-center space-x-3">
                  <div class="text-right">
                    <div class="text-sm text-gray-600">学习进度</div>
                    <div class="text-lg font-semibold text-blue-600">{{ course.progress || 0 }}%</div>
                  </div>
                  <router-link :to="`/course/${course.id}`" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                    继续学习
                  </router-link>
                </div>
              </div>
              
              <!-- 进度条 -->
              <div class="mt-4">
                <div class="w-full bg-gray-200 rounded-full h-2">
                  <div class="bg-blue-600 h-2 rounded-full transition-all duration-300" :style="{ width: (course.progress || 0) + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyCourses',
  data() {
    return {
      courses: [],
      loading: true,
      courseStats: {
        totalCourses: 0,
        inProgress: 0,
        pending: 0,
        completed: 0
      }
    }
  },
  mounted() {
    this.loadMyCourses();
  },
  methods: {
    async loadMyCourses() {
      try {
        const user = JSON.parse(localStorage.getItem('user') || 'null');
        if (!user || !user.userId) {
          this.$router.push('/auth');
          return;
        }
        
        // 这里应该调用后端API获取用户的课程列表
        // 暂时使用模拟数据
        this.courses = [
          {
            id: 1,
            title: 'Python基础入门',
            description: '从零开始学习Python编程语言',
            image: '/course_images/2169.jpg',
            teacherName: '张老师',
            duration: '20小时',
            rating: 4.8,
            progress: 65
          },
          {
            id: 2,
            title: '数据结构与算法',
            description: '掌握计算机科学的核心概念',
            image: '/course_images/2170.jpg',
            teacherName: '李老师',
            duration: '30小时',
            rating: 4.9,
            progress: 30
          }
        ];
        
        this.calculateStats();
        this.loading = false;
      } catch (error) {
        console.error('加载课程失败:', error);
        this.loading = false;
      }
    },
    
    calculateStats() {
      this.courseStats.totalCourses = this.courses.length;
      this.courseStats.inProgress = this.courses.filter(c => c.progress > 0 && c.progress < 100).length;
      this.courseStats.pending = this.courses.filter(c => c.progress === 0).length;
      this.courseStats.completed = this.courses.filter(c => c.progress === 100).length;
    },
    
    getCourseImage(imagePath) {
      if (!imagePath) return 'https://picsum.photos/200/200';
      if (imagePath.startsWith('/course_images/')) {
        return `http://localhost:8080${imagePath}`;
      }
      return imagePath;
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1200px;
}

.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
