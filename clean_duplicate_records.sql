-- 清理知识点学习记录中的重复记录
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 查看重复记录
SELECT 
    user_id, 
    knowledge_id, 
    knowledge_title,
    COUNT(*) as record_count
FROM knowledge_study_record 
GROUP BY user_id, knowledge_id 
HAVING COUNT(*) > 1
ORDER BY user_id, knowledge_id;

-- 删除重复记录，只保留最新的一条（ID最大的）
DELETE FROM knowledge_study_record 
WHERE id NOT IN (
    SELECT id FROM (
        SELECT MAX(id) as id 
        FROM knowledge_study_record 
        GROUP BY user_id, knowledge_id
    ) as temp
);

-- 验证清理结果
SELECT 
    user_id, 
    knowledge_id, 
    knowledge_title,
    COUNT(*) as record_count
FROM knowledge_study_record 
GROUP BY user_id, knowledge_id 
HAVING COUNT(*) > 1
ORDER BY user_id, knowledge_id;

-- 显示清理后的记录
SELECT 
    id,
    user_id,
    knowledge_id,
    knowledge_title,
    status,
    start_time,
    study_time,
    progress
FROM knowledge_study_record 
ORDER BY user_id, knowledge_id, created_at DESC;









