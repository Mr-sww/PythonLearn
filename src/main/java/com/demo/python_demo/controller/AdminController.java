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
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.demo.python_demo.entity.User;
import com.demo.python_demo.service.UserService;
import com.demo.python_demo.config.CourseConfig;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

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
     * 获取待审核课程（专门用于课程审核页面）
     */
    @GetMapping("/courses/pending")
    public ResponseEntity<?> getPendingCourses() {
        try {
            List<Course> pendingCourses = courseRepository.findByStatus("pending");
            // 添加调试信息
            System.out.println("Pending courses found: " + pendingCourses.size());
            for (Course course : pendingCourses) {
                System.out.println("Pending Course: " + course.getTitle() + ", Author: " + course.getAuthor());
            }
            return ResponseEntity.ok(pendingCourses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取待审核课程失败: " + e.getMessage());
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
            // 参数验证
            if (courseId == null) {
                return ResponseEntity.badRequest().body("课程ID不能为空");
            }
            
            String action = (String) reviewData.get("action");
            String comment = (String) reviewData.get("comment");
            
            if (action == null || (!"approve".equals(action) && !"reject".equals(action))) {
                return ResponseEntity.badRequest().body("无效的审核操作，必须是 'approve' 或 'reject'");
            }

            Course course = courseRepository.findById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }

            // 状态验证
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
            }

            // 设置审核信息
            course.setReviewComment(comment != null ? comment.trim() : "");
            course.setReviewedAt(LocalDateTime.now());
            course.setReviewedBy(1); // TODO: 从session获取当前管理员ID
            course.setUpdatedAt(LocalDateTime.now());

            // 保存更新
            int updateResult = courseRepository.update(course);
            if (updateResult <= 0) {
                return ResponseEntity.internalServerError().body("更新课程状态失败");
            }

            // 记录审核日志
            System.out.println("课程审核完成 - 课程ID: " + courseId + 
                             ", 操作: " + action + 
                             ", 状态: " + course.getStatus() + 
                             ", 意见: " + comment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "审核操作成功");
            response.put("courseId", courseId);
            response.put("status", course.getStatus());
            response.put("comment", comment);
            response.put("reviewedAt", course.getReviewedAt());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
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

            // 参数验证
            if (courseIds == null || courseIds.isEmpty()) {
                return ResponseEntity.badRequest().body("请选择要审核的课程");
            }
            
            if (action == null || (!"approve".equals(action) && !"reject".equals(action))) {
                return ResponseEntity.badRequest().body("无效的审核操作");
            }

            // 拒绝时必须填写理由
            if ("reject".equals(action) && (comment == null || comment.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("拒绝课程时必须填写拒绝理由");
            }

            int successCount = 0;
            int failCount = 0;
            List<String> failedReasons = new ArrayList<>();

            for (Integer courseId : courseIds) {
                try {
                    Course course = courseRepository.findById(courseId);
                    if (course == null) {
                        failCount++;
                        failedReasons.add("课程ID " + courseId + " 不存在");
                        continue;
                    }
                    
                    if (!"pending".equals(course.getStatus())) {
                        failCount++;
                        failedReasons.add("课程 " + course.getTitle() + " 不是待审核状态");
                        continue;
                    }
                    
                    // 更新课程状态
                    if ("approve".equals(action)) {
                        course.setStatus("approved");
                    } else if ("reject".equals(action)) {
                        course.setStatus("rejected");
                    }
                    
                    course.setReviewComment(comment != null ? comment.trim() : "");
                    course.setReviewedAt(LocalDateTime.now());
                    course.setReviewedBy(1); // TODO: 从session获取当前管理员ID
                    course.setUpdatedAt(LocalDateTime.now());
                    
                    int updateResult = courseRepository.update(course);
                    if (updateResult > 0) {
                        successCount++;
                    } else {
                        failCount++;
                        failedReasons.add("课程 " + course.getTitle() + " 更新失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    failedReasons.add("课程ID " + courseId + " 处理异常: " + e.getMessage());
                }
            }

            // 记录批量审核日志
            System.out.println("批量审核完成 - 总数: " + courseIds.size() + 
                             ", 成功: " + successCount + 
                             ", 失败: " + failCount + 
                             ", 操作: " + action);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量审核完成");
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("totalCount", courseIds.size());
            response.put("action", action);
            if (!failedReasons.isEmpty()) {
                response.put("failedReasons", failedReasons);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("批量审核失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程详情（用于审核查看）
     */
    @GetMapping("/courses/{courseId}/detail")
    public ResponseEntity<?> getCourseDetail(@PathVariable Integer courseId) {
        try {
            if (courseId == null) {
                return ResponseEntity.badRequest().body("课程ID不能为空");
            }
            
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("course", course);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取课程详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程审核历史
     */
    @GetMapping("/courses/review-history")
    public ResponseEntity<?> getReviewHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Course> allCourses = courseRepository.findAll();
            
            // 过滤已审核的课程
            List<Course> reviewedCourses = allCourses.stream()
                .filter(course -> course.getReviewedAt() != null)
                .sorted((c1, c2) -> c2.getReviewedAt().compareTo(c1.getReviewedAt()))
                .collect(Collectors.toList());
            
            // 分页处理
            int total = reviewedCourses.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<Course> pagedCourses = reviewedCourses.subList(start, end);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("courses", pagedCourses);
            response.put("total", total);
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", (int) Math.ceil((double) total / size));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取审核历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取已审核课程列表（用于课程管理）
     */
    @GetMapping("/courses/managed")
    public ResponseEntity<?> getManagedCourses(
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Course> allCourses = courseRepository.findAll();
            
            // 根据状态筛选
            List<Course> filteredCourses;
            if ("approved".equals(status)) {
                filteredCourses = allCourses.stream()
                    .filter(course -> "approved".equals(course.getStatus()))
                    .collect(Collectors.toList());
            } else if ("rejected".equals(status)) {
                filteredCourses = allCourses.stream()
                    .filter(course -> "rejected".equals(course.getStatus()))
                    .collect(Collectors.toList());
            } else if ("banned".equals(status)) {
                filteredCourses = allCourses.stream()
                    .filter(course -> "banned".equals(course.getStatus()))
                    .collect(Collectors.toList());
            } else {
                // 默认返回所有已审核的课程
                filteredCourses = allCourses.stream()
                    .filter(course -> course.getReviewedAt() != null)
                    .collect(Collectors.toList());
            }
            
            // 按更新时间排序
            filteredCourses.sort((c1, c2) -> {
                LocalDateTime time1 = c2.getUpdatedAt() != null ? c2.getUpdatedAt() : c2.getCreatedAt();
                LocalDateTime time2 = c1.getUpdatedAt() != null ? c1.getUpdatedAt() : c1.getCreatedAt();
                if (time1 == null) time1 = LocalDateTime.MIN;
                if (time2 == null) time2 = LocalDateTime.MIN;
                return time1.compareTo(time2);
            });
            
            // 分页处理
            int total = filteredCourses.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<Course> pagedCourses = filteredCourses.subList(start, end);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("courses", pagedCourses);
            response.put("total", total);
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", (int) Math.ceil((double) total / size));
            response.put("status", status);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取课程列表失败: " + e.getMessage());
        }
    }

    /**
     * 封禁/解禁课程
     */
    @PostMapping("/courses/{courseId}/toggle-ban")
    public ResponseEntity<?> toggleCourseBan(@PathVariable Integer courseId) {
        try {
            if (courseId == null) {
                return ResponseEntity.badRequest().body("课程ID不能为空");
            }
            
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 只能封禁/解禁已审核的课程
            if (course.getReviewedAt() == null) {
                return ResponseEntity.badRequest().body("只能封禁/解禁已审核的课程");
            }
            
            String newStatus;
            String message;
            
            if ("banned".equals(course.getStatus())) {
                // 解禁：恢复到之前的审核状态
                newStatus = course.getReviewComment() != null && course.getReviewComment().contains("封禁") ? "rejected" : "approved";
                message = "课程已解禁";
            } else {
                // 封禁
                newStatus = "banned";
                message = "课程已封禁";
            }
            
            course.setStatus(newStatus);
            course.setUpdatedAt(LocalDateTime.now());
            
            int updateResult = courseRepository.update(course);
            if (updateResult <= 0) {
                return ResponseEntity.internalServerError().body("更新课程状态失败");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);
            response.put("courseId", courseId);
            response.put("status", newStatus);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("操作失败: " + e.getMessage());
        }
    }

    /**
     * 重新审核已拒绝的课程
     */
    @PostMapping("/courses/{courseId}/re-review")
    public ResponseEntity<?> reReviewCourse(
            @PathVariable Integer courseId,
            @RequestBody Map<String, Object> reviewData) {
        
        try {
            if (courseId == null) {
                return ResponseEntity.badRequest().body("课程ID不能为空");
            }
            
            String action = (String) reviewData.get("action");
            String comment = (String) reviewData.get("comment");
            
            if (action == null || (!"approve".equals(action) && !"reject".equals(action))) {
                return ResponseEntity.badRequest().body("无效的审核操作");
            }
            
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 只能重新审核已拒绝的课程
            if (!"rejected".equals(course.getStatus())) {
                return ResponseEntity.badRequest().body("只能重新审核已拒绝的课程");
            }
            
            // 拒绝时必须填写理由
            if ("reject".equals(action) && (comment == null || comment.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("拒绝课程时必须填写拒绝理由");
            }
            
            // 更新课程状态
            course.setStatus(action.equals("approve") ? "approved" : "rejected");
            course.setReviewComment(comment != null ? comment.trim() : "");
            course.setReviewedAt(LocalDateTime.now());
            course.setReviewedBy(1); // TODO: 从session获取当前管理员ID
            course.setUpdatedAt(LocalDateTime.now());
            
            int updateResult = courseRepository.update(course);
            if (updateResult <= 0) {
                return ResponseEntity.internalServerError().body("更新课程状态失败");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "重新审核完成");
            response.put("courseId", courseId);
            response.put("status", course.getStatus());
            response.put("comment", comment);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("重新审核失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程管理统计信息
     */
    @GetMapping("/courses/management-stats")
    public ResponseEntity<?> getManagementStats() {
        try {
            List<Course> allCourses = courseRepository.findAll();
            
            int total = allCourses.size();
            int pending = (int) allCourses.stream().filter(c -> "pending".equals(c.getStatus())).count();
            int approved = (int) allCourses.stream().filter(c -> "approved".equals(c.getStatus())).count();
            int rejected = (int) allCourses.stream().filter(c -> "rejected".equals(c.getStatus())).count();
            int banned = (int) allCourses.stream().filter(c -> "banned".equals(c.getStatus())).count();
            int managed = approved + rejected + banned; // 已管理的课程数量

            Map<String, Object> stats = new HashMap<>();
            stats.put("total", total);
            stats.put("pending", pending);
            stats.put("approved", approved);
            stats.put("rejected", rejected);
            stats.put("banned", banned);
            stats.put("managed", managed);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取统计信息失败: " + e.getMessage());
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
} 