<template>
  <div class="user-statistics">
    <div class="row g-4">
      <!-- 练习统计卡片 -->
      <div class="col-md-6">
        <div class="card border-0 shadow-sm rounded-3 practice-card" @click="goToPracticeStatistics">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
              <i class="fa fa-code me-2"></i>
              练习统计
            </h5>
          </div>
          <div class="card-body">
            <div class="row g-3">
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-primary mb-1">{{ stats.completed || 0 }}</div>
                  <div class="text-muted small">已完成题目</div>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-success mb-1">{{ stats.accuracy || 0 }}%</div>
                  <div class="text-muted small">正确率</div>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-info mb-1">{{ formatTime(stats.practiceTime || 0) }}</div>
                  <div class="text-muted small">练习时长</div>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-warning mb-1">{{ stats.continuousDays || 0 }}</div>
                  <div class="text-muted small">连续天数</div>
                </div>
              </div>
            </div>
            <div class="mt-3">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="text-muted">总题目数</span>
                <span class="fw-medium">{{ stats.totalProblems || 0 }}</span>
              </div>
              <div class="progress" style="height: 8px;">
                <div 
                  class="progress-bar bg-primary" 
                  :style="{ width: getProgressPercentage() + '%' }"
                ></div>
              </div>
            </div>
            <div class="mt-3 text-center">
              <small class="text-muted">
                <i class="fa fa-arrow-right me-1"></i>点击查看详细记录
              </small>
            </div>
          </div>
        </div>
      </div>

      <!-- 学习统计卡片 -->
      <div class="col-md-6">
        <div class="card border-0 shadow-sm rounded-3 learning-card" @click="goToLearningStatistics">
          <div class="card-header bg-success text-white">
            <h5 class="mb-0">
              <i class="fa fa-graduation-cap me-2"></i>
              学习统计
            </h5>
          </div>
          <div class="card-body">
            <div class="row g-3">
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-success mb-1">{{ learningStats.totalStudyHours || 0 }}</div>
                  <div class="text-muted small">学习时长(小时)</div>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-info mb-1">{{ learningStats.completedCourses || 0 }}</div>
                  <div class="text-muted small">完成课程</div>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-warning mb-1">{{ learningStats.continuousDays || 0 }}</div>
                  <div class="text-muted small">连续学习</div>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center p-3 border rounded">
                  <div class="h4 text-primary mb-1">{{ getLearningLevel() }}</div>
                  <div class="text-muted small">学习等级</div>
                </div>
              </div>
            </div>
            <div class="mt-3 text-center">
              <small class="text-muted">
                <i class="fa fa-arrow-right me-1"></i>点击查看详细记录
              </small>
            </div>
          </div>
        </div>
      </div>

      <!-- 详细统计信息 -->
      <div class="col-12">
        <div class="card border-0 shadow-sm rounded-3">
          <div class="card-header bg-light">
            <h5 class="mb-0">
              <i class="fa fa-chart-bar me-2"></i>
              详细统计
            </h5>
          </div>
          <div class="card-body">
            <div class="row g-4">
              <!-- 练习趋势 -->
              <div class="col-md-6">
                <h6 class="text-muted mb-3">练习趋势</h6>
                <div class="d-flex align-items-center mb-2">
                  <span class="text-muted small me-2">本周练习</span>
                  <div class="flex-grow-1">
                    <div class="progress" style="height: 6px;">
                      <div class="progress-bar bg-primary" style="width: 75%"></div>
                    </div>
                  </div>
                  <span class="text-muted small ms-2">75%</span>
                </div>
                <div class="d-flex align-items-center mb-2">
                  <span class="text-muted small me-2">本月练习</span>
                  <div class="flex-grow-1">
                    <div class="progress" style="height: 6px;">
                      <div class="progress-bar bg-success" style="width: 60%"></div>
                    </div>
                  </div>
                  <span class="text-muted small ms-2">60%</span>
                </div>
              </div>

              <!-- 成就徽章 -->
              <div class="col-md-6">
                <h6 class="text-muted mb-3">成就徽章</h6>
                <div class="d-flex flex-wrap gap-2">
                  <span class="badge bg-primary p-2">
                    <i class="fa fa-star me-1"></i>
                    初学者
                  </span>
                  <span class="badge bg-success p-2" v-if="stats.completed >= 10">
                    <i class="fa fa-trophy me-1"></i>
                    练习达人
                  </span>
                  <span class="badge bg-warning p-2" v-if="stats.continuousDays >= 7">
                    <i class="fa fa-fire me-1"></i>
                    坚持不懈
                  </span>
                  <span class="badge bg-info p-2" v-if="stats.accuracy >= 80">
                    <i class="fa fa-bullseye me-1"></i>
                    精准射手
                  </span>
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

export default {
  name: 'UserStatistics',
  data() {
    return {
      stats: {
        completed: 0,
        accuracy: 0,
        practiceTime: 0,
        totalProblems: 0,
        continuousDays: 0
      },
      learningStats: {
        totalStudyHours: 0,
        completedCourses: 0,
        continuousDays: 0,
        login: false
      }
    };
  },
  async mounted() {
    await this.fetchStatistics();
  },
  methods: {
    async fetchStatistics() {
      try {
        // 获取练习统计
        const practiceRes = await axios.get('/api/user/statistics', { withCredentials: true });
        this.stats = practiceRes.data;

        // 获取学习统计
        const learningRes = await axios.get('/api/user/learning-statistics', { withCredentials: true });
        this.learningStats = learningRes.data;
      } catch (error) {
        console.error('获取统计数据失败:', error);
      }
    },
    formatTime(minutes) {
      if (minutes < 60) {
        return `${minutes}分钟`;
      }
      const hours = Math.floor(minutes / 60);
      const remainingMinutes = minutes % 60;
      return `${hours}小时${remainingMinutes}分钟`;
    },
    getProgressPercentage() {
      if (!this.stats.totalProblems) return 0;
      return Math.round((this.stats.completed / this.stats.totalProblems) * 100);
    },
    getLearningLevel() {
      const hours = this.learningStats.totalStudyHours || 0;
      if (hours < 10) return '初学者';
      if (hours < 50) return '进阶者';
      if (hours < 100) return '熟练者';
      return '专家';
    },
    goToPracticeStatistics() {
      this.$router.push('/practice-statistics');
    },
    goToLearningStatistics() {
      this.$router.push('/learning-statistics');
    }
  }
};
</script>

<style scoped>
.user-statistics .card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.user-statistics .card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1) !important;
}

.practice-card,
.learning-card {
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.practice-card::before,
.learning-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  transition: left 0.5s;
}

.practice-card:hover::before,
.learning-card:hover::before {
  left: 100%;
}

.progress {
  border-radius: 10px;
  background-color: #f8f9fa;
}

.progress-bar {
  border-radius: 10px;
}

.badge {
  font-size: 0.8rem;
}
</style>

