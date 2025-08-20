/**
 * 头像管理工具
 */

// 默认头像配置 - 使用更可靠的图片服务
const DEFAULT_AVATARS = {
  small: 'https://via.placeholder.com/32x32/3B82F6/FFFFFF?text=U',
  medium: 'https://via.placeholder.com/64x64/3B82F6/FFFFFF?text=U',
  large: 'https://via.placeholder.com/128x128/3B82F6/FFFFFF?text=U'
}

/**
 * 处理头像URL
 * @param {string} avatar 头像路径
 * @param {string} size 头像尺寸 (small, medium, large)
 * @returns {string} 处理后的头像URL
 */
export function getAvatarUrl(avatar, size = 'small') {
  // 如果没有头像，返回默认头像
  if (!avatar || typeof avatar !== 'string' || avatar.trim() === '') {
    return DEFAULT_AVATARS[size] || DEFAULT_AVATARS.small
  }
  
  // 如果是相对路径，添加后端基础URL
  if (avatar.startsWith('/avatar/')) {
    return 'http://localhost:8080' + avatar
  }
  
  // 如果是完整URL，直接返回
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  
  // 如果是其他相对路径，添加后端基础URL
  return 'http://localhost:8080' + avatar
}

/**
 * 获取默认头像URL
 * @param {string} size 头像尺寸
 * @returns {string} 默认头像URL
 */
export function getDefaultAvatarUrl(size = 'small') {
  return DEFAULT_AVATARS[size] || DEFAULT_AVATARS.small
}

/**
 * 检查头像是否有效
 * @param {string} avatar 头像路径
 * @returns {boolean} 是否有效
 */
export function isValidAvatar(avatar) {
  return avatar && typeof avatar === 'string' && avatar.trim() !== ''
}

/**
 * 获取头像显示状态
 * @param {string} avatar 头像路径
 * @returns {string} 状态描述
 */
export function getAvatarStatus(avatar) {
  if (!avatar) return '无头像'
  if (typeof avatar !== 'string') return '头像格式错误'
  if (avatar.trim() === '') return '头像路径为空'
  return '有头像'
}
