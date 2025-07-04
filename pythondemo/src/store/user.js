import { defineStore } from 'pinia'
import { ref } from 'vue'

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
