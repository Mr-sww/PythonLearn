-- =====================================================
-- 学习记录系统数据库创建脚本
-- 包含学习记录、学习进度、课程章节等表结构
-- =====================================================

-- 使用数据库
USE pythonlearn;

-- =====================================================
-- 1. 学习记录表 (study_record)
-- =====================================================
DROP TABLE IF EXISTS study_record;
CREATE TABLE study_record (
    `RecordID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    `UserID` INT NOT NULL COMMENT '用户ID',
    `CourseID` INT NOT NULL COMMENT '课程ID',
    `LessonID` INT NOT NULL COMMENT '课时ID',
    `StudyTime` INT DEFAULT 0 COMMENT '本次学习时长（秒）',
    `Progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '本次学习进度百分比',
    `Completed` BOOLEAN DEFAULT FALSE COMMENT '是否完成',
    `StudyDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '学习日期',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_course` (`UserID`, `CourseID`),
    INDEX `idx_study_date` (`StudyDate`),
    INDEX `idx_user_date` (`UserID`, `StudyDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习记录表';

-- =====================================================
-- 2. 学习进度表 (learning_progress)
-- =====================================================
DROP TABLE IF EXISTS learning_progress;
CREATE TABLE learning_progress (
    `ProgressID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '进度ID',
    `UserID` INT NOT NULL COMMENT '用户ID',
    `CourseID` INT NOT NULL COMMENT '课程ID',
    `ChapterID` INT DEFAULT NULL COMMENT '章节ID',
    `LessonID` INT DEFAULT NULL COMMENT '课时ID',
    `Progress` DECIMAL(5,2) DEFAULT 0.00 CHECK (Progress >= 0 AND Progress <= 100) COMMENT '学习进度百分比',
    `Status` ENUM('not_started', 'in_progress', 'completed') DEFAULT 'not_started' COMMENT '学习状态',
    `TimeSpent` INT DEFAULT 0 COMMENT '总学习时长（秒）',
    `LastStudyTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后学习时间',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `unique_user_course` (`UserID`, `CourseID`),
    INDEX `idx_user_status` (`UserID`, `Status`),
    INDEX `idx_course_progress` (`CourseID`, `Progress`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习进度表';

-- =====================================================
-- 3. 课程章节表 (course_chapter)
-- =====================================================
DROP TABLE IF EXISTS course_chapter;
CREATE TABLE course_chapter (
    `ChapterID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '章节ID',
    `CourseID` INT NOT NULL COMMENT '课程ID',
    `Title` NVARCHAR(255) NOT NULL COMMENT '章节标题',
    `Description` TEXT COMMENT '章节描述',
    `OrderIndex` INT DEFAULT 0 COMMENT '排序索引',
    `Duration` INT DEFAULT 0 COMMENT '章节时长（秒）',
    `IsFree` BOOLEAN DEFAULT FALSE COMMENT '是否免费',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_course_order` (`CourseID`, `OrderIndex`),
    INDEX `idx_course_free` (`CourseID`, `IsFree`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程章节表';

-- =====================================================
-- 4. 课程课时表 (course_lesson)
-- =====================================================
DROP TABLE IF EXISTS course_lesson;
CREATE TABLE course_lesson (
    `LessonID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '课时ID',
    `ChapterID` INT NOT NULL COMMENT '章节ID',
    `Title` NVARCHAR(255) NOT NULL COMMENT '课时标题',
    `Content` TEXT COMMENT '课时内容',
    `VideoURL` NVARCHAR(1024) COMMENT '视频URL',
    `Duration` INT DEFAULT 0 COMMENT '课时时长（秒）',
    `OrderIndex` INT DEFAULT 0 COMMENT '排序索引',
    `IsFree` BOOLEAN DEFAULT FALSE COMMENT '是否免费',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_chapter_order` (`ChapterID`, `OrderIndex`),
    INDEX `idx_chapter_free` (`ChapterID`, `IsFree`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程课时表';

-- =====================================================
-- 5. 学习统计表 (learning_statistics)
-- =====================================================
DROP TABLE IF EXISTS learning_statistics;
CREATE TABLE learning_statistics (
    `StatID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    `UserID` INT NOT NULL COMMENT '用户ID',
    `TotalCourses` INT DEFAULT 0 COMMENT '学习课程总数',
    `CompletedCourses` INT DEFAULT 0 COMMENT '完成课程数',
    `TotalStudyTime` INT DEFAULT 0 COMMENT '总学习时长（秒）',
    `ContinuousDays` INT DEFAULT 0 COMMENT '连续学习天数',
    `LastStudyDate` DATE COMMENT '最后学习日期',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `unique_user_stat` (`UserID`),
    INDEX `idx_user_continuous` (`UserID`, `ContinuousDays`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习统计表';

-- =====================================================
-- 6. 学习目标表 (learning_goals)
-- =====================================================
DROP TABLE IF EXISTS learning_goals;
CREATE TABLE learning_goals (
    `GoalID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '目标ID',
    `UserID` INT NOT NULL COMMENT '用户ID',
    `Title` NVARCHAR(255) NOT NULL COMMENT '目标标题',
    `Description` TEXT COMMENT '目标描述',
    `TargetDate` DATE COMMENT '目标完成日期',
    `Status` ENUM('pending', 'in_progress', 'completed', 'cancelled') DEFAULT 'pending' COMMENT '目标状态',
    `Progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '目标进度',
    `CreatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_status` (`UserID`, `Status`),
    INDEX `idx_target_date` (`TargetDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习目标表';

-- =====================================================
-- 外键约束
-- =====================================================

-- 学习记录表外键
ALTER TABLE study_record 
ADD CONSTRAINT fk_study_record_user 
FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE;

ALTER TABLE study_record 
ADD CONSTRAINT fk_study_record_course 
FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE;

ALTER TABLE study_record 
ADD CONSTRAINT fk_study_record_lesson 
FOREIGN KEY (LessonID) REFERENCES course_lesson(LessonID) ON DELETE CASCADE;

-- 学习进度表外键
ALTER TABLE learning_progress 
ADD CONSTRAINT fk_learning_progress_user 
FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE;

ALTER TABLE learning_progress 
ADD CONSTRAINT fk_learning_progress_course 
FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE;

ALTER TABLE learning_progress 
ADD CONSTRAINT fk_learning_progress_chapter 
FOREIGN KEY (ChapterID) REFERENCES course_chapter(ChapterID) ON DELETE SET NULL;

ALTER TABLE learning_progress 
ADD CONSTRAINT fk_learning_progress_lesson 
FOREIGN KEY (LessonID) REFERENCES course_lesson(LessonID) ON DELETE SET NULL;

-- 课程章节表外键
ALTER TABLE course_chapter 
ADD CONSTRAINT fk_course_chapter_course 
FOREIGN KEY (CourseID) REFERENCES course(ArticleID) ON DELETE CASCADE;

-- 课程课时表外键
ALTER TABLE course_lesson 
ADD CONSTRAINT fk_course_lesson_chapter 
FOREIGN KEY (ChapterID) REFERENCES course_chapter(ChapterID) ON DELETE CASCADE;

-- 学习统计表外键
ALTER TABLE learning_statistics 
ADD CONSTRAINT fk_learning_statistics_user 
FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE;

-- 学习目标表外键
ALTER TABLE learning_goals 
ADD CONSTRAINT fk_learning_goals_user 
FOREIGN KEY (UserID) REFERENCES user(user_id) ON DELETE CASCADE;

-- =====================================================
-- 插入示例数据
-- =====================================================

-- 插入课程章节数据
INSERT INTO course_chapter (CourseID, Title, Description, OrderIndex, Duration, IsFree) VALUES
(2169, '第一章：Python基础入门', 'Python编程基础知识和环境搭建', 1, 3600, TRUE),
(2169, '第二章：数据类型和变量', 'Python基本数据类型和变量使用', 2, 4800, TRUE),
(2169, '第三章：控制流程', '条件语句和循环语句', 3, 5400, FALSE),
(2170, '第一章：数据结构基础', '列表、元组、字典等数据结构', 1, 4200, TRUE),
(2170, '第二章：函数和模块', '函数定义和模块导入', 2, 6000, FALSE),
(2171, '第一章：面向对象编程', '类和对象的概念', 1, 7200, TRUE),
(2171, '第二章：继承和多态', '面向对象高级特性', 2, 5400, FALSE);

-- 插入课程课时数据
INSERT INTO course_lesson (ChapterID, Title, Content, VideoURL, Duration, OrderIndex, IsFree) VALUES
(1, '1.1 Python简介', 'Python语言特点和优势', 'https://example.com/video1.mp4', 1800, 1, TRUE),
(1, '1.2 环境搭建', 'Python开发环境配置', 'https://example.com/video2.mp4', 1200, 2, TRUE),
(1, '1.3 第一个程序', 'Hello World程序编写', 'https://example.com/video3.mp4', 600, 3, TRUE),
(2, '2.1 数字类型', '整数、浮点数运算', 'https://example.com/video4.mp4', 2400, 1, TRUE),
(2, '2.2 字符串操作', '字符串处理和方法', 'https://example.com/video5.mp4', 2400, 2, FALSE),
(3, '3.1 if条件语句', '条件判断和分支', 'https://example.com/video6.mp4', 2700, 1, FALSE),
(3, '3.2 for循环', 'for循环的使用', 'https://example.com/video7.mp4', 2700, 2, FALSE);

-- 插入学习记录测试数据
INSERT INTO study_record (UserID, CourseID, LessonID, StudyTime, Progress, Completed, StudyDate) VALUES
-- 用户1的学习记录
(1, 2169, 1, 1800, 100.0, 1, '2025-01-17 14:30:00'),
(1, 2169, 2, 1200, 100.0, 1, '2025-01-17 16:00:00'),
(1, 2169, 3, 600, 100.0, 1, '2025-01-17 17:30:00'),
(1, 2169, 4, 2400, 85.0, 0, '2025-01-16 10:15:00'),
(1, 2169, 5, 1800, 60.0, 0, '2025-01-16 14:20:00'),
(1, 2170, 1, 2100, 100.0, 1, '2025-01-15 09:45:00'),
(1, 2170, 2, 3000, 75.0, 0, '2025-01-15 15:30:00'),
(1, 2171, 1, 3600, 100.0, 1, '2025-01-14 11:00:00'),
(1, 2171, 2, 2700, 50.0, 0, '2025-01-14 16:45:00'),

-- 用户2的学习记录
(2, 2169, 1, 1800, 100.0, 1, '2025-01-17 11:20:00'),
(2, 2169, 2, 1200, 100.0, 1, '2025-01-17 13:00:00'),
(2, 2169, 3, 600, 100.0, 1, '2025-01-17 14:30:00'),
(2, 2169, 4, 2400, 90.0, 0, '2025-01-16 08:15:00'),
(2, 2170, 1, 2100, 100.0, 1, '2025-01-15 10:30:00'),
(2, 2170, 2, 3000, 80.0, 0, '2025-01-15 16:00:00'),

-- 用户3的学习记录
(3, 2169, 1, 900, 50.0, 0, '2025-01-17 08:30:00'),
(3, 2169, 2, 600, 30.0, 0, '2025-01-16 19:20:00'),
(3, 2170, 1, 1200, 60.0, 0, '2025-01-15 20:15:00');

-- 插入学习进度数据
INSERT INTO learning_progress (UserID, CourseID, ChapterID, LessonID, Progress, Status, TimeSpent, LastStudyTime) VALUES
(1, 2169, 1, 3, 100.0, 'completed', 3600, '2025-01-17 17:30:00'),
(1, 2169, 2, 5, 72.5, 'in_progress', 4200, '2025-01-16 14:20:00'),
(1, 2170, 1, 1, 100.0, 'completed', 2100, '2025-01-15 09:45:00'),
(1, 2170, 2, 2, 75.0, 'in_progress', 3000, '2025-01-15 15:30:00'),
(1, 2171, 1, 1, 100.0, 'completed', 3600, '2025-01-14 11:00:00'),
(1, 2171, 2, 2, 50.0, 'in_progress', 2700, '2025-01-14 16:45:00'),

(2, 2169, 1, 3, 100.0, 'completed', 3600, '2025-01-17 14:30:00'),
(2, 2169, 2, 4, 90.0, 'in_progress', 2400, '2025-01-16 08:15:00'),
(2, 2170, 1, 1, 100.0, 'completed', 2100, '2025-01-15 10:30:00'),
(2, 2170, 2, 2, 80.0, 'in_progress', 3000, '2025-01-15 16:00:00'),

(3, 2169, 1, 1, 50.0, 'in_progress', 900, '2025-01-17 08:30:00'),
(3, 2170, 1, 1, 60.0, 'in_progress', 1200, '2025-01-15 20:15:00');

-- 插入学习统计数据
INSERT INTO learning_statistics (UserID, TotalCourses, CompletedCourses, TotalStudyTime, ContinuousDays, LastStudyDate) VALUES
(1, 3, 1, 18900, 4, '2025-01-17'),
(2, 2, 1, 13200, 3, '2025-01-17'),
(3, 2, 0, 2700, 3, '2025-01-17');

-- 插入学习目标数据
INSERT INTO learning_goals (UserID, Title, Description, TargetDate, Status, Progress) VALUES
(1, '完成Python基础课程', '掌握Python基础语法和编程概念', '2025-02-01', 'in_progress', 72.5),
(1, '学习数据结构', '掌握Python常用数据结构', '2025-02-15', 'pending', 0.0),
(2, 'Python入门', '完成Python基础入门学习', '2025-01-31', 'in_progress', 90.0),
(3, '开始Python学习', '开始Python编程学习之旅', '2025-02-28', 'in_progress', 25.0);

-- =====================================================
-- 创建视图
-- =====================================================

-- 用户学习概览视图
CREATE OR REPLACE VIEW user_learning_overview AS
SELECT 
    u.user_id,
    u.username,
    COUNT(DISTINCT sr.CourseID) as total_courses,
    COUNT(CASE WHEN sr.Completed = 1 THEN 1 END) as completed_lessons,
    COALESCE(SUM(sr.StudyTime), 0) as total_study_time,
    ls.ContinuousDays,
    ls.LastStudyDate
FROM user u
LEFT JOIN study_record sr ON u.user_id = sr.UserID
LEFT JOIN learning_statistics ls ON u.user_id = ls.UserID
GROUP BY u.user_id, u.username, ls.ContinuousDays, ls.LastStudyDate;

-- 课程学习进度视图
CREATE OR REPLACE VIEW course_progress_view AS
SELECT 
    lp.UserID,
    lp.CourseID,
    c.Title as CourseTitle,
    c.CoverImage,
    lp.Progress,
    lp.Status,
    lp.TimeSpent,
    lp.LastStudyTime,
    COUNT(sr.RecordID) as total_records,
    COUNT(CASE WHEN sr.Completed = 1 THEN 1 END) as completed_records
FROM learning_progress lp
JOIN course c ON lp.CourseID = c.ArticleID
LEFT JOIN study_record sr ON lp.UserID = sr.UserID AND lp.CourseID = sr.CourseID
GROUP BY lp.UserID, lp.CourseID, c.Title, c.CoverImage, lp.Progress, lp.Status, lp.TimeSpent, lp.LastStudyTime;

-- =====================================================
-- 创建存储过程
-- =====================================================

DELIMITER //

-- 更新用户学习统计的存储过程
CREATE PROCEDURE UpdateUserLearningStatistics(IN p_user_id INT)
BEGIN
    DECLARE v_total_courses INT DEFAULT 0;
    DECLARE v_completed_courses INT DEFAULT 0;
    DECLARE v_total_study_time INT DEFAULT 0;
    DECLARE v_continuous_days INT DEFAULT 0;
    DECLARE v_last_study_date DATE DEFAULT NULL;
    
    -- 计算总课程数
    SELECT COUNT(DISTINCT CourseID) INTO v_total_courses
    FROM study_record 
    WHERE UserID = p_user_id;
    
    -- 计算完成课程数（进度>=90%的课程）
    SELECT COUNT(DISTINCT CourseID) INTO v_completed_courses
    FROM learning_progress 
    WHERE UserID = p_user_id AND Progress >= 90;
    
    -- 计算总学习时长
    SELECT COALESCE(SUM(StudyTime), 0) INTO v_total_study_time
    FROM study_record 
    WHERE UserID = p_user_id;
    
    -- 计算连续学习天数
    SELECT COUNT(DISTINCT DATE(StudyDate)) INTO v_continuous_days
    FROM study_record 
    WHERE UserID = p_user_id 
    AND StudyDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY);
    
    -- 获取最后学习日期
    SELECT DATE(MAX(StudyDate)) INTO v_last_study_date
    FROM study_record 
    WHERE UserID = p_user_id;
    
    -- 插入或更新统计信息
    INSERT INTO learning_statistics (UserID, TotalCourses, CompletedCourses, TotalStudyTime, ContinuousDays, LastStudyDate)
    VALUES (p_user_id, v_total_courses, v_completed_courses, v_total_study_time, v_continuous_days, v_last_study_date)
    ON DUPLICATE KEY UPDATE
        TotalCourses = v_total_courses,
        CompletedCourses = v_completed_courses,
        TotalStudyTime = v_total_study_time,
        ContinuousDays = v_continuous_days,
        LastStudyDate = v_last_study_date,
        UpdatedAt = CURRENT_TIMESTAMP;
END //

-- 记录学习进度的存储过程
CREATE PROCEDURE RecordStudyProgress(
    IN p_user_id INT,
    IN p_course_id INT,
    IN p_lesson_id INT,
    IN p_study_time INT,
    IN p_progress DECIMAL(5,2),
    IN p_completed BOOLEAN
)
BEGIN
    DECLARE v_chapter_id INT DEFAULT NULL;
    
    -- 获取课时所属的章节ID
    SELECT ChapterID INTO v_chapter_id
    FROM course_lesson
    WHERE LessonID = p_lesson_id;
    
    -- 插入学习记录
    INSERT INTO study_record (UserID, CourseID, LessonID, StudyTime, Progress, Completed, StudyDate)
    VALUES (p_user_id, p_course_id, p_lesson_id, p_study_time, p_progress, p_completed, NOW());
    
    -- 更新或插入学习进度
    INSERT INTO learning_progress (UserID, CourseID, ChapterID, LessonID, Progress, Status, TimeSpent, LastStudyTime)
    VALUES (p_user_id, p_course_id, v_chapter_id, p_lesson_id, p_progress, 
            CASE WHEN p_completed = 1 THEN 'completed' ELSE 'in_progress' END,
            p_study_time, NOW())
    ON DUPLICATE KEY UPDATE
        ChapterID = v_chapter_id,
        LessonID = p_lesson_id,
        Progress = p_progress,
        Status = CASE WHEN p_completed = 1 THEN 'completed' ELSE 'in_progress' END,
        TimeSpent = TimeSpent + p_study_time,
        LastStudyTime = NOW(),
        UpdatedAt = CURRENT_TIMESTAMP;
    
    -- 更新用户统计
    CALL UpdateUserLearningStatistics(p_user_id);
END //

DELIMITER ;

-- =====================================================
-- 创建触发器
-- =====================================================

-- 学习记录插入后更新统计的触发器
DELIMITER //
CREATE TRIGGER after_study_record_insert
AFTER INSERT ON study_record
FOR EACH ROW
BEGIN
    CALL UpdateUserLearningStatistics(NEW.UserID);
END //

-- 学习进度更新后更新统计的触发器
CREATE TRIGGER after_learning_progress_update
AFTER UPDATE ON learning_progress
FOR EACH ROW
BEGIN
    CALL UpdateUserLearningStatistics(NEW.UserID);
END //
DELIMITER ;

-- =====================================================
-- 验证数据
-- =====================================================

-- 查看创建的表
SHOW TABLES LIKE '%learning%';
SHOW TABLES LIKE '%study%';
SHOW TABLES LIKE '%course%';

-- 查看学习记录数据
SELECT 
    sr.RecordID,
    u.username,
    c.Title as CourseTitle,
    cl.Title as LessonTitle,
    sr.StudyTime,
    sr.Progress,
    sr.Completed,
    sr.StudyDate
FROM study_record sr
JOIN user u ON sr.UserID = u.user_id
JOIN course c ON sr.CourseID = c.ArticleID
JOIN course_lesson cl ON sr.LessonID = cl.LessonID
ORDER BY sr.StudyDate DESC
LIMIT 10;

-- 查看学习进度数据
SELECT 
    lp.UserID,
    u.username,
    c.Title as CourseTitle,
    lp.Progress,
    lp.Status,
    lp.TimeSpent,
    lp.LastStudyTime
FROM learning_progress lp
JOIN user u ON lp.UserID = u.user_id
JOIN course c ON lp.CourseID = c.ArticleID
ORDER BY lp.LastStudyTime DESC;

-- 查看学习统计数据
SELECT 
    ls.UserID,
    u.username,
    ls.TotalCourses,
    ls.CompletedCourses,
    ls.TotalStudyTime,
    ls.ContinuousDays,
    ls.LastStudyDate
FROM learning_statistics ls
JOIN user u ON ls.UserID = u.user_id;

-- 查看用户学习概览
SELECT * FROM user_learning_overview;

-- 查看课程进度
SELECT * FROM course_progress_view LIMIT 10;

-- 完成提示
SELECT '学习记录数据库创建完成！' as message;
