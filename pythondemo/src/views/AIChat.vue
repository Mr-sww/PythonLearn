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
          <input v-model="question" class="form-control" placeholder="输入您的问题..." @keyup.enter="askAI">
          <button class="btn btn-primary mt-2" @click="askAI" :disabled="isLoading">
            <span v-if="isLoading">
              <i class="fa fa-spinner fa-spin"></i> 思考中...
            </span>
            <span v-else>发送</span>
          </button>
          <input type="file" class="form-control mt-2" @change="uploadFile" accept=".py,.txt,.js,.java,.cpp,.c,.jpg,.jpeg,.png,.gif">
        </div>
        <div v-for="msg in messages" :key="msg.id" class="alert" :class="msg.from === 'user' ? 'alert-primary' : 'alert-success'">
          <div v-if="msg.loading" class="d-flex align-items-center">
            <i class="fa fa-spinner fa-spin me-2"></i>
            <span>AI正在思考中...</span>
          </div>
          <div v-else>{{ msg.content }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      question: '',
      messages: [],
      history: [],
      isLoading: false
    }
  },
  methods: {
    async askAI() {
      if (!this.question.trim() || this.isLoading) return;
      
      const question = this.question.trim();
      this.question = '';
      this.isLoading = true;
      
      // 添加用户消息
      this.messages.push({ 
        id: Date.now(), 
        from: 'user', 
        content: question 
      });
      
      // 添加AI加载消息
      const loadingMsg = {
        id: Date.now() + 1,
        from: 'ai',
        content: '',
        loading: true
      };
      this.messages.push(loadingMsg);
      
      try {
        const res = await axios.post("/api/ai/ask", { 
          question,
          history: ""
        });
        
        if (res.data.success) {
          // 移除加载消息，添加AI回复
          const loadingIndex = this.messages.findIndex(msg => msg.loading);
          if (loadingIndex !== -1) {
            this.messages.splice(loadingIndex, 1);
          }
          
          this.messages.push({ 
            id: Date.now() + 2, 
            from: 'ai', 
            content: res.data.answer 
          });
          
          // 保存到历史记录
          this.history.push({ question });
        } else {
          throw new Error(res.data.error || 'AI服务响应错误');
        }
      } catch (e) {
        console.error('AI请求失败:', e);
        
        // 移除加载消息，添加错误消息
        const loadingIndex = this.messages.findIndex(msg => msg.loading);
        if (loadingIndex !== -1) {
          this.messages.splice(loadingIndex, 1);
        }
        
        this.messages.push({ 
          id: Date.now() + 3, 
          from: 'ai', 
          content: `抱歉，AI服务暂时不可用。错误信息：${e.message}` 
        });
      } finally {
        this.isLoading = false;
      }
    },
    
    async uploadFile(e) {
      const file = e.target.files[0];
      if (!file || this.isLoading) return;
      
      this.isLoading = true;
      
      // 添加文件消息
      this.messages.push({
        id: Date.now(),
        from: 'user',
        content: `上传文件: ${file.name}`
      });
      
      // 添加AI加载消息
      const loadingMsg = {
        id: Date.now() + 1,
        from: 'ai',
        content: '',
        loading: true
      };
      this.messages.push(loadingMsg);
      
      try {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("question", "请分析这个文件的内容");
        
        const res = await axios.post("/api/ai/upload", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        
        if (res.data.success) {
          // 移除加载消息，添加AI回复
          const loadingIndex = this.messages.findIndex(msg => msg.loading);
          if (loadingIndex !== -1) {
            this.messages.splice(loadingIndex, 1);
          }
          
          this.messages.push({
            id: Date.now() + 2,
            from: 'ai',
            content: res.data.answer
          });
          
          // 保存到历史记录
          this.history.push({ question: `文件分析: ${file.name}` });
        } else {
          throw new Error(res.data.error || '文件分析失败');
        }
      } catch (e) {
        console.error('文件上传失败:', e);
        
        // 移除加载消息，添加错误消息
        const loadingIndex = this.messages.findIndex(msg => msg.loading);
        if (loadingIndex !== -1) {
          this.messages.splice(loadingIndex, 1);
        }
        
        this.messages.push({
          id: Date.now() + 3,
          from: 'ai',
          content: `文件上传失败：${e.message}`
        });
      } finally {
        this.isLoading = false;
        e.target.value = '';
      }
    },
    
    loadHistory(i) {
      this.messages = [
        { id: Date.now(), from: 'user', content: this.history[i].question },
        { id: Date.now() + 1, from: 'ai', content: '点击发送按钮重新获取AI回答' }
      ]
    }
  }
}
</script>
