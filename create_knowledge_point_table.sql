-- 创建知识点表
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 创建知识点表
CREATE TABLE IF NOT EXISTS `knowledge_point` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '知识点ID',
    `title` VARCHAR(255) NOT NULL COMMENT '知识点标题',
    `content` TEXT COMMENT '知识点内容',
    `question` TEXT COMMENT '关联的题目ID列表(逗号分隔)',
    `url` VARCHAR(1000) COMMENT '相关链接',
    `stage` VARCHAR(50) DEFAULT '1.1' COMMENT '学习阶段',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点表';

-- 插入示例数据
INSERT INTO `knowledge_point` (`title`, `content`, `question`, `stage`) VALUES
('Python基础语法', 'Python是一种解释型、面向对象、动态数据类型的高级程序设计语言。本章将介绍Python的基本语法规则。', 'P1001,P1002,P1003', '1.1'),
('数据类型与变量', 'Python中的基本数据类型包括数字、字符串、列表、元组、字典等。变量是存储数据的容器。', 'P1004,P1005,P1006', '1.2'),
('控制流语句', '控制流语句包括条件语句（if-elif-else）和循环语句（for、while），用于控制程序的执行流程。', 'P1007,P1008,P1009', '1.3'),
('函数定义与调用', '函数是一段可重用的代码块，可以接受参数并返回值。本章介绍如何定义和调用函数。', 'P1010,P1011,P1012', '1.4'),
('面向对象编程', '面向对象编程是一种编程范式，使用类和对象来组织代码。本章介绍类、对象、继承等概念。', 'P1013,P1014,P1015', '2.1'),
('文件操作', 'Python提供了丰富的文件操作功能，包括文件的读写、目录操作等。', 'P1016,P1017,P1018', '2.2'),
('异常处理', '异常处理是程序健壮性的重要组成部分，本章介绍try-except语句的使用。', 'P1019,P1020,P1021', '2.3'),
('模块与包', '模块是Python代码的组织单位，包是模块的集合。本章介绍如何创建和使用模块。', 'P1022,P1023,P1024', '2.4');

-- 验证表结构和数据
SELECT 'Table created successfully!' as Status;
SHOW TABLES LIKE 'knowledge_point';
SELECT * FROM knowledge_point;
