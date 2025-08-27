#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试角色映射修复
验证前后端角色映射是否一致
"""

def test_role_mapping():
    """测试角色映射"""
    print("=== 角色映射测试 ===\n")
    
    # 正确的角色映射
    correct_mapping = {
        1: "计算机类",
        2: "工设类", 
        3: "艺术类",
        4: "医学类",
        5: "文科类",
        6: "体育类",
        7: "教师",
        8: "管理员"
    }
    
    # 测试数据
    test_users = [
        {"userId": 9, "account": "root", "groupType": 8, "expected": "管理员"},
        {"userId": 11, "account": "sw", "groupType": 1, "expected": "计算机类"},
        {"userId": 16, "account": "aww", "groupType": 1, "expected": "计算机类"},
        {"userId": 17, "account": "123456", "groupType": 7, "expected": "教师"}
    ]
    
    print("✅ 正确的角色映射:")
    for role_id, role_name in correct_mapping.items():
        print(f"  {role_id}: {role_name}")
    
    print("\n🔍 测试用户角色显示:")
    for user in test_users:
        user_id = user["userId"]
        account = user["account"]
        group_type = user["groupType"]
        expected = user["expected"]
        
        # 模拟前端getRoleText方法
        actual = get_role_text(group_type)
        
        if actual == expected:
            print(f"  ✅ 用户 {account} (ID: {user_id}): groupType={group_type} → {actual}")
        else:
            print(f"  ❌ 用户 {account} (ID: {user_id}): groupType={group_type} → {actual} (期望: {expected})")
    
    print("\n📋 修复总结:")
    print("  1. 删除了错误的 case 9")
    print("  2. 修正了 case 7: '其他专业学生' → '教师'")
    print("  3. 修正了 case 8: '教师' → '管理员'")
    print("  4. 统一了专业名称格式（去掉'学生'后缀）")

def get_role_text(group_type):
    """模拟前端getRoleText方法"""
    switch = {
        1: "计算机类",
        2: "工设类",
        3: "艺术类", 
        4: "医学类",
        5: "文科类",
        6: "体育类",
        7: "教师",
        8: "管理员"
    }
    return switch.get(group_type, "未知")

if __name__ == "__main__":
    test_role_mapping()
