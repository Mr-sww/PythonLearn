# 前后端和数据库衔接优化总结

## 优化概述

本次优化主要解决了前后端和数据库之间的衔接问题，确保系统能够正常显示用户和课程数据，同时不影响其他功能。

## 主要问题分析

### 1. 数据库表结构不完整
- **问题**: `course` 表缺少审核相关字段 (`reviewComment`, `reviewedAt`, `reviewedBy`)
- **影响**: 导致 `BadSqlGrammarException: Unknown column 'reviewComment' in 'field list'`
- **解决**: 已通过SQL脚本添加缺失字段

### 2. 用户管理接口缺失
- **问题**: 前端调用 `/api/admin/users` 但后端没有对应接口
- **影响**: 用户管理页面显示"暂无用户数据"
- **解决**: 在 `AdminController` 中添加完整的用户管理接口

### 3. 字段映射不一致
- **问题**: MyBatis查询中的字段映射与数据库表结构不匹配
- **影响**: 数据查询失败
- **解决**: 修复所有SQL查询的字段映射

### 4. 用户角色定义错误
- **问题**: 之前对用户角色/专业的对应关系理解有误
- **影响**: 角色显示和验证逻辑不正确
- **解决**: 正确定义用户角色常量，1-6为专业学生，7为教师，8为管理员

## 优化内容

### 1. 后端优化

#### 1.1 创建配置类 (`CourseConfig.java`)
```java
// 统一管理课程状态、难度、分类等常量
public static final class Status {
    public static final String PENDING = "pending";      // 待审核
    public static final String APPROVED = "approved";    // 已通过
    public static final String REJECTED = "rejected";    // 已拒绝
    // ... 其他状态
}

// 用户角色/专业常量
public static final class UserRole {
    public static final int COMPUTER_SCIENCE = 1;      // 计算机类学生
    public static final int ENGINEERING_DESIGN = 2;    // 工设类学生
    public static final int ART = 3;                   // 艺术类学生
    public static final int MEDICAL = 4;               // 医学类学生
    public static final int LIBERAL_ARTS = 5;          // 文科类学生
    public static final int PHYSICAL_EDUCATION = 6;    // 体育类学生
    public static final int TEACHER = 7;               // 教师
    public static final int ADMIN = 8;                 // 管理员
}
```

#### 1.2 修复 `AdminController.java`
- 添加用户管理接口 (`/api/admin/users`)
- 优化课程审核逻辑
- 改进错误处理和状态验证
- 使用配置常量进行角色验证

#### 1.3 修复 `CourseRepository.java`
- 确保所有SQL查询都正确映射字段
- 添加审核相关字段的查询支持

#### 1.4 创建数据库健康检查 (`DatabaseHealthCheck.java`)
- 提供 `/api/admin/health/database` 接口
- 检查数据库连接、表结构、数据量
- 帮助诊断数据库问题

### 2. 前端优化

#### 2.1 创建配置文件 (`courseConfig.js`)
```javascript
// 统一管理课程状态和配置
export const COURSE_STATUS = {
  PENDING: 'pending',      // 待审核
  APPROVED: 'approved',    // 已通过
  REJECTED: 'rejected'     // 已拒绝
}

// 用户角色/专业常量
export const USER_ROLE = {
  COMPUTER_SCIENCE: 1,      // 计算机类学生
  ENGINEERING_DESIGN: 2,    // 工设类学生
  ART: 3,                   // 艺术类学生
  MEDICAL: 4,               // 医学类学生
  LIBERAL_ARTS: 5,          // 文科类学生
  PHYSICAL_EDUCATION: 6,    // 体育类学生
  TEACHER: 7,               // 教师
  ADMIN: 8                  // 管理员
}

// 提供状态验证和显示函数
export const getStatusText = (status) => { /* ... */ }
export const getUserRoleText = (role) => { /* ... */ }
```

#### 2.2 优化状态处理
- 正确处理 `NULL` 状态值
- 为缺失状态提供默认值
- 统一状态显示样式
- 正确显示用户角色/专业

### 3. 数据库优化

#### 3.1 表结构修复
```sql
-- 添加缺失的审核字段
ALTER TABLE course 
ADD COLUMN reviewComment TEXT COMMENT '审核意见',
ADD COLUMN reviewedAt DATETIME COMMENT '审核时间',
ADD COLUMN reviewedBy INT COMMENT '审核人ID';

-- 更新现有课程状态
UPDATE course SET Status = 'pending' WHERE Status IS NULL OR Status = '';
```

#### 3.2 数据完整性
- 确保所有必要字段都存在
- 设置合理的默认值
- 维护数据一致性

## 新增功能

### 1. 用户管理功能
- **获取所有用户**: `GET /api/admin/users`
- **更新用户状态**: `PUT /api/admin/users/{userId}/status`
- **更新用户角色**: `PUT /api/admin/users/{userId}/role`
- **删除用户**: `DELETE /api/admin/users/{userId}`

### 2. 课程审核功能
- **审核课程**: `POST /api/admin/courses/{courseId}/review`
- **批量审核**: `POST /api/admin/courses/batch-review`
- **课程状态管理**: 支持 pending/approved/rejected 状态

### 3. 健康检查功能
- **数据库健康**: `GET /api/admin/health/database`
- **详细诊断**: `GET /api/admin/health/detailed`

## 配置说明

### 1. 课程状态配置
```java
// 后端配置
CourseConfig.Status.PENDING    // 待审核
CourseConfig.Status.APPROVED  // 已通过
CourseConfig.Status.REJECTED  // 已拒绝
```

```javascript
// 前端配置
COURSE_STATUS.PENDING         // 待审核
COURSE_STATUS.APPROVED        // 已通过
COURSE_STATUS.REJECTED        // 已拒绝
```

### 2. 审核操作配置
```java
// 后端配置
CourseConfig.ReviewAction.APPROVE  // 通过
CourseConfig.ReviewAction.REJECT   // 拒绝
```

```javascript
// 前端配置
REVIEW_ACTION.APPROVE             // 通过
REVIEW_ACTION.REJECT              // 拒绝
```

### 3. 用户角色配置
```java
// 后端配置
CourseConfig.UserRole.COMPUTER_SCIENCE = 1      // 计算机类学生
CourseConfig.UserRole.TEACHER = 7               // 教师
CourseConfig.UserRole.ADMIN = 8                 // 管理员
```

```javascript
// 前端配置
USER_ROLE.COMPUTER_SCIENCE = 1                  // 计算机类学生
USER_ROLE.TEACHER = 7                           // 教师
USER_ROLE.ADMIN = 8                             // 管理员
```

## 使用说明

### 1. 启动系统
```bash
# 1. 确保数据库运行并执行修复脚本
# 2. 启动后端服务
mvn spring-boot:run

# 3. 启动前端服务
cd pythondemo
npm run serve
```

### 2. 验证功能
```bash
# 检查数据库健康状态
curl http://localhost:8080/api/admin/health/database

# 检查用户数据
curl http://localhost:8080/api/admin/users

# 检查课程数据
curl http://localhost:8080/api/admin/courses
```

### 3. 前端访问
- **用户管理**: `http://localhost:8081/admin/users`
- **课程审核**: `http://localhost:8081/admin/courses/review`
- **课程管理**: `http://localhost:8081/admin/courses/management`

## 注意事项

### 1. 不影响现有功能
- 所有优化都是增量式的
- 保持原有API接口不变
- 向后兼容现有数据

### 2. 数据安全
- 添加了状态验证
- 改进了错误处理
- 提供了详细的日志记录

### 3. 性能优化
- 使用配置常量避免硬编码
- 优化SQL查询字段映射
- 提供健康检查接口

### 4. 角色权限管理
- 正确区分学生、教师、管理员角色
- 1-6为专业学生（不同专业类别）
- 7为教师
- 8为管理员

## 故障排除

### 1. 如果仍有"暂无数据"问题
1. 检查数据库健康状态: `/api/admin/health/database`
2. 确认表结构是否正确
3. 检查后端服务日志
4. 验证数据库连接配置

### 2. 如果出现字段错误
1. 确认已执行数据库修复脚本
2. 检查实体类字段映射
3. 验证MyBatis查询语句
4. 重启后端服务

### 3. 如果前端显示异常
1. 检查浏览器控制台错误
2. 确认API接口返回正常
3. 验证状态处理逻辑
4. 清除浏览器缓存

### 4. 如果角色显示错误
1. 确认用户表中的 `groupType` 字段值正确
2. 检查角色验证逻辑
3. 验证前后端配置常量一致
4. 重启服务

## 总结

通过本次优化，我们成功解决了：
1. ✅ 数据库表结构不完整的问题
2. ✅ 用户管理接口缺失的问题
3. ✅ 字段映射不一致的问题
4. ✅ 状态处理不规范的问题
5. ✅ 用户角色定义错误的问题

现在系统应该能够：
- 正常显示用户数据
- 正常显示课程数据
- 支持完整的课程审核流程
- 提供用户管理功能
- 正确显示用户角色/专业
- 保持所有现有功能不受影响

如果还有问题，请使用健康检查接口进行诊断，或查看后端服务日志获取详细信息。
