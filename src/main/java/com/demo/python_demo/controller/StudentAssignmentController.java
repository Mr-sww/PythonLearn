package com.demo.python_demo.controller;

import com.demo.python_demo.entity.Assignment;
import com.demo.python_demo.entity.AssignmentSubmission;
import com.demo.python_demo.service.AssignmentService;
import com.demo.python_demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生作业控制器
 */
@RestController
@RequestMapping("/api/student/assignments")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class StudentAssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取学生可以查看的作业列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getCourseAssignments(@PathVariable Integer courseId, @RequestParam Integer studentId) {
        // 检查权限
        if (!permissionService.hasRole(studentId, "STUDENT")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        // 只返回已发布的作业
        List<Assignment> publishedAssignments = assignments.stream()
                .filter(a -> "published".equals(a.getStatus()))
                .toList();
        
        return ResponseEntity.ok(publishedAssignments);
    }

    /**
     * 获取作业详情
     */
    @GetMapping("/{assignmentId}")
    public ResponseEntity<?> getAssignmentDetail(@PathVariable Integer assignmentId, @RequestParam Integer studentId) {
        // 检查权限
        if (!permissionService.hasRole(studentId, "STUDENT")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        // 只允许查看已发布的作业
        if (!"published".equals(assignment.getStatus())) {
            return ResponseEntity.status(403).body("作业未发布");
        }

        return ResponseEntity.ok(assignment);
    }

    /**
     * 学生提交作业
     */
    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<?> submitAssignment(@PathVariable Integer assignmentId, 
                                           @RequestParam Integer studentId,
                                           @RequestBody Map<String, String> submissionData) {
        // 检查权限
        if (!permissionService.hasPermission(studentId, "assignment:submit")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        // 检查作业是否存在且已发布
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }
        if (!"published".equals(assignment.getStatus())) {
            return ResponseEntity.status(403).body("作业未发布或已关闭");
        }

        // 创建提交
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setContent(submissionData.get("content"));

        AssignmentSubmission result = assignmentService.submitAssignment(submission);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body("提交失败");
        }
    }

    /**
     * 获取学生的作业提交
     */
    @GetMapping("/{assignmentId}/submission")
    public ResponseEntity<?> getMySubmission(@PathVariable Integer assignmentId, @RequestParam Integer studentId) {
        // 检查权限
        if (!permissionService.hasRole(studentId, "STUDENT")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        AssignmentSubmission submission = assignmentService.getSubmissionByStudentAndAssignment(studentId, assignmentId);
        if (submission == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(submission);
    }

    /**
     * 更新作业提交
     */
    @PutMapping("/{assignmentId}/submission")
    public ResponseEntity<?> updateSubmission(@PathVariable Integer assignmentId, 
                                           @RequestParam Integer studentId,
                                           @RequestBody Map<String, String> submissionData) {
        // 检查权限
        if (!permissionService.hasPermission(studentId, "assignment:submit")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        // 检查作业是否已关闭
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }
        if ("closed".equals(assignment.getStatus())) {
            return ResponseEntity.status(403).body("作业已关闭，无法修改提交");
        }

        // 更新提交
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setContent(submissionData.get("content"));

        AssignmentSubmission result = assignmentService.submitAssignment(submission);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body("更新失败");
        }
    }

    /**
     * 获取学生的所有作业提交
     */
    @GetMapping("/my-submissions")
    public ResponseEntity<?> getMySubmissions(@RequestParam Integer studentId) {
        // 检查权限
        if (!permissionService.hasRole(studentId, "STUDENT")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        List<AssignmentSubmission> submissions = assignmentService.getSubmissionsByStudentId(studentId);
        return ResponseEntity.ok(submissions);
    }

    /**
     * 获取学生的作业统计
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getMyStatistics(@RequestParam Integer studentId) {
        // 检查权限
        if (!permissionService.hasRole(studentId, "STUDENT")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        Object statistics = assignmentService.getStudentAssignmentStatistics(studentId);
        return ResponseEntity.ok(statistics);
    }
} 