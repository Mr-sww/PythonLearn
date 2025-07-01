<template>
  <div class="container mt-5">
    <h2>课程详情</h2>
    <div class="card mb-3">
      <img :src="course.image" class="card-img-top" alt="课程图片" v-if="course.image">
      <div class="card-body">
        <h3 class="card-title">{{ course.title }}</h3>
        <p class="card-text">{{ course.description }}</p>
        <p>教学内容：{{ course.content }}</p>
        <p>订阅人数：{{ course.subscribers }}</p>
        <p>讲师：{{ course.teacher }}</p>
        <button class="btn btn-primary">立即学习</button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      course: {
        title: 'Python基础入门到精通',
        description: '从环境搭建到项目实战，全面系统地学习Python编程基础',
        content: '变量、数据类型、条件语句、循环、函数、模块等',
        subscribers: 12458,
        teacher: '张教授',
        image: 'https://picsum.photos/600/300?random=10'
      }
    }
  }
}
</script>
