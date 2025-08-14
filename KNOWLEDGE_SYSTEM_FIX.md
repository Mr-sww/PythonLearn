# 知识点系统修复指南

## 问题描述
前端调用 `/api/knowledge/{id}/detail` 接口时出现 400 错误，原因是数据库中缺少 `knowledge_point` 表。

## 错误分析
1. 前端 `LearnDetial.vue` 组件调用 `/api/knowledge/catalog` 获取知识点目录
2. 然后调用 `/api/knowledge/{id}/detail` 获取知识点详情和题目列表
3. 后端 `KnowledgeController.java` 中的 `getKnowledgeDetailWithProblems` 方法需要查询 `knowledge_point` 表
4. 但数据库中不存在该表，导致查询失败

## 解决方案

### 1. 更新数据库结构
运行更新后的 `database_tables.sql` 脚本，该脚本已添加：
```sql
-- 知识点表
CREATE TABLE IF NOT EXISTS `knowledge_point` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '知识点ID',
    `title` VARCHAR(255) NOT NULL COMMENT '知识点标题',
    `content` TEXT COMMENT '知识点内容',
    `question` TEXT COMMENT '关联的题目ID列表(逗号分隔)',
    `url` VARCHAR(1000) COMMENT '相关链接',
    `stage` VARCHAR(50) DEFAULT '1.1' COMMENT '学习阶段',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点表';
```

### 2. 填充示例数据
运行 `populate_knowledge_points.py` 脚本填充示例数据：
```bash
python populate_knowledge_points.py
```

该脚本会插入 8 个示例知识点，包括：
- Python基础语法
- 数据类型与变量
- 控制流语句
- 函数定义与调用
- 面向对象编程
- 文件操作
- 异常处理
- 模块与包

### 3. 验证修复
1. 确保 MySQL 服务正在运行
2. 确保数据库连接配置正确（用户名、密码、数据库名）
3. 重启 Spring Boot 应用
4. 访问前端页面，检查知识点目录是否正常加载
5. 点击知识点，检查详情页面是否正常显示

## 数据库连接配置
默认配置：
- 主机：localhost
- 端口：3306
- 用户名：root
- 密码：123456
- 数据库：python

如需修改，请编辑 `populate_knowledge_points.py` 中的 `MYSQL_CONFIG` 变量。

## 相关文件
- `database_tables.sql` - 数据库结构脚本（已更新）
- `populate_knowledge_points.py` - 数据填充脚本（新建）
- `src/main/java/com/demo/python_demo/controller/KnowledgeController.java` - 后端控制器
- `pythondemo/src/views/LearnDetial.vue` - 前端组件

## 注意事项
1. 确保 MySQL 数据库已创建并运行
2. 确保有足够的权限创建表和插入数据
3. 如果表已存在，脚本会先清空现有数据再插入新数据
4. 知识点表中的 `question` 字段存储题目ID，用逗号分隔
5. 前端会根据这些ID查询对应的题目列表
