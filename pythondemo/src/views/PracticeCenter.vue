<template>
<div>
  <!-- 实践中心内容 -->
  <main class="container py-4">
    <!-- 页面标题和搜索 -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h2 class="fw-bold text-dark mb-2">实践练习中心</h2>
        <p class="text-muted mb-0">通过实战练习巩固Python编程技能</p>
      </div>
      <div class="d-flex gap-2">
        <input type="text" class="form-control rounded-pill" style="min-width:250px;" placeholder="搜索练习题...">
        <button class="btn btn-primary rounded-pill px-4">
          <i class="fa fa-search me-1"></i>搜索
        </button>
      </div>
    </div>

    <!-- 快速入口 -->
    <div class="row g-4 mb-4">
      <div class="col-md-6">
        <div class="bg-gradient-primary text-white rounded-3 p-4 h-100">
          <div class="d-flex align-items-center justify-content-between">
            <div>
              <h4 class="fw-bold mb-2">编程练习</h4>
              <p class="mb-3 opacity-75">类似力扣的在线编程练习平台</p>
              <router-link to="/problems" class="btn btn-light rounded-pill px-4">
                <i class="fa fa-code me-2"></i>开始练习
              </router-link>
            </div>
            <div class="text-end">
              <div class="h2 fw-bold mb-0">156</div>
              <div class="opacity-75">道题目</div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="bg-gradient-success text-white rounded-3 p-4 h-100">
          <div class="d-flex align-items-center justify-content-between">
            <div>
              <h4 class="fw-bold mb-2">项目实战</h4>
              <p class="mb-3 opacity-75">完整的项目开发练习</p>
              <button class="btn btn-light rounded-pill px-4">
                <i class="fa fa-rocket me-2"></i>开始项目
              </button>
            </div>
            <div class="text-end">
              <div class="h2 fw-bold mb-0">12</div>
              <div class="opacity-75">个项目</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 练习统计卡片 -->
    <div class="row g-4 mb-4">
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-check-circle text-primary fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">{{ practiceStats.completed }}题</h4>
          <p class="text-muted mb-0">已完成</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-trophy text-success fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">{{ practiceStats.accuracy }}%</h4>
          <p class="text-muted mb-0">正确率</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-warning bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-clock text-warning fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">{{ practiceStats.practiceTime }}小时</h4>
          <p class="text-muted mb-0">练习时长</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-fire text-info fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">{{ practiceStats.continuousDays }}天</h4>
          <p class="text-muted mb-0">连续刷题</p>
        </div>
      </div>
    </div>

    <div class="row g-4">
      <!-- 左侧筛选和分类 -->
      <div class="col-lg-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <h5 class="fw-bold text-dark mb-3">题目分类</h5>
          <div class="mb-4">
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="cat1" checked>
              <label class="form-check-label" for="cat1">基础语法 (45)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="cat2" checked>
              <label class="form-check-label" for="cat2">数据结构 (32)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="cat3">
              <label class="form-check-label" for="cat3">算法基础 (28)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="cat4">
              <label class="form-check-label" for="cat4">Web开发 (23)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="cat5">
              <label class="form-check-label" for="cat5">数据分析 (18)</label>
            </div>
          </div>

          <h5 class="fw-bold text-dark mb-3">难度等级</h5>
          <div class="mb-4">
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="level1" checked>
              <label class="form-check-label" for="level1">
                <span class="badge bg-success me-2">简单</span>基础 (56)
              </label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="level2" checked>
              <label class="form-check-label" for="level2">
                <span class="badge bg-warning text-dark me-2">中等</span>进阶 (67)
              </label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="level3">
              <label class="form-check-label" for="level3">
                <span class="badge bg-danger me-2">困难</span>高级 (33)
              </label>
            </div>
          </div>

          <h5 class="fw-bold text-dark mb-3">完成状态</h5>
          <div class="mb-4">
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="status1" checked>
              <label class="form-check-label" for="status1">未完成 (132)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="status2" checked>
              <label class="form-check-label" for="status2">已完成 (24)</label>
            </div>
          </div>

          <button class="btn btn-outline-primary w-100 rounded-pill">重置筛选</button>
        </div>

        <!-- 练习排行榜 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h5 class="fw-bold text-dark mb-3">练习排行榜</h5>
          <div class="space-y-2">
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="fw-bold text-warning me-2">1</div>
              <img src="https://picsum.photos/32/32?random=1" class="rounded-circle me-2" width="32" height="32" alt="用户头像">
              <div class="flex-grow-1">
                <div class="fw-medium">张三</div>
                <div class="text-muted small">156题</div>
              </div>
              <span class="badge bg-warning">金牌</span>
            </div>
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="fw-bold text-secondary me-2">2</div>
              <img src="https://picsum.photos/32/32?random=2" class="rounded-circle me-2" width="32" height="32" alt="用户头像">
              <div class="flex-grow-1">
                <div class="fw-medium">李四</div>
                <div class="text-muted small">142题</div>
              </div>
              <span class="badge bg-secondary">银牌</span>
            </div>
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="fw-bold text-warning me-2">3</div>
              <img src="https://picsum.photos/32/32?random=3" class="rounded-circle me-2" width="32" height="32" alt="用户头像">
              <div class="flex-grow-1">
                <div class="fw-medium">王五</div>
                <div class="text-muted small">128题</div>
              </div>
              <span class="badge bg-warning">铜牌</span>
            </div>
          </div>
        </div>

        <!-- 我的收藏题目 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <h5 class="fw-bold text-dark mb-3">我的收藏题目</h5>
          <div v-if="favoriteProblems.length === 0" class="text-muted">暂无收藏题目</div>
          <div v-else class="row g-2">
            <div class="col-md-6 col-lg-4" v-for="problem in favoriteProblems" :key="problem.problemId">
              <div class="problem-card" @click="$router.push('/problem/' + problem.problemId)">
                <h6 class="fw-bold mb-1">{{ problem.Title }}</h6>
                <div class="text-muted small">收藏于：{{ problem.createdAt }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧题目列表 -->
      <div class="col-lg-9">
        <!-- 排序和统计 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-muted">共找到 <strong>156</strong> 道练习题</span>
          <div class="d-flex align-items-center gap-2">
            <span class="text-muted">排序：</span>
            <select class="form-select form-select-sm" style="width:auto;">
              <option>推荐顺序</option>
              <option>难度从低到高</option>
              <option>难度从高到低</option>
              <option>完成人数最多</option>
            </select>
          </div>
        </div>

        <!-- 题目网格 -->
        <div class="row g-3">
          <div class="col-md-6 col-lg-4" v-for="problem in problems" :key="problem.id">
            <div class="problem-card" @click="goToProblem(problem.id)">
              <h5 class="fw-bold mb-2">{{ problem.title }}</h5>
              <div class="text-muted small mb-1">{{ problem.background }}</div>
              <div class="text-truncate">{{ problem.description }}</div>
              <div class="mt-2">
                <span class="badge bg-secondary me-2">{{ problem.id }}</span>
                <span class="badge bg-info">{{ problem.createTime }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <nav class="mt-4">
          <ul class="pagination justify-content-center">
            <li class="page-item disabled">
              <a class="page-link" href="#" tabindex="-1">上一页</a>
            </li>
            <li class="page-item active"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item">
              <a class="page-link" href="#">下一页</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </main>
</div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'PracticeCenter',
  data() {
    return {
      problems: [],
      favoriteProblems: [],
      userId: 1, // 实际应取当前登录用户
      practiceStats: {
        completed: 36, // 静态假数据：已完成36题
        accuracy: 85, // 静态假数据：正确率85%
        practiceTime: 4, // 静态假数据：练习时长4小时
        totalProblems: 156, // 静态假数据：总题数
        continuousDays: 5 // 静态假数据：连续刷题5天
      }
    }
  },
  async mounted() {
    await this.fetchProblems();
    await this.loadFavoriteProblems();
    // await this.fetchPracticeStats(); // 注释掉动态获取，使用静态假数据
  },
  methods: {
    async fetchProblems() {
      const res = await axios.get('/api/practice/problems');
      this.problems = res.data;
    },
    goToProblem(id) {
      this.$router.push('/problem/' + id);
    },
    async loadFavoriteProblems() {
      const res = await axios.get('/api/practice/favorites', { params: { userId: this.userId } });
      this.favoriteProblems = res.data;
    },
    // async fetchPracticeStats() {
    //   try {
    //     const res = await axios.get('/api/user/statistics', { withCredentials: true });
    //     this.practiceStats = res.data;
    //   } catch (e) {
    //     this.practiceStats = { completed: 0, accuracy: 0, practiceTime: 0, totalProblems: 0, continuousDays: 0 };
    //   }
    // }
  }
}
</script>

<style scoped>
.practice-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.practice-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.1) !important;
}

.space-y-2 > * + * {
  margin-top: 0.5rem;
}

.bg-gradient-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.bg-gradient-success {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.problem-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 1px 4px #e0e0e0;
  padding: 20px;
  cursor: pointer;
  transition: box-shadow 0.2s, background 0.2s;
}
.problem-card:hover {
  box-shadow: 0 4px 16px #d0d7e2;
  background: #f5faff;
}
</style> 