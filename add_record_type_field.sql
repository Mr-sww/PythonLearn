-- 为学习记录表添加内容类型字段
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 为 knowledge_study_record 表添加内容类型字段
ALTER TABLE knowledge_study_record 
ADD COLUMN content_type ENUM('text', 'video') DEFAULT 'text' 
COMMENT '内容类型：text-文字知识点，video-视频课程' AFTER knowledge_title;

-- 更新现有记录，根据标题判断是否为视频
UPDATE knowledge_study_record 
SET content_type = 'video' 
WHERE knowledge_title LIKE '%视频%' OR 
      knowledge_title LIKE '%教程%' OR 
      knowledge_title LIKE '%实战%' OR
      knowledge_title LIKE '%完整版%' OR
      knowledge_title LIKE '%Web%' OR
      knowledge_title LIKE '%开发%';

-- 验证表结构
DESCRIBE knowledge_study_record;

-- 显示更新后的数据示例
SELECT id, knowledge_title, content_type, status, start_time 
FROM knowledge_study_record 
ORDER BY created_at DESC 
LIMIT 10;
