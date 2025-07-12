package com.demo.python_demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    
    @Value("${ai.api.url:https://ark.cn-beijing.volces.com/api/v3/chat/completions}")
    private String aiApiUrl;
    
    @Value("${ai.api.token:78f2acc1-1fd0-4eb6-b0a5-805eee21c997}")
    private String aiApiToken;
    
    @Value("${ai.api.model:doubao-seed-1-6-thinking-250615}")
    private String aiModel;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @GetMapping("/faq")
    public List<Map<String, String>> aiFaq() {
        List<Map<String, String>> faq = new ArrayList<>();
        faq.add(Map.of("question", "如何高效学习Python?", "answer", "多练习，多做项目，善用文档和社区。"));
        faq.add(Map.of("question", "推荐哪些Python项目?", "answer", "爬虫、数据分析、Web开发等。"));
        return faq;
    }
    
    @PostMapping("/ask")
    public Map<String, Object> askAI(@RequestBody Map<String, Object> request) {
        try {
            String question = (String) request.get("question");
            String history = (String) request.getOrDefault("history", "");
            
            // 构建消息列表
            List<Map<String, Object>> messages = new ArrayList<>();
            
            // 如果有历史记录，添加系统消息
            if (!history.isEmpty()) {
                messages.add(Map.of(
                    "role", "system",
                    "content", "你是一个专业的Python编程助手，请用中文回答用户的问题。"
                ));
            }
            
            // 添加用户消息
            messages.add(Map.of(
                "role", "user",
                "content", question
            ));
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", aiModel);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 2000);
            requestBody.put("temperature", 0.7);
            
            // 发送请求到AI API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiApiToken);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                aiApiUrl, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    String content = (String) message.get("content");
                    
                    return Map.of(
                        "success", true,
                        "answer", content,
                        "usage", responseBody.get("usage")
                    );
                }
            }
            
            return Map.of("success", false, "error", "AI响应格式错误");
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", "AI服务调用失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload")
    public Map<String, Object> uploadAndAnalyze(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "question", defaultValue = "请分析这个文件的内容") String question) {
        try {
            // 检查文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || !isValidFileType(fileName)) {
                return Map.of("success", false, "error", "不支持的文件类型");
            }
            
            // 读取文件内容
            String fileContent = new String(file.getBytes());
            
            // 构建消息
            List<Map<String, Object>> messages = new ArrayList<>();
            
            // 系统消息
            messages.add(Map.of(
                "role", "system",
                "content", "你是一个专业的Python编程助手，请分析用户上传的代码文件并给出建议。"
            ));
            
            // 用户消息
            List<Map<String, Object>> content = new ArrayList<>();
            content.add(Map.of("type", "text", "text", question));
            
            // 如果是图片文件，添加图片URL
            if (isImageFile(fileName)) {
                // 这里需要先将图片上传到某个存储服务，然后获取URL
                // 暂时使用base64编码
                String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
                content.add(Map.of(
                    "type", "image_url",
                    "image_url", Map.of("url", "data:image/jpeg;base64," + base64Image)
                ));
            } else {
                // 文本文件，直接添加文本内容
                content.add(Map.of("type", "text", "text", "\n文件内容：\n```\n" + fileContent + "\n```"));
            }
            
            messages.add(Map.of("role", "user", "content", content));
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", aiModel);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 2000);
            requestBody.put("temperature", 0.7);
            
            // 发送请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiApiToken);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                aiApiUrl, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    String content_response = (String) message.get("content");
                    
                    return Map.of(
                        "success", true,
                        "answer", content_response,
                        "fileUrl", "/uploads/" + fileName,
                        "usage", responseBody.get("usage")
                    );
                }
            }
            
            return Map.of("success", false, "error", "AI响应格式错误");
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", "文件处理失败: " + e.getMessage());
        }
    }
    
    private boolean isValidFileType(String fileName) {
        String[] allowedExtensions = {".py", ".txt", ".js", ".java", ".cpp", ".c", ".jpg", ".jpeg", ".png", ".gif"};
        String lowerFileName = fileName.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerFileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isImageFile(String fileName) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        String lowerFileName = fileName.toLowerCase();
        for (String ext : imageExtensions) {
            if (lowerFileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
} 