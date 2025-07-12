<template>
  <div class="container mt-5">
    <h2>练习进度表</h2>
    <div v-for="item in progress" :key="item.name" class="mb-3">
      <div class="d-flex justify-content-between">
        <span>{{ item.name }}</span>
        <span>{{ item.done }}/{{ item.total }}</span>
      </div>
      <div class="progress">
        <div class="progress-bar" :style="{width: (item.done/item.total*100)+'%'}"></div>
      </div>
    </div>
    <h4 class="mt-4">推荐练习题</h4>
    <ul class="list-group">
      <li class="list-group-item" v-for="ex in exercises" :key="ex.id" @click="goToExercise(ex.id)">
        {{ ex.title }} <span class="badge bg-info">{{ ex.level }}</span>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'PracticePage',
  data() {
    return {
      progress: [
        { name: '基础语法', done: 15, total: 20 },
        { name: '数据结构', done: 8, total: 15 }
      ],
      exercises: [
        { id: 1, title: '计算斐波那契数列', level: '简单' },
        { id: 2, title: '字符串反转', level: '简单' }
      ]
    }
  },
  methods: {
    goToExercise(id) {
      this.$router.push('/exercise/' + id)
    }
  }
}
</script>
