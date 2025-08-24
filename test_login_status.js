// 测试登录状态的脚本
const axios = require('axios');

async function testLoginStatus() {
  console.log('=== 测试登录状态 ===');
  
  try {
    // 1. 测试登录接口
    console.log('\n1. 测试登录...');
    const loginResponse = await axios.post('http://localhost:8080/api/user/login', {
      account: 'testuser', // 替换为实际的用户名
      password: '123456'   // 替换为实际的密码
    }, {
      withCredentials: true
    });
    
    console.log('登录成功:', loginResponse.data);
    
    // 2. 测试获取学习记录（使用Session）
    console.log('\n2. 测试获取学习记录（Session认证）...');
    try {
      const recordsResponse = await axios.get('http://localhost:8080/api/learning/knowledge/records', {
        params: { limit: 5 },
        withCredentials: true
      });
      console.log('学习记录（Session）:', recordsResponse.data);
    } catch (error) {
      console.log('Session认证失败:', error.response?.data);
    }
    
    // 3. 测试获取学习记录（请求头认证）
    console.log('\n3. 测试获取学习记录（请求头认证）...');
    try {
      const recordsResponse2 = await axios.get('http://localhost:8080/api/learning/knowledge/records', {
        params: { limit: 5 },
        headers: {
          'X-User-ID': loginResponse.data.userId
        },
        withCredentials: true
      });
      console.log('学习记录（请求头）:', recordsResponse2.data);
    } catch (error) {
      console.log('请求头认证失败:', error.response?.data);
    }
    
  } catch (error) {
    console.error('测试失败:', error.message);
    if (error.response) {
      console.error('响应状态:', error.response.status);
      console.error('响应数据:', error.response.data);
    }
  }
}

// 运行测试
testLoginStatus();
