<template>
  <div class="page">
    <h2>知识点管理</h2>
    <div class="toolbar">
      <input v-model="q" placeholder="搜索知识点" />
      <button @click="refresh">刷新</button>
      <button @click="create">新建知识点</button>
    </div>
    <div class="empty" v-if="list.length===0">暂无数据</div>
    <ul v-else>
      <li v-for="k in list" :key="k.id">{{ k.name }}</li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'
const http = axios.create({ baseURL: 'http://localhost:8080/api', withCredentials: true })
export default {
  name: 'AdminKnowledges',
  data(){return{q:'',list:[], loading:false}},
  async mounted(){ await this.refresh() },
  methods:{
    async refresh(){
      this.loading = true
      try{
        const res = await http.get('/knowledge/points')
        this.list = Array.isArray(res.data) ? res.data : []
      } finally { this.loading = false }
    },
    create(){ /* TODO: 打开新建知识点对话框 */ }
  }
}
</script>

<style scoped>
.page{ padding:20px }
.toolbar{ display:flex; gap:8px; margin:12px 0 }
.empty{ color:#94a3b8 }
</style>


