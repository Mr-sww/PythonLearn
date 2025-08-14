#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
知识点表数据填充脚本
使用方法: python populate_knowledge_points.py
"""

import pymysql
import logging

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)

# MySQL 连接配置
MYSQL_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'username': 'root',
    'password': '123456',
    'database': 'python',
    'charset': 'utf8mb4'
}

def connect_mysql():
    """连接到 MySQL"""
    try:
        conn = pymysql.connect(**MYSQL_CONFIG)
        logging.info("成功连接到 MySQL")
        return conn
    except Exception as e:
        logging.error(f"连接 MySQL 失败: {e}")
        return None

def populate_knowledge_points():
    """填充知识点表数据"""
    conn = connect_mysql()
    if not conn:
        return
    
    try:
        cursor = conn.cursor()
        
        # 检查表是否存在
        cursor.execute("SHOW TABLES LIKE 'knowledge_point'")
        if not cursor.fetchone():
            logging.error("knowledge_point 表不存在，请先运行数据库创建脚本")
            return
        
        # 清空现有数据
        cursor.execute("DELETE FROM knowledge_point")
        logging.info("已清空现有数据")
        
        # 插入示例数据
        sample_data = [
            {
                'title': 'Python基础语法',
                'content': 'Python是一种解释型、面向对象、动态数据类型的高级程序设计语言。本章将介绍Python的基本语法，包括变量、数据类型、运算符、控制流等基础知识。',
                'question': '1,2,3',
                'url': 'https://docs.python.org/3/tutorial/',
                'stage': '1.1'
            },
            {
                'title': '数据类型与变量',
                'content': 'Python中的数据类型包括数字、字符串、列表、元组、字典、集合等。变量是存储数据的容器，不需要声明类型。',
                'question': '4,5,6',
                'url': 'https://docs.python.org/3/tutorial/introduction.html',
                'stage': '1.2'
            },
            {
                'title': '控制流语句',
                'content': 'Python的控制流语句包括if条件语句、for循环、while循环等，用于控制程序的执行流程。',
                'question': '7,8,9',
                'url': 'https://docs.python.org/3/tutorial/controlflow.html',
                'stage': '1.3'
            },
            {
                'title': '函数定义与调用',
                'content': '函数是一段可重用的代码块，可以接受参数并返回值。Python中函数的定义和调用非常灵活。',
                'question': '10,11,12',
                'url': 'https://docs.python.org/3/tutorial/controlflow.html#defining-functions',
                'stage': '1.4'
            },
            {
                'title': '面向对象编程',
                'content': 'Python支持面向对象编程，包括类、对象、继承、多态等概念。类是创建对象的模板。',
                'question': '13,14,15',
                'url': 'https://docs.python.org/3/tutorial/classes.html',
                'stage': '1.5'
            },
            {
                'title': '文件操作',
                'content': 'Python提供了丰富的文件操作功能，包括文件的读取、写入、追加等操作。',
                'question': '16,17,18',
                'url': 'https://docs.python.org/3/tutorial/inputoutput.html#reading-and-writing-files',
                'stage': '1.6'
            },
            {
                'title': '异常处理',
                'content': '异常处理是Python程序健壮性的重要组成部分，使用try-except语句可以捕获和处理程序运行时的错误。',
                'question': '19,20,21',
                'url': 'https://docs.python.org/3/tutorial/errors.html',
                'stage': '1.7'
            },
            {
                'title': '模块与包',
                'content': 'Python的模块和包机制使得代码可以更好地组织和复用。模块是包含Python代码的文件，包是包含多个模块的目录。',
                'question': '22,23,24',
                'url': 'https://docs.python.org/3/tutorial/modules.html',
                'stage': '1.8'
            }
        ]
        
        # 插入数据
        insert_sql = """
        INSERT INTO knowledge_point (title, content, question, url, stage) 
        VALUES (%s, %s, %s, %s, %s)
        """
        
        for data in sample_data:
            cursor.execute(insert_sql, (
                data['title'],
                data['content'],
                data['question'],
                data['url'],
                data['stage']
            ))
        
        # 提交事务
        conn.commit()
        logging.info(f"成功插入 {len(sample_data)} 条知识点数据")
        
        # 验证数据
        cursor.execute("SELECT COUNT(*) FROM knowledge_point")
        count = cursor.fetchone()[0]
        logging.info(f"知识点表中共有 {count} 条数据")
        
    except Exception as e:
        logging.error(f"填充数据失败: {e}")
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    logging.info("开始填充知识点表数据...")
    populate_knowledge_points()
    logging.info("数据填充完成")
