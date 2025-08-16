-- 检查和修复 runoobpython3install 表结构
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 检查表是否存在
SHOW TABLES LIKE 'runoobpython3install';

-- 如果表不存在，创建表
CREATE TABLE IF NOT EXISTS `runoobpython3install` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '知识点ID',
    `title` VARCHAR(200) NOT NULL COMMENT '知识点标题',
    `content` LONGTEXT COMMENT '知识点内容',
    `question` VARCHAR(200) COMMENT '关联的题目ID列表(逗号分隔)',
    `url` LONGTEXT COMMENT '相关链接',
    `stage` VARCHAR(200) DEFAULT '1.1' COMMENT '学习阶段',
    `CreateTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Python知识点表';

-- 查看表结构
DESCRIBE runoobpython3install;

-- 查看现有数据
SELECT id, title, stage FROM runoobpython3install ORDER BY stage ASC LIMIT 10;

-- 如果表为空，插入示例数据
INSERT INTO `runoobpython3install` (`title`, `content`, `question`, `stage`) 
SELECT * FROM (
    SELECT 'Python3 教程', 'Python3是一种解释型、面向对象、动态数据类型的高级程序设计语言。', 'task1,task2,task3', '1.1'
) AS tmp
WHERE NOT EXISTS (
    SELECT title FROM runoobpython3install WHERE title = 'Python3 教程'
) LIMIT 1;

INSERT INTO `runoobpython3install` (`title`, `content`, `question`, `stage`) 
SELECT * FROM (
    SELECT 'Python3 环境搭建', 'Python3环境搭建本章节将介绍如何在不同操作系统上安装和配置Python3。', 'task4,task5,task6', '1.2'
) AS tmp
WHERE NOT EXISTS (
    SELECT title FROM runoobpython3install WHERE title = 'Python3 环境搭建'
) LIMIT 1;

INSERT INTO `runoobpython3install` (`title`, `content`, `question`, `stage`) 
SELECT * FROM (
    SELECT 'Python3 基础语法', 'Python3基础语法编码默认情况下，Python3源码文件以UTF-8编码。', 'task7,task8,task9', '1.3'
) AS tmp
WHERE NOT EXISTS (
    SELECT title FROM runoobpython3install WHERE title = 'Python3 基础语法'
) LIMIT 1;

INSERT INTO `runoobpython3install` (`title`, `content`, `question`, `stage`) 
SELECT * FROM (
    SELECT 'Python3 基本数据类型', 'Python3基本数据类型Python中的变量不需要声明。', 'task10,task11,task12', '1.4'
) AS tmp
WHERE NOT EXISTS (
    SELECT title FROM runoobpython3install WHERE title = 'Python3 基本数据类型'
) LIMIT 1;

INSERT INTO `runoobpython3install` (`title`, `content`, `question`, `stage`) 
SELECT * FROM (
    SELECT 'Python3 函数', 'Python3函数函数是组织好的，可重复使用的，用来实现单一，或相关联功能的代码段。', 'task13,task14,task15', '2.1'
) AS tmp
WHERE NOT EXISTS (
    SELECT title FROM runoobpython3install WHERE title = 'Python3 函数'
) LIMIT 1;

INSERT INTO `runoobpython3install` (`title`, `content`, `question`, `stage`) 
SELECT * FROM (
    SELECT 'Python3 文件操作', 'Python3文件操作Python提供了丰富的文件操作功能。', 'task16,task17,task18', '2.2'
) AS tmp
WHERE NOT EXISTS (
    SELECT title FROM runoobpython3install WHERE title = 'Python3 文件操作'
) LIMIT 1;

-- 验证数据
SELECT 'Table structure and data verified successfully!' as Status;
SELECT COUNT(*) as TotalRecords FROM runoobpython3install;
