-- 检查数据库中的课程数据
USE pythonlearn;

-- 1. 检查课程表是否存在
SHOW TABLES LIKE 'course';

-- 2. 检查课程表结构
DESCRIBE course;

-- 3. 检查课程数据数量
SELECT COUNT(*) as course_count FROM course;

-- 4. 查看所有课程数据
SELECT 
    ArticleID,
    Title,
    Author,
    Category,
    Status,
    CreatedAt,
    UpdatedAt
FROM course 
LIMIT 10;

-- 5. 检查课程状态分布
SELECT 
    Status,
    COUNT(*) as count
FROM course 
GROUP BY Status;

-- 6. 检查是否有待审核的课程
SELECT 
    ArticleID,
    Title,
    Author,
    Status
FROM course 
WHERE Status = 'pending';

-- 7. 检查用户数据
SELECT 
    user_id,
    account,
    nickname,
    group_type,
    status
FROM user 
LIMIT 5;
