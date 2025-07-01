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
            <button class="btn btn-outline-primary btn-sm rounded-pill">
              <i class="fa fa-star-o me-1"></i>收藏
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
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h5 class="fw-bold text-dark mb-0">讨论区</h5>
          <button class="btn btn-primary btn-sm rounded-pill">
            <i class="fa fa-plus me-1"></i>发表评论
          </button>
        </div>
        
        <div class="comments-list">
          <div class="comment-item" v-for="comment in comments" :key="comment.id">
            <div class="d-flex">
              <img :src="comment.avatar" class="rounded-circle me-3" width="40" height="40" alt="用户头像">
              <div class="flex-grow-1">
                <div class="d-flex align-items-center mb-1">
                  <span class="fw-medium text-dark">{{ comment.author }}</span>
                  <span class="text-muted small ms-2">{{ comment.time }}</span>
                  <span class="badge bg-primary ms-2" v-if="comment.isAuthor">题主</span>
                </div>
                <div class="comment-content mb-2">{{ comment.content }}</div>
                <div class="comment-actions">
                  <button class="btn btn-link btn-sm p-0 me-3">
                    <i class="fa fa-thumbs-up me-1"></i>{{ comment.likes }}
                  </button>
                  <button class="btn btn-link btn-sm p-0 me-3">
                    <i class="fa fa-comment me-1"></i>回复
                  </button>
                  <button class="btn btn-link btn-sm p-0">
                    <i class="fa fa-flag me-1"></i>举报
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
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

export default {
  name: 'CodePractice',
  data() {
    return {
      problemId: 1,
      problem: null,
      code: '',
      showResult: false,
      result: {
        status: '',
        output: '',
        testCases: []
      },
      comments: [
        {
          id: 1,
          author: '张三',
          avatar: 'https://picsum.photos/40/40?random=1',
          content: '这道题用哈希表解法很经典，时间复杂度O(n)，空间复杂度O(n)。',
          time: '2小时前',
          likes: 15,
          isAuthor: true
        },
        {
          id: 2,
          author: '李四',
          avatar: 'https://picsum.photos/40/40?random=2',
          content: '暴力解法虽然简单，但时间复杂度是O(n²)，面试时建议用哈希表。',
          time: '1天前',
          likes: 8,
          isAuthor: false
        },
        {
          id: 3,
          author: '王五',
          avatar: 'https://picsum.photos/40/40?random=3',
          content: '有没有人用双指针解法？虽然这道题不太适合，但想看看思路。',
          time: '3天前',
          likes: 3,
          isAuthor: false
        }
      ],
      isRunning: false
    }
  },
  async mounted() {
    // 从URL参数获取题目ID
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    if (id) {
      this.problemId = parseInt(id);
    }
    
    // 加载题目信息
    await this.loadProblem();
  },
  methods: {
    goBackToList() {
    this.$router.push({ path: '/problems', query: { highlight: this.problemId } });
    },
 
    async loadProblem() {
      this.problem = CodeRunner.getProblem(this.problemId);
      if (this.problem) {
        this.code = this.problem.template;
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
</style> 