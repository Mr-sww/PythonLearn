import axios from 'axios';

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
      
      const response = await axios.post('/api/learning/knowledge/start', null, {
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
      const response = await axios.put('/api/learning/knowledge/progress', null, {
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
      const response = await axios.post('/api/learning/knowledge/complete', null, {
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
      const response = await axios.get('/api/learning/knowledge/records', {
        params: { limit }
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
      const response = await axios.get('/api/learning/knowledge/stats');
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
      const response = await axios.post('/api/learning/video/start', null, {
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
      const response = await axios.put('/api/learning/video/progress', null, {
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
      const response = await axios.post('/api/learning/video/complete', null, {
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
      const response = await axios.get('/api/learning/video/records', {
        params: { limit }
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
      const response = await axios.get('/api/learning/video/stats');
      return response.data;
    } catch (error) {
      console.error('获取观看统计失败:', error);
      throw error;
    }
  }
};
