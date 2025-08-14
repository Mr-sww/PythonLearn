<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center py-6 px-4 sm:px-6 lg:px-8 relative overflow-hidden" @mousemove="handleMouseMove">
    <!-- 动态背景粒子 -->
    <div class="absolute inset-0 pointer-events-none">
      <div 
        v-for="(particle, index) in particles" 
        :key="index"
        :style="{
          left: particle.x + '%',
          top: particle.y + '%',
          transform: `translate(${particle.offsetX}px, ${particle.offsetY}px) scale(${particle.scale}) rotate(${particle.rotation}deg)`,
          opacity: particle.opacity
        }"
        class="absolute transition-all duration-1000 ease-out"
      >
        <!-- 蓝色玫瑰花粒子 -->
        <div class="relative rose-particle">
          <!-- 花瓣层 -->
          <div class="absolute inset-0">
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-45 absolute top-0 left-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-90 absolute top-0 left-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-135 absolute top-0 left-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-180 absolute top-0 left-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-225 absolute top-0 left-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-270 absolute top-0 left-0 shadow-lg"></div>
            <div class="w-3 h-3 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full transform rotate-315 absolute top-0 left-0 shadow-lg"></div>
          </div>
          <!-- 花心 -->
          <div class="w-1.5 h-1.5 bg-gradient-to-br from-blue-300 to-blue-500 rounded-full absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 shadow-md"></div>
        </div>
      </div>
    </div>

    <!-- 动态光晕效果 -->
    <div 
      class="absolute w-96 h-96 bg-gradient-to-r from-blue-300/20 to-indigo-300/20 rounded-full blur-3xl pointer-events-none transition-all duration-500 ease-out"
      :style="{
        left: mouseX + 'px',
        top: mouseY + 'px',
        transform: 'translate(-50%, -50%)'
      }"
    ></div>

    <!-- 浮动装饰元素 -->
    <div class="absolute inset-0 pointer-events-none">
      <div 
        v-for="(decoration, index) in decorations" 
        :key="'dec-' + index"
        :style="{
          left: decoration.x + '%',
          top: decoration.y + '%',
          transform: `translate(${decoration.offsetX}px, ${decoration.offsetY}px) rotate(${decoration.rotation}deg)`,
          opacity: decoration.opacity
        }"
        class="absolute w-8 h-8 border-2 border-blue-200/30 rounded-full transition-all duration-2000 ease-in-out"
      ></div>
    </div>

    <div class="max-w-md w-full space-y-4 relative z-10">
      <!-- 页面标题 -->
      <div class="text-center">
        <div class="mx-auto h-16 w-16 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full flex items-center justify-center mb-4 shadow-lg transform hover:scale-110 transition-all duration-300 hover:shadow-xl">
          <i class="fa fa-code text-white text-2xl"></i>
        </div>
        <h2 class="text-3xl font-bold text-gray-900 mb-2 transform hover:scale-105 transition-all duration-300">Python学习平台</h2>
        <p class="text-gray-600 text-base">欢迎加入我们的学习社区</p>
      </div>

      <!-- 登录/注册切换 -->
      <div class="bg-white/80 backdrop-blur-xl rounded-2xl shadow-xl p-6 border border-white/30 transform hover:scale-[1.02] transition-all duration-300 hover:shadow-2xl hover:bg-white/90">
        <!-- 切换按钮 -->
        <div class="flex mb-6 bg-gray-100/80 rounded-lg p-1">
          <button 
            @click="switchToLogin" 
            :class="[
              'flex-1 py-3 px-4 rounded-md font-semibold transition-all duration-300',
              isLoginMode 
                ? 'bg-white text-blue-600 shadow-md transform scale-105' 
                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
            ]"
          >
            登录
          </button>
          <button 
            @click="switchToRegister" 
            :class="[
              'flex-1 py-3 px-4 rounded-md font-semibold transition-all duration-300',
              !isLoginMode 
                ? 'bg-white text-blue-600 shadow-md transform scale-105' 
                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
            ]"
          >
            注册
          </button>
        </div>

        <!-- 登录表单 -->
        <form v-if="isLoginMode" @submit.prevent="handleLogin" class="space-y-4">
          <div class="transform hover:scale-[1.02] transition-all duration-200">
            <label for="username" class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="loginForm.username" 
              required
              class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
              placeholder="请输入您的用户名"
            >
          </div>

          <div class="transform hover:scale-[1.02] transition-all duration-200">
            <label for="password" class="block text-sm font-medium text-gray-700 mb-1">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="loginForm.password" 
              required
              class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
              placeholder="请输入密码"
            >
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <input type="checkbox" id="remember" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded">
              <label for="remember" class="ml-2 block text-sm text-gray-700">记住我</label>
            </div>
            <a href="#" class="text-sm font-medium text-blue-600 hover:text-blue-500 transition-colors">忘记密码?</a>
          </div>

          <button 
            type="submit" 
            :disabled="isLoading"
            class="w-full py-3 px-4 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold text-base transition-all hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed shadow-md hover:shadow-lg transform hover:scale-105 hover:-translate-y-1"
          >
            {{ isLoading ? '登录中...' : '登录' }}
          </button>

          <!-- 第三方登录 -->
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300"></div>
            </div>
            <div class="relative flex justify-center text-xs uppercase">
              <span class="px-2 bg-white/90 backdrop-blur-sm text-gray-500">其他登录方式</span>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <button type="button" class="w-full py-2 px-3 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 transition-all font-medium text-sm transform hover:scale-105 hover:-translate-y-1">
              <i class="fa fa-github text-lg mr-2"></i>
              GitHub
            </button>
            <button type="button" class="w-full py-2 px-3 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 transition-all font-medium text-sm transform hover:scale-105 hover:-translate-y-1">
              <i class="fa fa-google text-lg mr-2"></i>
              Google
            </button>
          </div>
        </form>

        <!-- 注册表单 -->
        <form v-else @submit.prevent="handleRegister" class="space-y-4">
          <div class="grid grid-cols-2 gap-3">
            <div class="transform hover:scale-[1.02] transition-all duration-200">
              <label for="reg-nickname" class="block text-sm font-medium text-gray-700 mb-1">昵称</label>
              <input 
                type="text" 
                id="reg-nickname" 
                v-model="registerForm.nickname" 
                required
                class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
                placeholder="请设置您的昵称"
              >
            </div>

            <div class="transform hover:scale-[1.02] transition-all duration-200">
              <label for="reg-gender" class="block text-sm font-medium text-gray-700 mb-1">性别</label>
              <select 
                id="reg-gender" 
                v-model="registerForm.gender" 
                required
                class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
              >
                <option value="">请选择性别</option>
                <option value="男">男</option>
                <option value="女">女</option>
                <option value="保密">保密</option>
              </select>
            </div>
          </div>

          <div class="transform hover:scale-[1.02] transition-all duration-200">
            <label for="reg-username" class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
            <input 
              type="text" 
              id="reg-username" 
              v-model="registerForm.username" 
              required
              class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
              placeholder="请设置用户名"
            >
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="transform hover:scale-[1.02] transition-all duration-200">
              <label for="reg-email" class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
              <input 
                type="email" 
                id="reg-email" 
                v-model="registerForm.email" 
                required
                class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
                placeholder="请输入您的邮箱"
              >
            </div>

            <div class="transform hover:scale-[1.02] transition-all duration-200">
              <label for="reg-phone" class="block text-sm font-medium text-gray-700 mb-1">手机号</label>
              <input 
                type="tel" 
                id="reg-phone" 
                v-model="registerForm.phone" 
                required
                class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
                placeholder="请输入您的手机号"
              >
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="transform hover:scale-[1.02] transition-all duration-200">
              <label for="reg-password" class="block text-sm font-medium text-gray-700 mb-1">密码</label>
              <input 
                type="password" 
                id="reg-password" 
                v-model="registerForm.password" 
                required
                class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
                placeholder="请设置密码"
              >
            </div>

            <div class="transform hover:scale-[1.02] transition-all duration-200">
              <label for="reg-confirm-password" class="block text-sm font-medium text-gray-700 mb-1">确认密码</label>
              <input 
                type="password" 
                id="reg-confirm-password" 
                v-model="registerForm.confirmPassword" 
                required
                class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
                placeholder="请再次输入密码"
              >
            </div>
          </div>

          <div class="transform hover:scale-[1.02] transition-all duration-200">
            <label for="reg-major" class="block text-sm font-medium text-gray-700 mb-1">专业选择</label>
            <select 
              id="reg-major" 
              v-model="registerForm.major" 
              required
              class="w-full px-3 py-3 border border-gray-300/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-base bg-white/70 backdrop-blur-md hover:bg-white/90 focus:bg-white/95"
            >
              <option value="">请选择您的专业</option>
              <option value="1">计算机类</option>
              <option value="2">工设类</option>
              <option value="3">艺术类</option>
              <option value="4">医学类</option>
              <option value="5">文科类</option>
              <option value="6">体育类</option>
              <option value="7">其他</option>
            </select>
          </div>

          <div class="flex items-start">
            <input 
              type="checkbox" 
              id="terms" 
              v-model="registerForm.agreeTerms" 
              required
              class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded mt-1"
            >
            <label for="terms" class="ml-2 block text-sm text-gray-700">
              我已阅读并同意<a href="#" class="text-blue-600 hover:text-blue-500 transition-colors">服务条款</a>和<a href="#" class="text-blue-600 hover:text-blue-500 transition-colors">隐私政策</a>
            </label>
          </div>

          <button 
            type="submit" 
            :disabled="isLoading"
            class="w-full py-3 px-4 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold text-base transition-all hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed shadow-md hover:shadow-lg transform hover:scale-105 hover:-translate-y-1"
          >
            {{ isLoading ? '注册中...' : '注册' }}
          </button>
        </form>

        <!-- 切换提示 -->
        <div class="mt-6 text-center">
          <span v-if="isLoginMode" class="text-gray-600 text-base">还没有账号?</span>
          <span v-else class="text-gray-600 text-base">已有账号?</span>
          <button 
            @click="isLoginMode ? switchToRegister() : switchToLogin()"
            class="ml-2 text-blue-600 font-semibold hover:text-blue-500 text-base transition-colors hover:scale-105 transform"
          >
            {{ isLoginMode ? '立即注册' : '立即登录' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 专业选择模态框 -->
    <div v-if="showMajorModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-md">
      <div class="bg-white/90 backdrop-blur-xl rounded-2xl shadow-xl w-full max-w-md overflow-hidden transform hover:scale-[1.02] transition-all duration-300 border border-white/30">
        <div class="p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-xl font-bold text-gray-900">选择您的专业大类</h3>
            <button @click="closeMajorModal" class="text-gray-400 hover:text-gray-600 transition-colors transform hover:scale-110">
              <i class="fa fa-times"></i>
            </button>
          </div>
          
          <div class="grid grid-cols-2 gap-3 mb-4">
            <label v-for="(name, value) in majorOptions" :key="value" class="border border-gray-300 rounded-lg p-3 cursor-pointer hover:border-blue-500 transition-all flex items-center transform hover:scale-105 hover:shadow-md">
              <input type="radio" :value="parseInt(value)" v-model="user.groupType" class="mr-2">
              <span class="font-medium text-sm">{{ name }}</span>
            </label>
          </div>
          
          <button 
            @click="completeRegistration" 
            :disabled="!user.groupType"
            class="w-full py-3 px-4 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold text-base transition-all hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed shadow-md hover:shadow-lg transform hover:scale-105 hover:-translate-y-1"
          >
            完成注册
          </button>
        </div>
      </div>
    </div>

    <!-- 学习方向选择模态框 -->
    <div v-if="showLearningDirectionModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-md">
      <div class="bg-white/90 backdrop-blur-xl rounded-2xl shadow-xl w-full max-w-2xl overflow-hidden transform hover:scale-[1.02] transition-all duration-300 border border-white/30">
        <div class="p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-xl font-bold text-gray-900">选择您的学习方向</h3>
            <button @click="closeLearningDirectionModal" class="text-gray-400 hover:text-gray-600 transition-colors transform hover:scale-110">
              <i class="fa fa-times"></i>
            </button>
          </div>
          
          <p class="text-gray-600 mb-4 text-sm">请选择您感兴趣的学习方向，我们将为您推荐相关课程和练习</p>
          
          <div class="grid grid-cols-2 gap-3 mb-4">
            <label v-for="(name, value) in learningDirectionOptions" :key="value" class="border border-gray-300 rounded-lg p-3 cursor-pointer hover:border-blue-500 transition-all flex items-center transform hover:scale-105 hover:shadow-md">
              <input type="checkbox" :value="parseInt(value)" v-model="user.intestTypes" class="mr-2">
              <span class="font-medium text-sm">{{ name }}</span>
            </label>
          </div>
          
          <button 
            @click="saveLearningDirections" 
            :disabled="user.intestTypes.length === 0"
            class="w-full py-3 px-4 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold text-base transition-all hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed shadow-md hover:shadow-lg transform hover:scale-105 hover:-translate-y-1"
          >
            保存并开始学习
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { EventBus } from '../eventBus'
import { setLoginState, getAndClearRedirectPath } from '../utils/auth'

export default {
  name: 'AuthPage',
  data() {
    return {
      isLoginMode: true,
      isLoading: false,
      showMajorModal: false,
      showLearningDirectionModal: false,
      
      // 鼠标位置
      mouseX: 0,
      mouseY: 0,
      
      // 背景粒子
      particles: [],
      
      // 装饰元素
      decorations: [],
      
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
        confirmPassword: '',
        major: '',
        agreeTerms: false
      },
      
      user: {
        userId: null,
        groupType: null,
        intestTypes: []
      },
      
      majorOptions: {
        1: '计算机类',
        2: '工设类',
        3: '艺术类',
        4: '医学类',
        5: '文科类',
        6: '体育类',
        7: '其他'
      },
      
      learningDirectionOptions: {
        1: 'Python基础',
        2: 'Web开发',
        3: '数据分析',
        4: '人工智能',
        5: '自动化脚本',
        6: '游戏开发'
      }
    }
  },
  
  mounted() {
    // 检查URL参数，决定显示登录还是注册
    const mode = this.$route.query.mode
    if (mode === 'register') {
      this.switchToRegister()
    } else if (mode === 'login') {
      this.switchToLogin()
    }
    
    // 初始化背景动画
    this.initBackgroundAnimation()
    
    // 设置鼠标位置为屏幕中心
    this.mouseX = window.innerWidth / 2
    this.mouseY = window.innerHeight / 2
  },
  
  beforeUnmount() {
    // 清理动画定时器
    if (this.animationTimer) {
      clearInterval(this.animationTimer)
    }
  },
  
  methods: {
    // 初始化背景动画
    initBackgroundAnimation() {
      // 创建粒子
      this.particles = Array.from({ length: 15 }, () => ({
        x: Math.random() * 100,
        y: Math.random() * 100,
        offsetX: 0,
        offsetY: 0,
        scale: 0.5 + Math.random() * 1,
        opacity: 0.3 + Math.random() * 0.4,
        rotation: Math.random() * 360
      }))
      
      // 创建装饰元素
      this.decorations = Array.from({ length: 8 }, () => ({
        x: Math.random() * 100,
        y: Math.random() * 100,
        offsetX: 0,
        offsetY: 0,
        rotation: Math.random() * 360,
        opacity: 0.1 + Math.random() * 0.2
      }))
      
      // 启动动画循环
      this.animationTimer = setInterval(() => {
        this.updateParticles()
        this.updateDecorations()
      }, 50)
    },
    
    // 更新粒子位置
    updateParticles() {
      this.particles.forEach(particle => {
        // 根据鼠标位置计算偏移
        const mouseInfluence = 0.1
        const targetOffsetX = (this.mouseX / window.innerWidth - 0.5) * 50 * mouseInfluence
        const targetOffsetY = (this.mouseY / window.innerHeight - 0.5) * 50 * mouseInfluence
        
        // 平滑过渡
        particle.offsetX += (targetOffsetX - particle.offsetX) * 0.05
        particle.offsetY += (targetOffsetY - particle.offsetY) * 0.05
        
        // 添加轻微的浮动效果
        particle.offsetX += Math.sin(Date.now() * 0.001 + particle.x) * 0.5
        particle.offsetY += Math.cos(Date.now() * 0.001 + particle.y) * 0.5
        
        // 添加旋转效果
        particle.rotation += 0.5
      })
    },
    
    // 更新装饰元素
    updateDecorations() {
      this.decorations.forEach((decoration, index) => {
        // 缓慢旋转
        decoration.rotation += 0.2
        
        // 根据鼠标位置轻微移动
        const mouseInfluence = 0.05
        const targetOffsetX = (this.mouseX / window.innerWidth - 0.5) * 30 * mouseInfluence
        const targetOffsetY = (this.mouseY / window.innerHeight - 0.5) * 30 * mouseInfluence
        
        decoration.offsetX += (targetOffsetX - decoration.offsetX) * 0.03
        decoration.offsetY += (targetOffsetY - decoration.offsetY) * 0.03
        
        // 添加呼吸效果
        decoration.opacity = 0.1 + Math.sin(Date.now() * 0.002 + index) * 0.1
      })
    },
    
    // 处理鼠标移动
    handleMouseMove(event) {
      this.mouseX = event.clientX
      this.mouseY = event.clientY
    },
    
    switchToLogin() {
      this.isLoginMode = true
      this.resetForms()
    },
    
    switchToRegister() {
      this.isLoginMode = false
      this.resetForms()
    },
    
    resetForms() {
      this.loginForm = { username: '', password: '' }
      this.registerForm = {
        nickname: '', gender: '', username: '', email: '', phone: '',
        password: '', confirmPassword: '', major: '', agreeTerms: false
      }
    },
    
    async handleLogin() {
      if (!this.loginForm.username || !this.loginForm.password) {
        this.$message?.error('请填写完整的登录信息')
        return
      }
      
      this.isLoading = true
      try {
        const response = await axios.post('http://localhost:8080/api/user/login', {
          account: this.loginForm.username,
          password: this.loginForm.password
        })
        
                  const user = response.data
          console.log('登录响应数据:', user) // 添加调试信息
          
          if (user && (user.userId || user.user_id)) {
            console.log('用户数据:', user) // 添加调试信息
            console.log('用户groupType:', user.groupType || user.group_type) // 添加调试信息
            console.log('用户intestTypes:', user.intestTypes || user.intest_types) // 添加调试信息
            this.user = user
            this.user.userId = user.userId || user.user_id
            this.user.groupType = user.groupType || user.group_type
            this.user.intestTypes = this.parseIntestTypes(user.intestTypes || user.intest_types)
          
          // 保存用户信息和登录状态
          setLoginState(user)
          console.log('登录状态已保存') // 添加调试信息
          
          // 检查是否需要选择专业或学习方向
          if (!this.user.groupType) {
            console.log('需要选择专业') // 添加调试信息
            this.showMajorModal = true
          } else if (!this.user.intestTypes || this.user.intestTypes.length === 0) {
            console.log('需要选择学习方向') // 添加调试信息
            this.showLearningDirectionModal = true
          } else {
            // 登录成功，发送登录事件
            console.log('登录成功，准备跳转') // 添加调试信息
            EventBus.emit('user-logged-in')
            this.$message?.success('登录成功！')
            
            // 检查是否有保存的跳转路径
            const redirectPath = getAndClearRedirectPath()
            console.log('跳转路径:', redirectPath) // 添加调试信息
            if (redirectPath) {
              console.log('跳转到保存的路径:', redirectPath) // 添加调试信息
              this.$router.push(redirectPath)
            } else {
              console.log('跳转到首页') // 添加调试信息
              this.$router.push('/')
            }
          }
        } else {
          console.log('登录失败: 用户数据无效') // 添加调试信息
          this.$message?.error('登录失败')
        }
      } catch (error) {
        console.error('登录失败:', error)
        let errorMsg = '登录失败，请检查网络连接'
        if (error.response?.data?.message) {
          errorMsg = error.response.data.message
        } else if (error.response?.status === 400) {
          errorMsg = '用户名或密码错误'
        } else if (error.response?.status === 500) {
          errorMsg = '服务器错误，请稍后重试'
        }
        this.$message?.error(errorMsg)
      } finally {
        this.isLoading = false
      }
    },
    
    async handleRegister() {
      if (!this.validateRegisterForm()) {
        return
      }
      
      this.isLoading = true
      try {
        const response = await axios.post('http://localhost:8080/api/user/register', {
          nickname: this.registerForm.nickname,
          account: this.registerForm.username,
          phone: this.registerForm.phone,
          password: this.registerForm.password,
          groupType: parseInt(this.registerForm.major),
          intestTypes: '',
          email: this.registerForm.email
        })
        
        const user = response.data
        if (user && user.userId) {
          this.$message?.success('注册成功！请登录')
          this.resetRegisterForm()
          
          // 切换到登录模式
          this.switchToLogin()
        } else if (typeof user === 'string') {
          // 后端返回错误消息字符串
          this.$message?.error(user)
        } else {
          this.$message?.error('注册失败')
        }
      } catch (error) {
        console.error('注册失败:', error)
        let errorMsg = '注册失败，请检查网络连接'
        if (error.response?.data?.message) {
          errorMsg = error.response.data.message
        } else if (error.response?.status === 400) {
          errorMsg = '注册信息有误，请检查输入'
        } else if (error.response?.status === 500) {
          errorMsg = '服务器错误，请稍后重试'
        }
        this.$message?.error(errorMsg)
      } finally {
        this.isLoading = false
      }
    },
    
    validateRegisterForm() {
      if (!this.registerForm.nickname || !this.registerForm.username || !this.registerForm.password) {
        this.$message?.error('请填写完整的注册信息')
        return false
      }
      
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.$message?.error('两次输入的密码不一致')
        return false
      }
      
      if (!this.registerForm.major) {
        this.$message?.error('请选择您的专业')
        return false
      }
      
      if (!this.registerForm.agreeTerms) {
        this.$message?.error('请同意服务条款和隐私政策')
        return false
      }
      
      return true
    },
    
    resetRegisterForm() {
      this.registerForm = {
        nickname: '', gender: '', username: '', email: '', phone: '',
        password: '', confirmPassword: '', major: '', agreeTerms: false
      }
    },
    
    async completeRegistration() {
      if (!this.user.groupType) {
        this.$message?.error('请选择您的专业')
        return
      }
      
      try {
        await axios.patch(`http://localhost:8080/api/user/${this.user.userId}/groupType`, {
          groupType: this.user.groupType
        })
        
        this.closeMajorModal()
        this.showLearningDirectionModal = true
        this.$message?.success('专业设置成功！')
      } catch (error) {
        console.error('保存专业失败:', error)
        this.$message?.error('保存专业失败，请稍后重试')
      }
    },
    
    async saveLearningDirections() {
      if (this.user.intestTypes.length === 0) {
        this.$message?.error('请至少选择一个学习方向')
        return
      }
      
      try {
        const intestTypesStr = this.user.intestTypes.join(',')
        await axios.patch(`http://localhost:8080/api/user/${this.user.userId}/intestTypes`, {
          intestTypes: intestTypesStr
        })
        
        this.closeLearningDirectionModal()
        this.$message?.success('设置完成！欢迎加入Python学习平台')
        
        // 发送用户登录事件
        EventBus.emit('user-logged-in')
        this.$router.push('/')
      } catch (error) {
        console.error('保存学习方向失败:', error)
        this.$message?.error('保存学习方向失败，请稍后重试')
      }
    },
    
    closeMajorModal() {
      this.showMajorModal = false
    },
    
    closeLearningDirectionModal() {
      this.showLearningDirectionModal = false
    },
    
    parseIntestTypes(intestTypes) {
      if (Array.isArray(intestTypes)) return intestTypes
      if (typeof intestTypes === 'string' && intestTypes.trim() && intestTypes !== 'null') {
        return intestTypes.split(/[,，\s]+/).filter(x => x).map(Number)
      }
      return []
    }
  }
}
</script>

<style scoped>
/* 自定义样式 */
.bg-gradient-to-br {
  background: linear-gradient(to bottom right, var(--tw-gradient-stops));
}

.from-blue-50 {
  --tw-gradient-from: #eff6ff;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to, rgba(239, 246, 255, 0));
}

.to-indigo-100 {
  --tw-gradient-to: #e0e7ff;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .max-w-md {
    max-width: 100%;
    margin: 0 1rem;
  }
  
  .p-8 {
    padding: 1.5rem;
  }
}

/* 动画优化 */
* {
  will-change: transform, opacity;
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 增强毛玻璃效果 */
.backdrop-blur-xl {
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
}

.backdrop-blur-md {
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

/* 蓝色玫瑰花粒子样式 */
.rose-particle {
  filter: drop-shadow(0 0 8px rgba(59, 130, 246, 0.3));
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(59, 130, 246, 0.5);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(59, 130, 246, 0.7);
}
</style>
