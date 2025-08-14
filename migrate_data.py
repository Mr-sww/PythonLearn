#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
SQL Server 到 MySQL 数据迁移脚本
使用方法: python migrate_data.py
"""

import pyodbc
import pymysql
import pandas as pd
import logging
from datetime import datetime
import sys
import os

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('migration.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)

class DataMigrator:
    def __init__(self):
        # SQL Server 连接配置 - 请根据实际情况修改
        self.sqlserver_config = {
            'server': 'localhost',  # 修改为您的 SQL Server 地址
            'database': 'your_database',  # 修改为您的数据库名
            'username': 'your_username',  # 修改为您的用户名
            'password': 'your_password'   # 修改为您的密码
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
        try:
            # 尝试不同的 ODBC 驱动
            drivers = [
                '{ODBC Driver 17 for SQL Server}',
                '{ODBC Driver 13 for SQL Server}',
                '{SQL Server}',
                '{SQL Server Native Client 11.0}'
            ]
            
            for driver in drivers:
                try:
                    conn_str = (
                        f"DRIVER={driver};"
                        f"SERVER={self.sqlserver_config['server']};"
                        f"DATABASE={self.sqlserver_config['database']};"
                        f"UID={self.sqlserver_config['username']};"
                        f"PWD={self.sqlserver_config['password']};"
                        f"TrustServerCertificate=yes;"
                    )
                    conn = pyodbc.connect(conn_str)
                    logging.info(f"成功连接到 SQL Server，使用驱动: {driver}")
                    return conn
                except Exception as e:
                    logging.warning(f"驱动 {driver} 连接失败: {e}")
                    continue
            
            raise Exception("无法连接到 SQL Server，请检查配置和驱动")
            
        except Exception as e:
            logging.error(f"连接 SQL Server 失败: {e}")
            return None
    
    def connect_mysql(self):
        """连接到 MySQL"""
        try:
            conn = pymysql.connect(
                host=self.mysql_config['host'],
                port=self.mysql_config['port'],
                user=self.mysql_config['username'],
                password=self.mysql_config['password'],
                database=self.mysql_config['database'],
                charset='utf8mb4'
            )
            logging.info("成功连接到 MySQL")
            return conn
        except Exception as e:
            logging.error(f"连接 MySQL 失败: {e}")
            return None
    
    def get_sqlserver_tables(self):
        """获取 SQL Server 中的所有表"""
        try:
            conn = self.connect_sqlserver()
            if not conn:
                return []
            
            cursor = conn.cursor()
            cursor.execute("""
                SELECT TABLE_NAME 
                FROM INFORMATION_SCHEMA.TABLES 
                WHERE TABLE_TYPE = 'BASE TABLE'
                ORDER BY TABLE_NAME
            """)
            tables = [row[0] for row in cursor.fetchall()]
            cursor.close()
            conn.close()
            
            logging.info(f"找到 {len(tables)} 个表: {tables}")
            return tables
        except Exception as e:
            logging.error(f"获取表列表失败: {e}")
            return []
    
    def get_table_data(self, table_name):
        """获取表的数据"""
        try:
            conn = self.connect_sqlserver()
            if not conn:
                return pd.DataFrame()
            
            query = f"SELECT * FROM {table_name}"
            df = pd.read_sql(query, conn)
            conn.close()
            
            logging.info(f"表 {table_name} 包含 {len(df)} 行数据")
            return df
        except Exception as e:
            logging.error(f"获取表 {table_name} 数据失败: {e}")
            return pd.DataFrame()
    
    def convert_data_types(self, df):
        """转换数据类型以适配 MySQL"""
        for col in df.columns:
            # 处理 datetime 类型
            if df[col].dtype == 'object':
                try:
                    pd.to_datetime(df[col])
                    df[col] = pd.to_datetime(df[col]).dt.strftime('%Y-%m-%d %H:%M:%S')
                except:
                    pass
            
            # 处理 NaN 值
            df[col] = df[col].fillna('')
            
            # 处理字符串长度限制
            if df[col].dtype == 'object':
                df[col] = df[col].astype(str).str[:65535]  # 限制为 TEXT 类型长度
        
        return df
    
    def check_mysql_table_exists(self, table_name):
        """检查 MySQL 中表是否存在"""
        try:
            conn = self.connect_mysql()
            if not conn:
                return False
            
            cursor = conn.cursor()
            cursor.execute(f"SHOW TABLES LIKE '{table_name}'")
            exists = cursor.fetchone() is not None
            cursor.close()
            conn.close()
            return exists
        except Exception as e:
            logging.error(f"检查表 {table_name} 是否存在失败: {e}")
            return False
    
    def insert_to_mysql(self, table_name, df):
        """将数据插入到 MySQL"""
        try:
            if df.empty:
                logging.warning(f"表 {table_name} 没有数据，跳过")
                return True
            
            # 检查表是否存在
            if not self.check_mysql_table_exists(table_name):
                logging.error(f"MySQL 中不存在表 {table_name}，请先创建表结构")
                return False
            
            # 转换数据类型
            df = self.convert_data_types(df)
            
            # 连接 MySQL
            conn = self.connect_mysql()
            if not conn:
                return False
            
            cursor = conn.cursor()
            
            # 清空目标表
            cursor.execute(f"TRUNCATE TABLE `{table_name}`")
            
            # 构建 INSERT 语句
            columns = ', '.join([f'`{col}`' for col in df.columns])
            placeholders = ', '.join(['%s'] * len(df.columns))
            insert_query = f"INSERT INTO `{table_name}` ({columns}) VALUES ({placeholders})"
            
            # 分批插入数据（避免内存问题）
            batch_size = 1000
            total_rows = len(df)
            
            for i in range(0, total_rows, batch_size):
                batch_df = df.iloc[i:i+batch_size]
                values = [tuple(row) for row in batch_df.values]
                cursor.executemany(insert_query, values)
                logging.info(f"已插入 {min(i+batch_size, total_rows)}/{total_rows} 行到表 {table_name}")
            
            conn.commit()
            cursor.close()
            conn.close()
            
            logging.info(f"成功迁移表 {table_name} 的 {len(df)} 行数据")
            return True
            
        except Exception as e:
            logging.error(f"迁移表 {table_name} 失败: {e}")
            if 'conn' in locals():
                conn.rollback()
                conn.close()
            return False
    
    def migrate_table(self, table_name):
        """迁移单个表"""
        logging.info(f"开始迁移表: {table_name}")
        
        # 获取数据
        df = self.get_table_data(table_name)
        if df.empty:
            logging.warning(f"表 {table_name} 没有数据")
            return False
        
        # 插入到 MySQL
        success = self.insert_to_mysql(table_name, df)
        return success
    
    def migrate_all_tables(self, exclude_tables=None):
        """迁移所有表"""
        if exclude_tables is None:
            exclude_tables = []
        
        # 获取所有表
        tables = self.get_sqlserver_tables()
        
        # 过滤掉排除的表
        tables = [t for t in tables if t not in exclude_tables]
        
        if not tables:
            logging.warning("没有找到要迁移的表")
            return 0, []
        
        success_count = 0
        failed_tables = []
        
        for i, table_name in enumerate(tables, 1):
            logging.info(f"正在迁移第 {i}/{len(tables)} 个表: {table_name}")
            try:
                if self.migrate_table(table_name):
                    success_count += 1
                else:
                    failed_tables.append(table_name)
            except Exception as e:
                logging.error(f"迁移表 {table_name} 时发生错误: {e}")
                failed_tables.append(table_name)
        
        logging.info(f"迁移完成: 成功 {success_count}/{len(tables)} 个表")
        if failed_tables:
            logging.warning(f"失败的表: {failed_tables}")
        
        return success_count, failed_tables

def main():
    """主函数"""
    print("SQL Server 到 MySQL 数据迁移工具")
    print("=" * 50)
    
    # 创建迁移器
    migrator = DataMigrator()
    
    # 检查配置
    print("\n请确认以下配置:")
    print(f"SQL Server: {migrator.sqlserver_config['server']}")
    print(f"SQL Server 数据库: {migrator.sqlserver_config['database']}")
    print(f"MySQL: {migrator.mysql_config['host']}:{migrator.mysql_config['port']}")
    print(f"MySQL 数据库: {migrator.mysql_config['database']}")
    
    # 询问是否继续
    response = input("\n配置是否正确？(y/n): ").lower().strip()
    if response != 'y':
        print("请修改 migrate_data.py 中的配置后重新运行")
        return
    
    try:
        # 测试连接
        print("\n测试数据库连接...")
        
        sqlserver_conn = migrator.connect_sqlserver()
        if not sqlserver_conn:
            print("无法连接到 SQL Server，请检查配置")
            return
        
        mysql_conn = migrator.connect_mysql()
        if not mysql_conn:
            print("无法连接到 MySQL，请检查配置")
            return
        
        print("数据库连接测试成功！")
        
        # 获取表列表
        tables = migrator.get_sqlserver_tables()
        if not tables:
            print("没有找到要迁移的表")
            return
        
        print(f"\n找到 {len(tables)} 个表:")
        for i, table in enumerate(tables, 1):
            print(f"  {i}. {table}")
        
        # 询问是否排除某些表
        exclude_input = input("\n请输入要排除的表名（用逗号分隔，直接回车表示不排除）: ").strip()
        exclude_tables = [t.strip() for t in exclude_input.split(',')] if exclude_input else []
        
        if exclude_tables:
            print(f"将排除以下表: {exclude_tables}")
        
        # 确认开始迁移
        confirm = input("\n确认开始迁移？(y/n): ").lower().strip()
        if confirm != 'y':
            print("迁移已取消")
            return
        
        # 开始迁移
        print("\n开始迁移...")
        success_count, failed_tables = migrator.migrate_all_tables(exclude_tables)
        
        # 输出结果
        print(f"\n迁移结果:")
        print(f"成功迁移: {success_count} 个表")
        if failed_tables:
            print(f"失败的表: {failed_tables}")
        
        print(f"\n详细日志请查看 migration.log 文件")
        
    except Exception as e:
        logging.error(f"迁移过程中发生错误: {e}")
        print(f"迁移失败: {e}")
    finally:
        # 关闭连接
        if 'sqlserver_conn' in locals():
            sqlserver_conn.close()
        if 'mysql_conn' in locals():
            mysql_conn.close()

if __name__ == "__main__":
    main()
