-- 修复数据库表结构 - 简化版本
-- 注意：如果列已存在，这些语句会报错，可以忽略错误继续执行

-- 1. 为 user 表添加 status 列
ALTER TABLE `user` ADD COLUMN `status` VARCHAR(20) DEFAULT 'active' COMMENT '用户状态: active-正常, inactive-禁用';

-- 2. 为 user_problem_record 表添加缺失的列
ALTER TABLE `user_problem_record` ADD COLUMN `execution_time` INT DEFAULT 0 COMMENT '执行时间(毫秒)';
ALTER TABLE `user_problem_record` ADD COLUMN `memory_usage` INT DEFAULT 0 COMMENT '内存使用量(KB)';
ALTER TABLE `user_problem_record` ADD COLUMN `score` INT DEFAULT 0 COMMENT '得分';

-- 3. 为 assignment_submission 表添加缺失的列
ALTER TABLE `assignment_submission` ADD COLUMN `score` DOUBLE DEFAULT NULL COMMENT '得分';
ALTER TABLE `assignment_submission` ADD COLUMN `feedback` TEXT COMMENT '反馈';
ALTER TABLE `assignment_submission` ADD COLUMN `grade_time` DATETIME DEFAULT NULL COMMENT '评分时间';
ALTER TABLE `assignment_submission` ADD COLUMN `status` VARCHAR(20) DEFAULT 'submitted' COMMENT '状态: submitted-已提交, graded-已评分';

-- 4. 为 assignment 表添加缺失的列
ALTER TABLE `assignment` ADD COLUMN `max_score` INT DEFAULT 100 COMMENT '最高分';
ALTER TABLE `assignment` ADD COLUMN `status` VARCHAR(20) DEFAULT 'active' COMMENT '状态: active-活跃, inactive-禁用';

-- 5. 更新现有数据
UPDATE `user` SET `status` = 'active' WHERE `status` IS NULL;
UPDATE `user_problem_record` SET `execution_time` = 0 WHERE `execution_time` IS NULL;
UPDATE `user_problem_record` SET `memory_usage` = 0 WHERE `memory_usage` IS NULL;
UPDATE `user_problem_record` SET `score` = 0 WHERE `score` IS NULL;
UPDATE `assignment_submission` SET `status` = 'submitted' WHERE `status` IS NULL;
UPDATE `assignment` SET `max_score` = 100 WHERE `max_score` IS NULL;
UPDATE `assignment` SET `status` = 'active' WHERE `status` IS NULL;
