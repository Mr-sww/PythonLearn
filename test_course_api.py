#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试课程API
验证后端课程接口是否正常工作
"""

import requests
import json

def test_course_api():
    """测试课程API"""
    base_url = "http://localhost:8080"
    
    print("=== 课程API测试 ===\n")
    
    # 测试1: 获取所有课程
    print("1. 测试获取所有课程")
    try:
        response = requests.get(f"{base_url}/api/admin/courses")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            courses = response.json()
            print(f"   返回课程数量: {len(courses)}")
            if courses:
                print(f"   第一个课程: {courses[0].get('title', 'N/A')} - 状态: {courses[0].get('status', 'N/A')}")
            else:
                print("   ⚠️ 没有返回课程数据")
        else:
            print(f"   ❌ 请求失败: {response.text}")
    except Exception as e:
        print(f"   ❌ 请求异常: {e}")
    
    print()
    
    # 测试2: 获取课程统计
    print("2. 测试获取课程统计")
    try:
        response = requests.get(f"{base_url}/api/admin/courses/stats")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            stats = response.json()
            print(f"   统计信息: {stats}")
        else:
            print(f"   ❌ 请求失败: {response.text}")
    except Exception as e:
        print(f"   ❌ 请求异常: {e}")
    
    print()
    
    # 测试3: 测试数据库健康检查
    print("3. 测试数据库健康检查")
    try:
        response = requests.get(f"{base_url}/api/admin/health/database")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            health = response.json()
            print(f"   数据库状态: {health.get('status', 'N/A')}")
            print(f"   课程表状态: {health.get('course_table', 'N/A')}")
            print(f"   课程数量: {health.get('course_count', 'N/A')}")
        else:
            print(f"   ❌ 请求失败: {response.text}")
    except Exception as e:
        print(f"   ❌ 请求异常: {e}")
    
    print("\n=== 测试完成 ===")

if __name__ == "__main__":
    test_course_api()
