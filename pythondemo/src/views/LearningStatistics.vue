<template>
  <div class="learning-statistics">
    <div class="container-fluid">
      <!-- 页面头部 -->
      <div class="page-header mb-4">
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <h1 class="page-title mb-2">
              <i class="fa fa-graduation-cap text-success me-3"></i>
              学习统计详情
            </h1>
            <p class="text-muted mb-0">查看你的详细学习记录和课程进度</p>
          </div>
          <button class="btn btn-outline-secondary" @click="$router.go(-1)">
            <i class="fa fa-arrow-left me-2"></i>返回
          </button>
        </div>
      </div>

      <!-- 统计概览卡片 -->
      <div class="row g-4 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-clock text-success fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ overview.totalStudyHours }}h</h4>
              <p class="text-muted mb-0">总学习时长</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-book text-primary fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ overview.completedCourses }}</h4>
              <p class="text-muted mb-0">完成课程</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="bg-warning bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-fire text-warning fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ overview.continuousDays }}</h4>
              <p class="text-muted mb-0">连续学习</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-star text-info fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ getLearningLevel() }}</h4>
              <p class="text-muted mb-0">学习等级</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 学习进度图表 -->
      <div class="row g-4 mb-4">
        <div class="col-md-8">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-light">
              <h5 class="mb-0">
                <i class="fa fa-chart-line me-2"></i>
                学习时长趋势
              </h5>
            </div>
            <div class="card-body">
              <canvas ref="learningChart" height="300"></canvas>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-light">
              <h5 class="mb-0">
                <i class="fa fa-pie-chart me-2"></i>
                学习分布
              </h5>
            </div>
            <div class="card-body">
              <canvas ref="distributionChart" height="300"></canvas>
            </div>
          </div>
        </div>
      </div>

      <!-- 课程进度列表 -->
      <div class="card border-0 shadow-sm">
        <div class="card-header bg-light">
          <h5 class="mb-0">
            <i class="fa fa-list me-2"></i>
            课程学习进度
          </h5>
        </div>
        <div class="card-body p-0">
          <div v-if="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
            <p class="mt-3 text-muted">正在加载课程进度...</p>
          </div>
          
          <div v-else-if="courseProgress.length === 0" class="text-center py-5">
            <i class="fa fa-book fa-3x text-muted mb-3"></i>
            <p class="text-muted">暂无课程学习记录</p>
            <button class="btn btn-primary" @click="$router.push('/courses')">
              <i class="fa fa-plus me-2"></i>开始学习课程
            </button>
          </div>
          
          <div v-else class="course-progress-list">
            <div v-for="course in courseProgress" :key="course.id" class="course-progress-item">
              <div class="row align-items-center">
                <div class="col-md-8">
                  <div class="d-flex align-items-center">
                    <div class="course-image me-3">
                      <img :src="course.coverImage || '/default-course.jpg'" :alt="course.title" class="rounded">
                    </div>
                    <div class="course-info">
                      <h6 class="course-title mb-1">{{ course.title }}</h6>
                      <p class="course-desc text-muted mb-2">{{ course.description }}</p>
                      <div class="course-meta">
                        <span class="badge bg-light text-dark me-2">
                          <i class="fa fa-clock me-1"></i>{{ course.duration }}分钟
                        </span>
                        <span class="badge bg-light text-dark me-2">
                          <i class="fa fa-play me-1"></i>{{ course.lessons }}课时
                        </span>
                        <span class="badge bg-light text-dark">
                          <i class="fa fa-signal me-1"></i>{{ course.difficulty }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="progress-info text-end">
                    <div class="progress mb-2" style="height: 8px;">
                      <div class="progress-bar bg-success" :style="{ width: course.progress + '%' }"></div>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                      <span class="text-muted small">进度</span>
                      <span class="fw-medium">{{ course.progress }}%</span>
                    </div>
                    <div class="mt-2">
                      <button class="btn btn-sm btn-primary" @click="continueLearning(course)">
                        <i class="fa fa-play me-1"></i>继续学习
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 学习成就 -->
      <div class="row g-4 mt-4">
        <div class="col-md-6">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-light">
              <h5 class="mb-0">
                <i class="fa fa-trophy me-2"></i>
                学习成就
              </h5>
            </div>
            <div class="card-body">
              <div class="achievements-grid">
                <div v-for="achievement in achievements" :key="achievement.id" 
                     class="achievement-item" :class="{ unlocked: achievement.unlocked }">
                  <div class="achievement-icon">
                    <i :class="achievement.icon"></i>
                  </div>
                  <div class="achievement-info">
                    <h6 class="achievement-title">{{ achievement.title }}</h6>
                    <p class="achievement-desc">{{ achievement.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="col-md-6">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-light">
              <h5 class="mb-0">
                <i class="fa fa-calendar me-2"></i>
                学习日历
              </h5>
            </div>
            <div class="card-body">
              <div class="learning-calendar">
                <div class="calendar-grid">
                  <div v-for="day in learningCalendar" :key="day.date" 
                       class="calendar-day" :class="getCalendarDayClass(day)">
                    <span class="day-date">{{ day.day }}</span>
                    <span v-if="day.studyTime > 0" class="study-indicator"></span>
                  </div>
                </div>
                <div class="calendar-legend mt-3">
                  <div class="d-flex justify-content-center">
                    <div class="legend-item me-3">
                      <span class="legend-color bg-light"></span>
                      <small class="text-muted">无学习</small>
                    </div>
                    <div class="legend-item me-3">
                      <span class="legend-color bg-success"></span>
                      <small class="text-muted">学习1-30分钟</small>
                    </div>
                    <div class="legend-item me-3">
                      <span class="legend-color bg-primary"></span>
                      <small class="text-muted">学习30-60分钟</small>
                    </div>
                    <div class="legend-item">
                      <span class="legend-color bg-warning"></span>
                      <small class="text-muted">学习60分钟以上</small>
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
import axios from 'axios';
import Chart from 'chart.js/auto';

export default {
  name: 'LearningStatistics',
  data() {
    return {
      loading: false,
      overview: {
        totalStudyHours: 0,
        completedCourses: 0,
        continuousDays: 0
      },
      courseProgress: [],
      achievements: [
        {
          id: 1,
          title: '初学者',
          description: '完成第一门课程',
          icon: 'fa fa-star',
          unlocked: true
        },
        {
          id: 2,
          title: '坚持不懈',
          description: '连续学习7天',
          icon: 'fa fa-fire',
          unlocked: false
        },
        {
          id: 3,
          title: '知识探索者',
          description: '完成10门课程',
          icon: 'fa fa-compass',
          unlocked: false
        },
        {
          id: 4,
          title: '学习达人',
          description: '总学习时长达到100小时',
          icon: 'fa fa-crown',
          unlocked: false
        }
      ],
      learningCalendar: [],
      learningChart: null,
      distributionChart: null
    };
  },
  async mounted() {
    this.userId = localStorage.getItem('userId');
    if (!this.userId) {
      this.$router.push('/auth');
      return;
    }
    
    await this.loadOverview();
    await this.loadCourseProgress();
    await this.generateLearningCalendar();
    this.initCharts();
  },
  methods: {
    async loadOverview() {
      try {
        const response = await axios.get('/api/user/learning-statistics', { withCredentials: true });
        this.overview = response.data;
      } catch (error) {
        console.error('获取学习统计失败:', error);
      }
    },
    
    async loadCourseProgress() {
      this.loading = true;
      try {
        // 这里应该调用实际的API，暂时使用模拟数据
        this.courseProgress = [
          {
            id: 1,
            title: 'Python基础入门',
            description: '从零开始学习Python编程语言的基础知识',
            coverImage: '/course-1.jpg',
            duration: 120,
            lessons: 12,
            difficulty: '初级',
            progress: 85
          },
          {
            id: 2,
            title: 'Python数据结构',
            description: '深入学习Python中的各种数据结构',
            coverImage: '/course-2.jpg',
            duration: 180,
            lessons: 15,
            difficulty: '中级',
            progress: 60
          },
          {
            id: 3,
            title: 'Python算法设计',
            description: '掌握常用算法和设计模式',
            coverImage: '/course-3.jpg',
            duration: 240,
            lessons: 20,
            difficulty: '高级',
            progress: 30
          }
        ];
      } catch (error) {
        console.error('获取课程进度失败:', error);
      } finally {
        this.loading = false;
      }
    },
    
    generateLearningCalendar() {
      // 生成最近30天的学习日历数据
      const calendar = [];
      const today = new Date();
      
      for (let i = 29; i >= 0; i--) {
        const date = new Date(today);
        date.setDate(date.getDate() - i);
        
        calendar.push({
          date: date.toISOString().split('T')[0],
          day: date.getDate(),
          studyTime: Math.floor(Math.random() * 120) // 模拟学习时间
        });
      }
      
      this.learningCalendar = calendar;
    },
    
    initCharts() {
      // 学习时长趋势图
      const learningCtx = this.$refs.learningChart.getContext('2d');
      this.learningChart = new Chart(learningCtx, {
        type: 'line',
        data: {
          labels: ['第1周', '第2周', '第3周', '第4周'],
          datasets: [{
            label: '学习时长(小时)',
            data: [5, 8, 12, 10],
            borderColor: '#28a745',
            backgroundColor: 'rgba(40, 167, 69, 0.1)',
            tension: 0.4,
            fill: true
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: '小时'
              }
            }
          }
        }
      });
      
      // 学习分布饼图
      const distributionCtx = this.$refs.distributionChart.getContext('2d');
      this.distributionChart = new Chart(distributionCtx, {
        type: 'doughnut',
        data: {
          labels: ['基础课程', '进阶课程', '实战项目'],
          datasets: [{
            data: [40, 35, 25],
            backgroundColor: ['#007bff', '#28a745', '#ffc107'],
            borderWidth: 0
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
      });
    },
    
    getLearningLevel() {
      const hours = this.overview.totalStudyHours || 0;
      if (hours < 10) return '初学者';
      if (hours < 50) return '进阶者';
      if (hours < 100) return '熟练者';
      return '专家';
    },
    
    continueLearning(course) {
      this.$router.push(`/course/${course.id}`);
    },
    
    getCalendarDayClass(day) {
      if (day.studyTime === 0) return 'no-study';
      if (day.studyTime <= 30) return 'study-light';
      if (day.studyTime <= 60) return 'study-medium';
      return 'study-heavy';
    }
  }
};
</script>

<style scoped>
.learning-statistics {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 2rem 0;
}

.page-title {
  color: #2c3e50;
  font-weight: 600;
}

.card {
  border-radius: 12px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1) !important;
}

.course-progress-item {
  padding: 1.5rem;
  border-bottom: 1px solid #e9ecef;
  transition: background-color 0.2s ease;
}

.course-progress-item:hover {
  background-color: #f8f9fa;
}

.course-progress-item:last-child {
  border-bottom: none;
}

.course-image img {
  width: 80px;
  height: 60px;
  object-fit: cover;
}

.course-title {
  color: #2c3e50;
  font-weight: 600;
}

.course-desc {
  font-size: 0.875rem;
  line-height: 1.4;
}

.achievements-grid {
  display: grid;
  gap: 1rem;
}

.achievement-item {
  display: flex;
  align-items: center;
  padding: 1rem;
  border-radius: 8px;
  background-color: #f8f9fa;
  opacity: 0.5;
  transition: all 0.3s ease;
}

.achievement-item.unlocked {
  opacity: 1;
  background-color: #e8f5e8;
  border: 1px solid #d4edda;
}

.achievement-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
}

.achievement-item.unlocked .achievement-icon {
  background-color: #28a745;
}

.achievement-title {
  margin: 0;
  font-weight: 600;
  color: #2c3e50;
}

.achievement-desc {
  margin: 0;
  font-size: 0.875rem;
  color: #6c757d;
}

.learning-calendar {
  text-align: center;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
  margin-bottom: 1rem;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  background-color: #f8f9fa;
  position: relative;
  font-size: 0.875rem;
}

.calendar-day.study-light {
  background-color: #d4edda;
}

.calendar-day.study-medium {
  background-color: #cce7ff;
}

.calendar-day.study-heavy {
  background-color: #fff3cd;
}

.study-indicator {
  position: absolute;
  bottom: 2px;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background-color: #28a745;
}

.calendar-legend .legend-item {
  display: flex;
  align-items: center;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
  margin-right: 0.5rem;
}

.progress {
  border-radius: 10px;
  background-color: #e9ecef;
}

.progress-bar {
  border-radius: 10px;
}
</style>
