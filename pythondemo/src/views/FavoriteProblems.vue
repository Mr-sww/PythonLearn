<template>
  <div class="favorite-problems-container bg-gray-50 min-h-screen py-8">
    <div class="container mx-auto max-w-5xl">
      <!-- 顶部标题 -->
      <div class="flex items-center justify-between mb-8">
        <div class="flex items-center gap-3">
          <router-link to="/profile" class="btn btn-outline-primary rounded-pill px-4 py-2">
            <i class="fa fa-arrow-left"></i> 返回个人中心
          </router-link>
          <h2 class="text-2xl font-bold text-dark ml-2 flex items-center">
            <i class="fa fa-star text-warning mr-2"></i>我的收藏题目
          </h2>
        </div>
        <button @click="refreshFavorites" class="btn btn-outline-primary rounded-pill px-4 py-2" :disabled="loading">
          <i class="fa fa-refresh mr-1"></i>刷新
        </button>
      </div>

      <!-- 加载动画 -->
      <div v-if="loading" class="flex flex-col items-center justify-center py-16">
        <div class="spinner-border text-primary mb-4" role="status"></div>
        <div class="text-muted">正在加载收藏题目...</div>
      </div>

      <!-- 收藏题目列表 -->
      <div v-else-if="favoriteProblems.length > 0" class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div v-for="favorite in favoriteProblems" :key="favorite.favorite_id">
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1 flex flex-col h-full">
            <!-- 题目头部 -->
            <div class="relative p-5 pb-0">
              <h3 class="text-lg font-bold text-gray-800 mb-1 line-clamp-2">{{ favorite.title }}</h3>
              <div class="flex items-center text-sm text-gray-500 mb-2">
                <span class="badge bg-secondary me-2">ID: {{ favorite.problem_id }}</span>
                <span><i class="fa fa-clock mr-1"></i>{{ formatDate(favorite.created_at) }} 收藏</span>
                <span class="ml-auto"><i class="fa fa-star text-warning mr-1"></i>{{ favorite.favoriteCount ?? '--' }}</span>
              </div>
            </div>
            <!-- 题目描述 -->
            <div class="px-5 pb-5 flex-1">
              <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ favorite.description || '暂无描述' }}</p>
              <div class="flex gap-2 mt-auto">
                <button @click="viewProblem(favorite.problem_id)" class="flex-1 bg-blue-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-blue-700 transition-colors">
                  <i class="fa fa-eye mr-1"></i>去做题
                </button>
                <button @click="removeFavorite(favorite)" class="bg-red-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-red-700 transition-colors">
                  <i class="fa fa-star-half-alt mr-1"></i>取消收藏
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="flex flex-col items-center justify-center py-16">
        <i class="fa fa-star-half-alt fa-4x text-muted mb-4"></i>
        <h4 class="text-muted mb-2">暂无收藏题目</h4>
        <p class="text-muted mb-4">您还没有收藏任何题目，快去刷题吧！</p>
        <router-link to="/practice" class="btn btn-primary btn-lg">
          <i class="fa fa-search mr-2"></i>浏览题库
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'FavoriteProblems',
  data() {
    return {
      favoriteProblems: [],
      loading: true,
      currentUser: {
        userId: 1
      }
    }
  },
  mounted() {
    this.loadCurrentUser()
    this.loadFavoriteProblems()
  },
  methods: {
    loadCurrentUser() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        this.currentUser.userId = parseInt(userId)
      } else {
        this.$router.push('/login')
      }
    },
    async loadFavoriteProblems() {
      this.loading = true
      try {
        const res = await axios.get('/api/practice/favorites', { params: { userId: this.currentUser.userId } })
        this.favoriteProblems = res.data
      } catch (error) {
        this.favoriteProblems = []
      } finally {
        this.loading = false
      }
    },
    async refreshFavorites() {
      await this.loadFavoriteProblems()
    },
    viewProblem(problemId) {
      this.$router.push(`/problem/${problemId}`)
    },
    async removeFavorite(favorite) {
      if (!confirm('确定要取消收藏该题目吗？')) return
      try {
        await axios.delete(`/api/practice/problem/${favorite.problem_id}/favorite`, { params: { userId: this.currentUser.userId } })
        await this.loadFavoriteProblems()
        this.$toast?.success?.('取消收藏成功')
      } catch (error) {
        this.$toast?.error?.('取消收藏失败，请重试')
      }
    },
    formatDate(date) {
      if (!date) return '未知'
      const d = new Date(date)
      const now = new Date()
      const diff = now - d
      if (diff < 86400000) return '今天'
      if (diff < 172800000) return '昨天'
      if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`
      return d.toLocaleDateString('zh-CN')
    }
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.spinner-border {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #2563eb;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  animation: spin 1s linear infinite;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 