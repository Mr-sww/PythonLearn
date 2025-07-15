<template>
  <header class="global-header-nav sticky top-0 z-50">
    <div class="container mx-auto flex items-center justify-between h-16 px-4">
      <!-- Logo -->
      <div class="global-logo flex items-center">
        <i class="fa fa-code mr-2"></i> Python学习平台
      </div>
      <!-- 导航栏 -->
      <nav class="flex-1 flex justify-center relative">
        <div class="flex space-x-8 navbar-menu" ref="navBar">
          <router-link v-for="(item, idx) in navItems" :key="item.id" :to="item.to"
            class="global-nav-link nav-item"
            :class="{ active: isActive(item) }"
            exact
            ref="navLinks"
            @click="moveHighlight(idx)"
          >
            <span v-if="isActive(item)" class="nav-symbol" :key="$route.path">{{ item.symbol }}</span>
            {{ item.label }}
          </router-link>
          <div class="nav-highlight" :style="highlightStyle"></div>
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
        { id: 'home', label: '首页', to: '/', symbol: '🏠' },
        { id: 'courses', label: '课程中心', to: '/courses', symbol: '📚' },
        { id: 'learning', label: '学习中心', to: '/learning', symbol: '🧑‍🎓' },
        { id: 'practice', label: '实践中心', to: '/practice', symbol: '🛠️' },
        { id: 'ai', label: 'AI问答', to: '/ai', symbol: '🤖' }
      ],
      navIndicatorStyle: null,
      userMenuStyle: {
        top: '60px',
        right: '32px',
        left: 'auto',
        zIndex: 10000
      },
      highlightStyle: {
        left: '0px',
        width: '0px',
        opacity: 0
      },
      activeIdx: 0
    }
  },
  mounted() {
    this.fetchUser();
    EventBus.on('user-logged-in', this.fetchUser);
    this.$nextTick(() => {
      this.updateHighlight();
      window.addEventListener('resize', this.updateHighlight);
      window.addEventListener('scroll', this.updateUserMenuPosition);
      window.addEventListener('resize', this.updateUserMenuPosition);
      document.addEventListener('click', this.handleClickOutside);
    });
  },
  beforeUnmount() {
    EventBus.off('user-logged-in', this.fetchUser);
    window.removeEventListener('resize', this.updateHighlight);
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
    moveHighlight(idx) {
      this.activeIdx = idx;
      this.updateHighlight();
    },
    updateHighlight() {
      this.$nextTick(() => {
        const navLinks = this.$refs.navLinks;
        let idx = this.activeIdx;
        // 自动根据路由匹配高亮
        for (let i = 0; i < this.navItems.length; i++) {
          if (this.isActive(this.navItems[i])) {
            idx = i;
            break;
          }
        }
        const el = navLinks && navLinks[idx] && navLinks[idx].$el ? navLinks[idx].$el : navLinks && navLinks[idx];
        if (el) {
          this.highlightStyle = {
            left: el.offsetLeft + 'px',
            width: el.offsetWidth + 'px',
            opacity: 1
          };
        } else {
          this.highlightStyle = { left: '0px', width: '0px', opacity: 0 };
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
      this.updateHighlight();
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
.navbar-menu {
  position: relative;
  display: flex;
  align-items: center;
}
.nav-item {
  color: #fff;
  font-weight: 500;
  font-size: 18px;
  padding: 0 28px;
  height: 48px;
  border-radius: 24px;
  cursor: pointer;
  background: transparent;
  transition: color 0.2s, background 0.2s;
  z-index: 1;
  display: flex;
  align-items: center;
  letter-spacing: 1px;
}
.nav-item:not(.active):hover {
  background: rgba(255,255,255,0.10);
  color: #ffe066;
}
.nav-item .nav-icon {
  margin-right: 8px;
  font-size: 22px;
  transition: transform 0.3s cubic-bezier(.68,-0.55,.27,1.55);
}
.nav-item.active {
  color: #222 !important;
  background: linear-gradient(90deg, #ffe066 60%, #ffe100 100%);
  font-weight: bold;
  box-shadow: 0 4px 16px rgba(255,225,0,0.18), 0 1.5px 6px rgba(0,0,0,0.04);
  z-index: 2;
  transition: background 0.3s, color 0.2s, box-shadow 0.2s;
  text-shadow: 0 1px 0 #fffbe6, 0 0.5px 0 #fffbe6;
  letter-spacing: 1px;
}
.nav-item.active .nav-icon {
  animation: bounce 0.4s;
  transform: scale(1.2);
}
@keyframes bounce {
  0%   { transform: scale(1); }
  40%  { transform: scale(1.3); }
  60%  { transform: scale(0.95); }
  100% { transform: scale(1.2); }
}
.nav-highlight {
  position: absolute;
  top: 0;
  left: 0;
  height: 48px;
  border-radius: 24px;
  background: linear-gradient(90deg, #ffe066 60%, #ffe100 100%);
  box-shadow: 0 4px 16px rgba(255,225,0,0.18), 0 1.5px 6px rgba(0,0,0,0.04);
  z-index: 0;
  transition: left 0.3s cubic-bezier(.68,-0.55,.27,1.55), width 0.3s, background 0.3s, box-shadow 0.2s;
  opacity: 1;
  pointer-events: none;
}
.nav-symbol {
  display: inline-block;
  margin-right: 8px;
  font-size: 22px;
  color: #222;
  animation: symbol-bounce 0.5s;
  vertical-align: middle;
}
@keyframes symbol-bounce {
  0%   { transform: translateY(0) scale(1);}
  30%  { transform: translateY(-12px) scale(1.2);}
  50%  { transform: translateY(0) scale(1);}
  70%  { transform: translateY(-6px) scale(1.1);}
  100% { transform: translateY(0) scale(1);}
}
</style>
