# CORS配置修复说明

## 🐛 问题描述

启动Spring Boot应用时出现以下错误：

```
When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
```

## 🔍 问题原因

这个错误是由于CORS配置冲突导致的：

1. **全局CORS配置**：在`WebConfig.java`中已经正确配置了全局CORS
2. **控制器级CORS配置**：在`StudyRecordController.java`中又添加了`@CrossOrigin`注解
3. **配置冲突**：当`allowCredentials=true`时，不能使用通配符`*`作为`allowedOrigins`

## ✅ 解决方案

### 1. 移除控制器级CORS配置

从`StudyRecordController.java`中移除`@CrossOrigin`注解：

```java
// 修改前
@RestController
@RequestMapping("/api/study-records")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class StudyRecordController {

// 修改后
@RestController
@RequestMapping("/api/study-records")
public class StudyRecordController {
```

### 2. 保持全局CORS配置

`WebConfig.java`中的全局CORS配置已经正确：

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:8081")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*");
}
```

## 🚀 验证修复

### 1. 启动应用
```bash
cd src
mvn spring-boot:run
```

### 2. 运行测试脚本
```bash
python test_cors_fix.py
```

### 3. 检查输出
如果看到以下输出，说明修复成功：
```
=== 测试CORS配置 ===
OPTIONS请求状态: 200
Access-Control-Allow-Origin: http://localhost:8081
Access-Control-Allow-Credentials: true

=== 测试登录 ===
登录状态: 200
登录成功

=== 测试学习记录API ===
获取统计状态: 200
获取最近记录状态: 200

=== 测试完成 ===
如果所有测试都通过，说明CORS问题已修复
```

## 📋 CORS配置说明

### 全局配置 (推荐)
- 在`WebConfig.java`中统一配置
- 适用于所有控制器
- 避免重复配置

### 控制器级配置 (不推荐)
- 在每个控制器上使用`@CrossOrigin`注解
- 容易产生配置冲突
- 维护困难

## 🔧 最佳实践

1. **使用全局CORS配置**：在`WebConfig.java`中统一配置
2. **避免重复配置**：不要在控制器上重复添加`@CrossOrigin`
3. **明确指定源**：使用具体的域名而不是通配符
4. **启用凭据**：设置`allowCredentials(true)`以支持Session

## 📞 故障排除

如果仍然有问题，请检查：

1. **端口配置**：确保前端运行在8081端口
2. **域名配置**：确保CORS配置中的域名正确
3. **缓存问题**：清除浏览器缓存
4. **代理配置**：检查前端代理配置

## ✅ 修复完成

现在您可以正常启动Spring Boot应用并使用学习记录功能了！
