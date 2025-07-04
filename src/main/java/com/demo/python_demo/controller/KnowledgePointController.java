package com.demo.python_demo.controller;

import com.demo.python_demo.entity.KnowledgePoint;
import com.demo.python_demo.repository.KnowledgePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
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
}
