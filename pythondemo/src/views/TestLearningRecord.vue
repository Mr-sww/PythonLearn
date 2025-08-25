<template>
  <div class="test-learning-record">
    <div class="container">
      <h2>学习记录功能测试</h2>
      
      <!-- 登录状态 -->
      <div class="card mb-4">
        <div class="card-header">
          <h5>登录状态</h5>
        </div>
        <div class="card-body">
          <div v-if="user">
            <p><strong>用户ID:</strong> {{ user.userId }}</p>
            <p><strong>用户名:</strong> {{ user.nickname }}</p>
            <p><strong>登录状态:</strong> <span class="text-success">已登录</span></p>
          </div>
          <div v-else>
            <p><strong>登录状态:</strong> <span class="text-danger">未登录</span></p>
            <button @click="goToLogin" class="btn btn-primary">去登录</button>
          </div>
        </div>
      </div>

      <!-- 测试操作 -->
      <div class="card mb-4">
        <div class="card-header">
          <h5>测试操作</h5>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <h6>知识点学习记录测试</h6>
              <div class="mb-3">
                <label class="form-label">知识点ID:</label>
                <input v-model="testKnowledgeId" type="number" class="form-control" placeholder="输入知识点ID">
              </div>
              <div class="mb-3">
                <label class="form-label">知识点标题:</label>
                <input v-model="testKnowledgeTitle" type="text" class="form-control" placeholder="输入知识点标题">
              </div>
              <button @click="testStartKnowledge" class="btn btn-success me-2">开始学习</button>
              <button @click="testUpdateProgress" class="btn btn-info me-2">更新进度</button>
              <button @click="testCompleteKnowledge" class="btn btn-warning">完成学习</button>
            </div>
            <div class="col-md-6">
              <h6>API测试</h6>
              <button @click="testGetRecords" class="btn btn-primary me-2">获取记录</button>
              <button @click="testGetStats" class="btn btn-secondary">获取统计</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 测试结果 -->
      <div class="card mb-4">
        <div class="card-header">
          <h5>测试结果</h5>
        </div>
        <div class="card-body">
          <div v-if="testResult">
            <h6>API响应:</h6>
            <pre class="bg-light p-3 rounded">{{ JSON.stringify(testResult, null, 2) }}</pre>
          </div>
          <div v-else>
            <p class="text-muted">暂无测试结果</p>
          </div>
        </div>
      </div>

      <!-- 数据库记录 -->
      <div class="card">
        <div class="card-header">
          <h5>数据库记录</h5>
        </div>
        <div class="card-body">
          <div v-if="records.length > 0">
            <div v-for="record in records" :key="record.id" class="border-bottom pb-2 mb-2">
              <p><strong>ID:</strong> {{ record.id }}</p>
              <p><strong>知识点:</strong> {{ record.knowledgeTitle }}</p>
              <p><strong>状态:</strong> {{ record.status }}</p>
              <p><strong>进度:</strong> {{ record.progress }}%</p>
              <p><strong>学习时长:</strong> {{ record.studyTime }}秒</p>
              <p><strong>开始时间:</strong> {{ formatDate(record.startTime) }}</p>
            </div>
          </div>
          <div v-else>
            <p class="text-muted">暂无记录</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'TestLearningRecord',
  data() {
    return {
      user: null,
      testKnowledgeId: 1,
      testKnowledgeTitle: '测试知识点',
      testResult: null,
      records: []
    }
  },
  mounted() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      const userStr = localStorage.getItem('user');
      if (userStr) {
        this.user = JSON.parse(userStr);
      }
    },
    goToLogin() {
      this.$router.push('/login-test');
    },
    async testStartKnowledge() {
      if (!this.user) {
        alert('请先登录');
        return;
      }
      
      try {
        const response = await axios.post('http://localhost:8080/api/learning/knowledge/start', null, {
          params: {
            knowledgeId: this.testKnowledgeId,
            knowledgeTitle: this.testKnowledgeTitle
          },
          withCredentials: true
        });
        
        this.testResult = response.data;
        this.testGetRecords();
        alert('开始学习成功！');
      } catch (error) {
        console.error('开始学习失败:', error);
        this.testResult = { error: error.response?.data || error.message };
        alert('开始学习失败: ' + (error.response?.data || error.message));
      }
    },
    async testUpdateProgress() {
      if (!this.user) {
        alert('请先登录');
        return;
      }
      
      try {
        const response = await axios.put('http://localhost:8080/api/learning/knowledge/progress', null, {
          params: {
            knowledgeId: this.testKnowledgeId,
            studyTime: 300, // 5分钟
            progress: 50.0
          },
          withCredentials: true
        });
        
        this.testResult = response.data;
        this.testGetRecords();
        alert('更新进度成功！');
      } catch (error) {
        console.error('更新进度失败:', error);
        this.testResult = { error: error.response?.data || error.message };
        alert('更新进度失败: ' + (error.response?.data || error.message));
      }
    },
    async testCompleteKnowledge() {
      if (!this.user) {
        alert('请先登录');
        return;
      }
      
      try {
        const response = await axios.post('http://localhost:8080/api/learning/knowledge/complete', null, {
          params: {
            knowledgeId: this.testKnowledgeId
          },
          withCredentials: true
        });
        
        this.testResult = response.data;
        this.testGetRecords();
        alert('完成学习成功！');
      } catch (error) {
        console.error('完成学习失败:', error);
        this.testResult = { error: error.response?.data || error.message };
        alert('完成学习失败: ' + (error.response?.data || error.message));
      }
    },
    async testGetRecords() {
      if (!this.user) {
        alert('请先登录');
        return;
      }
      
      try {
        const response = await axios.get('http://localhost:8080/api/learning/knowledge/records', {
          params: { limit: 10 },
          withCredentials: true
        });
        
        this.records = response.data || [];
        this.testResult = { records: this.records };
        console.log('获取记录成功:', this.records);
      } catch (error) {
        console.error('获取记录失败:', error);
        this.testResult = { error: error.response?.data || error.message };
        alert('获取记录失败: ' + (error.response?.data || error.message));
      }
    },
    async testGetStats() {
      if (!this.user) {
        alert('请先登录');
        return;
      }
      
      try {
        const response = await axios.get('http://localhost:8080/api/learning/knowledge/stats', {
          withCredentials: true
        });
        
        this.testResult = response.data;
        console.log('获取统计成功:', response.data);
      } catch (error) {
        console.error('获取统计失败:', error);
        this.testResult = { error: error.response?.data || error.message };
        alert('获取统计失败: ' + (error.response?.data || error.message));
      }
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN');
    }
  }
}
</script>

<style scoped>
.test-learning-record {
  padding: 20px;
}

pre {
  max-height: 300px;
  overflow-y: auto;
}
</style>





