<template>
  <div class="admin-user-roles">
    <div class="page-header">
      <h2>用户角色管理</h2>
      <div class="header-actions">
        <button class="btn btn-primary" @click="refreshData">
          <i class="fas fa-sync-alt"></i> 刷新数据
        </button>
      </div>
    </div>

    <!-- 角色统计卡片 -->
    <div class="role-stats">
      <div class="stat-card" v-for="stat in roleStats" :key="stat.roleType">
        <div class="stat-icon" :class="stat.roleType">
          <i :class="getRoleIcon(stat.roleType)"></i>
        </div>
        <div class="stat-content">
          <h3>{{ stat.count }}</h3>
          <p>{{ stat.roleName }}</p>
        </div>
      </div>
    </div>

    <!-- 专业分布统计 -->
    <div class="major-stats">
      <h3>学生专业分布</h3>
      <div class="major-grid">
        <div class="major-card" v-for="major in majorStats" :key="major.majorType">
          <div class="major-icon">
            <i :class="getMajorIcon(major.majorType)"></i>
          </div>
          <div class="major-content">
            <h4>{{ major.majorName }}</h4>
            <p>{{ major.count }} 人</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 用户列表 -->
    <div class="users-section">
      <h3>用户列表</h3>
      <div class="filter-bar">
        <select v-model="filterRole" @change="filterUsers" class="filter-select">
          <option value="">所有角色</option>
          <option value="student">学生</option>
          <option value="teacher">教师</option>
          <option value="admin">管理员</option>
        </select>
        
        <select v-model="filterMajor" @change="filterUsers" class="filter-select" v-if="filterRole === 'student'">
          <option value="">所有专业</option>
          <option value="1">计算机类</option>
          <option value="2">工设类</option>
          <option value="3">艺术类</option>
          <option value="4">医学类</option>
          <option value="5">文科类</option>
          <option value="6">体育类</option>
          <option value="7">其他</option>
        </select>
      </div>

      <div class="users-table">
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>头像</th>
              <th>账号</th>
              <th>昵称</th>
              <th>邮箱</th>
              <th>当前角色</th>
              <th>状态</th>
              <th>注册时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.userId">
              <td>{{ user.userId }}</td>
              <td>
                <img :src="user.avatar || '/default-avatar.png'" :alt="user.nickname" class="user-avatar">
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
                <button class="btn btn-sm btn-outline-info" @click="changeUserRole(user)">
                  修改角色
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
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
                <option value="7">其他</option>
              </optgroup>
              <optgroup label="系统角色">
                <option value="8">教师</option>
                <option value="9">管理员</option>
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
  name: 'AdminUserRoles',
  setup() {
    const users = ref([])
    const roleStats = ref([])
    const majorStats = ref([])
    const filterRole = ref('')
    const filterMajor = ref('')
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

      return filtered
    })

    const loadData = async () => {
      try {
        const [usersRes, roleStatsRes, majorStatsRes] = await Promise.all([
          axios.get('/api/admin/users'),
          axios.get('/api/admin/statistics/roles'),
          axios.get('/api/admin/statistics/majors')
        ])
        
        users.value = usersRes.data
        roleStats.value = roleStatsRes.data
        majorStats.value = majorStatsRes.data
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    }

    const filterUsers = () => {
      // 实时筛选，不需要额外API调用
    }

    const getRoleIcon = (roleType) => {
      const iconMap = {
        'student': 'fas fa-graduation-cap',
        'teacher': 'fas fa-chalkboard-teacher',
        'admin': 'fas fa-user-shield'
      }
      return iconMap[roleType] || 'fas fa-user'
    }

    const getMajorIcon = (majorType) => {
      const iconMap = {
        1: 'fas fa-laptop-code',
        2: 'fas fa-palette',
        3: 'fas fa-paint-brush',
        4: 'fas fa-heartbeat',
        5: 'fas fa-book-open',
        6: 'fas fa-running',
        7: 'fas fa-ellipsis-h'
      }
      return iconMap[majorType] || 'fas fa-user-graduate'
    }

    const getRoleText = (groupType) => {
      switch (groupType) {
        case 1: return '计算机类学生'
        case 2: return '工设类学生'
        case 3: return '艺术类学生'
        case 4: return '医学类学生'
        case 5: return '文科类学生'
        case 6: return '体育类学生'
        case 7: return '其他专业学生'
        case 8: return '教师'
        case 9: return '管理员'
        default: return '未知'
      }
    }

    const getRoleBadgeClass = (groupType) => {
      if (groupType >= 1 && groupType <= 7) {
        return 'badge badge-primary'
      } else if (groupType === 8) {
        return 'badge badge-warning'
      } else if (groupType === 9) {
        return 'badge badge-danger'
      }
      return 'badge badge-secondary'
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
        await loadData() // 重新加载数据以更新统计
        this.$message.success('用户角色更新成功')
      } catch (error) {
        console.error('更新用户角色失败:', error)
        this.$message.error('更新用户角色失败')
      }
    }

    const refreshData = async () => {
      await loadData()
    }

    onMounted(() => {
      loadData()
    })

    return {
      users,
      roleStats,
      majorStats,
      filterRole,
      filterMajor,
      showRoleModal,
      selectedUser,
      newRole,
      filteredUsers,
      loadData,
      filterUsers,
      getRoleIcon,
      getMajorIcon,
      getRoleText,
      getRoleBadgeClass,
      getStatusText,
      getStatusBadgeClass,
      formatDate,
      changeUserRole,
      confirmChangeRole,
      refreshData
    }
  }
}
</script>

<style scoped>
.admin-user-roles {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.role-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.stat-icon.student { background: linear-gradient(135deg, #007bff, #0056b3); }
.stat-icon.teacher { background: linear-gradient(135deg, #ffc107, #e0a800); }
.stat-icon.admin { background: linear-gradient(135deg, #dc3545, #c82333); }

.stat-content h3 {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  color: #333;
}

.stat-content p {
  margin: 5px 0 0;
  color: #666;
  font-size: 14px;
}

.major-stats {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 30px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.major-stats h3 {
  margin: 0 0 20px;
  color: #333;
  font-size: 18px;
}

.major-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.major-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px;
  border-radius: 8px;
  background: #f8f9fa;
  transition: background 0.2s;
}

.major-card:hover {
  background: #e9ecef;
}

.major-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.major-content h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
}

.major-content p {
  margin: 5px 0 0;
  font-size: 12px;
  color: #666;
}

.users-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.users-section h3 {
  margin: 0 0 20px;
  color: #333;
  font-size: 18px;
}

.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

.users-table {
  overflow-x: auto;
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
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
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
.btn-outline-info { background: white; color: #17a2b8; border: 1px solid #17a2b8; }
.btn-sm { padding: 4px 8px; font-size: 12px; }

@media (max-width: 768px) {
  .role-stats {
    grid-template-columns: 1fr;
  }
  
  .major-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
}
</style>
