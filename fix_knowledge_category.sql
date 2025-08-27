-- 为知识点学习记录表添加分类字段
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 检查字段是否存在，如果不存在则添加
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'pythonlearn' 
     AND TABLE_NAME = 'knowledge_study_record' 
     AND COLUMN_NAME = 'knowledge_category') = 0,
    'ALTER TABLE knowledge_study_record ADD COLUMN knowledge_category varchar(100) DEFAULT "文字知识点" COMMENT "知识点分类" AFTER knowledge_title',
    'SELECT "knowledge_category字段已存在" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新现有记录的分类字段
UPDATE knowledge_study_record 
SET knowledge_category = '文字知识点' 
WHERE knowledge_category IS NULL OR knowledge_category = '';

-- 验证表结构
DESCRIBE knowledge_study_record;

-- 显示更新后的数据
SELECT id, knowledge_title, knowledge_category, study_time, progress, status, start_time 
FROM knowledge_study_record 
ORDER BY created_at DESC 
LIMIT 10;








