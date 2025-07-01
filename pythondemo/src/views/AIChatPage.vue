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
          v-if="!msg.file && !msg.markdown"
          class="bubble"
        >{{ msg.content }}</div>
        <!-- Markdown渲染 -->
        <div
          v-if="msg.markdown"
          class="bubble"
          v-html="renderMarkdown(msg.content)"
        ></div>
        <!-- 文件气泡 -->
        <div class="bubble file-bubble" v-if="msg.file">
          <i class="fa fa-file-code-o"></i>
          <a :href="msg.file.url" target="_blank">{{ msg.file.name }}</a>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <textarea
        v-model="input"
        class="chat-textarea"
        placeholder="请输入问题..."
        rows="2"
        @keydown.enter.exact.prevent="send"
        @keydown.shift.enter.stop
      ></textarea>
      <input
        type="file"
        ref="fileInput"
        @change="upload"
        class="upload-btn"
        accept=".py,.txt,.js,.java,.cpp,.c"
      />
      <button @click="triggerUpload" title="上传代码">
        <i class="fa fa-paperclip"></i>
      </button>
      <button @click="send">发送</button>
    </div>
    <div class="chat-input-tip">
      <span class="tip-left"><i class="fa fa-lightbulb-o"></i> Enter发送，Shift+Enter换行</span>
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
      userAvatar: "https://picsum.photos/seed/user/40/40",
      aiAvatar: "https://picsum.photos/seed/ai/40/40",
      historyList: [
        {
          title: "Python基础语法问题",
          time: "2小时前",
          messages: [
            {
              id: 1,
              from: "user",
              content: "Python的变量怎么定义？",
            },
            {
              id: 2,
              from: "ai",
              content:
                "Python变量无需声明类型，直接赋值即可，如：\n```python\nx = 10\n```",
              markdown: true,
            },
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
      if (!this.input.trim()) return;
      // 用户消息
      this.currentHistory.messages.push({
        id: Date.now(),
        from: "user",
        content: this.input,
      });
      const question = this.input;
      this.input = "";
      this.$nextTick(this.scrollToBottom);
      // 向后端发送问题
      try {
        const res = await axios.post("/api/ai/ask", { question });
        // 假设后端返回 { answer: "markdown内容" }
        this.currentHistory.messages.push({
          id: Date.now() + 1,
          from: "ai",
          content: res.data.answer,
          markdown: true,
        });
      } catch (e) {
        this.currentHistory.messages.push({
          id: Date.now() + 2,
          from: "ai",
          content: "AI服务暂时不可用，请稍后再试。",
        });
      }
      this.$nextTick(this.scrollToBottom);
    },
    async upload(e) {
      const file = e.target.files[0];
      if (!file) return;
      // 上传到后端
      const formData = new FormData();
      formData.append("file", file);
      this.currentHistory.messages.push({
        id: Date.now(),
        from: "user",
        content: "",
        file: { name: file.name, url: "" },
      });
      this.$nextTick(this.scrollToBottom);
      try {
        const res = await axios.post("/api/ai/upload", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        // 假设后端返回 { answer: "markdown内容", fileUrl: "..." }
        this.currentHistory.messages.push({
          id: Date.now() + 1,
          from: "ai",
          content: res.data.answer,
          markdown: true,
        });
        // 更新文件url
        this.currentHistory.messages[
          this.currentHistory.messages.length - 2
        ].file.url = res.data.fileUrl || "";
      } catch (e) {
        this.currentHistory.messages.push({
          id: Date.now() + 2,
          from: "ai",
          content: "文件上传失败，请重试。",
        });
      }
      this.$refs.fileInput.value = "";
      this.$nextTick(this.scrollToBottom);
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
    newChat() {
      this.historyList.unshift({
        title: '新会话',
        time: new Date().toLocaleString(),
        messages: [],
      });
      this.currentHistoryIndex = 0;
    },
    scrollToBottom() {
      const el = this.$refs.msgList;
      if (el) el.scrollTop = el.scrollHeight;
    },
    renderMarkdown(md) {
      return marked(md || "");
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
  top: 60px; /* 假设顶部导航栏高度为60px */
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
}
.ai-title {
  color: #2563eb;
}
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 32px 24px 24px 24px;
  background: #f7f8fa;
  display: flex;
  flex-direction: column;
  margin-bottom: 110px; /* 留出输入栏高度，避免被遮挡 */
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
  gap: 8px;
  background: #fffbe6;
  color: #b8860b;
  border: 1px solid #ffe066;
}
.chat-input {
  position: fixed;
  left: 260px;
  right: 0;
  bottom: 0;
  z-index: 20;
  display: flex;
  align-items: flex-end;
  padding: 18px 24px 10px 24px;
  background: #fff;
  border-top: 1px solid #eee;
  gap: 10px;
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
.chat-input button:hover {
  background: #1746a2;
}
.chat-input button[title] {
  background: #f5f5f5;
  color: #2563eb;
  border: 1px solid #e0e7ef;
  margin-right: 0;
  padding: 12px 12px;
}
.chat-input button[title]:hover {
  background: #e0e7ef;
}
.chat-input-tip {
  position: fixed;
  left: 260px;
  right: 0;
  bottom: 0;
  z-index: 21;
  padding: 0 24px 8px 24px;
  background: transparent;
  pointer-events: none;
  display: flex;
  align-items: center;
  font-size: 0.98rem;
  color: #b0b3b8;
}
.tip-left {
  display: flex;
  align-items: center;
  gap: 6px;
}
@media (max-width: 900px) {
  .chat-history {
    width: 180px;
    left: 0;
  }
  .chat-main {
    margin-left: 180px;
  }
  .chat-input, .chat-input-tip {
    left: 180px;
  }
}
</style> 