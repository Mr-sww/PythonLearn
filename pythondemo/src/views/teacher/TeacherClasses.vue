<template>
  <div class="container mx-auto p-6">
    <h2 class="text-2xl font-bold mb-4">我的班级</h2>
    <div class="mb-4 flex gap-2">
      <input v-model="form.name" class="border rounded px-3 py-2" placeholder="班级名称" />
      <input v-model="form.description" class="border rounded px-3 py-2 w-64" placeholder="班级描述" />
      <input v-model="keyword" placeholder="搜索" class="border rounded px-3 py-2 w-56" />
      <button @click="createClass" class="bg-blue-600 text-white px-4 py-2 rounded">创建班级</button>
    </div>
    <div v-if="loading">加载中...</div>
    <ul v-else class="space-y-2">
      <li v-for="c in paged" :key="c.classId" class="p-3 bg-white rounded border">
        <div class="font-semibold">{{ c.name }}</div>
        <div class="text-gray-500 text-sm">{{ c.description }}</div>
      </li>
    </ul>
    <div v-if="totalPages>1" class="flex justify-end items-center gap-2 mt-3">
      <button :disabled="page===1" @click="page--" class="px-3 py-1 border rounded disabled:opacity-50">上一页</button>
      <span class="text-sm text-gray-600">{{ page }} / {{ totalPages }}</span>
      <button :disabled="page===totalPages" @click="page++" class="px-3 py-1 border rounded disabled:opacity-50">下一页</button>
    </div>
  </div>
  
</template>

<script>
import axios from 'axios'

export default {
  name: 'TeacherClasses',
  data () {
    return { classes: [], loading: true, form: { name: '', description: '' }, keyword: '', page: 1, pageSize: 8 }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    fetch () {
      this.loading = true
      axios.get('http://localhost:8080/api/teacher/classes', { withCredentials: true })
        .then(res => { this.classes = res.data || [] })
        .finally(() => { this.loading = false })
    },
    createClass () {
      if (!this.form.name) { alert('请输入班级名称'); return }
      axios.post('http://localhost:8080/api/teacher/classes', this.form, { withCredentials: true })
        .then(() => { this.form = { name: '', description: '' }; this.fetch() })
        .catch(err => alert(err.response?.data || '创建失败'))
    },
    paged () {
      const kw = (this.keyword || '').trim()
      const list = kw ? this.classes.filter(x => (x.name || '').toLowerCase().includes(kw.toLowerCase())) : this.classes
      this._total = list.length
      const start = (this.page - 1) * this.pageSize
      return list.slice(start, start + this.pageSize)
    },
    totalPages () { return Math.max(1, Math.ceil((this._total || this.classes.length) / this.pageSize)) }
  }
}
</script>

<style scoped>
</style>


