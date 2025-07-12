# AI对话功能实现说明

## 功能概述

本项目已成功集成了真正的AI对话功能，支持文本对话和文件分析，使用了豆包AI的API服务。

## 主要功能

### 1. 文本对话
- 支持自然语言问答
- 智能上下文理解
- 实时响应
- 支持Markdown格式回复

### 2. 文件分析
- 支持代码文件分析（.py, .txt, .js, .java, .cpp, .c）
- 支持图片分析（.jpg, .jpeg, .png, .gif）
- 智能代码审查和建议
- 图片内容识别和描述

### 3. 对话管理
- 多会话历史记录
- 对话导出功能
- 会话标题自动生成
- 清空对话功能

## 技术实现

### 后端实现

#### 1. AI控制器 (`AiController.java`)
```java
@RestController
@RequestMapping("/api/ai")
public class AiController {
    // 配置AI API参数
    @Value("${ai.api.url}")
    private String aiApiUrl;
    
    @Value("${ai.api.token}")
    private String aiApiToken;
    
    @Value("${ai.api.model}")
    private String aiModel;
}
```

#### 2. 主要接口
- `POST /api/ai/ask` - 文本对话接口
- `POST /api/ai/upload` - 文件分析接口
- `GET /api/ai/faq` - 常见问题接口

#### 3. 配置参数
```properties
# AI API配置
ai.api.url=https://ark.cn-beijing.volces.com/api/v3/chat/completions
ai.api.token=78f2acc1-1fd0-4eb6-b0a5-805eee21c997
ai.api.model=doubao-seed-1-6-thinking-250615

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 前端实现

#### 1. 主要页面
- `AIChatPage.vue` - 完整的AI聊天界面
- `HomePage.vue` - 主页面的AI聊天组件
- `AIChat.vue` - 简单的AI聊天页面

#### 2. 核心功能
- 实时对话界面
- 文件上传和预览
- 加载状态显示
- 错误处理
- Markdown渲染
- 响应式设计

## 使用方法

### 1. 启动服务
```bash
# 启动后端服务
cd src
mvn spring-boot:run

# 启动前端服务
cd pythondemo
npm run serve
```

### 2. 访问AI功能
- 主页面AI聊天：访问首页，在AI问答区域进行对话
- 完整AI聊天：访问 `/ai` 路径
- 简单AI聊天：访问 `/aichat` 路径

### 3. 测试AI功能
```bash
# 运行测试脚本
python test_ai_api.py
```

## API接口说明

### 1. 文本对话接口
```http
POST /api/ai/ask
Content-Type: application/json

{
    "question": "请介绍一下Python编程语言",
    "history": "之前的对话历史"
}
```

响应：
```json
{
    "success": true,
    "answer": "Python是一门简单易学、功能强大的编程语言...",
    "usage": {
        "prompt_tokens": 10,
        "completion_tokens": 50,
        "total_tokens": 60
    }
}
```

### 2. 文件分析接口
```http
POST /api/ai/upload
Content-Type: multipart/form-data

file: [文件内容]
question: "请分析这个文件的内容"
```

响应：
```json
{
    "success": true,
    "answer": "这是一个Python函数，功能是...",
    "fileUrl": "/uploads/filename.py",
    "usage": {
        "prompt_tokens": 100,
        "completion_tokens": 200,
        "total_tokens": 300
    }
}
```

## 支持的文件类型

### 代码文件
- `.py` - Python文件
- `.txt` - 文本文件
- `.js` - JavaScript文件
- `.java` - Java文件
- `.cpp` - C++文件
- `.c` - C文件

### 图片文件
- `.jpg` - JPEG图片
- `.jpeg` - JPEG图片
- `.png` - PNG图片
- `.gif` - GIF图片

## 错误处理

### 常见错误
1. **网络连接错误** - 检查网络连接和API地址
2. **认证失败** - 检查API Token是否正确
3. **文件格式错误** - 确保上传的文件类型受支持
4. **文件大小超限** - 文件大小不能超过10MB

### 错误响应格式
```json
{
    "success": false,
    "error": "错误描述信息"
}
```

## 性能优化

### 1. 请求优化
- 设置合理的超时时间
- 使用连接池
- 实现请求重试机制

### 2. 前端优化
- 防抖处理
- 加载状态显示
- 错误重试机制

### 3. 缓存策略
- 对话历史本地存储
- 常见问题缓存

## 安全考虑

### 1. API安全
- Token安全存储
- 请求频率限制
- 输入验证和过滤

### 2. 文件安全
- 文件类型验证
- 文件大小限制
- 恶意文件检测

## 扩展功能

### 1. 可能的扩展
- 语音对话功能
- 多语言支持
- 个性化设置
- 对话分享功能
- 代码执行环境

### 2. 集成其他AI服务
- OpenAI GPT
- 百度文心一言
- 阿里通义千问
- 腾讯混元

## 故障排除

### 1. AI服务不可用
- 检查API Token是否有效
- 确认网络连接正常
- 查看后端日志

### 2. 文件上传失败
- 检查文件格式是否支持
- 确认文件大小是否超限
- 查看服务器存储空间

### 3. 前端显示异常
- 检查浏览器控制台错误
- 确认API接口是否正常
- 清除浏览器缓存

## 更新日志

### v1.0.0 (2025-01-XX)
- 集成豆包AI API
- 实现文本对话功能
- 实现文件分析功能
- 添加对话历史管理
- 支持Markdown渲染
- 添加错误处理机制

## 联系方式

如有问题或建议，请联系开发团队。 