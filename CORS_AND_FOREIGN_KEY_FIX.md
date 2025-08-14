# CORS配置和外键约束错误修复

## 问题描述

### 1. CORS配置错误
**错误信息：**
```
When allowCredentials is true, allowedOrigins cannot contain the special value "*"
```

**原因：** 当 `allowCredentials` 设置为 `true` 时，不能使用通配符 `*` 作为允许的源。

**影响：** 前端无法正常访问后端API，导致500错误。

### 2. 外键约束错误
**错误信息：**
```
Cannot add or update a child row: a foreign key constraint fails (`python`.`user_problem_record`, CONSTRAINT `fk_user_problem_record_problem` FOREIGN KEY (`problem_id`) REFERENCES `pythonproblems` (`Id`) ON DELETE CASCADE)
```

**原因：** 在 `UserProblemRecordServiceImpl` 中，当用户没有提交记录时，代码尝试插入一条默认记录，但 `problemId` 被设置为空字符串，违反了外键约束。

**影响：** 获取用户统计信息时抛出异常，导致500错误。

### 3. SQL语法错误
**错误信息：**
```
You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'groups AS (
```

**原因：** `groups` 是MySQL的保留字，不能直接用作表别名。

**影响：** 获取用户连续刷题天数时抛出SQL语法异常。

### 4. 知识点API参数错误
**错误信息：**
```
Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: "null"
```

**原因：** 前端路由查询参数使用错误，传递了 `"null"` 字符串而不是知识点标题。

**影响：** 知识点详情页面无法正常加载，导致400错误。

## 修复方案

### 1. CORS配置修复
将所有控制器的 `@CrossOrigin` 注解统一为：
```java
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
```

**修复的文件：**
- `AdminController.java`
- `PythonProblemController.java` 
- `PythonVideoController.java`
- `KnowledgeController.java`

### 2. 外键约束修复
移除 `UserProblemRecordServiceImpl` 中不必要的默认记录插入逻辑，直接返回查询结果：

**修复的方法：**
- `getTotalSubmissions()` - 直接返回提交数量
- `getPassedProblems()` - 直接返回通过题目数量  
- `getAccuracy()` - 直接返回准确率

### 3. SQL语法修复
将MySQL保留字 `groups` 改为 `date_groups`：

**修复的SQL：**
```sql
date_groups AS (
    SELECT d, DATE_SUB(d, INTERVAL rn DAY) AS grp
    FROM ranked
)
SELECT COUNT(*) as continuous_days
FROM date_groups
GROUP BY grp
```

### 4. 前端路由参数修复
修正 `LearnDetial.vue` 中的路由查询参数获取：

**修复内容：**
- 将 `this.$route.query.id` 改为 `this.$route.query.title`
- 将路由监听器从 `$route.query.id` 改为 `$route.query.title`

## 修复后的效果

1. **CORS问题解决：** 前端可以正常访问后端API
2. **外键约束问题解决：** 用户统计信息可以正常获取
3. **系统稳定性提升：** 避免了不必要的数据库插入操作

## 注意事项

1. 确保前端运行在 `http://localhost:8081` 端口
2. 如果需要支持其他端口，请在 `@CrossOrigin` 中添加相应的地址
3. 用户统计信息在没有记录时会返回0，这是正常的行为

## 相关文件

- `src/main/java/com/demo/python_demo/controller/AdminController.java`
- `src/main/java/com/demo/python_demo/controller/PythonProblemController.java`
- `src/main/java/com/demo/python_demo/controller/PythonVideoController.java`
- `src/main/java/com/demo/python_demo/controller/KnowledgeController.java`
- `src/main/java/com/demo/python_demo/service/impl/UserProblemRecordServiceImpl.java`
- `src/main/java/com/demo/python_demo/repository/UserProblemRecordRepository.java`
- `pythondemo/src/views/LearnDetial.vue`
