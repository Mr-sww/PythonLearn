<template>
  <div class="container mx-auto p-6">
    <h2 class="text-2xl font-bold mb-4">课程审核</h2>
    <div class="mb-3 flex items-center gap-2">
      <select v-model="status" class="border rounded px-3 py-2">
        <option value="pending">待审核</option>
        <option value="approved">已通过</option>
        <option value="rejected">已拒绝</option>
      </select>
      <input v-model="keyword" class="border rounded px-3 py-2 w-56" placeholder="搜索标题" />
      <button @click="fetch" class="bg-gray-200 px-3 py-2 rounded">刷新</button>
    </div>
    <div v-if="loading">加载中...</div>
    <table v-else class="min-w-full bg-white rounded border">
      <thead>
        <tr class="bg-gray-50 text-left">
          <th class="p-2">标题</th>
          <th class="p-2">教师ID</th>
          <th class="p-2">状态</th>
          <th class="p-2">备注</th>
          <th class="p-2">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in paged" :key="r.requestId" class="border-t">
          <td class="p-2">{{ r.title }}</td>
          <td class="p-2">{{ r.teacherId }}</td>
          <td class="p-2">{{ r.status }}</td>
          <td class="p-2">{{ r.reviewNote || '-' }}</td>
          <td class="p-2 space-x-2">
            <button v-if="r.status==='pending'" @click="review(r.requestId,true)" class="bg-green-600 text-white px-3 py-1 rounded">通过</button>
            <button v-if="r.status==='pending'" @click="review(r.requestId,false)" class="bg-red-600 text-white px-3 py-1 rounded">拒绝</button>
          </td>
        </tr>
      </tbody>
    </table>
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
  name: 'AdminCourseApprovals',
  data () {
    return { list: [], loading: true, status: 'pending', keyword: '', page: 1, pageSize: 10 }
  },
  mounted () { this.fetch() },
  methods: {
    fetch () {
      this.loading = true
      axios.get(`http://localhost:8080/api/admin/course-requests?status=${this.status}`, { withCredentials: true })
        .then(res => { this.list = res.data || [] })
        .catch(err => {
          if (err?.response?.status === 403) {
            alert('需要管理员登录后才能访问，请先登录');
            this.$router && this.$router.push('/auth')
          } else {
            console.error(err)
          }
        })
        .finally(() => { this.loading = false })
    },
    review (id, approve) {
      const note = approve ? '同意' : '不符合要求'
      axios.patch(`http://localhost:8080/api/admin/course-requests/${id}`, { approve, note }, { withCredentials: true })
        .then(() => this.fetch())
        .catch(err => {
          if (err?.response?.status === 403) {
            alert('需要管理员登录后才能操作，请先登录');
            this.$router && this.$router.push('/auth')
          } else {
            alert(err.response?.data || '操作失败')
          }
        })
    },
    paged () {
      const kw = (this.keyword || '').trim()
      const list = kw ? this.list.filter(x => (x.title || '').toLowerCase().includes(kw.toLowerCase())) : this.list
      this._total = list.length
      const start = (this.page - 1) * this.pageSize
      return list.slice(start, start + this.pageSize)
    },
    totalPages () { return Math.max(1, Math.ceil((this._total || this.list.length) / this.pageSize)) }
  }
}
</script>

<style scoped>
</style>


