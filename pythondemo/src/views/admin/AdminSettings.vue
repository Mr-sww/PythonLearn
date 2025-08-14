<template>
  <div class="admin-settings">
    <div class="page-header">
      <h1 class="page-title">系统设置</h1>
      <button class="btn btn-primary" @click="saveSettings" :disabled="saving">
        <i :class="saving ? 'fas fa-spinner fa-spin' : 'fas fa-save'"></i>
        {{ saving ? '保存中...' : '保存设置' }}
      </button>
    </div>

    <div class="settings-container">
      <!-- 基本设置 -->
      <div class="settings-section">
        <h2 class="section-title">基本设置</h2>
        <div class="settings-grid">
          <div class="setting-item">
            <label class="setting-label">网站名称</label>
            <input v-model="settings.siteName" type="text" class="setting-input">
          </div>
          <div class="setting-item">
            <label class="setting-label">网站描述</label>
            <textarea v-model="settings.siteDescription" class="setting-textarea"></textarea>
          </div>
          <div class="setting-item">
            <label class="setting-label">管理员邮箱</label>
            <input v-model="settings.adminEmail" type="email" class="setting-input">
          </div>
        </div>
      </div>

      <!-- 用户设置 -->
      <div class="settings-section">
        <h2 class="section-title">用户设置</h2>
        <div class="settings-grid">
          <div class="setting-item">
            <label class="setting-label">允许注册</label>
            <div class="setting-toggle">
              <input v-model="settings.allowRegistration" type="checkbox" id="allowRegistration">
              <label for="allowRegistration" class="toggle-label"></label>
            </div>
          </div>
          <div class="setting-item">
            <label class="setting-label">邮箱验证</label>
            <div class="setting-toggle">
              <input v-model="settings.emailVerification" type="checkbox" id="emailVerification">
              <label for="emailVerification" class="toggle-label"></label>
            </div>
          </div>
          <div class="setting-item">
            <label class="setting-label">最大文件上传大小(MB)</label>
            <input v-model="settings.maxFileSize" type="number" class="setting-input">
          </div>
        </div>
      </div>

      <!-- 课程设置 -->
      <div class="settings-section">
        <h2 class="section-title">课程设置</h2>
        <div class="settings-grid">
          <div class="setting-item">
            <label class="setting-label">课程审核</label>
            <div class="setting-toggle">
              <input v-model="settings.courseApproval" type="checkbox" id="courseApproval">
              <label for="courseApproval" class="toggle-label"></label>
            </div>
          </div>
          <div class="setting-item">
            <label class="setting-label">最大课程数量</label>
            <input v-model="settings.maxCourses" type="number" class="setting-input">
          </div>
          <div class="setting-item">
            <label class="setting-label">课程过期时间(天)</label>
            <input v-model="settings.courseExpireDays" type="number" class="setting-input">
          </div>
        </div>
      </div>

      <!-- 系统维护 -->
      <div class="settings-section">
        <h2 class="section-title">系统维护</h2>
        <div class="maintenance-actions">
          <button class="btn btn-secondary" @click="clearCache">
            <i class="fas fa-broom"></i>
            清理缓存
          </button>
          <button class="btn btn-info" @click="backupDatabase">
            <i class="fas fa-database"></i>
            备份数据库
          </button>
          <button class="btn btn-warning" @click="optimizeDatabase">
            <i class="fas fa-tools"></i>
            优化数据库
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminSettings',
  data() {
    return {
      settings: {
        siteName: 'Python学习平台',
        siteDescription: '专业的Python在线学习平台',
        adminEmail: 'admin@example.com',
        allowRegistration: true,
        emailVerification: true,
        maxFileSize: 10,
        courseApproval: true,
        maxCourses: 50,
        courseExpireDays: 365
      },
      loading: false,
      saving: false
    }
  },
  async mounted() {
    await this.loadSettings()
  },
  methods: {
    async loadSettings() {
      this.loading = true
      try {
        // 这里可以从API加载设置，暂时使用默认值
        // const response = await fetch('/api/admin/settings')
        // if (response.ok) {
        //   this.settings = await response.json()
        // }
      } catch (error) {
        console.error('加载设置失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    async saveSettings() {
      this.saving = true
      try {
        // 这里可以调用API保存设置
        // const response = await fetch('/api/admin/settings', {
        //   method: 'PUT',
        //   headers: { 'Content-Type': 'application/json' },
        //   body: JSON.stringify(this.settings)
        // })
        
        // 模拟保存成功
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('设置保存成功')
      } catch (error) {
        console.error('保存设置失败:', error)
        this.$message.error('保存失败')
      } finally {
        this.saving = false
      }
    },
    
    async clearCache() {
      try {
        const response = await fetch(`/api/admin/maintenance/clear-cache?adminId=${this.getCurrentUserId()}`, {
          method: 'POST',
          credentials: 'include'
        })
        
        if (response.ok) {
          this.$message.success('缓存清理完成')
        } else {
          this.$message.error('缓存清理失败')
        }
      } catch (error) {
        console.error('清理缓存失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    async backupDatabase() {
      try {
        const response = await fetch(`/api/admin/maintenance/backup-database?adminId=${this.getCurrentUserId()}`, {
          method: 'POST',
          credentials: 'include'
        })
        
        if (response.ok) {
          this.$message.success('数据库备份完成')
        } else {
          this.$message.error('数据库备份失败')
        }
      } catch (error) {
        console.error('备份数据库失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    async optimizeDatabase() {
      try {
        const response = await fetch(`/api/admin/maintenance/optimize-database?adminId=${this.getCurrentUserId()}`, {
          method: 'POST',
          credentials: 'include'
        })
        
        if (response.ok) {
          this.$message.success('数据库优化完成')
        } else {
          this.$message.error('数据库优化失败')
        }
      } catch (error) {
        console.error('优化数据库失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    getCurrentUserId() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      return user.userId || 1
    }
  }
}
</script>

<style scoped>
.admin-settings {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-primary {
  background: linear-gradient(135deg, #d32f2f 0%, #f44336 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(211, 47, 47, 0.3);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-info:hover {
  background: #138496;
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.btn-warning:hover {
  background: #e0a800;
}

.settings-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.settings-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.section-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 1.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #f0f0f0;
}

.settings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.setting-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.setting-label {
  font-weight: 500;
  color: #333;
  font-size: 0.9rem;
}

.setting-input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
  transition: border-color 0.2s;
}

.setting-input:focus {
  outline: none;
  border-color: #d32f2f;
}

.setting-textarea {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
  min-height: 80px;
  resize: vertical;
  transition: border-color 0.2s;
}

.setting-textarea:focus {
  outline: none;
  border-color: #d32f2f;
}

.setting-toggle {
  display: flex;
  align-items: center;
}

.setting-toggle input[type="checkbox"] {
  display: none;
}

.toggle-label {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
  background: #ccc;
  border-radius: 12px;
  cursor: pointer;
  transition: background 0.3s;
}

.toggle-label::before {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px;
  width: 20px;
  height: 20px;
  background: white;
  border-radius: 50%;
  transition: transform 0.3s;
}

.setting-toggle input[type="checkbox"]:checked + .toggle-label {
  background: #d32f2f;
}

.setting-toggle input[type="checkbox"]:checked + .toggle-label::before {
  transform: translateX(26px);
}

.maintenance-actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .settings-grid {
    grid-template-columns: 1fr;
  }
  
  .maintenance-actions {
    flex-direction: column;
  }
}
</style> 