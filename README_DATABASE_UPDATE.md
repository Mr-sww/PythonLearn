# 数据库更新说明

## 概述

本次更新重新设计了数据库结构，完善了用户身份管理、课程管理、学习进度跟踪等功能。

## 数据库结构

### 主要表结构

1. **user** - 用户表
   - `user_id`: 用户ID (主键)
   - `phone`: 手机号 (唯一)
   - `account`: 账号 (唯一)
   - `password`: 密码 (加密)
   - `nickname`: 昵称
   - `avatar`: 头像URL
   - `group_type`: 用户组类型 (1-学生, 2-教师, 3-管理员)
   - `intest_types`: 兴趣类型
   - `email`: 邮箱
   - `status`: 状态 (active/inactive)
   - `create_time`: 创建时间
   - `update_time`: 更新时间

2. **pythonvideos** - Python视频表
   - `ID`: 视频ID (主键)
   - `Title`: 视频标题
   - `URL`: 视频URL
   - `ImageURL`: 封面图片URL
   - `PlayCount`: 播放次数
   - `PublishDate`: 发布日期
   - `Tags`: 标签
   - `ImportDate`: 导入时间

3. **pythonproblems** - Python问题表
   - `Id`: 问题ID (主键)
   - `Title`: 问题标题
   - `Background`: 问题背景
   - `Description`: 问题描述
   - `InputFormat`: 输入格式
   - `OutputFormat`: 输出格式
   - `Note`: 备注
   - `Samples`: 示例(JSON格式)
   - `CreateTime`: 创建时间

4. **course** - 课程表
   - `ArticleID`: 课程ID (主键)
   - `Title`: 课程标题
   - `Content`: 课程内容
   - `Author`: 作者
   - `Category`: 分类
   - `Tags`: 标签
   - `Views`: 浏览量
   - `Price`: 价格
   - `Rating`: 评分
   - `Duration`: 时长(分钟)
   - `Lessons`: 课时数
   - `Difficulty`: 难度等级
   - `CoverImage`: 封面图片
   - `AuthorAvatar`: 作者头像
   - `URL`: 课程URL
   - `PublicationDate`: 发布日期
   - `status`: 状态 (pending/approved/rejected)
   - `CreatedAt`: 创建时间
   - `UpdatedAt`: 更新时间

## 用户身份管理

### 用户组类型 (group_type)
- `1`: 学生 - 可以学习课程、提交作业、参与讨论
- `2`: 教师 - 可以创建课程、管理学生、批改作业
- `3`: 管理员 - 可以管理所有用户、审核课程、系统维护

### 用户状态 (status)
- `active`: 正常状态
- `inactive`: 禁用状态

## 安装步骤

### 1. 创建数据库
```sql
CREATE DATABASE `python` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 执行表结构脚本
```bash
mysql -u root -p python < database_tables.sql
```

### 3. 插入测试数据
```bash
mysql -u root -p python < database_init.sql
```

### 4. 更新应用配置
确保 `application.properties` 中的数据库配置正确：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/python?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456
```

## 新功能特性

### 1. 用户管理
- 用户注册、登录、信息更新
- 用户身份管理 (学生/教师/管理员)
- 用户状态管理 (正常/禁用)
- 用户搜索和筛选

### 2. 课程管理
- 课程创建、编辑、删除
- 课程审核流程 (待审核/已通过/已拒绝)
- 课程分类和标签管理
- 课程统计和分析

### 3. 学习进度跟踪
- 学习进度记录
- 学习时长统计
- 学习状态管理
- 学习历史记录

### 4. 评论系统
- 课程评论和评分
- 评论回复功能
- 评论点赞功能
- 评论管理

### 5. 收藏系统
- 课程收藏
- 收藏列表管理
- 收藏统计

### 6. 系统管理
- 系统日志记录
- 系统统计信息
- 系统维护功能
- 数据备份和恢复

## API接口

### 用户相关
- `GET /api/admin/users` - 获取所有用户
- `GET /api/admin/users/search` - 搜索用户
- `PUT /api/admin/users/{userId}/status` - 更新用户状态
- `PUT /api/admin/users/{userId}/role` - 更新用户角色
- `DELETE /api/admin/users/{userId}` - 删除用户

### 课程相关
- `GET /api/admin/courses` - 获取所有课程
- `PUT /api/admin/courses/{courseId}/review` - 审核课程

### 系统相关
- `GET /api/admin/statistics` - 获取系统统计
- `GET /api/admin/logs` - 获取系统日志
- `GET /api/admin/activities` - 获取最近活动
- `POST /api/admin/maintenance/{action}` - 系统维护

### Python视频相关
- `GET /api/python-videos` - 获取所有视频
- `GET /api/python-videos/{id}` - 获取视频详情
- `GET /api/python-videos/recommend` - 获取推荐视频
- `GET /api/python-videos/random` - 获取随机视频
- `GET /api/python-videos/search` - 搜索视频

### Python问题相关
- `GET /api/python-problems` - 获取所有问题
- `GET /api/python-problems/{id}` - 获取问题详情
- `GET /api/python-problems/search` - 搜索问题
- `GET /api/python-problems/recent` - 获取最新问题

## 测试账号

### 管理员账号
- 账号: `admin`
- 密码: `123456`
- 权限: 系统管理员

### 教师账号
- 账号: `teacher1`
- 密码: `123456`
- 权限: 教师

### 学生账号
- 账号: `student1`
- 密码: `123456`
- 权限: 学生

## 注意事项

1. **数据迁移**: 如果从旧数据库迁移，请先备份数据
2. **权限管理**: 确保用户权限设置正确
3. **字符编码**: 使用 utf8mb4 字符集支持emoji等特殊字符
4. **索引优化**: 已为常用查询字段创建索引
5. **外键约束**: 确保数据完整性

## 故障排除

### 常见问题

1. **连接数据库失败**
   - 检查数据库服务是否启动
   - 验证用户名密码是否正确
   - 确认数据库名是否正确

2. **字符编码问题**
   - 确保数据库使用 utf8mb4 字符集
   - 检查应用配置中的字符编码设置

3. **权限问题**
   - 确保用户有足够的数据库权限
   - 检查应用配置中的用户名密码

4. **外键约束错误**
   - 确保先创建被引用的表
   - 检查外键字段的数据类型是否匹配

## 联系支持

如有问题，请联系技术支持团队。
