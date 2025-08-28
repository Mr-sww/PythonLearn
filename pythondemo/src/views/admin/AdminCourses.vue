<template>
  <div class="admin-courses">
    <div class="page-header">
      <h1 class="page-title">课程管理</h1>
      <div class="header-actions">
        <select v-model="filterStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="pending">待审核</option>
          <option value="approved">已通过</option>
          <option value="rejected">已拒绝</option>
        </select>
        <button class="btn btn-primary" @click="exportCourses">
          <i class="fas fa-download"></i>
          导出数据
        </button>
      </div>
    </div>

    <div class="courses-container">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="filteredCourses.length === 0" class="empty-container">
        <i class="fas fa-book"></i>
        <p>暂无课程数据</p>
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
          </div>
        </div>
        <div class="course-actions">
          <button class="btn btn-info" @click="viewCourse(course.articleId)">
            <i class="fas fa-eye"></i>
            查看
          </button>
          <button v-if="course.status === 'pending'" class="btn btn-success" @click="openReviewModal(course, 'approve')">
            <i class="fas fa-check"></i>
            通过
          </button>
          <button v-if="course.status === 'pending'" class="btn btn-warning" @click="openReviewModal(course, 'reject')">
            <i class="fas fa-times"></i>
            拒绝
          </button>
          <button class="btn btn-secondary" @click="editCourse(course.articleId)">
            <i class="fas fa-edit"></i>
            编辑
          </button>
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
            v-if="selectedCourse && selectedCourse.status === 'pending'"
            class="btn btn-success" 
            @click="approveCourse(selectedCourse.articleId)"
          >
            通过
          </button>
          <button 
            v-if="selectedCourse && selectedCourse.status === 'pending'"
            class="button btn-warning" 
            @click="rejectCourse(selectedCourse.articleId)"
          >
            拒绝
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const http = axios.create({ baseURL: 'http://localhost:8080/api', withCredentials: true })
export default {
  name: 'AdminCourses',
  data() {
    return {
      filterStatus: 'approved',
      courses: [],
      loading: false,
      showReviewModal: false,
      selectedCourse: null,
      reviewComment: ''
    }
  },
  computed: {
    filteredCourses() {
      if (!this.filterStatus) return this.courses
      return this.courses.filter(course => course.status === this.filterStatus)
    }
  },
  async mounted() {
    await this.loadCourses()
  },
  methods: {
    async loadCourses() {
      this.loading = true
      try {
        const res = await http.get('/admin/courses')
        this.courses = Array.isArray(res.data) ? res.data : []
      } catch (error) {
        console.error('加载课程列表失败:', error)
      } finally {
        this.loading = false
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
    
    async approveCourse(courseId) {
      try {
        await http.put(`/admin/courses/${courseId}/review`, null, { params: { action: 'approve', comment: this.reviewComment || '审核通过' } })
          this.$message.success('课程审核通过')
          await this.loadCourses()
          this.showReviewModal = false
          this.reviewComment = ''
      } catch (error) {
        console.error('审核课程失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    async rejectCourse(courseId) {
      if (!this.reviewComment.trim()) {
        this.$message.warning('请填写拒绝原因')
        return
      }
      
      try {
        await http.put(`/admin/courses/${courseId}/review`, null, { params: { action: 'reject', comment: this.reviewComment } })
          this.$message.success('课程已拒绝')
          await this.loadCourses()
          this.showReviewModal = false
          this.reviewComment = ''
      } catch (error) {
        console.error('拒绝课程失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    openReviewModal(course) {
      this.selectedCourse = course
      this.showReviewModal = true
      this.reviewComment = ''
    },
    
    exportCourses() {
      // 导出课程数据为CSV
      const csvContent = this.generateCSV()
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `courses_${new Date().toISOString().split('T')[0]}.csv`
      link.click()
    },
    
    generateCSV() {
      const headers = ['课程ID', '课程名称', '描述', '教师', '状态', '创建时间']
      const rows = this.courses.map(course => [
        course.articleId,
        course.title,
        course.content || '暂无描述',
        course.author || '未知教师',
        this.getStatusText(course.status),
        this.formatDate(course.createdAt) || '未知'
      ])
      
      return [headers, ...rows].map(row => row.join(',')).join('\n')
    },
    
    viewCourse(courseId) {
      this.$router.push(`/course/${courseId}`)
    },
    
    editCourse(courseId) {
      // 跳转到课程编辑页面
      this.$router.push(`/admin/courses/${courseId}/edit`)
    },
    
    getCurrentUserId() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      return user.userId || 1
    },
    
    formatDate(date) {
      if (!date) return null
      return new Date(date).toLocaleDateString('zh-CN')
    }
  }
}
</script>

<style scoped>
.admin-courses {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.filter-select {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
  background: white;
}

.filter-select:focus {
  outline: none;
  border-color: #d32f2f;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-primary {
  background: linear-gradient(135deg, #d32f2f 0%, #f44336 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(211, 47, 47, 0.3);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-info:hover {
  background: #138496;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-success:hover {
  background: #218838;
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.btn-warning:hover {
  background: #e0a800;
}

.courses-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #d32f2f;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  color: #666;
}

.empty-container i {
  font-size: 3rem;
  margin-bottom: 1rem;
  color: #ddd;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #666;
}

.modal-close:hover {
  color: #333;
}

.modal-body {
  padding: 1.5rem;
}

.course-info {
  margin-bottom: 1.5rem;
}

.course-info h4 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.course-info p {
  margin: 0;
  color: #666;
  line-height: 1.5;
}

.review-form label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.review-form textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
  resize: vertical;
}

.review-form textarea:focus {
  outline: none;
  border-color: #d32f2f;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid #f0f0f0;
}

.course-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.course-card:hover {
  transform: translateY(-2px);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.course-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.course-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.course-status.pending {
  background: #fff3cd;
  color: #856404;
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
  margin-bottom: 1.5rem;
}

.course-desc {
  color: #666;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.course-meta {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #666;
  font-size: 0.9rem;
}

.course-actions {
  display: flex;
  gap: 1rem;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .header-actions {
    width: 100%;
    flex-direction: column;
  }
  
  .course-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .course-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .course-actions {
    flex-direction: column;
  }
}
</style> 