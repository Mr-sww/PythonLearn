package com.demo.python_demo.controller;

import com.demo.python_demo.entity.CourseClass;
import com.demo.python_demo.entity.CourseRequest;
import com.demo.python_demo.repository.CourseClassRepository;
import com.demo.python_demo.repository.CourseRequestRepository;
import com.demo.python_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class InstructorController {

    @Autowired
    private CourseClassRepository courseClassRepository;
    @Autowired
    private CourseRequestRepository courseRequestRepository;

    private boolean isTeacher(User user) {
        return user != null && user.getGroupType() != null && user.getGroupType() == 7;
    }

    @PostMapping("/classes")
    public ResponseEntity<?> createClass(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isTeacher(user)) return ResponseEntity.status(403).body("仅教师可创建班级");
        CourseClass c = new CourseClass();
        c.setName(body.getOrDefault("name", "未命名班级"));
        c.setDescription(body.getOrDefault("description", ""));
        c.setTeacherId(user.getUserId());
        courseClassRepository.insert(c);
        return ResponseEntity.ok(c);
    }

    @GetMapping("/classes")
    public ResponseEntity<?> myClasses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isTeacher(user)) return ResponseEntity.status(403).body("仅教师可访问");
        List<CourseClass> list = courseClassRepository.findByTeacher(user.getUserId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/course-requests")
    public ResponseEntity<?> createCourseRequest(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isTeacher(user)) return ResponseEntity.status(403).body("仅教师可申请课程");
        CourseRequest r = new CourseRequest();
        r.setTeacherId(user.getUserId());
        r.setTitle(body.getOrDefault("title", "未命名课程"));
        r.setDescription(body.getOrDefault("description", ""));
        r.setCoverImage(body.get("coverImage"));
        courseRequestRepository.insert(r);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/course-requests")
    public ResponseEntity<?> myCourseRequests(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isTeacher(user)) return ResponseEntity.status(403).body("仅教师可访问");
        List<CourseRequest> list = courseRequestRepository.findByTeacher(user.getUserId());
        return ResponseEntity.ok(list);
    }
}


