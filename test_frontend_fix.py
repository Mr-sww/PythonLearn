#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
前端修复验证脚本
"""

import os
import re

def check_vue_file(file_path):
    """检查Vue文件的HTML标签结构"""
    print(f"检查文件: {file_path}")
    
    if not os.path.exists(file_path):
        print(f"文件不存在: {file_path}")
        return False
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 检查基本的HTML标签结构
    issues = []
    
    # 检查template标签
    if '<template>' not in content or '</template>' not in content:
        issues.append("缺少template标签")
    
    # 检查script标签
    if '<script>' not in content or '</script>' not in content:
        issues.append("缺少script标签")
    
    # 检查style标签
    if '<style' not in content or '</style>' not in content:
        issues.append("缺少style标签")
    
    # 检查div标签匹配
    div_open = content.count('<div')
    div_close = content.count('</div>')
    if div_open != div_close:
        issues.append(f"div标签不匹配: 开始{div_open}个, 结束{div_close}个")
    
    # 检查table标签匹配
    table_open = content.count('<table')
    table_close = content.count('</table>')
    if table_open != table_close:
        issues.append(f"table标签不匹配: 开始{table_open}个, 结束{table_close}个")
    
    # 检查tr标签匹配
    tr_open = content.count('<tr')
    tr_close = content.count('</tr>')
    if tr_open != tr_close:
        issues.append(f"tr标签不匹配: 开始{tr_open}个, 结束{tr_close}个")
    
    # 检查td标签匹配
    td_open = content.count('<td')
    td_close = content.count('</td>')
    if td_open != td_close:
        issues.append(f"td标签不匹配: 开始{td_open}个, 结束{td_close}个")
    
    # 检查tbody标签匹配
    tbody_open = content.count('<tbody')
    tbody_close = content.count('</tbody>')
    if tbody_open != tbody_close:
        issues.append(f"tbody标签不匹配: 开始{tbody_open}个, 结束{tbody_close}个")
    
    # 检查thead标签匹配
    thead_open = content.count('<thead')
    thead_close = content.count('</thead>')
    if thead_open != thead_close:
        issues.append(f"thead标签不匹配: 开始{thead_open}个, 结束{thead_close}个")
    
    if issues:
        print("发现以下问题:")
        for issue in issues:
            print(f"  - {issue}")
        return False
    else:
        print("✅ 文件结构正确")
        return True

def main():
    """主测试函数"""
    print("开始检查前端文件...")
    
    # 检查LearningCenter.vue文件
    vue_file = "pythondemo/src/views/LearningCenter.vue"
    success = check_vue_file(vue_file)
    
    if success:
        print("\n✅ 前端文件修复成功！")
        print("现在可以正常启动前端服务了")
    else:
        print("\n❌ 前端文件仍有问题，需要进一步修复")

if __name__ == "__main__":
    main()
