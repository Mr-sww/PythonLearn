import axios from 'axios'

// 配置axios基础URL
const API_BASE_URL = 'http://localhost:8080/api'

// 创建axios实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  config => {
    // 可以在这里添加认证token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 课程服务类
 */
class CourseService {
  /**
   * 获取所有课程
   * @param {Object} params 查询参数
   * @returns {Promise}
   */
  async getAllCourses(params = {}) {
    try {
      const response = await apiClient.get('/courses', { params })
      return response.data
    } catch (error) {
      console.error('获取课程列表失败:', error)
      throw error
    }
  }

  /**
   * 根据ID获取课程详情
   * @param {number} courseId 课程ID
   * @returns {Promise}
   */
  async getCourseById(courseId) {
    try {
      const response = await apiClient.get(`/courses/${courseId}`)
      return response.data
    } catch (error) {
      console.error('获取课程详情失败:', error)
      throw error
    }
  }

  /**
   * 根据分类获取课程
   * @param {string} category 分类名称
   * @returns {Promise}
   */
  async getCoursesByCategory(category) {
    try {
      const response = await apiClient.get(`/courses/category/${category}`)
      return response.data
    } catch (error) {
      console.error('获取分类课程失败:', error)
      throw error
    }
  }

  /**
   * 根据作者获取课程
   * @param {string} author 作者名称
   * @returns {Promise}
   */
  async getCoursesByAuthor(author) {
    try {
      const response = await apiClient.get(`/courses/author/${author}`)
      return response.data
    } catch (error) {
      console.error('获取作者课程失败:', error)
      throw error
    }
  }

  /**
   * 搜索课程
   * @param {string} keyword 搜索关键词
   * @returns {Promise}
   */
  async searchCourses(keyword) {
    try {
      const response = await apiClient.get('/courses/search', {
        params: { keyword }
      })
      return response.data
    } catch (error) {
      console.error('搜索课程失败:', error)
      throw error
    }
  }

  /**
   * 获取热门课程
   * @param {number} limit 限制数量
   * @returns {Promise}
   */
  async getPopularCourses(limit = 10) {
    try {
      const response = await apiClient.get('/courses/popular', {
        params: { limit }
      })
      return response.data
    } catch (error) {
      console.error('获取热门课程失败:', error)
      throw error
    }
  }

  /**
   * 获取最新课程
   * @param {number} limit 限制数量
   * @returns {Promise}
   */
  async getLatestCourses(limit = 10) {
    try {
      const response = await apiClient.get('/courses/latest', {
        params: { limit }
      })
      return response.data
    } catch (error) {
      console.error('获取最新课程失败:', error)
      throw error
    }
  }

  /**
   * 获取推荐课程
   * @param {number} userId 用户ID
   * @param {number} limit 限制数量
   * @returns {Promise}
   */
  async getRecommendedCourses(userId = null, limit = 6) {
    try {
      const params = { limit }
      if (userId) {
        params.userId = userId
      }
      const response = await apiClient.get('/courses/recommended', { params })
      return response.data
    } catch (error) {
      console.error('获取推荐课程失败:', error)
      throw error
    }
  }

  /**
   * 分页获取课程
   * @param {number} page 页码
   * @param {number} size 每页大小
   * @param {Object} filters 筛选条件
   * @returns {Promise}
   */
  async getCoursesWithPagination(page = 0, size = 10, filters = {}) {
    try {
      const params = {
        page,
        size,
        ...filters
      }
      const response = await apiClient.get('/courses/page', { params })
      return response.data
    } catch (error) {
      console.error('分页获取课程失败:', error)
      throw error
    }
  }

  /**
   * 获取课程总数
   * @returns {Promise}
   */
  async getCourseCount() {
    try {
      const response = await apiClient.get('/courses/count')
      return response.data
    } catch (error) {
      console.error('获取课程总数失败:', error)
      throw error
    }
  }

  /**
   * 获取分类统计
   * @returns {Promise}
   */
  async getCategoryStatistics() {
    try {
      const response = await apiClient.get('/courses/statistics/category')
      return response.data
    } catch (error) {
      console.error('获取分类统计失败:', error)
      throw error
    }
  }

  /**
   * 增加课程浏览量
   * @param {number} courseId 课程ID
   * @returns {Promise}
   */
  async incrementViews(courseId) {
    try {
      const response = await apiClient.post(`/courses/${courseId}/view`)
      return response.data
    } catch (error) {
      console.error('增加浏览量失败:', error)
      throw error
    }
  }

  /**
   * 创建课程
   * @param {Object} courseData 课程数据
   * @returns {Promise}
   */
  async createCourse(courseData) {
    try {
      const response = await apiClient.post('/courses', courseData)
      return response.data
    } catch (error) {
      console.error('创建课程失败:', error)
      throw error
    }
  }

  /**
   * 更新课程
   * @param {number} courseId 课程ID
   * @param {Object} courseData 课程数据
   * @returns {Promise}
   */
  async updateCourse(courseId, courseData) {
    try {
      const response = await apiClient.put(`/courses/${courseId}`, courseData)
      return response.data
    } catch (error) {
      console.error('更新课程失败:', error)
      throw error
    }
  }

  /**
   * 删除课程
   * @param {number} courseId 课程ID
   * @returns {Promise}
   */
  async deleteCourse(courseId) {
    try {
      const response = await apiClient.delete(`/courses/${courseId}`)
      return response.data
    } catch (error) {
      console.error('删除课程失败:', error)
      throw error
    }
  }

  /**
   * 获取模拟课程数据（用于开发测试）
   * @param {number} count 数量
   * @returns {Array}
   */
  getMockCourses(count = 9) {
    return Array.from({ length: count }, (_, i) => ({
      articleId: i + 1,
      title: `Python${i === 0 ? '基础入门到精通' : i === 1 ? 'Web开发实战' : i === 2 ? '数据分析与可视化' : '课程标题' + (i + 1)}`,
      content: '从环境搭建到项目实战，全面系统地学习Python编程基础，适合零基础学习者。',
      author: `讲师${i + 1}`,
      category: ['Python基础', 'Web开发', '数据分析'][i % 3],
      tags: 'Python,编程,学习',
      views: 1000 + i * 123,
      difficulty: ['beginner', 'intermediate', 'advanced'][i % 3],
      price: i % 3 === 0 ? 0 : 99 + i * 10,
      rating: 4.5 + (i % 5) * 0.1,
      duration: `${2 + i * 0.5}小时`,
      lessons: `${15 + i * 3}课时`,
      publicationDate: new Date(Date.now() - i * 86400000),
      coverImage: `https://picsum.photos/600/300?random=${i + 1}`,
      authorAvatar: `https://picsum.photos/100/100?random=${100 + i}`
    }))
  }

  /**
   * 获取模拟课程详情
   * @param {number} courseId 课程ID
   * @returns {Object}
   */
  getMockCourseDetail(courseId) {
    return {
      articleId: parseInt(courseId),
      title: 'Python基础入门到精通',
      content: '从环境搭建到项目实战，全面系统地学习Python编程基础，适合零基础学习者。本课程将带你从零开始，逐步掌握Python编程的核心概念和实践技能。',
      author: '张教授',
      category: 'Python基础',
      tags: 'Python,编程,学习,入门',
      views: 12458,
      price: 0,
      rating: 4.8,
      duration: '2小时',
      lessons: '15课时',
      publicationDate: new Date('2024-01-15'),
      difficulty: 'beginner',
      coverImage: `https://picsum.photos/800/400?random=${courseId}`,
      authorAvatar: `https://picsum.photos/100/100?random=${100 + parseInt(courseId)}`
    }
  }
}

// 创建服务实例
const courseService = new CourseService()

export default courseService 