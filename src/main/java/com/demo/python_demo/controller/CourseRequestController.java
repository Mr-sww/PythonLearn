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

/**
 * 课程申请和审核控制器
 */
@RestController
@RequestMapping("/api/course-requests")
public class CourseRequestController {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * 提交课程申请
     */
    @PostMapping
    public ResponseEntity<?> submitCourseRequest(@RequestBody Course courseRequest) {
        try {
            // 设置申请状态
            courseRequest.setStatus("pending");
            courseRequest.setCreatedAt(LocalDateTime.now());
            courseRequest.setUpdatedAt(LocalDateTime.now());
            
            // 保存课程申请
            courseRepository.insert(courseRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "课程申请提交成功，等待审核");
            response.put("courseId", courseRequest.getArticleId());
            response.put("status", "pending");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("提交课程申请失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有课程申请
     */
    @GetMapping
    public ResponseEntity<?> getAllCourseRequests() {
        try {
            List<Course> requests = courseRepository.findAll();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取课程申请失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态获取课程申请
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getCourseRequestsByStatus(@PathVariable String status) {
        try {
            List<Course> requests = courseRepository.findByStatus(status);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取课程申请失败: " + e.getMessage());
        }
    }

    /**
     * 获取待审核的课程申请
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests() {
        try {
            List<Course> pendingRequests = courseRepository.findByStatus("pending");
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("获取待审核申请失败: " + e.getMessage());
        }
    }

    /**
     * 审核课程申请
     */
    @PostMapping("/{courseId}/review")
    public ResponseEntity<?> reviewCourseRequest(
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
                return ResponseEntity.badRequest().body("只能审核待审核状态的课程申请");
            }

            // 拒绝时必须填写理由
            if ("reject".equals(action) && (comment == null || comment.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("拒绝课程申请时必须填写拒绝理由");
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
            response.put("message", "课程申请审核完成");
            response.put("courseId", courseId);
            response.put("status", course.getStatus());
            response.put("comment", comment);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("审核课程申请失败: " + e.getMessage());
        }
    }

    /**
     * 批量审核课程申请
     */
    @PostMapping("/batch-review")
    public ResponseEntity<?> batchReviewCourseRequests(@RequestBody Map<String, Object> batchData) {
        try {
            List<Integer> courseIds = (List<Integer>) batchData.get("courseIds");
            String action = (String) batchData.get("action");
            String comment = (String) batchData.get("comment");

            if (courseIds == null || courseIds.isEmpty()) {
                return ResponseEntity.badRequest().body("请选择要审核的课程申请");
            }

            if (!"approve".equals(action) && !"reject".equals(action)) {
                return ResponseEntity.badRequest().body("无效的审核操作");
            }

            int successCount = 0;
            for (Integer courseId : courseIds) {
                try {
                    Course course = courseRepository.findById(courseId);
                    if (course != null && "pending".equals(course.getStatus())) {
                        course.setStatus(action.equals("approve") ? "approved" : "rejected");
                        course.setReviewComment(comment);
                        course.setReviewedAt(LocalDateTime.now());
                        course.setReviewedBy(1);
                        course.setUpdatedAt(LocalDateTime.now());
                        
                        courseRepository.update(course);
                        successCount++;
                    }
                } catch (Exception e) {
                    System.err.println("批量审核课程 " + courseId + " 失败: " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "批量审核完成，成功处理 " + successCount + " 个申请");
            response.put("totalRequested", courseIds.size());
            response.put("successCount", successCount);
            response.put("action", action);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("批量审核失败: " + e.getMessage());
        }
    }
}
