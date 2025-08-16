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
                      <h6 class="mb-1">{{ record.courseTitle }}</h6>
                      <p class="text-muted mb-1 small">{{ record.lessonTitle }}</p>
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
        totalCourses: 12,
        completedLessons: 45,
        totalStudyTime: 28,
        continuousDays: 7
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
    this.loadRecords()
  },
  methods: {
    async loadRecords() {
      this.loading = true
      try {
        // 模拟数据
        this.records = [
          {
            id: 1,
            courseTitle: 'Python基础入门',
            lessonTitle: '第一章：Python简介',
            courseImage: '/course_images/2169.jpg',
            studyDate: '2025-01-17 14:30:00',
            duration: 45,
            progress: 100
          },
          {
            id: 2,
            courseTitle: '数据结构与算法',
            lessonTitle: '第二章：数组和链表',
            courseImage: '/course_images/2170.jpg',
            studyDate: '2025-01-16 16:20:00',
            duration: 60,
            progress: 85
          },
          {
            id: 3,
            courseTitle: 'Web开发实战',
            lessonTitle: '第三章：HTML基础',
            courseImage: '/course_images/2171.jpg',
            studyDate: '2025-01-15 10:15:00',
            duration: 30,
            progress: 100
          }
        ]
      } catch (error) {
        console.error('加载学习记录失败:', error)
      } finally {
        this.loading = false
      }
    },
    setFilter(filter) {
      this.currentFilter = filter
      this.loadRecords()
    },
    handleSearch() {
      // 实现搜索逻辑
      this.loadRecords()
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
