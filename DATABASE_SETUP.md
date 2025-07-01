# 数据库设置说明

## 问题解决

### 外键约束错误修复

如果您遇到以下错误：
```
3734 - Failed to add the foreign key constraint. Missing column 'UserID' for constraint 'course_comment_ibfk_2' in the referenced table 'user'
```

这是因为外键约束引用了错误的字段名。已修复为使用正确的字段名 `user_id`。

## 数据库初始化步骤

### 1. 创建数据库表结构

执行 `database_tables.sql` 文件：

```sql
-- 在MySQL中执行
source database_tables.sql;
```

或者直接复制文件内容到MySQL客户端执行。

### 2. 插入测试数据

执行 `database_init.sql` 文件：

```sql
-- 在MySQL中执行
source database_init.sql;
```

### 3. 验证表结构

检查表是否创建成功：

```sql
SHOW TABLES;
```

应该包含以下表：
- `user` - 用户表
- `course` - 课程表
- `course_comment` - 课程评论表
- `learning_progress` - 学习进度表
- `course_chapter` - 课程章节表
- `course_lesson` - 课程课时表
- `study_record` - 学习记录表
- `user_favorite` - 用户收藏表

### 4. 验证数据

检查测试数据是否插入成功：

```sql
-- 检查用户数据
SELECT * FROM user;

-- 检查课程数据
SELECT * FROM course;

-- 检查评论数据
SELECT * FROM course_comment;

-- 检查学习进度数据
SELECT * FROM learning_progress;
```

## 表结构说明

### user 表
- `user_id` - 用户ID (主键)
- `phone` - 手机号 (唯一)
- `account` - 账号 (唯一)
- `password` - 密码 (加密)
- `nickname` - 昵称
- `avatar` - 头像URL
- `group_type` - 用户组类型
- `intest_types` - 兴趣类型
- `email` - 邮箱
- `create_time` - 创建时间
- `update_time` - 更新时间

### course 表
- `ArticleID` - 课程ID (主键)
- `Title` - 课程标题
- `Content` - 课程内容
- `Author` - 作者
- `Category` - 分类
- `Tags` - 标签
- `Views` - 浏览量
- `Price` - 价格
- `Rating` - 评分
- `Duration` - 时长
- `Lessons` - 课时数
- `Difficulty` - 难度等级
- `CoverImage` - 封面图片
- `AuthorAvatar` - 作者头像

### course_comment 表
- `CommentID` - 评论ID (主键)
- `CourseID` - 课程ID (外键)
- `UserID` - 用户ID (外键)
- `Content` - 评论内容
- `Rating` - 评分 (1-5)
- `ParentID` - 父评论ID (支持回复)
- `Likes` - 点赞数
- `CreatedAt` - 创建时间
- `UpdatedAt` - 更新时间

### learning_progress 表
- `ProgressID` - 进度ID (主键)
- `UserID` - 用户ID (外键)
- `CourseID` - 课程ID (外键)
- `ChapterID` - 章节ID (外键)
- `LessonID` - 课时ID (外键)
- `Progress` - 学习进度 (0-100%)
- `Status` - 学习状态 (not_started/in_progress/completed)
- `TimeSpent` - 学习时长 (秒)
- `LastStudyTime` - 最后学习时间
- `CreatedAt` - 创建时间
- `UpdatedAt` - 更新时间

## 外键关系

```
user (user_id) 
  ↓
course_comment (UserID)
learning_progress (UserID)
study_record (UserID)
user_favorite (UserID)

course (ArticleID)
  ↓
course_comment (CourseID)
learning_progress (CourseID)
course_chapter (CourseID)
study_record (CourseID)
user_favorite (CourseID)

course_chapter (ChapterID)
  ↓
course_lesson (ChapterID)

course_lesson (LessonID)
  ↓
study_record (LessonID)

course_comment (CommentID)
  ↓
course_comment (ParentID) - 自引用，支持回复功能
```

## 测试数据说明

### 测试用户
- 账号：testuser1, testuser2, testuser3
- 密码：123456 (已加密)
- 包含不同的用户组和兴趣类型

### 测试课程
- Python基础入门到精通 (免费)
- Python进阶开发 (99元)
- Python Web开发实战 (199元)
- Python数据分析 (149元)

### 测试评论
- 包含不同评分的评论
- 包含回复功能的评论
- 包含点赞数据

### 测试学习进度
- 不同用户的学习进度
- 不同状态的学习记录
- 真实的学习时长数据

## 常见问题

### 1. 外键约束错误
确保先创建被引用的表，再创建引用表。

### 2. 字符集问题
所有表使用 `utf8mb4` 字符集，支持emoji等特殊字符。

### 3. 时间字段
使用 `DATETIME` 类型，支持时区转换。

### 4. 唯一约束
- 用户表的 `phone` 和 `account` 字段有唯一约束
- 学习进度表的 `UserID` 和 `CourseID` 组合有唯一约束
- 用户收藏表的 `UserID` 和 `CourseID` 组合有唯一约束

## 性能优化建议

### 1. 索引优化
```sql
-- 为常用查询字段添加索引
CREATE INDEX idx_course_comment_courseid ON course_comment(CourseID);
CREATE INDEX idx_course_comment_userid ON course_comment(UserID);
CREATE INDEX idx_learning_progress_userid ON learning_progress(UserID);
CREATE INDEX idx_learning_progress_courseid ON learning_progress(CourseID);
```

### 2. 分区表
对于大量数据的表，可以考虑按时间分区。

### 3. 缓存策略
- 课程信息缓存
- 用户学习进度缓存
- 评论统计缓存

## 备份和恢复

### 备份数据库
```bash
mysqldump -u username -p database_name > backup.sql
```

### 恢复数据库
```bash
mysql -u username -p database_name < backup.sql
```

## 监控和维护

### 1. 定期检查
- 表大小监控
- 索引使用情况
- 慢查询日志

### 2. 数据清理
- 定期清理无效数据
- 归档历史数据
- 优化表结构

### 3. 安全维护
- 定期更新密码
- 监控异常访问
- 备份重要数据 