<template>
  <header class="global-header-nav sticky top-0 z-50">
    <div class="container mx-auto flex items-center justify-between h-16 px-4">
      <!-- Logo -->
      <div class="global-logo flex items-center">
        <i class="fa fa-code mr-2"></i> Python学习平台
      </div>
      <!-- 导航栏 -->
      <nav class="flex-1 flex justify-center relative">
        <div class="flex space-x-8" ref="navBar">
          <router-link v-for="item in navItems" :key="item.id" :to="item.to" class="global-nav-link" :class="{ 'router-link-exact-active': isActive(item) }" exact>{{ item.label }}</router-link>
        </div>
        <div v-if="navIndicatorStyle" class="nav-indicator-white" :style="navIndicatorStyle"></div>
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
          <button @click="openLoginModal" class="px-4 py-2 border border-white text-white rounded-lg font-medium hover:bg-white/10 transition-colors mr-2">登录</button>
          <button @click="openRegisterModal" class="px-4 py-2 bg-white text-primary rounded-lg font-medium hover:bg-white/90 transition-colors">注册</button>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
import axios from 'axios';
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
        { id: 'home', label: '首页', to: '/' },
        { id: 'courses', label: '课程中心', to: '/courses' },
        { id: 'learning', label: '学习中心', to: '/learning' },
        { id: 'practice', label: '实践中心', to: '/practice' },
        { id: 'ai', label: 'AI问答', to: '/ai' }
      ],
      navIndicatorStyle: null,
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
      this.updateNavIndicator();
      window.addEventListener('resize', this.updateNavIndicator);
      window.addEventListener('scroll', this.updateUserMenuPosition);
      window.addEventListener('resize', this.updateUserMenuPosition);
      document.addEventListener('click', this.handleClickOutside);
    });
  },
  beforeUnmount() {
    EventBus.off('user-logged-in', this.fetchUser);
    window.removeEventListener('resize', this.updateNavIndicator);
    window.removeEventListener('scroll', this.updateUserMenuPosition);
    window.removeEventListener('resize', this.updateUserMenuPosition);
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    fetchUser() {
      let userId = localStorage.getItem('userId');
      if (!userId || userId === 'null') {
        this.isLoggedIn = false;
        return;
      }
      axios.get('http://localhost:8080/api/user/me', { withCredentials: true })
        .then(res => {
          this.isLoggedIn = true;
          this.user = res.data;
        })
        .catch(() => {
          this.isLoggedIn = false;
          this.user = { avatar: null, nickname: '', userId: null };
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
      this.isLoggedIn = false;
      this.user = { avatar: null, nickname: '', userId: null };
      localStorage.removeItem('userId');
      this.showUserMenu = false;
      this.$router.push('/login');
    },
    isActive(item) {
      return this.$route.path === item.to;
    },
    updateNavIndicator() {
      this.$nextTick(() => {
        const navBar = this.$refs.navBar;
        if (!navBar) {
          this.navIndicatorStyle = null;
          return;
        }
        // 直接选中当前激活的 router-link
        const activeLink = navBar.querySelector('.router-link-exact-active');
        if (activeLink) {
          const navRect = navBar.getBoundingClientRect();
          const linkRect = activeLink.getBoundingClientRect();
          const left = linkRect.left - navRect.left;
          const width = linkRect.width;
          this.navIndicatorStyle = {
            position: 'absolute',
            left: left + 'px',
            bottom: '0',
            width: width + 'px',
            height: '2px',
            background: '#fff',
            borderRadius: '2px',
            transition: 'left 0.3s cubic-bezier(.4,0,.2,1), width 0.3s cubic-bezier(.4,0,.2,1)'
          };
        } else {
          this.navIndicatorStyle = null;
        }
      });
    },
    handleClickOutside(e) {
      if (!this.showUserMenu) return;
      const menu = this.$refs.userMenu;
      const btn = this.$refs.avatarBtn;
      if (menu && !menu.contains(e.target) && btn && !btn.contains(e.target)) {
        this.showUserMenu = false;
      }
    },
    openLoginModal() {
      EventBus.emit('open-login-modal');
    },
    openRegisterModal() {
      EventBus.emit('open-register-modal');
    }
  },
  watch: {
    '$route.path'() {
      this.updateNavIndicator();
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
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  transition: background 0.2s, color 0.2s;
  text-decoration: none;
  display: inline-block;
}
.global-nav-link:hover,
.global-nav-link.router-link-exact-active {
  background: rgba(255,255,255,0.18);
  color: #ffe066 !important;
  text-decoration: none;
}
@media (max-width: 768px) {
  .global-header-nav .container {
    flex-direction: column;
    height: auto;
    padding: 1rem 0;
  }
  .global-logo {
    margin-bottom: 0.5rem;
  }
  .global-header-nav nav {
    flex-wrap: wrap;
    justify-content: center;
  }
  .global-nav-link {
    margin-bottom: 0.5rem;
  }
}
.nav-indicator-white {
  position: absolute;
  height: 3px;
  border-radius: 2px;
  background: #fff;
  bottom: 0;
  transition: left 0.3s cubic-bezier(.4,0,.2,1), width 0.3s cubic-bezier(.4,0,.2,1);
  z-index: 10;
}
.z-100 { z-index: 10000 !important; }
</style>
