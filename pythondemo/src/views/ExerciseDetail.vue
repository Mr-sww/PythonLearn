<template>
    <div class="container mt-5">
      <h2>题目详情</h2>
      <div class="card mb-3">
        <div class="card-body">
          <h4>{{ exercise.title }}</h4>
          <p>{{ exercise.description }}</p>
          <button class="btn btn-outline-info mb-3" @click="askAI">AI解答</button>
          <h5>评论区</h5>
          <ul class="list-group mb-3">
            <li class="list-group-item" v-for="comment in comments" :key="comment.id">
              <strong>{{ comment.user }}</strong>：{{ comment.content }}
            </li>
          </ul>
          <div class="input-group">
            <input v-model="newComment" class="form-control" placeholder="发布你的评论或题解">
            <button class="btn btn-primary" @click="postComment">发布</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  <script>
  export default {
    data() {
      return {
        exercise: {
          title: '计算斐波那契数列',
          description: '编写一个Python函数，计算并返回斐波那契数列的前n项。'
        },
        comments: [
          { id: 1, user: '小明', content: '可以用递归实现！' }
        ],
        newComment: ''
      }
    },
    methods: {
      askAI() {
        alert('AI解答：fib(n) = fib(n-1) + fib(n-2)')
      },
      postComment() {
        if (this.newComment) {
          this.comments.push({ id: Date.now(), user: '我', content: this.newComment })
          this.newComment = ''
        }
      }
    }
  }
  </script>