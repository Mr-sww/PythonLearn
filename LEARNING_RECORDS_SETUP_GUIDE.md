# 学习记录系统设置指南

## 📋 概述

本指南将帮助您快速设置和配置学习记录系统，包括数据库创建、测试数据插入和功能验证。

## 🗄️ 数据库设置

### 1. 完整数据库创建

运行完整的数据库创建脚本：

```bash
mysql -u root -p pythonlearn < create_learning_records_database.sql
```

这个脚本会创建：
- ✅ 学习记录表 (`study_record`)
- ✅ 学习进度表 (`learning_progress`)
- ✅ 课程章节表 (`course_chapter`)
- ✅ 课程课时表 (`course_lesson`)
- ✅ 学习统计表 (`learning_statistics`)
- ✅ 学习目标表 (`learning_goals`)
- ✅ 外键约束
- ✅ 索引优化
- ✅ 存储过程
- ✅ 触发器
- ✅ 视图

### 2. 快速测试数据插入

如果只需要测试数据，运行简化脚本：

```bash
mysql -u root -p pythonlearn < insert_test_data_simple.sql
```

## 🔧 后端配置

### 1. 确保实体类已创建

检查以下文件是否存在：
- `src/main/java/com/demo/python_demo/entity/StudyRecord.java`
- `src/main/java/com/demo/python_demo/repository/StudyRecordRepository.java`
- `src/main/java/com/demo/python_demo/service/StudyRecordService.java`
- `src/main/java/com/demo/python_demo/service/impl/StudyRecordServiceImpl.java`
- `src/main/java/com/demo/python_demo/controller/StudyRecordController.java`

### 2. 启动Spring Boot应用

```bash
cd src
mvn spring-boot:run
```

## 🎨 前端配置

### 1. 启动Vue开发服务器

```bash
cd pythondemo
npm run serve
```

### 2. 访问页面

- 学习记录页面：`http://localhost:8081/learning-records`
- 学习中心页面：`http://localhost:8081/learning`

## 📊 功能测试

### 1. API接口测试

使用Postman或curl测试以下接口：

#### 获取学习记录
```bash
curl -X GET "http://localhost:8080/api/study-records" \
  -H "Cookie: JSESSIONID=your_session_id" \
  -H "Content-Type: application/json"
```

#### 获取学习统计
```bash
curl -X GET "http://localhost:8080/api/study-records/statistics" \
  -H "Cookie: JSESSIONID=your_session_id" \
  -H "Content-Type: application/json"
```

#### 获取最近学习记录
```bash
curl -X GET "http://localhost:8080/api/study-records/recent?limit=5" \
  -H "Cookie: JSESSIONID=your_session_id" \
  -H "Content-Type: application/json"
```

#### 记录学习进度
```bash
curl -X POST "http://localhost:8080/api/study-records/progress" \
  -H "Cookie: JSESSIONID=your_session_id" \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": 2169,
    "lessonId": 1,
    "studyTime": 1800,
    "progress": 100.0,
    "completed": true
  }'
```

### 2. 数据库查询测试

#### 查看学习记录
```sql
SELECT 
    sr.RecordID,
    u.username,
    c.Title as CourseTitle,
    sr.StudyTime,
    sr.Progress,
    sr.Completed,
    sr.StudyDate
FROM study_record sr
JOIN user u ON sr.UserID = u.user_id
JOIN course c ON sr.CourseID = c.ArticleID
ORDER BY sr.StudyDate DESC
LIMIT 10;
```

#### 查看学习统计
```sql
SELECT 
    ls.UserID,
    u.username,
    ls.TotalCourses,
    ls.CompletedCourses,
    ls.TotalStudyTime,
    ls.ContinuousDays
FROM learning_statistics ls
JOIN user u ON ls.UserID = u.user_id;
```

#### 查看用户学习概览
```sql
SELECT * FROM user_learning_overview;
```

## 🚀 功能特性

### 1. 学习记录管理
- ✅ 记录每次学习活动
- ✅ 跟踪学习时长和进度
- ✅ 支持完成状态标记
- ✅ 自动时间戳记录

### 2. 学习进度跟踪
- ✅ 课程级别进度管理
- ✅ 章节和课时进度
- ✅ 学习状态跟踪
- ✅ 最后学习时间记录

### 3. 统计功能
- ✅ 总学习时长统计
- ✅ 完成课程数统计
- ✅ 连续学习天数
- ✅ 学习记录数量

### 4. 前端功能
- ✅ 学习记录列表展示
- ✅ 统计卡片显示
- ✅ 时间筛选功能
- ✅ 搜索功能
- ✅ 最近学习记录
- ✅ 继续学习跳转

## 🔍 故障排除

### 1. 数据库连接问题

检查 `application.properties` 配置：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pythonlearn
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. 外键约束错误

如果遇到外键约束错误，确保：
- 用户表 (`user`) 存在且有数据
- 课程表 (`course`) 存在且有数据
- 课程课时表 (`course_lesson`) 存在且有数据

### 3. 前端API调用失败

检查：
- 后端服务是否正常运行
- CORS配置是否正确
- Session是否有效
- 网络连接是否正常

### 4. 数据不显示

检查：
- 数据库是否有测试数据
- API返回的数据格式是否正确
- 前端数据绑定是否正确

## 📈 性能优化

### 1. 数据库优化
- 已创建必要的索引
- 使用存储过程减少网络传输
- 触发器自动更新统计

### 2. 前端优化
- 分页加载大量数据
- 缓存统计信息
- 异步加载数据

## 🔄 数据维护

### 1. 定期清理
```sql
-- 清理30天前的学习记录（可选）
DELETE FROM study_record 
WHERE StudyDate < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

### 2. 统计更新
```sql
-- 手动更新用户统计
CALL UpdateUserLearningStatistics(1);
```

### 3. 数据备份
```bash
mysqldump -u root -p pythonlearn > backup_$(date +%Y%m%d).sql
```

## 📞 支持

如果遇到问题，请检查：
1. 数据库日志
2. Spring Boot应用日志
3. 浏览器控制台错误
4. 网络请求状态

## ✅ 验证清单

- [ ] 数据库表创建成功
- [ ] 测试数据插入成功
- [ ] 后端API正常响应
- [ ] 前端页面正常显示
- [ ] 学习记录功能正常
- [ ] 统计功能正常
- [ ] 筛选搜索功能正常
- [ ] 继续学习跳转正常

完成以上步骤后，学习记录系统就可以正常使用了！
