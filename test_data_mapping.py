#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试数据映射是否正确
"""

import requests
import json

# 配置
BASE_URL = "http://localhost:8080"
SESSION = requests.Session()

def test_data_mapping():
    """测试数据映射"""
    
    print("=== 测试数据映射 ===")
    
    # 1. 测试获取学习记录
    print("\n1. 获取学习记录")
    try:
        response = SESSION.get(f"{BASE_URL}/api/learning/knowledge/records", 
                              params={"limit": 5})
        
        if response.status_code == 200:
            records = response.json()
            print(f"✅ 获取学习记录成功，共 {len(records)} 条记录")
            
            if records:
                print("\n第一条记录详情:")
                record = records[0]
                print(f"   ID: {record.get('id')}")
                print(f"   知识点ID: {record.get('knowledgeId')}")
                print(f"   知识点标题: {record.get('knowledgeTitle')}")
                print(f"   学习时长: {record.get('studyTime')}")
                print(f"   进度: {record.get('progress')}")
                print(f"   状态: {record.get('status')}")
                print(f"   开始时间: {record.get('startTime')}")
                print(f"   结束时间: {record.get('endTime')}")
                print(f"   最后学习时间: {record.get('lastStudyTime')}")
                print(f"   创建时间: {record.get('createdAt')}")
                print(f"   更新时间: {record.get('updatedAt')}")
                
                # 检查字段映射
                print("\n字段映射检查:")
                print(f"   knowledgeTitle 存在: {'knowledgeTitle' in record}")
                print(f"   studyTime 存在: {'studyTime' in record}")
                print(f"   startTime 存在: {'startTime' in record}")
                print(f"   status 存在: {'status' in record}")
                
                # 检查数据内容
                print("\n数据内容检查:")
                if record.get('knowledgeTitle') and record.get('knowledgeTitle') != '未知知识点':
                    print(f"   ✅ 知识点标题正确: {record.get('knowledgeTitle')}")
                else:
                    print(f"   ❌ 知识点标题异常: {record.get('knowledgeTitle')}")
                
                if record.get('startTime') and '1970' not in str(record.get('startTime')):
                    print(f"   ✅ 开始时间正确: {record.get('startTime')}")
                else:
                    print(f"   ❌ 开始时间异常: {record.get('startTime')}")
                
                if record.get('studyTime') is not None:
                    print(f"   ✅ 学习时长存在: {record.get('studyTime')}")
                else:
                    print(f"   ❌ 学习时长为空: {record.get('studyTime')}")
            else:
                print("❌ 没有学习记录")
        else:
            print(f"❌ 获取学习记录失败: {response.status_code}")
            print(f"   错误信息: {response.text}")
            
    except Exception as e:
        print(f"❌ 请求失败: {e}")

if __name__ == "__main__":
    test_data_mapping()
