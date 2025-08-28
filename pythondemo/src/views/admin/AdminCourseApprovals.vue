<template>
  <div class="container mx-auto p-6">
    <h2 class="text-2xl font-bold mb-4">课程审核</h2>
    
    <!-- 统计信息 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
      <div class="bg-blue-50 p-4 rounded-lg">
        <div class="text-2xl font-bold text-blue-600">{{ stats.pending || 0 }}</div>
        <div class="text-sm text-gray-600">待审核</div>
      </div>
      <div class="bg-green-50 p-4 rounded-lg">
        <div class="text-2xl font-bold text-green-600">{{ stats.approved || 0 }}</div>
        <div class="text-sm text-gray-600">已通过</div>
      </div>
      <div class="bg-red-50 p-4 rounded-lg">
        <div class="text-2xl font-bold text-red-600">{{ stats.rejected || 0 }}</div>
        <div class="text-sm text-gray-600">已拒绝</div>
      </div>
      <div class="bg-gray-50 p-4 rounded-lg">
        <div class="text-2xl font-bold text-gray-600">{{ stats.total || 0 }}</div>
        <div class="text-sm text-gray-600">总计</div>
      </div>
    </div>
    
    <!-- 筛选和操作区域 -->
    <div class="mb-4 flex flex-col md:flex-row gap-4 items-center justify-between">
      <div class="flex items-center gap-2">
        <select v-model="status" @change="fetch" class="border rounded px-3 py-2">
        <option value="pending">待审核</option>
        <option value="approved">已通过</option>
        <option value="rejected">已拒绝</option>
      </select>
        <input 
          v-model="keyword" 
          @input="debounceSearch"
          class="border rounded px-3 py-2 w-56" 
          placeholder="搜索标题" 
        />
        <button @click="fetch" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors">
          刷新
        </button>
      </div>
      
      <!-- 批量操作 -->
      <div v-if="status === 'pending' && filteredList.length > 0" class="flex items-center gap-2">
        <label class="flex items-center gap-2">
          <input 
            type="checkbox" 
            v-model="selectAll" 
            @change="toggleSelectAll"
            class="rounded"
          />
          <span class="text-sm">全选</span>
        </label>
        <button 
          @click="showBatchModal = true" 
          :disabled="selectedCourses.length === 0"
          class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          批量操作 ({{ selectedCourses.length }})
        </button>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      <span class="ml-2 text-gray-600">加载中...</span>
    </div>
    
    <!-- 课程列表 -->
    <div v-else-if="paged.length > 0">
      <table class="min-w-full bg-white rounded border shadow-sm">
      <thead>
        <tr class="bg-gray-50 text-left">
            <th v-if="status === 'pending'" class="p-3">
              <input 
                type="checkbox" 
                v-model="selectAll" 
                @change="toggleSelectAll"
                class="rounded"
              />
            </th>
            <th class="p-3 font-medium">标题</th>
            <th class="p-3 font-medium">教师ID</th>
            <th class="p-3 font-medium">状态</th>
            <th class="p-3 font-medium">备注</th>
            <th class="p-3 font-medium">操作</th>
        </tr>
      </thead>
      <tbody>
          <tr v-for="r in paged" :key="r.articleId || r.requestId" class="border-t hover:bg-gray-50">
            <td v-if="status === 'pending'" class="p-3">
              <input 
                type="checkbox" 
                v-model="selectedCourses" 
                :value="r.articleId || r.requestId"
                class="rounded"
              />
            </td>
            <td class="p-3">{{ r.title }}</td>
            <td class="p-3">{{ r.teacherId || r.authorId || r.author || '-' }}</td>
            <td class="p-3">
              <span class="px-2 py-1 rounded-full text-xs" :class="getStatusClass(r.status)">
                {{ getStatusText(r.status) }}
              </span>
            </td>
            <td class="p-3">{{ r.reviewNote || r.reviewComment || '-' }}</td>
            <td class="p-3 space-x-2">
              <button @click="viewCourse(r)" class="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 transition-colors">
                查看
              </button>
          </td>
        </tr>
      </tbody>
    </table>
      
      <!-- 分页 -->
      <div v-if="totalPages > 1" class="flex justify-between items-center mt-4">
        <div class="text-sm text-gray-600">
          显示 {{ (page - 1) * pageSize + 1 }} - {{ Math.min(page * pageSize, filteredList.length) }} 条，共 {{ filteredList.length }} 条
        </div>
        <div class="flex items-center gap-2">
          <button 
            :disabled="page === 1" 
            @click="page--" 
            class="px-3 py-1 border rounded disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
          >
            上一页
          </button>
      <span class="text-sm text-gray-600">{{ page }} / {{ totalPages }}</span>
          <button 
            :disabled="page === totalPages" 
            @click="page++" 
            class="px-3 py-1 border rounded disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
          >
            下一页
          </button>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="text-center py-8 text-gray-500">
      <div class="text-4xl mb-2">📝</div>
      <div>暂无{{ getStatusText(status) }}的课程</div>
    </div>
    
    <!-- 课程详情模态框 -->
    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
        <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
          <h3 class="text-lg font-semibold text-gray-900">课程详情</h3>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>
        
        <div class="p-6" v-if="selectedCourse">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <h4 class="font-medium text-gray-700 mb-3">基本信息</h4>
              <div class="space-y-2 text-sm">
                <p><span class="font-medium">标题：</span>{{ selectedCourse.title }}</p>
                <p><span class="font-medium">内容：</span>{{ selectedCourse.content || '暂无描述' }}</p>
                <p><span class="font-medium">分类：</span>{{ selectedCourse.category || '未分类' }}</p>
                <p><span class="font-medium">难度：</span>{{ selectedCourse.difficulty || '未知' }}</p>
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
                <p v-if="selectedCourse.reviewedBy"><span class="font-medium">审核人：</span>{{ selectedCourse.reviewedBy }}</p>
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
          
          <!-- 审核操作区域 -->
          <div v-if="selectedCourse.status === 'pending'" class="mt-6 p-4 bg-gray-50 rounded-lg">
            <h4 class="font-medium text-gray-700 mb-3">审核操作</h4>
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-2">审核意见</label>
              <textarea 
                v-model="reviewComment" 
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                placeholder="请输入审核意见..."
              ></textarea>
            </div>
            <div class="flex gap-3">
              <button 
                @click="approveCourse" 
                :disabled="approving"
                class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50"
              >
                {{ approving ? '处理中...' : '通过' }}
              </button>
              <button 
                @click="rejectCourse" 
                :disabled="rejecting"
                class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50"
              >
                {{ rejecting ? '处理中...' : '拒绝' }}
              </button>
            </div>
          </div>
        </div>
        
        <div class="px-6 py-4 border-t border-gray-200">
          <button 
            @click="closeModal" 
            class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
          >
            关闭
          </button>
        </div>
      </div>
    </div>
    
    <!-- 批量操作模态框 -->
    <div v-if="showBatchModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-md mx-4">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-semibold text-gray-900">批量审核</h3>
        </div>
        
        <div class="p-6">
          <p class="text-sm text-gray-600 mb-4">
            已选择 {{ selectedCourses.length }} 个课程进行批量操作
          </p>
          
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">审核意见</label>
            <textarea 
              v-model="batchComment" 
              rows="3"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
              placeholder="请输入审核意见..."
            ></textarea>
          </div>
          
          <div class="flex gap-3">
            <button 
              @click="batchApprove" 
              :disabled="batchProcessing"
              class="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50"
            >
              {{ batchProcessing ? '处理中...' : '批量通过' }}
            </button>
            <button 
              @click="batchReject" 
              :disabled="batchProcessing"
              class="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50"
            >
              {{ batchProcessing ? '处理中...' : '批量拒绝' }}
            </button>
          </div>
        </div>
        
        <div class="px-6 py-4 border-t border-gray-200">
          <button 
            @click="closeBatchModal" 
            class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
          >
            取消
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AdminCourseApprovals',
  data () {
    return { 
      list: [], 
      loading: true, 
      status: 'pending', 
      keyword: '', 
      page: 1, 
      pageSize: 10,
      showModal: false,
      selectedCourse: null,
      reviewComment: '',
      approving: false,
      rejecting: false,
      stats: {},
      selectedCourses: [],
      selectAll: false,
      showBatchModal: false,
      batchComment: '',
      batchProcessing: false,
      searchTimeout: null
    }
  },
  mounted () { 
    this.fetch()
    this.fetchStats()
  },
  computed: {
    filteredList () {
      const kw = (this.keyword || '').trim().toLowerCase()
      if (!kw) return this.list
      return this.list.filter(x => (x.title || '').toLowerCase().includes(kw))
    },
    paged () {
      const start = (this.page - 1) * this.pageSize
      return this.filteredList.slice(start, start + this.pageSize)
    },
    totalPages () { 
      return Math.max(1, Math.ceil(this.filteredList.length / this.pageSize)) 
    }
  },
  methods: {
    async fetch () {
      this.loading = true
      try {
        const res = await axios.get(`http://localhost:8080/api/admin/courses`, { withCredentials: true })
          // 筛选对应状态的课程
          const allCourses = res.data || []
          this.list = allCourses.filter(course => course.status === this.status)
        this.page = 1 // 重置页码
      } catch (err) {
          if (err?.response?.status === 403) {
            alert('需要管理员登录后才能访问，请先登录');
            this.$router && this.$router.push('/auth')
          } else {
          console.error('获取课程列表失败:', err)
          alert('获取课程列表失败: ' + (err.response?.data || err.message))
        }
      } finally {
        this.loading = false
      }
    },
    
    async fetchStats() {
      try {
        const res = await axios.get(`http://localhost:8080/api/admin/courses/stats`, { withCredentials: true })
        this.stats = res.data || {}
      } catch (err) {
        console.error('获取统计信息失败:', err)
      }
    },
    
    debounceSearch() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout)
      }
      this.searchTimeout = setTimeout(() => {
        this.page = 1 // 搜索时重置页码
      }, 300)
    },
    
    viewCourse(course) {
      this.selectedCourse = course
      this.reviewComment = ''
      this.showModal = true
    },
    
    closeModal() {
      this.showModal = false
      this.selectedCourse = null
      this.reviewComment = ''
      this.approving = false
      this.rejecting = false
    },
    
    async approveCourse() {
      if (this.approving) return
      
      this.approving = true
      try {
        const res = await axios.post(`http://localhost:8080/api/admin/courses/${this.selectedCourse.articleId}/review`, {
          action: 'approve',
          comment: this.reviewComment || '审核通过'
        }, { withCredentials: true })
        
        if (res.data?.success) {
          alert('课程审核通过！')
          this.closeModal()
          this.fetch()
          this.fetchStats()
        } else {
          alert('审核操作失败：' + (res.data?.message || '未知错误'))
        }
      } catch (err) {
        if (err?.response?.status === 403) {
          alert('需要管理员登录后才能操作，请先登录');
          this.$router && this.$router.push('/auth')
        } else {
          alert('审核操作失败：' + (err.response?.data || err.message))
        }
      } finally {
        this.approving = false
      }
    },
    
    async rejectCourse() {
      if (this.rejecting) return
      
      if (!this.reviewComment.trim()) {
        alert('拒绝课程时必须填写拒绝原因')
        return
      }
      
      this.rejecting = true
      try {
        const res = await axios.post(`http://localhost:8080/api/admin/courses/${this.selectedCourse.articleId}/review`, {
          action: 'reject',
          comment: this.reviewComment
        }, { withCredentials: true })
        
        if (res.data?.success) {
          alert('课程已拒绝')
          this.closeModal()
          this.fetch()
          this.fetchStats()
        } else {
          alert('审核操作失败：' + (res.data?.message || '未知错误'))
        }
      } catch (err) {
        if (err?.response?.status === 403) {
          alert('需要管理员登录后才能操作，请先登录');
          this.$router && this.$router.push('/auth')
        } else {
          alert('审核操作失败：' + (err.response?.data || err.message))
        }
      } finally {
        this.rejecting = false
      }
    },
    
    toggleSelectAll() {
      if (this.selectAll) {
        this.selectedCourses = this.paged.map(course => course.articleId || course.requestId)
      } else {
        this.selectedCourses = []
      }
    },
    
    closeBatchModal() {
      this.showBatchModal = false
      this.batchComment = ''
      this.batchProcessing = false
    },
    
    async batchApprove() {
      if (this.batchProcessing) return
      
      this.batchProcessing = true
      try {
        const res = await axios.post(`http://localhost:8080/api/admin/courses/batch-review`, {
          courseIds: this.selectedCourses,
          action: 'approve',
          comment: this.batchComment || '批量审核通过'
        }, { withCredentials: true })
        
        if (res.data?.success) {
          alert(`批量审核完成！成功处理 ${res.data.successCount} 个课程`)
          this.closeBatchModal()
          this.selectedCourses = []
          this.selectAll = false
          this.fetch()
          this.fetchStats()
        } else {
          alert('批量审核失败：' + (res.data?.message || '未知错误'))
        }
      } catch (err) {
        if (err?.response?.status === 403) {
          alert('需要管理员登录后才能操作，请先登录');
          this.$router && this.$router.push('/auth')
        } else {
          alert('批量审核失败：' + (err.response?.data || err.message))
        }
      } finally {
        this.batchProcessing = false
      }
    },
    
    async batchReject() {
      if (this.batchProcessing) return
      
      if (!this.batchComment.trim()) {
        alert('批量拒绝时必须填写拒绝原因')
        return
      }
      
      this.batchProcessing = true
      try {
        const res = await axios.post(`http://localhost:8080/api/admin/courses/batch-review`, {
          courseIds: this.selectedCourses,
          action: 'reject',
          comment: this.batchComment
      }, { withCredentials: true })
        
        if (res.data?.success) {
          alert(`批量拒绝完成！成功处理 ${res.data.successCount} 个课程`)
          this.closeBatchModal()
          this.selectedCourses = []
          this.selectAll = false
          this.fetch()
          this.fetchStats()
        } else {
          alert('批量审核失败：' + (res.data?.message || '未知错误'))
        }
      } catch (err) {
          if (err?.response?.status === 403) {
            alert('需要管理员登录后才能操作，请先登录');
            this.$router && this.$router.push('/auth')
          } else {
          alert('批量审核失败：' + (err.response?.data || err.message))
        }
      } finally {
        this.batchProcessing = false
      }
    },
    
    getStatusText(status) {
      const statusMap = {
        'pending': '待审核',
        'approved': '已通过',
        'rejected': '已拒绝'
      }
      return statusMap[status] || status
    },
    
    getStatusClass(status) {
      const statusClassMap = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'approved': 'bg-green-100 text-green-800',
        'rejected': 'bg-red-100 text-red-800'
      }
      return statusClassMap[status] || 'bg-gray-100 text-gray-800'
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
    }
  },
  
  watch: {
    selectedCourses() {
      // 更新全选状态
      this.selectAll = this.selectedCourses.length === this.paged.length && this.paged.length > 0
    }
  }
}
</script>

<style scoped>
</style>


