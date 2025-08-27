#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
简单测试课程API
验证基本的课程获取功能
"""

import requests
import json

def test_simple_course_api():
    """测试简单的课程API"""
    base_url = "http://localhost:8080"
    
    print("=== 简单课程API测试 ===\n")
    
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
                
                # 筛选待审核的课程
                pending_courses = [c for c in courses if c.get('status') == 'pending']
                print(f"   待审核课程数量: {len(pending_courses)}")
                if pending_courses:
                    print(f"   待审核课程: {pending_courses[0].get('title', 'N/A')}")
            else:
                print("   ⚠️ 没有返回课程数据")
        else:
            print(f"   ❌ 请求失败: {response.text}")
    except Exception as e:
        print(f"   ❌ 请求异常: {e}")
    
    print("\n=== 测试完成 ===")

if __name__ == "__main__":
    test_simple_course_api()
