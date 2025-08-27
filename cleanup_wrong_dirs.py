#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
清理错误的目录结构
删除 src/main/java/com/demo/java 目录
"""

import os
import shutil

def cleanup_wrong_directories():
    """清理错误的目录结构"""
    wrong_path = "src/main/java/com/demo/java"
    
    if os.path.exists(wrong_path):
        print(f"发现错误的目录: {wrong_path}")
        try:
            shutil.rmtree(wrong_path)
            print(f"✅ 成功删除错误目录: {wrong_path}")
        except Exception as e:
            print(f"❌ 删除失败: {e}")
    else:
        print(f"✅ 目录 {wrong_path} 不存在，无需清理")
    
    # 检查清理结果
    if os.path.exists(wrong_path):
        print(f"⚠️ 目录仍然存在: {wrong_path}")
    else:
        print(f"✅ 清理完成，目录已删除")

if __name__ == "__main__":
    cleanup_wrong_directories()
