package com.demo.python_demo.controller;

import com.demo.python_demo.entity.Course;
import com.demo.python_demo.entity.CourseRequest;
import com.demo.python_demo.repository.CourseRepository;
import com.demo.python_demo.repository.CourseRequestRepository;
import com.demo.python_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class InstructorController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRequestRepository courseRequestRepository;

    private boolean isLoggedIn(User user) {
        return user != null && user.getUserId() != null;
    }
    
    private boolean isTeacher(User user) {
        return isLoggedIn(user) && user.getGroupType() != null && user.getGroupType() == 7;
    }
    
    private boolean isAdmin(User user) {
        return isLoggedIn(user) && user.getGroupType() != null && user.getGroupType() == 8;
    }

    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (!isTeacher(user)) {
            return ResponseEntity.status(403).body("仅教师可创建课程");
        }
        
        Course course = new Course();
        course.setTitle((String) body.getOrDefault("title", "未命名课程"));
        course.setContent((String) body.getOrDefault("content", ""));
        course.setAuthor(user.getNickname() != null ? user.getNickname() : user.getAccount());
        course.setCategory((String) body.getOrDefault("category", "编程开发"));
        course.setTags((String) body.getOrDefault("tags", ""));
        course.setDifficulty((String) body.getOrDefault("difficulty", "beginner"));
        course.setDuration((String) body.getOrDefault("duration", "10小时"));
        course.setLessons((Integer) body.getOrDefault("lessons", 10));
        course.setCoverImage((String) body.getOrDefault("coverImage", ""));
        course.setStatus("active");
        course.setViews(0);
        course.setRating(0.0);
        course.setPublicationDate(LocalDateTime.now());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        
        courseRepository.insert(course);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/courses")
    public ResponseEntity<?> myCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (!isLoggedIn(user)) {
            return ResponseEntity.status(401).body("请先登录");
        }
        
        // 如果是教师，显示自己创建的课程
        if (isTeacher(user)) {
            List<Course> list = courseRepository.findByAuthor(user.getNickname() != null ? user.getNickname() : user.getAccount());
            return ResponseEntity.ok(list);
        }
        
        // 如果是普通用户，显示空列表
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/test-auth")
    public ResponseEntity<?> testAuth(HttpSession session) {
        System.out.println("DEBUG: Testing auth - Session ID: " + session.getId());
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("用户未登录");
        }
        return ResponseEntity.ok(Map.of(
            "userId", user.getUserId(),
            "groupType", user.getGroupType(),
            "account", user.getAccount(),
            "sessionId", session.getId()
        ));
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer courseId, @RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isTeacher(user)) return ResponseEntity.status(403).body("仅教师可更新课程");
        
        Course existingCourse = courseRepository.findById(courseId);
        if (existingCourse == null) return ResponseEntity.status(404).body("课程不存在");
        if (!existingCourse.getAuthor().equals(user.getNickname() != null ? user.getNickname() : user.getAccount())) {
            return ResponseEntity.status(403).body("只能更新自己的课程");
        }
        
        Course course = new Course();
        course.setArticleId(courseId);
        course.setTitle((String) body.getOrDefault("title", existingCourse.getTitle()));
        course.setContent((String) body.getOrDefault("content", existingCourse.getContent()));
        course.setCategory((String) body.getOrDefault("category", existingCourse.getCategory()));
        course.setTags((String) body.getOrDefault("tags", existingCourse.getTags()));
        course.setDifficulty((String) body.getOrDefault("difficulty", existingCourse.getDifficulty()));
        course.setDuration((String) body.getOrDefault("duration", existingCourse.getDuration()));
        course.setLessons((Integer) body.getOrDefault("lessons", existingCourse.getLessons()));
        course.setCoverImage((String) body.getOrDefault("coverImage", existingCourse.getCoverImage()));
        course.setUpdatedAt(LocalDateTime.now());
        
        courseRepository.update(course);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer courseId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isTeacher(user)) return ResponseEntity.status(403).body("仅教师可删除课程");
        
        Course existingCourse = courseRepository.findById(courseId);
        if (existingCourse == null) return ResponseEntity.status(404).body("课程不存在");
        if (!existingCourse.getAuthor().equals(user.getNickname() != null ? user.getNickname() : user.getAccount())) {
            return ResponseEntity.status(403).body("只能删除自己的课程");
        }
        
        courseRepository.deleteById(courseId);
        return ResponseEntity.ok("课程删除成功");
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


