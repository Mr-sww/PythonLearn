<template>
  <div class="teacher-courses">
    <div class="page-header">
      <h1 class="page-title">课程管理</h1>
      <button class="btn btn-primary" @click="createCourse">
        <i class="fas fa-plus"></i>
        创建课程
      </button>
    </div>

    <div class="courses-container">
      <div v-for="course in courses" :key="course.id" class="course-card">
        <div class="course-header">
          <h3 class="course-title">{{ course.title }}</h3>
          <span :class="['course-status', course.status]">{{ course.status }}</span>
        </div>
        <div class="course-content">
          <p class="course-desc">{{ course.description }}</p>
          <div class="course-meta">
            <span class="meta-item">
              <i class="fas fa-users"></i>
              {{ course.studentCount }}名学生
            </span>
            <span class="meta-item">
              <i class="fas fa-calendar"></i>
              创建时间：{{ course.createTime }}
            </span>
          </div>
        </div>
        <div class="course-actions">
          <button class="btn btn-secondary" @click="editCourse(course.id)">
            <i class="fas fa-edit"></i>
            编辑
          </button>
          <button class="btn btn-info" @click="viewStudents(course.id)">
            <i class="fas fa-users"></i>
            学生
          </button>
          <button class="btn btn-success" @click="manageContent(course.id)">
            <i class="fas fa-book"></i>
            内容
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TeacherCourses',
  data() {
    return {
      courses: [
        {
          id: 1,
          title: 'Python基础编程',
          description: 'Python编程基础语法和概念，适合初学者',
          status: '进行中',
          studentCount: 15,
          createTime: '2024-01-01'
        },
        {
          id: 2,
          title: '数据分析入门',
          description: '使用Python进行数据分析，包括pandas、numpy等',
          status: '进行中',
          studentCount: 20,
          createTime: '2024-01-05'
        },
        {
          id: 3,
          title: 'Web开发实战',
          description: 'Flask框架Web开发，从零开始构建Web应用',
          status: '即将开始',
          studentCount: 10,
          createTime: '2024-01-10'
        }
      ]
    }
  },
  methods: {
    createCourse() {
      console.log('创建课程')
    },
    editCourse(id) {
      console.log('编辑课程:', id)
    },
    viewStudents(id) {
      console.log('查看学生:', id)
    },
    manageContent(id) {
      console.log('管理内容:', id)
    }
  }
}
</script>

<style scoped>
.teacher-courses {
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

.courses-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.course-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.course-card:hover {
  transform: translateY(-2px);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.course-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.course-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.course-status.进行中 {
  background: #d4edda;
  color: #155724;
}

.course-status.即将开始 {
  background: #fff3cd;
  color: #856404;
}

.course-content {
  margin-bottom: 1.5rem;
}

.course-desc {
  color: #666;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.course-meta {
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

.course-actions {
  display: flex;
  gap: 1rem;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .course-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .course-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .course-actions {
    flex-direction: column;
  }
}
</style> 