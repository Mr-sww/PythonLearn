<template>
  <div class="container mx-auto p-6">
    <!-- 页面标题 -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">课程审核管理</h1>
      <p class="text-gray-600">管理待审核的课程申请和已通过的课程</p>
    </div>

    <!-- 统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="bg-white rounded-xl shadow-lg p-6">
        <div class="flex items-center">
          <div class="p-3 bg-yellow-100 rounded-full">
            <i class="fa fa-clock text-yellow-600 text-xl"></i>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">待审核</p>
            <p class="text-2xl font-bold text-yellow-600">{{ stats.pending }}</p>
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-xl shadow-lg p-6">
        <div class="flex items-center">
          <div class="p-3 bg-green-100 rounded-full">
            <i class="fa fa-check text-green-600 text-xl"></i>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">已通过</p>
            <p class="text-2xl font-bold text-green-600">{{ stats.approved }}</p>
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-xl shadow-lg p-6">
        <div class="flex items-center">
          <div class="p-3 bg-red-100 rounded-full">
            <i class="fa fa-times text-red-600 text-xl"></i>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">已拒绝</p>
            <p class="text-2xl font-bold text-red-600">{{ stats.rejected }}</p>
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-xl shadow-lg p-6">
        <div class="flex items-center">
          <div class="p-3 bg-blue-100 rounded-full">
            <i class="fa fa-book text-blue-600 text-xl"></i>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">总课程</p>
            <p class="text-2xl font-bold text-blue-600">{{ stats.total }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能切换标签 -->
    <div class="bg-white rounded-lg shadow-sm mb-6">
      <div class="border-b border-gray-200">
        <nav class="-mb-px flex space-x-8 px-6">
          <button 
            @click="activeTab = 'review'" 
            :class="[
              'py-4 px-1 border-b-2 font-medium text-sm',
              activeTab === 'review' 
                ? 'border-blue-500 text-blue-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
          >
            课程审核
          </button>
          <button 
            @click="activeTab = 'management'" 
            :class="[
              'py-4 px-1 border-b-2 font-medium text-sm',
              activeTab === 'management' 
                ? 'border-blue-500 text-blue-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
          >
            课程管理
          </button>
        </nav>
      </div>
    </div>

    <!-- 课程审核标签页 -->
    <div v-if="activeTab === 'review'">
      <!-- 筛选和搜索栏 -->
      <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
        <div class="flex flex-col md:flex-row gap-4 items-center justify-between">
          <div class="flex flex-col md:flex-row gap-4 items-center">
            <select 
              v-model="filterStatus" 
              class="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="pending">待审核</option>
              <option value="approved">已通过</option>
              <option value="rejected">已拒绝</option>
            </select>
            
            <input 
              v-model="searchKeyword" 
              placeholder="搜索课程标题或教师姓名" 
              class="border rounded-lg px-4 py-2 w-64 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" 
            />
            
            <button 
              @click="refreshData" 
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center"
            >
              <i class="fa fa-refresh mr-2"></i>刷新
            </button>
          </div>
          
          <div class="flex gap-2">
            <button 
              v-if="filterStatus === 'pending'"
              @click="showBatchReviewModal = true" 
              class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center"
            >
              <i class="fa fa-check-double mr-2"></i>批量审核
            </button>
            <button 
              @click="exportData" 
              class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center"
            >
              <i class="fa fa-download mr-2"></i>导出数据
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
      <div v-else-if="filteredCourses.length === 0" class="text-center py-16 bg-white rounded-lg shadow-sm">
        <div class="w-24 h-24 mx-auto bg-gradient-to-br from-gray-100 to-gray-200 rounded-full flex items-center justify-center mb-6">
          <i class="fa fa-search text-4xl text-gray-400"></i>
        </div>
        <h3 class="text-xl font-semibold text-gray-700 mb-2">暂无课程</h3>
        <p class="text-gray-500 mb-6">{{ getEmptyMessage() }}</p>
      </div>

      <!-- 课程列表 -->
      <div v-else class="space-y-6">
        <div v-for="course in pagedCourses" :key="course.articleId" class="bg-white rounded-lg border shadow-sm hover:shadow-md transition-shadow overflow-hidden">
          <div class="p-6">
            <!-- 课程头部信息 -->
            <div class="flex items-start justify-between mb-4">
              <div class="flex-1">
                <div class="flex items-center gap-3 mb-2">
                  <h3 class="text-xl font-bold text-gray-900">{{ course.title }}</h3>
                  <span class="px-2 py-1 rounded-full text-xs font-medium" :class="getStatusClass(course.status)">
                    {{ getStatusText(course.status) }}
                  </span>
                </div>
                <p class="text-gray-600 text-sm mb-2">{{ course.content || '暂无描述' }}</p>
                <div class="flex items-center gap-4 text-sm text-gray-500">
                  <span class="flex items-center">
                    <i class="fa fa-user mr-2 text-blue-500"></i>
                    教师：{{ course.author }}
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-tag mr-2 text-green-500"></i>
                    {{ course.category || '未分类' }}
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-clock mr-2 text-purple-500"></i>
                    {{ course.duration || '未知时长' }}
                  </span>
                  <span class="flex items-center">
                    <i class="fa fa-list mr-2 text-orange-500"></i>
                    {{ course.lessons || 0 }} 课时
                  </span>
                </div>
              </div>
              
              <!-- 右侧操作按钮 -->
              <div class="flex flex-col gap-2 ml-6">
                <button 
                  @click="viewCourse(course)" 
                  class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors flex items-center"
                >
                  <i class="fa fa-eye mr-1"></i>查看详情
                </button>
                
                <button 
                  v-if="course.status === 'pending'"
                  @click="approveCourse(course)" 
                  class="px-4 py-2 bg-green-600 text-white text-sm rounded-lg hover:bg-green-700 transition-colors flex items-center"
                >
                  <i class="fa fa-check mr-1"></i>通过
                </button>
                
                <button 
                  v-if="course.status === 'pending'"
                  @click="rejectCourse(course)" 
                  class="px-4 py-2 bg-red-600 text-white text-sm rounded-lg hover:bg-red-700 transition-colors flex items-center"
                >
                  <i class="fa fa-times mr-1"></i>拒绝
                </button>
                
                <button 
                  @click="editCourse(course)" 
                  class="px-4 py-2 bg-yellow-600 text-white text-sm rounded-lg hover:bg-yellow-700 transition-colors flex items-center"
                >
                  <i class="fa fa-edit mr-1"></i>编辑
                </button>
              </div>
            </div>

            <!-- 课程详细信息 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <div>
                <h4 class="font-medium text-gray-700 mb-2">课程信息</h4>
                <div class="space-y-1 text-sm text-gray-600">
                  <p><span class="font-medium">难度：</span>{{ getDifficultyText(course.difficulty) }}</p>
                  <p><span class="font-medium">标签：</span>{{ course.tags || '无' }}</p>
                  <p><span class="font-medium">创建时间：</span>{{ formatDate(course.createdAt) }}</p>
                  <p><span class="font-medium">更新时间：</span>{{ formatDate(course.updatedAt) }}</p>
                </div>
              </div>
              
              <div>
                <h4 class="font-medium text-gray-700 mb-2">审核信息</h4>
                <div class="space-y-1 text-sm text-gray-600">
                  <p><span class="font-medium">审核状态：</span>{{ getStatusText(course.status) }}</p>
                  <p v-if="course.reviewComment"><span class="font-medium">审核意见：</span>{{ course.reviewComment }}</p>
                  <p v-if="course.reviewedAt"><span class="font-medium">审核时间：</span>{{ formatDate(course.reviewedAt) }}</p>
                  <p v-if="course.reviewedBy"><span class="font-medium">审核人：</span>{{ getReviewerName(course.reviewedBy) }}</p>
                </div>
              </div>
            </div>

            <!-- 封面图片 -->
            <div v-if="course.coverImage" class="mb-4">
              <h4 class="font-medium text-gray-700 mb-2">课程封面</h4>
              <img 
                :src="getCoverImageUrl(course.coverImage)" 
                :alt="course.title"
                class="w-32 h-24 object-cover rounded-lg border"
                @error="handleImageError"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 课程管理标签页 -->
    <div v-else-if="activeTab === 'management'">
      <!-- 筛选和搜索栏 -->
      <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
        <div class="flex flex-col md:flex-row gap-4 items-center justify-between">
          <div class="flex flex-col md:flex-row gap-4 items-center">
            <select 
              v-model="managementFilterStatus" 
              class="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="approved">已通过</option>
              <option value="active">已发布</option>
              <option value="draft">草稿</option>
            </select>
            
            <input 
              v-model="managementSearchKeyword" 
              placeholder="搜索课程标题或教师姓名" 
              class="border rounded-lg px-4 py-2 w-64 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" 
            />
            
            <button 
              @click="refreshManagementData" 
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center"
            >
              <i class="fa fa-refresh mr-2"></i>刷新
            </button>
          </div>
          
          <div class="flex gap-2">
            <button 
              @click="showBatchDeleteModal = true" 
              class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors flex items-center"
            >
              <i class="fa fa-trash mr-2"></i>批量删除
            </button>
            <button 
              @click="exportManagementData" 
              class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center"
            >
              <i class="fa fa-download mr-2"></i>导出数据
            </button>
          </div>
        </div>
      </div>

      <!-- 课程管理列表 -->
      <div class="space-y-6">
        <div v-for="course in managementCourses" :key="course.articleId" class="bg-white rounded-lg border shadow-sm hover:shadow-md transition-shadow overflow-hidden">
          <div class="p-6">
            <div class="flex items-start justify-between">
              <div class="flex-1">
                <h3 class="text-xl font-bold text-gray-900 mb-2">{{ course.title }}</h3>
                <p class="text-gray-600 text-sm mb-2">{{ course.content || '暂无描述' }}</p>
                <div class="flex items-center gap-4 text-sm text-gray-500">
                  <span>教师：{{ course.author }}</span>
                  <span>分类：{{ course.category || '未分类' }}</span>
                  <span>状态：{{ getStatusText(course.status) }}</span>
                </div>
              </div>
              
              <div class="flex flex-col gap-2 ml-6">
                <button 
                  @click="viewCourse(course)" 
                  class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
                >
                  查看详情
                </button>
                <button 
                  @click="editCourse(course)" 
                  class="px-4 py-2 bg-yellow-600 text-white text-sm rounded-lg hover:bg-yellow-700 transition-colors"
                >
                  编辑
                </button>
                <button 
                  @click="deleteCourse(course)" 
                  class="px-4 py-2 bg-red-600 text-white text-sm rounded-lg hover:bg-red-700 transition-colors"
                >
                  删除
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
        :disabled="currentPage === 1" 
        @click="currentPage--" 
        class="px-4 py-2 border rounded-lg disabled:opacity-50 hover:bg-gray-50 transition-colors disabled:cursor-not-allowed"
      >
        上一页
      </button>
      <span class="text-sm text-gray-600">{{ currentPage }} / {{ totalPages }}</span>
      <button 
        :disabled="currentPage === totalPages" 
        @click="currentPage++" 
        class="px-4 py-2 border rounded-lg disabled:opacity-50 hover:bg-gray-50 transition-colors disabled:cursor-not-allowed"
      >
        下一页
      </button>
    </div>
  </div>

  <!-- 审核操作模态框 -->
  <div v-if="showReviewModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
    <div class="bg-white rounded-xl shadow-2xl w-full max-w-md mx-4">
      <div class="px-6 py-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-900">{{ reviewAction === 'approve' ? '通过课程' : '拒绝课程' }}</h3>
      </div>
      
      <div class="p-6">
        <div class="mb-4">
          <h4 class="font-medium text-gray-700 mb-2">课程信息</h4>
          <p class="text-sm text-gray-600">{{ selectedCourse?.title }}</p>
          <p class="text-sm text-gray-600">教师：{{ selectedCourse?.author }}</p>
        </div>
        
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">审核意见</label>
          <textarea 
            v-model="reviewComment" 
            rows="3"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
            :placeholder="reviewAction === 'approve' ? '请输入通过意见（可选）' : '请输入拒绝原因'"
          ></textarea>
        </div>
        
        <div class="flex gap-3">
          <button 
            @click="showReviewModal = false" 
            class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
          >
            取消
          </button>
          <button 
            @click="submitReview" 
            :class="[
              'flex-1 px-4 py-2 text-white rounded-lg transition-colors',
              reviewAction === 'approve' 
                ? 'bg-green-600 hover:bg-green-700' 
                : 'bg-red-600 hover:bg-red-700'
            ]"
          >
            {{ reviewAction === 'approve' ? '确认通过' : '确认拒绝' }}
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- 批量审核模态框 -->
  <div v-if="showBatchReviewModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
    <div class="bg-white rounded-xl shadow-2xl w-full max-w-md mx-4">
      <div class="px-6 py-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-900">批量审核课程</h3>
      </div>
      
      <div class="p-6">
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">审核操作</label>
          <select 
            v-model="batchReviewAction" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="approve">通过</option>
            <option value="reject">拒绝</option>
          </select>
        </div>
        
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">审核意见</label>
          <textarea 
            v-model="batchReviewComment" 
            rows="3"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
            :placeholder="batchReviewAction === 'approve' ? '请输入通过意见（可选）' : '请输入拒绝原因'"
          ></textarea>
        </div>
        
        <div class="flex gap-3">
          <button 
            @click="showBatchReviewModal = false" 
            class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
          >
            取消
          </button>
          <button 
            @click="submitBatchReview" 
            :class="[
              'flex-1 px-4 py-2 text-white rounded-lg transition-colors',
              batchReviewAction === 'approve' 
                ? 'bg-green-600 hover:bg-green-700' 
                : 'bg-red-600 hover:bg-red-700'
            ]"
          >
            确认批量审核
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- 课程详情模态框 -->
  <div v-if="showDetailModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
    <div class="bg-white rounded-xl shadow-2xl w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
      <div class="px-6 py-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-900">课程详情</h3>
      </div>
      
      <div class="p-6" v-if="selectedCourse">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h4 class="font-medium text-gray-700 mb-3">基本信息</h4>
            <div class="space-y-2 text-sm">
              <p><span class="font-medium">标题：</span>{{ selectedCourse.title }}</p>
              <p><span class="font-medium">内容：</span>{{ selectedCourse.content || '暂无描述' }}</p>
              <p><span class="font-medium">分类：</span>{{ selectedCourse.category || '未分类' }}</p>
              <p><span class="font-medium">难度：</span>{{ getDifficultyText(selectedCourse.difficulty) }}</p>
              <p><span class="font-medium">时长：</span>{{ selectedCourse.duration || '未知' }}</p>
              <p><span class="font-medium">课时：</span>{{ selectedCourse.lessons || 0 }}</p>
              <p><span class="font-medium">标签：</span>{{ selectedCourse.tags || '无' }}</p>
            </div>
          </div>
          
          <div>
            <h4 class="font-medium text-gray-700 mb-3">时间信息</h4>
            <div class="space-y-2 text-sm">
              <p><span class="font-medium">创建时间：</span>{{ formatDate(selectedCourse.createdAt) }}</p>
              <p><span class="font-medium">更新时间：</span>{{ formatDate(selectedCourse.updatedAt) }}</p>
              <p v-if="selectedCourse.reviewedAt"><span class="font-medium">审核时间：</span>{{ formatDate(selectedCourse.reviewedAt) }}</p>
            </div>
            
            <h4 class="font-medium text-gray-700 mb-3 mt-4">审核信息</h4>
            <div class="space-y-2 text-sm">
              <p><span class="font-medium">状态：</span>
                <span class="px-2 py-1 rounded-full text-xs" :class="getStatusClass(selectedCourse.status)">
                  {{ getStatusText(selectedCourse.status) }}
                </span>
              </p>
              <p v-if="selectedCourse.reviewComment"><span class="font-medium">审核意见：</span>{{ selectedCourse.reviewComment }}</p>
              <p v-if="selectedCourse.reviewedBy"><span class="font-medium">审核人：</span>{{ getReviewerName(selectedCourse.reviewedBy) }}</p>
            </div>
          </div>
        </div>
        
        <div v-if="selectedCourse.coverImage" class="mt-6">
          <h4 class="font-medium text-gray-700 mb-3">课程封面</h4>
          <img 
            :src="getCoverImageUrl(selectedCourse.coverImage)" 
            :alt="selectedCourse.title"
            class="w-48 h-36 object-cover rounded-lg border"
            @error="handleImageError"
          />
        </div>
      </div>
      
      <div class="px-6 py-4 border-t border-gray-200">
        <button 
          @click="showDetailModal = false" 
          class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
        >
          关闭
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CourseReview',
  data() {
    return {
      activeTab: 'review',
      courses: [],
      managementCourses: [],
      loading: false,
      filterStatus: 'pending',
      managementFilterStatus: 'approved',
      searchKeyword: '',
      managementSearchKeyword: '',
      currentPage: 1,
      pageSize: 10,
      stats: {
        pending: 0,
        approved: 0,
        rejected: 0,
        total: 0
      },
      showReviewModal: false,
      showBatchReviewModal: false,
      showDetailModal: false,
      reviewAction: 'approve',
      batchReviewAction: 'approve',
      selectedCourse: null,
      reviewComment: '',
      batchReviewComment: '',
      selectedCourseIds: []
    }
  },

  computed: {
    filteredCourses() {
      let filtered = this.courses

      // 状态筛选
      if (this.filterStatus) {
        filtered = filtered.filter(c => c.status === this.filterStatus)
      }

      // 关键词搜索
      if (this.searchKeyword) {
        const keyword = this.searchKeyword.toLowerCase()
        filtered = filtered.filter(c => 
          c.title.toLowerCase().includes(keyword) ||
          c.author.toLowerCase().includes(keyword) ||
          (c.content && c.content.toLowerCase().includes(keyword))
        )
      }

      return filtered
    },

    pagedCourses() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredCourses.slice(start, end)
    },

    totalPages() {
      return Math.max(1, Math.ceil(this.filteredCourses.length / this.pageSize))
    }
  },

  mounted() {
    this.fetchCourses()
    this.fetchStats()
    this.fetchManagementCourses()
  },

  methods: {
    async fetchCourses() {
      this.loading = true
      try {
        const response = await axios.get('http://localhost:8080/api/admin/courses', { withCredentials: true })
        this.courses = response.data || []
      } catch (err) {
        console.error('Failed to fetch courses:', err)
        this.courses = []
      } finally {
        this.loading = false
      }
    },

    async fetchManagementCourses() {
      try {
        const response = await axios.get(`http://localhost:8080/api/admin/courses/search/status?status=${this.managementFilterStatus}`, { withCredentials: true })
        this.managementCourses = response.data || []
      } catch (err) {
        console.error('Failed to fetch management courses:', err)
        this.managementCourses = []
      }
    },

    async fetchStats() {
      try {
        const response = await axios.get('http://localhost:8080/api/admin/courses/stats', { withCredentials: true })
        if (response.data) {
          this.stats = response.data
        }
      } catch (err) {
        console.error('Failed to fetch stats:', err)
        this.calculateLocalStats()
      }
    },

    calculateLocalStats() {
      this.stats.total = this.courses.length
      this.stats.pending = this.courses.filter(c => c.status === 'pending').length
      this.stats.approved = this.courses.filter(c => c.status === 'approved').length
      this.stats.rejected = this.courses.filter(c => c.status === 'rejected').length
    },

    refreshData() {
      this.fetchCourses()
      this.fetchStats()
      this.currentPage = 1
    },

    refreshManagementData() {
      this.fetchManagementCourses()
    },

    exportData() {
      const data = this.filteredCourses.map(c => ({
        标题: c.title,
        教师: c.author,
        分类: c.category,
        状态: this.getStatusText(c.status),
        创建时间: this.formatDate(c.createdAt),
        审核时间: c.reviewedAt ? this.formatDate(c.reviewedAt) : '',
        审核意见: c.reviewComment || ''
      }))

      const csv = this.convertToCSV(data)
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `课程审核数据_${new Date().toISOString().split('T')[0]}.csv`
      link.click()
    },

    exportManagementData() {
      const data = this.managementCourses.map(c => ({
        标题: c.title,
        教师: c.author,
        分类: c.category,
        状态: this.getStatusText(c.status),
        创建时间: this.formatDate(c.createdAt),
        更新时间: this.formatDate(c.updatedAt)
      }))

      const csv = this.convertToCSV(data)
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `课程管理数据_${new Date().toISOString().split('T')[0]}.csv`
      link.click()
    },

    convertToCSV(data) {
      if (data.length === 0) return ''
      
      const headers = Object.keys(data[0])
      const csvRows = [
        headers.join(','),
        ...data.map(row => headers.map(header => `"${row[header]}"`).join(','))
      ]
      
      return csvRows.join('\n')
    },

    viewCourse(course) {
      this.selectedCourse = course
      this.showDetailModal = true
    },

    editCourse(course) {
      // 跳转到编辑页面或打开编辑模态框
      this.$router.push(`/admin/courses/edit/${course.articleId}`)
    },

    approveCourse(course) {
      this.selectedCourse = course
      this.reviewAction = 'approve'
      this.reviewComment = ''
      this.showReviewModal = true
    },

    rejectCourse(course) {
      this.selectedCourse = course
      this.reviewAction = 'reject'
      this.reviewComment = ''
      this.showReviewModal = true
    },

    async submitReview() {
      if (this.reviewAction === 'reject' && !this.reviewComment.trim()) {
        alert('拒绝课程时必须填写拒绝原因')
        return
      }

      try {
        await axios.post(`http://localhost:8080/api/admin/courses/${this.selectedCourse.articleId}/review`, {
          action: this.reviewAction,
          comment: this.reviewComment,
          status: this.reviewAction === 'approve' ? 'approved' : 'rejected'
        }, { withCredentials: true })

        alert(this.reviewAction === 'approve' ? '课程审核通过！' : '课程已拒绝')
        this.showReviewModal = false
        this.refreshData()
      } catch (err) {
        alert('审核操作失败：' + (err.response?.data || err.message))
      }
    },

    async submitBatchReview() {
      if (this.batchReviewAction === 'reject' && !this.batchReviewComment.trim()) {
        alert('拒绝课程时必须填写拒绝原因')
        return
      }

      // 获取所有待审核课程的ID
      const pendingCourseIds = this.courses
        .filter(c => c.status === 'pending')
        .map(c => c.articleId)

      if (pendingCourseIds.length === 0) {
        alert('没有待审核的课程')
        return
      }

      try {
        await axios.post('http://localhost:8080/api/admin/courses/batch-review', {
          courseIds: pendingCourseIds,
          action: this.batchReviewAction,
          comment: this.batchReviewComment
        }, { withCredentials: true })

        alert('批量审核完成！')
        this.showBatchReviewModal = false
        this.refreshData()
      } catch (err) {
        alert('批量审核失败：' + (err.response?.data || err.message))
      }
    },

    async deleteCourse(course) {
      if (!confirm(`确定要删除课程"${course.title}"吗？`)) {
        return
      }

      try {
        await axios.delete(`http://localhost:8080/api/admin/courses/${course.articleId}`, { withCredentials: true })
        alert('课程删除成功！')
        this.refreshManagementData()
      } catch (err) {
        alert('删除失败：' + (err.response?.data || err.message))
      }
    },

    getStatusText(status) {
      const statusMap = {
        'pending': '待审核',
        'approved': '已通过',
        'rejected': '已拒绝',
        'draft': '草稿',
        'active': '已发布'
      }
      return statusMap[status] || '未知'
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

    getDifficultyText(difficulty) {
      const difficultyMap = {
        'beginner': '入门',
        'intermediate': '中级',
        'advanced': '高级'
      }
      return difficultyMap[difficulty] || difficulty
    },

    getCoverImageUrl(coverImage) {
      if (!coverImage) return ''
      if (coverImage.startsWith('http')) return coverImage
      return `http://localhost:8080${coverImage}`
    },

    handleImageError(event) {
      event.target.style.display = 'none'
    },

    formatDate(dateString) {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },

    getReviewerName(reviewerId) {
      return `审核员${reviewerId}`
    },

    getEmptyMessage() {
      if (this.filterStatus === 'pending') return '当前没有待审核的课程'
      if (this.filterStatus === 'approved') return '当前没有已通过的课程'
      if (this.filterStatus === 'rejected') return '当前没有已拒绝的课程'
      if (this.searchKeyword) return `没有找到包含"${this.searchKeyword}"的课程`
      return '当前没有课程数据'
    }
  }
}
</script>
