#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI API 测试脚本
用于测试后端AI接口是否正常工作
"""

import requests
import json
import base64

# 配置
BASE_URL = "http://localhost:8080"
AI_API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions"
AI_TOKEN = "78f2acc1-1fd0-4eb6-b0a5-805eee21c997"
AI_MODEL = "doubao-seed-1-6-thinking-250615"

def test_direct_ai_api():
    """直接测试AI API"""
    print("=== 直接测试AI API ===")
    
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {AI_TOKEN}"
    }
    
    data = {
        "model": AI_MODEL,
        "messages": [
            {
                "role": "user",
                "content": "你好，请简单介绍一下Python编程语言"
            }
        ],
        "max_tokens": 500,
        "temperature": 0.7
    }
    
    try:
        response = requests.post(AI_API_URL, headers=headers, json=data, timeout=30)
        print(f"状态码: {response.status_code}")
        
        if response.status_code == 200:
            result = response.json()
            print("✅ AI API 直接调用成功")
            print(f"回复内容: {result['choices'][0]['message']['content']}")
            return True
        else:
            print(f"❌ AI API 调用失败: {response.text}")
            return False
    except Exception as e:
        print(f"❌ AI API 调用异常: {e}")
        return False

def test_backend_ai_ask():
    """测试后端AI问答接口"""
    print("\n=== 测试后端AI问答接口 ===")
    
    url = f"{BASE_URL}/api/ai/ask"
    data = {
        "question": "请简单介绍一下Python编程语言的特点",
        "history": ""
    }
    
    try:
        response = requests.post(url, json=data, timeout=30)
        print(f"状态码: {response.status_code}")
        
        if response.status_code == 200:
            result = response.json()
            if result.get('success'):
                print("✅ 后端AI问答接口调用成功")
                print(f"回复内容: {result['answer']}")
                return True
            else:
                print(f"❌ 后端AI问答接口返回错误: {result.get('error')}")
                return False
        else:
            print(f"❌ 后端AI问答接口调用失败: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 后端AI问答接口调用异常: {e}")
        return False

def test_backend_ai_upload():
    """测试后端AI文件上传接口"""
    print("\n=== 测试后端AI文件上传接口 ===")
    
    # 创建一个简单的Python代码文件
    test_code = '''def hello_world():
    """简单的Hello World函数"""
    print("Hello, World!")
    return "Hello from Python!"

# 测试函数
if __name__ == "__main__":
    result = hello_world()
    print(f"函数返回值: {result}")
'''
    
    url = f"{BASE_URL}/api/ai/upload"
    
    try:
        files = {
            'file': ('test.py', test_code, 'text/plain')
        }
        data = {
            'question': '请分析这段Python代码的功能和结构'
        }
        
        response = requests.post(url, files=files, data=data, timeout=60)
        print(f"状态码: {response.status_code}")
        
        if response.status_code == 200:
            result = response.json()
            if result.get('success'):
                print("✅ 后端AI文件上传接口调用成功")
                print(f"回复内容: {result['answer']}")
                return True
            else:
                print(f"❌ 后端AI文件上传接口返回错误: {result.get('error')}")
                return False
        else:
            print(f"❌ 后端AI文件上传接口调用失败: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 后端AI文件上传接口调用异常: {e}")
        return False

def test_backend_faq():
    """测试后端FAQ接口"""
    print("\n=== 测试后端FAQ接口 ===")
    
    url = f"{BASE_URL}/api/ai/faq"
    
    try:
        response = requests.get(url, timeout=10)
        print(f"状态码: {response.status_code}")
        
        if response.status_code == 200:
            result = response.json()
            print("✅ 后端FAQ接口调用成功")
            print(f"FAQ数量: {len(result)}")
            for i, faq in enumerate(result, 1):
                print(f"  {i}. {faq['question']} -> {faq['answer']}")
            return True
        else:
            print(f"❌ 后端FAQ接口调用失败: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 后端FAQ接口调用异常: {e}")
        return False

def main():
    """主测试函数"""
    print("开始AI API测试...\n")
    
    # 测试直接AI API
    direct_success = test_direct_ai_api()
    
    # 测试后端接口
    backend_ask_success = test_backend_ai_ask()
    backend_upload_success = test_backend_ai_upload()
    backend_faq_success = test_backend_faq()
    
    # 总结
    print("\n=== 测试总结 ===")
    print(f"直接AI API: {'✅ 成功' if direct_success else '❌ 失败'}")
    print(f"后端AI问答: {'✅ 成功' if backend_ask_success else '❌ 失败'}")
    print(f"后端AI上传: {'✅ 成功' if backend_upload_success else '❌ 失败'}")
    print(f"后端FAQ: {'✅ 成功' if backend_faq_success else '❌ 失败'}")
    
    if all([direct_success, backend_ask_success, backend_upload_success, backend_faq_success]):
        print("\n🎉 所有测试通过！AI功能正常工作。")
    else:
        print("\n⚠️  部分测试失败，请检查配置和网络连接。")

if __name__ == "__main__":
    main() 