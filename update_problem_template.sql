-- 更新A+B问题的模板代码
UPDATE pythonproblems 
SET template = 'a, b = map(int, input().split())
print(a + b)'
WHERE title LIKE '%A+B%' OR title LIKE '%a+b%';

-- 查看更新结果
SELECT id, title, template FROM pythonproblems WHERE title LIKE '%A+B%' OR title LIKE '%a+b%';
