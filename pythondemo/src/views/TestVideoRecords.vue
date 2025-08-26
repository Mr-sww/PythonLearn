<template>
  <div class="test-video-records">
    <div class="container">
      <h2>视频记录功能测试</h2>
      
      <div class="row">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">
              <h5>视频播放器测试</h5>
            </div>
            <div class="card-body">
              <VideoPlayer
                :video-id="testVideo.id"
                :video-title="testVideo.title"
                :video-url="testVideo.url"
                :poster="testVideo.poster"
                :total-duration="testVideo.duration"
                @video-completed="onVideoCompleted"
              />
            </div>
          </div>
        </div>
        
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">
              <h5>视频记录API测试</h5>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <button @click="testStartVideo" class="btn btn-primary me-2">
                  开始观看
                </button>
                <button @click="testUpdateProgress" class="btn btn-warning me-2">
                  更新进度
                </button>
                <button @click="testCompleteVideo" class="btn btn-success me-2">
                  完成观看
                </button>
              </div>
              
              <div class="mb-3">
                <button @click="testGetVideoRecords" class="btn btn-info me-2">
                  获取记录
                </button>
                <button @click="testGetVideoStats" class="btn btn-secondary">
                  获取统计
                </button>
              </div>
              
              <div v-if="testResult" class="alert alert-info">
                <pre>{{ JSON.stringify(testResult, null, 2) }}</pre>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row mt-4">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5>视频记录列表</h5>
            </div>
            <div class="card-body">
              <div v-if="videoRecords.length === 0" class="text-center text-muted">
                暂无视频观看记录
              </div>
              <div v-else class="video-records-list">
                <div v-for="record in videoRecords" :key="record.id" class="video-record-item">
                  <div class="record-header">
                    <h6>{{ record.videoTitle }}</h6>
                    <span class="badge" :class="getStatusBadgeClass(record.status)">
                      {{ getStatusText(record.status) }}
                    </span>
                  </div>
                  <div class="record-details">
                    <span class="text-muted">视频ID: {{ record.videoId }}</span>
                    <span class="text-muted">观看时长: {{ formatDuration(record.watchTime) }}</span>
                    <span class="text-muted">进度: {{ record.progress }}%</span>
                    <span class="text-muted">开始时间: {{ formatDate(record.startTime) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row mt-4">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5>视频统计信息</h5>
            </div>
            <div class="card-body">
              <div v-if="videoStats" class="stats-grid">
                <div class="stat-item">
                  <div class="stat-number">{{ videoStats.totalVideos || 0 }}</div>
                  <div class="stat-label">总视频数</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ videoStats.completedVideos || 0 }}</div>
                  <div class="stat-label">已完成</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ Math.round((videoStats.totalWatchTime || 0) / 60) }}分钟</div>
                  <div class="stat-label">总观看时长</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ videoStats.continuousDays || 0 }}</div>
                  <div class="stat-label">连续观看天数</div>
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
import VideoPlayer from '@/components/VideoPlayer.vue'
import { learningRecordService } from '@/services/learningRecordService'

export default {
  name: 'TestVideoRecords',
  components: {
    VideoPlayer
  },
  data() {
    return {
      testVideo: {
        id: 1,
        title: '测试视频 - Python基础语法',
        url: 'https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4',
        poster: '/default-video-poster.jpg',
        duration: 1800 // 30分钟
      },
      testResult: null,
      videoRecords: [],
      videoStats: null
    }
  },
  mounted() {
    this.loadVideoRecords()
    this.loadVideoStats()
  },
  methods: {
    async testStartVideo() {
      try {
        const result = await learningRecordService.startVideoWatch(
          this.testVideo.id,
          this.testVideo.title,
          this.testVideo.url,
          this.testVideo.duration
        )
        this.testResult = { type: 'startVideo', result }
        this.loadVideoRecords()
        this.loadVideoStats()
      } catch (error) {
        this.testResult = { type: 'startVideo', error: error.message }
      }
    },

    async testUpdateProgress() {
      try {
        const result = await learningRecordService.updateVideoProgress(
          this.testVideo.id,
          900, // 15分钟
          50.0 // 50%进度
        )
        this.testResult = { type: 'updateProgress', result }
        this.loadVideoRecords()
      } catch (error) {
        this.testResult = { type: 'updateProgress', error: error.message }
      }
    },

    async testCompleteVideo() {
      try {
        const result = await learningRecordService.completeVideoWatch(this.testVideo.id)
        this.testResult = { type: 'completeVideo', result }
        this.loadVideoRecords()
        this.loadVideoStats()
      } catch (error) {
        this.testResult = { type: 'completeVideo', error: error.message }
      }
    },

    async testGetVideoRecords() {
      try {
        const result = await learningRecordService.getVideoRecords(10)
        this.testResult = { type: 'getVideoRecords', result }
        this.videoRecords = result
      } catch (error) {
        this.testResult = { type: 'getVideoRecords', error: error.message }
      }
    },

    async testGetVideoStats() {
      try {
        const result = await learningRecordService.getVideoStats()
        this.testResult = { type: 'getVideoStats', result }
        this.videoStats = result
      } catch (error) {
        this.testResult = { type: 'getVideoStats', error: error.message }
      }
    },

    async loadVideoRecords() {
      try {
        this.videoRecords = await learningRecordService.getVideoRecords(10)
      } catch (error) {
        console.error('加载视频记录失败:', error)
      }
    },

    async loadVideoStats() {
      try {
        this.videoStats = await learningRecordService.getVideoStats()
      } catch (error) {
        console.error('加载视频统计失败:', error)
      }
    },

    onVideoCompleted(data) {
      console.log('视频观看完成:', data)
      this.loadVideoRecords()
      this.loadVideoStats()
    },

    formatDuration(seconds) {
      if (!seconds || isNaN(seconds)) return '00:00'
      
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      const secs = Math.floor(seconds % 60)
      
      if (hours > 0) {
        return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
      } else {
        return `${minutes}:${secs.toString().padStart(2, '0')}`
      }
    },

    formatDate(dateString) {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    },

    getStatusBadgeClass(status) {
      switch (status) {
        case 'completed': return 'bg-success'
        case 'in_progress': return 'bg-warning'
        case 'started': return 'bg-info'
        default: return 'bg-secondary'
      }
    },

    getStatusText(status) {
      switch (status) {
        case 'completed': return '已完成'
        case 'in_progress': return '学习中'
        case 'started': return '已开始'
        default: return '未知'
      }
    }
  }
}
</script>

<style scoped>
.test-video-records {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}

pre {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 300px;
  overflow-y: auto;
}

.video-records-list {
  max-height: 400px;
  overflow-y: auto;
}

.video-record-item {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.record-header h6 {
  margin: 0;
  color: #333;
}

.record-details {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #6c757d;
}

@media (max-width: 768px) {
  .record-details {
    flex-direction: column;
    gap: 8px;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>





