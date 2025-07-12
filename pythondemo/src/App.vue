<template>
  <div>
    <GlobalNavbar />
    <router-view />
    <!-- 全局登录/注册弹窗 -->
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showLoginModal && !isLoggedIn">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
        <div class="p-8">
          <div class="flex justify-between items-center mb-6">
            <h3 class="text-2xl font-bold text-dark">用户登录</h3>
            <button @click="closeLoginModal" class="text-muted hover:text-dark">
              <i class="fa fa-times"></i>
            </button>
          </div>
          <form @submit.prevent="handleLogin">
            <div class="space-y-4">
              <div>
                <label for="username" class="block text-sm font-medium text-muted mb-1">用户名</label>
                <input type="text" id="username" v-model="loginForm.username" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入您的用户名">
              </div>
              <div>
                <label for="password" class="block text-sm font-medium text-muted mb-1">密码</label>
                <input type="password" id="password" v-model="loginForm.password" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入密码">
              </div>
              <div class="flex items-center justify-between">
                <div class="flex items-center">
                  <input type="checkbox" id="remember" class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded">
                  <label for="remember" class="ml-2 block text-sm text-muted">记住我</label>
                </div>
                <a href="#" class="text-sm font-medium text-primary hover:text-primary/80">忘记密码?</a>
              </div>
              <button type="submit" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50">
                登录
              </button>
              <div class="relative">
                <div class="absolute inset-0 flex items-center">
                  <div class="w-full border-t border-gray-300"></div>
                </div>
                <div class="relative flex justify-center text-xs uppercase">
                  <span class="px-2 bg-white text-muted">其他登录方式</span>
                </div>
              </div>
              <div class="grid grid-cols-2 gap-3">
                <button class="py-2 px-4 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 transition-all">
                  <i class="fa fa-github text-2xl"></i>
                </button>
                <button class="py-2 px-4 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 transition-all">
                  <i class="fa fa-google text-2xl"></i>
                </button>
              </div>
            </div>
          </form>
          <div class="mt-6 text-center">
            <span class="text-muted">还没有账号?</span>
            <button @click="openRegisterModal" class="ml-2 text-primary font-medium hover:text-primary/80">立即注册</button>
          </div>
        </div>
      </div>
    </div>
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showRegisterModal && !isLoggedIn">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
        <div class="p-8">
          <div class="flex justify-between items-center mb-6">
            <h3 class="text-2xl font-bold text-dark">注册账号</h3>
            <button @click="closeRegisterModal" class="text-muted hover:text-dark">
              <i class="fa fa-times"></i>
            </button>
          </div>
          <form @submit.prevent="handleRegister">
            <div class="space-y-4">
              <div>
                <label for="reg-nickname" class="block text-sm font-medium text-muted mb-1">昵称</label>
                <input type="text" id="reg-nickname" v-model="registerForm.nickname" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请设置您的昵称">
              </div>
              <div>
                <label for="reg-gender" class="block text-sm font-medium text-muted mb-1">性别</label>
                <select id="reg-gender" v-model="registerForm.gender" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all">
                  <option value="">请选择性别</option>
                  <option value="男">男</option>
                  <option value="女">女</option>
                  <option value="保密">保密</option>
                </select>
              </div>
              <div>
                <label for="reg-username" class="block text-sm font-medium text-muted mb-1">用户名</label>
                <input type="text" id="reg-username" v-model="registerForm.username" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请设置用户名">
              </div>
              <div>
                <label for="reg-email" class="block text-sm font-medium text-muted mb-1">邮箱</label>
                <input type="email" id="reg-email" v-model="registerForm.email" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入您的邮箱">
              </div>
              <div>
                <label for="reg-phone" class="block text-sm font-medium text-muted mb-1">手机号</label>
                <input type="tel" id="reg-phone" v-model="registerForm.phone" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入您的手机号">
              </div>
              <div>
                <label for="reg-password" class="block text-sm font-medium text-muted mb-1">密码</label>
                <input type="password" id="reg-password" v-model="registerForm.password" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请设置密码">
              </div>
              <div>
                <label for="reg-confirm-password" class="block text-sm font-medium text-muted mb-1">确认密码</label>
                <input type="password" id="reg-confirm-password" v-model="registerForm.confirmPassword" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请再次输入密码">
              </div>
              <div class="flex items-center">
                <input type="checkbox" id="terms" class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded">
                <label for="terms" class="ml-2 block text-sm text-muted">我已阅读并同意<a href="#" class="text-primary hover:text-primary/80">服务条款</a>和<a href="#" class="text-primary hover:text-primary/80">隐私政策</a></label>
              </div>
              <button type="submit" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50">
                注册
              </button>
            </div>
          </form>
          <div class="mt-6 text-center">
            <span class="text-muted">已有账号?</span>
            <button @click="openLoginModal" class="ml-2 text-primary font-medium hover:text-primary/80">立即登录</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GlobalNavbar from './components/GlobalNavbar.vue'
import { EventBus } from './eventBus';
import axios from 'axios';
export default {
  name: 'App',
  components: { GlobalNavbar },
  data() {
    return {
      showLoginModal: false,
      showRegisterModal: false,
      isLoggedIn: false,
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        nickname: '',
        gender: '',
        username: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  mounted() {
    EventBus.on('open-login-modal', this.openLoginModal);
    EventBus.on('open-register-modal', this.openRegisterModal);
  },
  beforeUnmount() {
    EventBus.off('open-login-modal', this.openLoginModal);
    EventBus.off('open-register-modal', this.openRegisterModal);
  },
  methods: {
    openLoginModal() {
      this.showLoginModal = true;
      this.showRegisterModal = false;
    },
    closeLoginModal() {
      this.showLoginModal = false;
    },
    openRegisterModal() {
      this.showRegisterModal = true;
      this.showLoginModal = false;
    },
    closeRegisterModal() {
      this.showRegisterModal = false;
    },
    handleLogin() {
      if (this.loginForm.username && this.loginForm.password) {
        axios.post('http://localhost:8080/api/user/login', {
          account: this.loginForm.username,
          password: this.loginForm.password
        }).then(res => {
          this.isLoggedIn = true;
          this.closeLoginModal();
          localStorage.setItem('userId', res.data.userId || res.data.user_id);
          EventBus.emit('user-logged-in');
        }).catch(() => {
          alert('登录失败');
        });
      } else {
        alert('请填写完整的登录信息');
      }
    },
    handleRegister() {
      if (this.registerForm.nickname && this.registerForm.username && this.registerForm.password) {
        if (this.registerForm.password !== this.registerForm.confirmPassword) {
          alert('两次输入的密码不一致');
          return;
        }
        axios.post('http://localhost:8080/api/user/register', {
          nickname: this.registerForm.nickname,
          account: this.registerForm.username,
          phone: this.registerForm.phone,
          password: this.registerForm.password,
          email: this.registerForm.email
        }).then(res => {
          this.isLoggedIn = true;
          this.closeRegisterModal();
          localStorage.setItem('userId', res.data.userId || res.data.user_id);
          EventBus.emit('user-logged-in');
        }).catch(() => {
          alert('注册失败');
        });
      } else {
        alert('请填写完整的注册信息');
      }
    }
  }
}
</script>

<style>
body {
  font-family: 'Inter', 'system-ui', 'sans-serif';
  background: #F8FAFC;
  color: #1E293B;
}
</style>
