# 数据映射修复

## 问题分析

前端显示的是默认值而不是数据库中的真实数据：
- ❌ 标题显示 "未知知识点" (应该是 "Python3 数字(Numbe...")
- ❌ 开始时间显示 "1970/01/01 08:00" (应该是 "2025-08-24 03...")
- ❌ 学习时长显示 "0分钟" (数据库显示 study_time 是 NULL)

## 根本原因

**MyBatis 字段映射问题**: 数据库字段使用下划线命名（如 `knowledge_title`），但前端使用驼峰命名（如 `knowledgeTitle`），MyBatis 没有配置自动转换。

## 解决方案

### 1. 添加 MyBatis 驼峰命名转换配置

**文件**: `src/main/resources/application.properties`

**添加配置**:
```properties
mybatis.configuration.map-underscore-to-camel-case=true
```

### 2. 字段映射关系

| 数据库字段 | Java实体字段 | 前端字段 | 说明 |
|-----------|-------------|---------|------|
| `knowledge_title` | `knowledgeTitle` | `knowledgeTitle` | 知识点标题 |
| `study_time` | `studyTime` | `studyTime` | 学习时长(秒) |
| `start_time` | `startTime` | `startTime` | 开始时间 |
| `status` | `status` | `status` | 学习状态 |
| `progress` | `progress` | `progress` | 学习进度 |

### 3. 测试验证

运行测试脚本验证数据映射：
```bash
python test_data_mapping.py
```

## 预期效果

修复后，前端应该正确显示：

```
📘 Python3 数字(Number)
   [文字知识点] ID: 16
   🕐 2025-08-24 03:39  ⏳ 0分钟  [已完成]
```

而不是：
```
📘 未知知识点
   [文字知识点] ID: 
   🕐 1970/01/01 08:00  ⏳ 0分钟  [已完成]
```

## 重启要求

修改 MyBatis 配置后需要重启后端服务才能生效：

```bash
mvn spring-boot:run
```

## 验证步骤

1. 重启后端服务
2. 运行测试脚本 `python test_data_mapping.py`
3. 检查前端页面显示是否正确
4. 确认显示的是数据库中的真实数据









