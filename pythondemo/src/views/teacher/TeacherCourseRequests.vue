<template>
  <div class="container mx-auto p-6">
    <h2 class="text-2xl font-bold mb-4">课程创建申请</h2>
    <div class="mb-4 grid grid-cols-1 md:grid-cols-4 gap-2">
      <input v-model="form.title" class="border rounded px-3 py-2" placeholder="课程标题" />
      <input v-model="form.coverImage" class="border rounded px-3 py-2" placeholder="封面URL(可选)" />
      <input v-model="form.description" class="border rounded px-3 py-2 md:col-span-2" placeholder="描述" />
      <button @click="createRequest" class="bg-blue-600 text-white px-4 py-2 rounded">提交申请</button>
    </div>
    <div v-if="loading">加载中...</div>
    <table v-else class="min-w-full bg-white rounded border">
      <thead>
        <tr class="bg-gray-50 text-left">
          <th class="p-2">标题</th>
          <th class="p-2">状态</th>
          <th class="p-2">时间</th>
          <th class="p-2">备注</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in list" :key="r.requestId" class="border-t">
          <td class="p-2">{{ r.title }}</td>
          <td class="p-2">
            <span :class="{
              'text-yellow-600': r.status==='pending',
              'text-green-600': r.status==='approved',
              'text-red-600': r.status==='rejected'
            }">{{ r.status }}</span>
          </td>
          <td class="p-2">{{ r.createdAt }}</td>
          <td class="p-2">{{ r.reviewNote || '-' }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TeacherCourseRequests',
  data () {
    return { list: [], loading: true, form: { title: '', coverImage: '', description: '' } }
  },
  mounted () { this.fetch() },
  methods: {
    fetch () {
      this.loading = true
      axios.get('http://localhost:8080/api/teacher/course-requests', { withCredentials: true })
        .then(res => { this.list = res.data || [] })
        .finally(() => { this.loading = false })
    },
    createRequest () {
      if (!this.form.title) { alert('请输入课程标题'); return }
      axios.post('http://localhost:8080/api/teacher/course-requests', this.form, { withCredentials: true })
        .then(() => { this.form = { title: '', coverImage: '', description: '' }; this.fetch() })
        .catch(err => alert(err.response?.data || '提交失败'))
    }
  }
}
</script>

<style scoped>
</style>


