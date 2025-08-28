<template>
  <div class="admin-courses">
    <div class="page-header">
      <h1 class="page-title">课程管理</h1>
      <div class="header-actions">
        <select v-model="filterStatus" @change="filterCourses" class="filter-select">
          <option value="">全部状态</option>
          <option value="approved">已通过</option>
          <option value="rejected">已拒绝</option>
        </select>
        <button class="btn btn-primary" @click="exportCourses">
          <i class="fas fa-download"></i>
          导出数据
        </button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-container">
      <div class="stat-card">
        <div class="stat-number">{{ stats.approved || 0 }}</div>
        <div class="stat-label">已通过课程</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.rejected || 0 }}</div>
        <div class="stat-label">已拒绝课程</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.total || 0 }}</div>
        <div class="stat-label">总课程数</div>
      </div>
    </div>

    <div class="courses-container">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="filteredCourses.length === 0" class="empty-container">
        <i class="fas fa-book"></i>
        <p>暂无{{ getStatusText(filterStatus) }}的课程</p>
      </div>
      
      <div v-else v-for="course in filteredCourses" :key="course.articleId" class="course-card">
        <div class="course-header">
          <h3 class="course-title">{{ course.title }}</h3>
          <span :class="['course-status', course.status]">{{ getStatusText(course.status) }}</span>
        </div>
        <div class="course-content">
          <p class="course-desc">{{ course.content || '暂无描述' }}</p>
          <div class="course-meta">
            <span class="meta-item">
              <i class="fas fa-user"></i>
              {{ course.author || '未知教师' }}
            </span>
            <span class="meta-item">
              <i class="fas fa-calendar"></i>
              创建时间：{{ formatDate(course.createdAt) || '未知' }}
            </span>
            <span class="meta-item">
              <i class="fas fa-clock"></i>
              课程时长：{{ course.duration || '未知' }}
            </span>
            <span v-if="course.reviewedAt" class="meta-item">
              <i class="fas fa-check-circle"></i>
              审核时间：{{ formatDate(course.reviewedAt) }}
            </span>
            <span v-if="course.reviewComment" class="meta-item">
              <i class="fas fa-comment"></i>
              审核意见：{{ course.reviewComment }}
            </span>
          </div>
        </div>
        <div class="course-actions">
          <button class="btn btn-info" @click="viewCourse(course)">
            <i class="fas fa-eye"></i>
            查看详情
          </button>
          <button v-if="course.status === 'rejected'" class="btn btn-success" @click="openReviewModal(course, 'approve')">
            <i class="fas fa-check"></i>
            重新通过
          </button>
          <button v-if="course.status === 'approved'" class="btn btn-warning" @click="openReviewModal(course, 'reject')">
            <i class="fas fa-times"></i>
            重新拒绝
          </button>
          <button class="btn btn-secondary" @click="editCourse(course.articleId)">
            <i class="fas fa-edit"></i>
            编辑
          </button>
        </div>
      </div>
    </div>

    <!-- 课程详情模态框 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="showDetailModal = false">
      <div class="modal-content large-modal" @click.stop>
        <div class="modal-header">
          <h3>课程详情</h3>
          <button class="modal-close" @click="showDetailModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body" v-if="selectedCourse">
          <div class="course-detail-grid">
            <div class="detail-section">
              <h4>基本信息</h4>
              <div class="detail-item">
                <span class="label">标题：</span>
                <span class="value">{{ selectedCourse.title }}</span>
              </div>
              <div class="detail-item">
                <span class="label">内容：</span>
                <span class="value">{{ selectedCourse.content || '暂无描述' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">分类：</span>
                <span class="value">{{ selectedCourse.category || '未分类' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">难度：</span>
                <span class="value">{{ selectedCourse.difficulty || '未知' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">时长：</span>
                <span class="value">{{ selectedCourse.duration || '未知' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">课时：</span>
                <span class="value">{{ selectedCourse.lessons || 0 }}</span>
              </div>
              <div class="detail-item">
                <span class="label">标签：</span>
                <span class="value">{{ selectedCourse.tags || '无' }}</span>
              </div>
            </div>
            
            <div class="detail-section">
              <h4>时间信息</h4>
              <div class="detail-item">
                <span class="label">创建时间：</span>
                <span class="value">{{ formatDate(selectedCourse.createdAt) }}</span>
              </div>
              <div class="detail-item">
                <span class="label">更新时间：</span>
                <span class="value">{{ formatDate(selectedCourse.updatedAt) }}</span>
              </div>
              <div v-if="selectedCourse.reviewedAt" class="detail-item">
                <span class="label">审核时间：</span>
                <span class="value">{{ formatDate(selectedCourse.reviewedAt) }}</span>
              </div>
              
              <h4>审核信息</h4>
              <div class="detail-item">
                <span class="label">状态：</span>
                <span :class="['status-badge', selectedCourse.status]">
                  {{ getStatusText(selectedCourse.status) }}
                </span>
              </div>
              <div v-if="selectedCourse.reviewComment" class="detail-item">
                <span class="label">审核意见：</span>
                <span class="value">{{ selectedCourse.reviewComment }}</span>
              </div>
              <div v-if="selectedCourse.reviewedBy" class="detail-item">
                <span class="label">审核人：</span>
                <span class="value">{{ selectedCourse.reviewedBy }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="selectedCourse.coverImage" class="cover-image-section">
            <h4>课程封面</h4>
            <img 
              :src="getCoverImageUrl(selectedCourse.coverImage)" 
              :alt="selectedCourse.title"
              class="cover-image"
              @error="handleImageError"
            />
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showDetailModal = false">关闭</button>
        </div>
      </div>
    </div>

    <!-- 审核模态框 -->
    <div v-if="showReviewModal" class="modal-overlay" @click="showReviewModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>课程审核</h3>
          <button class="modal-close" @click="showReviewModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="course-info">
            <h4>{{ selectedCourse && selectedCourse.title }}</h4>
            <p>{{ (selectedCourse && selectedCourse.content) || '暂无描述' }}</p>
          </div>
          <div class="review-form">
            <label>审核意见：</label>
            <textarea 
              v-model="reviewComment" 
              placeholder="请输入审核意见..."
              rows="4"
            ></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showReviewModal = false">取消</button>
          <button 
            class="btn btn-success" 
            @click="submitReview"
            :disabled="submitting"
          >
            {{ submitting ? '处理中...' : '确认审核' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AdminCourses',
  data() {
    return {
      courses: [],
      loading: true,
      filterStatus: 'approved', // 默认显示已通过的课程
      showDetailModal: false,
      showReviewModal: false,
      selectedCourse: null,
      reviewComment: '',
      reviewAction: '',
      submitting: false,
      stats: {}
    }
  },
  computed: {
    filteredCourses() {
      if (!this.filterStatus) return this.courses
      return this.courses.filter(course => course.status === this.filterStatus)
    }
  },
  mounted() {
    this.loadCourses()
    this.loadStats()
  },
  methods: {
    async loadCourses() {
      this.loading = true
      try {
        const response = await axios.get('http://localhost:8080/api/admin/courses', { withCredentials: true })
        // 只获取已审核的课程（已通过和已拒绝）
        this.courses = (response.data || []).filter(course => 
          course.status === 'approved' || course.status === 'rejected'
        )
      } catch (error) {
        console.error('加载课程失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能访问，请先登录')
          this.$router && this.$router.push('/auth')
        }
      } finally {
        this.loading = false
      }
    },
    
    async loadStats() {
      try {
        const response = await axios.get('http://localhost:8080/api/admin/courses/stats', { withCredentials: true })
        this.stats = response.data || {}
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    },
    
    filterCourses() {
      // 筛选功能已通过计算属性实现
    },
    
    viewCourse(course) {
      this.selectedCourse = course
      this.showDetailModal = true
    },
    
    openReviewModal(course, action) {
      this.selectedCourse = course
      this.reviewAction = action
      this.reviewComment = ''
      this.showReviewModal = true
    },
    
    async submitReview() {
      if (!this.reviewComment.trim()) {
        alert('请填写审核意见')
        return
      }
      
      this.submitting = true
      try {
        const response = await axios.post(
          `http://localhost:8080/api/admin/courses/${this.selectedCourse.articleId}/review`,
          {
            action: this.reviewAction,
            comment: this.reviewComment
          },
          { withCredentials: true }
        )
        
        if (response.data && response.data.success) {
          alert('审核操作成功！')
          this.showReviewModal = false
          this.loadCourses()
          this.loadStats()
        } else {
          alert('审核操作失败：' + (response.data && response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('审核操作失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能操作，请先登录')
          this.$router && this.$router.push('/auth')
        } else {
          alert('审核操作失败：' + (error.response && error.response.data || error.message))
        }
      } finally {
        this.submitting = false
      }
    },
    
    editCourse(courseId) {
      // TODO: 实现编辑功能
      console.log('编辑课程:', courseId)
      alert('编辑功能开发中...')
    },
    
    exportCourses() {
      // TODO: 实现导出功能
      alert('导出功能开发中...')
    },
    
    getStatusText(status) {
      const statusMap = {
        'pending': '待审核',
        'approved': '已通过',
        'rejected': '已拒绝'
      }
      return statusMap[status] || status
    },
    
    formatDate(dateString) {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },
    
    getCoverImageUrl(coverImage) {
      if (!coverImage) return ''
      if (coverImage.startsWith('http')) return coverImage
      return `http://localhost:8080${coverImage}`
    },
    
    handleImageError(event) {
      event.target.style.display = 'none'
    }
  }
}
</script>

<style scoped>
.admin-courses {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.courses-container {
  display: grid;
  gap: 20px;
}

.course-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.course-title {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.course-status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.course-status.approved {
  background: #d4edda;
  color: #155724;
}

.course-status.rejected {
  background: #f8d7da;
  color: #721c24;
}

.course-content {
  padding: 20px;
}

.course-desc {
  color: #666;
  margin-bottom: 15px;
  line-height: 1.5;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #888;
  font-size: 14px;
}

.course-actions {
  display: flex;
  gap: 10px;
  padding: 20px;
  background: #f8f9fa;
  border-top: 1px solid #eee;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn:hover {
  opacity: 0.8;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.loading-container, .empty-container {
  text-align: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.large-modal {
  max-width: 800px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 20px;
}

.course-info h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.course-info p {
  color: #666;
  margin-bottom: 20px;
}

.review-form label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.review-form textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #eee;
}

.course-detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.detail-section h4 {
  margin: 0 0 15px 0;
  color: #333;
  border-bottom: 2px solid #007bff;
  padding-bottom: 5px;
}

.detail-item {
  display: flex;
  margin-bottom: 10px;
  align-items: flex-start;
}

.detail-item .label {
  font-weight: 500;
  color: #666;
  min-width: 100px;
  flex-shrink: 0;
}

.detail-item .value {
  color: #333;
  flex: 1;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.approved {
  background: #d4edda;
  color: #155724;
}

.status-badge.rejected {
  background: #f8d7da;
  color: #721c24;
}

.cover-image-section {
  margin-top: 30px;
  text-align: center;
}

.cover-image-section h4 {
  margin: 0 0 15px 0;
  color: #333;
}

.cover-image {
  max-width: 300px;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

@media (max-width: 768px) {
  .course-detail-grid {
    grid-template-columns: 1fr;
  }
  
  .course-meta {
    flex-direction: column;
    gap: 10px;
  }
  
  .course-actions {
    flex-wrap: wrap;
  }
}
</style> 