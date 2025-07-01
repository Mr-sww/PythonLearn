<template>
<div>
  <!-- AI问答内容 -->
  <main class="container py-4">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="fw-bold text-dark mb-2">AI智能问答</h2>
      <p class="text-muted mb-0">智能解答Python相关问题，助力学习进步</p>
    </div>

    <div class="row g-4">
      <!-- 左侧边栏 -->
      <div class="col-lg-3">
        <!-- 常见问题 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <h5 class="fw-bold text-dark mb-3">常见问题</h5>
          <div class="space-y-2">
            <button class="btn btn-light w-100 text-start mb-2" @click="sendQuickQuestion('如何安装Python')">
              <i class="fa fa-question-circle me-2 text-primary"></i>
              如何安装Python
            </button>
            <button class="btn btn-light w-100 text-start mb-2" @click="sendQuickQuestion('Python基础语法')">
              <i class="fa fa-question-circle me-2 text-primary"></i>
              Python基础语法
            </button>
            <button class="btn btn-light w-100 text-start mb-2" @click="sendQuickQuestion('Python和JavaScript的区别')">
              <i class="fa fa-question-circle me-2 text-primary"></i>
              Python和JavaScript的区别
            </button>
            <button class="btn btn-light w-100 text-start mb-2" @click="sendQuickQuestion('如何使用Django')">
              <i class="fa fa-question-circle me-2 text-primary"></i>
              如何使用Django
            </button>
            <button class="btn btn-light w-100 text-start mb-2" @click="sendQuickQuestion('Python数据分析库')">
              <i class="fa fa-question-circle me-2 text-primary"></i>
              Python数据分析库
            </button>
            <button class="btn btn-light w-100 text-start mb-2" @click="sendQuickQuestion('Python爬虫教程')">
              <i class="fa fa-question-circle me-2 text-primary"></i>
              Python爬虫教程
            </button>
          </div>
        </div>

        <!-- 聊天历史 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-bold text-dark mb-0">聊天历史</h5>
            <button class="btn btn-outline-primary btn-sm rounded-pill">清空</button>
          </div>
          <div class="space-y-2">
            <div class="p-2 border rounded cursor-pointer hover-bg-light" v-for="(history, index) in chatHistory" :key="index" @click="loadHistory(history)">
              <div class="fw-medium text-dark small">{{ history.title }}</div>
              <div class="text-muted small">{{ history.time }}</div>
            </div>
          </div>
        </div>

        <!-- AI助手信息 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h5 class="fw-bold text-dark mb-3">AI助手</h5>
          <div class="text-center">
            <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:80px;height:80px;">
              <i class="fa fa-robot text-primary fa-3x"></i>
            </div>
            <h6 class="fw-bold text-dark mb-2">Python学习助手</h6>
            <p class="text-muted small mb-3">专业的Python编程问答助手，为您解答学习过程中的各种问题</p>
            <div class="d-flex justify-content-center gap-2">
              <span class="badge bg-success">在线</span>
              <span class="badge bg-primary">响应快速</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧聊天区域 -->
      <div class="col-lg-9">
        <div class="bg-white rounded-3 shadow-sm border d-flex flex-column" style="height:600px;">
          <!-- 聊天头部 -->
          <div class="p-3 border-bottom bg-light">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <div class="bg-primary rounded-circle d-flex align-items-center justify-content-center text-white me-2" style="width:32px;height:32px;">
                  <i class="fa fa-robot"></i>
                </div>
                <div>
                  <div class="fw-bold text-dark">Python学习助手</div>
                  <div class="text-muted small">在线 · 响应快速</div>
                </div>
              </div>
              <div class="d-flex gap-2">
                <button class="btn btn-outline-secondary btn-sm rounded-pill">
                  <i class="fa fa-download"></i>
                </button>
                <button class="btn btn-outline-secondary btn-sm rounded-pill">
                  <i class="fa fa-share"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- 聊天消息区域 -->
          <div class="flex-grow-1 p-3 overflow-auto" ref="chatContainer">
            <div class="space-y-3">
              <!-- AI欢迎消息 -->
              <div class="d-flex align-items-start">
                <div class="rounded-circle bg-primary d-flex align-items-center justify-content-center text-white me-2 mt-1" style="width:32px;height:32px;">
                  <i class="fa fa-robot"></i>
                </div>
                <div class="bg-light border rounded-4 rounded-start-0 px-4 py-3" style="max-width:70%;">
                  <div class="text-dark mb-2">你好！我是Python学习助手，很高兴为您服务！</div>
                  <div class="text-muted small">我可以帮助您解答Python编程相关的问题，包括语法、框架、最佳实践等。请随时向我提问！</div>
                </div>
              </div>

              <!-- 用户问题 -->
              <div class="d-flex align-items-start justify-content-end">
                <div class="bg-primary text-white rounded-4 rounded-end-0 px-4 py-3 me-2" style="max-width:70%;">
                  <div>请问Python适合零基础学习吗？</div>
                </div>
                <div class="rounded-circle bg-secondary d-flex align-items-center justify-content-center text-dark ms-2 mt-1" style="width:32px;height:32px;">
                  <i class="fa fa-user"></i>
                </div>
              </div>

              <!-- AI回答 -->
              <div class="d-flex align-items-start">
                <div class="rounded-circle bg-primary d-flex align-items-center justify-content-center text-white me-2 mt-1" style="width:32px;height:32px;">
                  <i class="fa fa-robot"></i>
                </div>
                <div class="bg-light border rounded-4 rounded-start-0 px-4 py-3" style="max-width:70%;">
                  <div class="text-dark mb-2">当然适合！Python是公认的最适合初学者的编程语言之一：</div>
                  <ul class="text-dark mb-2">
                    <li>语法简洁明了，接近自然语言</li>
                    <li>丰富的学习资源和社区支持</li>
                    <li>应用领域广泛，就业前景好</li>
                    <li>有大量适合初学者的教程和项目</li>
                  </ul>
                  <div class="text-muted small">建议从基础语法开始，逐步学习面向对象编程、Web开发等进阶内容。</div>
                </div>
              </div>

              <!-- 代码示例 -->
              <div class="d-flex align-items-start">
                <div class="rounded-circle bg-primary d-flex align-items-center justify-content-center text-white me-2 mt-1" style="width:32px;height:32px;">
                  <i class="fa fa-robot"></i>
                </div>
                <div class="bg-light border rounded-4 rounded-start-0 px-4 py-3" style="max-width:70%;">
                  <div class="text-dark mb-2">这里是一个简单的Python示例：</div>
                  <div class="bg-dark text-light rounded p-3 mb-2" style="font-family: 'Courier New', monospace; font-size: 0.9rem;">
                    <div class="text-success"># 这是一个简单的Python程序</div>
                    <div class="text-primary">print</div><span class="text-warning">(</span><span class="text-info">"Hello, Python!"</span><span class="text-warning">)</span>
                    <br>
                    <div class="text-primary">name</div> <span class="text-warning">=</span> <span class="text-info">"初学者"</span>
                    <br>
                    <div class="text-primary">print</div><span class="text-warning">(</span><span class="text-info">f"欢迎 {name} 学习Python!"</span><span class="text-warning">)</span>
                  </div>
                  <div class="text-muted small">运行结果：Hello, Python! 欢迎 初学者 学习Python!</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="p-3 border-top bg-light">
            <div class="d-flex align-items-end gap-2">
              <div class="flex-grow-1">
                <textarea 
                  v-model="userMessage" 
                  @keydown.enter.prevent="sendMessage"
                  class="form-control rounded-pill" 
                  placeholder="输入您的问题..." 
                  rows="2"
                  style="resize: none;"
                ></textarea>
              </div>
              <div class="d-flex flex-column gap-2">
                <button class="btn btn-outline-secondary btn-sm rounded-pill" title="上传文件">
                  <i class="fa fa-paperclip"></i>
                </button>
                <button @click="sendMessage" class="btn btn-primary rounded-pill px-4">
                  <i class="fa fa-paper-plane me-1"></i>发送
                </button>
              </div>
            </div>
            <div class="d-flex justify-content-between align-items-center mt-2">
              <div class="text-muted small">
                <i class="fa fa-lightbulb-o me-1"></i>
                提示：可以问语法、框架、调试等问题
              </div>
              <div class="text-muted small">
                <i class="fa fa-keyboard-o me-1"></i>
                Enter发送，Shift+Enter换行
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</div>
</template>

<script>
export default {
  name: 'AIChatPage',
  data() {
    return {
      userMessage: '',
      chatHistory: [
        { title: 'Python基础语法问题', time: '2小时前' },
        { title: 'Django框架使用', time: '昨天' },
        { title: '数据分析库介绍', time: '3天前' },
        { title: 'Python环境配置', time: '1周前' }
      ]
    }
  },
  methods: {
    sendMessage() {
      if (this.userMessage.trim()) {
        // 这里可以添加发送消息的逻辑
        console.log('发送消息:', this.userMessage);
        this.userMessage = '';
      }
    },
    sendQuickQuestion(question) {
      this.userMessage = question;
      this.sendMessage();
    },
    loadHistory(history) {
      console.log('加载历史记录:', history);
    }
  }
}
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}

.hover-bg-light:hover {
  background-color: #f8f9fa;
}

.space-y-2 > * + * {
  margin-top: 0.5rem;
}

.space-y-3 > * + * {
  margin-top: 1rem;
}
</style> 