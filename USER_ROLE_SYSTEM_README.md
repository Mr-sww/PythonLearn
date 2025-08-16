# 用户角色权限系统说明

## 概述

本系统实现了基于 `group_type` 字段的用户角色权限管理，用户可以根据其专业类型自动获得相应的角色和权限。

## 角色定义

### 学生 (Student)
- **group_type**: 1-6
- **专业类型**:
  - 1: 计算机类
  - 2: 工设类
  - 3: 艺术类
  - 4: 医学类
  - 5: 文科类
  - 6: 体育类
- **权限**: 查看课程、练习题目、查看个人资料、AI助手、查看已选课程

### 教师 (Teacher)
- **group_type**: 7
- **权限**: 查看课程、管理课程、管理学生、查看分析数据、查看创建的课程、创建新课程

### 管理员 (Admin)
- **group_type**: 8
- **权限**: 查看课程、管理用户、管理系统、查看所有分析数据、访问后台管理面板

## 功能特性

### 1. 自动角色识别
- 系统根据用户的 `group_type` 字段自动识别用户角色
- 登录时自动设置相应的权限和菜单

### 2. 个人主页功能
- **学生**: 可以查看已选课程、学习进度、练习统计等
- **教师**: 可以查看创建的课程、学生统计、课程管理等
- **管理员**: 可以进入后台管理系统

### 3. 导航菜单
- 根据用户角色动态显示相应的导航菜单
- 学生和教师都有"我的课程"入口
- 管理员有"后台管理系统"入口

### 4. 路由保护
- 基于角色的路由访问控制
- 未授权访问自动重定向到对应角色的仪表板

## 技术实现

### 认证工具 (`src/utils/auth.js`)
```javascript
// 根据group_type获取用户角色
export function getRoleByGroupType(groupType) {
  if (groupType >= 1 && groupType <= 6) return 'student'
  if (groupType === 7) return 'teacher'
  if (groupType === 8) return 'admin'
  return 'student'
}

// 获取用户专业名称
export function getMajorName(groupType) {
  const MAJOR_TYPES = {
    1: '计算机类', 2: '工设类', 3: '艺术类',
    4: '医学类', 5: '文科类', 6: '体育类'
  }
  return MAJOR_TYPES[groupType] || '未设置'
}
```

### 角色判断函数
```javascript
export function isStudent() { return isRole('student') }
export function isTeacher() { return isRole('teacher') }
export function isAdmin() { return isRole('admin') }
```

### 路由守卫
- 检查用户登录状态
- 根据 `group_type` 计算用户角色
- 验证访问权限并重定向

## 页面组件

### 1. 我的课程 (`/my-courses`)
- **访问权限**: 学生
- **功能**: 查看已选课程、学习进度、课程统计

### 2. 教师课程 (`/teacher-courses`)
- **访问权限**: 教师
- **功能**: 管理创建的课程、创建新课程、查看学生统计

### 3. 个人主页 (`/profile`)
- **访问权限**: 所有已登录用户
- **功能**: 根据角色显示不同的功能卡片

## 数据库设计

### 用户表 (user)
```sql
CREATE TABLE user (
  user_id INT PRIMARY KEY,
  phone VARCHAR(20),
  account VARCHAR(50),
  password VARCHAR(100),
  nickname VARCHAR(50),
  avatar TEXT,
  group_type INT,  -- 关键字段：1-6学生，7教师，8管理员
  create_time DATETIME,
  update_time DATETIME,
  intest_types VARCHAR(100),
  email VARCHAR(255),
  status VARCHAR(50)
);
```

## 使用说明

### 1. 用户注册/登录
- 系统根据 `group_type` 自动设置用户角色
- 无需手动指定角色

### 2. 权限验证
- 前端组件使用 `v-if="isStudent"` 等指令控制显示
- 路由使用 `meta: { requiresRole: 'student' }` 控制访问

### 3. 角色切换
- 修改数据库中的 `group_type` 字段
- 重新登录后自动更新角色和权限

## 扩展建议

### 1. 细粒度权限
- 可以进一步细化每个角色的具体权限
- 支持权限组合和继承

### 2. 动态权限
- 支持运行时修改用户权限
- 权限变更实时生效

### 3. 审计日志
- 记录用户权限变更
- 跟踪权限使用情况

## 注意事项

1. **数据一致性**: 确保 `group_type` 字段的值在有效范围内
2. **权限缓存**: 角色信息缓存在 localStorage 中，修改后需要重新登录
3. **安全考虑**: 前端权限控制仅用于用户体验，后端必须进行权限验证
4. **错误处理**: 无效的 `group_type` 值默认设置为学生角色

## 更新日志

- **v1.0.0**: 实现基础角色权限系统
- **v1.1.0**: 添加基于角色的功能卡片
- **v1.2.0**: 完善路由保护和权限验证

