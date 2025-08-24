import axios from 'axios';

// 创建axios实例，确保withCredentials设置正确
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
  timeout: 10000
});

// 配置axios默认设置
axios.defaults.withCredentials = true;
axios.defaults.baseURL = 'http://localhost:8080';

/**
 * 学习记录服务
 */
export const learningRecordService = {
  // 知识点学习记录相关方法

  /**
   * 开始知识点学习
   */
  async startKnowledgeStudy(knowledgeId, knowledgeTitle) {
    try {
      // 检查用户是否登录
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      if (!user || !user.userId) {
        throw new Error('用户未登录');
      }
      
      console.log('开始学习记录，用户ID:', user.userId, '知识点ID:', knowledgeId);
      
      const response = await apiClient.post('/api/learning/knowledge/start', null, {
        params: {
          knowledgeId,
          knowledgeTitle
        }
      });
      return response.data;
    } catch (error) {
      console.error('开始学习失败:', error);
      if (error.response?.data) {
        console.error('服务器响应:', error.response.data);
      }
      throw error;
    }
  },

  /**
   * 更新知识点学习进度
   */
  async updateKnowledgeProgress(knowledgeId, studyTime, progress) {
    try {
      const response = await apiClient.put('/api/learning/knowledge/progress', null, {
        params: {
          knowledgeId,
          studyTime,
          progress
        }
      });
      return response.data;
    } catch (error) {
      console.error('更新学习进度失败:', error);
      throw error;
    }
  },

  /**
   * 完成知识点学习
   */
  async completeKnowledgeStudy(knowledgeId) {
    try {
      const response = await apiClient.post('/api/learning/knowledge/complete', null, {
        params: {
          knowledgeId
        }
      });
      return response.data;
    } catch (error) {
      console.error('完成学习失败:', error);
      throw error;
    }
  },

  /**
   * 获取知识点学习记录
   */
  async getKnowledgeRecords(limit = 10) {
    try {
      // 从localStorage获取用户信息
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      const userId = user ? (user.userId || user.user_id) : null;
      
      console.log('=== 前端调试信息 ===');
      console.log('localStorage中的user:', user);
      console.log('提取的userId:', userId);
      console.log('localStorage中的userId:', localStorage.getItem('userId'));
      console.log('localStorage中的isLoggedIn:', localStorage.getItem('isLoggedIn'));
      
      const response = await apiClient.get('/api/learning/knowledge/records', {
        params: { limit },
        headers: {
          'X-User-ID': userId
        }
      });
      return response.data;
    } catch (error) {
      console.error('获取学习记录失败:', error);
      throw error;
    }
  },

  /**
   * 获取知识点学习统计
   */
  async getKnowledgeStats() {
    try {
      // 从localStorage获取用户信息
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      const userId = user ? (user.userId || user.user_id) : null;
      
      console.log('=== 前端调试信息（统计） ===');
      console.log('localStorage中的user:', user);
      console.log('提取的userId:', userId);
      console.log('localStorage中的userId:', localStorage.getItem('userId'));
      console.log('localStorage中的isLoggedIn:', localStorage.getItem('isLoggedIn'));
      
      const response = await apiClient.get('/api/learning/knowledge/stats', {
        headers: {
          'X-User-ID': userId
        }
      });
      return response.data;
    } catch (error) {
      console.error('获取学习统计失败:', error);
      throw error;
    }
  },

  // 视频观看记录相关方法

  /**
   * 开始视频观看
   */
  async startVideoWatch(videoId, videoTitle, videoUrl, totalDuration = 0) {
    try {
      const response = await apiClient.post('/api/learning/video/start', null, {
        params: {
          videoId,
          videoTitle,
          videoUrl,
          totalDuration
        }
      });
      return response.data;
    } catch (error) {
      console.error('开始观看失败:', error);
      throw error;
    }
  },

  /**
   * 更新视频观看进度
   */
  async updateVideoProgress(videoId, watchTime, progress) {
    try {
      const response = await apiClient.put('/api/learning/video/progress', null, {
        params: {
          videoId,
          watchTime,
          progress
        }
      });
      return response.data;
    } catch (error) {
      console.error('更新观看进度失败:', error);
      throw error;
    }
  },

  /**
   * 完成视频观看
   */
  async completeVideoWatch(videoId) {
    try {
      const response = await apiClient.post('/api/learning/video/complete', null, {
        params: {
          videoId
        }
      });
      return response.data;
    } catch (error) {
      console.error('完成观看失败:', error);
      throw error;
    }
  },

  /**
   * 获取视频观看记录
   */
  async getVideoRecords(limit = 10) {
    try {
      // 从localStorage获取用户信息
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      const userId = user ? (user.userId || user.user_id) : null;
      
      console.log('=== 前端调试信息（视频记录） ===');
      console.log('localStorage中的user:', user);
      console.log('提取的userId:', userId);
      
      const response = await apiClient.get('/api/learning/video/records', {
        params: { limit },
        headers: {
          'X-User-ID': userId
        }
      });
      return response.data;
    } catch (error) {
      console.error('获取观看记录失败:', error);
      throw error;
    }
  },

  /**
   * 获取视频观看统计
   */
  async getVideoStats() {
    try {
      // 从localStorage获取用户信息
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      const userId = user ? (user.userId || user.user_id) : null;
      
      console.log('=== 前端调试信息（视频统计） ===');
      console.log('localStorage中的user:', user);
      console.log('提取的userId:', userId);
      
      const response = await apiClient.get('/api/learning/video/stats', {
        headers: {
          'X-User-ID': userId
        }
      });
      return response.data;
    } catch (error) {
      console.error('获取观看统计失败:', error);
      throw error;
    }
  }
};
