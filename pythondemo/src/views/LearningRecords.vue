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
              <h4 class="fw-bold text-dark">{{ statistics.totalKnowledge || 0 }}</h4>
              <p class="text-muted mb-0">学习知识点</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-success mb-2">
                <i class="fa fa-check-circle fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.completedKnowledge || 0 }}</h4>
              <p class="text-muted mb-0">完成知识点</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-info mb-2">
                <i class="fa fa-clock fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalStudyTime || 0 }}分钟</h4>
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
              placeholder="搜索知识点..."
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
              <h5 class="mb-0">知识点学习记录</h5>
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
                <p class="text-muted small">开始学习知识点来查看记录</p>
              </div>
              <div v-else class="list-group list-group-flush">
                <div 
                  v-for="record in records" 
                  :key="record.id"
                  class="list-group-item border-0 py-3"
                >
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0 me-3">
                      <div class="bg-primary rounded d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                        <i class="fa fa-book text-white fa-2x"></i>
                      </div>
                    </div>
                    <div class="flex-grow-1">
                      <h6 class="mb-2">{{ record.knowledgeTitle || '未知知识点' }}</h6>
                      <div class="d-flex align-items-center mb-2">
                        <span class="badge bg-info me-3">文字知识点</span>
                        <small class="text-muted">ID: {{ record.knowledgeId }}</small>
                      </div>
                      <div class="d-flex align-items-center justify-content-between">
                        <div class="d-flex align-items-center">
                          <small class="text-muted me-4">
                            <i class="fa fa-clock me-1"></i>{{ formatDate(record.startTime) }}
                          </small>
                          <small class="text-muted me-4">
                            <i class="fa fa-hourglass-half me-1"></i>{{ formatStudyTime(record.studyTime) }}
                          </small>
                        </div>
                        <span class="badge" :class="getStatusBadgeClass(record.status)">
                          {{ getStatusText(record.status) }}
                        </span>
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
import axios from 'axios';

export default {
  name: 'LearningRecords',
  data() {
    return {
      loading: false,
      records: [],
      statistics: {
        totalKnowledge: 0,
        completedKnowledge: 0,
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
        const res = await axios.get('http://localhost:8080/api/learning/knowledge/stats', { 
          withCredentials: true 
        })
        console.log('统计信息:', res.data);
        this.statistics = {
          totalKnowledge: res.data.totalKnowledge || 0,
          completedKnowledge: res.data.completedKnowledge || 0,
          totalStudyTime: Math.round((res.data.totalStudyTime || 0) / 60), // 转换为分钟
          continuousDays: res.data.continuousDays || 0
        }
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    },
    async loadRecords() {
      this.loading = true
      try {
        const res = await axios.get('http://localhost:8080/api/learning/knowledge/records', { 
          withCredentials: true,
          params: { limit: 50 }
        })
        console.log('学习记录:', res.data);
        this.records = res.data || []
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
        const res = await axios.get('http://localhost:8080/api/learning/knowledge/records', { 
          withCredentials: true,
          params: { limit: 100 }
        })
        let filteredRecords = res.data || []
        
        // 根据筛选条件过滤数据
        const now = new Date()
        const oneWeekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
        const oneMonthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
        const threeMonthsAgo = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
        
        switch (filter) {
          case 'week':
            filteredRecords = filteredRecords.filter(record => 
              new Date(record.startTime) >= oneWeekAgo
            )
            break
          case 'month':
            filteredRecords = filteredRecords.filter(record => 
              new Date(record.startTime) >= oneMonthAgo
            )
            break
          case 'quarter':
            filteredRecords = filteredRecords.filter(record => 
              new Date(record.startTime) >= threeMonthsAgo
            )
            break
        }
        
        this.records = filteredRecords
      } catch (error) {
        console.error('加载筛选记录失败:', error)
        this.records = []
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      // 实现搜索逻辑 - 可以过滤知识点标题
      if (this.searchKeyword.trim()) {
        this.records = this.records.filter(record => 
          record.knowledgeTitle.toLowerCase().includes(this.searchKeyword.toLowerCase())
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
    },
    formatStudyTime(seconds) {
      if (!seconds || seconds === 0) {
        return '0分钟'
      }
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      if (minutes > 0) {
        return remainingSeconds > 0 ? `${minutes}分${remainingSeconds}秒` : `${minutes}分钟`
      } else {
        return `${remainingSeconds}秒`
      }
    },
    getStatusText(status) {
      switch (status) {
        case 'started': return '已开始'
        case 'in_progress': return '学习中'
        case 'completed': return '已完成'
        default: return '未知'
      }
    },
    getStatusBadgeClass(status) {
      switch (status) {
        case 'started': return 'bg-warning'
        case 'in_progress': return 'bg-info'
        case 'completed': return 'bg-success'
        default: return 'bg-secondary'
      }
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
