// 代码运行服务 - 模拟在线OJ功能
import axios from 'axios';

class CodeRunner {
  constructor() {
    this.problems = {
      1: {
        id: 1,
        title: '两数之和',
        difficulty: '简单',
        description: '给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。',
        examples: [
          {
            input: 'nums = [2,7,11,15], target = 9',
            output: '[0,1]',
            explanation: '因为 nums[0] + nums[1] == 9，返回 [0, 1]。'
          }
        ],
        template: `class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        # 在这里编写你的代码
        pass`
      }
      // 可以继续添加更多题目
    };
  }

  getProblem(problemId) {
    return this.problems[problemId] || null;
  }

  async runCode(problemId, code, language = 'python', input = '') {
    const res = await axios.post('/api/judge/run', {
      code,
      language,
      input
    });
    return res.data;
  }
}

export default new CodeRunner();
