<template>
  <div class="student-dashboard">
    <div class="dashboard-header">
      <h1 class="dashboard-title">学习概览</h1>
      <p class="dashboard-subtitle">欢迎回来，{{ userInfo.nickname || '同学' }}！</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-book"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalCourses }}</div>
          <div class="stat-label">已选课程</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-tasks"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.pendingAssignments }}</div>
          <div class="stat-label">待完成作业</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-clock"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.studyHours }}</div>
          <div class="stat-label">学习时长(小时)</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-star"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.averageScore }}</div>
          <div class="stat-label">平均成绩</div>
        </div>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="dashboard-section">
      <h2 class="section-title">最近活动</h2>
      <div class="activity-list">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-icon">
            <i :class="activity.icon"></i>
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-time">{{ activity.time }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 推荐课程 -->
    <div class="dashboard-section">
      <h2 class="section-title">推荐课程</h2>
      <div class="course-grid">
        <div v-for="course in recommendedCourses" :key="course.id" class="course-card">
          <div class="course-image">
            <img :src="course.image" :alt="course.title">
          </div>
          <div class="course-content">
            <h3 class="course-title">{{ course.title }}</h3>
            <p class="course-desc">{{ course.description }}</p>
            <div class="course-meta">
              <span class="course-teacher">{{ course.teacher }}</span>
              <span class="course-duration">{{ course.duration }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StudentDashboard',
  data() {
    return {
      userInfo: {
        nickname: '张三'
      },
      stats: {
        totalCourses: 5,
        pendingAssignments: 3,
        studyHours: 24,
        averageScore: 85
      },
      recentActivities: [
        {
          id: 1,
          title: '完成了《Python基础》第3章练习',
          time: '2小时前',
          icon: 'fas fa-check-circle'
        },
        {
          id: 2,
          title: '提交了《数据分析》作业',
          time: '1天前',
          icon: 'fas fa-upload'
        },
        {
          id: 3,
          title: '开始学习《机器学习入门》',
          time: '2天前',
          icon: 'fas fa-play'
        }
      ],
      recommendedCourses: [
        {
          id: 1,
          title: 'Python高级编程',
          description: '深入学习Python的高级特性和最佳实践',
          teacher: '李老师',
          duration: '8周',
          image: 'https://picsum.photos/300/200?random=1'
        },
        {
          id: 2,
          title: 'Web开发实战',
          description: '从零开始学习现代Web开发技术',
          teacher: '王老师',
          duration: '12周',
          image: 'https://picsum.photos/300/200?random=2'
        }
      ]
    }
  }
}
</script>

<style scoped>
.student-dashboard {
  padding: 0;
}

.dashboard-header {
  margin-bottom: 2rem;
}

.dashboard-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.dashboard-subtitle {
  color: #666;
  font-size: 1.1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  line-height: 1;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
  margin-top: 0.25rem;
}

.dashboard-section {
  margin-bottom: 2rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 1rem;
}

.activity-list {
  background: white;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #667eea;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: 500;
  color: #333;
  margin-bottom: 0.25rem;
}

.activity-time {
  font-size: 0.9rem;
  color: #666;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.course-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.course-card:hover {
  transform: translateY(-4px);
}

.course-image {
  height: 150px;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-content {
  padding: 1rem;
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
  margin-bottom: 1rem;
  line-height: 1.4;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: #999;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .course-grid {
    grid-template-columns: 1fr;
  }
}
</style> 