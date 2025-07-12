<template>
<div>
  <!-- 顶部导航栏 -->
  <header class="sticky-top bg-white shadow-sm mb-4">
    <div class="container d-flex align-items-center justify-content-between py-3">
      <div class="d-flex align-items-center">
        <div class="bg-primary text-white rounded-3 p-2 me-2"><i class="fa fa-code fa-lg"></i></div>
        <span class="fw-bold fs-4">Python学习平台</span>
      </div>
      <nav class="d-none d-md-flex gap-4">
        <router-link to="/" class="nav-link text-dark">首页</router-link>
        <router-link to="/courses" class="nav-link text-dark">课程中心</router-link>
        <router-link to="/learning" class="nav-link text-dark">学习中心</router-link>
        <router-link to="/practice" class="nav-link text-primary fw-bold">实践中心</router-link>
        <router-link to="/ai" class="nav-link text-dark">AI问答</router-link>
      </nav>
    </div>
  </header>

  <!-- 题目列表内容 -->
  <main class="container py-4">
    <!-- 页面标题和搜索 -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h2 class="fw-bold text-dark mb-2">编程练习</h2>
        <p class="text-muted mb-0">通过实战练习提升Python编程技能</p>
      </div>
      <div class="d-flex gap-2">
        <input type="text" class="form-control rounded-pill" style="min-width:250px;" placeholder="搜索题目...">
        <button class="btn btn-primary rounded-pill px-4">
          <i class="fa fa-search me-1"></i>搜索
        </button>
      </div>
    </div>

    <!-- 筛选和统计 -->
    <div class="row g-4">
      <!-- 左侧筛选 -->
      <div class="col-lg-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <h5 class="fw-bold text-dark mb-3">难度等级</h5>
          <div class="mb-4">
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="easy" checked>
              <label class="form-check-label" for="easy">
                <span class="badge bg-success me-2">简单</span>基础 (45)
              </label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="medium" checked>
              <label class="form-check-label" for="medium">
                <span class="badge bg-warning text-dark me-2">中等</span>进阶 (67)
              </label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="hard">
              <label class="form-check-label" for="hard">
                <span class="badge bg-danger me-2">困难</span>高级 (33)
              </label>
            </div>
          </div>

          <h5 class="fw-bold text-dark mb-3">题目分类</h5>
          <div class="mb-4">
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="array" checked>
              <label class="form-check-label" for="array">数组 (23)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="string" checked>
              <label class="form-check-label" for="string">字符串 (18)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="linkedlist">
              <label class="form-check-label" for="linkedlist">链表 (12)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="tree">
              <label class="form-check-label" for="tree">树 (15)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="dp">
              <label class="form-check-label" for="dp">动态规划 (20)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="graph">
              <label class="form-check-label" for="graph">图 (8)</label>
            </div>
          </div>

          <h5 class="fw-bold text-dark mb-3">完成状态</h5>
          <div class="mb-4">
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="unsolved" checked>
              <label class="form-check-label" for="unsolved">未完成 (132)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="solved" checked>
              <label class="form-check-label" for="solved">已完成 (24)</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="attempted">
              <label class="form-check-label" for="attempted">尝试过 (8)</label>
            </div>
          </div>

          <button class="btn btn-outline-primary w-100 rounded-pill">重置筛选</button>
        </div>

        <!-- 学习进度 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h5 class="fw-bold text-dark mb-3">我的进度</h5>
          <div class="progress mb-3" style="height:8px;">
            <div class="progress-bar bg-success" style="width:18%"></div>
          </div>
          <div class="d-flex justify-content-between text-muted small mb-2">
            <span>已完成</span>
            <span>24/156</span>
          </div>
          <div class="d-flex justify-content-between text-muted small mb-2">
            <span>连续天数</span>
            <span>7天</span>
          </div>
          <div class="d-flex justify-content-between text-muted small">
            <span>正确率</span>
            <span>85%</span>
          </div>
        </div>
      </div>

      <!-- 右侧题目列表 -->
      <div class="col-lg-9">
        <!-- 排序和统计 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-muted">共找到 <strong>156</strong> 道题目</span>
          <div class="d-flex align-items-center gap-2">
            <span class="text-muted">排序：</span>
            <select class="form-select form-select-sm" style="width:auto;">
              <option>推荐顺序</option>
              <option>难度从低到高</option>
              <option>难度从高到低</option>
              <option>通过率从高到低</option>
              <option>通过率从低到高</option>
            </select>
          </div>
        </div>

        <!-- 题目表格 -->
        <div class="bg-white rounded-3 shadow-sm border">
          <div class="table-responsive">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th style="width:60px;">状态</th>
                  <th style="width:80px;">编号</th>
                  <th>题目</th>
                  <th style="width:100px;">难度</th>
                  <th style="width:120px;">通过率</th>
                  <th style="width:100px;">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="problem in problems" :key="problem.id" :id="'problem-row-' + problem.id">
                  <td>
                    <div class="d-flex justify-content-center">
                      <i v-if="problem.status === 'solved'" class="fa fa-check-circle text-success fa-lg"></i>
                      <i v-else-if="problem.status === 'attempted'" class="fa fa-times-circle text-warning fa-lg"></i>
                      <i v-else class="fa fa-circle-o text-muted fa-lg"></i>
                    </div>
                  </td>
                  <td class="fw-medium">{{ problem.id }}</td>
                  <td>
                    <div class="d-flex align-items-center">
                      <div class="flex-grow-1">
                        <div class="fw-medium text-dark">{{ problem.title }}</div>
                        <div class="text-muted small">{{ problem.tags.join(', ') }}</div>
                      </div>
                      <div class="ms-2">
                        <i v-if="problem.isPremium" class="fa fa-crown text-warning"></i>
                      </div>
                    </div>
                  </td>
                  <td>
                    <span :class="['badge rounded-pill px-3 py-2', 
                      problem.difficulty === '简单' ? 'bg-success' : 
                      problem.difficulty === '中等' ? 'bg-warning text-dark' : 'bg-danger']">
                      {{ problem.difficulty }}
                    </span>
                  </td>
                  <td>
                    <div class="text-muted small">{{ problem.acceptanceRate }}%</div>
                    <div class="progress" style="height:4px;">
                      <div class="progress-bar" :class="
                        problem.acceptanceRate > 70 ? 'bg-success' : 
                        problem.acceptanceRate > 40 ? 'bg-warning' : 'bg-danger'"
                        :style="`width:${problem.acceptanceRate}%`">
                      </div>
                    </div>
                  </td>
                  <td>
                    <router-link :to="`/code-practice?id=${problem.id}`" class="btn btn-outline-primary btn-sm rounded-pill">
                      开始练习
                    </router-link>
                  </td>
                </tr>
              </tbody>
            </table>
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
export default {
  name: 'ProblemList',
  data() {
    return {
      problems: [
        {
          id: 1,
          title: '两数之和',
          difficulty: '简单',
          acceptanceRate: 52.3,
          status: 'solved',
          tags: ['数组', '哈希表'],
          isPremium: false
        },
        {
          id: 2,
          title: '两数相加',
          difficulty: '中等',
          acceptanceRate: 41.2,
          status: 'attempted',
          tags: ['链表', '数学'],
          isPremium: false
        },
        {
          id: 3,
          title: '无重复字符的最长子串',
          difficulty: '中等',
          acceptanceRate: 38.7,
          status: 'unsolved',
          tags: ['字符串', '滑动窗口'],
          isPremium: false
        },
        {
          id: 4,
          title: '寻找两个正序数组的中位数',
          difficulty: '困难',
          acceptanceRate: 32.1,
          status: 'unsolved',
          tags: ['数组', '二分查找'],
          isPremium: true
        },
        {
          id: 5,
          title: '最长回文子串',
          difficulty: '中等',
          acceptanceRate: 35.8,
          status: 'unsolved',
          tags: ['字符串', '动态规划'],
          isPremium: false
        },
        {
          id: 6,
          title: 'Z字形变换',
          difficulty: '中等',
          acceptanceRate: 45.2,
          status: 'solved',
          tags: ['字符串'],
          isPremium: false
        },
        {
          id: 7,
          title: '整数反转',
          difficulty: '简单',
          acceptanceRate: 67.4,
          status: 'solved',
          tags: ['数学'],
          isPremium: false
        },
        {
          id: 8,
          title: '字符串转换整数',
          difficulty: '中等',
          acceptanceRate: 28.9,
          status: 'unsolved',
          tags: ['字符串', '数学'],
          isPremium: false
        },
        {
          id: 9,
          title: '回文数',
          difficulty: '简单',
          acceptanceRate: 58.6,
          status: 'solved',
          tags: ['数学'],
          isPremium: false
        },
        {
          id: 10,
          title: '正则表达式匹配',
          difficulty: '困难',
          acceptanceRate: 25.3,
          status: 'unsolved',
          tags: ['字符串', '动态规划'],
          isPremium: true
        }
      ]
    }
  },
  mounted() {
    this.$nextTick(() => {
      const highlightId = this.$route.query.highlight;
      if (highlightId) {
        // 等待表格渲染后滚动
        setTimeout(() => {
          const row = document.getElementById('problem-row-' + highlightId);
          if (row) {
            row.scrollIntoView({ behavior: 'smooth', block: 'center' });
            row.classList.add('highlight-row');
            setTimeout(() => row.classList.remove('highlight-row'), 2000);
          }
        }, 300);
      }
    });
  }
}
</script>

<style scoped>
.table th {
  border-top: none;
  font-weight: 600;
  color: #495057;
}

.table td {
  vertical-align: middle;
  border-top: 1px solid #f8f9fa;
}

.table tbody tr:hover {
  background-color: #f8f9fa;
}

.highlight-row {
  background: #e0f0ff !important;
  transition: background 0.8s;
}
</style> 