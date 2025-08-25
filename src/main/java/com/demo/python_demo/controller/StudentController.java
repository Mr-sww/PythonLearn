package com.demo.python_demo.controller;

import com.demo.python_demo.repository.ClassMemberRepository;
import com.demo.python_demo.repository.UserCourseRepository;
import com.demo.python_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class StudentController {

    @Autowired
    private ClassMemberRepository classMemberRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;

    private boolean isStudent(User user) {
        return user != null && user.getGroupType() != null && user.getGroupType() >= 1 && user.getGroupType() <= 6;
    }

    @GetMapping("/classes")
    public ResponseEntity<?> myClasses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isStudent(user)) return ResponseEntity.status(403).body("仅学生可访问");
        return ResponseEntity.ok(classMemberRepository.findJoinedClasses(user.getUserId()));
    }

    @PostMapping("/classes/join")
    public ResponseEntity<?> joinClass(@RequestBody Map<String, Integer> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isStudent(user)) return ResponseEntity.status(403).body("仅学生可加入");
        Integer classId = body.get("classId");
        if (classId == null) return ResponseEntity.status(400).body("classId 必填");
        // 直接插入 class_member（防重复需要唯一键约束，若已存在由数据库报错）
        // 使用简单 SQL 避免创建实体：
        // INSERT IGNORE 兼容：若你的 MySQL 不允许，可用 try-catch 忽略重复
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/pythonlearn", "root", "")) {
            try (java.sql.PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO class_member (ClassID, UserID) VALUES (?, ?)")) {
                ps.setInt(1, classId);
                ps.setInt(2, user.getUserId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("加入失败");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses")
    public ResponseEntity<?> myCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isStudent(user)) return ResponseEntity.status(403).body("仅学生可访问");
        return ResponseEntity.ok(userCourseRepository.findUserCourses(user.getUserId()));
    }

    @PostMapping("/courses/join")
    public ResponseEntity<?> joinCourse(@RequestBody Map<String, Integer> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isStudent(user)) return ResponseEntity.status(403).body("仅学生可加入课程");
        Integer courseId = body.get("courseId");
        if (courseId == null) return ResponseEntity.status(400).body("courseId 必填");
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/pythonlearn", "root", "")) {
            try (java.sql.PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO user_course (UserID, CourseID) VALUES (?, ?)")) {
                ps.setInt(1, user.getUserId());
                ps.setInt(2, courseId);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("加入失败");
        }
        return ResponseEntity.ok().build();
    }
}


