<template>
  <div class="admin-shell">
    <aside class="sidebar">
      <div class="brand">后台管理</div>
      <nav class="menu">
        <div class="section">用户与权限</div>
        <router-link to="/admin/users" class="item" :class="{ active: isActive('/admin/users') }">
          <i class="fa fa-user"></i><span>用户管理</span>
        </router-link>
        <div class="section">内容与审核</div>
        <router-link to="/admin/course-approvals" class="item" :class="{ active: isActive('/admin/course-approvals') }">
          <i class="fa fa-clipboard-check"></i><span>课程审核</span>
        </router-link>
        <router-link to="/admin/courses" class="item" :class="{ active: isActive('/admin/courses') }">
          <i class="fa fa-book"></i><span>课程管理</span>
        </router-link>
        <router-link to="/admin/problems" class="item" :class="{ active: isActive('/admin/problems') }">
          <i class="fa fa-tasks"></i><span>题库管理</span>
        </router-link>
        <router-link to="/admin/knowledges" class="item" :class="{ active: isActive('/admin/knowledges') }">
          <i class="fa fa-project-diagram"></i><span>知识点管理</span>
        </router-link>
        <div class="section">系统</div>
        <router-link to="/admin/logs" class="item" :class="{ active: isActive('/admin/logs') }">
          <i class="fa fa-file-alt"></i><span>系统日志</span>
        </router-link>
      </nav>
    </aside>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'AdminShell',
  methods: {
    isActive (path) { return this.$route.path.startsWith(path) }
  }
}
</script>

<style scoped>
.admin-shell { display: grid; grid-template-columns: 240px 1fr; min-height: 100vh; background: #f5f6fa; }
.sidebar { background: #0f172a; color: #e2e8f0; padding: 16px; }
.brand { font-weight: 700; font-size: 18px; padding: 12px 8px; border-bottom: 1px solid #1f2937; margin-bottom: 8px; }
.menu { display: flex; flex-direction: column; gap: 4px; }
.section { margin: 12px 8px 4px; color: #94a3b8; font-size: 12px; text-transform: uppercase; letter-spacing: .08em; }
.item { display: flex; align-items: center; gap: 10px; padding: 10px 12px; color: #e2e8f0; text-decoration: none; border-radius: 8px; }
.item:hover { background: #111827; }
.item.active { background: #1f2937; }
.content { padding: 20px; }
</style>


