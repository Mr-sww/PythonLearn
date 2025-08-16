-- 修复course表缺失的URL列
-- 在MySQL中执行此脚本解决 "Unknown column 'c.URL' in 'field list'" 错误

USE pythonlearn;

-- 检查URL列是否存在，如果不存在则添加
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'pythonlearn' 
     AND TABLE_NAME = 'course' 
     AND COLUMN_NAME = 'URL') = 0,
    'ALTER TABLE course ADD COLUMN `URL` VARCHAR(500) AFTER `Title`',
    'SELECT "URL column already exists" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新现有课程的URL（如果为空）
UPDATE course SET URL = CONCAT('/course/', ArticleID) WHERE URL IS NULL OR URL = '';

-- 验证表结构
DESCRIBE course;

-- 查看更新后的课程数据
SELECT ArticleID, Title, URL, Author, Category FROM course;
