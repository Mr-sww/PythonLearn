<template>
  <div class="practice-records-container">
    <div class="container-fluid">
      <!-- 页面标题 -->
      <div class="row mb-4">
        <div class="col-12">
          <h2 class="fw-bold text-dark">
            <i class="fa fa-history me-2"></i>练习记录
          </h2>
          <p class="text-muted">查看你的做题历史和进度</p>
        </div>
      </div>

      <!-- 统计卡片 -->
      <div class="row mb-4">
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-primary mb-2">
                <i class="fa fa-code fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.totalSubmissions || 0 }}</h4>
              <p class="text-muted mb-0">总提交次数</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-success mb-2">
                <i class="fa fa-check-circle fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.passedProblems || 0 }}</h4>
              <p class="text-muted mb-0">通过题目数</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-info mb-2">
                <i class="fa fa-percentage fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ (statistics.accuracy * 100).toFixed(1) }}%</h4>
              <p class="text-muted mb-0">通过率</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 mb-3">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center">
              <div class="text-warning mb-2">
                <i class="fa fa-calendar fa-2x"></i>
              </div>
              <h4 class="fw-bold text-dark">{{ statistics.continuousDays || 0 }}</h4>
              <p class="text-muted mb-0">连续刷题天数</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 筛选和搜索 -->
      <div class="row mb-4">
        <div class="col-md-8">
          <div class="btn-group" role="group">
            <button 
              v-for="filter in filters" 
              :key="filter.value"
              @click="setFilter(filter.value)"
              :class="['btn', currentFilter === filter.value ? 'btn-primary' : 'btn-outline-primary']"
            >
              {{ filter.label }}
            </button>
          </div>
        </div>
        <div class="col-md-4">
          <div class="input-group">
            <input 
              v-model="searchKeyword" 
              type="text" 
              class="form-control" 
              placeholder="搜索题目..."
              @input="handleSearch"
            >
            <button class="btn btn-outline-secondary" type="button">
              <i class="fa fa-search"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 记录列表 -->
      <div class="row">
        <div class="col-12">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-white">
              <h5 class="mb-0">做题记录</h5>
            </div>
            <div class="card-body p-0">
              <div v-if="loading" class="text-center py-5">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">加载中...</span>
                </div>
              </div>
              
              <div v-else-if="records.length === 0" class="text-center py-5">
                <i class="fa fa-inbox fa-3x text-muted mb-3"></i>
                <h5 class="text-muted">暂无做题记录</h5>
                <p class="text-muted">开始做题来创建你的第一个记录吧！</p>
                <router-link to="/practice" class="btn btn-primary">
                  <i class="fa fa-play me-2"></i>开始练习
                </router-link>
              </div>
              
              <div v-else class="table-responsive">
                <table class="table table-hover mb-0">
                  <thead class="table-light">
                    <tr>
                      <th>题目</th>
                      <th>提交时间</th>
                      <th>结果</th>
                      <th>通过率</th>
                      <th>执行时间</th>
                      <th>语言</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="record in records" :key="record.id">
                      <td>
                        <div class="d-flex align-items-center">
                          <div class="me-3">
                            <span :class="['badge', getDifficultyBadgeClass(record.difficulty)]">
                              {{ record.difficulty || '未知' }}
                            </span>
                          </div>
                          <div>
                            <h6 class="mb-0">{{ record.problem_title || '未知题目' }}</h6>
                            <small class="text-muted">ID: {{ record.problem_id }}</small>
                          </div>
                        </div>
                      </td>
                      <td>
                        <div>
                          <div class="fw-medium">{{ formatDate(record.submit_time) }}</div>
                          <small class="text-muted">{{ formatTime(record.submit_time) }}</small>
                        </div>
                      </td>
                      <td>
                        <span :class="['badge', getResultBadgeClass(record.result)]">
                          {{ record.result }}
                        </span>
                      </td>
                      <td>
                        <div class="d-flex align-items-center">
                          <div class="progress me-2" style="width: 60px; height: 6px;">
                            <div 
                              class="progress-bar" 
                              :class="getProgressBarClass(record.pass_rate)"
                              :style="{ width: (record.pass_rate * 100) + '%' }"
                            ></div>
                          </div>
                          <span class="small">{{ (record.pass_rate * 100).toFixed(0) }}%</span>
                        </div>
                      </td>
                      <td>
                        <span class="text-muted">{{ record.execution_time || 0 }}ms</span>
                      </td>
                      <td>
                        <span class="badge bg-secondary">{{ record.language }}</span>
                      </td>
                      <td>
                        <div class="btn-group btn-group-sm">
                          <button 
                            @click="viewCode(record)" 
                            class="btn btn-outline-primary btn-sm"
                            title="查看代码"
                          >
                            <i class="fa fa-code"></i>
                          </button>
                          <button 
                            @click="retryProblem(record)" 
                            class="btn btn-outline-success btn-sm"
                            title="重新尝试"
                          >
                            <i class="fa fa-redo"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="row mt-4">
        <div class="col-12">
          <nav v-if="totalPages > 1">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">
                  <i class="fa fa-chevron-left"></i>
                </a>
              </li>
              <li 
                v-for="page in visiblePages" 
                :key="page"
                class="page-item" 
                :class="{ active: page === currentPage }"
              >
                <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">
                  <i class="fa fa-chevron-right"></i>
                </a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>

         <!-- 代码查看模态框 -->
     <div class="modal fade" id="codeModal" tabindex="-1" @click="handleModalClick">
       <div class="modal-dialog modal-lg">
         <div class="modal-content" @click.stop>
           <div class="modal-header">
             <h5 class="modal-title">查看代码</h5>
             <button type="button" class="btn-close" @click="closeModal"></button>
           </div>
          <div class="modal-body">
            <div v-if="selectedRecord">
              <div class="mb-3">
                <strong>题目：</strong>{{ selectedRecord.problem_title }}
              </div>
              <div class="mb-3">
                <strong>提交时间：</strong>{{ formatDateTime(selectedRecord.submit_time) }}
              </div>
              <div class="mb-3">
                <strong>结果：</strong>
                <span :class="['badge', getResultBadgeClass(selectedRecord.result)]">
                  {{ selectedRecord.result }}
                </span>
              </div>
              <div class="mb-3">
                <strong>代码：</strong>
                <pre class="bg-light p-3 rounded mt-2"><code>{{ selectedRecord.code }}</code></pre>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const records = ref([])
const statistics = ref({})
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(0)
const currentFilter = ref('all')
const searchKeyword = ref('')
const selectedRecord = ref(null)

// 筛选选项
const filters = [
  { label: '全部', value: 'all' },
  { label: '通过', value: '通过' },
  { label: '未通过', value: '未通过' },
  { label: '部分通过', value: '部分通过' }
]

// 计算属性
const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, currentPage.value + 2)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// 方法
const loadStatistics = async () => {
  try {
    const user = JSON.parse(localStorage.getItem('user') || 'null')
    if (!user) return
    
    const userId = user.userId || user.user_id
    const response = await axios.get(`/api/user-problem-record/statistics?userId=${userId}`)
    statistics.value = response.data.data || {}
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

const loadRecords = async () => {
  loading.value = true
  try {
    const user = JSON.parse(localStorage.getItem('user') || 'null')
    if (!user) {
      records.value = []
      return
    }
    
    const userId = user.userId || user.user_id
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      userId: userId
    }
    
    if (currentFilter.value !== 'all') {
      params.result = currentFilter.value
    }
    
    const response = await axios.get('/api/user-problem-record/records', { params })
    const data = response.data.data
    
    records.value = data.records || []
    totalPages.value = data.pagination?.totalPages || 0
  } catch (error) {
    console.error('加载记录失败:', error)
    records.value = []
  } finally {
    loading.value = false
  }
}

const setFilter = (filter) => {
  currentFilter.value = filter
  currentPage.value = 1
  loadRecords()
}

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    loadRecords()
  }
}

const handleSearch = () => {
  // 实现搜索功能
  console.log('搜索关键词:', searchKeyword.value)
}

const viewCode = (record) => {
  selectedRecord.value = record
  // 显示模态框 - 使用原生 DOM 方法
  const modalElement = document.getElementById('codeModal')
  if (modalElement) {
    // 手动显示模态框
    modalElement.classList.add('show')
    modalElement.style.display = 'block'
    modalElement.setAttribute('aria-hidden', 'false')
    
    // 添加背景遮罩
    const backdrop = document.createElement('div')
    backdrop.className = 'modal-backdrop fade show'
    backdrop.id = 'modalBackdrop'
    document.body.appendChild(backdrop)
    
    // 添加 body 类
    document.body.classList.add('modal-open')
  }
}

const closeModal = () => {
  const modalElement = document.getElementById('codeModal')
  if (modalElement) {
    // 隐藏模态框
    modalElement.classList.remove('show')
    modalElement.style.display = 'none'
    modalElement.setAttribute('aria-hidden', 'true')
    
    // 移除背景遮罩
    const backdrop = document.getElementById('modalBackdrop')
    if (backdrop) {
      document.body.removeChild(backdrop)
    }
    
    // 移除 body 类
    document.body.classList.remove('modal-open')
  }
}

const handleModalClick = (event) => {
  // 如果点击的是模态框背景，则关闭模态框
  if (event.target.id === 'codeModal') {
    closeModal()
  }
}

const handleKeydown = (event) => {
  // ESC 键关闭模态框
  if (event.key === 'Escape') {
    closeModal()
  }
}

const retryProblem = (record) => {
  router.push(`/problem/${record.problem_id}`)
}

// 工具方法
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

const formatTime = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleTimeString('zh-CN')
}

const formatDateTime = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const getResultBadgeClass = (result) => {
  switch (result) {
    case '通过': return 'bg-success'
    case '未通过': return 'bg-danger'
    case '部分通过': return 'bg-warning text-dark'
    default: return 'bg-secondary'
  }
}

const getDifficultyBadgeClass = (difficulty) => {
  switch (difficulty) {
    case '简单': return 'bg-success'
    case '中等': return 'bg-warning text-dark'
    case '困难': return 'bg-danger'
    default: return 'bg-secondary'
  }
}

const getProgressBarClass = (passRate) => {
  if (passRate >= 0.8) return 'bg-success'
  if (passRate >= 0.5) return 'bg-warning'
  return 'bg-danger'
}

// 生命周期
onMounted(() => {
  loadStatistics()
  loadRecords()
  
  // 添加键盘事件监听器
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  // 移除键盘事件监听器
  document.removeEventListener('keydown', handleKeydown)
})
</script>

<style scoped>
.practice-records-container {
  padding: 2rem 0;
  background: #f8f9fa;
  min-height: 100vh;
}

.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-2px);
}

.table th {
  border-top: none;
  font-weight: 600;
  color: #495057;
}

.progress {
  background-color: #e9ecef;
}

.btn-group .btn {
  border-radius: 0.375rem;
}

.pagination .page-link {
  border: none;
  color: #6c757d;
}

.pagination .page-item.active .page-link {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

.pagination .page-link:hover {
  background-color: #e9ecef;
  color: #495057;
}
</style>
