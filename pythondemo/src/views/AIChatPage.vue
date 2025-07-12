<template>
<div class="ai-chat-page">
  <!-- 左侧历史 -->
  <div class="chat-history">
    <div class="history-title">历史记录</div>
    <div class="history-toolbar">
      <button class="toolbar-btn" @click="newChat">
        <i class="fa fa-plus"></i> 新建对话
      </button>
    </div>
    <ul>
      <li
        v-for="(item, idx) in historyList"
        :key="idx"
        :class="{active: idx === currentHistoryIndex}"
        @click="switchHistory(idx)"
      >
        <div class="history-title-text">{{ item.title || '新会话' }}</div>
        <div class="history-time">{{ item.time }}</div>
      </li>
    </ul>
    <button class="clear-btn" @click="clearHistory">清空历史</button>
  </div>
  <!-- 右侧主对话区 -->
  <div class="chat-main">
    <div class="chat-header">
      <span class="ai-title"><i class="fa fa-robot"></i> Python学习助手</span>
      <div class="header-actions">
        <button class="action-btn" @click="exportChat" title="导出对话">
          <i class="fa fa-download"></i>
        </button>
        <button class="action-btn" @click="clearCurrentChat" title="清空当前对话">
          <i class="fa fa-trash"></i>
        </button>
      </div>
    </div>
    <div class="chat-messages" ref="msgList">
      <div
        v-for="msg in currentHistory.messages"
        :key="msg.id"
        :class="['msg', msg.from]"
      >
        <img
          :src="msg.from === 'user' ? userAvatar : aiAvatar"
          class="avatar"
        />
        <!-- 普通文本 -->
        <div
          v-if="!msg.file && !msg.markdown && !msg.image"
          class="bubble"
        >{{ msg.content }}</div>
        <!-- Markdown渲染 -->
        <div
          v-if="msg.markdown"
          class="bubble"
          v-html="renderMarkdown(msg.content)"
        ></div>
        <!-- 图片消息 -->
        <div class="bubble image-bubble" v-if="msg.image">
          <img :src="msg.image.url" :alt="msg.image.alt || '图片'" class="chat-image" />
          <div class="image-caption" v-if="msg.image.caption">{{ msg.image.caption }}</div>
        </div>
        <!-- 文件气泡 -->
        <div class="bubble file-bubble" v-if="msg.file">
          <i class="fa fa-file-code-o"></i>
          <div class="file-info">
            <div class="file-name">{{ msg.file.name }}</div>
            <div class="file-size">{{ formatFileSize(msg.file.size) }}</div>
          </div>
          <a :href="msg.file.url" target="_blank" class="file-download">
            <i class="fa fa-download"></i>
          </a>
        </div>
        <!-- 加载状态 -->
        <div class="bubble loading-bubble" v-if="msg.loading">
          <div class="loading-dots">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <div class="input-container">
        <textarea
          v-model="input"
          class="chat-textarea"
          placeholder="请输入问题或上传文件进行分析..."
          rows="2"
          @keydown.enter.exact.prevent="send"
          @keydown.shift.enter.stop
          :disabled="isLoading"
        ></textarea>
        <div class="input-actions">
          <input
            type="file"
            ref="fileInput"
            @change="upload"
            class="upload-btn"
            accept=".py,.txt,.js,.java,.cpp,.c,.jpg,.jpeg,.png,.gif"
            :disabled="isLoading"
          />
          <button @click="triggerUpload" title="上传文件" :disabled="isLoading">
            <i class="fa fa-paperclip"></i>
          </button>
          <button @click="send" :disabled="isLoading || !input.trim()">
            <i class="fa fa-paper-plane"></i>
          </button>
        </div>
      </div>
      <div class="input-tips">
        <span class="tip-item"><i class="fa fa-lightbulb-o"></i> Enter发送，Shift+Enter换行</span>
        <span class="tip-item"><i class="fa fa-image"></i> 支持上传图片进行分析</span>
        <span class="tip-item"><i class="fa fa-code"></i> 支持代码文件分析</span>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import axios from "axios";
import { marked } from "marked";
import hljs from "highlight.js";
import "highlight.js/styles/github.css";

marked.setOptions({
  highlight: function (code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value;
    }
    return hljs.highlightAuto(code).value;
  },
  breaks: true,
  gfm: true,
});

export default {
  name: "AIChatPage",
  data() {
    return {
      input: "",
      isLoading: false,
      userAvatar: "https://picsum.photos/seed/user/40/40",
      aiAvatar: "https://picsum.photos/seed/ai/40/40",
      historyList: [
        {
          title: "Python基础语法问题",
          time: "2小时前",
          messages: [
            { id: 1, from: "user", content: "Python的变量怎么定义？" },
            { id: 2, from: "ai", content: "Python变量无需声明类型，直接赋值即可，如：\n```python\nx = 10\n```", markdown: true },
            { id: 3, from: "user", content: "那如何定义一个字符串变量？" },
            { id: 4, from: "ai", content: "直接赋值即可，如：\n```python\nname = 'Alice'\n```", markdown: true },
            { id: 5, from: "user", content: "变量名有什么命名规则？" },
            { id: 6, from: "ai", content: "变量名只能包含字母、数字和下划线，不能以数字开头，区分大小写。" },
          ],
        },
        {
          title: "文件操作怎么写",
          time: "1小时前",
          messages: [
            { id: 7, from: "user", content: "Python如何读取一个文本文件？" },
            { id: 8, from: "ai", content: "可以使用open函数，示例：\n```python\nwith open('file.txt', 'r') as f:\n    content = f.read()\n```", markdown: true },
            { id: 9, from: "user", content: "如何逐行读取文件？" },
            { id: 10, from: "ai", content: "可以遍历文件对象：\n```python\nwith open('file.txt') as f:\n    for line in f:\n        print(line)\n```", markdown: true },
            { id: 11, from: "user", content: "写文件怎么做？" },
            { id: 12, from: "ai", content: "用'w'模式打开文件即可：\n```python\nwith open('out.txt', 'w') as f:\n    f.write('Hello!')\n```", markdown: true },
          ],
        },
        {
          title: "列表和字典的区别",
          time: "30分钟前",
          messages: [
            { id: 13, from: "user", content: "Python的列表和字典有什么区别？" },
            { id: 14, from: "ai", content: "列表是有序的元素集合，字典是键值对的无序集合。\n\n- 列表：`[1, 2, 3]`\n- 字典：`{'a': 1, 'b': 2}`", markdown: true },
            { id: 15, from: "user", content: "怎么遍历字典的所有键值？" },
            { id: 16, from: "ai", content: "可以这样：\n```python\nfor k, v in d.items():\n    print(k, v)\n```", markdown: true },
            { id: 17, from: "user", content: "列表可以存不同类型吗？" },
            { id: 18, from: "ai", content: "可以，Python列表可以包含任意类型的元素。" },
          ],
        },
      ],
      currentHistoryIndex: 0,
    };
  },
  computed: {
    currentHistory() {
      return this.historyList[this.currentHistoryIndex];
    },
  },
  methods: {
    async send() {
      if (!this.input.trim() || this.isLoading) return;
      
      const question = this.input.trim();
      this.input = "";
      this.isLoading = true;
      
      // 添加用户消息
      this.currentHistory.messages.push({
        id: Date.now(),
        from: "user",
        content: question,
      });
      
      // 添加AI加载消息
      const loadingMsgId = Date.now() + 1;
      this.currentHistory.messages.push({
        id: loadingMsgId,
        from: "ai",
        loading: true,
      });
      
      this.$nextTick(this.scrollToBottom);
      
      try {
        // 构建历史记录
        const history = this.currentHistory.messages
          .filter(msg => !msg.loading && msg.from === 'user')
          .map(msg => msg.content)
          .join('\n');
        
        const res = await axios.post("/api/ai/ask", { 
          question,
          history 
        });
        
        if (res.data.success) {
          // 移除加载消息，添加AI回复
          const loadingIndex = this.currentHistory.messages.findIndex(msg => msg.id === loadingMsgId);
          if (loadingIndex !== -1) {
            this.currentHistory.messages.splice(loadingIndex, 1);
          }
          
          this.currentHistory.messages.push({
            id: Date.now() + 2,
            from: "ai",
            content: res.data.answer,
            markdown: true,
          });
          
          // 更新对话标题
          if (this.currentHistory.messages.length === 2) {
            this.currentHistory.title = question.substring(0, 20) + (question.length > 20 ? '...' : '');
          }
        } else {
          throw new Error(res.data.error || 'AI服务响应错误');
        }
      } catch (e) {
        console.error('AI请求失败:', e);
        
        // 移除加载消息，添加错误消息
        const loadingIndex = this.currentHistory.messages.findIndex(msg => msg.id === loadingMsgId);
        if (loadingIndex !== -1) {
          this.currentHistory.messages.splice(loadingIndex, 1);
        }
        
        this.currentHistory.messages.push({
          id: Date.now() + 3,
          from: "ai",
          content: `抱歉，AI服务暂时不可用。错误信息：${e.message}`,
        });
      } finally {
        this.isLoading = false;
        this.$nextTick(this.scrollToBottom);
      }
    },
    
    async upload(e) {
      const file = e.target.files[0];
      if (!file || this.isLoading) return;
      
      this.isLoading = true;
      
      // 添加文件消息
      this.currentHistory.messages.push({
        id: Date.now(),
        from: "user",
        content: "",
        file: { 
          name: file.name, 
          size: file.size,
          url: "" 
        },
      });
      
      // 添加AI加载消息
      const loadingMsgId = Date.now() + 1;
      this.currentHistory.messages.push({
        id: loadingMsgId,
        from: "ai",
        loading: true,
      });
      
      this.$nextTick(this.scrollToBottom);
      
      try {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("question", "请分析这个文件的内容");
        
        const res = await axios.post("/api/ai/upload", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        
        if (res.data.success) {
          // 移除加载消息，添加AI回复
          const loadingIndex = this.currentHistory.messages.findIndex(msg => msg.id === loadingMsgId);
          if (loadingIndex !== -1) {
            this.currentHistory.messages.splice(loadingIndex, 1);
          }
          
          this.currentHistory.messages.push({
            id: Date.now() + 2,
            from: "ai",
            content: res.data.answer,
            markdown: true,
          });
          
          // 更新文件URL
          const fileIndex = this.currentHistory.messages.length - 2;
          if (this.currentHistory.messages[fileIndex].file) {
            this.currentHistory.messages[fileIndex].file.url = res.data.fileUrl || "";
          }
          
          // 更新对话标题
          if (this.currentHistory.messages.length === 2) {
            this.currentHistory.title = `文件分析: ${file.name}`;
          }
        } else {
          throw new Error(res.data.error || '文件分析失败');
        }
      } catch (e) {
        console.error('文件上传失败:', e);
        
        // 移除加载消息，添加错误消息
        const loadingIndex = this.currentHistory.messages.findIndex(msg => msg.id === loadingMsgId);
        if (loadingIndex !== -1) {
          this.currentHistory.messages.splice(loadingIndex, 1);
        }
        
        this.currentHistory.messages.push({
          id: Date.now() + 3,
          from: "ai",
          content: `文件上传失败：${e.message}`,
        });
      } finally {
        this.isLoading = false;
        this.$refs.fileInput.value = "";
        this.$nextTick(this.scrollToBottom);
      }
    },
    
    triggerUpload() {
      this.$refs.fileInput.click();
    },
    
    switchHistory(idx) {
      this.currentHistoryIndex = idx;
      this.$nextTick(this.scrollToBottom);
    },
    
    clearHistory() {
      this.historyList = [
        {
          title: "新会话",
          time: new Date().toLocaleString(),
          messages: [],
        },
      ];
      this.currentHistoryIndex = 0;
    },
    
    clearCurrentChat() {
      this.currentHistory.messages = [];
      this.currentHistory.title = "新会话";
    },
    
    newChat() {
      this.historyList.unshift({
        title: '新会话',
        time: new Date().toLocaleString(),
        messages: [],
      });
      this.currentHistoryIndex = 0;
    },
    
    exportChat() {
      const chat = this.currentHistory;
      const content = chat.messages.map(msg => {
        const role = msg.from === 'user' ? '用户' : 'AI';
        return `${role}: ${msg.content || ''}`;
      }).join('\n\n');
      
      const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${chat.title || '对话记录'}.txt`;
      a.click();
      URL.revokeObjectURL(url);
    },
    
    scrollToBottom() {
      const el = this.$refs.msgList;
      if (el) el.scrollTop = el.scrollHeight;
    },
    
    renderMarkdown(md) {
      return marked(md || "");
    },
    
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
  },
};
</script>

<style scoped>
.ai-chat-page {
  display: flex;
  height: 100vh;
  background: #f7f8fa;
}
.chat-history {
  width: 260px;
  background: #fff;
  border-right: 1px solid #eee;
  display: flex;
  flex-direction: column;
  padding: 0 0 12px 0;
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  z-index: 10;
  height: auto;
  max-height: calc(100vh - 60px);
}
.history-title {
  font-weight: bold;
  font-size: 1.1rem;
  padding: 18px 18px 10px 18px;
  border-bottom: 1px solid #f0f0f0;
}
.history-toolbar {
  display: flex;
  gap: 8px;
  padding: 12px 18px 0 18px;
  background: #fff;
}
.toolbar-btn {
  background: #2563eb;
  color: #fff;
  border: none;
  border-radius: 16px;
  padding: 6px 16px;
  font-size: 0.98rem;
  cursor: pointer;
  transition: background 0.2s;
}
.toolbar-btn:hover {
  background: #1746a2;
}
.chat-history ul {
  flex: 1;
  list-style: none;
  margin: 0;
  padding: 0 0 0 0;
  overflow-y: auto;
}
.chat-history li {
  padding: 12px 18px 8px 18px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: background 0.2s;
}
.chat-history li.active,
.chat-history li:hover {
  background: #f0f6ff;
}
.history-title-text {
  font-weight: 500;
  color: #2563eb;
}
.history-time {
  font-size: 0.85rem;
  color: #999;
}
.clear-btn {
  margin: 10px 18px 0 18px;
  background: #f5f5f5;
  border: none;
  border-radius: 16px;
  padding: 6px 0;
  color: #888;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background 0.2s;
}
.clear-btn:hover {
  background: #e0e7ef;
}
.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f7f8fa;
  margin-left: 260px;
  min-width: 0;
}
.chat-header {
  padding: 18px 24px;
  font-weight: bold;
  font-size: 1.2rem;
  border-bottom: 1px solid #eee;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.ai-title {
  color: #2563eb;
}
.header-actions {
  display: flex;
  gap: 8px;
}
.action-btn {
  background: #f5f5f5;
  color: #666;
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
  transition: all 0.2s;
}
.action-btn:hover {
  background: #e0e7ef;
  color: #2563eb;
}
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 32px 24px 24px 24px;
  background: #f7f8fa;
  display: flex;
  flex-direction: column;
  margin-bottom: 140px;
}
.msg {
  display: flex;
  align-items: flex-end;
  margin-bottom: 18px;
}
.msg.user {
  flex-direction: row-reverse;
}
.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  margin: 0 10px;
}
.bubble {
  max-width: 60%;
  padding: 12px 18px;
  border-radius: 18px;
  background: #e6f0ff;
  color: #222;
  font-size: 1rem;
  word-break: break-all;
  box-shadow: 0 2px 8px rgba(37,99,235,0.04);
}
.msg.user .bubble {
  background: #2563eb;
  color: #fff;
}
.file-bubble {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fffbe6;
  color: #b8860b;
  border: 1px solid #ffe066;
  padding: 16px;
}
.file-info {
  flex: 1;
}
.file-name {
  font-weight: 500;
  margin-bottom: 4px;
}
.file-size {
  font-size: 0.85rem;
  color: #999;
}
.file-download {
  color: #b8860b;
  text-decoration: none;
  padding: 8px;
  border-radius: 6px;
  transition: background 0.2s;
}
.file-download:hover {
  background: #ffe066;
}
.image-bubble {
  padding: 8px;
  background: #fff;
  border: 1px solid #eee;
}
.chat-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 8px;
}
.image-caption {
  margin-top: 8px;
  font-size: 0.9rem;
  color: #666;
  text-align: center;
}
.loading-bubble {
  background: #f0f0f0;
  color: #666;
}
.loading-dots {
  display: flex;
  gap: 4px;
  align-items: center;
}
.loading-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #999;
  animation: loading 1.4s infinite ease-in-out;
}
.loading-dots span:nth-child(1) { animation-delay: -0.32s; }
.loading-dots span:nth-child(2) { animation-delay: -0.16s; }
@keyframes loading {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}
.chat-input {
  position: fixed;
  left: 260px;
  right: 0;
  bottom: 0;
  z-index: 20;
  background: #fff;
  border-top: 1px solid #eee;
  padding: 18px 24px 10px 24px;
}
.input-container {
  display: flex;
  align-items: flex-end;
  gap: 10px;
  margin-bottom: 8px;
}
.chat-textarea {
  flex: 1;
  border: 1.5px solid #e0e7ef;
  border-radius: 24px;
  padding: 16px 20px;
  font-size: 1.15rem;
  min-height: 48px;
  max-height: 120px;
  resize: none;
  background: #f8fafc;
  transition: border 0.2s;
  box-sizing: border-box;
  line-height: 1.6;
}
.chat-textarea:focus {
  border: 1.5px solid #2563eb;
  background: #fff;
}
.chat-textarea:disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
}
.input-actions {
  display: flex;
  gap: 8px;
  align-items: flex-end;
}
.upload-btn {
  display: none;
}
.chat-input button {
  background: #2563eb;
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 12px 22px;
  font-size: 1.08rem;
  cursor: pointer;
  transition: background 0.2s;
  margin-bottom: 4px;
}
.chat-input button:hover:not(:disabled) {
  background: #1746a2;
}
.chat-input button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.chat-input button[title] {
  background: #f5f5f5;
  color: #2563eb;
  border: 1px solid #e0e7ef;
  margin-right: 0;
  padding: 12px 12px;
}
.chat-input button[title]:hover:not(:disabled) {
  background: #e0e7ef;
}
.input-tips {
  display: flex;
  gap: 16px;
  font-size: 0.9rem;
  color: #999;
}
.tip-item {
  display: flex;
  align-items: center;
  gap: 4px;
}
@media (max-width: 900px) {
  .chat-history {
    width: 180px;
    left: 0;
  }
  .chat-main {
    margin-left: 180px;
  }
  .chat-input {
    left: 180px;
  }
  .input-tips {
    flex-direction: column;
    gap: 4px;
  }
}
</style> 