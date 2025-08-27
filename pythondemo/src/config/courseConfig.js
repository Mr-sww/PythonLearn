/**
 * 课程配置常量
 * 统一管理课程相关的配置和状态
 */

// 课程状态常量
export const COURSE_STATUS = {
  PENDING: 'pending',      // 待审核
  APPROVED: 'approved',    // 已通过
  REJECTED: 'rejected',    // 已拒绝
  ACTIVE: 'active',        // 已激活
  DRAFT: 'draft',          // 草稿
  INACTIVE: 'inactive'     // 已禁用
}

// 课程难度常量
export const COURSE_DIFFICULTY = {
  BEGINNER: 'beginner',           // 初级
  INTERMEDIATE: 'intermediate',   // 中级
  ADVANCED: 'advanced'            // 高级
}

// 课程分类常量
export const COURSE_CATEGORY = {
  PROGRAMMING: '编程开发',
  WEB_DEVELOPMENT: 'Web开发',
  DATA_SCIENCE: '数据科学',
  ARTIFICIAL_INTELLIGENCE: '人工智能',
  MOBILE_DEVELOPMENT: '移动开发',
  GAME_DEVELOPMENT: '游戏开发',
  CYBERSECURITY: '网络安全',
  CLOUD_COMPUTING: '云计算',
  OTHER: '其他'
}

// 审核操作常量
export const REVIEW_ACTION = {
  APPROVE: 'approve',     // 通过
  REJECT: 'reject'        // 拒绝
}

// 用户角色/专业常量
// 1-6: 各专业学生（具体专业类别）
// 7: 教师
// 8: 管理员
export const USER_ROLE = {
  COMPUTER_SCIENCE: 1,      // 计算机类
  INDUSTRIAL_DESIGN: 2,     // 工设类
  ARTS: 3,                  // 艺术类
  MEDICINE: 4,              // 医学类
  LIBERAL_ARTS: 5,          // 文科类
  SPORTS: 6,                // 体育类
  TEACHER: 7,               // 教师
  ADMIN: 8                  // 管理员
}

// 默认值配置
export const DEFAULTS = {
  DEFAULT_VIEWS: 0,
  DEFAULT_RATING: 0.0,
  DEFAULT_LESSONS: 0,
  DEFAULT_DIFFICULTY: COURSE_DIFFICULTY.BEGINNER,
  DEFAULT_STATUS: COURSE_STATUS.PENDING
}

// 状态验证函数
export const isValidStatus = (status) => {
  return Object.values(COURSE_STATUS).includes(status)
}

// 难度验证函数
export const isValidDifficulty = (difficulty) => {
  return Object.values(COURSE_DIFFICULTY).includes(difficulty)
}

// 用户角色验证函数
export const isValidUserRole = (role) => {
  return role !== null && role >= 1 && role <= 8
}

// 获取状态的中文描述
export const getStatusText = (status) => {
  const statusMap = {
    [COURSE_STATUS.PENDING]: '待审核',
    [COURSE_STATUS.APPROVED]: '已通过',
    [COURSE_STATUS.REJECTED]: '已拒绝',
    [COURSE_STATUS.ACTIVE]: '已激活',
    [COURSE_STATUS.DRAFT]: '草稿',
    [COURSE_STATUS.INACTIVE]: '已禁用'
  }
  return statusMap[status] || '未知状态'
}

// 获取难度的中文描述
export const getDifficultyText = (difficulty) => {
  const difficultyMap = {
    [COURSE_DIFFICULTY.BEGINNER]: '初级',
    [COURSE_DIFFICULTY.INTERMEDIATE]: '中级',
    [COURSE_DIFFICULTY.ADVANCED]: '高级'
  }
  return difficultyMap[difficulty] || '未知难度'
}

// 获取用户角色的中文描述
export const getUserRoleText = (role) => {
  if (role === null || role === undefined) return '未知'
  
  const roleMap = {
    [USER_ROLE.COMPUTER_SCIENCE]: '计算机类',
    [USER_ROLE.INDUSTRIAL_DESIGN]: '工设类',
    [USER_ROLE.ARTS]: '艺术类',
    [USER_ROLE.MEDICINE]: '医学类',
    [USER_ROLE.LIBERAL_ARTS]: '文科类',
    [USER_ROLE.SPORTS]: '体育类',
    [USER_ROLE.TEACHER]: '教师',
    [USER_ROLE.ADMIN]: '管理员'
  }
  return roleMap[role] || '未知角色'
}

// 获取状态对应的CSS类
export const getStatusClass = (status) => {
  const statusClassMap = {
    [COURSE_STATUS.PENDING]: 'bg-yellow-100 text-yellow-800',
    [COURSE_STATUS.APPROVED]: 'bg-green-100 text-green-800',
    [COURSE_STATUS.REJECTED]: 'bg-red-100 text-red-800',
    [COURSE_STATUS.ACTIVE]: 'bg-blue-100 text-blue-800',
    [COURSE_STATUS.DRAFT]: 'bg-gray-100 text-gray-800',
    [COURSE_STATUS.INACTIVE]: 'bg-gray-100 text-gray-600'
  }
  return statusClassMap[status] || 'bg-gray-100 text-gray-600'
}

// 获取难度对应的CSS类
export const getDifficultyClass = (difficulty) => {
  const difficultyClassMap = {
    [COURSE_DIFFICULTY.BEGINNER]: 'bg-green-100 text-green-800',
    [COURSE_DIFFICULTY.INTERMEDIATE]: 'bg-yellow-100 text-yellow-800',
    [COURSE_DIFFICULTY.ADVANCED]: 'bg-red-100 text-red-800'
  }
  return difficultyClassMap[difficulty] || 'bg-gray-100 text-gray-600'
}

// 获取用户角色对应的CSS类
export const getUserRoleClass = (role) => {
  if (role >= 1 && role <= 6) {
    return 'bg-blue-100 text-blue-800'  // 学生角色
  } else if (role === USER_ROLE.TEACHER) {
    return 'bg-yellow-100 text-yellow-800'  // 教师角色
  } else if (role === USER_ROLE.ADMIN) {
    return 'bg-red-100 text-red-800'  // 管理员角色
  }
  return 'bg-gray-100 text-gray-600'  // 默认样式
}

// 判断是否为学生角色
export const isStudent = (role) => {
  return role !== null && role >= 1 && role <= 6
}

// 判断是否为教师角色
export const isTeacher = (role) => {
  return role === USER_ROLE.TEACHER
}

// 判断是否为管理员角色
export const isAdmin = (role) => {
  return role === USER_ROLE.ADMIN
}

export default {
  COURSE_STATUS,
  COURSE_DIFFICULTY,
  COURSE_CATEGORY,
  REVIEW_ACTION,
  USER_ROLE,
  DEFAULTS,
  isValidStatus,
  isValidDifficulty,
  isValidUserRole,
  getStatusText,
  getDifficultyText,
  getUserRoleText,
  getStatusClass,
  getDifficultyClass,
  getUserRoleClass,
  isStudent,
  isTeacher,
  isAdmin
}
