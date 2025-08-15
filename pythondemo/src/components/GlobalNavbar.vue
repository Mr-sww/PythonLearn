<template>
  <header class="global-header-nav sticky top-0 z-50">
    <div class="container mx-auto flex items-center justify-between h-16 px-4">
      <!-- Logo -->
      <div class="global-logo flex items-center">
        <i class="fa fa-code mr-2"></i> Python学习平台
      </div>
      <!-- 导航栏 -->
      <nav class="flex-1 flex justify-center relative">
        <div class="flex space-x-4 md:space-x-8" ref="navBar">
          <router-link
            v-for="item in navItems"
            :key="item.id"
            :to="item.to"
            class="global-nav-link"
            :class="{ 'is-active': isActive(item) }"
            exact
          >
            <span class="emoji" aria-hidden="true">{{ item.emoji }}</span>
            <span class="label">{{ item.label }}</span>
          </router-link>
        </div>
      </nav>
      <!-- 用户区 -->
      <div class="flex items-center ml-4 relative">
        <template v-if="isLoggedIn">
          <button @click="toggleUserMenu" class="flex items-center focus:outline-none" ref="avatarBtn">
            <img :src="getAvatarUrl(user.avatar)" alt="用户头像" class="w-9 h-9 rounded-full border-2 border-primary object-cover" />
            <i class="fa fa-caret-down ml-2 text-white"></i>
          </button>
          <div v-if="showUserMenu" class="fixed z-100 w-44 bg-white rounded-xl shadow-lg py-2" :style="userMenuStyle" ref="userMenu">
            <router-link to="/profile" class="block px-4 py-2 text-dark hover:bg-gray-100 cursor-pointer"><i class="fa fa-user-circle mr-2 text-muted"></i>个人中心</router-link>
            <div class="border-t border-gray-100 my-1"></div>
            <a @click="logout" class="block px-4 py-2 text-red-600 hover:bg-gray-100 cursor-pointer"><i class="fa fa-sign-out mr-2"></i>退出登录</a>
          </div>
        </template>
        <template v-else>
          <router-link to="/auth" class="px-4 py-2 border border-white text-white rounded-lg font-medium hover:bg-white/10 transition-colors mr-2">登录</router-link>
          <router-link to="/auth?mode=register" class="px-4 py-2 bg-white text-primary rounded-lg font-medium hover:bg-white/90 transition-colors">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
import { EventBus } from '../eventBus';
export default {
  name: 'GlobalNavbar',
  data() {
    return {
      isLoggedIn: false,
      user: {
        avatar: null,
        nickname: '',
        userId: null
      },
      showUserMenu: false,
      navItems: [
        { id: 'home', label: '首页', to: '/', emoji: '🏠' },
        { id: 'courses', label: '课程中心', to: '/courses', emoji: '📚' },
        { id: 'learning', label: '学习中心', to: '/learning', emoji: '🎓' },
        { id: 'practice', label: '实践中心', to: '/practice', emoji: '🛠️' },
        { id: 'ai', label: 'AI问答', to: '/ai', emoji: '🤖' }
      ],
      userMenuStyle: {
        top: '60px',
        right: '32px',
        left: 'auto',
        zIndex: 10000
      }
    }
  },
  mounted() {
    this.fetchUser();
    EventBus.on('user-logged-in', this.fetchUser);
    this.$nextTick(() => {
      window.addEventListener('scroll', this.updateUserMenuPosition);
      window.addEventListener('resize', this.updateUserMenuPosition);
      document.addEventListener('click', this.handleClickOutside);
    });
  },
  beforeUnmount() {
    EventBus.off('user-logged-in', this.fetchUser);
    window.removeEventListener('scroll', this.updateUserMenuPosition);
    window.removeEventListener('resize', this.updateUserMenuPosition);
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    fetchUser() {
      // 使用认证工具函数检查登录状态
      import('../utils/auth').then(({ isLoggedIn, getCurrentUser }) => {
        if (isLoggedIn()) {
          this.isLoggedIn = true;
          const user = getCurrentUser();
          this.user = user;
          console.log('GlobalNavbar: 用户已登录', user);
        } else {
          this.isLoggedIn = false;
          this.user = { avatar: null, nickname: '', userId: null };
          console.log('GlobalNavbar: 用户未登录');
        }
      });
    },
    getAvatarUrl(url) {
      if (!url || typeof url !== 'string') {
        return 'https://picsum.photos/200/200';
      }
      if (url.startsWith('/avatar/')) {
        return 'http://localhost:8080' + url;
      }
      return url;
    },
    updateUserMenuPosition() {
      if (this.showUserMenu) {
        this.$nextTick(() => {
          const btn = this.$refs.avatarBtn;
          if (btn) {
            const rect = btn.getBoundingClientRect();
            this.userMenuStyle = {
              position: 'fixed',
              top: rect.bottom + 8 + 'px',
              right: (window.innerWidth - rect.right - 8) + 'px',
              left: 'auto',
              zIndex: 10000
            };
          }
        });
      }
    },
    toggleUserMenu() {
      this.showUserMenu = !this.showUserMenu;
      this.updateUserMenuPosition();
    },
    logout() {
      // 使用认证工具函数清除登录状态
      import('../utils/auth').then(({ clearLoginState }) => {
        clearLoginState();
      });
      this.isLoggedIn = false;
      this.user = { avatar: null, nickname: '', userId: null };
      this.showUserMenu = false;
      this.$router.push('/auth');
    },
    isActive(item) {
      return this.$route.path === item.to;
    },
    handleClickOutside(e) {
      if (!this.showUserMenu) return;
      const menu = this.$refs.userMenu;
      const btn = this.$refs.avatarBtn;
      if (menu && !menu.contains(e.target) && btn && !btn.contains(e.target)) {
        this.showUserMenu = false;
      }
    }
  },
  watch: {
    '$route.path'() {
      // 触发激活项的跳跃动画
      this.$nextTick(() => {
        const navBar = this.$refs.navBar;
        if (!navBar) return;
        const activeLink = navBar.querySelector('.global-nav-link.is-active');
        if (activeLink) {
          // 移除之前的动画类
          activeLink.classList.remove('jump-animation');
          // 强制重绘
          void activeLink.offsetWidth;
          // 添加跳跃动画
          activeLink.classList.add('jump-animation');
        }
      });
    }
  }
}
</script>

<style>
.global-header-nav {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
  box-shadow: 0 2px 8px rgba(37,99,235,0.08);
}
.global-logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
  display: flex;
  align-items: center;
}
.global-logo i {
  margin-right: 8px;
  color: #ffe066;
}
.global-nav-link {
  color: #fff !important;
  font-weight: 500;
  font-size: 1.1rem;
  padding: 0.5rem 1.0rem;
  border-radius: 9999px;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  position: relative;
  z-index: 10;
}
.global-nav-link:hover {
  background: rgba(255,255,255,0.15);
  transform: translateY(-1px);
}
.global-nav-link.is-active {
  background: linear-gradient(135deg, #fef3c7 0%, #fde68a 50%, #fcd34d 100%);
  color: #374151 !important;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(255,255,255,0.9);
  box-shadow: 0 4px 20px rgba(254, 243, 199, 0.4), 0 0 0 1px rgba(254, 243, 199, 0.3);
}
.global-nav-link .emoji {
  margin-right: 8px;
  filter: drop-shadow(0 2px 2px rgba(0,0,0,0.15));
  transition: transform 0.2s ease;
}
.global-nav-link:hover .emoji {
  transform: scale(1.1);
}
.pulse-once { animation: pillPulse 380ms ease-out 1; }
@keyframes pillPulse {
  0% { transform: scale(0.96); }
  60% { transform: scale(1.03); }
  100% { transform: scale(1); }
}

/* 跳跃动画 */
.jump-animation .emoji {
  animation: emojiJump 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

@keyframes emojiJump {
  0% { transform: translateY(0) scale(1); }
  25% { transform: translateY(-8px) scale(1.1); }
  50% { transform: translateY(-12px) scale(1.2); }
  75% { transform: translateY(-6px) scale(1.1); }
  100% { transform: translateY(0) scale(1); }
}

@media (max-width: 768px) {
  .global-header-nav .container {
    flex-direction: column;
    height: auto;
    padding: 1rem 0;
  }
  .global-logo { margin-bottom: 0.5rem; }
  .global-header-nav nav { flex-wrap: wrap; justify-content: center; }
  .global-nav-link { margin-bottom: 0.5rem; }
}
.z-100 { z-index: 10000 !important; }
</style>
