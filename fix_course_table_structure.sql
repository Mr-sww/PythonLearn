-- 修复course表结构，确保与实体类匹配（免费课程版本）
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 1. 删除现有的course表（如果存在）
DROP TABLE IF EXISTS user_favorite;
DROP TABLE IF EXISTS course_comment;
DROP TABLE IF EXISTS learning_progress;
DROP TABLE IF EXISTS study_record;
DROP TABLE IF EXISTS course_lesson;
DROP TABLE IF EXISTS course_chapter;
DROP TABLE IF EXISTS course;

-- 2. 重新创建course表，确保字段与实体类匹配（移除Price字段）
CREATE TABLE course (
    `ArticleID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `Title` VARCHAR(255) NOT NULL,
    `Content` TEXT,
    `Author` VARCHAR(100),
    `Category` VARCHAR(50),
    `Tags` TEXT,
    `Views` INT DEFAULT 0,
    `Rating` DECIMAL(3,2) DEFAULT 0.00,
    `Duration` VARCHAR(50),
    `Lessons` INT DEFAULT 0,
    `PublicationDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `Difficulty` ENUM('beginner', 'intermediate', 'advanced') DEFAULT 'beginner',
    `CoverImage` VARCHAR(255),
    `AuthorAvatar` VARCHAR(255),
    `Status` VARCHAR(50) DEFAULT 'active',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. 创建user_favorite表
CREATE TABLE user_favorite (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES user(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES course(`ArticleID`) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_course_favorite` (`user_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 插入示例课程数据（免费课程）
INSERT INTO course (Title, Content, Author, Category, Tags, Views, Rating, Duration, Lessons, Difficulty, CoverImage) VALUES
('Python基础入门', '从零开始学习Python编程语言，掌握基础语法和编程思维', '张老师', '编程开发', 'Python,编程,入门', 1200, 4.8, '20小时', 15, 'beginner', '/course_images/2169.jpg'),
('数据结构与算法', '掌握计算机科学的核心概念，提升编程能力', '李老师', '编程开发', '算法,数据结构,编程', 980, 4.9, '30小时', 20, 'intermediate', '/course_images/2170.jpg'),
('Web开发实战', '学习HTML、CSS、JavaScript，构建现代化网站', '王老师', 'Web开发', 'HTML,CSS,JavaScript,Web', 850, 4.7, '25小时', 18, 'beginner', '/course_images/2171.jpg'),
('机器学习基础', '入门机器学习，理解核心概念和算法', '陈老师', '人工智能', '机器学习,AI,算法', 650, 4.6, '35小时', 25, 'advanced', '/course_images/2172.jpg'),
('数据库设计', '学习数据库设计原理和SQL编程', '刘老师', '数据科学', '数据库,SQL,设计', 720, 4.8, '22小时', 16, 'intermediate', '/course_images/2173.jpg');

-- 5. 插入一些示例收藏数据（假设用户ID为9）
INSERT INTO user_favorite (user_id, course_id) VALUES
(9, 1),
(9, 2),
(9, 3);

-- 6. 验证表结构和数据
SELECT 'Tables created successfully!' as Status;
SHOW TABLES LIKE 'course';
SHOW TABLES LIKE 'user_favorite';

-- 7. 查看课程数据
SELECT * FROM course;

-- 8. 查看收藏数据
SELECT uf.*, c.Title as CourseTitle 
FROM user_favorite uf 
LEFT JOIN course c ON uf.course_id = c.ArticleID;
