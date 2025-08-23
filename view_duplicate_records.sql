-- 查看知识点学习记录中的重复记录（只查看，不删除）
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 查看重复记录统计
SELECT 
    user_id, 
    knowledge_id, 
    knowledge_title,
    COUNT(*) as record_count
FROM knowledge_study_record 
GROUP BY user_id, knowledge_id 
HAVING COUNT(*) > 1
ORDER BY user_id, knowledge_id;

-- 查看具体的重复记录详情
SELECT 
    id,
    user_id,
    knowledge_id,
    knowledge_title,
    status,
    start_time,
    study_time,
    progress,
    created_at
FROM knowledge_study_record 
WHERE (user_id, knowledge_id) IN (
    SELECT user_id, knowledge_id 
    FROM knowledge_study_record 
    GROUP BY user_id, knowledge_id 
    HAVING COUNT(*) > 1
)
ORDER BY user_id, knowledge_id, id DESC;

-- 查看每个知识点的最新记录（用于验证显示逻辑）
SELECT 
    knowledge_id,
    knowledge_title,
    MAX(id) as latest_id,
    COUNT(*) as total_records
FROM knowledge_study_record 
GROUP BY knowledge_id, knowledge_title
ORDER BY latest_id DESC;
