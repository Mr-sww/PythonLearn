<template>
  <div class="test-learning-records">
    <div class="container">
      <h2>学习记录整合测试</h2>
      
      <div class="row">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">
              <h5>知识点记录测试</h5>
            </div>
            <div class="card-body">
              <button @click="testKnowledgeRecords" class="btn btn-primary mb-3">
                测试知识点记录
              </button>
              <div v-if="knowledgeResult" class="alert alert-info">
                <pre>{{ JSON.stringify(knowledgeResult, null, 2) }}</pre>
              </div>
            </div>
          </div>
        </div>
        
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">
              <h5>视频记录测试</h5>
            </div>
            <div class="card-body">
              <button @click="testVideoRecords" class="btn btn-info mb-3">
                测试视频记录
              </button>
              <div v-if="videoResult" class="alert alert-info">
                <pre>{{ JSON.stringify(videoResult, null, 2) }}</pre>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row mt-4">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5>统计信息测试</h5>
            </div>
            <div class="card-body">
              <button @click="testStatistics" class="btn btn-success mb-3">
                测试统计信息
              </button>
              <div v-if="statsResult" class="alert alert-success">
                <pre>{{ JSON.stringify(statsResult, null, 2) }}</pre>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row mt-4">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5>整合测试</h5>
            </div>
            <div class="card-body">
              <button @click="testIntegration" class="btn btn-warning mb-3">
                测试整合功能
              </button>
              <div v-if="integrationResult" class="alert alert-warning">
                <h6>整合结果：</h6>
                <ul>
                  <li>知识点记录数量: {{ integrationResult.knowledgeCount }}</li>
                  <li>视频记录数量: {{ integrationResult.videoCount }}</li>
                  <li>总记录数量: {{ integrationResult.totalCount }}</li>
                  <li>知识点统计: {{ integrationResult.knowledgeStats ? '成功' : '失败' }}</li>
                  <li>视频统计: {{ integrationResult.videoStats ? '成功' : '失败' }}</li>
                </ul>
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
  name: 'TestLearningRecords',
  data() {
    return {
      knowledgeResult: null,
      videoResult: null,
      statsResult: null,
      integrationResult: null
    }
  },
  methods: {
    async testKnowledgeRecords() {
      try {
        const result = await learningRecordService.getKnowledgeRecords(10)
        this.knowledgeResult = result
        console.log('知识点记录测试成功:', result)
      } catch (error) {
        this.knowledgeResult = { error: error.message }
        console.error('知识点记录测试失败:', error)
      }
    },
    
    async testVideoRecords() {
      try {
        const result = await learningRecordService.getVideoRecords(10)
        this.videoResult = result
        console.log('视频记录测试成功:', result)
      } catch (error) {
        this.videoResult = { error: error.message }
        console.error('视频记录测试失败:', error)
      }
    },
    
    async testStatistics() {
      try {
        const [knowledgeStats, videoStats] = await Promise.all([
          learningRecordService.getKnowledgeStats(),
          learningRecordService.getVideoStats()
        ])
        
        this.statsResult = {
          knowledge: knowledgeStats,
          video: videoStats,
          combined: {
            totalKnowledge: knowledgeStats.totalKnowledge || 0,
            totalVideos: videoStats.totalVideos || 0,
            completedItems: (knowledgeStats.completedKnowledge || 0) + (videoStats.completedVideos || 0),
            totalStudyTime: Math.round(((knowledgeStats.totalStudyTime || 0) + (videoStats.totalWatchTime || 0)) / 60)
          }
        }
        console.log('统计信息测试成功:', this.statsResult)
      } catch (error) {
        this.statsResult = { error: error.message }
        console.error('统计信息测试失败:', error)
      }
    },
    
    async testIntegration() {
      try {
        // 并行加载所有数据
        const [knowledgeRecords, videoRecords, knowledgeStats, videoStats] = await Promise.all([
          learningRecordService.getKnowledgeRecords(50),
          learningRecordService.getVideoRecords(50),
          learningRecordService.getKnowledgeStats(),
          learningRecordService.getVideoStats()
        ])
        
        // 为记录添加类型标识
        const knowledgeWithType = (knowledgeRecords || []).map(record => ({
          ...record,
          type: 'knowledge'
        }))
        
        const videoWithType = (videoRecords || []).map(record => ({
          ...record,
          type: 'video'
        }))
        
        // 合并并按时间排序
        const allRecords = [...knowledgeWithType, ...videoWithType]
        allRecords.sort((a, b) => new Date(b.startTime) - new Date(a.startTime))
        
        this.integrationResult = {
          knowledgeCount: knowledgeWithType.length,
          videoCount: videoWithType.length,
          totalCount: allRecords.length,
          knowledgeStats: knowledgeStats,
          videoStats: videoStats,
          sampleRecords: allRecords.slice(0, 5) // 显示前5条记录作为示例
        }
        
        console.log('整合测试成功:', this.integrationResult)
      } catch (error) {
        this.integrationResult = { error: error.message }
        console.error('整合测试失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.test-learning-records {
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

.alert {
  margin-top: 10px;
}
</style>
