package com.demo.python_demo.controller;

import com.demo.python_demo.entity.CourseComment;
import com.demo.python_demo.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 课程评论控制器
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CourseCommentController {

    @Autowired
    private CourseCommentService commentService;

    /**
     * 根据课程ID获取评论列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseComment>> getCommentsByCourseId(@PathVariable Integer courseId) {
        List<CourseComment> comments = commentService.getCommentsByCourseId(courseId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 根据ID获取评论
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CourseComment> getCommentById(@PathVariable Integer commentId) {
        Optional<CourseComment> comment = commentService.getCommentById(commentId);
        if (comment.isPresent()) {
            return ResponseEntity.ok(comment.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 创建评论
     */
    @PostMapping
    public ResponseEntity<CourseComment> createComment(@RequestBody CourseComment comment) {
        CourseComment createdComment = commentService.createComment(comment);
        if (createdComment != null) {
            return ResponseEntity.ok(createdComment);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 更新评论
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CourseComment> updateComment(@PathVariable Integer commentId, @RequestBody CourseComment comment) {
        Optional<CourseComment> updatedComment = commentService.updateComment(commentId, comment);
        if (updatedComment.isPresent()) {
            return ResponseEntity.ok(updatedComment.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        boolean deleted = commentService.deleteComment(commentId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 点赞评论
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Map<String, String>> likeComment(@PathVariable Integer commentId) {
        boolean success = commentService.likeComment(commentId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "点赞成功"));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 取消点赞评论
     */
    @PostMapping("/{commentId}/unlike")
    public ResponseEntity<Map<String, String>> unlikeComment(@PathVariable Integer commentId) {
        boolean success = commentService.unlikeComment(commentId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "取消点赞成功"));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 获取课程评论总数
     */
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Map<String, Integer>> getCommentCountByCourseId(@PathVariable Integer courseId) {
        int count = commentService.getCommentCountByCourseId(courseId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取课程平均评分
     */
    @GetMapping("/course/{courseId}/rating")
    public ResponseEntity<Map<String, Double>> getAverageRatingByCourseId(@PathVariable Integer courseId) {
        Double rating = commentService.getAverageRatingByCourseId(courseId);
        return ResponseEntity.ok(Map.of("rating", rating != null ? rating : 0.0));
    }

    /**
     * 根据用户ID获取评论列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseComment>> getCommentsByUserId(@PathVariable Integer userId) {
        List<CourseComment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }
} 