-- 简化修复：为course表添加缺失的字段（免费课程版本）
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 1. 为course表添加缺失的字段（如果不存在），移除Price字段
ALTER TABLE course 
ADD COLUMN IF NOT EXISTS `Rating` DECIMAL(3,2) DEFAULT 0.00 AFTER `Views`,
ADD COLUMN IF NOT EXISTS `Duration` VARCHAR(50) AFTER `Rating`,
ADD COLUMN IF NOT EXISTS `Lessons` INT DEFAULT 0 AFTER `Duration`,
ADD COLUMN IF NOT EXISTS `Difficulty` ENUM('beginner', 'intermediate', 'advanced') DEFAULT 'beginner' AFTER `Lessons`,
ADD COLUMN IF NOT EXISTS `CoverImage` VARCHAR(255) AFTER `Difficulty`,
ADD COLUMN IF NOT EXISTS `AuthorAvatar` VARCHAR(255) AFTER `CoverImage`,
ADD COLUMN IF NOT EXISTS `Status` VARCHAR(50) DEFAULT 'active' AFTER `AuthorAvatar`;

-- 2. 如果存在Price字段，则删除它（因为课程都是免费的）
ALTER TABLE course DROP COLUMN IF EXISTS `Price`;

-- 3. 更新现有课程的评分和时长信息
UPDATE course SET 
    Rating = 4.8,
    Duration = '20小时',
    Lessons = 15,
    Difficulty = 'beginner',
    CoverImage = '/course_images/2169.jpg'
WHERE ArticleID = 1;

UPDATE course SET 
    Rating = 4.9,
    Duration = '30小时',
    Lessons = 20,
    Difficulty = 'intermediate',
    CoverImage = '/course_images/2170.jpg'
WHERE ArticleID = 2;

UPDATE course SET 
    Rating = 4.7,
    Duration = '25小时',
    Lessons = 18,
    Difficulty = 'beginner',
    CoverImage = '/course_images/2171.jpg'
WHERE ArticleID = 3;

UPDATE course SET 
    Rating = 4.6,
    Duration = '35小时',
    Lessons = 25,
    Difficulty = 'advanced',
    CoverImage = '/course_images/2172.jpg'
WHERE ArticleID = 4;

UPDATE course SET 
    Rating = 4.8,
    Duration = '22小时',
    Lessons = 16,
    Difficulty = 'intermediate',
    CoverImage = '/course_images/2173.jpg'
WHERE ArticleID = 5;

-- 4. 验证表结构
DESCRIBE course;

-- 5. 查看更新后的课程数据
SELECT ArticleID, Title, Rating, Duration, Lessons, Difficulty, CoverImage FROM course;

-- 6. 测试收藏查询（确保没有错误）
SELECT uf.id, uf.user_id, uf.course_id, uf.create_time, 
       c.ArticleID, c.Title, c.PublicationDate, c.Content, c.Author, 
       c.Category, c.Tags, c.Views, c.Rating, c.Duration, c.Lessons, 
       c.Difficulty, c.CoverImage, c.CreatedAt, c.UpdatedAt 
FROM user_favorite uf 
LEFT JOIN course c ON uf.course_id = c.ArticleID 
WHERE uf.user_id = 9 
ORDER BY uf.create_time DESC;
