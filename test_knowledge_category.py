#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试知识点分类功能
"""

import requests
import json

# 配置
BASE_URL = "http://localhost:8080"
SESSION = requests.Session()

def test_knowledge_category():
    """测试知识点分类功能"""
    
    print("=== 测试知识点分类功能 ===")
    
    # 1. 测试开始学习（带分类）
    print("\n1. 测试开始学习（带分类）")
    try:
        response = SESSION.post(f"{BASE_URL}/api/learning/knowledge/start", 
                               params={
                                   "knowledgeId": 1,
                                   "knowledgeTitle": "Python3 教程",
                                   "knowledgeCategory": "文字知识点"
                               })
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ 开始学习成功")
            print(f"   知识点ID: {data.get('knowledgeId')}")
            print(f"   知识点标题: {data.get('knowledgeTitle')}")
            print(f"   知识点分类: {data.get('knowledgeCategory')}")
            print(f"   学习状态: {data.get('status')}")
            print(f"   开始时间: {data.get('startTime')}")
        else:
            print(f"❌ 开始学习失败: {response.status_code}")
            print(f"   错误信息: {response.text}")
            
    except Exception as e:
        print(f"❌ 请求失败: {e}")
    
    # 2. 测试获取学习记录
    print("\n2. 测试获取学习记录")
    try:
        response = SESSION.get(f"{BASE_URL}/api/learning/knowledge/records", 
                              params={"limit": 10})
        
        if response.status_code == 200:
            records = response.json()
            print(f"✅ 获取学习记录成功，共 {len(records)} 条记录")
            
            for i, record in enumerate(records[:3]):  # 只显示前3条
                print(f"   记录 {i+1}:")
                print(f"     - 标题: {record.get('knowledgeTitle')}")
                print(f"     - 分类: {record.get('knowledgeCategory')}")
                print(f"     - 开始时间: {record.get('startTime')}")
                print(f"     - 学习时长: {record.get('studyTime')}秒")
                print(f"     - 进度: {record.get('progress')}%")
                print(f"     - 状态: {record.get('status')}")
        else:
            print(f"❌ 获取学习记录失败: {response.status_code}")
            print(f"   错误信息: {response.text}")
            
    except Exception as e:
        print(f"❌ 请求失败: {e}")
    
    # 3. 测试获取学习统计
    print("\n3. 测试获取学习统计")
    try:
        response = SESSION.get(f"{BASE_URL}/api/learning/knowledge/stats")
        
        if response.status_code == 200:
            stats = response.json()
            print(f"✅ 获取学习统计成功")
            print(f"   总知识点数: {stats.get('totalKnowledge')}")
            print(f"   已完成知识点数: {stats.get('completedKnowledge')}")
            print(f"   总学习时长: {stats.get('totalStudyTime')}秒")
            print(f"   连续学习天数: {stats.get('continuousDays')}")
        else:
            print(f"❌ 获取学习统计失败: {response.status_code}")
            print(f"   错误信息: {response.text}")
            
    except Exception as e:
        print(f"❌ 请求失败: {e}")

if __name__ == "__main__":
    test_knowledge_category()






