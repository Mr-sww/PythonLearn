// 认证工具函数

// 用户角色定义
export const USER_ROLES = {
  STUDENT: 'student',
  TEACHER: 'teacher', 
  ADMIN: 'admin'
}

// 专业类型映射
export const MAJOR_TYPES = {
  1: '计算机类',
  2: '工设类', 
  3: '艺术类',
  4: '医学类',
  5: '文科类',
  6: '体育类'
}

// 角色权限映射
export const ROLE_PERMISSIONS = {
  [USER_ROLES.STUDENT]: ['view_courses', 'practice_problems', 'view_profile', 'ai_chat', 'view_my_courses'],
  [USER_ROLES.TEACHER]: ['view_courses', 'manage_courses', 'manage_students', 'view_analytics', 'view_my_courses', 'create_courses'],
  [USER_ROLES.ADMIN]: ['view_courses', 'manage_users', 'manage_system', 'view_all_analytics', 'access_admin_panel']
}

/**
 * 根据group_type获取用户角色
 * @param {number} groupType 用户专业类型
 * @returns {string} 用户角色
 */
export function getRoleByGroupType(groupType) {
  if (!groupType) return USER_ROLES.STUDENT
  
  const groupTypeNum = Number(groupType)
  
  if (groupTypeNum >= 1 && groupTypeNum <= 6) {
    return USER_ROLES.STUDENT
  } else if (groupTypeNum === 7) {
    return USER_ROLES.TEACHER
  } else if (groupTypeNum === 8) {
    return USER_ROLES.ADMIN
  }
  
  return USER_ROLES.STUDENT
}

/**
 * 获取用户专业名称
 * @param {number} groupType 用户专业类型
 * @returns {string} 专业名称
 */
export function getMajorName(groupType) {
  if (!groupType) return '未设置'
  
  const groupTypeNum = Number(groupType)
  
  if (groupTypeNum >= 1 && groupTypeNum <= 6) {
    return MAJOR_TYPES[groupTypeNum] || '未知专业'
  } else if (groupTypeNum === 7) {
    return '教师'
  } else if (groupTypeNum === 8) {
    return '管理员'
  }
  
  return '未设置'
}

/**
 * 设置登录状态
 * @param {Object} user 用户信息
 */
export function setLoginState(user) {
  // 根据group_type自动设置用户角色
  const userRole = getRoleByGroupType(user.groupType)
  
  localStorage.setItem('user', JSON.stringify(user))
  localStorage.setItem('userId', user.userId || user.user_id)
  localStorage.setItem('isLoggedIn', 'true')
  localStorage.setItem('userRole', userRole)
}

/**
 * 清除登录状态
 */
export function clearLoginState() {
  localStorage.removeItem('user')
  localStorage.removeItem('userId')
  localStorage.removeItem('isLoggedIn')
  localStorage.removeItem('redirectPath')
  localStorage.removeItem('userRole')
}

/**
 * 获取登录状态
 * @returns {boolean} 是否已登录
 */
export function isLoggedIn() {
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  return user && (user.userId || user.user_id)
}

/**
 * 获取当前用户信息
 * @returns {Object|null} 用户信息
 */
export function getCurrentUser() {
  return JSON.parse(localStorage.getItem('user') || 'null')
}

/**
 * 获取当前用户角色
 * @returns {string} 用户角色
 */
export function getCurrentUserRole() {
  // 优先从localStorage获取，如果没有则从用户信息中计算
  let userRole = localStorage.getItem('userRole')
  
  if (!userRole) {
    const user = getCurrentUser()
    if (user && user.groupType) {
      userRole = getRoleByGroupType(user.groupType)
      // 保存到localStorage
      localStorage.setItem('userRole', userRole)
    } else {
      userRole = USER_ROLES.STUDENT
    }
  }
  
  return userRole
}

/**
 * 检查用户是否有指定权限
 * @param {string} permission 权限名称
 * @returns {boolean} 是否有权限
 */
export function hasPermission(permission) {
  const role = getCurrentUserRole()
  return ROLE_PERMISSIONS[role]?.includes(permission) || false
}

/**
 * 检查用户是否为指定角色
 * @param {string} role 角色名称
 * @returns {boolean} 是否为指定角色
 */
export function isRole(role) {
  const currentRole = getCurrentUserRole()
  return currentRole === role
}

/**
 * 检查用户是否为学生
 * @returns {boolean} 是否为学生
 */
export function isStudent() {
  return isRole(USER_ROLES.STUDENT)
}

/**
 * 检查用户是否为教师
 * @returns {boolean} 是否为教师
 */
export function isTeacher() {
  return isRole(USER_ROLES.TEACHER)
}

/**
 * 检查用户是否为管理员
 * @returns {boolean} 是否为管理员
 */
export function isAdmin() {
  return isRole(USER_ROLES.ADMIN)
}

/**
 * 保存跳转路径
 * @param {string} path 路径
 */
export function saveRedirectPath(path) {
  localStorage.setItem('redirectPath', path)
}

/**
 * 获取并清除跳转路径
 * @returns {string|null} 跳转路径
 */
export function getAndClearRedirectPath() {
  const path = localStorage.getItem('redirectPath')
  if (path) {
    localStorage.removeItem('redirectPath')
  }
  return path
}

/**
 * 检查用户是否完成专业选择
 * @returns {boolean} 是否完成专业选择
 */
export function hasCompletedMajorSelection() {
  const user = getCurrentUser()
  return user && user.groupType
}

/**
 * 检查用户是否完成学习方向选择
 * @returns {boolean} 是否完成学习方向选择
 */
export function hasCompletedLearningDirectionSelection() {
  const user = getCurrentUser()
  return user && user.intestTypes && user.intestTypes.length > 0
}

/**
 * 获取当前用户ID
 * @returns {number|null} 用户ID
 */
export function getCurrentUserId() {
  const userId = localStorage.getItem('userId')
  if (userId) {
    return parseInt(userId)
  }
  
  const user = getCurrentUser()
  return user ? (user.userId || user.user_id) : null
}

/**
 * 检查登录状态并重定向
 * @param {Object} router Vue Router实例
 * @returns {boolean} 是否已登录
 */
export function checkLoginAndRedirect(router) {
  if (!isLoggedIn()) {
    // 保存当前路径用于登录后重定向
    saveRedirectPath(router.currentRoute.value.fullPath)
    router.push('/auth')
    return false
  }
  return true
}








