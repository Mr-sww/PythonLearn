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

    <!-- 搜索和筛选 -->
    <div class="search-filters">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索用户..." 
          @input="searchUsers"
          class="form-control"
        >
      </div>
      
      <div class="filter-group">
        <select v-model="filterRole" @change="searchUsers" class="filter-select">
          <option value="">所有角色</option>
          <option value="student">学生</option>
          <option value="teacher">教师</option>
          <option value="admin">管理员</option>
        </select>
        
        <select v-model="filterMajor" @change="searchUsers" class="filter-select" v-if="filterRole === 'student'">
          <option value="">所有专业</option>
          <option value="1">计算机类</option>
          <option value="2">工设类</option>
          <option value="3">艺术类</option>
          <option value="4">医学类</option>
          <option value="5">文科类</option>
          <option value="6">体育类</option>
          <option value="7">其他</option>
        </select>
        
        <select v-model="filterStatus" @change="searchUsers" class="filter-select">
          <option value="">所有状态</option>
          <option value="active">正常</option>
          <option value="inactive">禁用</option>
        </select>
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
                  :src="user.avatar || '/default-avatar.png'" 
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
                <button class="btn btn-sm btn-outline-warning" @click="toggleUserStatus(user)">
                  {{ user.status === 'active' ? '禁用' : '启用' }}
                </button>
                <button class="btn btn-sm btn-outline-info" @click="changeUserRole(user)">
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

    <!-- 修改角色模态框 -->
    <div v-if="showRoleModal" class="modal-overlay" @click="showRoleModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>修改用户角色</h3>
          <button class="close-btn" @click="showRoleModal = false">&times;</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>用户: {{ selectedUser?.nickname || selectedUser?.account }}</label>
          </div>
          <div class="form-group">
            <label>当前角色: {{ getRoleText(selectedUser?.groupType) }}</label>
          </div>
          <div class="form-group">
            <label>新角色:</label>
            <select v-model="newRole" class="form-control">
              <optgroup label="学生专业">
                <option value="1">计算机类</option>
                <option value="2">工设类</option>
                <option value="3">艺术类</option>
                <option value="4">医学类</option>
                <option value="5">文科类</option>
                <option value="6">体育类</option>
              </optgroup>
              <optgroup label="系统角色">
                <option value="7">教师</option>
                <option value="8">管理员</option>
              </optgroup>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showRoleModal = false">取消</button>
          <button class="btn btn-primary" @click="confirmChangeRole">确认修改</button>
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
    const filterMajor = ref('')
    const filterStatus = ref('')
    const showRoleModal = ref(false)
    const selectedUser = ref(null)
    const newRole = ref('')

    const filteredUsers = computed(() => {
      let filtered = users.value

      // 按角色筛选
      if (filterRole.value) {
        if (filterRole.value === 'student') {
          filtered = filtered.filter(user => user.groupType >= 1 && user.groupType <= 7)
        } else if (filterRole.value === 'teacher') {
          filtered = filtered.filter(user => user.groupType === 8)
        } else if (filterRole.value === 'admin') {
          filtered = filtered.filter(user => user.groupType === 9)
        }
      }

      // 按专业筛选（仅对学生）
      if (filterMajor.value && filterRole.value === 'student') {
        filtered = filtered.filter(user => user.groupType === parseInt(filterMajor.value))
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
          (user.email && user.email.toLowerCase().includes(query))
        )
      }

      return filtered
    })

    const loadUsers = async () => {
      loading.value = true
      try {
        const response = await axios.get('/api/admin/users')
        users.value = response.data
      } catch (error) {
        console.error('加载用户失败:', error)
      } finally {
        loading.value = false
      }
    }

    const searchUsers = () => {
      // 实时搜索，不需要额外API调用
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
      return new Date(date).toLocaleDateString('zh-CN')
    }

    const toggleUserStatus = async (user) => {
      try {
        const newStatus = user.status === 'active' ? 'inactive' : 'active'
        await axios.put(`/api/admin/users/${user.userId}/status?status=${newStatus}`)
        user.status = newStatus
        this.$message.success('用户状态更新成功')
      } catch (error) {
        console.error('更新用户状态失败:', error)
        this.$message.error('更新用户状态失败')
      }
    }

    const changeUserRole = (user) => {
      selectedUser.value = user
      newRole.value = user.groupType.toString()
      showRoleModal.value = true
    }

    const confirmChangeRole = async () => {
      try {
        await axios.put(`/api/admin/users/${selectedUser.value.userId}/role?groupType=${newRole.value}`)
        selectedUser.value.groupType = parseInt(newRole.value)
        showRoleModal.value = false
        this.$message.success('用户角色更新成功')
      } catch (error) {
        console.error('更新用户角色失败:', error)
        this.$message.error('更新用户角色失败')
      }
    }

    const handleAvatarError = (event) => {
      if (event.target && event.target.nextElementSibling) {
        event.target.style.display = 'none'
        event.target.nextElementSibling.style.display = 'flex'
      }
    }

    const deleteUser = async (user) => {
      if (!confirm(`确定要删除用户 ${user.nickname || user.account} 吗？`)) {
        return
      }
      
      try {
        await axios.delete(`/api/admin/users/${user.userId}`)
        users.value = users.value.filter(u => u.userId !== user.userId)
        this.$message.success('用户删除成功')
      } catch (error) {
        console.error('删除用户失败:', error)
        this.$message.error('删除用户失败')
      }
    }

    onMounted(() => {
      loadUsers()
    })

    return {
      users,
      loading,
      searchQuery,
      filterRole,
      filterMajor,
      filterStatus,
      showRoleModal,
      selectedUser,
      newRole,
      filteredUsers,
      loadUsers,
      searchUsers,
      getRoleText,
      getRoleBadgeClass,
      getStatusText,
      getStatusBadgeClass,
      formatDate,
      toggleUserStatus,
      changeUserRole,
      confirmChangeRole,
      deleteUser,
      handleAvatarError
    }
  }
}
</script>

<style scoped>
.admin-users {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-filters {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 200px;
}

.filter-group {
  display: flex;
  gap: 10px;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

.users-table {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.loading-container, .empty-container {
  text-align: center;
  padding: 40px;
}

.loading-spinner {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #007bff;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
  margin: 0 auto 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.avatar-container {
  position: relative;
  width: 40px;
  height: 40px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #e5e7eb;
}

.avatar-fallback {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: none;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  border: 2px solid #e5e7eb;
}

.action-buttons {
  display: flex;
  gap: 5px;
}

.badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
}

.badge-primary { background: #007bff; color: white; }
.badge-warning { background: #ffc107; color: black; }
.badge-danger { background: #dc3545; color: white; }
.badge-success { background: #28a745; color: white; }
.badge-secondary { background: #6c757d; color: white; }

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
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-body {
  padding: 20px;
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.btn-primary { background: #007bff; color: white; }
.btn-secondary { background: #6c757d; color: white; }
.btn-success { background: #28a745; color: white; }
.btn-danger { background: #dc3545; color: white; }
.btn-warning { background: #ffc107; color: black; }
.btn-info { background: #17a2b8; color: white; }
.btn-outline-primary { background: white; color: #007bff; border: 1px solid #007bff; }
.btn-outline-warning { background: white; color: #ffc107; border: 1px solid #ffc107; }
.btn-outline-info { background: white; color: #17a2b8; border: 1px solid #17a2b8; }
.btn-outline-danger { background: white; color: #dc3545; border: 1px solid #dc3545; }
.btn-sm { padding: 4px 8px; font-size: 12px; }

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
}
</style> 