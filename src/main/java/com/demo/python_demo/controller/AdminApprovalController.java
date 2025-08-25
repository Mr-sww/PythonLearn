package com.demo.python_demo.controller;

import com.demo.python_demo.entity.CourseRequest;
import com.demo.python_demo.repository.CourseRequestRepository;
import com.demo.python_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class AdminApprovalController {

    @Autowired
    private CourseRequestRepository courseRequestRepository;

    private boolean isAdmin(User user) {
        return user != null && user.getGroupType() != null && user.getGroupType() == 8;
    }

    @GetMapping("/course-requests")
    public ResponseEntity<?> listCourseRequests(@RequestParam(defaultValue = "pending") String status, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) return ResponseEntity.status(403).body("仅管理员可访问");
        List<CourseRequest> list = courseRequestRepository.findByStatus(status);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/course-requests/{id}")
    public ResponseEntity<?> reviewCourseRequest(@PathVariable Integer id, @RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) return ResponseEntity.status(403).body("仅管理员可审核");
        boolean approve = Boolean.TRUE.equals(body.get("approve"));
        String note = body.get("note") == null ? null : body.get("note").toString();
        String status = approve ? "approved" : "rejected";
        int updated = courseRequestRepository.updateStatus(id, status, note);
        return updated > 0 ? ResponseEntity.ok().build() : ResponseEntity.status(400).body("更新失败");
    }
}


