<template>
  <div class="session-test">
    <div class="container">
      <h2>Session测试页面</h2>
      
      <div class="card">
        <h3>当前用户信息</h3>
        <pre>{{ JSON.stringify(userInfo, null, 2) }}</pre>
      </div>
      
      <div class="card">
        <h3>测试结果</h3>
        <div v-if="loading">测试中...</div>
        <div v-else>
          <p><strong>Session状态:</strong> {{ sessionStatus }}</p>
          <p><strong>学习记录:</strong> {{ recordsStatus }}</p>
          <p><strong>学习统计:</strong> {{ statsStatus }}</p>
        </div>
        <button @click="runTest" :disabled="loading">运行测试</button>
      </div>
      
      <div class="card" v-if="testResults.length > 0">
        <h3>详细测试结果</h3>
        <div v-for="(result, index) in testResults" :key="index" class="test-result">
          <strong>{{ result.name }}:</strong> {{ result.status }}
          <div v-if="result.error" class="error">{{ result.error }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { learningRecordService } from '@/services/learningRecordService'

export default {
  name: 'SessionTest',
  data() {
    return {
      userInfo: {},
      loading: false,
      sessionStatus: '未测试',
      recordsStatus: '未测试',
      statsStatus: '未测试',
      testResults: []
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const user = JSON.parse(localStorage.getItem('user') || 'null')
      this.userInfo = user || {}
    },
    
    async runTest() {
      this.loading = true
      this.testResults = []
      
      try {
        // 测试1: 检查用户信息
        this.testResults.push({
          name: '用户信息检查',
          status: this.userInfo.userId ? '✅ 用户已登录' : '❌ 用户未登录',
          error: null
        })
        
        // 测试2: 获取学习记录
        try {
          const records = await learningRecordService.getKnowledgeRecords(5)
          this.recordsStatus = `✅ 成功获取 ${records.length} 条记录`
          this.testResults.push({
            name: '获取学习记录',
            status: '✅ 成功',
            error: null
          })
        } catch (error) {
          this.recordsStatus = '❌ 获取失败'
          this.testResults.push({
            name: '获取学习记录',
            status: '❌ 失败',
            error: error.message || error.toString()
          })
        }
        
                 // 测试3: 获取学习统计
         try {
           await learningRecordService.getKnowledgeStats()
           this.statsStatus = '✅ 获取成功'
           this.testResults.push({
             name: '获取学习统计',
             status: '✅ 成功',
             error: null
           })
         } catch (error) {
          this.statsStatus = '❌ 获取失败'
          this.testResults.push({
            name: '获取学习统计',
            status: '❌ 失败',
            error: error.message || error.toString()
          })
        }
        
        // 测试4: 检查localStorage
        const userId = localStorage.getItem('userId')
        const isLoggedIn = localStorage.getItem('isLoggedIn')
        this.testResults.push({
          name: 'localStorage检查',
          status: userId ? '✅ 有用户ID' : '❌ 无用户ID',
          error: `userId: ${userId}, isLoggedIn: ${isLoggedIn}`
        })
        
      } catch (error) {
        console.error('测试失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.session-test {
  padding: 20px;
}

.container {
  max-width: 800px;
  margin: 0 auto;
}

.card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.test-result {
  margin-bottom: 10px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
}

.error {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

button {
  background: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

pre {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
}
</style>
