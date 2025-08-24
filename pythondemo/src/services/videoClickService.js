import axios from 'axios';

// 创建axios实例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
  timeout: 10000
});

/**
 * 视频点击服务
 * 专门处理视频点击事件和学习记录
 */
export const videoClickService = {
    /**
   * 记录视频点击事件
   * @param {number} videoId - 视频课程ID
   */
  async recordVideoClick(videoId) {
    try {
      // 检查用户是否登录
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      if (!user || !user.userId) {
        console.warn('用户未登录，无法记录视频点击');
        return null;
      }
      
      console.log('记录视频点击:', {
        userId: user.userId,
        videoId
      });
      
      // 调用后端API记录点击事件
      const response = await apiClient.post('/api/learning/video-click', null, {
        params: { videoId }
      });
      
      console.log('视频点击记录成功:', response.data);
      return response.data;
      
    } catch (error) {
      console.error('记录视频点击失败:', error);
      // 不抛出错误，避免影响用户体验
      return null;
    }
  },

  /**
   * 检查知识点是否为视频类型
   * @param {string} url - 知识点URL
   * @param {string} contentType - 内容类型
   * @returns {boolean} 是否为视频
   */
  isVideoContent(url, contentType) {
    // 如果明确指定了内容类型
    if (contentType === 'video') {
      return true;
    }
    
    // 根据URL判断是否为视频
    if (url) {
      const videoPatterns = [
        /youtube\.com/i,
        /bilibili\.com/i,
        /youku\.com/i,
        /iqiyi\.com/i,
        /\.mp4$/i,
        /\.avi$/i,
        /\.mov$/i,
        /\.wmv$/i,
        /video/i
      ];
      
      return videoPatterns.some(pattern => pattern.test(url));
    }
    
    return false;
  },

  /**
   * 处理知识点点击事件
   * @param {Object} knowledgeItem - 知识点对象
   */
  async handleKnowledgeClick(knowledgeItem) {
    const isVideo = this.isVideoContent(knowledgeItem.url, knowledgeItem.contentType);
    
    if (isVideo) {
      // 如果是视频，记录点击事件
      await this.recordVideoClick(knowledgeItem.id);
    }
    
    // 返回是否为视频，供调用方使用
    return isVideo;
  }
};
