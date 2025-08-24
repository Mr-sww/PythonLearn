-- 为知识点表添加内容类型字段
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 为 knowledge_point 表添加内容类型字段
ALTER TABLE knowledge_point 
ADD COLUMN content_type ENUM('text', 'video') DEFAULT 'text' COMMENT '内容类型：text-文字知识点，video-视频知识点' AFTER url;

-- 为 runoobpython3install 表添加内容类型字段（如果存在）
ALTER TABLE runoobpython3install 
ADD COLUMN content_type ENUM('text', 'video') DEFAULT 'text' COMMENT '内容类型：text-文字知识点，video-视频知识点' AFTER url;

-- 更新现有记录，根据URL字段判断是否为视频
UPDATE knowledge_point 
SET content_type = 'video' 
WHERE url IS NOT NULL AND url != '' AND (
    url LIKE '%youtube%' OR 
    url LIKE '%bilibili%' OR 
    url LIKE '%youku%' OR 
    url LIKE '%iqiyi%' OR 
    url LIKE '%.mp4%' OR 
    url LIKE '%.avi%' OR 
    url LIKE '%.mov%' OR 
    url LIKE '%.wmv%' OR
    url LIKE '%video%'
);

UPDATE runoobpython3install 
SET content_type = 'video' 
WHERE url IS NOT NULL AND url != '' AND (
    url LIKE '%youtube%' OR 
    url LIKE '%bilibili%' OR 
    url LIKE '%youku%' OR 
    url LIKE '%iqiyi%' OR 
    url LIKE '%.mp4%' OR 
    url LIKE '%.avi%' OR 
    url LIKE '%.mov%' OR 
    url LIKE '%.wmv%' OR
    url LIKE '%video%'
);

-- 验证表结构
DESCRIBE knowledge_point;
DESCRIBE runoobpython3install;

-- 显示更新后的数据示例
SELECT id, title, content_type, url FROM knowledge_point ORDER BY id LIMIT 10;
SELECT id, title, content_type, url FROM runoobpython3install ORDER BY id LIMIT 10;
