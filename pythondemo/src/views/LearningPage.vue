<template>
  <div class="container mt-5">
    <h2>学习进度树</h2>
    <ul class="list-group mb-4">
      <li class="list-group-item" v-for="node in nodes" :key="node.id" @click="goToNode(node)">
        {{ node.title }} <span class="badge bg-success" v-if="node.completed">已完成</span>
      </li>
    </ul>
    <h4>精品课程推荐</h4>
    <div class="row">
      <div class="col-md-4" v-for="course in courses" :key="course.id">
        <div class="card mb-3">
          <img :src="course.image" class="card-img-top" alt="课程图片">
          <div class="card-body">
            <h5 class="card-title">{{ course.title }}</h5>
            <button class="btn btn-outline-primary" @click="goToCourse(course.id)">查看详情</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'LearningPage',
  data() {
    return {
      nodes: [
        { id: 1, title: 'Python基础', completed: true },
        { id: 2, title: 'Python进阶', completed: false },
        { id: 3, title: 'Web开发', completed: false }
      ],
      courses: [
        { id: 1, title: 'Python基础入门', image: 'https://picsum.photos/600/300?random=10' },
        { id: 2, title: '数据分析与可视化', image: 'https://picsum.photos/600/300?random=11' }
      ]
    }
  },
  methods: {
    goToNode(node) {
      // 跳转到知识点学习页面
      alert('进入知识点学习页面：' + node.title)
    },
    goToCourse(id) {
      this.$router.push('/course/' + id)
    }
  }
}
</script>
