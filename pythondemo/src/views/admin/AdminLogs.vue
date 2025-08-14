<template>
  <div class="admin-logs">
    <div class="page-header">
      <h1 class="page-title">系统日志</h1>
      <div class="header-actions">
        <div class="log-filters">
          <select v-model="filterLevel" class="filter-select">
            <option value="">全部级别</option>
            <option value="INFO">信息</option>
            <option value="WARN">警告</option>
            <option value="ERROR">错误</option>
            <option value="DEBUG">调试</option>
          </select>
          <input 
            type="date" 
            v-model="startDate" 
            class="date-input"
            placeholder="开始日期"
          >
          <input 
            type="date" 
            v-model="endDate" 
            class="date-input"
            placeholder="结束日期"
          >
          <button class="btn btn-secondary" @click="searchLogs">
            <i class="fas fa-search"></i>
            搜索
          </button>
          <button class="btn btn-info" @click="exportLogs">
            <i class="fas fa-download"></i>
            导出
          </button>
        </div>
      </div>
    </div>

    <div class="logs-container">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="logs.length === 0" class="empty-container">
        <i class="fas fa-file-alt"></i>
        <p>暂无日志数据</p>
      </div>
      
      <div v-else class="logs-list">
        <div v-for="log in logs" :key="log.id" class="log-item" :class="log.level.toLowerCase()">
          <div class="log-header">
            <span :class="['log-level', log.level.toLowerCase()]">{{ log.level }}</span>
            <span class="log-time">{{ log.timestamp }}</span>
          </div>
          <div class="log-content">
            <div class="log-message">{{ log.message }}</div>
            <div class="log-meta">
              <span v-if="log.userId" class="meta-item">
                <i class="fas fa-user"></i>
                用户ID: {{ log.userId }}
              </span>
              <span v-if="log.ip" class="meta-item">
                <i class="fas fa-network-wired"></i>
                IP: {{ log.ip }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminLogs',
  data() {
    return {
      logs: [],
      loading: false,
      filterLevel: '',
      startDate: '',
      endDate: ''
    }
  },
  async mounted() {
    await this.loadLogs()
  },
  methods: {
    async loadLogs() {
      this.loading = true
      try {
        const params = new URLSearchParams({
          adminId: this.getCurrentUserId()
        })
        if (this.filterLevel) params.append('level', this.filterLevel)
        if (this.startDate) params.append('startDate', this.startDate)
        if (this.endDate) params.append('endDate', this.endDate)
        
        const response = await fetch(`/api/admin/logs?${params}`, {
          credentials: 'include'
        })
        if (response.ok) {
          this.logs = await response.json()
        }
      } catch (error) {
        console.error('加载日志失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    async searchLogs() {
      await this.loadLogs()
    },
    
    exportLogs() {
      // 导出日志为CSV
      const csvContent = this.generateLogsCSV()
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `system_logs_${new Date().toISOString().split('T')[0]}.csv`
      link.click()
    },
    
    generateLogsCSV() {
      const headers = ['时间', '级别', '消息', '用户ID', 'IP地址']
      const rows = this.logs.map(log => [
        log.timestamp,
        log.level,
        log.message,
        log.userId || '',
        log.ip || ''
      ])
      
      return [headers, ...rows].map(row => row.join(',')).join('\n')
    },
    
    getCurrentUserId() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      return user.userId || 1
    }
  }
}
</script>

<style scoped>
.admin-logs {
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

.log-filters {
  display: flex;
  gap: 0.5rem;
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

.date-input {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}

.date-input:focus {
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

.logs-container {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
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

.logs-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.log-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 1rem;
  transition: all 0.2s;
}

.log-item:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.log-item.info {
  border-left: 4px solid #17a2b8;
}

.log-item.warn {
  border-left: 4px solid #ffc107;
}

.log-item.error {
  border-left: 4px solid #dc3545;
}

.log-item.debug {
  border-left: 4px solid #6c757d;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.log-level {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
  text-transform: uppercase;
}

.log-level.info {
  background: #d1ecf1;
  color: #0c5460;
}

.log-level.warn {
  background: #fff3cd;
  color: #856404;
}

.log-level.error {
  background: #f8d7da;
  color: #721c24;
}

.log-level.debug {
  background: #e2e3e5;
  color: #383d41;
}

.log-time {
  font-size: 0.8rem;
  color: #666;
}

.log-content {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.log-message {
  font-weight: 500;
  color: #333;
  line-height: 1.4;
}

.log-meta {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.8rem;
  color: #666;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .log-filters {
    width: 100%;
    flex-direction: column;
  }
  
  .log-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .log-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style>
