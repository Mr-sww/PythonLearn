# SQL Server 到 MySQL 数据迁移指南

## 方法一：使用 SQL Server Management Studio (SSMS) 导出数据

### 步骤 1: 从 SQL Server 导出数据

1. **打开 SQL Server Management Studio**
2. **连接到您的 SQL Server 数据库**
3. **右键点击数据库 → Tasks → Generate Scripts**
4. **选择要迁移的表**
5. **点击 Advanced → Types of data to script → Schema and data**
6. **生成脚本文件**

### 步骤 2: 修改脚本以适配 MySQL

需要修改的 SQL Server 特定语法：

```sql
-- SQL Server 语法
SELECT TOP 10 * FROM table_name
INSERT INTO table_name (id, name) VALUES (1, 'test')

-- MySQL 语法
SELECT * FROM table_name LIMIT 10
INSERT INTO table_name (id, name) VALUES (1, 'test')
```

### 步骤 3: 在 MySQL 中执行脚本

```bash
mysql -u root -p python_demo < migration_script.sql
```

## 方法二：使用 Python 脚本自动化迁移

### 安装必要的 Python 包

```bash
pip install pyodbc pymysql pandas sqlalchemy
```

### 创建迁移脚本

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import pyodbc
import pymysql
import pandas as pd
import logging
from datetime import datetime

# 配置日志
logging.basicConfig(level=logging.INFO)

class DataMigrator:
    def __init__(self):
        # SQL Server 连接配置
        self.sqlserver_config = {
            'server': 'your_sqlserver_server',
            'database': 'your_database_name',
            'username': 'your_username',
            'password': 'your_password'
        }
        
        # MySQL 连接配置
        self.mysql_config = {
            'host': 'localhost',
            'port': 3306,
            'username': 'root',
            'password': '123456',
            'database': 'python_demo'
        }
    
    def connect_sqlserver(self):
        """连接到 SQL Server"""
        conn_str = (
            f"DRIVER={{ODBC Driver 17 for SQL Server}};"
            f"SERVER={self.sqlserver_config['server']};"
            f"DATABASE={self.sqlserver_config['database']};"
            f"UID={self.sqlserver_config['username']};"
            f"PWD={self.sqlserver_config['password']};"
            f"TrustServerCertificate=yes;"
        )
        return pyodbc.connect(conn_str)
    
    def connect_mysql(self):
        """连接到 MySQL"""
        return pymysql.connect(
            host=self.mysql_config['host'],
            port=self.mysql_config['port'],
            user=self.mysql_config['username'],
            password=self.mysql_config['password'],
            database=self.mysql_config['database'],
            charset='utf8mb4'
        )
    
    def migrate_table(self, table_name):
        """迁移单个表"""
        try:
            # 连接数据库
            sqlserver_conn = self.connect_sqlserver()
            mysql_conn = self.connect_mysql()
            
            # 从 SQL Server 读取数据
            query = f"SELECT * FROM {table_name}"
            df = pd.read_sql(query, sqlserver_conn)
            
            if df.empty:
                logging.warning(f"表 {table_name} 没有数据")
                return True
            
            # 处理数据类型
            for col in df.columns:
                if df[col].dtype == 'object':
                    try:
                        pd.to_datetime(df[col])
                        df[col] = pd.to_datetime(df[col]).dt.strftime('%Y-%m-%d %H:%M:%S')
                    except:
                        pass
                df[col] = df[col].fillna('')
            
            # 插入到 MySQL
            cursor = mysql_conn.cursor()
            
            # 清空目标表
            cursor.execute(f"TRUNCATE TABLE {table_name}")
            
            # 构建 INSERT 语句
            columns = ', '.join([f'`{col}`' for col in df.columns])
            placeholders = ', '.join(['%s'] * len(df.columns))
            insert_query = f"INSERT INTO {table_name} ({columns}) VALUES ({placeholders})"
            
            # 执行批量插入
            values = [tuple(row) for row in df.values]
            cursor.executemany(insert_query, values)
            
            mysql_conn.commit()
            cursor.close()
            
            logging.info(f"成功迁移表 {table_name} 的 {len(df)} 行数据")
            return True
            
        except Exception as e:
            logging.error(f"迁移表 {table_name} 失败: {e}")
            return False
        finally:
            if 'sqlserver_conn' in locals():
                sqlserver_conn.close()
            if 'mysql_conn' in locals():
                mysql_conn.close()
    
    def get_tables(self):
        """获取所有表名"""
        sqlserver_conn = self.connect_sqlserver()
        cursor = sqlserver_conn.cursor()
        cursor.execute("""
            SELECT TABLE_NAME 
            FROM INFORMATION_SCHEMA.TABLES 
            WHERE TABLE_TYPE = 'BASE TABLE'
            ORDER BY TABLE_NAME
        """)
        tables = [row[0] for row in cursor.fetchall()]
        cursor.close()
        sqlserver_conn.close()
        return tables
    
    def migrate_all(self, exclude_tables=None):
        """迁移所有表"""
        if exclude_tables is None:
            exclude_tables = []
        
        tables = self.get_tables()
        tables = [t for t in tables if t not in exclude_tables]
        
        success_count = 0
        failed_tables = []
        
        for table_name in tables:
            if self.migrate_table(table_name):
                success_count += 1
            else:
                failed_tables.append(table_name)
        
        logging.info(f"迁移完成: 成功 {success_count}/{len(tables)} 个表")
        if failed_tables:
            logging.warning(f"失败的表: {failed_tables}")
        
        return success_count, failed_tables

# 使用示例
if __name__ == "__main__":
    migrator = DataMigrator()
    
    # 排除不需要迁移的表
    exclude_tables = [
        # 'temp_table',
        # 'log_table'
    ]
    
    # 开始迁移
    success_count, failed_tables = migrator.migrate_all(exclude_tables)
    
    print(f"迁移结果:")
    print(f"成功迁移: {success_count} 个表")
    if failed_tables:
        print(f"失败的表: {failed_tables}")
```

## 方法三：使用数据库工具

### 使用 Navicat Premium

1. **连接到 SQL Server 数据库**
2. **选择要迁移的表**
3. **右键 → 数据传输**
4. **选择 MySQL 作为目标**
5. **配置字段映射**
6. **开始传输**

### 使用 DBeaver

1. **创建 SQL Server 和 MySQL 连接**
2. **选择源表**
3. **右键 → 工具 → 数据传输**
4. **选择目标数据库**
5. **配置传输选项**

## 方法四：使用命令行工具

### 使用 mysqldump 和 sqlcmd

```bash
# 从 SQL Server 导出数据
sqlcmd -S your_server -d your_database -U your_username -P your_password -Q "SELECT * FROM table_name" -o output.csv

# 处理数据格式
# 然后导入到 MySQL
mysql -u root -p python_demo -e "LOAD DATA INFILE 'output.csv' INTO TABLE table_name"
```

## 常见问题和解决方案

### 1. 字符编码问题

```sql
-- 在 MySQL 中设置正确的字符编码
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
```

### 2. 数据类型不匹配

```sql
-- SQL Server 的 datetime 转换为 MySQL 的 datetime
-- SQL Server 的 nvarchar 转换为 MySQL 的 varchar
-- SQL Server 的 bit 转换为 MySQL 的 tinyint(1)
```

### 3. 自增主键问题

```sql
-- 在迁移前重置自增主键
ALTER TABLE table_name AUTO_INCREMENT = 1;
```

### 4. 外键约束问题

```sql
-- 临时禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 执行迁移

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;
```

## 迁移前检查清单

1. **备份原始数据**
2. **确认表结构匹配**
3. **检查数据类型兼容性**
4. **准备字符编码设置**
5. **测试小量数据迁移**
6. **准备回滚方案**

## 迁移后验证

1. **检查数据完整性**
2. **验证记录数量**
3. **测试应用程序功能**
4. **检查性能表现**
5. **更新应用程序配置**

## 注意事项

1. **大数据量迁移时考虑分批处理**
2. **注意时区设置**
3. **处理 NULL 值**
4. **注意 SQL Server 特有的数据类型**
5. **考虑网络带宽和传输时间**
