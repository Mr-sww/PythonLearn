<template>
  <div class="learning-page">
    <!-- 左侧目录 -->
    <aside class="sidebar">
      <h3>知识点目录</h3>
      <div v-for="group in catalog" :key="group.id" class="catalog-group">
        <div class="group-header" :class="{active: selectedGroup === group.id}">
          <i class="fa" :class="group.status === '已完成' ? 'fa-check-circle text-success' : 'fa-dot-circle-o text-warning'"></i>
          <span class="group-title">{{ group.title }}</span>
        </div>
        <ul class="group-items" v-if="group.children && group.children.length">
          <li v-for="item in group.children" :key="item.id" @click="selectKnowledge(item)" :class="{active: selectedKnowledge && selectedKnowledge.id === item.id}">
            {{ item.title }}
          </li>
        </ul>
      </div>
    </aside>

    <!-- 中间内容 -->
    <main class="content">
      <div class="knowledge-section card">
        <h2 class="knowledge-title">{{ selectedKnowledge ? selectedKnowledge.title : '知识点' }}</h2>
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="selectedKnowledge">
          <div class="knowledge-content" v-html="renderedContent"></div>
        </div>
        <div v-else>
          <p class="text-muted">请选择左侧知识点</p>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="card comment-section" v-if="selectedKnowledge">
        <h3>评论</h3>
        <div class="comment-input">
          <img class="avatar" :src="currentUserAvatar" alt="avatar" />
          <div class="input-area">
            <textarea v-model="newComment" rows="3" class="form-control" placeholder="写下你的评论..."></textarea>
            <button class="send-btn btn btn-primary rounded-pill" :disabled="submitting || !newComment.trim()" @click="submitComment">
              <i class="fa fa-paper-plane"></i>
              <span class="ms-1">{{ submitting ? '发送中...' : '发表' }}</span>
            </button>
          </div>
        </div>
        <div class="comment-list" v-if="comments.length">
          <div class="comment-item" v-for="c in comments" :key="c.id">
            <img class="avatar" :src="c.avatar || defaultAvatar" alt="avatar" />
            <div class="comment-body">
              <div class="comment-header">
                <span class="author">{{ c.nickname || ('用户' + c.userId) }}</span>
                <span class="dot">·</span>
                <span class="time">{{ formatTime(c.createdAt) }}</span>
              </div>
              <div class="comment-content">{{ c.content }}</div>
              <div class="comment-actions">
                <button type="button" class="link-btn" @click="toggleLike(c); syncLike(c)">
                  <i class="fa" :class="c._liked ? 'fa-thumbs-up' : 'fa-thumbs-o-up'"></i>
                  <span class="ms-1">{{ c._likes }}</span>
                </button>
                <button type="button" class="link-btn"><i class="fa fa-reply"></i><span class="ms-1">回复</span></button>
                <button type="button" class="link-btn"><i class="fa fa-share"></i><span class="ms-1">分享</span></button>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-muted">暂无评论</div>
      </div>
    </main>

    <!-- 右侧悬浮题目列表 -->
    <aside class="right-panel" v-if="problems.length > 0">
      <div class="card sticky">
        <h3>题目列表</h3>
        <div class="problem-list-mini">
          <div class="problem-mini" v-for="prob in problems" :key="prob.id">
            <span class="title" :title="prob.title">{{ prob.title }}</span>
            <button class="go-btn" @click="goToProblem(prob.id)">去做题</button>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script>
import axios from 'axios';
import { learningRecordService } from '@/services/learningRecordService.js';
import { videoClickService } from '@/services/videoClickService.js';
import { checkLoginAndRedirect } from '@/utils/auth.js';

export default {
  name: 'LearnDetial',
  data() {
    return {
      catalog: [],
      selectedKnowledge: null,
      selectedGroup: null,
      problems: [],
      loading: false,
      // 评论
      comments: [],
      newComment: '',
      submitting: false,
      defaultAvatar: '/avatar/default.png',
      // 学习记录相关
      studyStartTime: null,
      studyTimer: null,
      isStudying: false
    }
  },
  computed: {
    currentUserAvatar() {
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      return user?.avatar || this.defaultAvatar;
    },
    renderedContent() {
      const raw = this.selectedKnowledge?.content || '';
      if (!raw) return '';
      let html = String(raw);
      // 还原常见转义符
      html = html
        .replace(/\\r\\n/g, '<br/>')
        .replace(/\\n/g, '<br/>')
        .replace(/\\r/g, '')
        .replace(/\\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
        .replace(/\\"/g, '"');
      return html;
    }
  },
  mounted() {
    // 检查登录状态
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    console.log('=== 页面加载时检查登录状态 ===');
    console.log('localStorage中的用户信息:', user);
    console.log('用户ID:', user?.userId);
    console.log('是否登录:', !!user?.userId);
    
    if (!user || !user.userId) {
      console.log('用户未登录，重定向到登录页面');
      this.$router.push('/login');
      return;
    }
    
    this.fetchCatalog();
    const title = this.$route.query.id;
    if (title) this.selectKnowledgeByTitle(title);
  },
  
  beforeUnmount() {
    // 清理定时器
    if (this.studyTimer) {
      clearInterval(this.studyTimer);
    }
    // 完成当前学习记录
    this.completeCurrentStudy();
  },
  watch: {
    '$route.query.id': {
      immediate: true,
      handler(newId) {
        if (newId) this.selectKnowledgeByTitle(newId);
      }
    }
  },
  methods: {
    async fetchCatalog() {
      this.loading = true;
      try {
        const res = await axios.get('/api/knowledge/catalog');
        this.catalog = res.data;
      } finally {
        this.loading = false;
      }
    },
    async selectKnowledge(item) {
      // 检查登录状态
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      console.log('当前用户信息:', user);
      console.log('用户ID:', user?.userId);
      console.log('是否登录:', !!user?.userId);
      
      if (!checkLoginAndRedirect(this.$router)) {
        console.log('用户未登录，已重定向到登录页面');
        return;
      }
      
      // 处理视频点击事件
      const isVideo = await videoClickService.handleKnowledgeClick(item);
      console.log('知识点类型:', isVideo ? '视频' : '文字');
      
      // 完成之前的学习记录
      await this.completeCurrentStudy();
      
      this.loading = true;
      try {
        const res = await axios.get(`/api/knowledge/${item.id}/detail`);
        this.selectedKnowledge = res.data.knowledge;
        this.problems = Array.isArray(res.data.problems) ? res.data.problems : [];
        // 组标记
        this.selectedGroup = this.catalog.find(g => g.children && g.children.some(c => c.id === item.id))?.id;
        // 拉取评论
        await this.fetchComments();
        
        // 开始新的学习记录
        await this.startStudyRecord(item);
      } catch (e) {
        console.error('获取知识点详情失败:', e);
        this.selectedKnowledge = { id: item.id, title: item.title, content: '内容加载失败' };
        this.problems = [];
        await this.fetchComments();
      } finally {
        this.loading = false;
      }
    },
    async fetchComments() {
      if (!this.selectedKnowledge?.id) { this.comments = []; return; }
      try {
        const { data } = await axios.get(`/api/knowledge/${this.selectedKnowledge.id}/comments`, { params: { page: 1, pageSize: 20 }});
        const list = Array.isArray(data) ? data : [];
        // 附加本地计数与点赞状态
        this.comments = list.map(it => ({
          ...it,
          _likes: typeof it.likes === 'number' ? it.likes : 0,
          _liked: false
        }));
      } catch (e) {
        this.comments = [];
      }
    },
    async submitComment() {
      if (!this.newComment.trim() || !this.selectedKnowledge?.id) return;
      this.submitting = true;
      try {
        const user = JSON.parse(localStorage.getItem('user') || 'null');
        const payload = { content: this.newComment.trim(), userId: user?.userId || user?.user_id || 0, nickname: user?.nickname || '' };
        await axios.post(`/api/knowledge/${this.selectedKnowledge.id}/comments`, payload);
        this.newComment = '';
        await this.fetchComments();
      } finally {
        this.submitting = false;
      }
    },
    async selectKnowledgeByTitle(title) {
      if (!this.catalog.length) await this.fetchCatalog();
      for (const group of this.catalog) {
        if (group.children) {
          const item = group.children.find(k => k.title === title);
          if (item) { await this.selectKnowledge(item); return; }
        }
      }
    },
    goToProblem(problemId) { this.$router.push(`/problem/${problemId}`); },
    formatTime(ts) { return ts ? new Date(ts).toLocaleString() : ''; },
    toggleLike(c) {
      if (!c) return;
      c._liked = !c._liked;
      c._likes = (c._likes || 0) + (c._liked ? 1 : -1);
    },
    async syncLike(c) {
      try {
        const delta = c._liked ? 1 : -1;
        await axios.post(`/api/knowledge/comments/${c.id}/likes`, null, { params: { delta } });
      } catch (e) {
        // 失败回滚
        c._liked = !c._liked;
        c._likes = (c._likes || 0) + (c._liked ? 1 : -1);
      }
    },
    
    // 学习记录相关方法
    async startStudyRecord(knowledgeItem) {
      try {
        // 开始学习记录
        await learningRecordService.startKnowledgeStudy(
          knowledgeItem.id,
          knowledgeItem.title
        );
        
        this.isStudying = true;
        this.studyStartTime = new Date();
        
        // 启动定时器，每30秒更新一次学习进度
        this.studyTimer = setInterval(() => {
          this.updateStudyProgress();
        }, 30000);
        
        console.log('开始学习记录:', knowledgeItem.title);
      } catch (error) {
        console.error('开始学习记录失败:', error);
      }
    },
    
    async updateStudyProgress() {
      if (!this.isStudying || !this.studyStartTime || !this.selectedKnowledge) {
        return;
      }
      
      try {
        const studyTime = Math.floor((new Date() - this.studyStartTime) / 1000);
        const progress = Math.min(studyTime / 60, 100); // 假设60秒为100%进度
        
        await learningRecordService.updateKnowledgeProgress(
          this.selectedKnowledge.id,
          studyTime,
          progress
        );
        
        console.log('更新学习进度:', progress.toFixed(1) + '%');
      } catch (error) {
        console.error('更新学习进度失败:', error);
      }
    },
    
    async completeCurrentStudy() {
      if (!this.isStudying || !this.selectedKnowledge) {
        return;
      }
      
      try {
        // 停止定时器
        if (this.studyTimer) {
          clearInterval(this.studyTimer);
          this.studyTimer = null;
        }
        
        // 计算学习时长
        const studyTime = this.studyStartTime ? 
          Math.floor((new Date() - this.studyStartTime) / 1000) : 0;
        
        // 完成学习记录
        await learningRecordService.completeKnowledgeStudy(this.selectedKnowledge.id);
        
        console.log('完成学习记录:', this.selectedKnowledge.title, '学习时长:', studyTime + '秒');
        
        // 重置状态
        this.isStudying = false;
        this.studyStartTime = null;
      } catch (error) {
        console.error('完成学习记录失败:', error);
      }
    }
  }
}
</script>

<style scoped>
.learning-page { display: grid; grid-template-columns: minmax(220px, 240px) 1fr minmax(220px, 280px); gap: 24px; align-items: start; max-width: 1440px; margin: 0 auto; padding: 0 24px; width: 100%; box-sizing: border-box; }
@media (max-width: 1280px) { .learning-page { grid-template-columns: 260px 1fr; } .right-panel { display: none; } }

.sidebar { background: #fff; border-radius: 14px; box-shadow: 0 2px 16px rgba(37,99,235,0.06); padding: 18px; }
.content { display: flex; flex-direction: column; gap: 20px; }
.right-panel .sticky { position: sticky; top: 80px; }

.card { background: #fff; border-radius: 14px; box-shadow: 0 2px 16px rgba(37,99,235,0.06); padding: 24px; }
.knowledge-title { font-size: 22px; font-weight: 700; color: #2563eb; margin-bottom: 12px; }
.knowledge-content { background:#f8fafd; border-radius:8px; padding:16px; line-height:1.75; overflow-wrap:anywhere; word-break: break-word; white-space: pre-wrap; }

.problem-list-mini { display: flex; flex-direction: column; gap: 10px; }
.problem-mini { display:flex; align-items:center; justify-content: space-between; background:#f8fafd; border-radius:10px; padding:10px 12px; }
.problem-mini .title { flex:1; margin-right:10px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; color:#2563eb; font-weight:600; }
.go-btn { background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%); color:#fff; border:none; border-radius:20px; padding:6px 14px; cursor:pointer; }

/* 目录样式（沿用原来的） */
.catalog-group { margin-bottom: 16px; }
.group-header { display:flex; align-items:center; gap:8px; padding:8px 6px; border-radius:8px; background:#f8fafc; border:1px solid #e2e8f0; font-weight:600; }
.group-items { list-style: none; padding:0; margin:8px 0 0 0; }
.group-items li { padding:8px 10px; border-radius:6px; margin-bottom:4px; cursor:pointer; }
.group-items li:hover { background:#e3eafe; color:#2563eb; }
.group-items li.active { background:linear-gradient(90deg, #2563eb 0%, #60a5fa 100%); color:#fff; }

/* 评论区 */
.comment-section h3 { margin-bottom: 10px; }
.comment-input { display:flex; gap:12px; align-items:flex-start; }
.comment-input .avatar { width:40px; height:40px; border-radius:50%; object-fit:cover; }
.comment-input .input-area { position: relative; flex:1; }
.comment-input textarea { width:100%; resize: vertical; padding-right: 120px; }
.comment-input .send-btn { position: absolute; right: 12px; bottom: 12px; display: inline-flex; align-items: center; gap: 6px; padding: 8px 14px; }
.comment-item { display:flex; gap:12px; padding:14px 0; border-bottom:1px solid #eef2f7; }
.comment-item .avatar { width:40px; height:40px; border-radius:50%; object-fit:cover; }
.comment-body { flex:1; }
.comment-header { font-size:14px; color:#374151; margin-bottom:6px; display:flex; align-items:center; gap:6px; }
.comment-header .author { font-weight:600; color:#111827; }
.comment-header .dot { color:#9ca3af; }
.comment-content { font-size:14px; color:#111827; white-space: pre-wrap; }
.comment-actions { margin-top:6px; display:flex; gap:16px; align-items:center; color:#9ca3af; }
.link-btn { background:transparent; border:none; color:#9ca3af; cursor:pointer; padding:0; display:flex; align-items:center; gap:6px; }
.link-btn:hover { color:#2563eb; }
</style> 