<template>
  <div class="teacher-students">
    <div class="page-header">
      <h1 class="page-title">学生管理</h1>
      <div class="header-actions">
        <input 
          type="text" 
          placeholder="搜索学生..." 
          class="search-input"
          v-model="searchQuery"
        >
        <button class="btn btn-primary" @click="exportStudents">
          <i class="fas fa-download"></i>
          导出名单
        </button>
      </div>
    </div>

    <div class="students-container">
      <div v-for="student in filteredStudents" :key="student.id" class="student-card">
        <div class="student-avatar">
          <img :src="student.avatar" :alt="student.name">
        </div>
        <div class="student-info">
          <h3 class="student-name">{{ student.name }}</h3>
          <p class="student-email">{{ student.email }}</p>
          <div class="student-stats">
            <span class="stat-item">
              <i class="fas fa-book"></i>
              {{ student.courseCount }}门课程
            </span>
            <span class="stat-item">
              <i class="fas fa-clock"></i>
              {{ student.studyHours }}小时
            </span>
            <span class="stat-item">
              <i class="fas fa-star"></i>
              {{ student.averageScore }}分
            </span>
          </div>
        </div>
        <div class="student-actions">
          <button class="btn btn-info" @click="viewProgress(student.id)">
            <i class="fas fa-chart-line"></i>
            进度
          </button>
          <button class="btn btn-secondary" @click="sendMessage(student.id)">
            <i class="fas fa-envelope"></i>
            消息
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TeacherStudents',
  data() {
    return {
      searchQuery: '',
      students: [
        {
          id: 1,
          name: '张三',
          email: 'zhangsan@example.com',
          avatar: 'https://picsum.photos/50/50?random=1',
          courseCount: 3,
          studyHours: 24,
          averageScore: 85
        },
        {
          id: 2,
          name: '李四',
          email: 'lisi@example.com',
          avatar: 'https://picsum.photos/50/50?random=2',
          courseCount: 2,
          studyHours: 18,
          averageScore: 92
        },
        {
          id: 3,
          name: '王五',
          email: 'wangwu@example.com',
          avatar: 'https://picsum.photos/50/50?random=3',
          courseCount: 4,
          studyHours: 32,
          averageScore: 78
        }
      ]
    }
  },
  computed: {
    filteredStudents() {
      if (!this.searchQuery) return this.students
      return this.students.filter(student => 
        student.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        student.email.toLowerCase().includes(this.searchQuery.toLowerCase())
      )
    }
  },
  methods: {
    exportStudents() {
      console.log('导出学生名单')
    },
    viewProgress(id) {
      console.log('查看学生进度:', id)
    },
    sendMessage(id) {
      console.log('发送消息:', id)
    }
  }
}
</script>

<style scoped>
.teacher-students {
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

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-input {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
  width: 200px;
}

.search-input:focus {
  outline: none;
  border-color: #7b1fa2;
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

.students-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.student-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  display: flex;
  align-items: center;
  gap: 1.5rem;
  transition: transform 0.2s;
}

.student-card:hover {
  transform: translateY(-2px);
}

.student-avatar {
  flex-shrink: 0;
}

.student-avatar img {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.student-info {
  flex: 1;
}

.student-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.25rem;
}

.student-email {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 0.75rem;
}

.student-stats {
  display: flex;
  gap: 1.5rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #666;
  font-size: 0.8rem;
}

.student-actions {
  display: flex;
  gap: 0.5rem;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .header-actions {
    width: 100%;
    flex-direction: column;
  }
  
  .search-input {
    width: 100%;
  }
  
  .student-card {
    flex-direction: column;
    text-align: center;
  }
  
  .student-stats {
    justify-content: center;
  }
  
  .student-actions {
    width: 100%;
    justify-content: center;
  }
}
</style> 