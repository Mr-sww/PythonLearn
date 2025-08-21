<template>
  <div class="practice-statistics">
    <div class="container-fluid">
      <!-- 页面头部 -->
      <div class="page-header mb-4">
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <h1 class="page-title mb-2">
              <i class="fa fa-chart-line text-primary me-3"></i>
              练习统计详情
            </h1>
            <p class="text-muted mb-0">查看你的详细练习记录和数据分析</p>
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
              <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-check-circle text-primary fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ overview.totalSubmissions }}</h4>
              <p class="text-muted mb-0">总提交次数</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-trophy text-success fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ overview.passedProblems }}</h4>
              <p class="text-muted mb-0">已通过题目</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
                <i class="fa fa-percentage text-info fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark mb-1">{{ overview.accuracy }}%</h4>
              <p class="text-muted mb-0">正确率</p>
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
              <p class="text-muted mb-0">连续天数</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 筛选和搜索 -->
      <div class="card border-0 shadow-sm mb-4">
        <div class="card-body">
          <div class="row g-3 align-items-end">
            <div class="col-md-3">
              <label class="form-label">结果筛选</label>
              <select v-model="filter.result" class="form-select" @change="loadRecords">
                <option value="">全部记录</option>
                <option value="通过">已通过</option>
                <option value="未通过">未通过</option>
              </select>
            </div>
            <div class="col-md-3">
              <label class="form-label">每页显示</label>
              <select v-model="pagination.size" class="form-select" @change="loadRecords">
                <option value="10">10条</option>
                <option value="20">20条</option>
                <option value="50">50条</option>
              </select>
            </div>
            <div class="col-md-6">
              <div class="d-flex justify-content-end">
                <button class="btn btn-primary" @click="loadRecords">
                  <i class="fa fa-search me-2"></i>刷新数据
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 记录列表 -->
      <div class="card border-0 shadow-sm">
        <div class="card-header bg-light">
          <h5 class="mb-0">
            <i class="fa fa-list me-2"></i>
            练习记录
            <span class="badge bg-primary ms-2">{{ pagination.total }}条记录</span>
          </h5>
        </div>
        <div class="card-body p-0">
          <div v-if="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
            <p class="mt-3 text-muted">正在加载记录...</p>
          </div>
          
          <div v-else-if="records.length === 0" class="text-center py-5">
            <i class="fa fa-inbox fa-3x text-muted mb-3"></i>
            <p class="text-muted">暂无练习记录</p>
          </div>
          
          <div v-else class="table-responsive">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>题目</th>
                  <th>提交时间</th>
                  <th>结果</th>
                  <th>执行时间</th>
                  <th>内存使用</th>
                  <th>得分</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="record in records" :key="record.id">
                  <td>
                    <div class="d-flex align-items-center">
                      <div class="me-3">
                        <i :class="getResultIcon(record.result)" :style="{ color: getResultColor(record.result) }"></i>
                      </div>
                      <div>
                        <div class="fw-medium">{{ record.problem_title || '未知题目' }}</div>
                        <small class="text-muted">ID: {{ record.problem_id }}</small>
                      </div>
                    </div>
                  </td>
                  <td>
                    <div>{{ formatDate(record.submit_time) }}</div>
                    <small class="text-muted">{{ formatTime(record.submit_time) }}</small>
                  </td>
                  <td>
                    <span :class="getResultBadgeClass(record.result)">
                      {{ record.result }}
                    </span>
                  </td>
                  <td>
                    <span v-if="record.execution_time" class="text-muted">
                      {{ record.execution_time }}ms
                    </span>
                    <span v-else class="text-muted">-</span>
                  </td>
                  <td>
                    <span v-if="record.memory_usage" class="text-muted">
                      {{ formatMemory(record.memory_usage) }}
                    </span>
                    <span v-else class="text-muted">-</span>
                  </td>
                  <td>
                    <span v-if="record.score !== null" class="fw-medium" :class="getScoreClass(record.score)">
                      {{ record.score }}分
                    </span>
                    <span v-else class="text-muted">-</span>
                  </td>
                  <td>
                    <button class="btn btn-sm btn-outline-primary" @click="viewCode(record)">
                      <i class="fa fa-code me-1"></i>查看代码
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        
        <!-- 分页 -->
        <div v-if="pagination.totalPages > 1" class="card-footer">
          <nav aria-label="分页导航">
            <ul class="pagination justify-content-center mb-0">
              <li class="page-item" :class="{ disabled: pagination.page <= 1 }">
                <button class="page-link" @click="changePage(pagination.page - 1)" :disabled="pagination.page <= 1">
                  <i class="fa fa-chevron-left"></i>
                </button>
              </li>
              
              <li v-for="page in getPageNumbers()" :key="page" class="page-item" :class="{ active: page === pagination.page }">
                <button class="page-link" @click="changePage(page)">{{ page }}</button>
              </li>
              
              <li class="page-item" :class="{ disabled: pagination.page >= pagination.totalPages }">
                <button class="page-link" @click="changePage(pagination.page + 1)" :disabled="pagination.page >= pagination.totalPages">
                  <i class="fa fa-chevron-right"></i>
                </button>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>

    <!-- 代码查看模态框 -->
    <div class="modal fade" id="codeModal" tabindex="-1" ref="codeModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="fa fa-code me-2"></i>
              提交代码
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedRecord">
              <div class="mb-3">
                <strong>题目：</strong>{{ selectedRecord.problem_title || '未知题目' }}
              </div>
              <div class="mb-3">
                <strong>提交时间：</strong>{{ formatDateTime(selectedRecord.submit_time) }}
              </div>
              <div class="mb-3">
                <strong>结果：</strong>
                <span :class="getResultBadgeClass(selectedRecord.result)">
                  {{ selectedRecord.result }}
                </span>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>代码：</strong></label>
                <pre class="bg-light p-3 rounded border"><code>{{ selectedRecord.code }}</code></pre>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { Modal } from 'bootstrap';

export default {
  name: 'PracticeStatistics',
  data() {
    return {
      loading: false,
      records: [],
      overview: {
        totalSubmissions: 0,
        passedProblems: 0,
        accuracy: 0,
        continuousDays: 0
      },
      filter: {
        result: ''
      },
      pagination: {
        page: 1,
        size: 10,
        total: 0,
        totalPages: 0
      },
      selectedRecord: null,
      codeModal: null
    };
  },
  async mounted() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    this.userId = user ? (user.userId || user.user_id) : null;
    if (!this.userId) {
      this.$router.push('/auth');
      return;
    }
    
    await this.loadOverview();
    await this.loadRecords();
    
    // 初始化模态框
    this.codeModal = new Modal(this.$refs.codeModal);
  },
  methods: {
    async loadOverview() {
      try {
        const response = await axios.get(`/api/user-problem-record/statistics`, { params: { userId: this.userId } });
        const data = response.data?.data || {};
        this.overview = {
          totalSubmissions: data.totalSubmissions || 0,
          passedProblems: data.passedProblems || 0,
          accuracy: Math.round(((data.accuracy || 0) * 100)),
          continuousDays: data.continuousDays || 0
        };
      } catch (error) {
        console.error('获取统计概览失败:', error);
      }
    },
    
    async loadRecords() {
      this.loading = true;
      try {
        const params = {
          userId: this.userId,
          page: this.pagination.page,
          size: this.pagination.size
        };

        let response;
        if (this.filter.result) {
          response = await axios.get('/api/user-problem-record/records', { params: { ...params, result: this.filter.result } });
        } else {
          response = await axios.get('/api/user-problem-record/records', { params });
        }

        if (response.data.success && response.data.data) {
          this.records = response.data.data.records || [];
          this.pagination.total = response.data.data.pagination?.total || 0;
          this.pagination.totalPages = response.data.data.pagination?.totalPages || 0;
        }
      } catch (error) {
        console.error('获取记录失败:', error);
        this.records = [];
      } finally {
        this.loading = false;
      }
    },
    
    changePage(page) {
      if (page >= 1 && page <= this.pagination.totalPages) {
        this.pagination.page = page;
        this.loadRecords();
      }
    },
    
    getPageNumbers() {
      const pages = [];
      const current = this.pagination.page;
      const total = this.pagination.totalPages;
      
      if (total <= 7) {
        for (let i = 1; i <= total; i++) {
          pages.push(i);
        }
      } else {
        if (current <= 4) {
          for (let i = 1; i <= 5; i++) {
            pages.push(i);
          }
          pages.push('...');
          pages.push(total);
        } else if (current >= total - 3) {
          pages.push(1);
          pages.push('...');
          for (let i = total - 4; i <= total; i++) {
            pages.push(i);
          }
        } else {
          pages.push(1);
          pages.push('...');
          for (let i = current - 1; i <= current + 1; i++) {
            pages.push(i);
          }
          pages.push('...');
          pages.push(total);
        }
      }
      
      return pages.filter(page => page !== '...' || pages.indexOf(page) !== pages.lastIndexOf(page));
    },
    
    viewCode(record) {
      this.selectedRecord = record;
      this.codeModal.show();
    },
    
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN');
    },
    
    formatTime(dateString) {
      const date = new Date(dateString);
      return date.toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
    },
    
    formatDateTime(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN');
    },
    
    formatMemory(bytes) {
      if (bytes < 1024) {
        return bytes + ' B';
      } else if (bytes < 1024 * 1024) {
        return (bytes / 1024).toFixed(1) + ' KB';
      } else {
        return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
      }
    },
    
    getResultIcon(result) {
      return result === '通过' ? 'fa fa-check-circle' : 'fa fa-times-circle';
    },
    
    getResultColor(result) {
      return result === '通过' ? '#28a745' : '#dc3545';
    },
    
    getResultBadgeClass(result) {
      return result === '通过' ? 'badge bg-success' : 'badge bg-danger';
    },
    
    getScoreClass(score) {
      if (score >= 90) return 'text-success';
      if (score >= 80) return 'text-primary';
      if (score >= 60) return 'text-warning';
      return 'text-danger';
    }
  }
};
</script>

<style scoped>
.practice-statistics {
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

.table th {
  border-top: none;
  font-weight: 600;
  color: #495057;
}

.table td {
  vertical-align: middle;
}

.badge {
  font-size: 0.75rem;
  padding: 0.5em 0.75em;
}

.pagination .page-link {
  border: none;
  color: #6c757d;
  padding: 0.5rem 0.75rem;
}

.pagination .page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
}

.pagination .page-item.disabled .page-link {
  color: #adb5bd;
}

pre {
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  font-size: 0.875rem;
  line-height: 1.5;
  max-height: 400px;
  overflow-y: auto;
}

code {
  color: #e83e8c;
  background-color: transparent;
}
</style>
