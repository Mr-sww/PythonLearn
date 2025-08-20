#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
检查数据库中的头像数据
"""

import pymysql
import os

def check_avatar_data():
    try:
        # 数据库连接配置
        connection = pymysql.connect(
            host='localhost',
            user='root',
            password='123456',  # 请根据实际情况修改密码
            database='python_demo',
            charset='utf8mb4'
        )
        
        with connection.cursor() as cursor:
            # 查询用户头像数据
            sql = """
            SELECT user_id, account, nickname, avatar, 
                   CASE 
                       WHEN avatar IS NULL THEN 'NULL'
                       WHEN avatar = '' THEN '空字符串'
                       ELSE avatar
                   END as avatar_status
            FROM user 
            LIMIT 10
            """
            cursor.execute(sql)
            results = cursor.cursor.fetchall()
            
            print("数据库中的头像数据:")
            print("-" * 80)
            print(f"{'用户ID':<8} {'账号':<15} {'昵称':<15} {'头像状态':<15} {'头像路径'}")
            print("-" * 80)
            
            for row in results:
                user_id, account, nickname, avatar, avatar_status = row
                nickname = nickname or '未设置'
                avatar_path = avatar or '无'
                print(f"{user_id:<8} {account:<15} {nickname:<15} {avatar_status:<15} {avatar_path}")
            
            # 统计头像数据
            cursor.execute("SELECT COUNT(*) as total_users FROM user")
            total_users = cursor.fetchone()[0]
            
            cursor.execute("SELECT COUNT(*) as users_with_avatar FROM user WHERE avatar IS NOT NULL AND avatar != ''")
            users_with_avatar = cursor.fetchone()[0]
            
            cursor.execute("SELECT COUNT(*) as users_without_avatar FROM user WHERE avatar IS NULL OR avatar = ''")
            users_without_avatar = cursor.fetchone()[0]
            
            print("\n头像数据统计:")
            print(f"总用户数: {total_users}")
            print(f"有头像用户: {users_with_avatar}")
            print(f"无头像用户: {users_without_avatar}")
            
    except Exception as e:
        print(f"数据库连接失败: {e}")
    finally:
        if 'connection' in locals():
            connection.close()

if __name__ == "__main__":
    check_avatar_data()
