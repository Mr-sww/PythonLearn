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
 * 评论相关API服务
 */
export const commentService = {
  /**
   * 获取课程评论列表
   */
  getCommentsByCourseId(courseId) {
    return api.get(`/comments/course/${courseId}`)
  },

  /**
   * 获取评论详情
   */
  getCommentById(commentId) {
    return api.get(`/comments/${commentId}`)
  },

  /**
   * 创建评论
   */
  createComment(commentData) {
    return api.post('/comments', commentData)
  },

  /**
   * 更新评论
   */
  updateComment(commentId, commentData) {
    return api.put(`/comments/${commentId}`, commentData)
  },

  /**
   * 删除评论
   */
  deleteComment(commentId) {
    return api.delete(`/comments/${commentId}`)
  },

  /**
   * 点赞评论
   */
  likeComment(commentId) {
    return api.post(`/comments/${commentId}/like`)
  },

  /**
   * 取消点赞评论
   */
  unlikeComment(commentId) {
    return api.post(`/comments/${commentId}/unlike`)
  },

  /**
   * 获取课程评论总数
   */
  getCommentCountByCourseId(courseId) {
    return api.get(`/comments/course/${courseId}/count`)
  },

  /**
   * 获取课程平均评分
   */
  getAverageRatingByCourseId(courseId) {
    return api.get(`/comments/course/${courseId}/rating`)
  },

  /**
   * 获取用户评论列表
   */
  getCommentsByUserId(userId) {
    return api.get(`/comments/user/${userId}`)
  }
}

export default commentService 