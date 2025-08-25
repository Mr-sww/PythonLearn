<template>
  <div class="page">
    <div class="header">
      <h2 class="title">知识点管理</h2>
      <div class="actions">
        <input v-model.trim="q" @input="onQueryChange" class="input" type="text" placeholder="搜索知识点 关键词..." />
        <button class="btn btn-secondary" @click="refresh"><i class="fas fa-sync"></i> 刷新</button>
        <button class="btn btn-primary" @click="create"><i class="fas fa-plus"></i> 新建知识点</button>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="paged.length===0" class="empty">暂无数据</div>

    <div v-else class="grid">
      <div v-for="k in paged" :key="k.id" class="card">
        <div class="card-title">{{ k.title }}</div>
        <div class="card-meta">
          <span class="tag">ID: {{ k.id }}</span>
          <span class="tag" v-if="k.stage">阶段: {{ k.stage }}</span>
        </div>
        <div class="card-content">{{ k.content || '暂无内容' }}</div>
        <div class="card-actions">
          <button class="btn btn-sm" @click="openView(k)"><i class="fas fa-eye"></i> 查看</button>
          <button class="btn btn-sm btn-secondary" @click="openEdit(k)"><i class="fas fa-edit"></i> 编辑</button>
          <button class="btn btn-sm btn-danger" @click="remove(k)"><i class="fas fa-trash"></i> 删除</button>
        </div>
      </div>
    </div>

    <div v-if="pages>1" class="pagination">
      <button class="btn btn-sm" :disabled="page===1" @click="go(page-1)">上一页</button>
      <span class="page-info">{{ page }} / {{ pages }}</span>
      <button class="btn btn-sm" :disabled="page===pages" @click="go(page+1)">下一页</button>
      <select class="page-size" v-model.number="pageSize" @change="page=1">
        <option :value="10">10/页</option>
        <option :value="20">20/页</option>
        <option :value="50">50/页</option>
      </select>
    </div>

    <!-- 查看模态框 -->
    <div v-if="showViewModal" class="modal-overlay" @click="showViewModal=false">
      <div class="modal-content large" @click.stop>
        <div class="modal-header">
          <h3>查看知识点</h3>
          <button class="modal-close" @click="showViewModal=false">×</button>
        </div>
        <div class="modal-body">
          <div class="knowledge-header">
            <div class="knowledge-title">{{ selected?.title }}</div>
            <div class="knowledge-meta">
              <span class="badge">ID: {{ selected?.id }}</span>
              <span class="badge stage" v-if="selected?.stage">阶段: {{ selected?.stage }}</span>
            </div>
          </div>
          
          <div class="knowledge-section">
            <h4>知识点内容</h4>
            <div class="content-box">
              <div v-if="selected?.content" class="content-text">{{ selected?.content }}</div>
              <div v-else class="empty-content">暂无内容</div>
            </div>
          </div>
          
          <div class="knowledge-section" v-if="selected?.question">
            <h4>关联题目</h4>
            <div class="question-list">
              <div v-for="(qid, index) in questionIds" :key="index" class="question-item">
                <div class="question-info">
                  <span class="question-id">{{ qid }}</span>
                  <span class="question-title">{{ getQuestionTitle(qid) }}</span>
                </div>
                <div class="question-actions">
                  <span class="question-link" @click="viewProblem(qid)">查看题目</span>
                  <span class="question-link" @click="editProblem(qid)">编辑题目</span>
                </div>
              </div>
            </div>
          </div>
          
          <div class="knowledge-section" v-if="selected?.url">
            <h4>相关链接</h4>
            <div class="url-box">
              <a :href="selected?.url" target="_blank" class="url-link">{{ selected?.url }}</a>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="openEdit(selected)">编辑</button>
          <button class="btn" @click="showViewModal=false">关闭</button>
        </div>
      </div>
    </div>

    <!-- 编辑模态框 -->
    <div v-if="showEditModal" class="modal-overlay" @click="showEditModal=false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑知识点' : '新建知识点' }}</h3>
          <button class="modal-close" @click="showEditModal=false">×</button>
        </div>
        <div class="modal-body form">
          <label>标题</label>
          <input class="input" v-model.trim="editForm.title" placeholder="请输入标题" />
          <label>阶段</label>
          <input class="input" v-model.trim="editForm.stage" placeholder="如: 1.1, 2.1" />
          <label>内容</label>
          <textarea class="textarea" rows="4" v-model.trim="editForm.content" placeholder="请输入知识点内容"></textarea>
          <label>关联题目</label>
          <div class="question-tags-input">
            <div v-for="qid in editFormQuestionIds" :key="qid" class="question-tag">
              <span>{{ getQuestionTitle(qid) }} (ID: {{ qid }})</span>
              <button type="button" class="remove-tag-btn" @click="removeQuestion(qid)">×</button>
            </div>
            <input
              type="text"
              v-model="questionSearchQuery"
              @input="searchProblems"
              @keydown.enter.prevent="addFirstSearchResult"
              placeholder="搜索并添加题目 (ID 或 标题)"
              class="input search-input"
            />
            <div v-if="questionSearchResults.length > 0 && questionSearchQuery" class="search-results">
              <div
                v-for="p in questionSearchResults"
                :key="p.id"
                class="search-result-item"
                @click="addQuestion(p.id)"
              >
                {{ p.title }} (ID: {{ p.id }})
              </div>
            </div>
          </div>
          <label>相关链接</label>
          <input class="input" v-model.trim="editForm.url" placeholder="请输入相关链接" />
        </div>
        <div class="modal-footer">
          <button class="btn" @click="showEditModal=false">取消</button>
          <button class="btn btn-primary" @click="saveEdit">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const http = axios.create({ baseURL: 'http://localhost:8080/api', withCredentials: true })
export default {
  name: 'AdminKnowledges',
  data(){return{
    q:'',
    list:[],
    loading:false,
    page:1,
    pageSize:20,
    debounceTimer:null,
    showViewModal:false,
    showEditModal:false,
    selected:null,
    isEdit:false,
    questionTitles: {}, // 缓存题目标题
    questionSearchQuery: '', // 关联题目搜索框的查询
    questionSearchResults: [], // 关联题目搜索结果
    problemListCache: [], // 缓存所有题目，用于搜索
    editForm:{
      title:'',
      stage:'',
      content:'',
      question:'',
      url:''
    }
  }},
  async mounted(){ 
    await this.refresh() 
    await this.loadQuestionTitles()
  },
  computed:{
    pages(){ return Math.max(1, Math.ceil(this.list.length / this.pageSize)) },
    paged(){ const s=(this.page-1)*this.pageSize; return this.list.slice(s, s+this.pageSize) },
    questionIds(){
      if(!this.selected?.question) return []
      return this.selected.question.split(/[,，\s]+/).filter(id => id.trim())
    },
    editFormQuestionIds() {
      if (!this.editForm.question) return []
      return this.editForm.question.split(/[,，\s]+/).filter(id => id.trim())
    }
  },
  methods:{
    async refresh(){
      this.loading = true
      try{
        const res = await http.get('/knowledge/points')
        this.list = Array.isArray(res.data) ? res.data : []
      } finally { this.loading = false }
    },
    create(){
      this.isEdit = false
      this.editForm = { title:'', stage:'', content:'', question:'', url:'' }
      this.questionSearchQuery = ''
      this.questionSearchResults = []
      this.showEditModal = true
    },
    onQueryChange(){
      clearTimeout(this.debounceTimer)
      this.debounceTimer = setTimeout(()=>{
        this.page=1
        this.refresh()
      },300)
    },
    openView(k){ this.selected = k; this.showViewModal = true },
    openEdit(k){
      this.isEdit = true
      this.selected = k
      this.editForm = {
        title: k.title || '',
        stage: k.stage || '',
        content: k.content || '',
        question: k.question || '',
        url: k.url || ''
      }
      this.questionSearchQuery = ''
      this.questionSearchResults = []
      this.showEditModal = true
    },
    async saveEdit(){
      if(!this.editForm.title.trim()) {
        alert('请输入标题')
        return
      }
      
      try{
        if(this.isEdit && this.selected) {
          // 编辑现有知识点
          await http.put(`/knowledge/points/${this.selected.id}`, this.editForm)
          Object.assign(this.selected, this.editForm)
        } else {
          // 创建新知识点
          const res = await http.post('/knowledge/points', this.editForm)
          this.list.unshift(res.data)
        }
        this.showEditModal = false
        alert(this.isEdit ? '更新成功' : '创建成功')
      }catch(e){
        alert(this.isEdit ? '更新失败' : '创建失败')
      }
    },
    async remove(k){
      if(!window.confirm(`确定删除知识点 ${k.title} 吗？`)) return
      try{
        await http.delete(`/knowledge/points/${k.id}`)
        this.list = this.list.filter(x=>x.id!==k.id)
        alert('删除成功')
      }catch(e){
        alert('删除失败')
      }
    },
    go(p){ if(p>=1 && p<=this.pages) this.page=p },
    viewProblem(qid){
      // 跳转到题目详情页面
      this.$router.push(`/problem/${qid}`)
    },
    editProblem(qid){
      // 跳转到题库管理页面并打开编辑模态框
      this.$router.push(`/admin/problems?edit=${qid}`)
    },
    getQuestionTitle(qid){
      // 从缓存中获取题目标题，如果没有则显示ID
      return this.questionTitles[qid] || qid
    },
    async loadQuestionTitles(){
      // 加载所有题目的标题
      try {
        const res = await http.get('/python-problems')
        const problems = Array.isArray(res.data) ? res.data : []
        this.problemListCache = problems // 缓存所有题目
        const titles = {}
        problems.forEach(p => {
          titles[p.id] = p.title || `无标题题目 (ID: ${p.id})`
        })
        this.questionTitles = titles
      } catch (e) {
        console.error('加载题目标题失败:', e)
      }
    },
    searchProblems(){
      const query = this.questionSearchQuery.toLowerCase()
      if (!query) {
        this.questionSearchResults = []
        return
      }
      this.questionSearchResults = this.problemListCache.filter(p =>
        p.id.toString().includes(query) || p.title.toLowerCase().includes(query)
      ).slice(0, 10) // 只显示前10个结果
    },
    addQuestion(qid){
      const currentIds = this.editFormQuestionIds
      if (!currentIds.includes(qid.toString())) {
        currentIds.push(qid.toString())
        this.editForm.question = currentIds.join(',')
      }
      this.questionSearchQuery = ''
      this.questionSearchResults = []
    },
    removeQuestion(qid){
      const currentIds = this.editFormQuestionIds.filter(id => id !== qid.toString())
      this.editForm.question = currentIds.join(',')
    },
    addFirstSearchResult(){
      if (this.questionSearchResults.length > 0) {
        this.addQuestion(this.questionSearchResults[0].id)
      }
    }
  }
}
</script>

<style scoped>
.page{ padding:20px }
.header{ display:flex; justify-content:space-between; align-items:center; margin-bottom:16px }
.title{ font-weight:700; font-size:20px }
.actions{ display:flex; gap:10px; align-items:center }
.input{ padding:8px 12px; border:1px solid #e5e7eb; border-radius:8px; width:260px }
.btn{ padding:8px 12px; border:1px solid transparent; border-radius:8px; background:#f3f4f6; cursor:pointer }
.btn:hover{ filter:brightness(0.98) }
.btn-primary{ background:#2563eb; color:#fff }
.btn-secondary{ background:#10b981; color:#fff }
.btn-danger{ background:#ef4444; color:#fff }
.btn-sm{ padding:6px 10px; font-size:12px }
.loading{ color:#64748b }
.empty{ color:#94a3b8 }
.grid{ display:grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap:12px }
.card{ background:#fff; border-radius:12px; box-shadow:0 2px 8px rgba(0,0,0,.06); padding:14px; display:flex; flex-direction:column; gap:10px }
.card-title{ font-weight:600; color:#0f172a }
.card-meta{ display:flex; gap:8px; color:#64748b; font-size:12px }
.card-content{ color:#475569; font-size:14px; line-height:1.5; max-height:60px; overflow:hidden; text-overflow:ellipsis; display:-webkit-box; -webkit-line-clamp:3; -webkit-box-orient:vertical }
.tag{ background:#f1f5f9; padding:2px 8px; border-radius:999px }
.card-actions{ display:flex; gap:8px }
.pagination{ display:flex; align-items:center; gap:10px; justify-content:flex-end; margin-top:16px }
.page-info{ color:#64748b }
.page-size{ padding:6px 8px; border:1px solid #e5e7eb; border-radius:6px }
/* modal */
.modal-overlay{ position:fixed; inset:0; background:rgba(0,0,0,.45); display:flex; align-items:center; justify-content:center; z-index:1000 }
.modal-content{ background:#fff; border-radius:12px; width:90%; max-width:520px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,.2) }
.modal-content.large{ max-width:700px; max-height:80vh }
.modal-header{ display:flex; align-items:center; justify-content:space-between; padding:14px 16px; border-bottom:1px solid #eee }
.modal-body{ padding:16px; overflow-y:auto; max-height:calc(80vh - 120px) }
.modal-footer{ padding:12px 16px; border-top:1px solid #eee; display:flex; justify-content:flex-end; gap:8px }
.modal-close{ background:none; border:none; font-size:20px; cursor:pointer }
.kv{ display:flex; gap:12px; padding:6px 0 }
.kv .k{ width:64px; color:#64748b }
.kv .v{ flex:1; color:#0f172a }
.kv .v.multiline{ white-space:pre-wrap; line-height:1.6 }
.form label{ display:block; margin:8px 0 6px; color:#334155; font-size:14px }

/* 知识点查看样式 */
.knowledge-header{ margin-bottom:20px; padding-bottom:16px; border-bottom:1px solid #e5e7eb }
.knowledge-title{ font-size:24px; font-weight:700; color:#0f172a; margin-bottom:8px }
.knowledge-meta{ display:flex; gap:8px }
.badge{ background:#f1f5f9; color:#64748b; padding:4px 8px; border-radius:6px; font-size:12px }
.badge.stage{ background:#dbeafe; color:#1d4ed8 }
.knowledge-section{ margin-bottom:20px }
.knowledge-section h4{ font-size:16px; font-weight:600; color:#334155; margin-bottom:8px }
.content-box{ background:#f8fafc; border:1px solid #e5e7eb; border-radius:8px; padding:12px }
.content-text{ white-space:pre-wrap; line-height:1.6; color:#475569 }
.empty-content{ color:#94a3b8; font-style:italic }
.question-list{ display:flex; flex-direction:column; gap:8px }
.question-item{ display:flex; justify-content:space-between; align-items:center; background:#f8fafc; border:1px solid #e5e7eb; border-radius:6px; padding:8px 12px }
.question-info{ display:flex; flex-direction:column; gap:4px }
.question-actions{ display:flex; gap:12px }
.question-id{ font-family:monospace; color:#64748b; font-size:12px }
.question-title{ color:#334155; font-weight:500 }
.question-link{ color:#2563eb; cursor:pointer; text-decoration:underline }
.question-link:hover{ color:#1d4ed8 }
.url-box{ background:#f8fafc; border:1px solid #e5e7eb; border-radius:6px; padding:8px 12px }
.url-link{ color:#2563eb; text-decoration:none; word-break:break-all }
.url-link:hover{ text-decoration:underline }
.textarea{ width:100%; padding:8px 12px; border:1px solid #e5e7eb; border-radius:8px; font-family:inherit }

/* 关联题目编辑样式 */
.question-tags-input {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 40px;
  align-items: center;
  position: relative;
}

.question-tag {
  background-color: #e0f2fe;
  color: #0c4a6e;
  padding: 6px 10px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
}

.remove-tag-btn {
  background: none;
  border: none;
  color: #0c4a6e;
  font-size: 16px;
  cursor: pointer;
  line-height: 1;
  padding: 0;
}

.search-input {
  flex-grow: 1;
  border: none;
  outline: none;
  padding: 0;
  min-width: 150px;
}

.search-results {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 10;
  max-height: 200px;
  overflow-y: auto;
  margin-top: 4px;
}

.search-result-item {
  padding: 10px 12px;
  cursor: pointer;
  font-size: 14px;
  color: #334155;
}

.search-result-item:hover {
  background-color: #f1f5f9;
}
@media (max-width: 768px) {
  .header{ flex-direction:column; gap:12px; align-items:stretch }
  .actions{ flex-wrap:wrap }
  .input{ width:100% }
  .grid{ grid-template-columns:1fr }
}
</style>


