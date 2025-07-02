<template>
  <div class="comment-item">
    <div class="d-flex">
      <img :src="comment.avatar || 'https://picsum.photos/40/40?random=' + comment.userId" class="rounded-circle me-3" width="40" height="40" alt="用户头像">
      <div class="flex-grow-1">
        <div class="d-flex align-items-center mb-1">
          <span class="fw-medium text-dark">{{ comment.nickname || '匿名用户' }}</span>
          <span class="text-muted small ms-2">{{ comment.createdAt }}</span>
        </div>
        <div class="comment-content mb-2">{{ comment.content }}</div>
        <div class="comment-actions">
          <button class="btn btn-link btn-sm p-0 me-3" @click="$emit('like', comment.commentId)">
            <i class="fa fa-thumbs-up me-1"></i>{{ comment.likes }}
          </button>
          <button class="btn btn-link btn-sm p-0 me-3" @click="showReply = !showReply">回复</button>
        </div>
        <div v-if="showReply" class="mb-2">
          <textarea v-model="replyContent" class="form-control mb-1" placeholder="回复..."></textarea>
          <button class="btn btn-primary btn-sm" @click="doReply">发送</button>
        </div>
        <div class="children ms-4">
          <CommentItem
            v-for="child in comment.children"
            :key="child.commentId"
            :comment="child"
            @reply="$emit('reply', child.commentId, $event)"
            @like="$emit('like', child.commentId)"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'CommentItem',
  props: ['comment'],
  data() {
    return { showReply: false, replyContent: '' }
  },
  methods: {
    doReply() {
      if (!this.replyContent.trim()) return;
      this.$emit('reply', this.comment.commentId, this.replyContent);
      this.replyContent = '';
      this.showReply = false;
    }
  },
  components: {
    CommentItem: () => import('./CommentItem.vue')
  }
}
</script> 