package com.demo.python_demo.controller;

import com.demo.python_demo.entity.Course;
import com.demo.python_demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 课程控制器
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取所有课程
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    /**
     * 根据ID获取课程
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Integer id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            // 增加浏览量
            courseService.incrementViews(id);
            return ResponseEntity.ok(course.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 根据分类获取课程
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable String category) {
        List<Course> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }

    /**
     * 根据作者获取课程
     */
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Course>> getCoursesByAuthor(@PathVariable String author) {
        List<Course> courses = courseService.getCoursesByAuthor(author);
        return ResponseEntity.ok(courses);
    }

    /**
     * 搜索课程
     */
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String keyword) {
        List<Course> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }

    /**
     * 获取热门课程
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Course>> getPopularCourses(@RequestParam(defaultValue = "10") Integer limit) {
        List<Course> courses = courseService.getPopularCourses(limit);
        return ResponseEntity.ok(courses);
    }

    /**
     * 获取最新课程
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Course>> getLatestCourses(@RequestParam(defaultValue = "10") Integer limit) {
        List<Course> courses = courseService.getLatestCourses(limit);
        return ResponseEntity.ok(courses);
    }

    /**
     * 创建课程
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        if (createdCourse != null) {
            return ResponseEntity.ok(createdCourse);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 更新课程
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Integer id, @RequestBody Course course) {
        Optional<Course> updatedCourse = courseService.updateCourse(id, course);
        if (updatedCourse.isPresent()) {
            return ResponseEntity.ok(updatedCourse.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        boolean deleted = courseService.deleteCourse(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 获取课程总数
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCourseCount() {
        int count = courseService.getCourseCount();
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 分页获取课程
     */
    @GetMapping("/page")
    public ResponseEntity<List<Course>> getCoursesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Course> courses = courseService.getCoursesWithPagination(page, size);
        return ResponseEntity.ok(courses);
    }

    /**
     * 获取分类统计
     */
    @GetMapping("/statistics/category")
    public ResponseEntity<List<Object>> getCategoryStatistics() {
        List<Object> statistics = courseService.getCategoryStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取推荐课程
     */
    @GetMapping("/recommended")
    public ResponseEntity<List<Course>> getRecommendedCourses(
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "6") Integer limit) {
        List<Course> courses = courseService.getRecommendedCourses(userId, limit);
        return ResponseEntity.ok(courses);
    }

    /**
     * 增加课程浏览量
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Map<String, String>> incrementViews(@PathVariable Integer id) {
        boolean success = courseService.incrementViews(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "浏览量增加成功"));
        }
        return ResponseEntity.notFound().build();
    }
} 