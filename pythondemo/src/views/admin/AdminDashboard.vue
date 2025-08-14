<template>
  <div class="admin-dashboard">
    <div class="page-header">
      <h2>管理员仪表板</h2>
      <div class="header-actions">
        <button class="btn btn-primary" @click="refreshData">
          <i class="fas fa-sync-alt"></i> 刷新数据
        </button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-users"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.totalUsers || 0 }}</h3>
          <p>总用户数</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon active">
          <i class="fas fa-user-check"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.activeUsers || 0 }}</h3>
          <p>活跃用户</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon students">
          <i class="fas fa-graduation-cap"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.totalStudents || 0 }}</h3>
          <p>学生用户</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon teachers">
          <i class="fas fa-chalkboard-teacher"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.totalTeachers || 0 }}</h3>
          <p>教师用户</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon admins">
          <i class="fas fa-user-shield"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.totalAdmins || 0 }}</h3>
          <p>管理员</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon courses">
          <i class="fas fa-book"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.totalCourses || 0 }}</h3>
          <p>总课程数</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon pending">
          <i class="fas fa-clock"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.pendingCourses || 0 }}</h3>
          <p>待审核课程</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon approved">
          <i class="fas fa-check-circle"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.approvedCourses || 0 }}</h3>
          <p>已通过课程</p>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <!-- 专业分布图表 -->
      <div class="chart-card">
        <h3>学生专业分布</h3>
        <div class="chart-container">
          <canvas ref="majorChart"></canvas>
        </div>
      </div>

      <!-- 用户角色分布图表 -->
      <div class="chart-card">
        <h3>用户角色分布</h3>
        <div class="chart-container">
          <canvas ref="roleChart"></canvas>
        </div>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activities">
      <h3>最近活动</h3>
      <div class="activities-list">
        <div v-if="loading" class="loading-container">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>
        
        <div v-else-if="activities.length === 0" class="empty-container">
          <i class="fas fa-info-circle"></i>
          <p>暂无活动记录</p>
        </div>
        
        <div v-else class="activity-item" v-for="activity in activities" :key="activity.id">
          <div class="activity-icon">
            <i :class="getActivityIcon(activity.type)"></i>
          </div>
          <div class="activity-content">
            <p class="activity-description">{{ activity.description }}</p>
            <span class="activity-time">{{ formatTime(activity.timestamp) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'
import Chart from 'chart.js/auto'

export default {
  name: 'AdminDashboard',
  setup() {
    const statistics = ref({})
    const activities = ref([])
    const loading = ref(false)
    const majorChart = ref(null)
    const roleChart = ref(null)
    let majorChartInstance = null
    let roleChartInstance = null

    const loadStatistics = async () => {
      try {
        const response = await axios.get('/api/admin/statistics')
        statistics.value = response.data
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    }

    const loadActivities = async () => {
      loading.value = true
      try {
        const response = await axios.get('/api/admin/activities')
        activities.value = response.data
      } catch (error) {
        console.error('加载活动记录失败:', error)
      } finally {
        loading.value = false
      }
    }

    const loadMajorStatistics = async () => {
      try {
        const response = await axios.get('/api/admin/statistics/majors')
        const majorData = response.data
        
        if (majorChartInstance) {
          majorChartInstance.destroy()
        }

        const ctx = majorChart.value.getContext('2d')
        majorChartInstance = new Chart(ctx, {
          type: 'doughnut',
          data: {
            labels: majorData.map(item => item.majorName),
            datasets: [{
              data: majorData.map(item => item.count),
              backgroundColor: [
                '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0',
                '#9966FF', '#FF9F40', '#FF6384'
              ]
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: 'bottom'
              }
            }
          }
        })
      } catch (error) {
        console.error('加载专业统计失败:', error)
      }
    }

    const loadRoleStatistics = async () => {
      try {
        const response = await axios.get('/api/admin/statistics/roles')
        const roleData = response.data
        
        if (roleChartInstance) {
          roleChartInstance.destroy()
        }

        const ctx = roleChart.value.getContext('2d')
        roleChartInstance = new Chart(ctx, {
          type: 'pie',
          data: {
            labels: roleData.map(item => item.roleName),
            datasets: [{
              data: roleData.map(item => item.count),
              backgroundColor: [
                '#36A2EB', '#FFCE56', '#FF6384'
              ]
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: 'bottom'
              }
            }
          }
        })
      } catch (error) {
        console.error('加载角色统计失败:', error)
      }
    }

    const refreshData = async () => {
      await Promise.all([
        loadStatistics(),
        loadActivities(),
        loadMajorStatistics(),
        loadRoleStatistics()
      ])
    }

    const getActivityIcon = (type) => {
      const iconMap = {
        'user_login': 'fas fa-sign-in-alt',
        'user_register': 'fas fa-user-plus',
        'course_create': 'fas fa-plus-circle',
        'course_review': 'fas fa-check-circle',
        'user_status_change': 'fas fa-user-edit'
      }
      return iconMap[type] || 'fas fa-info-circle'
    }

    const formatTime = (timestamp) => {
      if (!timestamp) return '未知时间'
      return new Date(timestamp).toLocaleString('zh-CN')
    }

    onMounted(async () => {
      await nextTick()
      await refreshData()
    })

    return {
      statistics,
      activities,
      loading,
      majorChart,
      roleChart,
      loadStatistics,
      loadActivities,
      loadMajorStatistics,
      loadRoleStatistics,
      refreshData,
      getActivityIcon,
      formatTime
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.active { background: linear-gradient(135deg, #28a745, #20c997); }
.stat-icon.students { background: linear-gradient(135deg, #007bff, #0056b3); }
.stat-icon.teachers { background: linear-gradient(135deg, #ffc107, #e0a800); }
.stat-icon.admins { background: linear-gradient(135deg, #dc3545, #c82333); }
.stat-icon.courses { background: linear-gradient(135deg, #17a2b8, #138496); }
.stat-icon.pending { background: linear-gradient(135deg, #fd7e14, #e55a00); }
.stat-icon.approved { background: linear-gradient(135deg, #28a745, #20c997); }

.stat-content h3 {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  color: #333;
}

.stat-content p {
  margin: 5px 0 0;
  color: #666;
  font-size: 14px;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.chart-card h3 {
  margin: 0 0 20px;
  color: #333;
  font-size: 18px;
}

.chart-container {
  height: 300px;
  position: relative;
}

.recent-activities {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.recent-activities h3 {
  margin: 0 0 20px;
  color: #333;
  font-size: 18px;
}

.loading-container, .empty-container {
  text-align: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #007bff;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
  margin: 0 auto 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.activities-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-radius: 8px;
  background: #f8f9fa;
  transition: background 0.2s;
}

.activity-item:hover {
  background: #e9ecef;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.activity-content {
  flex: 1;
}

.activity-description {
  margin: 0;
  color: #333;
  font-weight: 500;
}

.activity-time {
  font-size: 12px;
  color: #666;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-primary:hover {
  background: #0056b3;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .charts-container {
    grid-template-columns: 1fr;
  }
  
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
}
</style> 