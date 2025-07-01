<template>
  <div class="container mt-5">
    <h2>AI问答</h2>
    <div class="row">
      <div class="col-md-3">
        <h5>历史记录</h5>
        <ul class="list-group">
          <li class="list-group-item" v-for="(msg, i) in history" :key="i" @click="loadHistory(i)">
            {{ msg.question }}
          </li>
        </ul>
      </div>
      <div class="col-md-9">
        <div class="mb-3">
          <input v-model="question" class="form-control" placeholder="输入您的问题...">
          <button class="btn btn-primary mt-2" @click="askAI">发送</button>
          <input type="file" class="form-control mt-2" @change="uploadFile">
        </div>
        <div v-for="msg in messages" :key="msg.id" class="alert" :class="msg.from === 'user' ? 'alert-primary' : 'alert-success'">
          {{ msg.content }}
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      question: '',
      messages: [],
      history: []
    }
  },
  methods: {
    askAI() {
      if (!this.question) return
      this.messages.push({ id: Date.now(), from: 'user', content: this.question })
      this.history.push({ question: this.question })
      setTimeout(() => {
        this.messages.push({ id: Date.now() + 1, from: 'ai', content: 'AI的回答：' + this.question })
      }, 1000)
      this.question = ''
    },
    loadHistory(i) {
      this.messages = [
        { id: Date.now(), from: 'user', content: this.history[i].question },
        { id: Date.now() + 1, from: 'ai', content: 'AI的回答：' + this.history[i].question }
      ]
    },
    // eslint-disable-next-line no-unused-vars
    uploadFile(e) {
      alert('上传功能待实现')
    }
  }
}
</script>
