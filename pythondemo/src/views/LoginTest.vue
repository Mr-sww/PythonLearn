<template>
  <div class="login-test">
    <div class="card">
      <h2>登录测试</h2>
      <div class="form-group">
        <label>账号:</label>
        <input v-model="account" type="text" placeholder="请输入账号" />
      </div>
      <div class="form-group">
        <label>密码:</label>
        <input v-model="password" type="password" placeholder="请输入密码" />
      </div>
      <button @click="login" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
      <div v-if="message" class="message" :class="{ error: isError }">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginTest',
  data() {
    return {
      account: '',
      password: '',
      loading: false,
      message: '',
      isError: false
    }
  },
  methods: {
    async login() {
      if (!this.account || !this.password) {
        this.message = '请输入账号和密码';
        this.isError = true;
        return;
      }
      
      this.loading = true;
      this.message = '';
      
      try {
        const response = await axios.post('http://localhost:8080/api/user/login', {
          account: this.account,
          password: this.password
        }, {
          withCredentials: true
        });
        
        console.log('登录成功:', response.data);
        
        // 保存用户信息到localStorage
        localStorage.setItem('user', JSON.stringify(response.data));
        localStorage.setItem('userId', response.data.userId);
        
        this.message = '登录成功！正在跳转...';
        this.isError = false;
        
        // 跳转到学习页面
        setTimeout(() => {
          this.$router.push('/learn');
        }, 1000);
        
      } catch (error) {
        console.error('登录失败:', error);
        this.message = error.response?.data || '登录失败';
        this.isError = true;
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.login-test {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f5f5f5;
}

.card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 400px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 0.75rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.message {
  margin-top: 1rem;
  padding: 0.5rem;
  border-radius: 4px;
  background: #d4edda;
  color: #155724;
}

.message.error {
  background: #f8d7da;
  color: #721c24;
}
</style>


