-- 简单修复：为course表添加URL列
-- 在MySQL中执行此脚本解决 "Unknown column 'c.URL' in 'field list'" 错误

USE pythonlearn;

-- 直接添加URL列（如果列已存在会报错，但不会影响数据）
ALTER TABLE course ADD COLUMN `URL` VARCHAR(500) AFTER `Title`;

-- 更新现有课程的URL
UPDATE course SET URL = CONCAT('/course/', ArticleID) WHERE URL IS NULL OR URL = '';

-- 查看表结构
DESCRIBE course;

-- 查看课程数据
SELECT ArticleID, Title, URL, Author, Category FROM course;
