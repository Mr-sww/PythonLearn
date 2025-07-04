<template>
  <div class="comment-section">
    <!-- 评论统计 -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h5 class="fw-bold text-dark mb-0">
        评论 ({{ comments.length }})
      </h5>
      <div class="d-flex align-items-center">
        <div class="me-3">
          <span class="text-warning">
            <i class="fa fa-star" v-for="i in 5" :key="i"></i>
          </span>
          <span class="ms-1 text-muted">{{ averageRating.toFixed(1) }}</span>
        </div>
        <button 
          @click="showCommentForm = true"
          class="btn btn-primary btn-sm"
        >
          <i class="fa fa-comment me-1"></i>写评论
        </button>
      </div>
    </div>

    <!-- 评论表单 -->
    <div v-if="showCommentForm" class="bg-light rounded-3 p-4 mb-4">
      <div class="d-flex align-items-center mb-3">
        <img 
          :src="currentUser?.avatar || 'https://picsum.photos/100/100?random=1'" 
          :alt="currentUser?.nickname || '用户'"
          class="rounded-circle me-3"
          width="40"
          height="40"
        >
        <div>
          <div class="fw-bold">{{ currentUser?.nickname || '匿名用户' }}</div>
          <div class="text-muted small">发表评论</div>
        </div>
      </div>
      
      <!-- 评分 -->
      <div class="mb-3">
        <label class="form-label">评分</label>
        <div class="rating-stars">
          <i 
            v-for="i in 5" 
            :key="i"
            :class="['fa fa-star rating-star', i <= newComment.rating ? 'text-warning' : 'text-muted']"
            @click="newComment.rating = i"
            @mouseenter="hoverRating = i"
            @mouseleave="hoverRating = 0"
          ></i>
        </div>
      </div>
      
      <!-- 评论内容 -->
      <div class="mb-3">
        <textarea 
          v-model="newComment.content"
          class="form-control"
          rows="4"
          placeholder="分享您的学习心得..."
          maxlength="500"
        ></textarea>
        <div class="text-muted small mt-1">
          {{ newComment.content.length }}/500
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="d-flex gap-2">
        <button 
          @click="submitComment"
          :disabled="!newComment.content.trim() || submitting"
          class="btn btn-primary"
        >
          <span v-if="submitting" class="spinner-border spinner-border-sm me-1"></span>
          {{ submitting ? '提交中...' : '发表评论' }}
        </button>
        <button 
          @click="cancelComment"
          class="btn btn-outline-secondary"
        >
          取消
        </button>
      </div>
    </div>

    <!-- 评论列表 -->
    <div v-if="comments.length > 0" class="comment-list">
      <div 
        v-for="comment in comments" 
        :key="comment.commentId"
        class="comment-item border-bottom pb-4 mb-4"
      >
        <!-- 主评论 -->
        <div class="d-flex">
          <img 
            :src="comment.userAvatar || 'https://picsum.photos/100/100?random=1'" 
            :alt="comment.userName || '用户'"
            class="rounded-circle me-3"
            width="48"
            height="48"
          >
          <div class="flex-grow-1">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div>
                <div class="fw-bold">{{ comment.userName || '匿名用户' }}</div>
                <div class="text-muted small">
                  {{ formatDate(comment.createdAt) }}
                </div>
              </div>
              <div class="text-warning">
                <i 
                  v-for="i in 5" 
                  :key="i"
                  :class="['fa fa-star', i <= comment.rating ? 'text-warning' : 'text-muted']"
                ></i>
              </div>
            </div>
            
            <div class="comment-content mb-3">
              {{ comment.content }}
            </div>
            
            <div class="d-flex align-items-center gap-3">
              <button 
                @click="likeComment(comment.commentId)"
                :class="['btn btn-sm', comment.liked ? 'btn-primary' : 'btn-outline-primary']"
              >
                <i class="fa fa-thumbs-up me-1"></i>
                {{ comment.likes || 0 }}
              </button>
              <button 
                @click="showReplyForm(comment.commentId)"
                class="btn btn-sm btn-outline-secondary"
              >
                <i class="fa fa-reply me-1"></i>回复
              </button>
              <button 
                v-if="canDeleteComment(comment)"
                @click="deleteComment(comment.commentId)"
                class="btn btn-sm btn-outline-danger"
              >
                <i class="fa fa-trash me-1"></i>删除
              </button>
            </div>
            
            <!-- 回复表单 -->
            <div v-if="replyingTo === comment.commentId" class="mt-3">
              <div class="d-flex">
                <img 
                  :src="currentUser?.avatar || 'https://picsum.photos/100/100?random=1'" 
                  :alt="currentUser?.nickname || '用户'"
                  class="rounded-circle me-2"
                  width="32"
                  height="32"
                >
                <div class="flex-grow-1">
                  <textarea 
                    v-model="replyContent"
                    class="form-control form-control-sm"
                    rows="2"
                    placeholder="回复评论..."
                    maxlength="200"
                  ></textarea>
                  <div class="d-flex gap-2 mt-2">
                    <button 
                      @click="submitReply(comment.commentId)"
                      :disabled="!replyContent.trim()"
                      class="btn btn-primary btn-sm"
                    >
                      回复
                    </button>
                    <button 
                      @click="cancelReply"
                      class="btn btn-outline-secondary btn-sm"
                    >
                      取消
                    </button>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 回复列表 -->
            <div v-if="comment.replies && comment.replies.length > 0" class="replies mt-3">
              <div 
                v-for="reply in comment.replies" 
                :key="reply.commentId"
                class="reply-item bg-light rounded-3 p-3 mb-2"
              >
                <div class="d-flex">
                  <img 
                    :src="reply.userAvatar || 'https://picsum.photos/100/100?random=1'" 
                    :alt="reply.userName || '用户'"
                    class="rounded-circle me-2"
                    width="32"
                    height="32"
                  >
                  <div class="flex-grow-1">
                    <div class="d-flex justify-content-between align-items-start mb-1">
                      <div>
                        <div class="fw-bold small">{{ reply.userName || '匿名用户' }}</div>
                        <div class="text-muted small">{{ formatDate(reply.createdAt) }}</div>
                      </div>
                      <button 
                        v-if="canDeleteComment(reply)"
                        @click="deleteComment(reply.commentId)"
                        class="btn btn-sm btn-outline-danger"
                      >
                        <i class="fa fa-trash"></i>
                      </button>
                    </div>
                    <div class="reply-content">
                      {{ reply.content }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="text-center py-5">
      <i class="fa fa-comments fa-3x text-muted mb-3"></i>
      <h6 class="text-muted">暂无评论</h6>
      <p class="text-muted">成为第一个评论的人吧！</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CommentList',
  props: {
    courseId: {
      type: Number,
      required: true
    },
    currentUser: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      comments: [],
      loading: false,
      showCommentForm: false,
      replyingTo: null,
      replyContent: '',
      submitting: false,
      newComment: {
        content: '',
        rating: 5
      },
      hoverRating: 0
    }
  },
  computed: {
    averageRating() {
      if (this.comments.length === 0) return 0
      const totalRating = this.comments.reduce((sum, comment) => sum + comment.rating, 0)
      return totalRating / this.comments.length
    }
  },
  mounted() {
    this.loadComments()
  },
  methods: {
    // 加载评论
    async loadComments() {
      this.loading = true
      try {
        const response = await axios.get(`/api/comments/course/${this.courseId}`)
        this.comments = response.data
      } catch (error) {
        console.error('加载评论失败:', error)
        // 使用模拟数据
        this.loadMockComments()
      } finally {
        this.loading = false
      }
    },

    // 加载模拟评论数据
    loadMockComments() {
      this.comments = [
        {
          commentId: 1,
          courseId: this.courseId,
          userId: 1,
          content: '这个课程非常棒！老师讲解得很清楚，内容也很实用。',
          rating: 5,
          likes: 12,
          createdAt: new Date(Date.now() - 86400000),
          userName: '张三',
          userAvatar: 'https://picsum.photos/100/100?random=1',
          replies: [
            {
              commentId: 2,
              courseId: this.courseId,
              userId: 2,
              content: '同意！我也觉得这个课程很好。',
              rating: 5,
              likes: 3,
              createdAt: new Date(Date.now() - 43200000),
              userName: '李四',
              userAvatar: 'https://picsum.photos/100/100?random=2'
            }
          ]
        },
        {
          commentId: 3,
          courseId: this.courseId,
          userId: 3,
          content: '课程内容很全面，适合初学者。',
          rating: 4,
          likes: 8,
          createdAt: new Date(Date.now() - 172800000),
          userName: '王五',
          userAvatar: 'https://picsum.photos/100/100?random=3',
          replies: []
        }
      ]
    },

    // 提交评论
    async submitComment() {
      if (!this.newComment.content.trim()) return

      this.submitting = true
      try {
        const commentData = {
          courseId: this.courseId,
          userId: this.currentUser?.userId || 1,
          content: this.newComment.content,
          rating: this.newComment.rating
        }

        const response = await axios.post('/api/comments', commentData)
        this.comments.unshift(response.data)
        this.resetCommentForm()
        this.$emit('comment-added', response.data)
      } catch (error) {
        console.error('提交评论失败:', error)
        alert('评论提交失败，请重试')
      } finally {
        this.submitting = false
      }
    },

    // 提交回复
    async submitReply(parentId) {
      if (!this.replyContent.trim()) return

      try {
        const replyData = {
          courseId: this.courseId,
          userId: this.currentUser?.userId || 1,
          content: this.replyContent,
          parentId: parentId
        }

        const response = await axios.post('/api/comments', replyData)
        
        // 找到父评论并添加回复
        const parentComment = this.comments.find(c => c.commentId === parentId)
        if (parentComment) {
          if (!parentComment.replies) {
            parentComment.replies = []
          }
          parentComment.replies.push(response.data)
        }
        
        this.cancelReply()
      } catch (error) {
        console.error('提交回复失败:', error)
        alert('回复提交失败，请重试')
      }
    },

    // 点赞评论
    async likeComment(commentId) {
      try {
        await axios.post(`/api/comments/${commentId}/like`)
        const comment = this.findComment(commentId)
        if (comment) {
          comment.likes = (comment.likes || 0) + 1
          comment.liked = true
        }
      } catch (error) {
        console.error('点赞失败:', error)
      }
    },

    // 删除评论
    async deleteComment(commentId) {
      if (!confirm('确定要删除这条评论吗？')) return

      try {
        await axios.delete(`/api/comments/${commentId}`)
        this.removeComment(commentId)
        this.$emit('comment-deleted', commentId)
      } catch (error) {
        console.error('删除评论失败:', error)
        alert('删除失败，请重试')
      }
    },

    // 显示回复表单
    showReplyForm(commentId) {
      this.replyingTo = commentId
      this.replyContent = ''
    },

    // 取消回复
    cancelReply() {
      this.replyingTo = null
      this.replyContent = ''
    },

    // 取消评论
    cancelComment() {
      this.resetCommentForm()
    },

    // 重置评论表单
    resetCommentForm() {
      this.showCommentForm = false
      this.newComment = {
        content: '',
        rating: 5
      }
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const now = new Date()
      const diff = now - d
      
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
      if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
      if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`
      
      return d.toLocaleDateString('zh-CN')
    },

    // 查找评论
    findComment(commentId) {
      for (const comment of this.comments) {
        if (comment.commentId === commentId) return comment
        if (comment.replies) {
          const reply = comment.replies.find(r => r.commentId === commentId)
          if (reply) return reply
        }
      }
      return null
    },

    // 移除评论
    removeComment(commentId) {
      // 从主评论中移除
      const mainIndex = this.comments.findIndex(c => c.commentId === commentId)
      if (mainIndex !== -1) {
        this.comments.splice(mainIndex, 1)
        return
      }

      // 从回复中移除
      for (const comment of this.comments) {
        if (comment.replies) {
          const replyIndex = comment.replies.findIndex(r => r.commentId === commentId)
          if (replyIndex !== -1) {
            comment.replies.splice(replyIndex, 1)
            return
          }
        }
      }
    },

    // 检查是否可以删除评论
    canDeleteComment(comment) {
      return this.currentUser && comment.userId === this.currentUser.userId
    }
  }
}
</script>

<style scoped>
.rating-stars {
  display: flex;
  gap: 4px;
}

.rating-star {
  cursor: pointer;
  font-size: 1.2rem;
  transition: color 0.2s;
}

.rating-star:hover {
  color: #ffc107 !important;
}

.comment-content {
  line-height: 1.6;
}

.reply-content {
  line-height: 1.5;
  font-size: 0.9rem;
}

.replies {
  margin-left: 60px;
}

@media (max-width: 768px) {
  .replies {
    margin-left: 20px;
  }
}
</style>















