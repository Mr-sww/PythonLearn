-- 创建课程相关表的SQL脚本
-- 请在MySQL数据库中执行此脚本

USE pythonlearn;

-- 1. 创建课程表
CREATE TABLE IF NOT EXISTS course (
    `ArticleID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `Title` VARCHAR(255) NOT NULL,
    `Content` TEXT,
    `Author` VARCHAR(100),
    `Category` VARCHAR(50),
    `Tags` TEXT,
    `Views` INT DEFAULT 0,
    `Price` DECIMAL(10,2) DEFAULT 0.00,
    `Rating` DECIMAL(3,2) DEFAULT 0.00,
    `Duration` VARCHAR(50),
    `Lessons` INT DEFAULT 0,
    `PublicationDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `Difficulty` ENUM('beginner', 'intermediate', 'advanced') DEFAULT 'beginner',
    `CoverImage` VARCHAR(255),
    `AuthorAvatar` VARCHAR(255),
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 创建用户收藏表
CREATE TABLE IF NOT EXISTS user_favorite (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES user(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES course(`ArticleID`) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_course_favorite` (`user_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. 创建课程评论表
CREATE TABLE IF NOT EXISTS course_comment (
    `CommentID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `CourseID` INT NOT NULL,
    `UserID` INT NOT NULL,
    `Content` TEXT NOT NULL,
    `Rating` INT DEFAULT 5 CHECK (Rating >= 1 AND Rating <= 5),
    `ParentID` INT DEFAULT NULL,
    `Likes` INT DEFAULT 0,
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`CourseID`) REFERENCES course(`ArticleID`) ON DELETE CASCADE,
    FOREIGN KEY (`UserID`) REFERENCES user(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`ParentID`) REFERENCES course_comment(`CommentID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 创建学习进度表
CREATE TABLE IF NOT EXISTS learning_progress (
    `ProgressID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `UserID` INT NOT NULL,
    `CourseID` INT NOT NULL,
    `ChapterID` INT DEFAULT NULL,
    `LessonID` INT DEFAULT NULL,
    `Progress` DECIMAL(5,2) DEFAULT 0.00 CHECK (Progress >= 0 AND Progress <= 100),
    `Status` ENUM('not_started', 'in_progress', 'completed') DEFAULT 'not_started',
    `TimeSpent` INT DEFAULT 0 COMMENT '学习时长（秒）',
    `LastStudyTime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`UserID`) REFERENCES user(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`CourseID`) REFERENCES course(`ArticleID`) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_course` (`UserID`, `CourseID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. 创建课程章节表
CREATE TABLE IF NOT EXISTS course_chapter (
    `ChapterID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `CourseID` INT NOT NULL,
    `Title` VARCHAR(255) NOT NULL,
    `Description` TEXT,
    `OrderIndex` INT DEFAULT 0,
    `Duration` INT DEFAULT 0 COMMENT '章节时长（秒）',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`CourseID`) REFERENCES course(`ArticleID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. 创建课程课时表
CREATE TABLE IF NOT EXISTS course_lesson (
    `LessonID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ChapterID` INT NOT NULL,
    `Title` VARCHAR(255) NOT NULL,
    `Content` TEXT,
    `VideoURL` VARCHAR(1024),
    `Duration` INT DEFAULT 0 COMMENT '课时时长（秒）',
    `OrderIndex` INT DEFAULT 0,
    `IsFree` BOOLEAN DEFAULT FALSE,
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`ChapterID`) REFERENCES course_chapter(`ChapterID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. 创建学习记录表
CREATE TABLE IF NOT EXISTS study_record (
    `RecordID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `UserID` INT NOT NULL,
    `CourseID` INT NOT NULL,
    `LessonID` INT NOT NULL,
    `StudyTime` INT DEFAULT 0 COMMENT '本次学习时长（秒）',
    `Progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '本次学习进度百分比',
    `Completed` BOOLEAN DEFAULT FALSE,
    `StudyDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`UserID`) REFERENCES user(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`CourseID`) REFERENCES course(`ArticleID`) ON DELETE CASCADE,
    FOREIGN KEY (`LessonID`) REFERENCES course_lesson(`LessonID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. 插入一些示例课程数据
INSERT INTO course (Title, Content, Author, Category, Tags, Views, Rating, Duration, Lessons, Difficulty, CoverImage) VALUES
('Python基础入门', '从零开始学习Python编程语言，掌握基础语法和编程思维', '张老师', '编程开发', 'Python,编程,入门', 1200, 4.8, '20小时', 15, 'beginner', '/course_images/2169.jpg'),
('数据结构与算法', '掌握计算机科学的核心概念，提升编程能力', '李老师', '编程开发', '算法,数据结构,编程', 980, 4.9, '30小时', 20, 'intermediate', '/course_images/2170.jpg'),
('Web开发实战', '学习HTML、CSS、JavaScript，构建现代化网站', '王老师', 'Web开发', 'HTML,CSS,JavaScript,Web', 850, 4.7, '25小时', 18, 'beginner', '/course_images/2171.jpg'),
('机器学习基础', '入门机器学习，理解核心概念和算法', '陈老师', '人工智能', '机器学习,AI,算法', 650, 4.6, '35小时', 25, 'advanced', '/course_images/2172.jpg'),
('数据库设计', '学习数据库设计原理和SQL编程', '刘老师', '数据科学', '数据库,SQL,设计', 720, 4.8, '22小时', 16, 'intermediate', '/course_images/2173.jpg');

-- 9. 验证表创建成功
SHOW TABLES LIKE 'course';
SHOW TABLES LIKE 'user_favorite';
SHOW TABLES LIKE 'course_comment';
SHOW TABLES LIKE 'learning_progress';

-- 10. 查看课程数据
SELECT * FROM course LIMIT 5;

