#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试项目编译
"""

import subprocess
import sys
import os

def test_compile():
    """测试项目编译"""
    
    print("=== 测试项目编译 ===")
    
    # 检查Maven是否可用
    try:
        result = subprocess.run(['mvn', '--version'], 
                              capture_output=True, text=True, check=True)
        print("✅ Maven 可用")
        print(f"   Maven版本: {result.stdout.split('Apache Maven')[1].split('(')[0].strip()}")
    except (subprocess.CalledProcessError, FileNotFoundError):
        print("❌ Maven 不可用，请确保已安装Maven并添加到PATH")
        return False
    
    # 尝试编译项目
    print("\n正在编译项目...")
    try:
        result = subprocess.run(['mvn', 'compile'], 
                              capture_output=True, text=True, check=True)
        print("✅ 项目编译成功！")
        return True
    except subprocess.CalledProcessError as e:
        print("❌ 项目编译失败")
        print(f"   错误输出: {e.stderr}")
        return False

if __name__ == "__main__":
    success = test_compile()
    sys.exit(0 if success else 1)




