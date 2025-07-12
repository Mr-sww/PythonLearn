<template>
<div>
  <!-- 课程中心内容 -->
  <main class="container py-4">
    <!-- 页面标题和搜索 -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h2 class="fw-bold text-dark mb-2">课程中心</h2>
        <p class="text-muted mb-0">精选Python课程，从入门到精通</p>
      </div>
      <div class="d-flex gap-2">
        <input 
          v-model="searchKeyword" 
          @keyup.enter="handleSearch"
          type="text" 
          class="form-control rounded-pill" 
          style="min-width:250px;" 
          placeholder="搜索课程名称、讲师..."
        >
        <button @click="handleSearch" class="btn btn-primary rounded-pill px-4">
          <i class="fa fa-search me-1"></i>搜索
        </button>
      </div>
    </div>

    <!-- 课程分类和筛选 -->
    <div class="row mb-4">
      <div class="col-lg-3">
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h5 class="fw-bold text-dark mb-3">课程分类</h5>
          <div class="mb-4">
            <div 
              v-for="category in categories" 
              :key="category"
              class="form-check mb-2"
            >
              <input 
                class="form-check-input" 
                type="checkbox" 
                :id="'cat-' + category"
                :value="category"
                v-model="selectedCategories"
                @change="handleFilterChange"
              >
              <label class="form-check-label" :for="'cat-' + category">
                {{ category }}
              </label>
            </div>
          </div>

          

          <button @click="resetFilters" class="btn btn-outline-primary w-100 rounded-pill">
            重置筛选
          </button>
        </div>
      </div>

      <!-- 课程列表 -->
      <div class="col-lg-9">
        <!-- 排序和统计 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-muted">共找到 <strong>{{ totalCourses }}</strong> 门课程</span>
          <div class="d-flex align-items-center gap-2">
            <span class="text-muted">排序：</span>
            <select v-model="sortBy" @change="handleSortChange" class="form-select form-select-sm" style="width:auto;">
              <option value="latest">最新发布</option>
              <option value="popular">学习人数最多</option>
              <option value="rating">评分最高</option>
              <option value="price-asc">价格从低到高</option>
              <option value="price-desc">价格从高到低</option>
            </select>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">加载中...</span>
          </div>
          <p class="mt-2 text-muted">正在加载课程...</p>
        </div>

        <!-- 课程网格 -->
        <div v-else-if="filteredCourses.length > 0" class="row g-4">
          <div class="col-md-6 col-lg-4" v-for="course in filteredCourses" :key="course.articleId">
            <CourseCard 
              :course="course"
              @view-course="handleViewCourse"
              @start-learning="handleStartLearning"
              @purchase-course="handlePurchaseCourse"
            />
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="text-center py-5">
          <i class="fa fa-search fa-3x text-muted mb-3"></i>
          <h5 class="text-muted">暂无相关课程</h5>
          <p class="text-muted">尝试调整搜索条件或筛选条件</p>
        </div>

        <!-- 分页 -->
        <nav v-if="totalPages > 1" class="mt-4">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">上一页</a>
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
              <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">下一页</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </main>
</div>
</template>

<script>
import CourseCard from '@/components/CourseCard.vue'
import axios from 'axios'

export default {
  name: 'CourseCenter',
  components: {
    CourseCard
  },
  data() {
    return {
      courses: [],
      loading: false,
      searchKeyword: '',
      selectedCategories: [
        'Python综合教学', '爬虫', '人工智能', '设计', '历史', '体育',
        '数据分析', '数学建模', 'Web开发'
      ],
      selectedLevels: [],
      selectedPriceRange: 'all',
      sortBy: 'latest',
      currentPage: 1,
      pageSize: 9,
      totalCourses: 0,
      totalPages: 0,
      categories: [
        'Python综合教学', '爬虫', '人工智能', '设计', '历史', '体育',
        '数据分析', '数学建模', 'Web开发'
      ],
      difficultyLevels: [
        { value: 'beginner', name: '初级', label: '入门', count: 0, badgeClass: 'bg-success' },
        { value: 'intermediate', name: '进阶', label: '中级', count: 0, badgeClass: 'bg-warning text-dark' },
        { value: 'advanced', name: '专业', label: '高级', count: 0, badgeClass: 'bg-danger' }
      ],
      priceRanges: [
        { value: 'free', name: '免费课程' },
        { value: 'paid', name: '付费课程' },
        { value: 'all', name: '全部课程' }
      ]
    }
  },
  computed: {
    visiblePages() {
      const pages = []
      const start = Math.max(1, this.currentPage - 2)
      const end = Math.min(this.totalPages, this.currentPage + 2)
      
      for (let i = start; i <= end; i++) {
        pages.push(i)
      }
      return pages
    },
    filteredCourses() {
      if (!this.selectedCategories.length) return this.courses;
      return this.courses.filter(course => {
        const tags = (course.tags || '').split(',');
        return this.selectedCategories.some(cat => tags.includes(cat));
      });
    }
  },
  watch: {
    selectedCategories: {
      handler(newVal) {
        this.fetchCoursesByCategories(newVal);
      },
      immediate: true
    }
  },
  mounted() {
    this.loadCourses()
  },
  methods: {
    // 加载课程数据（从后端PythonVideos表获取）
    async loadCourses() {
      this.loading = true
      try {
        const response = await axios.get('/api/python-videos')
        this.courses = response.data
        this.totalCourses = this.courses.length
        this.totalPages = Math.ceil(this.totalCourses / this.pageSize)
      } catch (error) {
        console.error('加载课程失败:', error)
        this.courses = []
        this.totalCourses = 0
        this.totalPages = 0
      } finally {
        this.loading = false
      }
    },
    
    // 处理搜索
    handleSearch() {
      // 可实现本地过滤，或后端支持搜索时调用接口
      this.currentPage = 1
      // 简单本地过滤
      if (this.searchKeyword) {
        this.courses = this.courses.filter(c => c.title.includes(this.searchKeyword))
        this.totalCourses = this.courses.length
        this.totalPages = Math.ceil(this.totalCourses / this.pageSize)
      } else {
        this.loadCourses()
      }
    },
    
    // 处理筛选变化
    handleFilterChange() {
      // 可实现本地过滤，或后端支持筛选时调用接口
      this.currentPage = 1
      // 这里只做演示，实际可根据category等字段过滤
    },
    
    // 处理排序变化
    handleSortChange() {
      this.currentPage = 1
      // 可实现本地排序，或后端支持排序时调用接口
    },
    
    // 重置筛选
    resetFilters() {
      this.selectedCategories = [
        'Python综合教学', '爬虫', '人工智能', '设计', '历史', '体育',
        '数据分析', '数学建模', 'Web开发'
      ]
      this.selectedLevels = []
      this.selectedPriceRange = 'all'
      this.searchKeyword = ''
      this.sortBy = 'latest'
      this.currentPage = 1
      this.loadCourses()
    },
    
    // 切换页面
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page
      }
    },
    
    // 查看课程详情
    handleViewCourse(course) {
      console.log('查看课程:', course)
    },
    
    // 开始学习
    handleStartLearning(course) {
      console.log('开始学习:', course)
    },
    
    // 购买课程
    handlePurchaseCourse(course) {
      console.log('购买课程:', course)
      // 这里可以实现支付逻辑
      alert(`准备购买课程：${course.title}`)
    },

    async fetchCoursesByCategories(categories) {
      const excluded = this.categories.filter(cat => !categories.includes(cat));
      const res = await this.$axios.post('/api/python-videos/filter', { categories, excluded });
      this.courses = res.data;
    }
  }
}
</script>

<style scoped>
.course-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0,0,0,0.1) !important;
}
</style> 