package com.demo.python_demo.controller;

import com.demo.python_demo.entity.LearningProgress;
import com.demo.python_demo.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 学习进度控制器
 */
@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class LearningProgressController {

    @Autowired
    private LearningProgressService progressService;

    /**
     * 根据用户ID和课程ID获取学习进度
     */
    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<LearningProgress> getProgressByUserAndCourse(@PathVariable Integer userId, @PathVariable Integer courseId) {
        Optional<LearningProgress> progress = progressService.getProgressByUserAndCourse(userId, courseId);
        if (progress.isPresent()) {
            return ResponseEntity.ok(progress.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 根据用户ID获取所有学习进度
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LearningProgress>> getProgressByUserId(@PathVariable Integer userId) {
        List<LearningProgress> progressList = progressService.getProgressByUserId(userId);
        return ResponseEntity.ok(progressList);
    }

    /**
     * 创建或更新学习进度
     */
    @PostMapping
    public ResponseEntity<LearningProgress> saveProgress(@RequestBody LearningProgress progress) {
        LearningProgress savedProgress = progressService.saveProgress(progress);
        if (savedProgress != null) {
            return ResponseEntity.ok(savedProgress);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 更新学习进度
     */
    @PutMapping("/{progressId}")
    public ResponseEntity<LearningProgress> updateProgress(@PathVariable Integer progressId, @RequestBody LearningProgress progress) {
        Optional<LearningProgress> updatedProgress = progressService.updateProgress(progressId, progress);
        if (updatedProgress.isPresent()) {
            return ResponseEntity.ok(updatedProgress.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 删除学习进度
     */
    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Integer progressId) {
        boolean deleted = progressService.deleteProgress(progressId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 获取用户最近学习的课程
     */
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<LearningProgress>> getRecentCourses(@PathVariable Integer userId, @RequestParam(defaultValue = "5") Integer limit) {
        List<LearningProgress> recentCourses = progressService.getRecentCourses(userId, limit);
        return ResponseEntity.ok(recentCourses);
    }

    /**
     * 获取用户学习统计
     */
    @GetMapping("/user/{userId}/statistics")
    public ResponseEntity<Object> getUserStatistics(@PathVariable Integer userId) {
        Object statistics = progressService.getUserStatistics(userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取课程学习人数
     */
    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<Map<String, Integer>> getCourseStudentCount(@PathVariable Integer courseId) {
        int count = progressService.getCourseStudentCount(courseId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取课程完成人数
     */
    @GetMapping("/course/{courseId}/completed")
    public ResponseEntity<Map<String, Integer>> getCourseCompletedCount(@PathVariable Integer courseId) {
        int count = progressService.getCourseCompletedCount(courseId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 更新课时学习进度
     */
    @PostMapping("/lesson")
    public ResponseEntity<Map<String, String>> updateLessonProgress(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        Integer courseId = (Integer) params.get("courseId");
        Integer lessonId = (Integer) params.get("lessonId");
        Double progress = (Double) params.get("progress");
        Integer timeSpent = (Integer) params.get("timeSpent");

        if (userId == null || courseId == null || lessonId == null || progress == null || timeSpent == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "参数不完整"));
        }

        boolean success = progressService.updateLessonProgress(userId, courseId, lessonId, progress, timeSpent);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "学习进度更新成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "学习进度更新失败"));
    }

    /**
     * 完成课程
     */
    @PostMapping("/course/{courseId}/complete")
    public ResponseEntity<Map<String, String>> completeCourse(@PathVariable Integer courseId, @RequestBody Map<String, Integer> params) {
        Integer userId = params.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户ID不能为空"));
        }

        boolean success = progressService.completeCourse(userId, courseId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "课程完成！"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "课程完成失败"));
    }
} 