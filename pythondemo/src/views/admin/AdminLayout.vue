<template>
  <div class="admin-layout">
    <div class="layout-header">
      <div class="header-content">
        <h1 class="header-title">
          <i class="fas fa-cogs"></i>
          系统管理中心
        </h1>
        <div class="header-actions">
          <router-link to="/profile" class="header-link">
            <i class="fas fa-user"></i>
            个人中心
          </router-link>
          <router-link to="/" class="header-link">
            <i class="fas fa-home"></i>
            返回首页
          </router-link>
        </div>
      </div>
    </div>
    
    <div class="layout-container">
      <aside class="layout-sidebar">
        <nav class="sidebar-nav">
          <router-link 
            v-for="item in menuItems" 
            :key="item.path"
            :to="item.path"
            class="sidebar-item"
            active-class="active"
          >
            <i :class="item.icon"></i>
            <span>{{ item.title }}</span>
          </router-link>
        </nav>
      </aside>
      
      <main class="layout-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminLayout',
  data() {
    return {
      menuItems: [
        {
          title: '系统概览',
          path: '/admin/dashboard',
          icon: 'fas fa-tachometer-alt'
        },
        {
          title: '用户管理',
          path: '/admin/users',
          icon: 'fas fa-user-cog'
        },
        {
          title: '课程审核',
          path: '/admin/courses',
          icon: 'fas fa-clipboard-check'
        },
        {
          title: '系统日志',
          path: '/admin/logs',
          icon: 'fas fa-file-alt'
        },
        {
          title: '系统设置',
          path: '/admin/settings',
          icon: 'fas fa-sliders-h'
        }
      ]
    }
  }
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background: #f8f9fa;
}

.layout-header {
  background: linear-gradient(135deg, #d32f2f 0%, #f44336 100%);
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 1.5rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.header-actions {
  display: flex;
  gap: 1rem;
}

.header-link {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.header-link:hover {
  background: rgba(255,255,255,0.1);
}

.layout-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  display: flex;
  gap: 2rem;
}

.layout-sidebar {
  width: 250px;
  flex-shrink: 0;
}

.sidebar-nav {
  background: white;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.sidebar-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  color: #666;
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.2s;
  margin-bottom: 0.5rem;
}

.sidebar-item:hover {
  background: #f8f9fa;
  color: #333;
}

.sidebar-item.active {
  background: linear-gradient(135deg, #d32f2f 0%, #f44336 100%);
  color: white;
}

.layout-main {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

@media (max-width: 768px) {
  .layout-container {
    flex-direction: column;
    padding: 1rem;
  }
  
  .layout-sidebar {
    width: 100%;
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
  }
}
</style> 