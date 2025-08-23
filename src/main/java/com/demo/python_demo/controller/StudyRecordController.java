package com.demo.python_demo.controller;

import com.demo.python_demo.entity.StudyRecord;
import com.demo.python_demo.service.StudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习记录控制器
 */
@RestController
@RequestMapping("/api/study-records")
public class StudyRecordController {

    @Autowired
    private StudyRecordService studyRecordService;

    /**
     * 获取用户的学习记录列表
     */
    @GetMapping("/")
    public ResponseEntity<List<StudyRecord>> getStudyRecords(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<StudyRecord> records = studyRecordService.getStudyRecordsByUserId(userId);
        return ResponseEntity.ok(records);
    }

    /**
     * 获取指定课程的学习记录
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudyRecord>> getStudyRecordsByCourse(
            @PathVariable Integer courseId, 
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<StudyRecord> records = studyRecordService.getStudyRecordsByUserIdAndCourseId(userId, courseId);
        return ResponseEntity.ok(records);
    }

    /**
     * 获取用户最近的学习记录
     */
    @GetMapping("/recent")
    public ResponseEntity<List<StudyRecord>> getRecentStudyRecords(
            @RequestParam(defaultValue = "5") Integer limit,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<StudyRecord> records = studyRecordService.getRecentStudyRecords(userId, limit);
        return ResponseEntity.ok(records);
    }

    /**
     * 获取用户学习统计
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> statistics = studyRecordService.getUserStudyStatistics(userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 记录学习进度（用于课程学习）
     */
    @PostMapping("/progress")
    public ResponseEntity<Map<String, Object>> recordStudyProgress(
            @RequestBody Map<String, Object> request,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Integer courseId = (Integer) request.get("courseId");
        Integer lessonId = (Integer) request.get("lessonId");
        Double progress = ((Number) request.get("progress")).doubleValue();
        Boolean completed = (Boolean) request.get("completed");
        
        boolean success = studyRecordService.recordStudyProgress(
            userId, courseId, lessonId, 0, progress, completed);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }

    /**
     * 记录代码练习完成（新增）
     */
    @PostMapping("/practice-complete")
    public ResponseEntity<Map<String, Object>> recordPracticeComplete(
            @RequestBody Map<String, Object> request,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Integer problemId = (Integer) request.get("problemId");
        Boolean passed = (Boolean) request.get("passed");
        Integer passRate = (Integer) request.get("passRate");
        
        // 将代码练习视为课程学习，problemId作为lessonId
        // 课程ID使用固定值表示代码练习课程
        Integer courseId = 9999; // 代码练习课程ID
        Integer lessonId = problemId;
        Double progress = passed ? 100.0 : (passRate != null ? passRate.doubleValue() : 0.0);
        Boolean completed = passed;
        
        boolean success = studyRecordService.recordStudyProgress(
            userId, courseId, lessonId, 0, progress, completed);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", passed ? "练习完成！" : "继续加油！");
        return ResponseEntity.ok(response);
    }

    /**
     * 保存学习记录
     */
    @PostMapping("/")
    public ResponseEntity<StudyRecord> saveStudyRecord(
            @RequestBody StudyRecord record,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        record.setUserId(userId);
        if (record.getStudyDate() == null) {
            record.setStudyDate(LocalDateTime.now());
        }
        
        StudyRecord savedRecord = studyRecordService.saveStudyRecord(record);
        return ResponseEntity.ok(savedRecord);
    }

    /**
     * 删除学习记录
     */
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Map<String, Object>> deleteStudyRecord(
            @PathVariable Integer recordId,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean success = studyRecordService.deleteStudyRecord(recordId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }
}
