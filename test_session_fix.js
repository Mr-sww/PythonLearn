// 测试Session修复的简单脚本
const axios = require('axios');

// 模拟前端环境
const localStorage = {
  user: JSON.stringify({
    userId: 1,
    nickname: '测试用户',
    account: 'testuser'
  }),
  userId: '1',
  isLoggedIn: 'true'
};

// 创建axios实例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
  timeout: 10000
});

async function testSessionFix() {
  console.log('=== 测试Session修复 ===');
  
  try {
    // 获取用户信息
    const user = JSON.parse(localStorage.user || 'null');
    const userId = user ? (user.userId || user.user_id) : null;
    
    console.log('用户信息:', user);
    console.log('用户ID:', userId);
    
    // 测试学习记录API
    console.log('\n测试获取学习记录...');
    const recordsResponse = await apiClient.get('/api/learning/knowledge/records', {
      params: { limit: 5 },
      headers: {
        'X-User-ID': userId
      }
    });
    console.log('学习记录响应:', recordsResponse.data);
    
    // 测试学习统计API
    console.log('\n测试获取学习统计...');
    const statsResponse = await apiClient.get('/api/learning/knowledge/stats', {
      headers: {
        'X-User-ID': userId
      }
    });
    console.log('学习统计响应:', statsResponse.data);
    
    console.log('\n✅ 所有测试通过！');
    
  } catch (error) {
    console.error('❌ 测试失败:', error.message);
    if (error.response) {
      console.error('响应状态:', error.response.status);
      console.error('响应数据:', error.response.data);
    }
  }
}

// 运行测试
testSessionFix();
