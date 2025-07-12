-- 数据库初始化脚本
-- 包含测试数据

-- 插入测试用户
INSERT INTO user (phone, account, password, nickname, avatar, group_type, intest_types, email) VALUES
('13800138001', 'testuser1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户1', 'https://picsum.photos/100/100?random=1', 1, 'Python,Java', 'test1@example.com'),
('13800138002', 'testuser2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户2', 'https://picsum.photos/100/100?random=2', 2, 'Python,JavaScript', 'test2@example.com'),
('13800138003', 'testuser3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户3', 'https://picsum.photos/100/100?random=3', 1, 'Python,Go', 'test3@example.com');

-- 插入测试课程
INSERT INTO course (Title, Content, Author, Category, Tags, Views, Price, Rating, Duration, Lessons, Difficulty, CoverImage, AuthorAvatar) VALUES
('Python基础入门到精通', '从环境搭建到项目实战，全面系统地学习Python编程基础，适合零基础学习者。本课程将带你从零开始，逐步掌握Python编程的核心概念和实践技能。', '张教授', 'Python基础', 'Python,编程,学习,入门', 12458, 0.00, 4.8, '2小时', 15, 'beginner', 'https://picsum.photos/800/400?random=1', 'https://picsum.photos/100/100?random=1'),
('Python进阶开发', '深入学习Python高级特性，包括装饰器、生成器、异步编程等，提升编程技能。', '李老师', 'Python进阶', 'Python,高级,开发', 8567, 99.00, 4.6, '3小时', 20, 'intermediate', 'https://picsum.photos/800/400?random=2', 'https://picsum.photos/100/100?random=2'),
('Python Web开发实战', '使用Django和Flask框架进行Web开发，从零开始构建完整的Web应用。', '王老师', 'Web开发', 'Python,Django,Flask,Web', 6543, 199.00, 4.7, '4小时', 25, 'advanced', 'https://picsum.photos/800/400?random=3', 'https://picsum.photos/100/100?random=3'),
('Python数据分析', '学习使用Python进行数据分析，掌握pandas、numpy、matplotlib等库的使用。', '赵老师', '数据分析', 'Python,数据分析,pandas,numpy', 7890, 149.00, 4.5, '3.5小时', 18, 'intermediate', 'https://picsum.photos/800/400?random=4', 'https://picsum.photos/100/100?random=4');

-- 插入课程章节
INSERT INTO course_chapter (CourseID, Title, Description, OrderIndex, Duration) VALUES
(1, '第一章：Python基础入门', '学习Python的基本语法和数据类型', 1, 7200),
(1, '第二章：函数与模块', '掌握函数定义和模块使用', 2, 5400),
(1, '第三章：面向对象编程', '学习Python的面向对象特性', 3, 6000),
(2, '第一章：高级语法特性', '深入学习Python高级语法', 1, 9000),
(2, '第二章：装饰器与元编程', '掌握装饰器和元编程技术', 2, 7200),
(3, '第一章：Web开发基础', 'Web开发基础知识', 1, 6000),
(3, '第二章：Django框架', 'Django框架开发', 2, 12000),
(4, '第一章：数据分析基础', '数据分析基础知识', 1, 5400),
(4, '第二章：pandas库使用', 'pandas库详细教程', 2, 9000);

-- 插入课程课时
INSERT INTO course_lesson (ChapterID, Title, Content, VideoURL, Duration, OrderIndex, IsFree) VALUES
(1, '1.1 Python简介与环境搭建', 'Python语言介绍和环境配置', 'https://example.com/video1.mp4', 1800, 1, TRUE),
(1, '1.2 变量与数据类型', 'Python变量和基本数据类型', 'https://example.com/video2.mp4', 2400, 2, FALSE),
(1, '1.3 运算符与表达式', 'Python运算符和表达式', 'https://example.com/video3.mp4', 2100, 3, FALSE),
(1, '1.4 条件语句', 'if-else条件语句', 'https://example.com/video4.mp4', 2700, 4, FALSE),
(1, '1.5 循环语句', 'for和while循环', 'https://example.com/video5.mp4', 3000, 5, FALSE),
(2, '2.1 函数定义与调用', '函数的基本概念和使用', 'https://example.com/video6.mp4', 2400, 1, FALSE),
(2, '2.2 参数传递', '函数参数传递机制', 'https://example.com/video7.mp4', 2100, 2, FALSE),
(2, '2.3 返回值与作用域', '函数返回值和作用域', 'https://example.com/video8.mp4', 1800, 3, FALSE),
(2, '2.4 模块与包', '模块导入和包管理', 'https://example.com/video9.mp4', 2700, 4, FALSE);

-- 插入测试评论
INSERT INTO course_comment (CourseID, UserID, Content, Rating, ParentID, Likes) VALUES
(1, 1, '这个课程非常棒！老师讲解得很清楚，内容也很实用。', 5, NULL, 12),
(1, 2, '同意！我也觉得这个课程很好。', 5, 1, 3),
(1, 3, '课程内容很全面，适合初学者。', 4, NULL, 8),
(2, 1, '进阶课程很有挑战性，学到了很多。', 5, NULL, 15),
(2, 2, '老师讲解得很详细，推荐！', 4, NULL, 6),
(3, 1, 'Web开发实战课程很棒，项目很实用。', 5, NULL, 20),
(3, 3, 'Django框架讲解得很清楚。', 4, NULL, 10);

-- 插入学习进度
INSERT INTO learning_progress (UserID, CourseID, ChapterID, LessonID, Progress, Status, TimeSpent) VALUES
(1, 1, 1, 1, 100.00, 'completed', 1800),
(1, 1, 1, 2, 100.00, 'completed', 2400),
(1, 1, 1, 3, 60.00, 'in_progress', 1260),
(1, 1, 1, 4, 0.00, 'not_started', 0),
(1, 1, 1, 5, 0.00, 'not_started', 0),
(2, 1, 1, 1, 100.00, 'completed', 1800),
(2, 1, 1, 2, 80.00, 'in_progress', 1920),
(3, 1, 1, 1, 50.00, 'in_progress', 900);

-- 插入学习记录
INSERT INTO study_record (UserID, CourseID, LessonID, StudyTime, Progress, Completed) VALUES
(1, 1, 1, 1800, 100.00, TRUE),
(1, 1, 2, 2400, 100.00, TRUE),
(1, 1, 3, 1260, 60.00, FALSE),
(2, 1, 1, 1800, 100.00, TRUE),
(2, 1, 2, 1920, 80.00, FALSE),
(3, 1, 1, 900, 50.00, FALSE);

-- 用户收藏课程表
CREATE TABLE IF NOT EXISTS user_favorite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(article_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_course (user_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏课程表';

-- 插入一些测试数据
INSERT INTO user_favorite (user_id, course_id) VALUES 
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 4); 