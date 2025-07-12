<template>
  <div>
    <!-- 加载状态 -->
    <div v-if="loading" class="container py-5 text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">加载中...</span>
      </div>
      <p class="mt-2 text-muted">正在加载课程详情...</p>
    </div>

    <!-- 课程详情内容 -->
    <div v-else-if="course" class="container py-4">
      <!-- 面包屑导航 -->
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
          <li class="breadcrumb-item">
            <router-link to="/" class="text-decoration-none">首页</router-link>
          </li>
          <li class="breadcrumb-item">
            <router-link to="/courses" class="text-decoration-none">课程中心</router-link>
          </li>
          <li class="breadcrumb-item active" aria-current="page">{{ course.title }}</li>
        </ol>
      </nav>

      <!-- 课程基本信息 -->
      <div class="row">
        <!-- 左侧课程信息 -->
        <div class="col-lg-8">
          <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
            <!-- 课程标题和基本信息 -->
            <div class="mb-4">
              <h1 class="fw-bold text-dark mb-3">{{ course.title }}</h1>
              <div class="d-flex flex-wrap gap-3 text-muted">
                <span><i class="fa fa-user me-1"></i>讲师：{{ course.author }}</span>
                <span><i class="fa fa-calendar me-1"></i>发布时间：{{ formatDate(course.publicationDate) }}</span>
                <span><i class="fa fa-eye me-1"></i>浏览量：{{ formatNumber(course.views) }}</span>
                <span><i class="fa fa-tag me-1"></i>分类：{{ course.category }}</span>
              </div>
            </div>

            <!-- 课程封面 -->
            <div class="mb-4">
              <img 
                :src="course.coverImage || `https://picsum.photos/800/400?random=${course.articleId}`" 
                :alt="course.title"
                class="w-100 rounded-3"
                style="max-height: 400px; object-fit: cover;"
                @error="handleImageError"
              >
            </div>

            <!-- 课程描述 -->
            <div class="mb-4">
              <h4 class="fw-bold text-dark mb-3">课程简介</h4>
              <p class="text-muted">{{ course.content || course.description || '暂无课程描述' }}</p>
            </div>

            <!-- 课程标签 -->
            <div v-if="course.tags" class="mb-4">
              <h5 class="fw-bold text-dark mb-2">课程标签</h5>
              <div class="d-flex flex-wrap gap-2">
                <span 
                  v-for="tag in getTags(course.tags)" 
                  :key="tag"
                  class="badge bg-light text-dark px-3 py-2"
                >
                  {{ tag }}
                </span>
              </div>
            </div>

            <!-- 课程大纲 -->
            <div class="mb-4">
              <h4 class="fw-bold text-dark mb-3">课程大纲</h4>
              <div class="accordion" id="courseOutline">
                <div 
                  v-for="(chapter, index) in courseOutline" 
                  :key="index"
                  class="accordion-item"
                >
                  <h2 class="accordion-header" :id="'heading' + index">
                    <button 
                      class="accordion-button" 
                      :class="{ collapsed: index !== 0 }"
                      type="button" 
                      data-bs-toggle="collapse" 
                      :data-bs-target="'#collapse' + index"
                      :aria-expanded="index === 0"
                      :aria-controls="'collapse' + index"
                    >
                      <div class="d-flex justify-content-between align-items-center w-100 me-3">
                        <span>{{ chapter.title }}</span>
                        <span class="text-muted small">{{ chapter.duration }}</span>
                      </div>
                    </button>
                  </h2>
                  <div 
                    :id="'collapse' + index" 
                    class="accordion-collapse collapse" 
                    :class="{ show: index === 0 }"
                    :aria-labelledby="'heading' + index" 
                    data-bs-parent="#courseOutline"
                  >
                    <div class="accordion-body">
                      <ul class="list-unstyled">
                        <li 
                          v-for="lesson in chapter.lessons" 
                          :key="lesson.id"
                          class="d-flex justify-content-between align-items-center py-2 border-bottom"
                        >
                          <div class="d-flex align-items-center">
                            <i class="fa fa-play-circle text-primary me-2"></i>
                            <span>{{ lesson.title }}</span>
                          </div>
                          <span class="text-muted small">{{ lesson.duration }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 学习进度 -->
          <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
            <h4 class="fw-bold text-dark mb-3">学习进度</h4>
            <ProgressTree 
              :courseId="course.articleId"
              :userId="currentUser?.userId || 1"
              @lesson-selected="handleLessonSelected"
              @start-lesson="handleStartLesson"
              @review-lesson="handleReviewLesson"
            />
          </div>

          <!-- 评论系统 -->
          <div class="bg-white rounded-3 shadow-sm border p-4">
            <CommentList 
              :courseId="course.articleId"
              :currentUser="currentUser"
              @comment-added="handleCommentAdded"
              @comment-deleted="handleCommentDeleted"
            />
          </div>
        </div>

        <!-- 右侧购买信息 -->
        <div class="col-lg-4">
          <div class="bg-white rounded-3 shadow-sm border p-4 sticky-top" style="top: 100px;">
            <!-- 价格信息 -->
            <div class="text-center mb-4">
              <div class="mb-3">
                <span v-if="course.price === 0 || course.price === null" class="display-6 text-success fw-bold">
                  免费
                </span>
                <span v-else class="display-6 text-danger fw-bold">
                  ¥{{ course.price }}
                </span>
              </div>
              <div class="d-flex justify-content-center gap-3 text-muted small">
                <span><i class="fa fa-clock me-1"></i>{{ course.duration || '2小时' }}</span>
                <span><i class="fa fa-play-circle me-1"></i>{{ course.lessons || '15课时' }}</span>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="d-grid gap-2 mb-4">
              <button 
                v-if="course.price === 0 || course.price === null"
                @click="handleStartLearning"
                class="btn btn-success btn-lg"
              >
                <i class="fa fa-play me-2"></i>立即学习
              </button>
              <button 
                v-else
                @click="handlePurchaseCourse"
                class="btn btn-danger btn-lg"
              >
                <i class="fa fa-shopping-cart me-2"></i>立即购买
              </button>
              <button 
                @click="handleToggleFavorite"
                :class="['btn', isFavorited ? 'btn-danger' : 'btn-outline-primary', 'd-flex', 'align-items-center', 'gap-2', 'px-4', 'py-2', 'rounded-pill', 'fw-bold']"
                style="font-size:1.1rem;"
              >
                <i :class="['fa', isFavorited ? 'fa-heart' : 'fa-heart-o', heartAnimate ? 'heart-animate' : '']"></i>
                {{ isFavorited ? '已收藏' : '收藏课程' }}
                <span v-if="favoriteCount !== null" class="badge bg-light text-dark ms-2">{{ favoriteCount }}</span>
              </button>
            </div>

            <!-- 课程统计 -->
            <div class="border-top pt-3">
              <div class="row text-center">
                <div class="col-4">
                  <div class="fw-bold text-dark">{{ formatNumber(course.views) }}</div>
                  <div class="text-muted small">学习人数</div>
                </div>
                <div class="col-4">
                  <div class="fw-bold text-dark">{{ course.rating || '4.5' }}</div>
                  <div class="text-muted small">课程评分</div>
                </div>
                <div class="col-4">
                  <div class="fw-bold text-dark">{{ course.lessons || '15' }}</div>
                  <div class="text-muted small">课程章节</div>
                </div>
              </div>
            </div>

            <!-- 讲师信息 -->
            <div class="border-top pt-3 mt-3">
              <h6 class="fw-bold text-dark mb-3">讲师信息</h6>
              <div class="d-flex align-items-center">
                <img 
                  :src="course.authorAvatar || `https://picsum.photos/100/100?random=${course.articleId}`" 
                  :alt="course.author"
                  class="rounded-circle me-3"
                  width="50"
                  height="50"
                  @error="handleAvatarError"
                >
                <div>
                  <div class="fw-bold">{{ course.author }}</div>
                  <div class="text-muted small">资深Python讲师</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 相关课程推荐 -->
      <div class="mt-5">
        <h3 class="fw-bold text-dark mb-4">相关课程推荐</h3>
        <div class="row g-4">
          <div class="col-md-6 col-lg-3" v-for="relatedCourse in relatedCourses" :key="relatedCourse.articleId">
            <CourseCard 
              :course="relatedCourse"
              @view-course="handleViewCourse"
              @start-learning="handleStartLearning"
              @purchase-course="handlePurchaseCourse"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else class="container py-5 text-center">
      <i class="fa fa-exclamation-triangle fa-3x text-warning mb-3"></i>
      <h4 class="text-muted">课程不存在或已被删除</h4>
      <router-link to="/courses" class="btn btn-primary mt-3">
        返回课程中心
      </router-link>
    </div>
  </div>
</template>

<script>
import CourseCard from '@/components/CourseCard.vue'
import CommentList from '@/components/CommentList.vue'
import ProgressTree from '@/components/ProgressTree.vue'
import favoriteService from '@/services/favoriteService.js'
import axios from 'axios'

export default {
  name: 'CourseDetailPage',
  components: {
    CourseCard,
    CommentList,
    ProgressTree
  },
  data() {
    return {
      course: null,
      loading: true,
      relatedCourses: [],
      currentUser: {
        userId: 1,
        nickname: '测试用户',
        avatar: 'https://picsum.photos/100/100?random=1'
      },
      courseOutline: [
        {
          title: '第1章：Python基础入门',
          duration: '45分钟',
          lessons: [
            { id: 1, title: '1.1 Python简介与环境搭建', duration: '15分钟' },
            { id: 2, title: '1.2 变量与数据类型', duration: '20分钟' },
            { id: 3, title: '1.3 基本语法结构', duration: '10分钟' }
          ]
        },
        {
          title: '第2章：控制流程',
          duration: '60分钟',
          lessons: [
            { id: 4, title: '2.1 条件语句', duration: '20分钟' },
            { id: 5, title: '2.2 循环语句', duration: '25分钟' },
            { id: 6, title: '2.3 异常处理', duration: '15分钟' }
          ]
        },
        {
          title: '第3章：函数与模块',
          duration: '75分钟',
          lessons: [
            { id: 7, title: '3.1 函数定义与调用', duration: '25分钟' },
            { id: 8, title: '3.2 参数传递', duration: '20分钟' },
            { id: 9, title: '3.3 模块导入', duration: '15分钟' },
            { id: 10, title: '3.4 包管理', duration: '15分钟' }
          ]
        }
      ],
      isFavorited: false,
      favoriteCount: null,
      heartAnimate: false
    }
  },
  mounted() {
    this.loadCourseDetail()
    this.checkFavoriteStatus()
    this.loadFavoriteCount()
  },
  methods: {
    // 加载课程详情
    async loadCourseDetail() {
      const courseId = this.$route.params.id
      if (!courseId) {
        this.loading = false
        return
      }

      try {
        const response = await axios.get(`/api/courses/${courseId}`)
        this.course = response.data
        this.loadRelatedCourses()
      } catch (error) {
        console.error('加载课程详情失败:', error)
        // 使用模拟数据
        this.loadMockCourse(courseId)
      } finally {
        this.loading = false
      }
    },

    // 加载模拟课程数据
    loadMockCourse(courseId) {
      this.course = {
        articleId: parseInt(courseId),
        title: 'Python基础入门到精通',
        content: '从环境搭建到项目实战，全面系统地学习Python编程基础，适合零基础学习者。本课程将带你从零开始，逐步掌握Python编程的核心概念和实践技能。',
        author: '张教授',
        category: 'Python基础',
        tags: 'Python,编程,学习,入门',
        views: 12458,
        price: 0,
        rating: 4.8,
        duration: '2小时',
        lessons: '15课时',
        publicationDate: new Date('2024-01-15'),
        difficulty: 'beginner'
      }
      this.loadRelatedCourses()
    },

    // 加载相关课程
    async loadRelatedCourses() {
      try {
        const response = await axios.get('/api/courses/recommended', {
          params: { limit: 4 }
        })
        this.relatedCourses = response.data
      } catch (error) {
        console.error('加载相关课程失败:', error)
        // 使用模拟数据
        this.loadMockRelatedCourses()
      }
    },

    // 加载模拟相关课程数据
    loadMockRelatedCourses() {
      this.relatedCourses = Array.from({ length: 4 }, (_, i) => ({
        articleId: 100 + i,
        title: `相关课程${i + 1}`,
        content: '相关课程描述',
        author: `讲师${i + 1}`,
        category: 'Python基础',
        tags: 'Python,编程',
        views: 1000 + i * 200,
        price: i % 2 === 0 ? 0 : 99 + i * 10,
        rating: 4.5 + i * 0.1,
        duration: `${1 + i * 0.5}小时`,
        lessons: `${10 + i * 2}课时`,
        difficulty: ['beginner', 'intermediate', 'advanced'][i % 3]
      }))
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '未知'
      return new Date(date).toLocaleDateString('zh-CN')
    },

    // 格式化数字
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      }
      return num.toString()
    },

    // 处理标签
    getTags(tags) {
      if (typeof tags === 'string') {
        return tags.split(',')
      }
      return []
    },

    // 处理图片加载错误
    handleImageError(event) {
      event.target.src = 'https://picsum.photos/800/400?random=1'
    },

    // 处理头像加载错误
    handleAvatarError(event) {
      event.target.src = 'https://picsum.photos/100/100?random=1'
    },

    // 开始学习
    handleStartLearning() {
      console.log('开始学习课程:', this.course)
      // 这里可以实现学习逻辑
      alert('开始学习课程！')
    },

    // 购买课程
    handlePurchaseCourse() {
      console.log('购买课程:', this.course)
      // 这里可以实现支付逻辑
      alert(`准备购买课程：${this.course.title}`)
    },

    // 检查当前课程是否已收藏
    async checkFavoriteStatus() {
      let userId = null;
      try {
        const res = await axios.get('http://localhost:8080/api/user/me', { withCredentials: true });
        userId = res.data.userId;
      } catch {
        localStorage.removeItem('userId');
        this.$router.push('/login');
        return;
      }
      const courseId = this.$route.params.id;
      if (!userId || !courseId) return;
      try {
        this.isFavorited = await favoriteService.isFavorited(userId, courseId)
      } catch (e) {
        this.isFavorited = false
      }
    },

    // 收藏/取消收藏
    async handleToggleFavorite() {
      let userId = null;
      try {
        const res = await axios.get('http://localhost:8080/api/user/me', { withCredentials: true });
        userId = res.data.userId;
      } catch {
        localStorage.removeItem('userId');
        this.$router.push('/login');
        return;
      }
      const courseId = this.course.articleId;
      // 日志输出，方便排查
      console.log('[收藏操作] userId:', userId, 'courseId:', courseId);
      if (!userId || !courseId) return;
      if (this.isFavorited) {
        try {
          await favoriteService.removeFavorite(userId, courseId)
          this.isFavorited = false
          this.heartAnimate = true
          this.$toast?.success?.('已取消收藏')
        } catch (e) {
          this.$toast?.error?.('取消收藏失败')
        }
      } else {
        try {
          await favoriteService.addFavorite(userId, courseId)
          this.isFavorited = true
          this.heartAnimate = true
          this.$toast?.success?.('收藏成功')
        } catch (e) {
          this.$toast?.error?.('收藏失败')
        }
      }
      await this.loadFavoriteCount()
      setTimeout(() => { this.heartAnimate = false }, 400)
    },

    // 查看课程
    handleViewCourse(course) {
      this.$router.push(`/course/${course.articleId}`)
    },

    // 选择课时
    handleLessonSelected(lesson) {
      console.log('选择课时:', lesson)
      // 这里可以实现课时选择逻辑
    },

    // 开始学习课时
    handleStartLesson(lesson) {
      console.log('开始学习课时:', lesson)
      // 这里可以实现课时学习逻辑
      alert(`开始学习：${lesson.title}`)
    },

    // 复习课时
    handleReviewLesson(lesson) {
      console.log('复习课时:', lesson)
      // 这里可以实现课时复习逻辑
      alert(`复习：${lesson.title}`)
    },

    // 评论添加
    handleCommentAdded(comment) {
      console.log('评论已添加:', comment)
      // 这里可以更新课程评分等
    },

    // 评论删除
    handleCommentDeleted(commentId) {
      console.log('评论已删除:', commentId)
      // 这里可以更新相关数据
    },

    async loadFavoriteCount() {
      if (!this.course || !this.course.articleId) return
      try {
        this.favoriteCount = await favoriteService.getFavoriteCountByCourse(this.course.articleId)
      } catch (e) {
        this.favoriteCount = null
      }
    }
  }
}
</script>

<style scoped>
.accordion-button:not(.collapsed) {
  background-color: #e7f1ff;
  color: #0c63e4;
}

.accordion-button:focus {
  box-shadow: none;
  border-color: rgba(0, 0, 0, 0.125);
}

.heart-animate {
  animation: heartBeat 0.4s;
}
@keyframes heartBeat {
  0% { transform: scale(1);}
  30% { transform: scale(1.3);}
  60% { transform: scale(0.9);}
  100% { transform: scale(1);}
}
</style> 