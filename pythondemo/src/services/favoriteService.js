import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * 收藏课程服务
 */
class FavoriteService {
    
    /**
     * 获取用户收藏的课程列表
     * @param {number} userId 用户ID
     * @returns {Promise<Array>} 收藏课程列表
     */
    async getUserFavorites(userId) {
        try {
            const response = await axios.get(`${API_BASE_URL}/favorites/user/${userId}`);
            return response.data;
        } catch (error) {
            console.error('获取收藏课程失败:', error);
            throw error;
        }
    }
    
    /**
     * 添加收藏
     * @param {number} userId 用户ID
     * @param {number} courseId 课程ID
     * @returns {Promise<boolean>} 是否成功
     */
    async addFavorite(userId, courseId) {
        try {
            const response = await axios.post(`${API_BASE_URL}/favorites/add`, {
                userId,
                courseId
            });
            return response.status === 200;
        } catch (error) {
            console.error('添加收藏失败:', error);
            throw error;
        }
    }
    
    /**
     * 取消收藏
     * @param {number} userId 用户ID
     * @param {number} courseId 课程ID
     * @returns {Promise<boolean>} 是否成功
     */
    async removeFavorite(userId, courseId) {
        try {
            const response = await axios.post(`${API_BASE_URL}/favorites/remove`, {
                userId,
                courseId
            });
            return response.status === 200;
        } catch (error) {
            console.error('取消收藏失败:', error);
            throw error;
        }
    }
    
    /**
     * 检查是否已收藏
     * @param {number} userId 用户ID
     * @param {number} courseId 课程ID
     * @returns {Promise<boolean>} 是否已收藏
     */
    async isFavorited(userId, courseId) {
        try {
            const response = await axios.get(`${API_BASE_URL}/favorites/check/${userId}/${courseId}`);
            return response.data;
        } catch (error) {
            console.error('检查收藏状态失败:', error);
            return false;
        }
    }
    
    /**
     * 获取用户收藏数量
     * @param {number} userId 用户ID
     * @returns {Promise<number>} 收藏数量
     */
    async getFavoriteCount(userId) {
        try {
            const response = await axios.get(`${API_BASE_URL}/favorites/count/${userId}`);
            return response.data;
        } catch (error) {
            console.error('获取收藏数量失败:', error);
            return 0;
        }
    }
}

export default new FavoriteService(); 