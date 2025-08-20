-- 测试头像显示
USE python_demo;

-- 查看当前用户数据
SELECT 
    user_id, 
    account, 
    nickname, 
    avatar,
    CASE 
        WHEN avatar IS NULL THEN 'NULL'
        WHEN avatar = '' THEN '空字符串'
        ELSE avatar
    END as avatar_status
FROM user 
LIMIT 5;

-- 为测试用户设置头像
UPDATE user 
SET avatar = '/avatar/test_avatar.jpg' 
WHERE user_id = 1;

-- 查看更新后的结果
SELECT 
    user_id, 
    account, 
    nickname, 
    avatar
FROM user 
WHERE user_id = 1;
