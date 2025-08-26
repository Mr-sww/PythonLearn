<template>
  <div class="container mx-auto p-6">
    <!-- 页面标题 -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">我的课程</h1>
      <p class="text-gray-600">管理您创建的所有课程</p>
    </div>

    <!-- 课程统计卡片 -->
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
            <p class="text-sm font-medium text-gray-600">已审核课程</p>
            <p class="text-2xl font-bold text-gray-900">{{ courseStats.approvedCourses }}</p>
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-xl shadow-lg p-6">
        <div class="flex items-center">
          <div class="p-3 bg-yellow-100 rounded-full">
            <i class="fa fa-clock text-yellow-600 text-xl"></i>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">待审核课程</p>
            <p class="text-2xl font-bold text-gray-900">{{ courseStats.pendingCourses }}</p>
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
    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <div class="flex flex-col md:flex-row gap-4 items-center justify-between">
        <div class="flex flex-col md:flex-row gap-4 items-center">
          <input 
            v-model="keyword" 
            placeholder="搜索课程名称" 
            class="border rounded-lg px-4 py-2 w-64 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" 
          />
          <button 
            @click="testAuth" 
            class="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors flex items-center"
          >
            <i class="fa fa-key mr-2"></i>测试认证
          </button>
        </div>
        <div class="flex gap-3">
          <button 
            @click="refreshCourses" 
            class="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition-colors flex items-center"
          >
            <i class="fa fa-refresh mr-2"></i>刷新
          </button>
          <button 
            v-if="isTeacher" 
            @click="openCreateModal" 
            class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center"
          >
            <i class="fa fa-plus mr-2"></i>创建课程
          </button>
        </div>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-12">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      <p class="mt-4 text-gray-600 text-lg">加载中...</p>
    </div>
    
    <!-- 空状态 -->
    <div v-else-if="courses.length === 0" class="text-center py-16 bg-white rounded-lg shadow-sm">
      <div class="w-24 h-24 mx-auto bg-gradient-to-br from-blue-100 to-purple-100 rounded-full flex items-center justify-center mb-6">
        <i class="fa fa-book text-4xl text-blue-400"></i>
      </div>
      <h3 class="text-xl font-semibold text-gray-700 mb-2">还没有课程</h3>
      <p v-if="isTeacher" class="text-gray-500 mb-6">点击上方"创建课程"按钮开始创建您的第一个课程</p>
      <p v-else class="text-gray-500 mb-6">您还没有加入任何课程</p>
      <div v-if="isTeacher" class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors cursor-pointer" @click="openCreateModal">
        <i class="fa fa-plus mr-2"></i>
        创建第一个课程
      </div>
    </div>
    
    <!-- 搜索无结果 -->
    <div v-else-if="keyword && paged.length === 0" class="text-center py-16 bg-white rounded-lg shadow-sm">
      <div class="w-24 h-24 mx-auto bg-gradient-to-br from-gray-100 to-gray-200 rounded-full flex items-center justify-center mb-6">
        <i class="fa fa-search text-4xl text-gray-400"></i>
      </div>
      <h3 class="text-xl font-semibold text-gray-700 mb-2">搜索无结果</h3>
      <p class="text-gray-500 mb-6">没有找到包含"{{ keyword }}"的课程</p>
      <button @click="keyword = ''" class="inline-flex items-center px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors">
        <i class="fa fa-times mr-2"></i>
        清除搜索
      </button>
    </div>
    
    <!-- 课程列表 -->
    <div v-else class="space-y-6">
      <div v-for="c in paged" :key="c.articleId" class="bg-white rounded-lg border shadow-sm hover:shadow-md transition-shadow overflow-hidden">
        <div class="flex">
          <!-- 左侧：课程封面或图标 -->
          <div class="w-24 h-24 flex-shrink-0">
            <img 
              v-if="c.coverImage" 
              :src="getCoverImageUrl(c.coverImage)" 
              :alt="c.title"
              class="w-full h-full object-cover rounded-l-lg"
              @error="handleImageError"
            />
            <div v-else class="w-full h-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center text-white text-2xl font-bold rounded-l-lg">
              <i class="fa fa-book"></i>
            </div>
          </div>
          
          <!-- 右侧：课程信息和操作 -->
          <div class="flex-1 p-6">
            <div class="flex items-start justify-between">
              <div class="flex-1">
                <!-- 课程标题和描述 -->
                <h3 class="text-xl font-bold text-gray-900 mb-2">{{ c.title }}</h3>
                <p class="text-gray-600 text-sm mb-4">{{ c.content || '暂无描述' }}</p>
                
                <!-- 统计信息 -->
                <div class="flex items-center gap-6 text-sm text-gray-500 mb-3">
                  <span class="flex items-center">
                    <i class="fa fa-eye mr-2 text-blue-500"></i>
                    {{ c.views || 0 }} 次浏览
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-star mr-2 text-yellow-500"></i>
                    {{ c.rating || 0.0 }} 分
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-tag mr-2 text-green-500"></i>
                    {{ c.category || '未分类' }}
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-clock mr-2 text-purple-500"></i>
                    {{ c.duration || '未知时长' }}
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-list mr-2 text-orange-500"></i>
                    {{ c.lessons || 0 }} 课时
                  </span>
                </div>
                
                <!-- 标签 -->
                <div v-if="c.tags" class="mb-3">
                  <span class="inline-block bg-blue-100 text-blue-600 text-xs px-2 py-1 rounded-full mr-2">{{ c.tags }}</span>
                </div>
                
                <!-- 创建时间和状态 -->
                <div class="flex items-center justify-between">
                  <div class="text-xs text-gray-400">
                    创建时间：{{ formatDate(c.createdAt || c.publicationDate) }}
                  </div>
                  <div class="text-xs">
                    <span class="px-2 py-1 rounded-full" :class="getStatusClass(c.status)">
                      {{ getStatusText(c.status) }}
                    </span>
                  </div>
                </div>
              </div>
              
              <!-- 右侧操作按钮 -->
              <div v-if="isTeacher" class="flex flex-col gap-2 ml-6">
                <button @click="editCourse(c)" class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors flex items-center">
                  <i class="fa fa-edit mr-1"></i>编辑
                </button>
                <button @click="viewCourse(c)" class="px-4 py-2 bg-green-600 text-white text-sm rounded-lg hover:bg-green-700 transition-colors flex items-center">
                  <i class="fa fa-eye mr-1"></i>查看
                </button>
                <button @click="deleteCourse(c.articleId)" class="px-4 py-2 bg-red-600 text-white text-sm rounded-lg hover:bg-red-700 transition-colors flex items-center">
                  <i class="fa fa-trash mr-1"></i>删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div v-if="totalPages > 1" class="flex justify-center items-center gap-4 mt-8">
      <button 
        :disabled="page === 1" 
        @click="page--" 
        class="px-4 py-2 border rounded-lg disabled:opacity-50 hover:bg-gray-50 transition-colors disabled:cursor-not-allowed"
      >
        上一页
      </button>
      <span class="text-sm text-gray-600">{{ page }} / {{ totalPages }}</span>
      <button 
        :disabled="page === totalPages" 
        @click="page++" 
        class="px-4 py-2 border rounded-lg disabled:opacity-50 hover:bg-gray-50 transition-colors disabled:cursor-not-allowed"
      >
        下一页
      </button>
    </div>
  </div>

  <!-- 创建/编辑课程模态框 -->
  <div v-if="showCreateModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
    <div class="bg-white rounded-xl shadow-2xl w-full max-w-md mx-4 max-h-[90vh] overflow-y-auto">
      <div class="px-6 py-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-900">{{ isEditing ? '编辑课程' : '创建课程' }}</h3>
      </div>
      
      <form @submit.prevent="submitCourse" class="p-6 space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程标题 *</label>
          <input 
            v-model="form.title" 
            type="text" 
            required
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="请输入课程标题"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程内容</label>
          <textarea 
            v-model="form.content" 
            rows="3"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
            placeholder="请输入课程内容描述（可选）"
          ></textarea>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程分类</label>
          <select 
            v-model="form.category" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="编程开发">编程开发</option>
            <option value="数据科学">数据科学</option>
            <option value="人工智能">人工智能</option>
            <option value="Web开发">Web开发</option>
            <option value="移动开发">移动开发</option>
            <option value="其他">其他</option>
          </select>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程难度</label>
          <select 
            v-model="form.difficulty" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="beginner">入门</option>
            <option value="intermediate">中级</option>
            <option value="advanced">高级</option>
          </select>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程时长</label>
          <input 
            v-model="form.duration" 
            type="text" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="例如：20小时"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课时数量</label>
          <input 
            v-model="form.lessons" 
            type="number" 
            min="1"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="例如：10"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程标签</label>
          <input 
            v-model="form.tags" 
            type="text" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="例如：Python, 编程, 入门"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程封面</label>
          <div class="flex items-center space-x-4">
            <input 
              v-model="form.coverImage" 
              type="text" 
              class="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="请输入封面图片文件名，如：python_basic.jpg"
            />
            <button 
              type="button"
              @click="uploadCoverImage"
              class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
            >
              上传
            </button>
          </div>
          <p class="text-xs text-gray-500 mt-1">图片将保存到 /course/ 目录下</p>
        </div>
        
        <div class="flex gap-3 pt-4">
          <button 
            type="button"
            @click="showCreateModal = false" 
            class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
          >
            取消
          </button>
          <button 
            type="submit" 
            class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            {{ isEditing ? '更新课程' : '创建课程' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TeacherClasses',
  data () {
    return { 
      courses: [],
      loading: false,
      keyword: '',
      page: 1,
      pageSize: 10,
      showCreateModal: false,
      isEditing: false,
      editingCourseId: null,
      isTeacher: false,
      courseStats: {
        totalCourses: 0,
        approvedCourses: 0,
        pendingCourses: 0,
        totalViews: 0
      },
      form: { 
        title: '', 
        content: '', 
        category: '编程开发', 
        difficulty: 'beginner',
        duration: '10小时',
        lessons: 10,
        tags: '',
        coverImage: '' // Added for cover image
      }
    }
  },
  
  computed: {
    filtered() {
      if (!this.keyword) return this.courses
      const keyword = this.keyword.toLowerCase()
      return this.courses.filter(c => 
        c.title.toLowerCase().includes(keyword) || 
        c.content.toLowerCase().includes(keyword) ||
        c.category.toLowerCase().includes(keyword)
      )
    },
    
    paged() {
      const start = (this.page - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filtered.slice(start, end)
    },
    
    totalPages() { 
      return Math.max(1, Math.ceil(this.filtered.length / this.pageSize)) 
    }
  },
  
  mounted() {
    this.checkUserRole()
    this.fetch()
  },
  
  methods: {
    checkUserRole() {
      // 检查用户是否为教师
      this.isTeacher = true // 简化处理，实际应该从后端获取
    },
    
    async fetch() {
      this.loading = true
      try {
        const response = await axios.get('http://localhost:8080/api/teacher/courses', { withCredentials: true })
        this.courses = response.data || []
        await this.fetchCourseStatistics()
      } catch (err) {
        console.error('Failed to fetch courses:', err)
        this.courses = []
      } finally {
        this.loading = false
      }
    },

    async fetchCourseStatistics() {
      try {
        const response = await axios.get('http://localhost:8080/api/teacher/courses/statistics', { withCredentials: true })
        if (response.data) {
          this.courseStats = {
            totalCourses: response.data.totalCourses || 0,
            approvedCourses: response.data.approvedCourses || 0,
            pendingCourses: response.data.pendingCourses || 0,
            totalViews: response.data.totalViews || 0
          }
        }
      } catch (err) {
        console.error('Failed to fetch course statistics:', err)
        this.calculateStats() // Fallback to local calculation
      }
    },

    calculateStats() {
      this.courseStats.totalCourses = this.courses.length
      this.courseStats.approvedCourses = this.courses.filter(c => c.status === 'approved').length
      this.courseStats.pendingCourses = this.courses.filter(c => c.status === 'pending').length
      this.courseStats.totalViews = this.courses.reduce((sum, course) => sum + (course.views || 0), 0)
    },
    
    testAuth() {
      // 测试认证状态
      alert('认证测试功能')
    },
    
    openCreateModal() {
      this.isEditing = false
      this.editingCourseId = null
      this.resetForm()
      this.showCreateModal = true
    },
    
    editCourse(course) {
      this.isEditing = true
      this.editingCourseId = course.articleId
      this.populateForm(course)
      this.showCreateModal = true
    },
    
    populateForm(courseData) {
      this.form = {
        title: courseData.title || '',
        content: courseData.content || '',
        category: courseData.category || '编程开发',
        difficulty: courseData.difficulty || 'beginner',
        duration: courseData.duration || '10小时',
        lessons: courseData.lessons || 10,
        tags: courseData.tags || '',
        coverImage: courseData.coverImage || '' // Populate cover image
      }
    },
    
    resetForm() {
      this.form = {
        title: '',
        content: '',
        category: '编程开发',
        difficulty: 'beginner',
        duration: '10小时',
        lessons: 10,
        tags: '',
        coverImage: '' // Reset cover image
      }
    },
    
    async submitCourse() {
      try {
        const submitData = {
          title: this.form.title,
          content: this.form.content,
          category: this.form.category,
          difficulty: this.form.difficulty,
          duration: this.form.duration,
          lessons: this.form.lessons,
          tags: this.form.tags || '',
          coverImage: this.form.coverImage || '' // Include cover image
        }
        
        if (this.isEditing) {
          // 更新课程
          await axios.put(`http://localhost:8080/api/teacher/courses/${this.editingCourseId}`, submitData, { withCredentials: true })
          alert('课程更新成功！')
        } else {
          // 创建课程
          const response = await axios.post('http://localhost:8080/api/teacher/courses', submitData, { withCredentials: true })
          alert(response.data.message || '课程创建成功！')
        }
        
        this.showCreateModal = false
        this.fetch()
      } catch (err) {
        alert(err.response?.data || (this.isEditing ? '更新失败' : '创建失败'))
      }
    },
    
    async deleteCourse(courseId) {
      if (!confirm('确定要删除这个课程吗？')) return
      
      try {
        await axios.delete(`http://localhost:8080/api/teacher/courses/${courseId}`, { withCredentials: true })
        alert('课程删除成功！')
        this.fetch()
      } catch (err) {
        alert(err.response?.data || '删除失败')
      }
    },
    
    formatDate(dateString) {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    },
    
    getStatusText(status) {
      const statusMap = {
        'pending': '待审核',
        'approved': '已通过',
        'rejected': '已拒绝',
        'draft': '草稿',
        'active': '已发布'
      }
      return statusMap[status] || '待审核'
    },

    getStatusClass(status) {
      const statusClassMap = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'approved': 'bg-green-100 text-green-800',
        'rejected': 'bg-red-100 text-red-800',
        'draft': 'bg-gray-100 text-gray-800',
        'active': 'bg-blue-100 text-blue-800'
      }
      return statusClassMap[status] || 'bg-gray-100 text-gray-800'
    },

    getCoverImageUrl(coverImage) {
      if (!coverImage) return ''
      if (coverImage.startsWith('http')) return coverImage
      return `http://localhost:8080${coverImage}`
    },

    handleImageError(event) {
      // 图片加载失败时显示默认图标
      event.target.style.display = 'none'
      event.target.nextElementSibling.style.display = 'flex'
    },

    uploadCoverImage() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = 'image/*'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (file) {
          const formData = new FormData()
          formData.append('file', file)
          axios.post('http://localhost:8080/api/upload', formData, { withCredentials: true })
            .then(res => {
              this.form.coverImage = res.data.filename
              alert('封面图片上传成功！')
            })
            .catch(err => {
              console.error('封面图片上传失败:', err)
              alert('封面图片上传失败！')
            })
        }
      }
      input.click()
    },

    viewCourse(course) {
      // 跳转到课程详情页面
      this.$router.push(`/course/${course.articleId}`)
    },

    refreshCourses() {
      this.fetch()
    }
  }
}
</script>
