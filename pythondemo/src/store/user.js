import { defineStore } from 'pinia'
import { ref } from 'vue'

// 可复用：groupType 选项与校验（非侵入式导出）
export const GROUP_TYPE_OPTIONS = [
  { value: 1, label: '专业1' },
  { value: 2, label: '专业2' },
  { value: 3, label: '专业3' },
  { value: 4, label: '专业4' },
  { value: 5, label: '专业5' },
  { value: 6, label: '专业6' },
  { value: 7, label: '教师' },
  { value: 8, label: '管理员' }
]

export function isValidGroupType (v) {
  return Number.isInteger(v) && v >= 1 && v <= 8
}

export const useUserStore = defineStore('user', () => {
  const user = ref({})

  function setUser(newUser) {
    // 处理 groupType
    if (typeof newUser.groupType === 'string' && newUser.groupType.includes(',')) {
      // 只取第一个有效 groupType
      newUser.groupType = Number(newUser.groupType.split(',').find(x => x && x !== 'null'))
    } else if (typeof newUser.groupType === 'string') {
      newUser.groupType = Number(newUser.groupType)
    }
    // 处理 intestTypes
    if (typeof newUser.intestTypes === 'string') {
      newUser.intestTypes = newUser.intestTypes.split(',').filter(x => x).map(Number)
    }
    user.value = newUser
  }

  function updateAvatar(newAvatar) {
    if (user.value) user.value.avatar = newAvatar
  }

  function clearUser() {
    user.value = null
  }

  return { user, setUser, updateAvatar, clearUser }
})
