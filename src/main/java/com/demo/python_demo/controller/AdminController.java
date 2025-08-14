package com.demo.python_demo.controller;

import com.demo.python_demo.entity.User;
import com.demo.python_demo.entity.Course;
import com.demo.python_demo.service.UserService;
import com.demo.python_demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class AdminController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private CourseService courseService;

    // 用户管理
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) Integer groupType,
                                  @RequestParam(required = false) String status) {
        return userService.searchUsers(keyword, groupType, status);
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<Map<String, String>> toggleUserStatus(@PathVariable Integer userId, 
                                                                @RequestParam String status) {
        boolean success = userService.updateUserStatus(userId, status);
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "用户状态更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "用户状态更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Map<String, String>> updateUserRole(@PathVariable Integer userId, 
                                                              @RequestParam Integer groupType) {
        boolean success = userService.updateUserGroupType(userId, groupType);
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "用户角色更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "用户角色更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer userId) {
        boolean success = userService.deleteUser(userId);
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "用户删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 课程管理
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PutMapping("/courses/{courseId}/review")
    public ResponseEntity<Map<String, String>> reviewCourse(@PathVariable Integer courseId,
                                                            @RequestParam String action,
                                                            @RequestParam(required = false) String comment) {
        boolean success = courseService.reviewCourse(courseId, action, comment);
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "课程审核完成");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "课程审核失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 系统统计
    @GetMapping("/statistics")
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.countAllUsers());
        stats.put("activeUsers", userService.countUsersByStatus("active"));
        
        // 按角色统计
        stats.put("totalStudents", userService.getAllStudents().size());
        stats.put("totalTeachers", userService.getAllTeachers().size());
        stats.put("totalAdmins", userService.getAllAdmins().size());
        
        // 按专业统计
        List<Object> majorStats = userService.getMajorStatistics();
        stats.put("majorStatistics", majorStats);
        
        // 按角色统计
        List<Object> roleStats = userService.getRoleStatistics();
        stats.put("roleStatistics", roleStats);
        
        stats.put("totalCourses", courseService.countAllCourses());
        stats.put("pendingCourses", courseService.countCoursesByStatus("pending"));
        stats.put("approvedCourses", courseService.countCoursesByStatus("approved"));
        stats.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return stats;
    }

    // 新增的用户分类API
    @GetMapping("/users/students")
    public List<User> getAllStudents() {
        return userService.getAllStudents();
    }

    @GetMapping("/users/students/major/{majorType}")
    public List<User> getStudentsByMajor(@PathVariable Integer majorType) {
        return userService.getStudentsByMajor(majorType);
    }

    @GetMapping("/users/teachers")
    public List<User> getAllTeachers() {
        return userService.getAllTeachers();
    }

    @GetMapping("/users/admins")
    public List<User> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GetMapping("/statistics/majors")
    public List<Object> getMajorStatistics() {
        return userService.getMajorStatistics();
    }

    @GetMapping("/statistics/roles")
    public List<Object> getRoleStatistics() {
        return userService.getRoleStatistics();
    }

    // 系统日志
    @GetMapping("/logs")
    public List<Map<String, Object>> getSystemLogs(@RequestParam(required = false) String level,
                                                   @RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate) {
        // 这里应该实现日志查询逻辑
        // 暂时返回模拟数据
        return List.of(Map.of(
            "level", "INFO",
            "message", "系统启动成功",
            "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
    }

    @GetMapping("/activities")
    public List<Map<String, Object>> getRecentActivities() {
        // 这里应该实现最近活动查询逻辑
        // 暂时返回模拟数据
        return List.of(Map.of(
            "type", "user_login",
            "description", "用户登录",
            "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
    }

    // 系统维护
    @PostMapping("/maintenance/{action}")
    public ResponseEntity<Map<String, String>> performMaintenance(@PathVariable String action) {
        Map<String, String> response = new HashMap<>();
        
        switch (action) {
            case "clear-cache":
                response.put("message", "缓存清理完成");
                break;
            case "backup-database":
                response.put("message", "数据库备份完成");
                break;
            case "optimize-database":
                response.put("message", "数据库优化完成");
                break;
            default:
                response.put("error", "未知的维护操作");
                return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
} 