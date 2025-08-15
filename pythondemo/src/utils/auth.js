// 认证工具函数

/**
 * 设置登录状态
 * @param {Object} user 用户信息
 */
export function setLoginState(user) {
  localStorage.setItem('user', JSON.stringify(user))
  localStorage.setItem('isLoggedIn', 'true')
}

/**
 * 清除登录状态
 */
export function clearLoginState() {
  localStorage.removeItem('user')
  localStorage.removeItem('isLoggedIn')
  localStorage.removeItem('redirectPath')
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








