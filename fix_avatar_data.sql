-- 检查数据库中的头像数据
USE python_demo;

-- 查看用户头像数据
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
LIMIT 10;

-- 统计头像数据
SELECT 
    COUNT(*) as total_users,
    COUNT(CASE WHEN avatar IS NOT NULL AND avatar != '' THEN 1 END) as users_with_avatar,
    COUNT(CASE WHEN avatar IS NULL OR avatar = '' THEN 1 END) as users_without_avatar
FROM user;

-- 为没有头像的用户设置默认头像
UPDATE user 
SET avatar = '/avatar/default_avatar.jpg' 
WHERE avatar IS NULL OR avatar = '';

-- 查看更新后的结果
SELECT 
    user_id, 
    account, 
    nickname, 
    avatar
FROM user 
LIMIT 10;
