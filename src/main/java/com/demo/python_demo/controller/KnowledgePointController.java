package com.demo.python_demo.controller;

import com.demo.python_demo.entity.KnowledgePoint;
import com.demo.python_demo.repository.KnowledgePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class KnowledgePointController {
    @Autowired
    private KnowledgePointRepository repo;

    @GetMapping("/points")
    public List<KnowledgePoint> getAllPoints() {
        return repo.findAllOrderByStage();
    }

    @GetMapping("/point")
    public KnowledgePoint getPoint(@RequestParam String title) {
        return repo.findByTitle(title);
    }

    /**
     * 获取知识目录（为前端提供mock数据）
     */
    @GetMapping("/catalog")
    public List<Map<String, Object>> getCatalog() {
        List<Map<String, Object>> catalog = new ArrayList<>();
        catalog.add(Map.of("title", "Python基础", "status", "已完成"));
        catalog.add(Map.of("title", "Python进阶", "status", "进行中"));
        catalog.add(Map.of("title", "Web开发", "status", "未解锁"));
        return catalog;
    }
}
