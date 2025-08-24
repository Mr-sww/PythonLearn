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
        <div class="col-md-2 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-primary mb-2">
                <i class="fa fa-book fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalKnowledge || 0 }}</h4>
              <p class="text-muted mb-0">知识点</p>
            </div>
          </div>
        </div>
        <div class="col-md-2 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-info mb-2">
                <i class="fa fa-video fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalVideos || 0 }}</h4>
              <p class="text-muted mb-0">视频</p>
            </div>
          </div>
        </div>
        <div class="col-md-2 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-success mb-2">
                <i class="fa fa-check-circle fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.completedItems || 0 }}</h4>
              <p class="text-muted mb-0">已完成</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-warning mb-2">
                <i class="fa fa-clock fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalStudyTime || 0 }}分钟</h4>
              <p class="text-muted mb-0">总学习时长</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-secondary mb-2">
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
        <div class="col-md-6">
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
        <div class="col-md-3">
          <div class="btn-group" role="group">
            <button 
              v-for="type in recordTypes" 
              :key="type.value"
              @click="setRecordType(type.value)"
              :class="['btn', currentRecordType === type.value ? 'btn-success' : 'btn-outline-success']"
            >
              {{ type.label }}
            </button>
          </div>
        </div>
        <div class="col-md-3">
          <div class="input-group">
            <input 
              v-model="searchKeyword" 
              type="text" 
              class="form-control" 
              placeholder="搜索学习内容..."
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
              <div v-else-if="allRecords.length === 0" class="text-center py-5">
                <i class="fa fa-inbox fa-3x text-muted mb-3"></i>
                <p class="text-muted">暂无学习记录</p>
                <p class="text-muted small">开始学习知识点或观看视频来查看记录</p>
              </div>
              <div v-else class="list-group list-group-flush">
                <div 
                  v-for="record in allRecords" 
                  :key="record.id"
                  class="list-group-item border-0 py-3"
                >
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0 me-3">
                      <div class="rounded d-flex align-items-center justify-content-center" 
                           :class="getRecordIconClass(record.type)" 
                           style="width: 60px; height: 60px;">
                        <i :class="getRecordIcon(record.type)" class="text-white fa-2x"></i>
                      </div>
                    </div>
                    <div class="flex-grow-1">
                      <h6 class="mb-2">{{ getRecordTitle(record) }}</h6>
                      <div class="d-flex align-items-center mb-2">
                        <span class="badge me-3" :class="getRecordTypeBadgeClass(record.type)">
                          {{ getRecordTypeText(record.type) }}
                        </span>
                        <small class="text-muted">ID: {{ getRecordId(record) }}</small>
                      </div>
                      <div class="d-flex align-items-center justify-content-between">
                        <div class="d-flex align-items-center">
                          <small class="text-muted me-4">
                            <i class="fa fa-clock me-1"></i>{{ formatDate(record.startTime) }}
                          </small>
                          <small class="text-muted me-4">
                            <i class="fa fa-hourglass-half me-1"></i>{{ formatStudyTime(getRecordStudyTime(record)) }}
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
import { learningRecordService } from '@/services/learningRecordService'

export default {
  name: 'LearningRecords',
  data() {
    return {
      loading: false,
      knowledgeRecords: [],
      videoRecords: [],
      statistics: {
        totalKnowledge: 0,
        totalVideos: 0,
        completedItems: 0,
        totalStudyTime: 0,
        continuousDays: 0
      },
      filters: [
        { label: '全部', value: 'all' },
        { label: '最近一周', value: 'week' },
        { label: '最近一月', value: 'month' },
        { label: '最近三月', value: 'quarter' }
      ],
      recordTypes: [
        { label: '全部', value: 'all' },
        { label: '知识点', value: 'knowledge' },
        { label: '视频', value: 'video' }
      ],
      currentFilter: 'all',
      currentRecordType: 'all',
      searchKeyword: ''
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadAllRecords()
  },
  computed: {
    allRecords() {
      let records = [...this.knowledgeRecords, ...this.videoRecords]
      
      // 按时间排序（最新的在前）
      records.sort((a, b) => new Date(b.startTime) - new Date(a.startTime))
      
      // 根据记录类型筛选
      if (this.currentRecordType !== 'all') {
        records = records.filter(record => record.type === this.currentRecordType)
      }
      
      // 根据时间筛选
      if (this.currentFilter !== 'all') {
        const now = new Date()
        let filterDate
        switch (this.currentFilter) {
          case 'week':
            filterDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
            break
          case 'month':
            filterDate = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
            break
          case 'quarter':
            filterDate = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
            break
          default:
            filterDate = null
        }
        if (filterDate) {
          records = records.filter(record => new Date(record.startTime) >= filterDate)
        }
      }
      
      // 根据关键词搜索
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.toLowerCase()
        records = records.filter(record => {
          const title = this.getRecordTitle(record).toLowerCase()
          return title.includes(keyword)
        })
      }
      
      return records
    }
  },
  methods: {
    async loadStatistics() {
      try {
        // 加载知识点统计
        const knowledgeStats = await learningRecordService.getKnowledgeStats()
        console.log('知识点统计信息:', knowledgeStats);
        
        // 暂时设置视频统计为0，因为视频API可能还没有实现
        const videoStats = { totalVideos: 0, completedVideos: 0, totalWatchTime: 0, continuousDays: 0 }
        console.log('视频统计信息（临时）:', videoStats);
        
        this.statistics = {
          totalKnowledge: knowledgeStats.totalKnowledge || 0,
          totalVideos: videoStats.totalVideos || 0,
          completedItems: (knowledgeStats.completedKnowledge || 0) + (videoStats.completedVideos || 0),
          totalStudyTime: Math.round(((knowledgeStats.totalStudyTime || 0) + (videoStats.totalWatchTime || 0)) / 60), // 转换为分钟
          continuousDays: Math.max(knowledgeStats.continuousDays || 0, videoStats.continuousDays || 0)
        }
        
        console.log('最终统计信息:', this.statistics);
      } catch (error) {
        console.error('加载统计信息失败:', error)
        // 设置默认值
        this.statistics = {
          totalKnowledge: 0,
          totalVideos: 0,
          completedItems: 0,
          totalStudyTime: 0,
          continuousDays: 0
        }
      }
    },
    async loadAllRecords() {
      this.loading = true
      try {
        // 暂时只加载知识点记录，因为视频API可能还没有实现
        const knowledgeRes = await learningRecordService.getKnowledgeRecords(50)
        console.log('知识点记录:', knowledgeRes);
        
        // 为记录添加类型标识
        this.knowledgeRecords = (knowledgeRes || []).map(record => ({
          ...record,
          type: 'knowledge'
        }))
        
        // 暂时设置视频记录为空数组
        this.videoRecords = []
        console.log('视频记录（临时）:', this.videoRecords);
        
      } catch (error) {
        console.error('加载学习记录失败:', error)
        this.knowledgeRecords = []
        this.videoRecords = []
      } finally {
        this.loading = false
      }
    },
    setFilter(filter) {
      this.currentFilter = filter
    },
    
    setRecordType(type) {
      this.currentRecordType = type
    },
    // 记录类型相关方法
    getRecordIcon(type) {
      return type === 'knowledge' ? 'fa fa-book' : 'fa fa-video'
    },
    
    getRecordIconClass(type) {
      return type === 'knowledge' ? 'bg-primary' : 'bg-info'
    },
    
    getRecordTitle(record) {
      if (record.type === 'knowledge') {
        return record.knowledgeTitle || '未知知识点'
      } else {
        return record.videoTitle || '未知视频'
      }
    },
    
    getRecordTypeText(type) {
      return type === 'knowledge' ? '知识点' : '视频'
    },
    
    getRecordTypeBadgeClass(type) {
      return type === 'knowledge' ? 'bg-primary' : 'bg-info'
    },
    
    getRecordId(record) {
      if (record.type === 'knowledge') {
        return record.knowledgeId
      } else {
        return record.videoId
      }
    },
    
    getRecordStudyTime(record) {
      if (record.type === 'knowledge') {
        return record.studyTime
      } else {
        return record.watchTime
      }
    },
    handleSearch() {
      // 搜索逻辑现在在computed属性中处理
      // 这里可以添加额外的搜索逻辑
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
