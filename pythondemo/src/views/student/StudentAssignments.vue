<template>
  <div class="student-assignments">
    <div class="page-header">
      <h1 class="page-title">我的作业</h1>
      <p class="page-subtitle">查看和管理你的作业</p>
    </div>

    <!-- 作业列表 -->
    <div class="assignments-container">
      <div v-for="assignment in assignments" :key="assignment.id" class="assignment-card">
        <div class="assignment-header">
          <h3 class="assignment-title">{{ assignment.title }}</h3>
          <span :class="['assignment-status', assignment.status]">{{ getStatusText(assignment.status) }}</span>
        </div>
        <div class="assignment-content">
          <p class="assignment-desc">{{ assignment.description }}</p>
          <div class="assignment-meta">
            <span class="meta-item">
              <i class="fas fa-user"></i>
              {{ assignment.teacher }}
            </span>
            <span class="meta-item">
              <i class="fas fa-calendar"></i>
              截止时间：{{ assignment.dueDate }}
            </span>
            <span class="meta-item">
              <i class="fas fa-star"></i>
              满分：{{ assignment.maxScore }}分
            </span>
          </div>
        </div>
        <div class="assignment-actions">
          <button v-if="assignment.status === 'pending'" class="btn btn-primary" @click="submitAssignment(assignment.id)">
            <i class="fas fa-upload"></i>
            提交作业
          </button>
          <button v-if="assignment.status === 'submitted'" class="btn btn-secondary" @click="viewSubmission(assignment.id)">
            <i class="fas fa-eye"></i>
            查看提交
          </button>
          <button v-if="assignment.status === 'graded'" class="btn btn-success" @click="viewGrade(assignment.id)">
            <i class="fas fa-chart-bar"></i>
            查看成绩
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StudentAssignments',
  data() {
    return {
      assignments: [
        {
          id: 1,
          title: 'Python基础语法练习',
          description: '完成Python基础语法相关的编程练习，包括变量、循环、函数等知识点。',
          teacher: '李老师',
          dueDate: '2024-01-15',
          maxScore: 100,
          status: 'pending'
        },
        {
          id: 2,
          title: '数据分析项目',
          description: '使用Python进行数据分析，完成数据清洗、可视化等任务。',
          teacher: '王老师',
          dueDate: '2024-01-20',
          maxScore: 150,
          status: 'submitted'
        },
        {
          id: 3,
          title: 'Web开发实践',
          description: '使用Flask框架开发一个简单的Web应用。',
          teacher: '张老师',
          dueDate: '2024-01-10',
          maxScore: 120,
          status: 'graded',
          score: 95
        }
      ]
    }
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        'pending': '待提交',
        'submitted': '已提交',
        'graded': '已批改'
      }
      return statusMap[status] || status
    },
    submitAssignment(id) {
      // 提交作业逻辑
      console.log('提交作业:', id)
    },
    viewSubmission(id) {
      // 查看提交内容
      console.log('查看提交:', id)
    },
    viewGrade(id) {
      // 查看成绩
      console.log('查看成绩:', id)
    }
  }
}
</script>

<style scoped>
.student-assignments {
  padding: 0;
}

.page-header {
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.page-subtitle {
  color: #666;
  font-size: 1.1rem;
}

.assignments-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.assignment-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.assignment-card:hover {
  transform: translateY(-2px);
}

.assignment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.assignment-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.assignment-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.assignment-status.pending {
  background: #fff3cd;
  color: #856404;
}

.assignment-status.submitted {
  background: #d1ecf1;
  color: #0c5460;
}

.assignment-status.graded {
  background: #d4edda;
  color: #155724;
}

.assignment-content {
  margin-bottom: 1.5rem;
}

.assignment-desc {
  color: #666;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.assignment-meta {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #666;
  font-size: 0.9rem;
}

.assignment-actions {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-success:hover {
  background: #218838;
}

@media (max-width: 768px) {
  .assignment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .assignment-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .assignment-actions {
    flex-direction: column;
  }
}
</style> 