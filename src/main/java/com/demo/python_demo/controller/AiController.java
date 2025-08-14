package com.demo.python_demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

/**
 * AI控制器
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class AiController {

    /**
     * 获取AI问答
     */
    @GetMapping("/faq")
    public ResponseEntity<List<Map<String, String>>> getAiFaq() {
        List<Map<String, String>> faq = new ArrayList<>();
        faq.add(Map.of("question", "如何高效学习Python?", "answer", "多练习，多做项目，善用文档和社区。"));
        faq.add(Map.of("question", "推荐哪些Python项目?", "answer", "爬虫、数据分析、Web开发等。"));
        return ResponseEntity.ok(faq);
    }
} 