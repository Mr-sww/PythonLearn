<template>
  <div class="page">
    <div class="header">
      <h2 class="title">题库管理</h2>
      <div class="actions">
        <input v-model.trim="q" @input="onQueryChange" class="input" type="text" placeholder="搜索题目 关键词..." />
        <button class="btn btn-secondary" @click="refresh"><i class="fas fa-sync"></i> 刷新</button>
        <button class="btn btn-primary" @click="create"><i class="fas fa-plus"></i> 新建题目</button>
        <button class="btn btn-outline" @click="exportCsv"><i class="fas fa-download"></i> 导出CSV</button>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="paged.length===0" class="empty">暂无数据</div>

    <div v-else class="grid">
      <div v-for="p in paged" :key="p.id" class="card">
        <div class="card-title">{{ p.title }}</div>
        <div class="card-meta">
          <span class="tag">ID: {{ p.id }}</span>
          <span class="tag" v-if="p.dif">难度: {{ difficulty(p.dif) }}</span>
        </div>
        <div class="card-actions">
          <button class="btn btn-sm" @click="openView(p)"><i class="fas fa-eye"></i> 查看</button>
          <button class="btn btn-sm btn-secondary" @click="openEdit(p)"><i class="fas fa-edit"></i> 编辑</button>
          <button class="btn btn-sm btn-danger" @click="remove(p)"><i class="fas fa-trash"></i> 删除</button>
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
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>查看题目</h3>
          <button class="modal-close" @click="showViewModal=false">×</button>
        </div>
        <div class="modal-body">
          <div class="kv"><span class="k">ID</span><span class="v">{{ selected?.id }}</span></div>
          <div class="kv"><span class="k">标题</span><span class="v">{{ selected?.title }}</span></div>
          <div class="kv"><span class="k">难度</span>
            <span class="v">
              <span :class="['badge', 'dif-'+(selected?.dif||0)]">{{ difficulty(selected?.dif) }}</span>
            </span>
          </div>
          <div class="kv"><span class="k">描述</span><span class="v multiline">{{ selected?.description || '无' }}</span></div>
          <div class="kv"><span class="k">输入</span><span class="v multiline">{{ selected?.inputFormat || '无' }}</span></div>
          <div class="kv"><span class="k">输出</span><span class="v multiline">{{ selected?.outputFormat || '无' }}</span></div>
          <div class="kv"><span class="k">样例</span>
            <span class="v multiline">
              <template v-if="samplePairs.length">
                <div v-for="(s,i) in samplePairs" :key="i" class="sample-item">
                  <div class="sample-title">样例 {{ i+1 }}</div>
                  <div class="sample-io">
                    <div class="sample-block">
                      <div class="label">输入</div>
                      <pre class="code"><code>{{ s.input }}</code></pre>
                    </div>
                    <div class="sample-block">
                      <div class="label">输出</div>
                      <pre class="code"><code>{{ s.output }}</code></pre>
                    </div>
                  </div>
                </div>
              </template>
              <span v-else>无</span>
            </span>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn" @click="showViewModal=false">关闭</button>
        </div>
      </div>
    </div>

    <!-- 编辑模态框 -->
    <div v-if="showEditModal" class="modal-overlay" @click="showEditModal=false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>编辑题目</h3>
          <button class="modal-close" @click="showEditModal=false">×</button>
        </div>
        <div class="modal-body form">
          <label>标题</label>
          <input class="input" v-model.trim="editForm.title" placeholder="请输入标题" />
          <label>难度</label>
          <select class="input" v-model.number="editForm.dif">
            <option :value="1">简单</option>
            <option :value="2">中等</option>
            <option :value="3">困难</option>
          </select>
          <label>题目描述</label>
          <textarea class="textarea" rows="4" v-model.trim="editForm.description" placeholder="请输入题目描述"></textarea>
          <label>输入格式</label>
          <textarea class="textarea" rows="3" v-model.trim="editForm.inputFormat" placeholder="请输入输入格式"></textarea>
          <label>输出格式</label>
          <textarea class="textarea" rows="3" v-model.trim="editForm.outputFormat" placeholder="请输入输出格式"></textarea>
          <label>样例</label>
          <textarea class="textarea" rows="4" v-model.trim="editForm.samples" placeholder="请输入测试样例"></textarea>
          <label>备注</label>
          <textarea class="textarea" rows="3" v-model.trim="editForm.note" placeholder="备注(可选)"></textarea>
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
  name: 'AdminProblems',
  data(){return{q:'',list:[], loading:false, page:1, pageSize:20, debounceTimer:null, showViewModal:false, showEditModal:false, selected:null, editForm:{ title:'', dif:1, description:'', inputFormat:'', outputFormat:'', samples:'', note:'', background:'' }}},
  async mounted(){ 
    await this.refresh() 
    // 检查URL参数，如果有edit参数则打开编辑模态框
    const urlParams = new URLSearchParams(window.location.search)
    const editId = urlParams.get('edit')
    if(editId) {
      // 查找对应的题目并打开编辑模态框
      const problem = this.list.find(p => p.id === editId)
      if(problem) {
        this.openEdit(problem)
      }
    }
  },
  computed:{
    pages(){ return Math.max(1, Math.ceil(this.list.length / this.pageSize)) },
    paged(){ const s=(this.page-1)*this.pageSize; return this.list.slice(s, s+this.pageSize) },
    samplePairs(){
      // 后端Samples字段可能为 JSON 数组字符串，如 [{input:'..',output:'..'}]
      try{
        const raw = this.selected && this.selected.samples
        if(!raw) return []
        const arr = typeof raw === 'string' ? JSON.parse(raw) : raw
        if(Array.isArray(arr)) return arr.map(x=>({ input: x.input||'', output:x.output||'' }))
        return []
      }catch(e){ return [] }
    }
  },
  methods:{
    async refresh(){
      this.loading = true
      try{
        const res = await http.get(this.q ? '/python-problems/search' : '/python-problems', { params:{ keyword: this.q || undefined } })
        this.list = Array.isArray(res.data) ? res.data : []
      } finally { this.loading = false }
    },
    create(){ /* TODO: 打开新建题目对话框 */ },
    onQueryChange(){ clearTimeout(this.debounceTimer); this.debounceTimer = setTimeout(()=>{ this.page=1; this.refresh() },300) },
    difficulty(d){ return d===1?'简单':d===2?'中等':d===3?'困难':'未知' },
    openView(p){ this.selected = p; this.showViewModal = true },
    openEdit(p){ this.selected = p; this.editForm = { title: p.title || '', dif: p.dif || 1, description: p.description || '', inputFormat: p.inputFormat || '', outputFormat: p.outputFormat || '', samples: p.samples || '', note: p.note || '', background: p.background || '' }; this.showEditModal = true },
    async saveEdit(){
      if(!this.selected) return
      try{
        await http.put(`/python-problems/${this.selected.id}`, { 
          title: this.editForm.title,
          dif: this.editForm.dif,
          description: this.editForm.description,
          inputFormat: this.editForm.inputFormat,
          outputFormat: this.editForm.outputFormat,
          samples: this.editForm.samples,
          note: this.editForm.note,
          background: this.editForm.background
        })
        Object.assign(this.selected, this.editForm)
        this.showEditModal = false
      }catch(e){ alert('保存失败') }
    },
    async remove(p){
      if(!window.confirm(`确定删除题目 ${p && p.id} 吗？`)) return
      try{
        await http.delete(`/python-problems/${p.id}`)
        this.list = this.list.filter(x=>x.id!==p.id)
      }catch(e){ alert('删除失败') }
    },
    go(p){ if(p>=1 && p<=this.pages) this.page=p },
    exportCsv(){
      const header=['id','title']
      const rows=this.list.map(x=>[x.id,(x.title||'').replace(/\n/g,' ')])
      const csv=[header,...rows].map(r=>r.join(',')).join('\n')
      const blob=new Blob([csv],{type:'text/csv;charset=utf-8;'})
      const a=document.createElement('a'); a.href=URL.createObjectURL(blob); a.download=`problems_${new Date().toISOString().slice(0,10)}.csv`; a.click()
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
.btn-outline{ background:#fff; border-color:#e5e7eb }
.btn-danger{ background:#ef4444; color:#fff }
.btn-sm{ padding:6px 10px; font-size:12px }
.loading{ color:#64748b }
.empty{ color:#94a3b8 }
.grid{ display:grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap:12px }
.card{ background:#fff; border-radius:12px; box-shadow:0 2px 8px rgba(0,0,0,.06); padding:14px; display:flex; flex-direction:column; gap:10px }
.card-title{ font-weight:600; color:#0f172a }
.card-meta{ display:flex; gap:8px; color:#64748b; font-size:12px }
.tag{ background:#f1f5f9; padding:2px 8px; border-radius:999px }
.card-actions{ display:flex; gap:8px }
.pagination{ display:flex; align-items:center; gap:10px; justify-content:flex-end; margin-top:16px }
.page-info{ color:#64748b }
.page-size{ padding:6px 8px; border:1px solid #e5e7eb; border-radius:6px }
/* modal */
.modal-overlay{ position:fixed; inset:0; background:rgba(0,0,0,.45); display:flex; align-items:center; justify-content:center; z-index:1000 }
.modal-content{ background:#fff; border-radius:12px; width:90%; max-width:520px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,.2) }
.modal-header{ display:flex; align-items:center; justify-content:space-between; padding:14px 16px; border-bottom:1px solid #eee }
.modal-body{ padding:16px }
.modal-footer{ padding:12px 16px; border-top:1px solid #eee; display:flex; justify-content:flex-end; gap:8px }
.modal-close{ background:none; border:none; font-size:20px; cursor:pointer }
.kv{ display:flex; gap:12px; padding:6px 0 }
.kv .k{ width:64px; color:#64748b }
.kv .v{ flex:1; color:#0f172a }
.kv .v.multiline{ white-space:pre-wrap; line-height:1.6 }
.badge{ display:inline-block; padding:2px 8px; border-radius:999px; font-size:12px; color:#fff }
.dif-1{ background:#22c55e }
.dif-2{ background:#f59e0b }
.dif-3{ background:#ef4444 }
.sample-item{ background:#f8fafc; border:1px solid #e5e7eb; border-radius:8px; padding:10px; margin-bottom:10px }
.sample-title{ font-weight:600; margin-bottom:6px; color:#334155 }
.sample-io{ display:grid; grid-template-columns:1fr 1fr; gap:10px }
.sample-block .label{ font-size:12px; color:#64748b; margin-bottom:4px }
.code{ background:#0f172a; color:#e2e8f0; border-radius:6px; padding:8px; overflow:auto }
.textarea{ width:100%; padding:8px 12px; border:1px solid #e5e7eb; border-radius:8px; font-family:inherit }
.form label{ display:block; margin:8px 0 6px; color:#334155; font-size:14px }
</style>


