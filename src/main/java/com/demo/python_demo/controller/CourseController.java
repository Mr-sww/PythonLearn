package com.demo.python_demo.controller;

import com.demo.python_demo.entity.PythonVideo;
import com.demo.python_demo.repository.PythonVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 课程控制器
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CourseController {

    @Autowired
    private PythonVideoRepository pythonVideoRepository;

    /**
     * 获取所有课程
     */
    @GetMapping
    public List<PythonVideo> getAllCourses() {
        return pythonVideoRepository.findAll();
    }

    /**
     * 获取课程详情
     */
    @GetMapping("/{id}")
    public PythonVideo getCourseById(@PathVariable Integer id) {
        return pythonVideoRepository.findById(id);
    }

    /**
     * 获取推荐课程
     */
    @GetMapping("/recommend")
    public List<PythonVideo> getRecommendCourses() {
        List<PythonVideo> list = new ArrayList<>();
        list.add(pythonVideoRepository.findById(2281));
        list.add(pythonVideoRepository.findById(2282));
        list.add(pythonVideoRepository.findById(2283));
        return list;
    }

    // 学习进度（mock数据）
    @GetMapping("/learning-progress")
    public Map<String, Object> getLearningProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("progress", 80);
        progress.put("days", 12);
        progress.put("coursesCompleted", 5);
        return progress;
    }

    // 推荐练习题（mock数据）
    @GetMapping("/recommend-problems")
    public List<Map<String, Object>> getRecommendProblems() {
        List<Map<String, Object>> problems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", "task" + i);
            p.put("title", "推荐练习题" + i);
            p.put("difficulty", "中等");
            problems.add(p);
        }
        return problems;
    }

    // 练习进度（mock数据）
    @GetMapping("/practice-progress")
    public Map<String, Object> getPracticeProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("completed", 23);
        progress.put("accuracy", 92);
        progress.put("practiceTime", 8);
        progress.put("continuousDays", 4);
        return progress;
    }

    // AI问答（mock数据）
    @GetMapping("/ai-faq")
    public List<Map<String, String>> getAiFaq() {
        List<Map<String, String>> faq = new ArrayList<>();
        faq.add(Map.of("question", "如何高效学习Python?", "answer", "多练习，多做项目，善用文档和社区。"));
        faq.add(Map.of("question", "推荐哪些Python项目?", "answer", "爬虫、数据分析、Web开发等。"));
        return faq;
    }

    // 兼容前端请求的接口路径，转发到现有mock方法
    @GetMapping("/api/practice/progress")
    public Map<String, Object> practiceProgress() {
        return getPracticeProgress();
    }

    @GetMapping("/api/practice/recommend")
    public List<Map<String, Object>> recommendProblems() {
        return getRecommendProblems();
    }

    @GetMapping("/api/ai/faq")
    public List<Map<String, String>> aiFaq() {
        return getAiFaq();
    }
} 