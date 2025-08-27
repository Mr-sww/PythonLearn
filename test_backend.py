#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试后端API是否正常工作的脚本
"""

import requests
import json

def test_backend_apis():
    """测试后端API"""
    base_url = "http://localhost:8080"
    
    print("=== 测试后端API ===")
    
    # 测试课程API
    print("\n1. 测试课程API:")
    try:
        response = requests.get(f"{base_url}/api/admin/courses")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            courses = response.json()
            print(f"   课程数量: {len(courses)}")
            if courses:
                print(f"   第一个课程: {courses[0].get('title', 'N/A')}")
        else:
            print(f"   错误: {response.text}")
    except Exception as e:
        print(f"   连接失败: {e}")
    
    # 测试用户API
    print("\n2. 测试用户API:")
    try:
        response = requests.get(f"{base_url}/api/admin/users")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            users = response.json()
            print(f"   用户数量: {len(users)}")
            if users:
                print(f"   第一个用户: {users[0].get('account', 'N/A')}")
        else:
            print(f"   错误: {response.text}")
    except Exception as e:
        print(f"   连接失败: {e}")
    
    # 测试课程统计API
    print("\n3. 测试课程统计API:")
    try:
        response = requests.get(f"{base_url}/api/admin/courses/stats")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            stats = response.json()
            print(f"   统计信息: {json.dumps(stats, ensure_ascii=False, indent=2)}")
        else:
            print(f"   错误: {response.text}")
    except Exception as e:
        print(f"   连接失败: {e}")
    
    # 测试普通用户API
    print("\n4. 测试普通用户API:")
    try:
        response = requests.get(f"{base_url}/api/user")
        print(f"   状态码: {response.status_code}")
        if response.status_code == 200:
            users = response.json()
            print(f"   用户数量: {len(users)}")
            if users:
                print(f"   第一个用户: {users[0].get('account', 'N/A')}")
        else:
            print(f"   错误: {response.text}")
    except Exception as e:
        print(f"   连接失败: {e}")

if __name__ == "__main__":
    test_backend_apis()
