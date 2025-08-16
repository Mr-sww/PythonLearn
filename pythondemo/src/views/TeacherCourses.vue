<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="container mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">我的课程</h1>
        <p class="text-gray-600">管理您创建的所有课程</p>
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
              <i class="fa fa-users text-green-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总学生数</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.totalStudents }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center">
            <div class="p-3 bg-yellow-100 rounded-full">
              <i class="fa fa-star text-yellow-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">平均评分</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.averageRating }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center">
            <div class="p-3 bg-purple-100 rounded-full">
              <i class="fa fa-eye text-purple-600 text-xl"></i>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总浏览量</p>
              <p class="text-2xl font-bold text-gray-900">{{ courseStats.totalViews }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 操作栏 -->
      <div class="bg-white rounded-xl shadow-lg p-6 mb-8">
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center space-y-4 sm:space-y-0">
          <div>
            <h2 class="text-lg font-semibold text-gray-900">课程管理</h2>
            <p class="text-gray-600">创建新课程或管理现有课程</p>
          </div>
          <div class="flex space-x-3">
            <button @click="showCreateModal = true" class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center">
              <i class="fa fa-plus mr-2"></i>创建课程
            </button>
            <button @click="refreshCourses" class="px-6 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors flex items-center">
              <i class="fa fa-refresh mr-2"></i>刷新
            </button>
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
            <p class="text-gray-600">您还没有创建任何课程</p>
            <button @click="showCreateModal = true" class="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
              创建第一个课程
            </button>
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
                      <span><i class="fa fa-users mr-1"></i>{{ course.studentCount || 0 }} 名学生</span>
                      <span><i class="fa fa-star mr-1 text-yellow-500"></i>{{ course.rating || '暂无评分' }}</span>
                      <span><i class="fa fa-eye mr-1"></i>{{ course.views || 0 }} 次浏览</span>
                      <span><i class="fa fa-calendar mr-1"></i>{{ course.createTime || '未知时间' }}</span>
                    </div>
                  </div>
                </div>
                
                <div class="flex items-center space-x-3">
                  <div class="text-right">
                    <div class="text-sm text-gray-600">状态</div>
                    <div class="text-sm font-medium" :class="getStatusClass(course.status)">
                      {{ getStatusText(course.status) }}
                    </div>
                  </div>
                  <div class="flex space-x-2">
                    <button @click="editCourse(course)" class="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm">
                      编辑
                    </button>
                    <button @click="viewCourse(course)" class="px-3 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors text-sm">
                      查看
                    </button>
                    <button @click="deleteCourse(course.id)" class="px-3 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors text-sm">
                      删除
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 创建课程弹窗 -->
    <div v-if="showCreateModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div class="bg-white rounded-xl shadow-2xl p-8 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-xl font-bold text-gray-900">创建新课程</h3>
          <button @click="showCreateModal = false" class="text-gray-400 hover:text-gray-600">
            <i class="fa fa-times text-xl"></i>
          </button>
        </div>
        
        <form @submit.prevent="createCourse" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">课程标题</label>
            <input v-model="newCourse.title" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">课程描述</label>
            <textarea v-model="newCourse.description" rows="3" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">课程分类</label>
              <select v-model="newCourse.category" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="">请选择分类</option>
                <option value="programming">编程开发</option>
                <option value="data-science">数据科学</option>
                <option value="web-development">Web开发</option>
                <option value="mobile-development">移动开发</option>
                <option value="ai-ml">人工智能与机器学习</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">难度等级</label>
              <select v-model="newCourse.difficulty" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="">请选择难度</option>
                <option value="beginner">初级</option>
                <option value="intermediate">中级</option>
                <option value="advanced">高级</option>
              </select>
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 pt-4">
            <button type="button" @click="showCreateModal = false" class="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors">
              取消
            </button>
            <button type="submit" class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
              创建课程
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TeacherCourses',
  data() {
    return {
      courses: [],
      loading: true,
      showCreateModal: false,
      newCourse: {
        title: '',
        description: '',
        category: '',
        difficulty: ''
      },
      courseStats: {
        totalCourses: 0,
        totalStudents: 0,
        averageRating: 0,
        totalViews: 0
      }
    }
  },
  mounted() {
    this.loadTeacherCourses();
  },
  methods: {
    async loadTeacherCourses() {
      try {
        const user = JSON.parse(localStorage.getItem('user') || 'null');
        if (!user || !user.userId) {
          this.$router.push('/auth');
          return;
        }
        
        // 这里应该调用后端API获取教师的课程列表
        // 暂时使用模拟数据
        this.courses = [
          {
            id: 1,
            title: 'Python基础入门',
            description: '从零开始学习Python编程语言',
            image: '/course_images/2169.jpg',
            studentCount: 45,
            rating: 4.8,
            views: 1200,
            createTime: '2024-01-15',
            status: 'active'
          },
          {
            id: 2,
            title: '数据结构与算法',
            description: '掌握计算机科学的核心概念',
            image: '/course_images/2170.jpg',
            studentCount: 32,
            rating: 4.9,
            views: 980,
            createTime: '2024-02-01',
            status: 'active'
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
      this.courseStats.totalStudents = this.courses.reduce((sum, course) => sum + (course.studentCount || 0), 0);
      this.courseStats.averageRating = this.courses.length > 0 
        ? (this.courses.reduce((sum, course) => sum + (course.rating || 0), 0) / this.courses.length).toFixed(1)
        : 0;
      this.courseStats.totalViews = this.courses.reduce((sum, course) => sum + (course.views || 0), 0);
    },
    
    getCourseImage(imagePath) {
      if (!imagePath) return 'https://picsum.photos/200/200';
      if (imagePath.startsWith('/course_images/')) {
        return `http://localhost:8080${imagePath}`;
      }
      return imagePath;
    },
    
    getStatusClass(status) {
      switch (status) {
        case 'active': return 'text-green-600';
        case 'draft': return 'text-yellow-600';
        case 'archived': return 'text-gray-600';
        default: return 'text-gray-600';
      }
    },
    
    getStatusText(status) {
      switch (status) {
        case 'active': return '已发布';
        case 'draft': return '草稿';
        case 'archived': return '已归档';
        default: return '未知';
      }
    },
    
    refreshCourses() {
      this.loadTeacherCourses();
    },
    
    editCourse(course) {
      // 实现编辑课程功能
      console.log('编辑课程:', course);
    },
    
    viewCourse(course) {
      this.$router.push(`/course/${course.id}`);
    },
    
    async deleteCourse(courseId) {
      if (confirm('确定要删除这个课程吗？此操作不可恢复。')) {
        try {
          // 这里应该调用后端API删除课程
          console.log('删除课程:', courseId);
          this.courses = this.courses.filter(c => c.id !== courseId);
          this.calculateStats();
        } catch (error) {
          console.error('删除课程失败:', error);
        }
      }
    },
    
    async createCourse() {
      try {
        // 这里应该调用后端API创建课程
        const course = {
          id: Date.now(),
          title: this.newCourse.title,
          description: this.newCourse.description,
          category: this.newCourse.category,
          difficulty: this.newCourse.difficulty,
          image: '/course_images/2169.jpg',
          studentCount: 0,
          rating: 0,
          views: 0,
          createTime: new Date().toISOString().split('T')[0],
          status: 'draft'
        };
        
        this.courses.unshift(course);
        this.calculateStats();
        this.showCreateModal = false;
        this.newCourse = { title: '', description: '', category: '', difficulty: '' };
      } catch (error) {
        console.error('创建课程失败:', error);
      }
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
