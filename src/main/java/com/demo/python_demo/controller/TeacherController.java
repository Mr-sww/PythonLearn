package com.demo.python_demo.controller;

import com.demo.python_demo.entity.Assignment;
import com.demo.python_demo.entity.AssignmentSubmission;
import com.demo.python_demo.entity.User;
import com.demo.python_demo.service.AssignmentService;
import com.demo.python_demo.service.PermissionService;
import com.demo.python_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教师专用控制器
 */
@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class TeacherController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    /**
     * 获取教师信息
     */
    @GetMapping("/profile/{teacherId}")
    public ResponseEntity<?> getTeacherProfile(@PathVariable Integer teacherId) {
        // 检查权限
        if (!permissionService.hasRole(teacherId, "TEACHER")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        User teacher = userService.getUserById(teacherId).orElse(null);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(teacher);
    }

    /**
     * 获取教师的所有作业
     */
    @GetMapping("/assignments/{teacherId}")
    public ResponseEntity<?> getTeacherAssignments(@PathVariable Integer teacherId) {
        // 检查权限
        if (!permissionService.hasRole(teacherId, "TEACHER")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        List<Assignment> assignments = assignmentService.getAssignmentsByTeacherId(teacherId);
        return ResponseEntity.ok(assignments);
    }

    /**
     * 创建新作业
     */
    @PostMapping("/assignments")
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment) {
        // 检查权限
        if (!permissionService.hasPermission(assignment.getTeacherId(), "assignment:create")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        if (createdAssignment != null) {
            return ResponseEntity.ok(createdAssignment);
        } else {
            return ResponseEntity.badRequest().body("创建作业失败");
        }
    }

    /**
     * 更新作业
     */
    @PutMapping("/assignments/{assignmentId}")
    public ResponseEntity<?> updateAssignment(@PathVariable Integer assignmentId, @RequestBody Assignment assignment) {
        // 检查权限
        if (!permissionService.hasPermission(assignment.getTeacherId(), "assignment:edit")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        assignment.setAssignmentId(assignmentId);
        Assignment updatedAssignment = assignmentService.updateAssignment(assignment);
        if (updatedAssignment != null) {
            return ResponseEntity.ok(updatedAssignment);
        } else {
            return ResponseEntity.badRequest().body("更新作业失败");
        }
    }

    /**
     * 删除作业
     */
    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Integer assignmentId, @RequestParam Integer teacherId) {
        // 检查权限
        if (!permissionService.hasPermission(teacherId, "assignment:delete")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        boolean success = assignmentService.deleteAssignment(assignmentId, teacherId);
        if (success) {
            return ResponseEntity.ok("作业删除成功");
        } else {
            return ResponseEntity.badRequest().body("删除作业失败");
        }
    }

    /**
     * 获取作业的所有提交
     */
    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<?> getAssignmentSubmissions(@PathVariable Integer assignmentId, @RequestParam Integer teacherId) {
        // 检查权限
        if (!permissionService.hasPermission(teacherId, "assignment:grade")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        List<AssignmentSubmission> submissions = assignmentService.getSubmissionsByAssignmentId(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    /**
     * 批改作业
     */
    @PostMapping("/assignments/{assignmentId}/grade")
    public ResponseEntity<?> gradeAssignment(@PathVariable Integer assignmentId, 
                                           @RequestParam Integer submissionId,
                                           @RequestParam Integer teacherId,
                                           @RequestBody Map<String, Object> gradeData) {
        // 检查权限
        if (!permissionService.hasPermission(teacherId, "assignment:grade")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        Double score = Double.valueOf(gradeData.get("score").toString());
        String feedback = (String) gradeData.get("feedback");

        boolean success = assignmentService.gradeSubmission(submissionId, score, feedback, teacherId);
        if (success) {
            return ResponseEntity.ok("批改成功");
        } else {
            return ResponseEntity.badRequest().body("批改失败");
        }
    }

    /**
     * 获取教师统计信息
     */
    @GetMapping("/statistics/{teacherId}")
    public ResponseEntity<?> getTeacherStatistics(@PathVariable Integer teacherId) {
        // 检查权限
        if (!permissionService.hasRole(teacherId, "TEACHER")) {
            return ResponseEntity.status(403).body("权限不足");
        }

        Map<String, Object> statistics = new HashMap<>();
        
        // 获取作业统计
        List<Assignment> assignments = assignmentService.getAssignmentsByTeacherId(teacherId);
        statistics.put("totalAssignments", assignments.size());
        
        // 获取已发布的作业数量
        long publishedAssignments = assignments.stream()
                .filter(a -> "published".equals(a.getStatus()))
                .count();
        statistics.put("publishedAssignments", publishedAssignments);
        
        // 获取总提交数量
        long totalSubmissions = assignments.stream()
                .mapToLong(a -> assignmentService.getSubmissionsByAssignmentId(a.getAssignmentId()).size())
                .sum();
        statistics.put("totalSubmissions", totalSubmissions);

        return ResponseEntity.ok(statistics);
    }
} 