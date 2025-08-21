#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
CORS修复验证脚本
"""

import requests
import json

# 配置
BASE_URL = "http://localhost:8080"
SESSION = requests.Session()

def test_cors_config():
    """测试CORS配置"""
    print("=== 测试CORS配置 ===")
    
    # 测试OPTIONS预检请求
    headers = {
        'Origin': 'http://localhost:8081',
        'Access-Control-Request-Method': 'POST',
        'Access-Control-Request-Headers': 'Content-Type'
    }
    
    response = SESSION.options(f"{BASE_URL}/api/study-records/statistics", headers=headers)
    print(f"OPTIONS请求状态: {response.status_code}")
    print(f"Access-Control-Allow-Origin: {response.headers.get('Access-Control-Allow-Origin', 'Not Set')}")
    print(f"Access-Control-Allow-Credentials: {response.headers.get('Access-Control-Allow-Credentials', 'Not Set')}")
    
    return response.status_code == 200

def test_login():
    """测试登录"""
    print("\n=== 测试登录 ===")
    login_data = {
        "username": "testuser",
        "password": "123456"
    }
    
    response = SESSION.post(f"{BASE_URL}/api/auth/login", json=login_data)
    print(f"登录状态: {response.status_code}")
    if response.status_code == 200:
        print("登录成功")
        return True
    else:
        print(f"登录失败: {response.text}")
        return False

def test_study_records_api():
    """测试学习记录API"""
    print("\n=== 测试学习记录API ===")
    
    # 测试获取统计
    response = SESSION.get(f"{BASE_URL}/api/study-records/statistics")
    print(f"获取统计状态: {response.status_code}")
    
    # 测试获取最近记录
    response = SESSION.get(f"{BASE_URL}/api/study-records/recent?limit=5")
    print(f"获取最近记录状态: {response.status_code}")
    
    return True

def main():
    """主测试函数"""
    print("开始测试CORS修复...")
    
    # 测试CORS配置
    if not test_cors_config():
        print("CORS配置测试失败")
        return
    
    # 测试登录
    if not test_login():
        print("登录测试失败")
        return
    
    # 测试API
    test_study_records_api()
    
    print("\n=== 测试完成 ===")
    print("如果所有测试都通过，说明CORS问题已修复")

if __name__ == "__main__":
    main()
