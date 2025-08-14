<template>
  <div class="student-progress">
    <div class="page-header">
      <h1 class="page-title">学习统计</h1>
      <p class="page-subtitle">查看你的学习进度和数据分析</p>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalStudyTime }}</div>
        <div class="stat-label">总学习时长(小时)</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.completedCourses }}</div>
        <div class="stat-label">已完成课程</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.averageScore }}</div>
        <div class="stat-label">平均成绩</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.studyDays }}</div>
        <div class="stat-label">连续学习天数</div>
      </div>
    </div>

    <!-- 学习进度图表 -->
    <div class="chart-section">
      <h2 class="section-title">学习进度趋势</h2>
      <div class="chart-container">
        <canvas ref="progressChart"></canvas>
      </div>
    </div>

    <!-- 课程进度 -->
    <div class="course-progress-section">
      <h2 class="section-title">课程进度</h2>
      <div class="course-progress-list">
        <div v-for="course in courseProgress" :key="course.id" class="course-progress-item">
          <div class="course-info">
            <h3 class="course-title">{{ course.title }}</h3>
            <p class="course-desc">{{ course.description }}</p>
          </div>
          <div class="progress-info">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: course.progress + '%' }"></div>
            </div>
            <span class="progress-text">{{ course.progress }}%</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js/auto'

export default {
  name: 'StudentProgress',
  data() {
    return {
      stats: {
        totalStudyTime: 48,
        completedCourses: 3,
        averageScore: 85,
        studyDays: 15
      },
      courseProgress: [
        {
          id: 1,
          title: 'Python基础',
          description: 'Python编程基础语法和概念',
          progress: 75
        },
        {
          id: 2,
          title: '数据分析',
          description: '使用Python进行数据分析',
          progress: 45
        },
        {
          id: 3,
          title: 'Web开发',
          description: 'Flask框架Web开发',
          progress: 30
        }
      ]
    }
  },
  mounted() {
    this.initChart()
  },
  methods: {
    initChart() {
      const ctx = this.$refs.progressChart.getContext('2d')
      new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['第1周', '第2周', '第3周', '第4周', '第5周', '第6周'],
          datasets: [{
            label: '学习时长(小时)',
            data: [5, 8, 6, 10, 12, 9],
            borderColor: '#667eea',
            backgroundColor: 'rgba(102, 126, 234, 0.1)',
            tension: 0.4
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            }
          },
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.student-progress {
  padding: 0;
}

.page-header {
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.page-subtitle {
  color: #666;
  font-size: 1.1rem;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-item {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
}

.chart-section {
  margin-bottom: 2rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 1rem;
}

.chart-container {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.course-progress-section {
  margin-bottom: 2rem;
}

.course-progress-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.course-progress-item {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.course-info {
  flex: 1;
}

.course-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.course-desc {
  color: #666;
  font-size: 0.9rem;
}

.progress-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  min-width: 200px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: width 0.3s ease;
}

.progress-text {
  font-weight: 600;
  color: #667eea;
  min-width: 40px;
}

@media (max-width: 768px) {
  .course-progress-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .progress-info {
    width: 100%;
  }
}
</style> 