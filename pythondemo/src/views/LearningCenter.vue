<template>
<div>
  <!-- 学习中心内容 -->
  <main class="container py-4">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="fw-bold text-dark mb-2">我的学习中心</h2>
      <p class="text-muted mb-0">跟踪学习进度，管理学习计划</p>
    </div>

    <!-- 学习概览卡片 -->
    <div class="row g-4 mb-4">
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-clock text-primary fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">18.5小时</h4>
          <p class="text-muted mb-0">总学习时长</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-check-circle text-success fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">3门</h4>
          <p class="text-muted mb-0">完成课程</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-warning bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-tasks text-warning fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">24题</h4>
          <p class="text-muted mb-0">完成练习</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-calendar text-info fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">7天</h4>
          <p class="text-muted mb-0">连续学习</p>
        </div>
      </div>
    </div>

    <div class="row g-4">
      <!-- 学习进度树 -->
      <div class="col-lg-8">
        <div class="learning-tree-cards">
          <div v-for="(group, groupName) in groupedPoints" :key="groupName" class="tree-card">
            <div class="tree-card-header">
              <span class="tree-card-title">
                <i class="fa" :class="groupName === '基础' ? 'fa-check-circle text-success' : 'fa-dot-circle-o text-warning'"></i>
                <span class="ms-2">{{ groupName === '基础' ? 'Python基础' : 'Python进阶' }}</span>
              </span>
              <!-- 状态标签可选 -->
              <!-- <span class="tree-status" :class="groupName === '基础' ? 'done' : 'doing'">{{ groupName === '基础' ? '已完成' : '进行中' }}</span> -->
            </div>
            <div class="tree-card-body">
              <ul class="tree-card-list">
                <li v-for="point in group" :key="point.title" class="tree-card-list-item">
                  <span class="tree-point-icon">
                    <i class="fa fa-check-circle"></i>
                  </span>
                  <a class="tree-card-link" @click="$router.push(`/learn-detail?id=${encodeURIComponent(point.title)}`)">
                    {{ point.title }}
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 最近学习记录 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h3 class="fw-bold text-dark mb-4">最近学习记录</h3>
          <div class="table-responsive">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>课程名称</th>
                  <th>学习时长</th>
                  <th>进度</th>
                  <th>最后学习</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <div class="d-flex align-items-center">
                      <img src="https://picsum.photos/60/40?random=1" class="rounded me-2" width="60" height="40" alt="课程图片">
                      <div>
                        <div class="fw-medium">Python基础入门</div>
                        <div class="text-muted small">第3章：函数与模块</div>
                      </div>
                    </div>
                  </td>
                  <td>45分钟</td>
                  <td>
                    <div class="progress" style="height:6px;">
                      <div class="progress-bar bg-success" style="width:100%"></div>
                    </div>
                    <small class="text-muted">100%</small>
                  </td>
                  <td>2小时前</td>
                  <td>
                    <button class="btn btn-outline-primary btn-sm">继续学习</button>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="d-flex align-items-center">
                      <img src="https://picsum.photos/60/40?random=2" class="rounded me-2" width="60" height="40" alt="课程图片">
                      <div>
                        <div class="fw-medium">Python进阶实战</div>
                        <div class="text-muted small">第2章：文件操作</div>
                      </div>
                    </div>
                  </td>
                  <td>30分钟</td>
                  <td>
                    <div class="progress" style="height:6px;">
                      <div class="progress-bar bg-warning" style="width:65%"></div>
                    </div>
                    <small class="text-muted">65%</small>
                  </td>
                  <td>昨天</td>
                  <td>
                    <button class="btn btn-outline-primary btn-sm">继续学习</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 右侧统计和计划 -->
      <div class="col-lg-4">
        <!-- 学习统计图表 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <h3 class="fw-bold text-dark mb-4">学习时间分布</h3>
          <div style="height:200px;" class="bg-light rounded-3 d-flex align-items-center justify-content-center text-muted">
            <div class="text-center">
              <i class="fa fa-chart-bar fa-3x mb-2"></i>
              <p class="mb-0">学习时间统计图表</p>
            </div>
          </div>
        </div>

        <!-- 学习计划 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="fw-bold text-dark mb-0">今日学习计划</h3>
            <button class="btn btn-outline-primary btn-sm rounded-pill">编辑</button>
          </div>
          <div class="space-y-2">
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="rounded-circle bg-primary d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                <i class="fa fa-play"></i>
              </div>
              <div class="flex-grow-1">
                <div class="fw-medium">完成文件操作章节</div>
                <div class="text-muted small">预计30分钟</div>
              </div>
              <span class="badge bg-primary">进行中</span>
            </div>
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="rounded-circle bg-secondary d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                <i class="fa fa-clock-o"></i>
              </div>
              <div class="flex-grow-1">
                <div class="fw-medium">练习题目5道</div>
                <div class="text-muted small">预计20分钟</div>
              </div>
              <span class="badge bg-secondary">待完成</span>
            </div>
          </div>
        </div>

        <!-- 学习成就 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h3 class="fw-bold text-dark mb-4">学习成就</h3>
          <div class="row g-2">
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-trophy text-warning fa-2x mb-2"></i>
                <div class="fw-medium">连续学习7天</div>
                <div class="text-muted small">坚持不懈</div>
              </div>
            </div>
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-star text-warning fa-2x mb-2"></i>
                <div class="fw-medium">完成3门课程</div>
                <div class="text-muted small">学习达人</div>
              </div>
            </div>
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-clock text-info fa-2x mb-2"></i>
                <div class="fw-medium">学习18.5小时</div>
                <div class="text-muted small">时间管理</div>
              </div>
            </div>
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-check-circle text-success fa-2x mb-2"></i>
                <div class="fw-medium">完成24题</div>
                <div class="text-muted small">练习高手</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</div>
</template>

<script>
export default {
  name: 'LearningCenter',
  data() {
    return {
      learningStats: {
        totalStudyHours: 0,
        completedCourses: 0,
        continuousDays: 0,
        login: false
      },
      points: [],
      groupedPoints: { 基础: [], 进阶: [] }
    }
  },
  async mounted() {
    const res = await this.$axios.get('/api/knowledge/points');
    this.points = res.data;
    this.groupedPoints = {
      基础: this.points.filter(p => p.stage && p.stage.startsWith('1.')),
      进阶: this.points.filter(p => p.stage && p.stage.startsWith('2.'))
    };
  },
  methods: {
    async fetchLearningStatistics() {
      try {
        const res = await this.$axios.get('/api/user/learning-statistics', { withCredentials: true });
        this.learningStats = res.data;
      } catch (e) {
        // 失败时全部为0
        this.learningStats = { totalStudyHours: 0, completedCourses: 0, continuousDays: 0, login: false };
      }
    },
    goToDetail(id) {
      this.$router.push({ name: 'LearnDetial', query: { id } });
    }
  }
}
</script>

<style scoped>
.learning-tree {
  position: relative;
}

.learning-node {
  position: relative;
}

.learning-node:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 20px;
  top: 60px;
  bottom: -20px;
  width: 2px;
  background: #e9ecef;
  z-index: 1;
}

.learning-node.completed::after {
  background: #28a745;
}

.learning-tree-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.tree-card {
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 4px 24px rgba(37,99,235,0.08), 0 1.5px 4px rgba(0,0,0,0.04);
  border: none;
  padding: 0;
  min-height: 180px;
  width: 100%;
  margin-bottom: 0;
  transition: box-shadow 0.2s;
}

.tree-card:hover {
  box-shadow: 0 8px 32px rgba(37,99,235,0.13), 0 2px 8px rgba(0,0,0,0.06);
}

.tree-card-header {
  padding: 26px 32px 12px 32px;
  border-bottom: 1px solid #f3f6fd;
  font-size: 1.35rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  background: #f8fafc;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
}

.tree-card-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  color: #22223b;
}

.tree-card-title .fa {
  font-size: 2rem;
  margin-right: 10px;
}

.tree-status {
  font-size: 1rem;
  font-weight: 600;
  border-radius: 12px;
  padding: 2px 14px;
  margin-left: 18px;
}

.tree-status.done {
  background: #e6f4ea;
  color: #22c55e;
}

.tree-status.doing {
  background: #fff7e6;
  color: #f59e42;
}

.tree-card-body {
  padding: 0 32px 24px 32px;
  max-height: 320px;
  min-height: 180px;
  overflow-y: auto;
  background: #f8fafc;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
}

.tree-card-list {
  list-style: none;
  padding-left: 0;
  margin-bottom: 0;
}

.tree-card-list-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 1.08rem;
  border-bottom: 1px solid #e9ecef;
  padding: 10px 0;
  transition: background 0.18s;
}

.tree-card-list-item:last-child {
  border-bottom: none;
}

.tree-card-list-item:hover {
  background: #e8f0fe;
  border-radius: 8px;
}

.tree-point-icon {
  color: #22c55e;
  font-size: 1.2rem;
  margin-right: 12px;
  flex-shrink: 0;
}

.tree-card-link {
  color: #22223b;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  transition: color 0.2s;
}

.tree-card-link:hover {
  color: #2563eb;
  text-decoration: underline;
}
</style> 