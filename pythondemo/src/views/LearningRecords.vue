<template>
  <div class="learning-records-container">
    <div class="container-fluid">
      <!-- 页面标题 -->
      <div class="row mb-4">
        <div class="col-12">
          <h2 class="fw-bold text-dark">
            <i class="fa fa-graduation-cap me-2"></i>学习记录
          </h2>
          <p class="text-muted">查看你的学习历史和进度</p>
        </div>
      </div>

      <!-- 统计卡片 -->
      <div class="row mb-4">
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-primary mb-2">
                <i class="fa fa-book fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalCourses || 0 }}</h4>
              <p class="text-muted mb-0">学习课程数</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-success mb-2">
                <i class="fa fa-check-circle fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.completedLessons || 0 }}</h4>
              <p class="text-muted mb-0">完成课时</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-info mb-2">
                <i class="fa fa-clock fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalStudyTime || 0 }}小时</h4>
              <p class="text-muted mb-0">学习时长</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-warning mb-2">
                <i class="fa fa-calendar fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.continuousDays || 0 }}</h4>
              <p class="text-muted mb-0">连续学习天数</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 筛选和搜索 -->
      <div class="row mb-4">
        <div class="col-md-8">
          <div class="btn-group" role="group">
            <button 
              v-for="filter in filters" 
              :key="filter.value"
              @click="setFilter(filter.value)"
              :class="['btn', currentFilter === filter.value ? 'btn-primary' : 'btn-outline-primary']"
            >
              {{ filter.label }}
            </button>
          </div>
        </div>
        <div class="col-md-4">
          <div class="input-group">
            <input 
              v-model="searchKeyword" 
              type="text" 
              class="form-control" 
              placeholder="搜索课程..."
              @input="handleSearch"
            >
            <button class="btn btn-outline-secondary" type="button">
              <i class="fa fa-search"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 记录列表 -->
      <div class="row">
        <div class="col-12">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-white">
              <h5 class="mb-0">学习记录</h5>
            </div>
            <div class="card-body p-0">
              <div v-if="loading" class="text-center py-5">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">加载中...</span>
                </div>
              </div>
              <div v-else-if="records.length === 0" class="text-center py-5">
                <i class="fa fa-inbox fa-3x text-muted mb-3"></i>
                <p class="text-muted">暂无学习记录</p>
              </div>
              <div v-else class="list-group list-group-flush">
                <div 
                  v-for="record in records" 
                  :key="record.id"
                  class="list-group-item border-0 py-3"
                >
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0 me-3">
                      <img 
                        :src="record.courseImage || '/course_images/default.jpg'" 
                        alt="课程图片"
                        class="rounded"
                        style="width: 60px; height: 60px; object-fit: cover;"
                      >
                    </div>
                    <div class="flex-grow-1">
                                             <h6 class="mb-1">{{ record.courseTitle || '代码练习' }}</h6>
                       <p class="text-muted mb-1 small">{{ record.lessonTitle || `题目 ${record.lessonId}` }}</p>
                      <div class="d-flex align-items-center">
                        <small class="text-muted me-3">
                          <i class="fa fa-clock me-1"></i>{{ formatDate(record.studyDate) }}
                        </small>
                        <small class="text-muted me-3">
                          <i class="fa fa-play me-1"></i>{{ record.duration }}分钟
                        </small>
                        <div class="progress flex-grow-1 me-3" style="height: 6px;">
                          <div 
                            class="progress-bar" 
                            :style="{ width: record.progress + '%' }"
                          ></div>
                        </div>
                        <span class="badge bg-success">{{ record.progress }}%</span>
                      </div>
                    </div>
                  </div>
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
  name: 'LearningRecords',
  data() {
    return {
      loading: false,
      records: [],
      statistics: {
        totalCourses: 0,
        completedLessons: 0,
        totalStudyTime: 0,
        continuousDays: 0
      },
      filters: [
        { label: '全部', value: 'all' },
        { label: '最近一周', value: 'week' },
        { label: '最近一月', value: 'month' },
        { label: '最近三月', value: 'quarter' }
      ],
      currentFilter: 'all',
      searchKeyword: ''
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadRecords()
  },
  methods: {
    async loadStatistics() {
      try {
        const res = await this.$axios.get('/api/study-records/statistics', { withCredentials: true })
        this.statistics = {
          totalCourses: res.data.totalCourses || 0,
          completedLessons: res.data.completedLessons || 0,
          totalStudyTime: res.data.totalStudyHours || 0,
          continuousDays: res.data.continuousDays || 0
        }
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    },
    async loadRecords() {
      this.loading = true
      try {
        const res = await this.$axios.get('/api/study-records', { withCredentials: true })
                 this.records = res.data.map(record => ({
           id: record.recordId,
           courseTitle: record.courseTitle || (record.courseId === 9999 ? '代码练习' : '未知课程'),
           lessonTitle: record.lessonTitle || (record.courseId === 9999 ? `题目 ${record.lessonId}` : '未知课时'),
           courseImage: record.courseImage || (record.courseId === 9999 ? '/course_images/code_practice.jpg' : '/course_images/default.jpg'),
           studyDate: record.studyDate,
           duration: Math.round(record.studyTime / 60), // 转换为分钟
           progress: Math.round(record.progress || 0),
           courseId: record.courseId,
           lessonId: record.lessonId
         }))
      } catch (error) {
        console.error('加载学习记录失败:', error)
        this.records = []
      } finally {
        this.loading = false
      }
    },
    setFilter(filter) {
      this.currentFilter = filter
      // 根据筛选条件重新加载数据
      if (filter === 'all') {
        this.loadRecords()
      } else {
        this.loadFilteredRecords(filter)
      }
    },
    async loadFilteredRecords(filter) {
      this.loading = true
      try {
        const res = await this.$axios.get('/api/study-records', { withCredentials: true })
        let filteredRecords = res.data
        
        // 根据筛选条件过滤数据
        const now = new Date()
        const oneWeekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
        const oneMonthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
        const threeMonthsAgo = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
        
        switch (filter) {
          case 'week':
            filteredRecords = filteredRecords.filter(record => 
              new Date(record.studyDate) >= oneWeekAgo
            )
            break
          case 'month':
            filteredRecords = filteredRecords.filter(record => 
              new Date(record.studyDate) >= oneMonthAgo
            )
            break
          case 'quarter':
            filteredRecords = filteredRecords.filter(record => 
              new Date(record.studyDate) >= threeMonthsAgo
            )
            break
        }
        
                 this.records = filteredRecords.map(record => ({
           id: record.recordId,
           courseTitle: record.courseTitle || (record.courseId === 9999 ? '代码练习' : '未知课程'),
           lessonTitle: record.lessonTitle || (record.courseId === 9999 ? `题目 ${record.lessonId}` : '未知课时'),
           courseImage: record.courseImage || (record.courseId === 9999 ? '/course_images/code_practice.jpg' : '/course_images/default.jpg'),
           studyDate: record.studyDate,
           duration: Math.round(record.studyTime / 60),
           progress: Math.round(record.progress || 0),
           courseId: record.courseId,
           lessonId: record.lessonId
         }))
      } catch (error) {
        console.error('加载筛选记录失败:', error)
        this.records = []
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      // 实现搜索逻辑 - 可以过滤课程标题
      if (this.searchKeyword.trim()) {
        this.records = this.records.filter(record => 
          record.courseTitle.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
          record.lessonTitle.toLowerCase().includes(this.searchKeyword.toLowerCase())
        )
      } else {
        this.loadRecords()
      }
    },
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.learning-records-container {
  padding: 20px;
}

.progress-bar {
  background-color: #007bff;
}
</style>
