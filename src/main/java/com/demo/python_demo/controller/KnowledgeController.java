package com.demo.python_demo.controller;

import com.demo.python_demo.repository.CourseChapterRepository;
import com.demo.python_demo.repository.CourseLessonRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import com.demo.python_demo.service.KnowledgeService;
import com.demo.python_demo.service.ProblemService;
import com.demo.python_demo.entity.KnowledgePoint;
import com.demo.python_demo.repository.KnowledgePointRepository;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    private CourseChapterRepository chapterRepo;
    @Autowired
    private CourseLessonRepository lessonRepo;
    @Autowired
    private KnowledgeService knowledgeService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private KnowledgePointRepository knowledgePointRepository;

    // 获取知识点目录（修正版，返回知识点表内容）
    @GetMapping("/catalog")
    public List<KnowledgePoint> getCatalog() {
        return knowledgePointRepository.findAllOrderByStage();
    }

    // 获取知识点详情和题目列表
    @GetMapping("/{id}/detail")
    public Map<String, Object> getKnowledgeDetailWithProblems(@PathVariable("id") int id) {
        // 1. 查知识点
        KnowledgePoint kp = knowledgeService.getById(id);
        // 2. 解析 question 字段
        List<String> questionIds = new ArrayList<>();
        if (kp.getQuestion() != null && !kp.getQuestion().isEmpty()) {
            for (String qid : kp.getQuestion().split("[,，\\s]+")) {
                if (!qid.trim().isEmpty()) questionIds.add(qid.trim());
            }
        }
        // 3. 查题目
        List<com.demo.python_demo.entity.PythonProblem> problems = questionIds.isEmpty() ? new ArrayList<>() : problemService.findByIds(questionIds);

        // 4. 返回
        Map<String, Object> result = new HashMap<>();
        result.put("knowledge", kp);
        result.put("problems", problems);
        return result;
    }
}
