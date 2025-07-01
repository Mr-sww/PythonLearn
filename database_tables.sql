-- 用户表
CREATE TABLE IF NOT EXISTS user (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `phone` VARCHAR(20) UNIQUE,
    `account` VARCHAR(50) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `group_type` INT DEFAULT 0,
    `intest_types` TEXT,
    `email` VARCHAR(100),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    `ArticleID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `Title` NVARCHAR(255) NOT NULL,
    `Content` TEXT,
    `Author` NVARCHAR(100),
    `Category` NVARCHAR(50),
    `Tags` TEXT,
    `Views` INT DEFAULT 0,
    `Price` DECIMAL(10,2) DEFAULT 0.00,
    `Rating` DECIMAL(3,2) DEFAULT 0.00,
    `Duration` NVARCHAR(50),
    `Lessons` INT DEFAULT 0,
    `PublicationDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `Difficulty` ENUM('beginner', 'intermediate', 'advanced') DEFAULT 'beginner',
    `CoverImage` VARCHAR(255),
    `AuthorAvatar` VARCHAR(255),
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 课程评论表
CREATE TABLE course_comment (
    `CommentID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `CourseID` INT NOT NULL,
    `UserID` INT NOT NULL,
    `Content` TEXT NOT NULL,
    `Rating` INT DEFAULT 5 CHECK (Rating >= 1 AND Rating <= 5),
    `ParentID` INT DEFAULT NULL,
    `Likes` INT DEFAULT 0,
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (ParentID) REFERENCES course_comment(CommentID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 学习进度表
CREATE TABLE learning_progress (
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
    FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_course` (`UserID`, `CourseID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 课程章节表
CREATE TABLE course_chapter (
    `ChapterID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `CourseID` INT NOT NULL,
    `Title` NVARCHAR(255) NOT NULL,
    `Description` TEXT,
    `OrderIndex` INT DEFAULT 0,
    `Duration` INT DEFAULT 0 COMMENT '章节时长（秒）',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 课程课时表
CREATE TABLE course_lesson (
    `LessonID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ChapterID` INT NOT NULL,
    `Title` NVARCHAR(255) NOT NULL,
    `Content` TEXT,
    `VideoURL` NVARCHAR(1024),
    `Duration` INT DEFAULT 0 COMMENT '课时时长（秒）',
    `OrderIndex` INT DEFAULT 0,
    `IsFree` BOOLEAN DEFAULT FALSE,
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ChapterID) REFERENCES course_chapter(ChapterID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 学习记录表
CREATE TABLE study_record (
    `RecordID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `UserID` INT NOT NULL,
    `CourseID` INT NOT NULL,
    `LessonID` INT NOT NULL,
    `StudyTime` INT DEFAULT 0 COMMENT '本次学习时长（秒）',
    `Progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '本次学习进度百分比',
    `Completed` BOOLEAN DEFAULT FALSE,
    `StudyDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE,
    FOREIGN KEY (LessonID) REFERENCES course_lesson(LessonID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户收藏表
CREATE TABLE user_favorite (
    `FavoriteID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `UserID` INT NOT NULL,
    `CourseID` INT NOT NULL,
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_course_favorite` (`UserID`, `CourseID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci; 