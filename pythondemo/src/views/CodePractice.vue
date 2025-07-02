<template>
<div class="code-practice-container">
  <!-- 主要内容区域 -->
  <div class="practice-main">
    <!-- 左侧题目区域 -->
    <div class="problem-panel">
      <!-- 题目头部 -->
      <div class="problem-header">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <div>
            <h4 class="fw-bold text-dark mb-1">{{ problem ? problem.title : '加载中...' }}</h4>
            <div class="d-flex align-items-center gap-2">
              <span v-if="problem" :class="['badge', 
                problem.difficulty === '简单' ? 'bg-success' : 
                problem.difficulty === '中等' ? 'bg-warning text-dark' : 'bg-danger']">
                {{ problem.difficulty }}
              </span>
              <span class="text-muted small">通过率: 52.3%</span>
              <span class="text-muted small">提交次数: 2,456,789</span>
            </div>
          </div>
          <div class="d-flex gap-2">
            <button
              class="collect-btn"
              :class="{ collected: isFavorite, animating: animating }"
              @click="handleFavoriteClick"
              :disabled="favoriteLoading"
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
            </button>
            <button class="btn btn-outline-secondary btn-sm rounded-pill">
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
    <div class="code-panel">
      <!-- 代码编辑器头部 -->
      <div class="editor-header">
        <div class="d-flex justify-content-between align-items-center">
          <div class="d-flex align-items-center gap-3">
            <span class="fw-medium text-dark">Python3</span>
            <div class="dropdown">
              <button class="btn btn-outline-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown">
                主题
              </button>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#">默认主题</a></li>
                <li><a class="dropdown-item" href="#">暗色主题</a></li>
                <li><a class="dropdown-item" href="#">高对比度</a></li>
              </ul>
            </div>
          </div>
          <div class="d-flex gap-2">
            <button class="btn btn-outline-secondary btn-sm rounded-pill">
              <i class="fa fa-cog me-1"></i>设置
            </button>
            <button class="btn btn-outline-secondary btn-sm rounded-pill">
              <i class="fa fa-expand me-1"></i>全屏
            </button>
          </div>
        </div>
      </div>

      <!-- 代码编辑器 -->
      <div class="code-editor">
        <div class="editor-toolbar">
          <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex gap-2">
              <button class="btn btn-outline-primary btn-sm" @click="runCode" :disabled="isRunning">
                <i v-if="isRunning" class="fa fa-spinner fa-spin me-1"></i>
                <i v-else class="fa fa-play me-1"></i>
                {{ isRunning ? '运行中...' : '运行' }}
              </button>
              <button class="btn btn-outline-success btn-sm" @click="submitCode" :disabled="isRunning">
                <i v-if="isRunning" class="fa fa-spinner fa-spin me-1"></i>
                <i v-else class="fa fa-check me-1"></i>
                {{ isRunning ? '提交中...' : '提交' }}
              </button>
            </div>
            <div class="d-flex gap-2">
              <button class="btn btn-outline-secondary btn-sm" @click="resetCode" :disabled="isRunning">
                <i class="fa fa-refresh me-1"></i>重置
              </button>
              <button class="btn btn-outline-secondary btn-sm" @click="formatCode" :disabled="isRunning">
                <i class="fa fa-magic me-1"></i>格式化
              </button>
              <button class="btn btn-outline-info btn-sm" @click="insertTemplate" :disabled="isRunning">
                <i class="fa fa-code me-1"></i>模板
              </button>
            </div>
          </div>
        </div>

        <!-- 代码编辑区域 -->
        <div class="editor-content">
          <textarea 
            v-model="code" 
            class="form-control code-textarea"
            placeholder="在这里编写你的代码..."
            @keydown.tab.prevent="handleTab"
          ></textarea>
        </div>
      </div>

      <!-- 运行结果 -->
      <div class="result-panel" v-if="showResult">
        <div class="result-header">
          <div class="d-flex justify-content-between align-items-center">
            <h6 class="fw-bold text-dark mb-0">运行结果</h6>
            <button class="btn btn-outline-secondary btn-sm" @click="clearResult">
              <i class="fa fa-times"></i>
            </button>
          </div>
        </div>
        
        <div class="result-content">
          <div v-if="result.status === 'success'" class="alert alert-success">
            <i class="fa fa-check-circle me-2"></i>
            执行成功
          </div>
          <div v-else-if="result.status === 'error'" class="alert alert-danger">
            <i class="fa fa-exclamation-circle me-2"></i>
            执行错误
          </div>
          
          <div class="result-output">
            <h6 class="fw-bold text-dark mb-2">输出：</h6>
            <pre class="output-text">{{ result.output }}</pre>
          </div>
          
          <div v-if="result.testCases" class="test-cases">
            <h6 class="fw-bold text-dark mb-2">测试用例：</h6>
            <div v-for="(testCase, index) in result.testCases" :key="index" class="test-case">
              <div class="d-flex align-items-center mb-1">
                <span class="fw-medium">测试用例 {{ index + 1 }}：</span>
                <span :class="['badge ms-2', testCase.passed ? 'bg-success' : 'bg-danger']">
                  {{ testCase.passed ? '通过' : '失败' }}
                </span>
              </div>
              <div class="test-details">
                <div><strong>输入：</strong>{{ testCase.input }}</div>
                <div><strong>期望输出：</strong>{{ testCase.expected }}</div>
                <div><strong>实际输出：</strong>{{ testCase.actual }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import CodeRunner from '../services/codeRunner.js';
import axios from 'axios';
import CommentItem from './components/CommentItem.vue';
import BasePagination from './components/BasePagination.vue';

export default {
  name: 'CodePractice',
  components: { CommentItem, BasePagination },
  data() {
    return {
      problemId: '',
      problem: null,
      code: '',
      showResult: false,
      result: {
        status: '',
        output: '',
        testCases: []
      },
      comments: [],
      totalComments: 0,
      page: 1,
      pageSize: 10,
      newComment: '',
      isRunning: false,
      isFavorite: false,
      userId: null, // 初始为 null，登录后赋值
      animating: false,
      favoriteCount: 0,
      favoriteLoading: false
    }
  },
  async mounted() {
    // 从路由参数获取题目ID
    this.problemId = this.$route.params.id || this.$route.query.id;
    // 获取当前登录用户id
    this.userId = localStorage.getItem('userId');
    await this.loadProblem();
    await this.loadComments();
    await this.checkFavorite();
    await this.loadFavoriteCount();
  },
  methods: {
    goBackToList() {
    this.$router.push({ path: '/problems', query: { highlight: this.problemId } });
    },
 
    async loadProblem() {
      // 从后端获取题目信息
      if (!this.problemId) return;
      const res = await axios.get(`/api/practice/problems`);
      const list = res.data;
      this.problem = list.find(p => p.id == this.problemId);
      if (this.problem) {
        // 解析samples为示例
        try {
          const samples = JSON.parse(this.problem.samples || '[]');
          this.problem.examples = samples.map(s => ({
            input: s.input,
            output: s.output,
            explanation: s.explanation || ''
          }));
        } catch {
          this.problem.examples = [];
        }
        this.code = '';
      }
    },
    
    async runCode() {
      if (!this.code.trim()) {
        alert('请先编写代码');
        return;
      }
      
      this.isRunning = true;
      this.showResult = true;
      
      try {
        const response = await CodeRunner.runCode(this.problemId, this.code);
        
        if (response.success) {
          this.result = response.result;
        } else {
          this.result = {
            status: 'error',
            output: `执行错误: ${response.error}`,
            testCases: []
          };
        }
      } catch (error) {
        this.result = {
          status: 'error',
          output: `系统错误: ${error.message}`,
          testCases: []
        };
      } finally {
        this.isRunning = false;
      }
    },
    
    async submitCode() {
      if (!this.code.trim()) {
        alert('请先编写代码');
        return;
      }
      
      this.isRunning = true;
      this.showResult = true;
      
      try {
        const response = await CodeRunner.runCode(this.problemId, this.code);
        
        if (response.success) {
          const result = response.result;
          const allPassed = result.testCases.every(test => test.passed);
          
          if (allPassed) {
            this.result = {
              ...result,
              status: 'success',
              output: result.output + '\n\n🎉 恭喜！所有测试用例都通过了！'
            };
            // 这里可以添加提交成功的逻辑，比如更新用户进度
          } else {
            this.result = {
              ...result,
              status: 'error',
              output: result.output + '\n\n❌ 部分测试用例未通过，请检查代码逻辑。'
            };
          }
        } else {
          this.result = {
            status: 'error',
            output: `提交失败: ${response.error}`,
            testCases: []
          };
        }
      } catch (error) {
        this.result = {
          status: 'error',
          output: `系统错误: ${error.message}`,
          testCases: []
        };
      } finally {
        this.isRunning = false;
      }
    },
    
    resetCode() {
      if (this.problem) {
        this.code = this.problem.template;
      } else {
        this.code = `class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        # 在这里编写你的代码
        pass`;
      }
      this.showResult = false;
    },
    
    formatCode() {
      const validation = CodeRunner.validateCode(this.code);
      if (!validation.valid) {
        alert('代码有语法错误:\n' + validation.errors.join('\n'));
        return;
      }
      
      this.code = CodeRunner.formatCode(this.code);
    },
    
    clearResult() {
      this.showResult = false;
    },
    
    handleTab(e) {
      // 处理Tab键缩进
      e.preventDefault();
      const start = e.target.selectionStart;
      const end = e.target.selectionEnd;
      this.code = this.code.substring(0, start) + '    ' + this.code.substring(end);
      this.$nextTick(() => {
        e.target.selectionStart = e.target.selectionEnd = start + 4;
      });
    },
    
    // 快捷操作
    insertTemplate() {
      if (this.problemId === 1) {
        this.code = `class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        # 使用哈希表存储数字和索引
        num_map = {}
        
        for i, num in enumerate(nums):
            complement = target - num
            if complement in num_map:
                return [num_map[complement], i]
            num_map[num] = i
        
        return []  # 如果没有找到解

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [2, 7, 11, 15]
    target1 = 9
    result1 = solution.twoSum(nums1, target1)
    print(f"测试用例1: nums={nums1}, target={target1}")
    print(f"结果: {result1}")
    print(f"验证: {nums1[result1[0]]} + {nums1[result1[1]]} = {nums1[result1[0]] + nums1[result1[1]]}")
    print()
    
    # 测试用例2
    nums2 = [3, 2, 4]
    target2 = 6
    result2 = solution.twoSum(nums2, target2)
    print(f"测试用例2: nums={nums2}, target={target2}")
    print(f"结果: {result2}")
    print(f"验证: {nums2[result2[0]]} + {nums2[result2[1]]} = {nums2[result2[0]] + nums2[result2[1]]}")
    print()
    
    # 测试用例3
    nums3 = [3, 3]
    target3 = 6
    result3 = solution.twoSum(nums3, target3)
    print(f"测试用例3: nums={nums3}, target={target3}")
    print(f"结果: {result3}")
    print(f"验证: {nums3[result3[0]]} + {nums3[result3[1]]} = {nums3[result3[0]] + nums3[result3[1]]}")`;
      }
    },
    async loadComments() {
      const res = await axios.get(`/api/practice/problem/${this.problemId}/comments`, {
        params: { page: this.page, pageSize: this.pageSize }
      });
      this.comments = this.buildCommentTree(res.data.comments);
      this.totalComments = res.data.total;
    },
    buildCommentTree(list) {
      const map = {};
      list.forEach(c => { c.children = []; map[c.commentId] = c; });
      const tree = [];
      list.forEach(c => {
        if (c.parentId) {
          map[c.parentId]?.children.push(c);
        } else {
          tree.push(c);
        }
      });
      return tree;
    },
    async addComment() {
      if (!this.userId) {
        alert('请先登录后再评论！');
        this.$router.push('/login');
        return;
      }
      if (!this.newComment.trim()) return;
      await axios.post(`/api/practice/problem/${this.problemId}/comments`, {
        userId: this.userId,
        content: this.newComment,
        parentId: null
      });
      this.newComment = '';
      await this.loadComments();
    },
    async replyComment(parentId, content) {
      if (!this.userId) {
        alert('请先登录后再回复！');
        this.$router.push('/login');
        return;
      }
      await axios.post(`/api/practice/problem/${this.problemId}/comments`, {
        userId: this.userId,
        content,
        parentId
      });
      await this.loadComments();
    },
    async likeComment(commentId) {
      await axios.post(`/api/practice/comment/${commentId}/like`);
      await this.loadComments();
    },
    changePage(page) {
      this.page = page;
      this.loadComments();
    },
    async checkFavorite() {
      if (!this.userId) {
        this.isFavorite = false;
        return;
      }
      const res = await axios.get(`/api/practice/problem/${this.problemId}/favorite`, { params: { userId: this.userId } });
      console.log('收藏状态接口返回', res.data);
      this.isFavorite = res.data;
    },
    async loadFavoriteCount() {
      // 假设后端有接口 /api/practice/problem/{id}/favorite/count 返回数字
      try {
        const res = await axios.get(`/api/practice/problem/${this.problemId}/favorite/count`);
        this.favoriteCount = res.data;
      } catch {
        this.favoriteCount = 0;
      }
    },
    async handleFavoriteClick() {
      if (this.favoriteLoading) return;
      if (!this.userId) {
        alert('请先登录后再收藏题目！');
        this.$router.push('/login');
        return;
      }
      this.favoriteLoading = true;
      await this.toggleFavorite();
      await this.checkFavorite(); // 每次操作后都刷新收藏状态
      this.favoriteLoading = false;
      this.animating = true;
      setTimeout(() => { this.animating = false; }, 350);
    },
    async toggleFavorite() {
      if (this.isFavorite) {
        await axios.delete(`/api/practice/problem/${this.problemId}/favorite`, { params: { userId: this.userId } });
        this.favoriteCount = Math.max(0, this.favoriteCount - 1);
      } else {
        await axios.post(`/api/practice/problem/${this.problemId}/favorite`, null, { params: { userId: this.userId } });
        this.favoriteCount = this.favoriteCount + 1;
      }
      await this.checkFavorite();
      await this.loadFavoriteCount();
    },
    formatCount(val) {
      if (val >= 10000) return (val / 10000).toFixed(1) + '万';
      return val;
    }
  }
}
</script>

<style scoped>
.code-practice-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.practice-main {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.problem-panel {
  width: 45%;
  background: #f8f9fa;
  overflow-y: auto;
  padding: 20px;
  border-right: 1px solid #dee2e6;
}

.code-panel {
  width: 55%;
  display: flex;
  flex-direction: column;
  background: #fff;
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
  padding: 20px;
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
  border-top: 1px solid #dee2e6;
  background: #f8f9fa;
}

.result-header {
  padding: 15px 20px;
  border-bottom: 1px solid #dee2e6;
  background: white;
}

.result-content {
  padding: 20px;
  max-height: 300px;
  overflow-y: auto;
}

.output-text {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 15px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.4;
  white-space: pre-wrap;
  margin: 0;
}

.test-cases {
  margin-top: 20px;
}

.test-case {
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 15px;
  margin-bottom: 10px;
}

.test-details {
  font-size: 14px;
  line-height: 1.5;
}

.test-details > div {
  margin-bottom: 5px;
}

@media (max-width: 768px) {
  .practice-main {
    flex-direction: column;
  }
  
  .problem-panel,
  .code-panel {
    width: 100%;
  }
  
  .problem-panel {
    height: 40%;
  }
  
  .code-panel {
    height: 60%;
  }
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
  0% { transform: scale(1);}
  30% { transform: scale(1.15);}
  60% { transform: scale(0.95);}
  100% { transform: scale(1);}
}
</style> 