<template>
  <div class="admin-users">
    <div class="page-header">
      <h2>用户管理</h2>
      <div class="header-actions">
        <button class="btn btn-primary" @click="showAddUserModal = true">
          <i class="fas fa-plus"></i> 添加用户
        </button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-container">
      <div class="stat-card">
        <div class="stat-number">{{ stats.total || 0 }}</div>
        <div class="stat-label">总用户数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.active || 0 }}</div>
        <div class="stat-label">正常用户</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.inactive || 0 }}</div>
        <div class="stat-label">禁用用户</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.students || 0 }}</div>
        <div class="stat-label">学生用户</div>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-filters">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索用户..." 
          @input="debounceSearch"
          class="form-control"
        >
      </div>
      
      <div class="filter-group">
        <select v-model="filterRole" @change="searchUsers" class="filter-select">
          <option value="">所有角色</option>
          <option value="1">计算机类</option>
          <option value="2">工设类</option>
          <option value="3">艺术类</option>
          <option value="4">医学类</option>
          <option value="5">文科类</option>
          <option value="6">体育类</option>
          <option value="7">教师</option>
          <option value="8">管理员</option>
        </select>
        
        <select v-model="filterStatus" @change="searchUsers" class="filter-select">
          <option value="">所有状态</option>
          <option value="active">正常</option>
          <option value="inactive">禁用</option>
        </select>
        
        <button @click="loadUsers" class="btn btn-secondary">
          <i class="fas fa-refresh"></i> 刷新
        </button>
      </div>
    </div>

    <!-- 用户列表 -->
    <div class="users-table">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="filteredUsers.length === 0" class="empty-container">
        <i class="fas fa-users"></i>
        <p>暂无用户数据</p>
      </div>
      
      <table v-else class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>头像</th>
            <th>账号</th>
            <th>昵称</th>
            <th>邮箱</th>
            <th>角色/专业</th>
            <th>状态</th>
            <th>注册时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.userId">
            <td>{{ user.userId }}</td>
            <td>
              <div class="avatar-container">
                <img 
                  :src="getAvatarUrl(user.avatar)" 
                  :alt="user.nickname" 
                  class="user-avatar"
                  @error="handleAvatarError"
                >
                <div v-if="!user.avatar" class="avatar-fallback">
                  {{ (user.nickname || user.account || 'U').substring(0, 2).toUpperCase() }}
                </div>
              </div>
            </td>
            <td>{{ user.account }}</td>
            <td>{{ user.nickname || '未设置' }}</td>
            <td>{{ user.email || '未设置' }}</td>
            <td>
              <span :class="getRoleBadgeClass(user.groupType)">
                {{ getRoleText(user.groupType) }}
              </span>
            </td>
            <td>
              <span :class="getStatusBadgeClass(user.status)">
                {{ getStatusText(user.status) }}
              </span>
            </td>
            <td>{{ formatDate(user.createTime) }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn btn-sm btn-outline-info" @click="viewUser(user)">
                  <i class="fas fa-eye"></i> 查看
                </button>
                <button class="btn btn-sm btn-outline-warning" @click="toggleUserStatus(user)">
                  {{ user.status === 'active' ? '禁用' : '启用' }}
                </button>
                <button class="btn btn-sm btn-outline-secondary" @click="changeUserRole(user)">
                  修改角色
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="deleteUser(user)">
                  <i class="fas fa-trash"></i>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 用户详情模态框 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="showDetailModal = false">
      <div class="modal-content large-modal" @click.stop>
        <div class="modal-header">
          <h3>用户详情</h3>
          <button class="modal-close" @click="showDetailModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body" v-if="selectedUser">
          <div class="user-detail-grid">
            <div class="detail-section">
              <h4>基本信息</h4>
              <div class="detail-item">
                <span class="label">用户ID：</span>
                <span class="value">{{ selectedUser.userId }}</span>
              </div>
              <div class="detail-item">
                <span class="label">账号：</span>
                <span class="value">{{ selectedUser.account }}</span>
              </div>
              <div class="detail-item">
                <span class="label">昵称：</span>
                <span class="value">{{ selectedUser.nickname || '未设置' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">手机号：</span>
                <span class="value">{{ selectedUser.phone || '未设置' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">邮箱：</span>
                <span class="value">{{ selectedUser.email || '未设置' }}</span>
              </div>
              <div class="detail-item">
                <span class="label">角色：</span>
                <span :class="['role-badge', getRoleBadgeClass(selectedUser.groupType)]">
                  {{ getRoleText(selectedUser.groupType) }}
                </span>
              </div>
              <div class="detail-item">
                <span class="label">兴趣类型：</span>
                <span class="value">{{ selectedUser.intestTypes || '未设置' }}</span>
              </div>
            </div>
            
            <div class="detail-section">
              <h4>状态信息</h4>
              <div class="detail-item">
                <span class="label">状态：</span>
                <span :class="['status-badge', getStatusBadgeClass(selectedUser.status)]">
                  {{ getStatusText(selectedUser.status) }}
                </span>
              </div>
              <div class="detail-item">
                <span class="label">创建时间：</span>
                <span class="value">{{ formatDate(selectedUser.createTime) }}</span>
              </div>
              <div class="detail-item">
                <span class="label">更新时间：</span>
                <span class="value">{{ formatDate(selectedUser.updateTime) }}</span>
              </div>
              
              <h4>头像信息</h4>
              <div class="avatar-detail">
                <img 
                  :src="getAvatarUrl(selectedUser.avatar)" 
                  :alt="selectedUser.nickname"
                  class="detail-avatar"
                  @error="handleAvatarError"
                />
                <div v-if="!selectedUser.avatar" class="avatar-fallback detail-avatar">
                  {{ (selectedUser.nickname || selectedUser.account || 'U').substring(0, 2).toUpperCase() }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showDetailModal = false">关闭</button>
        </div>
      </div>
    </div>

    <!-- 修改角色模态框 -->
    <div v-if="showRoleModal" class="modal-overlay" @click="showRoleModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>修改用户角色</h3>
          <button class="modal-close" @click="showRoleModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="user-info">
            <p><strong>用户：</strong>{{ selectedUser && selectedUser.nickname || selectedUser && selectedUser.account }}</p>
            <p><strong>当前角色：</strong>{{ selectedUser && getRoleText(selectedUser.groupType) }}</p>
          </div>
          
          <div class="role-selection">
            <label>选择新角色：</label>
            <select v-model="newRole" class="role-select">
              <option value="1">计算机类</option>
              <option value="2">工设类</option>
              <option value="3">艺术类</option>
              <option value="4">医学类</option>
              <option value="5">文科类</option>
              <option value="6">体育类</option>
              <option value="7">教师</option>
              <option value="8">管理员</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showRoleModal = false">取消</button>
          <button class="btn btn-primary" @click="confirmChangeRole" :disabled="changingRole">
            {{ changingRole ? '处理中...' : '确认修改' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 添加用户模态框 -->
    <div v-if="showAddUserModal" class="modal-overlay" @click="showAddUserModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>添加用户</h3>
          <button class="modal-close" @click="showAddUserModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>账号：</label>
            <input v-model="newUser.account" type="text" class="form-control" placeholder="请输入账号">
          </div>
          <div class="form-group">
            <label>密码：</label>
            <input v-model="newUser.password" type="password" class="form-control" placeholder="请输入密码">
          </div>
          <div class="form-group">
            <label>昵称：</label>
            <input v-model="newUser.nickname" type="text" class="form-control" placeholder="请输入昵称">
          </div>
          <div class="form-group">
            <label>手机号：</label>
            <input v-model="newUser.phone" type="text" class="form-control" placeholder="请输入手机号">
          </div>
          <div class="form-group">
            <label>邮箱：</label>
            <input v-model="newUser.email" type="email" class="form-control" placeholder="请输入邮箱">
          </div>
          <div class="form-group">
            <label>角色：</label>
            <select v-model="newUser.groupType" class="form-control">
              <option value="1">计算机类</option>
              <option value="2">工设类</option>
              <option value="3">艺术类</option>
              <option value="4">医学类</option>
              <option value="5">文科类</option>
              <option value="6">体育类</option>
              <option value="7">教师</option>
              <option value="8">管理员</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showAddUserModal = false">取消</button>
          <button class="btn btn-primary" @click="confirmAddUser" :disabled="addingUser">
            {{ addingUser ? '添加中...' : '确认添加' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'AdminUsers',
  setup() {
    const users = ref([])
    const loading = ref(false)
    const searchQuery = ref('')
    const filterRole = ref('')
    const filterStatus = ref('')
    const showDetailModal = ref(false)
    const showRoleModal = ref(false)
    const showAddUserModal = ref(false)
    const selectedUser = ref(null)
    const newRole = ref('')
    const changingRole = ref(false)
    const addingUser = ref(false)
    const stats = ref({})
    const searchTimeout = ref(null)
    
    const newUser = ref({
      account: '',
      password: '',
      nickname: '',
      phone: '',
      email: '',
      groupType: '1'
    })

    const filteredUsers = computed(() => {
      let filtered = users.value

      // 按角色筛选
      if (filterRole.value) {
        filtered = filtered.filter(user => user.groupType === parseInt(filterRole.value))
      }

      // 按状态筛选
      if (filterStatus.value) {
        filtered = filtered.filter(user => user.status === filterStatus.value)
      }

      // 按关键词搜索
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(user => 
          user.account.toLowerCase().includes(query) ||
          (user.nickname && user.nickname.toLowerCase().includes(query)) ||
          (user.email && user.email.toLowerCase().includes(query)) ||
          (user.phone && user.phone.includes(query))
        )
      }

      return filtered
    })

    const loadUsers = async () => {
      loading.value = true
      try {
        const response = await axios.get('http://localhost:8080/api/admin/users', { withCredentials: true })
        users.value = response.data || []
      } catch (error) {
        console.error('加载用户失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能访问，请先登录')
          // this.$router && this.$router.push('/auth')
        }
      } finally {
        loading.value = false
      }
    }

    const loadStats = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/admin/users/stats', { withCredentials: true })
        stats.value = response.data || {}
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    }

    const debounceSearch = () => {
      if (searchTimeout.value) {
        clearTimeout(searchTimeout.value)
      }
      searchTimeout.value = setTimeout(() => {
        // 搜索功能已通过计算属性实现
      }, 300)
    }

    const searchUsers = () => {
      // 实时搜索，不需要额外API调用
    }

    const viewUser = (user) => {
      selectedUser.value = user
      showDetailModal.value = true
    }

    const toggleUserStatus = async (user) => {
      try {
        const newStatus = user.status === 'active' ? 'inactive' : 'active'
        const response = await axios.put(
          `http://localhost:8080/api/admin/users/${user.userId}/status`,
          { status: newStatus },
          { withCredentials: true }
        )
        
        if (response.data && response.data.success) {
          user.status = newStatus
          alert(`用户状态已${newStatus === 'active' ? '启用' : '禁用'}`)
          loadStats()
        } else {
          alert('操作失败：' + (response.data && response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('更新用户状态失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能操作，请先登录')
        } else {
          alert('操作失败：' + (error.response && error.response.data || error.message))
        }
      }
    }

    const changeUserRole = (user) => {
      selectedUser.value = user
      newRole.value = user.groupType.toString()
      showRoleModal.value = true
    }

    const confirmChangeRole = async () => {
      if (!newRole.value) {
        alert('请选择新角色')
        return
      }

      changingRole.value = true
      try {
        const response = await axios.put(
          `http://localhost:8080/api/admin/users/${selectedUser.value.userId}/role`,
          { groupType: parseInt(newRole.value) },
          { withCredentials: true }
        )
        
        if (response.data && response.data.success) {
          selectedUser.value.groupType = parseInt(newRole.value)
          alert('用户角色修改成功！')
          showRoleModal.value = false
          loadStats()
        } else {
          alert('修改失败：' + (response.data && response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('修改用户角色失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能操作，请先登录')
        } else {
          alert('修改失败：' + (error.response && error.response.data || error.message))
        }
      } finally {
        changingRole.value = false
      }
    }

    const confirmAddUser = async () => {
      if (!newUser.value.account || !newUser.value.password) {
        alert('账号和密码不能为空')
        return
      }

      addingUser.value = true
      try {
        const response = await axios.post(
          'http://localhost:8080/api/admin/users',
          newUser.value,
          { withCredentials: true }
        )
        
        if (response.data && response.data.success) {
          alert('用户添加成功！')
          showAddUserModal.value = false
          // 重置表单
          newUser.value = {
            account: '',
            password: '',
            nickname: '',
            phone: '',
            email: '',
            groupType: '1'
          }
          loadUsers()
          loadStats()
        } else {
          alert('添加失败：' + (response.data && response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('添加用户失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能操作，请先登录')
        } else {
          alert('添加失败：' + (error.response && error.response.data || error.message))
        }
      } finally {
        addingUser.value = false
      }
    }

    const deleteUser = async (user) => {
      if (!confirm(`确定要删除用户 "${user.nickname || user.account}" 吗？此操作不可恢复！`)) {
        return
      }

      try {
        const response = await axios.delete(
          `http://localhost:8080/api/admin/users/${user.userId}`,
          { withCredentials: true }
        )
        
        if (response.data && response.data.success) {
          alert('用户删除成功！')
          loadUsers()
          loadStats()
        } else {
          alert('删除失败：' + (response.data && response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('删除用户失败:', error)
        if (error.response && error.response.status === 403) {
          alert('需要管理员登录后才能操作，请先登录')
        } else {
          alert('删除失败：' + (error.response && error.response.data || error.message))
        }
      }
    }

    const getRoleText = (groupType) => {
      switch (groupType) {
        case 1: return '计算机类'
        case 2: return '工设类'
        case 3: return '艺术类'
        case 4: return '医学类'
        case 5: return '文科类'
        case 6: return '体育类'
        case 7: return '教师'
        case 8: return '管理员'
        default: return '未知'
      }
    }

    const getRoleBadgeClass = (groupType) => {
      if (groupType >= 1 && groupType <= 6) {
        return 'badge badge-primary'  // 学生角色 - 蓝色
      } else if (groupType === 7) {
        return 'badge badge-warning'  // 教师角色 - 黄色
      } else if (groupType === 8) {
        return 'badge badge-danger'   // 管理员角色 - 红色
      }
      return 'badge badge-secondary'  // 默认样式
    }

    const getStatusText = (status) => {
      return status === 'active' ? '正常' : '禁用'
    }

    const getStatusBadgeClass = (status) => {
      return status === 'active' ? 'badge badge-success' : 'badge badge-secondary'
    }

    const formatDate = (date) => {
      if (!date) return '未知'
      return new Date(date).toLocaleDateString('zh-CN') + ' ' + new Date(date).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    }

    const getAvatarUrl = (avatar) => {
      if (!avatar) return ''
      if (avatar.startsWith('http')) return avatar
      return `http://localhost:8080${avatar}`
    }

    const handleAvatarError = (event) => {
      event.target.style.display = 'none'
    }

    onMounted(() => {
      loadUsers()
      loadStats()
    })

    return {
      users,
      loading,
      searchQuery,
      filterRole,
      filterStatus,
      showDetailModal,
      showRoleModal,
      showAddUserModal,
      selectedUser,
      newRole,
      changingRole,
      addingUser,
      newUser,
      stats,
      filteredUsers,
      loadUsers,
      loadStats,
      debounceSearch,
      searchUsers,
      viewUser,
      toggleUserStatus,
      changeUserRole,
      confirmChangeRole,
      confirmAddUser,
      deleteUser,
      getRoleText,
      getRoleBadgeClass,
      getStatusText,
      getStatusBadgeClass,
      formatDate,
      getAvatarUrl,
      handleAvatarError
    }
  }
}
</script>

<style scoped>
.admin-users {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.search-filters {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
}

.search-box {
  flex: 1;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.filter-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
  min-width: 120px;
}

.users-table {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.avatar-container {
  position: relative;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
}

.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  width: 100%;
  height: 100%;
  background: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
}

.badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.badge-primary {
  background: #007bff;
  color: white;
}

.badge-warning {
  background: #ffc107;
  color: #212529;
}

.badge-danger {
  background: #dc3545;
  color: white;
}

.badge-success {
  background: #28a745;
  color: white;
}

.badge-secondary {
  background: #6c757d;
  color: white;
}

.action-buttons {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 11px;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-outline-info {
  background: transparent;
  color: #17a2b8;
  border: 1px solid #17a2b8;
}

.btn-outline-warning {
  background: transparent;
  color: #ffc107;
  border: 1px solid #ffc107;
}

.btn-outline-secondary {
  background: transparent;
  color: #6c757d;
  border: 1px solid #6c757d;
}

.btn-outline-danger {
  background: transparent;
  color: #dc3545;
  border: 1px solid #dc3545;
}

.btn:hover {
  opacity: 0.8;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.loading-container, .empty-container {
  text-align: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.large-modal {
  max-width: 800px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 20px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #eee;
}

.user-detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.detail-section h4 {
  margin: 0 0 15px 0;
  color: #333;
  border-bottom: 2px solid #007bff;
  padding-bottom: 5px;
}

.detail-item {
  display: flex;
  margin-bottom: 10px;
  align-items: flex-start;
}

.detail-item .label {
  font-weight: 500;
  color: #666;
  min-width: 100px;
  flex-shrink: 0;
}

.detail-item .value {
  color: #333;
  flex: 1;
}

.role-badge, .status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.avatar-detail {
  text-align: center;
  margin-top: 15px;
}

.detail-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info p {
  margin: 10px 0;
  color: #333;
}

.role-selection {
  margin-top: 20px;
}

.role-selection label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.role-select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

@media (max-width: 768px) {
  .user-detail-grid {
    grid-template-columns: 1fr;
  }
  
  .filter-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .table {
    font-size: 12px;
  }
  
  .table th,
  .table td {
    padding: 8px 6px;
  }
}
</style> 