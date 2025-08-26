package com.demo.python_demo.controller;

import com.demo.python_demo.entity.Course;
import com.demo.python_demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class AdminController {
    
    @Autowired
    private CourseRepository courseRepository;

    /**
     * 获取所有课程（管理员视图）
     */
    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            // 添加调试信息
            System.out.println("Total courses found: " + courses.size());
            for (Course course : courses) {
                System.out.println("Course: " + course.getTitle() + ", Status: " + course.getStatus());
            }
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取课程列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程统计信息
     */
    @GetMapping("/courses/stats")
    public ResponseEntity<?> getCourseStats() {
        try {
            List<Course> courses = courseRepository.findAll();
            
            int total = courses.size();
            int pending = (int) courses.stream().filter(c -> "pending".equals(c.getStatus())).count();
            int approved = (int) courses.stream().filter(c -> "approved".equals(c.getStatus())).count();
            int rejected = (int) courses.stream().filter(c -> "rejected".equals(c.getStatus())).count();
            int active = (int) courses.stream().filter(c -> "active".equals(c.getStatus())).count();
            int draft = (int) courses.stream().filter(c -> "draft".equals(c.getStatus())).count();

            Map<String, Object> stats = new HashMap<>();
            stats.put("total", total);
            stats.put("pending", pending);
            stats.put("approved", approved);
            stats.put("rejected", rejected);
            stats.put("active", active);
            stats.put("draft", draft);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 审核课程（通过/拒绝）
     */
    @PostMapping("/courses/{courseId}/review")
    public ResponseEntity<?> reviewCourse(
            @PathVariable Integer courseId,
            @RequestBody Map<String, Object> reviewData) {
        
        try {
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }

            String action = (String) reviewData.get("action");
            String comment = (String) reviewData.get("comment");

            if (!"pending".equals(course.getStatus())) {
                return ResponseEntity.badRequest().body("只能审核待审核状态的课程");
            }

            // 拒绝时必须填写理由
            if ("reject".equals(action) && (comment == null || comment.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("拒绝课程时必须填写拒绝理由");
            }

            // 更新课程状态
            if ("approve".equals(action)) {
                course.setStatus("approved");
            } else if ("reject".equals(action)) {
                course.setStatus("rejected");
            } else {
                return ResponseEntity.badRequest().body("无效的审核操作");
            }

            course.setReviewComment(comment);
            course.setReviewedAt(LocalDateTime.now());
            course.setReviewedBy(1); // 测试用，固定审核员ID为1
            course.setUpdatedAt(LocalDateTime.now());

            courseRepository.update(course);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "审核操作成功");
            response.put("courseId", courseId);
            response.put("status", course.getStatus());
            response.put("comment", comment);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("审核操作失败: " + e.getMessage());
        }
    }

    /**
     * 批量审核课程
     */
    @PostMapping("/courses/batch-review")
    public ResponseEntity<?> batchReviewCourses(
            @RequestBody Map<String, Object> batchData) {
        
        try {
            @SuppressWarnings("unchecked")
            List<Integer> courseIds = (List<Integer>) batchData.get("courseIds");
            String action = (String) batchData.get("action");
            String comment = (String) batchData.get("comment");

            if (courseIds == null || courseIds.isEmpty()) {
                return ResponseEntity.badRequest().body("请选择要审核的课程");
            }

            // 拒绝时必须填写理由
            if ("reject".equals(action) && (comment == null || comment.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("拒绝课程时必须填写拒绝理由");
            }

            int successCount = 0;
            int failCount = 0;

            for (Integer courseId : courseIds) {
                try {
                    Course course = courseRepository.findById(courseId);
                    if (course != null && "pending".equals(course.getStatus())) {
                        if ("approve".equals(action)) {
                            course.setStatus("approved");
                        } else if ("reject".equals(action)) {
                            course.setStatus("rejected");
                        }
                        
                        course.setReviewComment(comment);
                        course.setReviewedAt(LocalDateTime.now());
                        course.setReviewedBy(1);
                        course.setUpdatedAt(LocalDateTime.now());
                        
                        courseRepository.update(course);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "批量审核完成");
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("totalCount", courseIds.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("批量审核失败: " + e.getMessage());
        }
    }

    /**
     * 获取待审核课程列表（课程审核功能）
     */
    @GetMapping("/courses/pending")
    public ResponseEntity<?> getPendingCourses() {
        try {
            List<Course> pendingCourses = courseRepository.findByStatus("pending");
            // 添加调试信息
            System.out.println("Pending courses found: " + pendingCourses.size());
            for (Course course : pendingCourses) {
                System.out.println("Pending Course: " + course.getTitle() + ", Status: " + course.getStatus());
            }
            return ResponseEntity.ok(pendingCourses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取待审核课程失败: " + e.getMessage());
        }
    }

    /**
     * 获取已通过课程列表（课程管理功能）
     */
    @GetMapping("/courses/approved")
    public ResponseEntity<?> getApprovedCourses() {
        try {
            List<Course> approvedCourses = courseRepository.findByStatus("approved");
            return ResponseEntity.ok(approvedCourses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取已通过课程失败: " + e.getMessage());
        }
    }

    /**
     * 获取已拒绝课程列表
     */
    @GetMapping("/courses/rejected")
    public ResponseEntity<?> getRejectedCourses() {
        try {
            List<Course> rejectedCourses = courseRepository.findByStatus("rejected");
            return ResponseEntity.ok(rejectedCourses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取已拒绝课程失败: " + e.getMessage());
        }
    }

    /**
     * 获取审核历史
     */
    @GetMapping("/courses/review-history")
    public ResponseEntity<?> getReviewHistory() {
        try {
            List<Course> reviewedCourses = courseRepository.findReviewedCourses();
            return ResponseEntity.ok(reviewedCourses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取审核历史失败: " + e.getMessage());
        }
    }

    /**
     * 更新课程信息（管理员编辑）
     */
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody Course courseData) {
        
        try {
            Course existingCourse = courseRepository.findById(courseId);
            if (existingCourse == null) {
                return ResponseEntity.notFound().build();
            }

            // 更新课程信息
            existingCourse.setTitle(courseData.getTitle());
            existingCourse.setContent(courseData.getContent());
            existingCourse.setCategory(courseData.getCategory());
            existingCourse.setDifficulty(courseData.getDifficulty());
            existingCourse.setDuration(courseData.getDuration());
            existingCourse.setLessons(courseData.getLessons());
            existingCourse.setTags(courseData.getTags());
            existingCourse.setCoverImage(courseData.getCoverImage());
            existingCourse.setUpdatedAt(LocalDateTime.now());

            courseRepository.update(existingCourse);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "课程更新成功");
            response.put("course", existingCourse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("课程更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除课程（管理员操作）
     */
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer courseId) {
        try {
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }

            courseRepository.deleteById(courseId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "课程删除成功");
            response.put("courseId", courseId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("课程删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除课程
     */
    @DeleteMapping("/courses/batch-delete")
    public ResponseEntity<?> batchDeleteCourses(@RequestBody Map<String, Object> batchData) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> courseIds = (List<Integer>) batchData.get("courseIds");

            if (courseIds == null || courseIds.isEmpty()) {
                return ResponseEntity.badRequest().body("请选择要删除的课程");
            }

            int successCount = 0;
            int failCount = 0;

            for (Integer courseId : courseIds) {
                try {
                    Course course = courseRepository.findById(courseId);
                    if (course != null) {
                        courseRepository.deleteById(courseId);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "批量删除完成");
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("totalCount", courseIds.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 搜索课程
     */
    @GetMapping("/courses/search")
    public ResponseEntity<?> searchCourses(@RequestParam String keyword) {
        try {
            List<Course> courses = courseRepository.searchByKeyword(keyword);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("搜索课程失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态搜索课程
     */
    @GetMapping("/courses/search/status")
    public ResponseEntity<?> searchCoursesByStatus(@RequestParam String status) {
        try {
            List<Course> courses = courseRepository.findByStatus(status);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("根据状态搜索课程失败: " + e.getMessage());
        }
    }

    /**
     * 根据分类搜索课程
     */
    @GetMapping("/courses/search/category")
    public ResponseEntity<?> searchCoursesByCategory(@RequestParam String category) {
        try {
            List<Course> courses = courseRepository.findByCategory(category);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("根据分类搜索课程失败: " + e.getMessage());
        }
    }
} 