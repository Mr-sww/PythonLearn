// 个人中心功能入口配置
export default [
  {
    group: '学习功能',
    entries: [
      {
        key: 'student-dashboard',
        title: '我的学习',
        desc: '查看学习进度和课程',
        icon: 'fas fa-book',
        path: '/student/dashboard',
        roles: ['STUDENT'],
        color: '#1976d2'
      },
      {
        key: 'student-assignments',
        title: '我的作业',
        desc: '查看和提交作业',
        icon: 'fas fa-tasks',
        path: '/student/assignments',
        roles: ['STUDENT'],
        color: '#388e3c'
      },
      {
        key: 'student-progress',
        title: '学习统计',
        desc: '查看学习数据分析',
        icon: 'fas fa-chart-line',
        path: '/student/progress',
        roles: ['STUDENT'],
        color: '#f57c00'
      }
    ]
  },
  {
    group: '教学管理',
    entries: [
      {
        key: 'teacher-dashboard',
        title: '教学管理',
        desc: '教学数据概览',
        icon: 'fas fa-chalkboard-teacher',
        path: '/teacher/dashboard',
        roles: ['TEACHER'],
        color: '#7b1fa2'
      },
      {
        key: 'teacher-courses',
        title: '课程管理',
        desc: '创建和管理课程',
        icon: 'fas fa-graduation-cap',
        path: '/teacher/courses',
        roles: ['TEACHER'],
        color: '#1976d2'
      },
      {
        key: 'teacher-assignments',
        title: '作业管理',
        desc: '发布和批改作业',
        icon: 'fas fa-edit',
        path: '/teacher/assignments',
        roles: ['TEACHER'],
        color: '#388e3c'
      },
      {
        key: 'teacher-students',
        title: '学生管理',
        desc: '查看学生信息',
        icon: 'fas fa-users',
        path: '/teacher/students',
        roles: ['TEACHER'],
        color: '#f57c00'
      }
    ]
  },
  {
    group: '系统管理',
    entries: [
      {
        key: 'admin-dashboard',
        title: '系统管理',
        desc: '系统概览和统计',
        icon: 'fas fa-cogs',
        path: '/admin/dashboard',
        roles: ['ADMIN'],
        color: '#d32f2f'
      },
      {
        key: 'admin-users',
        title: '用户管理',
        desc: '管理所有用户',
        icon: 'fas fa-user-cog',
        path: '/admin/users',
        roles: ['ADMIN'],
        color: '#1976d2'
      },
      {
        key: 'admin-courses',
        title: '课程审核',
        desc: '审核课程内容',
        icon: 'fas fa-clipboard-check',
        path: '/admin/courses',
        roles: ['ADMIN'],
        color: '#388e3c'
      },
      {
        key: 'admin-settings',
        title: '系统设置',
        desc: '系统配置管理',
        icon: 'fas fa-sliders-h',
        path: '/admin/settings',
        roles: ['ADMIN'],
        color: '#f57c00'
      }
    ]
  }
] 