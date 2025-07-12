import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加token等认证信息
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
api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 学习进度相关API服务
 */
export const progressService = {
  /**
   * 根据用户ID和课程ID获取学习进度
   */
  getProgressByUserAndCourse(userId, courseId) {
    return api.get(`/progress/user/${userId}/course/${courseId}`)
  },

  /**
   * 根据用户ID获取所有学习进度
   */
  getProgressByUserId(userId) {
    return api.get(`/progress/user/${userId}`)
  },

  /**
   * 创建或更新学习进度
   */
  saveProgress(progressData) {
    return api.post('/progress', progressData)
  },

  /**
   * 更新学习进度
   */
  updateProgress(progressId, progressData) {
    return api.put(`/progress/${progressId}`, progressData)
  },

  /**
   * 删除学习进度
   */
  deleteProgress(progressId) {
    return api.delete(`/progress/${progressId}`)
  },

  /**
   * 获取用户最近学习的课程
   */
  getRecentCourses(userId, limit = 5) {
    return api.get(`/progress/user/${userId}/recent`, {
      params: { limit }
    })
  },

  /**
   * 获取用户学习统计
   */
  getUserStatistics(userId) {
    return api.get(`/progress/user/${userId}/statistics`)
  },

  /**
   * 获取课程学习人数
   */
  getCourseStudentCount(courseId) {
    return api.get(`/progress/course/${courseId}/students`)
  },

  /**
   * 获取课程完成人数
   */
  getCourseCompletedCount(courseId) {
    return api.get(`/progress/course/${courseId}/completed`)
  },

  /**
   * 更新课时学习进度
   */
  updateLessonProgress(params) {
    return api.post('/progress/lesson', params)
  },

  /**
   * 完成课程
   */
  completeCourse(courseId, userId) {
    return api.post(`/progress/course/${courseId}/complete`, { userId })
  },

  /**
   * 批量获取课程进度
   */
  getBatchProgress(userId, courseIds) {
    return api.post('/progress/batch', {
      userId,
      courseIds
    })
  },

  /**
   * 获取学习排行榜
   */
  getLeaderboard(courseId, limit = 10) {
    return api.get(`/progress/course/${courseId}/leaderboard`, {
      params: { limit }
    })
  },

  /**
   * 获取学习日历数据
   */
  getStudyCalendar(userId, year, month) {
    return api.get(`/progress/user/${userId}/calendar`, {
      params: { year, month }
    })
  },

  /**
   * 导出学习报告
   */
  exportStudyReport(userId, courseId) {
    return api.get(`/progress/user/${userId}/course/${courseId}/report`, {
      responseType: 'blob'
    })
  }
}

export default progressService 