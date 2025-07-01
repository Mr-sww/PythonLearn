<template>
  <div class="favorite-courses-container bg-gray-50 min-h-screen py-8">
    <div class="container mx-auto max-w-5xl">
      <!-- 顶部标题 -->
      <div class="flex items-center justify-between mb-8">
        <div class="flex items-center gap-3">
          <router-link to="/profile" class="btn btn-outline-primary rounded-pill px-4 py-2">
            <i class="fa fa-arrow-left"></i> 返回个人中心
          </router-link>
          <h2 class="text-2xl font-bold text-dark ml-2 flex items-center">
            <i class="fa fa-heart text-danger mr-2"></i>我的收藏课程
          </h2>
        </div>
        <button @click="refreshFavorites" class="btn btn-outline-primary rounded-pill px-4 py-2" :disabled="loading">
          <i class="fa fa-refresh mr-1"></i>刷新
        </button>
      </div>

      <!-- 加载动画 -->
      <div v-if="loading" class="flex flex-col items-center justify-center py-16">
        <div class="spinner-border text-primary mb-4" role="status"></div>
        <div class="text-muted">正在加载收藏课程...</div>
      </div>

      <!-- 收藏课程列表 -->
      <div v-else-if="favoriteCourses.length > 0" class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div v-for="favorite in favoriteCourses" :key="favorite.id">
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1 flex flex-col h-full">
            <!-- 课程封面 -->
            <div class="relative">
              <img :src="favorite.course?.coverImage || `https://picsum.photos/600/300?random=${favorite.courseId}`" :alt="favorite.course?.title" class="w-full h-48 object-cover">
              <!-- 收藏时间 -->
              <div class="absolute top-3 left-3 bg-dark bg-opacity-70 text-white text-xs rounded px-2 py-1">
                <i class="fa fa-clock mr-1"></i>{{ formatDate(favorite.createTime) }} 收藏
              </div>
              <!-- 收藏数 -->
              <div class="absolute top-3 right-3 bg-light text-dark text-xs rounded px-2 py-1 flex items-center">
                <i class="fa fa-heart text-danger mr-1"></i>{{ favorite.favoriteCount ?? '--' }}
              </div>
            </div>
            <!-- 课程内容 -->
            <div class="p-5 flex flex-col flex-1">
              <div class="flex items-center mb-2">
                <img :src="favorite.course?.authorAvatar || `https://picsum.photos/100/100?random=${favorite.courseId}`" class="w-8 h-8 rounded-full mr-2 border"/>
                <span class="text-sm text-gray-600">{{ favorite.course?.author || '未知讲师' }}</span>
                <div class="ml-auto flex items-center">
                  <div class="flex text-yellow-400 text-sm">
                    <i class="fa fa-star" v-for="i in 5" :key="i"></i>
                  </div>
                  <span class="text-sm text-gray-500 ml-1">{{ favorite.course?.rating || '4.5' }}</span>
                </div>
              </div>
              <h3 class="text-lg font-bold text-gray-800 mb-1 line-clamp-2">{{ favorite.course?.title || '课程标题' }}</h3>
              <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ favorite.course?.content || '暂无描述' }}</p>
              <div class="flex flex-wrap gap-2 mb-3">
                <span v-for="tag in getTags(favorite.course?.tags)" :key="tag" class="text-xs bg-blue-100 text-blue-600 px-2 py-1 rounded">{{ tag }}</span>
              </div>
              <div class="flex justify-between items-center text-sm text-gray-500 mb-3">
                <span><i class="fa fa-users mr-1"></i>{{ formatNumber(favorite.course?.views || 0) }}人学习</span>
                <span><i class="fa fa-clock mr-1"></i>{{ favorite.course?.duration || '2小时' }}</span>
                <span><i class="fa fa-play-circle mr-1"></i>{{ favorite.course?.lessons || '15课时' }}</span>
              </div>
              <div class="flex gap-2 mt-auto">
                <button @click="viewCourse(favorite.course)" class="flex-1 bg-blue-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-blue-700 transition-colors">
                  <i class="fa fa-eye mr-1"></i>查看详情
                </button>
                <button @click="removeFavorite(favorite)" class="bg-red-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-red-700 transition-colors">
                  <i class="fa fa-heart-broken mr-1"></i>取消收藏
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="flex flex-col items-center justify-center py-16">
        <i class="fa fa-heart-broken fa-4x text-muted mb-4"></i>
        <h4 class="text-muted mb-2">暂无收藏课程</h4>
        <p class="text-muted mb-4">您还没有收藏任何课程，快去发现感兴趣的课程吧！</p>
        <router-link to="/courses" class="btn btn-primary btn-lg">
          <i class="fa fa-search mr-2"></i>浏览课程
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import favoriteService from '@/services/favoriteService.js'

export default {
  name: 'FavoriteCourses',
  data() {
    return {
      favoriteCourses: [],
      loading: true,
      currentUser: {
        userId: 1
      }
    }
  },
  mounted() {
    this.loadCurrentUser()
    this.loadFavoriteCourses()
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
    async loadFavoriteCourses() {
      this.loading = true
      try {
        const list = await favoriteService.getUserFavorites(this.currentUser.userId)
        // 获取每门课程的收藏数
        const withCount = await Promise.all(list.map(async fav => {
          let favoriteCount = 0
          try {
            favoriteCount = await favoriteService.getFavoriteCountByCourse(fav.courseId)
          } catch (e) { /* 忽略错误 */ }
          return { ...fav, favoriteCount }
        }))
        this.favoriteCourses = withCount
      } catch (error) {
        this.favoriteCourses = []
      } finally {
        this.loading = false
      }
    },
    async refreshFavorites() {
      await this.loadFavoriteCourses()
    },
    viewCourse(course) {
      if (course && course.articleId) {
        this.$router.push(`/course/${course.articleId}`)
      }
    },
    async removeFavorite(favorite) {
      if (!confirm('确定要取消收藏这门课程吗？')) return
      try {
        await favoriteService.removeFavorite(this.currentUser.userId, favorite.courseId)
        await this.loadFavoriteCourses()
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
    },
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      }
      return num.toString()
    },
    getTags(tags) {
      if (typeof tags === 'string') {
        return tags.split(',').slice(0, 3)
      }
      return []
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