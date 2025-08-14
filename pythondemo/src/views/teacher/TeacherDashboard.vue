<template>
  <div class="teacher-dashboard">
    <div class="dashboard-header">
      <h1 class="dashboard-title">教学概览</h1>
      <p class="dashboard-subtitle">欢迎回来，{{ teacherInfo.name || '老师' }}！</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-graduation-cap"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalCourses }}</div>
          <div class="stat-label">开设课程</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-users"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalStudents }}</div>
          <div class="stat-label">学生总数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-tasks"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.pendingAssignments }}</div>
          <div class="stat-label">待批改作业</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-star"></i>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.averageRating }}</div>
          <div class="stat-label">平均评分</div>
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

    <!-- 课程列表 -->
    <div class="dashboard-section">
      <h2 class="section-title">我的课程</h2>
      <div class="course-grid">
        <div v-for="course in courses" :key="course.id" class="course-card">
          <div class="course-image">
            <img :src="course.image" :alt="course.title">
          </div>
          <div class="course-content">
            <h3 class="course-title">{{ course.title }}</h3>
            <p class="course-desc">{{ course.description }}</p>
            <div class="course-meta">
              <span class="course-students">{{ course.studentCount }}名学生</span>
              <span class="course-status">{{ course.status }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TeacherDashboard',
  data() {
    return {
      teacherInfo: {
        name: '李老师'
      },
      stats: {
        totalCourses: 3,
        totalStudents: 45,
        pendingAssignments: 8,
        averageRating: 4.8
      },
      recentActivities: [
        {
          id: 1,
          title: '批改了《Python基础》作业',
          time: '1小时前',
          icon: 'fas fa-check-circle'
        },
        {
          id: 2,
          title: '发布了新的作业',
          time: '2小时前',
          icon: 'fas fa-plus'
        },
        {
          id: 3,
          title: '更新了课程内容',
          time: '1天前',
          icon: 'fas fa-edit'
        }
      ],
      courses: [
        {
          id: 1,
          title: 'Python基础编程',
          description: 'Python编程基础语法和概念',
          studentCount: 15,
          status: '进行中',
          image: 'https://picsum.photos/300/200?random=3'
        },
        {
          id: 2,
          title: '数据分析入门',
          description: '使用Python进行数据分析',
          studentCount: 20,
          status: '进行中',
          image: 'https://picsum.photos/300/200?random=4'
        },
        {
          id: 3,
          title: 'Web开发实战',
          description: 'Flask框架Web开发',
          studentCount: 10,
          status: '即将开始',
          image: 'https://picsum.photos/300/200?random=5'
        }
      ]
    }
  }
}
</script>

<style scoped>
.teacher-dashboard {
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
  background: linear-gradient(135deg, #7b1fa2 0%, #9c27b0 100%);
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
  color: #7b1fa2;
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