-- 修复course表，添加审核相关字段
-- 在MySQL中执行此脚本

USE pythonlearn;

-- 1. 检查当前表结构
DESCRIBE course;

-- 2. 添加缺失的审核相关字段
ALTER TABLE course 
ADD COLUMN reviewComment TEXT COMMENT '审核意见',
ADD COLUMN reviewedAt DATETIME COMMENT '审核时间',
ADD COLUMN reviewedBy INT COMMENT '审核人ID';

-- 3. 检查是否有Price字段（从实体类看可能需要）
SELECT COUNT(*) as has_price FROM information_schema.columns 
WHERE table_schema = 'pythonlearn' AND table_name = 'course' AND column_name = 'Price';

-- 如果没有Price字段，添加它
SET @has_price = (SELECT COUNT(*) FROM information_schema.columns 
                  WHERE table_schema = 'pythonlearn' AND table_name = 'course' AND column_name = 'Price');

SET @sql = IF(@has_price = 0, 
    'ALTER TABLE course ADD COLUMN Price DECIMAL(10,2) DEFAULT 0.00 COMMENT "课程价格"',
    'SELECT "Price字段已存在" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 更新现有课程的状态为pending（待审核）
UPDATE course SET Status = 'pending' WHERE Status IS NULL OR Status = '';

-- 5. 验证表结构
DESCRIBE course;

-- 6. 查看修复后的数据
SELECT 
    ArticleID,
    Title,
    Author,
    Category,
    Status,
    reviewComment,
    reviewedAt,
    reviewedBy,
    CreatedAt,
    UpdatedAt
FROM course 
LIMIT 5;

-- 7. 检查状态分布
SELECT 
    Status,
    COUNT(*) as count
FROM course 
GROUP BY Status;
