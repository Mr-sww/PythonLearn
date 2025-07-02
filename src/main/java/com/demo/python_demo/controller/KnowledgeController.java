package com.demo.python_demo.controller;

import com.demo.python_demo.repository.CourseChapterRepository;
import com.demo.python_demo.repository.CourseLessonRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    private CourseChapterRepository chapterRepo;
    @Autowired
    private CourseLessonRepository lessonRepo;

    // 获取知识点目录
    @GetMapping("/catalog")
    public List<Map<String, Object>> getCatalog() {
        // 只返回章节id和标题
        List<Map<String, Object>> result = new ArrayList<>();
        chapterRepo.findAllChapters().forEach(ch -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ch.getChapterId());
            map.put("title", ch.getTitle());
            result.add(map);
        });
        return result;
    }

    // 获取知识点详情和课时列表
    @GetMapping("/detail")
    public Map<String, Object> getDetail(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        // 章节信息
        chapterRepo.findAllChapters().stream()
            .filter(ch -> ch.getChapterId().equals(id))
            .findFirst()
            .ifPresent(ch -> {
                Map<String, Object> knowledge = new HashMap<>();
                knowledge.put("id", ch.getChapterId());
                knowledge.put("title", ch.getTitle());
                knowledge.put("content", ch.getDescription());
                result.put("knowledge", knowledge);
            });
        // 课时列表
        List<Map<String, Object>> problems = new ArrayList<>();
        lessonRepo.findLessonsByChapterId(id).forEach(lesson -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", lesson.getLessonId());
            map.put("title", lesson.getTitle());
            problems.add(map);
        });
        result.put("problems", problems);
        return result;
    }
}
