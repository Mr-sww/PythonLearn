<template>
<div class="code-practice-container">
  <!-- 加载状态 -->
  <div v-if="loading" class="loading-overlay">
    <div class="spinner-border text-primary" role="status">
      <span class="visually-hidden">加载中...</span>
    </div>
  </div>

  <!-- 主要内容区域 -->
  <div class="practice-main">
    <!-- 左侧题目区域 -->
    <div class="problem-panel">
      <!-- 题目头部 -->
      <div class="problem-header">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <div>
            <h4 class="fw-bold text-dark mb-1">
              <span v-if="problem">{{ problem.title }}</span>
              <span v-else class="placeholder-glow">
                <span class="placeholder col-8"></span>
              </span>
            </h4>
            <div class="d-flex align-items-center gap-2">
              <span v-if="problem" :class="['badge', 
                problem.difficulty === '简单' ? 'bg-success' : 
                problem.difficulty === '中等' ? 'bg-warning text-dark' : 'bg-danger']">
                {{ problem.difficulty }}
              </span>
              <span v-else class="placeholder-glow">
                <span class="placeholder col-3"></span>
              </span>
              <span class="text-muted small">
                <i class="fa fa-check-circle me-1"></i>通过率: {{ problem ? '52.3%' : '--' }}
              </span>
              <span class="text-muted small">
                <i class="fa fa-upload me-1"></i>提交次数: {{ problem ? '2,456,789' : '--' }}
              </span>
            </div>
          </div>
          <div class="d-flex gap-2">
            <button
              class="collect-btn"
              :class="{ collected: isFavorite, animating: animating }"
              @click="handleFavoriteClick"
              :disabled="favoriteLoading"
              :title="isFavorite ? '取消收藏' : '收藏题目'"
            >
              <span class="star-icon">
                <svg v-if="!isFavorite" width="24" height="24" viewBox="0 0 40 40">
                  <polygon points="20,4 25,16 38,16 28,24 32,36 20,28 8,36 12,24 2,16 15,16"
                    fill="none" stroke="#2563eb" stroke-width="2"/>
                </svg>
                <svg v-else width="24" height="24" viewBox="0 0 40 40">
                  <polygon points="20,4 25,16 38,16 28,24 32,36 20,28 8,36 12,24 2,16 15,16"
                    fill="#FFD600" stroke="#2563eb" stroke-width="2"/>
                </svg>
              </span>
              <span class="collect-text">{{ isFavorite ? '已收藏' : '收藏' }}</span>
              <span v-if="favoriteCount > 0" class="favorite-count">{{ favoriteCount }}</span>
            </button>
            <button class="btn btn-outline-secondary btn-sm rounded-pill" @click="shareProblem" title="分享题目">
              <i class="fa fa-share me-1"></i>分享
            </button>
          </div>
        </div>
      </div>

      <!-- 题目描述 -->
      <div class="problem-description" v-if="problem">
        <h5 class="fw-bold text-dark mb-3">题目描述</h5>
        <div class="problem-content">
          <p>{{ problem.description }}</p>

          <h6 class="fw-bold text-dark mt-4 mb-2">示例：</h6>
          <div v-for="(example, index) in problem.examples" :key="index" class="example-block">
            <div class="example-item">
              <strong>输入：</strong><code>{{ example.input }}</code>
            </div>
            <div class="example-item">
              <strong>输出：</strong><code>{{ example.output }}</code>
            </div>
            <div v-if="example.explanation" class="example-item">
              <strong>解释：</strong>{{ example.explanation }}
            </div>
          </div>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="comments-section">
        <textarea v-model="newComment" class="form-control mb-2" placeholder="写下你的评论..."></textarea>
        <button class="btn btn-primary btn-sm rounded-pill mb-3" @click="addComment">
          <i class="fa fa-plus me-1"></i>发表评论
        </button>
        <div class="comments-list">
          <CommentItem
            v-for="comment in comments"
            :key="comment.commentId"
            :comment="comment"
            @reply="replyComment"
            @like="likeComment"
          />
        </div>
        <BasePagination
          :total="totalComments"
          :page-size="pageSize"
          :current-page="page"
          @change="changePage"
        />
      </div>
    </div>

    <!-- 右侧代码编辑区域 -->
    <div class="code-panel leetcode-style-panel" :class="{ fullscreen: isFullScreen }">
      <!-- 工具栏 -->
      <div class="editor-toolbar leetcode-toolbar">
        <div class="toolbar-left">
          <div class="lang-selector">
            <i class="fa fa-code me-1"></i>
            <span class="lang-label">Python3</span>
            <i class="fa fa-chevron-down ms-1"></i>
          </div>
          <div class="theme-selector">
            <i class="fa fa-palette me-1"></i>
            <select v-model="theme" class="theme-select" @change="savePreferences">
              <option value="vs-dark">暗色主题</option>
              <option value="vs">明亮主题</option>
              <option value="hc-black">高对比度</option>
            </select>
          </div>
          <div class="font-selector">
            <i class="fa fa-font me-1"></i>
            <select v-model="fontSize" class="font-select" @change="savePreferences">
              <option v-for="size in [12,14,16,18,20,22,24]" :key="size" :value="size">{{ size }}px</option>
            </select>
          </div>
        </div>
        <div class="toolbar-right">
          <button class="toolbar-btn" @click="formatCode" :disabled="isRunning" title="格式化代码">
            <i class="fa fa-magic"></i> 格式化
          </button>
          <button class="toolbar-btn" @click="resetCode" :disabled="isRunning" title="重置代码">
            <i class="fa fa-refresh"></i> 重置
          </button>
          <button class="toolbar-btn" @click="insertTemplate" :disabled="isRunning" title="插入模板">
            <i class="fa fa-code"></i> 模板
          </button>
          <button class="toolbar-btn" @click="saveCode" :disabled="isRunning" title="保存代码">
            <i class="fa fa-save"></i> 保存
          </button>
          <div class="toolbar-divider"></div>
          <button v-if="!isFullScreen" class="toolbar-btn" @click="toggleFullScreen" title="全屏模式">
            <i class="fa fa-expand"></i> 全屏
          </button>
          <button v-else class="toolbar-btn" @click="toggleFullScreen" title="退出全屏">
            <i class="fa fa-compress"></i> 退出全屏
          </button>
        </div>
      </div>
      <!-- 编辑器区 -->
      <div class="editor-content leetcode-editor-content" :class="{ fullscreen: isFullScreen }" style="flex: 1; min-height: 0; display: flex; flex-direction: column; position: relative;">
        <MonacoEditor
          v-model="code"
          language="python"
          :theme="theme"
          :font-size="fontSize"
          style="flex: 1; min-height: 300px; width: 100%; height: 100%;"
        />
        <!-- 浮层输入输出区+按钮 -->
        <transition name="fade">
          <div v-show="showResultPanel" class="io-float-panel">
            <div class="result-header">
              <div class="result-title">
                <i class="fa fa-terminal me-2"></i>输入/输出
                <span v-if="isRunning" class="ms-2">
                  <i class="fa fa-spinner fa-spin text-primary"></i> 运行中...
                </span>
              </div>
              <div class="result-actions">
                <button class="btn btn-sm btn-outline-secondary me-2" @click="clearOutput" title="清空输出">
                  <i class="fa fa-trash"></i>
                </button>
                <button class="btn btn-sm btn-outline-secondary me-2" @click="copyOutput" title="复制输出">
                  <i class="fa fa-copy"></i>
                </button>
                <button class="collapse-btn" @click="showResultPanel = false" title="收起面板">
                  <i class="fa fa-angle-down"></i>
                </button>
              </div>
            </div>
            <div class="result-body">
              <div class="io-flex-row">
                <div class="input-area io-half">
                  <label class="input-label">
                    <i class="fa fa-keyboard me-1"></i>自定义输入：
                  </label>
                  <textarea 
                    v-model="input" 
                    class="input-textarea" 
                    placeholder="请输入测试数据..." 
                    :disabled="isRunning"
                    @keydown.ctrl.enter="runCode"
                  ></textarea>
                  <div class="input-hint">
                    <small class="text-muted">
                      <i class="fa fa-info-circle me-1"></i>按 Ctrl+Enter 快速运行
                    </small>
                  </div>
                </div>
                <div class="output-area io-half">
                  <label class="output-label">
                    <i class="fa fa-terminal me-1"></i>输出结果：
                  </label>
                  <div class="output-container">
                    <pre class="output-text" :class="{ 'has-error': result.error }">{{ result.output || '等待运行...' }}</pre>
                    <div v-if="result.error" class="error-indicator">
                      <i class="fa fa-exclamation-triangle text-danger"></i>
                      <span class="error-message">{{ result.error }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="io-btn-row-bottom">
              <div class="btn-group">
                <button class="run-btn" @click="runCode" :disabled="isRunning">
                  <i v-if="isRunning" class="fa fa-spinner fa-spin"></i>
                  <i v-else class="fa fa-play"></i> 运行
                </button>
                <button class="submit-btn" @click="submitCode" :disabled="isRunning">
                  <i v-if="isRunning" class="fa fa-spinner fa-spin"></i>
                  <i v-else class="fa fa-check"></i> 提交
                </button>
              </div>
              <div class="execution-info" v-if="result.executionTime">
                <small class="text-muted">
                  <i class="fa fa-clock me-1"></i>执行时间: {{ result.executionTime }}ms
                </small>
              </div>
            </div>
          </div>
        </transition>
        <!-- 右下角悬浮展开按钮 -->
        <button v-show="!showResultPanel" class="expand-btn-float" @click="showResultPanel = true"><i class="fa fa-angle-up"></i> 输入/输出</button>
      </div>
    </div>
  </div>
</div>
</template>

<script setup>
import MonacoEditor from '../components/MonacoEditor.vue'
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import CommentItem from './components/CommentItem.vue'
import BasePagination from './components/BasePagination.vue'

const code = ref('')
const input = ref('')
const theme = ref('vs-dark')
const fontSize = ref(16)
const isFullScreen = ref(false)
const isRunning = ref(false)
const showResult = ref(false)
const result = ref({ output: '', error: null, executionTime: null })
const problemId = ref('')
const problem = ref(null)
const comments = ref([])
const totalComments = ref(0)
const page = ref(1)
const pageSize = ref(10)
const newComment = ref('')
const isFavorite = ref(false)
const userId = ref(null)
const animating = ref(false)
const favoriteCount = ref(0)
const favoriteLoading = ref(false)
const showResultPanel = ref(true)
const loading = ref(false)

const route = useRoute()

// 加载用户偏好设置
function loadPreferences() {
  const savedTheme = localStorage.getItem('codeEditor_theme')
  const savedFontSize = localStorage.getItem('codeEditor_fontSize')
  if (savedTheme) theme.value = savedTheme
  if (savedFontSize) fontSize.value = parseInt(savedFontSize)
}

// 保存用户偏好设置
function savePreferences() {
  localStorage.setItem('codeEditor_theme', theme.value)
  localStorage.setItem('codeEditor_fontSize', fontSize.value.toString())
}

onMounted(async () => {
  loading.value = true
  loadPreferences()
  
  problemId.value = route.params.id || window.location.pathname.split('/').pop() || ''
  // 从新的用户信息中获取userId
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  userId.value = user ? (user.userId || user.user_id) : null
  
  try {
    await Promise.all([
      loadProblem(),
      loadComments(),
      checkFavorite(),
      loadFavoriteCount()
    ])
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
})

watch(() => route.params.id, async (newId, oldId) => {
  if (newId && newId !== oldId) {
    problemId.value = newId
    // 重新获取用户ID
    const user = JSON.parse(localStorage.getItem('user') || 'null')
    userId.value = user ? (user.userId || user.user_id) : null
    await loadProblem()
    await loadComments()
    await checkFavorite()
    await loadFavoriteCount()
  }
})

async function loadProblem() {
  if (!problemId.value) return
  const res = await axios.get(`/api/practice/problems`)
  const list = res.data
  problem.value = list.find(p => p.id == problemId.value)
  if (problem.value) {
    try {
      const samples = JSON.parse(problem.value.samples || '[]')
      problem.value.examples = samples.map(s => ({
        input: s.input,
        output: s.output,
        explanation: s.explanation || ''
      }))
    } catch {
      problem.value.examples = []
    }
    
    // 先尝试加载保存的代码，如果没有则使用模板
    loadSavedCode()
    console.log('loadSavedCode后的code.value:', code.value) // 调试信息
    
    // 通用代码加载逻辑
    if (!code.value.trim() || code.value.includes('在这里编写你的代码')) {
      // 如果有题目模板，使用题目模板
      if (problem.value && problem.value.template) {
        code.value = problem.value.template
        console.log('使用题目模板:', code.value) // 调试信息
      } else {
        // 使用默认模板
        code.value = '# 在这里编写你的代码\n'
        console.log('使用默认模板:', code.value) // 调试信息
      }
    } else {
      console.log('使用已保存的代码:', code.value) // 调试信息
    }
  }
}

function formatCode() {
  const editor = document.querySelector('.editor-container')?.__editorInstance
  if (editor) {
    editor.getAction('editor.action.formatDocument').run()
  }
}

function resetCode() {
  if (problem.value && problem.value.template) {
    code.value = problem.value.template
  } else {
    code.value = ''
  }
}

function insertTemplate() {
  if (problem.value && problem.value.template) {
    code.value = problem.value.template
  } else {
    code.value = '# 在这里编写你的代码\n'
  }
}

// 保存代码到本地存储
function saveCode() {
  if (problemId.value && code.value) {
    localStorage.setItem(`code_${problemId.value}`, code.value)
    showToast('代码已保存到本地', 'success')
  }
}

// 加载保存的代码
function loadSavedCode() {
  if (problemId.value) {
    const savedCode = localStorage.getItem(`code_${problemId.value}`)
    if (savedCode && savedCode !== code.value) {
      code.value = savedCode
    }
  }
}

// 清空输出
function clearOutput() {
  result.value = { output: '', error: null, executionTime: null }
}

// 复制输出到剪贴板
async function copyOutput() {
  try {
    await navigator.clipboard.writeText(result.value.output || '')
    showToast('输出已复制到剪贴板', 'success')
  } catch (error) {
    console.error('复制失败:', error)
    showToast('复制失败', 'error')
  }
}

// 分享题目
function shareProblem() {
  const url = `${window.location.origin}/problem/${problemId.value}`
  if (navigator.share) {
    navigator.share({
      title: problem.value?.title || 'Python题目',
      url: url
    })
  } else {
    navigator.clipboard.writeText(url).then(() => {
      showToast('链接已复制到剪贴板', 'success')
    })
  }
}

// 显示提示消息
function showToast(message, type = 'info') {
  // 简单的提示实现，可以替换为更完善的toast组件
  const toast = document.createElement('div')
  toast.className = `toast toast-${type}`
  toast.textContent = message
  toast.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: ${type === 'success' ? '#28a745' : type === 'error' ? '#dc3545' : '#17a2b8'};
    color: white;
    padding: 12px 20px;
    border-radius: 4px;
    z-index: 10000;
    animation: slideIn 0.3s ease;
  `
  document.body.appendChild(toast)
  setTimeout(() => {
    toast.remove()
  }, 3000)
}

function toggleFullScreen() {
  isFullScreen.value = !isFullScreen.value
}



async function runCode() {
  console.log('runCode function called') // 调试信息
  console.log('code.value:', code.value) // 调试信息
  console.log('code.value.trim():', code.value.trim()) // 调试信息
  console.log('!code.value.trim():', !code.value.trim()) // 调试信息
  
  if (!code.value.trim()) {
    console.log('Code is empty, showing error toast') // 调试信息
    showToast('请先输入代码', 'error')
    return
  }
  
  console.log('Code to run:', code.value) // 调试信息
  console.log('Input:', input.value) // 调试信息
  
  isRunning.value = true
  showResult.value = true
  showResultPanel.value = true
  result.value = { output: '', error: null, executionTime: null }
  
  const startTime = Date.now()
  
  try {
    console.log('Sending request to /api/judge/run') // 调试信息
    console.log('Request payload:', { code: code.value, language: 'python', input: input.value }) // 调试信息
    
    const requestData = {
      code: code.value,
      language: 'python',
      input: input.value
    }
    
    console.log('Full URL:', axios.defaults.baseURL + '/api/judge/run') // 调试信息
    
    const response = await axios.post('/api/judge/run', requestData)
    
    const data = response.data
    const executionTime = Date.now() - startTime
    
    // 兼容 output 字段和 stdout 字段
    let output = data.output || data.stdout || ''
    let error = null
    
    if (data.stderr) {
      error = data.stderr
      output += (output ? '\n' : '') + '错误信息：' + data.stderr
    }
    
    result.value = {
      output: output,
      error: error,
      executionTime: executionTime
    }
    
    // 自动保存代码
    saveCode()
    
  } catch (err) {
    const executionTime = Date.now() - startTime
    result.value = {
      output: '运行失败：' + (err.response?.data?.message || err.message),
      error: err.response?.data?.message || err.message,
      executionTime: executionTime
    }
    showToast('运行失败，请检查代码', 'error')
  } finally {
    isRunning.value = false
  }
}

async function submitCode() {
  console.log('submitCode function called') // 调试信息
  
  if (!code.value.trim()) {
    showToast('请先输入代码', 'error')
    return
  }
  
  if (!userId.value) {
    showToast('请先登录后再提交', 'error')
    return
  }
  
  console.log('Code to submit:', code.value) // 调试信息
  console.log('User ID:', userId.value) // 调试信息
  
  isRunning.value = true
  showResult.value = true
  showResultPanel.value = true
  result.value = { output: '', error: null, executionTime: null }
  
  const startTime = Date.now()
  
  try {
    // 组装测试用例
    const testCases = (problem.value?.examples || []).map(e => ({
      input: e.input,
      expected: e.output
    }))
    
    console.log('Sending request to /api/judge/batch-judge') // 调试信息
    console.log('Request payload:', { code: code.value, language: 'python', testCases }) // 调试信息
    
    const requestData = {
      code: code.value,
      language: 'python',
      userId: userId.value,
      problemId: parseInt(problemId.value.replace('P', '')), // 移除P前缀
      testCases
    }
    
    console.log('Full URL:', axios.defaults.baseURL + '/api/judge/batch-judge') // 调试信息
    
    const response = await axios.post('/api/judge/batch-judge', requestData)
    
    const cases = response.data
    const executionTime = Date.now() - startTime
    
    let output = ''
    let passedCount = 0
    let totalCount = cases.length
    
    cases.forEach((c, idx) => {
      // 兼容 output/actual/stdout 字段
      let actual = c.output || c.actual || c.stdout || ''
      const passed = c.passed
      if (passed) passedCount++
      
      output += `用例${idx + 1}: 输入：${c.input}\n期望输出：${c.expected}\n实际输出：${actual}\n${passed ? '✅通过' : '❌未通过'}\n`
      if (c.stderr) output += '错误信息：' + c.stderr + '\n'
      output += '\n'
    })
    
    // 添加总结
    output += `\n=== 提交结果 ===\n通过用例：${passedCount}/${totalCount}\n执行时间：${executionTime}ms\n`
    
    if (passedCount === totalCount) {
      output += '🎉 恭喜！所有用例都通过了！'
      showToast('提交成功！所有用例通过', 'success')
      
      // 保存提交记录
      await saveSubmissionRecord(true)
    } else {
      output += '❌ 还有用例未通过，请检查代码'
      showToast(`提交完成，通过 ${passedCount}/${totalCount} 个用例`, 'info')
      
      // 保存提交记录
      await saveSubmissionRecord(false)
    }
    
    result.value = {
      output: output,
      error: null,
      executionTime: executionTime
    }
    
    // 自动保存代码
    saveCode()
    
  } catch (err) {
    const executionTime = Date.now() - startTime
    result.value = {
      output: '判题失败：' + (err.response?.data?.message || err.message),
      error: err.response?.data?.message || err.message,
      executionTime: executionTime
    }
    showToast('判题失败，请稍后重试', 'error')
  } finally {
    isRunning.value = false
  }
}

// 保存提交记录
async function saveSubmissionRecord(passed) {
  if (!userId.value || !problemId.value) return
  
  try {
    await axios.post('/api/user-problem-record', {
      userId: userId.value,
      problemId: problemId.value,
      code: code.value,
      result: passed ? '通过' : '未通过',
      passRate: passed ? 100 : 0,
      usedTime: result.value.executionTime || 0,
      usedMemory: 0,
      language: 'python'
    })
  } catch (error) {
    console.error('保存提交记录失败:', error)
  }
}

async function loadComments() {
  const res = await axios.get(`/api/practice/problem/${problemId.value}/comments`, {
    params: { page: page.value, pageSize: pageSize.value }
  })
  comments.value = buildCommentTree(res.data.comments)
  totalComments.value = res.data.total
}

function buildCommentTree(list) {
  const map = {}
  list.forEach(c => { c.children = []; map[c.commentId] = c })
  const tree = []
  list.forEach(c => {
    if (c.parentId) {
      map[c.parentId]?.children.push(c)
    } else {
      tree.push(c)
    }
  })
  return tree
}

async function addComment() {
  if (!userId.value) {
    alert('请先登录后再评论！')
    window.location.href = '/auth'
    return
  }
  if (!newComment.value.trim()) return
  await axios.post(`/api/practice/problem/${problemId.value}/comments`, {
    userId: userId.value,
    content: newComment.value,
    parentId: null
  })
  newComment.value = ''
  await loadComments()
}

async function replyComment(parentId, content) {
  if (!userId.value) {
    alert('请先登录后再回复！')
    window.location.href = '/auth'
    return
  }
  await axios.post(`/api/practice/problem/${problemId.value}/comments`, {
    userId: userId.value,
    content,
    parentId
  })
  await loadComments()
}

async function likeComment(commentId) {
  await axios.post(`/api/practice/comment/${commentId}/like`)
  await loadComments()
}

function changePage(p) {
  page.value = p
  loadComments()
}

async function checkFavorite() {
  if (!userId.value) {
    isFavorite.value = false
    return
  }
  const res = await axios.get(`/api/practice/problem/${problemId.value}/favorite`, { params: { userId: userId.value } })
  isFavorite.value = res.data
}

async function loadFavoriteCount() {
  try {
    const res = await axios.get(`/api/practice/problem/${problemId.value}/favorite/count`)
    favoriteCount.value = res.data
  } catch {
    favoriteCount.value = 0
  }
}

async function handleFavoriteClick() {
  if (favoriteLoading.value) return
  if (!userId.value) {
    alert('请先登录后再收藏题目！')
    window.location.href = '/auth'
    return
  }
  favoriteLoading.value = true
  await toggleFavorite()
  await checkFavorite()
  favoriteLoading.value = false
  animating.value = true
  setTimeout(() => { animating.value = false }, 350)
}

async function toggleFavorite() {
  if (isFavorite.value) {
    await axios.delete(`/api/practice/problem/${problemId.value}/favorite`, { params: { userId: userId.value } })
    favoriteCount.value = Math.max(0, favoriteCount.value - 1)
  } else {
    await axios.post(`/api/practice/problem/${problemId.value}/favorite`, null, { params: { userId: userId.value } })
    favoriteCount.value = favoriteCount.value + 1
  }
  await checkFavorite()
  await loadFavoriteCount()
}
</script>

<style scoped>
.code-practice-container {
  height: calc(100vh - 60px); /* 顶部导航栏高度60px */
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.practice-main {
  flex: 1;
  display: flex;
  overflow: hidden;
  min-height: 0;
}

.problem-panel {
  width: 45%;
  background: #f8f9fa;
  overflow-y: auto;
  padding: 16px;
  border-right: 1px solid #dee2e6;
}

.code-panel {
  width: 55%;
  display: flex;
  flex-direction: column;
  background: #fff;
  min-height: 0;
}

.problem-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.problem-description {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.problem-content {
  line-height: 1.6;
}

.problem-content code {
  background: #f1f3f4;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.example-block {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 15px;
  margin: 10px 0;
}

.example-item {
  margin-bottom: 8px;
}

.example-item:last-child {
  margin-bottom: 0;
}

.comments-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #e9ecef;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-content {
  line-height: 1.5;
}

.comment-actions {
  color: #6c757d;
}

.editor-header {
  padding: 15px 20px;
  border-bottom: 1px solid #dee2e6;
  background: #f8f9fa;
}

.editor-toolbar {
  padding: 10px 20px;
  border-bottom: 1px solid #dee2e6;
  background: #f8f9fa;
}

.editor-content {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: 8px;
}

.code-textarea {
  height: 100%;
  min-height: 400px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  resize: none;
}

.result-panel {
  margin-top: 2px;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  border: 1.5px solid #e0e7ef;
  overflow: hidden;
  transition: max-height 0.3s cubic-bezier(.4,0,.2,1), opacity 0.3s;
  max-height: 300px;
  opacity: 1;
}

.result-header {
  padding: 12px 20px 8px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e0e7ef;
}

.result-title {
  font-weight: bold;
  font-size: 1.1rem;
}

.collapse-btn {
  background: none;
  border: none;
  font-size: 1.3rem;
  cursor: pointer;
  color: #2563eb;
  transition: color 0.18s;
}

.collapse-btn:hover {
  color: #1746a2;
}

.result-body {
  padding: 2px 8px 2px 8px;
}

.io-flex-row {
  display: flex;
  flex-direction: row;
  gap: 24px;
}

.io-half {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.input-area {
  margin-bottom: 20px;
}

.input-textarea {
  width: 100%;
  height: 48px;
  padding: 10px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  resize: none;
  margin-top: 4px;
}

.output-area {
  margin-bottom: 20px;
}

.output-text {
  background: #fff;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  padding: 10px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.4;
  white-space: pre-wrap;
  margin: 4px 0 0 0;
  min-height: 48px;
}

.leetcode-btn-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.run-btn,
.submit-btn {
  padding: 10px 20px;
  border: none;
  background: none;
  cursor: pointer;
  transition: color 0.2s;
}

.run-btn {
  margin-right: 10px;
}

.run-btn:hover,
.submit-btn:hover {
  color: #00b4ff;
}

.run-btn:disabled,
.submit-btn:disabled {
  color: #6c757d;
}

.run-btn i,
.submit-btn i {
  margin-right: 5px;
}

.run-btn:disabled i,
.submit-btn:disabled i {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 1000;
  background: #fff;
  box-shadow: 0 0 0 100vw rgba(0,0,0,0.12);
  border-radius: 0;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.io-btn-row {
  display: flex;
  justify-content: flex-end;
  margin: 16px 0 0 0;
  gap: 12px;
}

.io-btn-row-right {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 8px;
}

.io-btn-row-bottom {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
}

.expand-btn {
  margin: 16px auto 0 auto;
  display: flex;
  align-items: center;
  background: #2563eb;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  padding: 8px 20px;
  cursor: pointer;
  box-shadow: 0 1px 4px #e0e0e0;
  transition: background 0.18s, color 0.18s;
}

.expand-btn:hover {
  background: #1746a2;
  color: #ffe066;
}

.slide-fade-enter-active, .slide-fade-leave-active {
  transition: max-height 0.3s cubic-bezier(.4,0,.2,1), opacity 0.3s;
}

.slide-fade-enter-from, .slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
}

/* 大号星星收藏按钮 */
.star-favorite-btn {
  border: none;
  background: none;
  display: flex;
  align-items: center;
  font-size: 1.6rem;
  color: #888;
  cursor: pointer;
  transition: color 0.2s;
  padding: 0;
  outline: none;
}

.star-favorite-btn .fa-star {
  font-size: 2.2rem;
  margin-right: 0.2em;
  transition: color 0.2s;
}

.star-favorite-btn .star-count {
  font-size: 1.3rem;
  font-weight: 500;
  transition: color 0.2s;
}

.star-favorite-btn.active,
.star-favorite-btn.active .fa-star,
.star-favorite-btn.active .star-count {
  color: #00b4ff;
}

.star-favorite-btn:active {
  transform: scale(0.96);
}

.favorite-btn-style {
  min-width: 90px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.13rem;
  font-weight: 500;
  gap: 2px;
  transition: background 0.18s, color 0.18s, border 0.18s, box-shadow 0.18s, transform 0.12s;
  user-select: none;
}

.favorite-btn-style:active {
  transform: scale(0.96);
}

.favorite-btn-solid {
  background: #2563eb !important;
  color: #fff !important;
  border: none !important;
}

.favorite-btn-solid:hover {
  background: #1746a2 !important;
  color: #ffe066 !important;
}

.favorite-btn-outline {
  background: #fff !important;
  color: #2563eb !important;
  border: 1.5px solid #2563eb !important;
}

.favorite-btn-outline:hover {
  background: #f4f8ff !important;
  color: #2563eb !important;
  border-color: #2563eb !important;
}

.star-icon {
  font-size: 1.35em;
  transition: color 0.18s;
  margin-right: 0.18em;
}

.collect-btn {
  display: flex;
  align-items: center;
  border: 1.5px solid #2563eb;
  background: #fff;
  color: #2563eb;
  border-radius: 18px;
  padding: 6px 18px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.25s, color 0.25s, border 0.25s;
  outline: none;
  min-width: 80px;
  min-height: 38px;
  box-shadow: 0 1px 4px #e0e0e0;
  position: relative;
  overflow: hidden;
}

.collect-btn .star-icon {
  display: flex;
  align-items: center;
  margin-right: 8px;
  transition: transform 0.25s;
}

.collect-btn .collect-text {
  font-size: 1.1rem;
  font-weight: 500;
  letter-spacing: 1px;
  transition: color 0.25s;
}

.collect-btn.collected {
  background: #2563eb;
  color: #fff;
  border: 1.5px solid #2563eb;
}

.collect-btn.collected .collect-text {
  color: #fff;
}

.collect-btn:active {
  transform: scale(0.97);
}

.collect-btn.animating {
  animation: btn-collect-ani 0.35s;
}

@keyframes btn-collect-ani {
  0% { transform: scale(1); }
  30% { transform: scale(1.15); }
  60% { transform: scale(0.95); }
  100% { transform: scale(1); }
}

.leetcode-style-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.leetcode-toolbar {
  padding: 10px 20px;
  border-bottom: 1px solid #dee2e6;
  background: #f8f9fa;
}

.toolbar-left {
  display: flex;
  align-items: center;
}

.lang-label {
  font-weight: bold;
  margin-right: 10px;
}

.theme-select,
.font-select {
  margin-left: 10px;
}

.toolbar-right {
  display: flex;
  align-items: center;
}

.toolbar-btn {
  margin-left: 10px;
  padding: 5px 10px;
  border: none;
  background: none;
  cursor: pointer;
  transition: color 0.2s;
}

.leetcode-editor-content {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: 8px;
}

.io-panel {
  padding: 20px;
  border-top: 1px solid #dee2e6;
  background: #f8f9fa;
}

.input-textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  resize: none;
}

.output-area {
  margin-bottom: 20px;
}

.output-text {
  background: #fff;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  padding: 10px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.4;
  white-space: pre-wrap;
  margin: 0;
}

.leetcode-btn-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.run-btn,
.submit-btn {
  padding: 10px 20px;
  border: none;
  background: none;
  cursor: pointer;
  transition: color 0.2s;
}

.run-btn {
  margin-right: 10px;
}

.run-btn:hover,
.submit-btn:hover {
  color: #00b4ff;
}

.run-btn:disabled,
.submit-btn:disabled {
  color: #6c757d;
}

.run-btn i,
.submit-btn i {
  margin-right: 5px;
}

.run-btn:disabled i,
.submit-btn:disabled i {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 1000;
  background: #fff;
  box-shadow: 0 0 0 100vw rgba(0,0,0,0.12);
  border-radius: 0;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.io-flex-row {
  display: flex;
  flex-direction: row;
  gap: 24px;
}

.io-half {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.io-btn-row {
  display: flex;
  justify-content: flex-end;
  margin: 16px 0 0 0;
  gap: 12px;
}

.input-textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  border: 1.5px solid #dee2e6;
  border-radius: 8px;
  resize: none;
  font-size: 15px;
  background: #fff;
  margin-top: 8px;
  transition: border 0.2s;
}

.input-textarea:focus {
  border-color: #2563eb;
  outline: none;
}

.output-area {
  margin-left: 0;
}

.output-text {
  background: #fff;
  border: 1.5px solid #dee2e6;
  border-radius: 8px;
  padding: 10px;
  font-family: 'Courier New', monospace;
  font-size: 15px;
  line-height: 1.4;
  white-space: pre-wrap;
  margin: 8px 0 0 0;
  min-height: 100px;
}

.run-btn, .submit-btn {
  padding: 10px 24px;
  border: none;
  background: #2563eb;
  color: #fff;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s, transform 0.12s;
  box-shadow: 0 1px 4px #e0e0e0;
  outline: none;
  display: flex;
  align-items: center;
  gap: 6px;
}

.run-btn:active, .submit-btn:active {
  transform: scale(0.96);
}

.run-btn:disabled, .submit-btn:disabled {
  background: #bfc8e2;
  color: #fff;
  cursor: not-allowed;
}

.run-btn:hover:not(:disabled), .submit-btn:hover:not(:disabled) {
  background: #1746a2;
  color: #ffe066;
}

.io-btn-row-right {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 8px;
}

.leetcode-result-panel {
  margin-top: 2px;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  border: 1.5px solid #e0e7ef;
  overflow: hidden;
  transition: max-height 0.4s cubic-bezier(.4,0,.2,1), opacity 0.3s;
  max-height: 180px;
  opacity: 1;
}

.result-body {
  padding: 2px 8px 2px 8px;
}

.input-textarea {
  height: 48px;
  margin-top: 4px;
}

.output-text {
  min-height: 48px;
  margin: 4px 0 0 0;
}

.run-btn, .submit-btn {
  padding: 10px 24px;
  border: none;
  background: #2563eb;
  color: #fff;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s, transform 0.12s;
  box-shadow: 0 1px 4px #e0e0e0;
  outline: none;
  display: flex;
  align-items: center;
  gap: 6px;
}

.run-btn:active, .submit-btn:active {
  transform: scale(0.96);
}

.run-btn:disabled, .submit-btn:disabled {
  background: #bfc8e2;
  color: #fff;
  cursor: not-allowed;
}

.run-btn:hover:not(:disabled), .submit-btn:hover:not(:disabled) {
  background: #1746a2;
  color: #ffe066;
}

.io-btn-row-right {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 8px;
}

.io-btn-row-bottom {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
}

.expand-btn {
  margin: 16px auto 0 auto;
  display: flex;
  align-items: center;
  background: #2563eb;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  padding: 8px 20px;
  cursor: pointer;
  box-shadow: 0 1px 4px #e0e0e0;
  transition: background 0.18s, color 0.18s;
}

.expand-btn:hover {
  background: #1746a2;
  color: #ffe066;
}

.slide-fade-enter-active, .slide-fade-leave-active {
  transition: max-height 0.3s cubic-bezier(.4,0,.2,1), opacity 0.3s;
}

.slide-fade-enter-from, .slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
}

.io-float-panel {
  position: absolute;
  left: 0; right: 0; bottom: 0;
  background: #fff;
  box-shadow: 0 -2px 16px rgba(0,0,0,0.08);
  border-radius: 12px 12px 0 0;
  z-index: 10;
  padding: 16px;
  transition: box-shadow 0.2s;
}
.expand-btn-float {
  position: absolute;
  right: 24px;
  bottom: 24px;
  z-index: 11;
  background: #2563eb;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 500;
  padding: 8px 20px;
  box-shadow: 0 1px 4px #e0e0e0;
  outline: none;
  display: flex;
  align-items: center;
  gap: 6px;
}
.expand-btn-float:hover {
  background: #1746a2;
  color: #ffe066;
}

/* 加载状态 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
}

/* 工具栏优化 */
.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
  gap: 16px;
}

.toolbar-left, .toolbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.lang-selector, .theme-selector, .font-selector {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  background: white;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  font-size: 14px;
  color: #495057;
}

.toolbar-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: white;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  font-size: 14px;
  color: #495057;
  cursor: pointer;
  transition: all 0.2s;
}

.toolbar-btn:hover {
  background: #e9ecef;
  border-color: #adb5bd;
}

.toolbar-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.toolbar-divider {
  width: 1px;
  height: 24px;
  background: #dee2e6;
  margin: 0 8px;
}

/* 输入输出区域优化 */
.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

.result-title {
  font-weight: 600;
  color: #495057;
  display: flex;
  align-items: center;
}

.result-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.input-label, .output-label {
  font-weight: 600;
  color: #495057;
  margin-bottom: 8px;
  display: block;
}

.input-textarea {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  resize: vertical;
}

.output-container {
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  padding: 12px;
  min-height: 80px;
}

.output-text {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.5;
  margin: 0;
  white-space: pre-wrap;
  word-break: break-all;
}

.output-text.has-error {
  color: #dc3545;
}

.error-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  padding: 8px;
  background: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
  color: #721c24;
}

.input-hint {
  margin-top: 4px;
}

.execution-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 收藏按钮优化 */
.collect-btn {
  position: relative;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: white;
  border: 2px solid #dee2e6;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  font-weight: 500;
}

.collect-btn:hover {
  border-color: #2563eb;
  background: #f8f9ff;
}

.collect-btn.collected {
  border-color: #ffd600;
  background: #fffbf0;
  color: #856404;
}

.collect-btn.animating {
  animation: collectPulse 0.6s ease;
}

.favorite-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #dc3545;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
}

/* 动画效果 */
@keyframes collectPulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* Toast 样式 */
.toast {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 6px;
  font-weight: 500;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .practice-main {
    flex-direction: column;
  }
  
  .problem-panel, .code-panel {
    width: 100%;
  }
  
  .toolbar-left, .toolbar-right {
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .io-flex-row {
    flex-direction: column;
  }
  
  .io-half {
    width: 100%;
  }
}
</style> 