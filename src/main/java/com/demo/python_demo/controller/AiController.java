package com.demo.python_demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @GetMapping("/faq")
    public List<Map<String, String>> aiFaq() {
        List<Map<String, String>> faq = new ArrayList<>();
        faq.add(Map.of("question", "如何高效学习Python?", "answer", "多练习，多做项目，善用文档和社区。"));
        faq.add(Map.of("question", "推荐哪些Python项目?", "answer", "爬虫、数据分析、Web开发等。"));
        return faq;
    }
} 