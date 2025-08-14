<template>
  <div class="teacher-assignments">
    <div class="page-header">
      <h1 class="page-title">作业管理</h1>
      <button class="btn btn-primary" @click="createAssignment">
        <i class="fas fa-plus"></i>
        发布作业
      </button>
    </div>

    <div class="assignments-container">
      <div v-for="assignment in assignments" :key="assignment.id" class="assignment-card">
        <div class="assignment-header">
          <h3 class="assignment-title">{{ assignment.title }}</h3>
          <span :class="['assignment-status', assignment.status]">{{ assignment.status }}</span>
        </div>
        <div class="assignment-content">
          <p class="assignment-desc">{{ assignment.description }}</p>
          <div class="assignment-meta">
            <span class="meta-item">
              <i class="fas fa-graduation-cap"></i>
              {{ assignment.courseName }}
            </span>
            <span class="meta-item">
              <i class="fas fa-calendar"></i>
              截止时间：{{ assignment.dueDate }}
            </span>
            <span class="meta-item">
              <i class="fas fa-users"></i>
              {{ assignment.submittedCount }}/{{ assignment.totalStudents }}已提交
            </span>
          </div>
        </div>
        <div class="assignment-actions">
          <button class="btn btn-secondary" @click="editAssignment(assignment.id)">
            <i class="fas fa-edit"></i>
            编辑
          </button>
          <button class="btn btn-info" @click="viewSubmissions(assignment.id)">
            <i class="fas fa-eye"></i>
            查看提交
          </button>
          <button class="btn btn-success" @click="gradeAssignments(assignment.id)">
            <i class="fas fa-check"></i>
            批改
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TeacherAssignments',
  data() {
    return {
      assignments: [
        {
          id: 1,
          title: 'Python基础语法练习',
          description: '完成Python基础语法相关的编程练习，包括变量、循环、函数等知识点。',
          courseName: 'Python基础编程',
          status: '进行中',
          dueDate: '2024-01-15',
          submittedCount: 12,
          totalStudents: 15
        },
        {
          id: 2,
          title: '数据分析项目',
          description: '使用Python进行数据分析，完成数据清洗、可视化等任务。',
          courseName: '数据分析入门',
          status: '进行中',
          dueDate: '2024-01-20',
          submittedCount: 18,
          totalStudents: 20
        },
        {
          id: 3,
          title: 'Web开发实践',
          description: '使用Flask框架开发一个简单的Web应用。',
          courseName: 'Web开发实战',
          status: '已结束',
          dueDate: '2024-01-10',
          submittedCount: 8,
          totalStudents: 10
        }
      ]
    }
  },
  methods: {
    createAssignment() {
      console.log('发布作业')
    },
    editAssignment(id) {
      console.log('编辑作业:', id)
    },
    viewSubmissions(id) {
      console.log('查看提交:', id)
    },
    gradeAssignments(id) {
      console.log('批改作业:', id)
    }
  }
}
</script>

<style scoped>
.teacher-assignments {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
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
  background: linear-gradient(135deg, #7b1fa2 0%, #9c27b0 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(123, 31, 162, 0.3);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-info:hover {
  background: #138496;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-success:hover {
  background: #218838;
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

.assignment-status.进行中 {
  background: #d1ecf1;
  color: #0c5460;
}

.assignment-status.已结束 {
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

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
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