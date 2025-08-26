-- 修复user表结构，确保user_id字段正确设置为自增主键
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 1. 检查当前表结构
DESCRIBE user;

-- 2. 如果user_id字段不是自增主键，则修复表结构
-- 先删除表（如果存在数据，请先备份）
-- DROP TABLE IF EXISTS user;

-- 3. 重新创建user表，确保user_id字段正确设置
CREATE TABLE IF NOT EXISTS user (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `phone` VARCHAR(20) UNIQUE,
    `account` VARCHAR(50) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `group_type` INT DEFAULT 1,
    `intest_types` TEXT,
    `email` VARCHAR(100),
    `status` VARCHAR(20) DEFAULT 'active',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 验证表结构
DESCRIBE user;

-- 5. 插入测试用户（可选）
INSERT INTO user (phone, account, password, nickname, group_type, email) VALUES
('13800138001', 'testuser1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户1', 1, 'test1@example.com'),
('13800138002', 'testuser2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户2', 1, 'test2@example.com');

-- 6. 验证插入结果
SELECT * FROM user;

