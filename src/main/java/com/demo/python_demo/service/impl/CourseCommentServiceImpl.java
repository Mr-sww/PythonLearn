package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.CourseComment;
import com.demo.python_demo.repository.CourseCommentRepository;
import com.demo.python_demo.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseCommentServiceImpl implements CourseCommentService {

    @Autowired
    private CourseCommentRepository commentRepository;

    @Override
    public List<CourseComment> getCommentsByCourseId(Integer courseId) {
        return commentRepository.findByCourseId(courseId);
    }

    @Override
    public Optional<CourseComment> getCommentById(Integer commentId) {
        return Optional.ofNullable(commentRepository.findById(commentId));
    }

    @Override
    public CourseComment createComment(CourseComment comment) {
        commentRepository.insert(comment);
        return comment;
    }

    @Override
    public Optional<CourseComment> updateComment(Integer commentId, CourseComment comment) {
        comment.setCommentId(commentId);
        int updated = commentRepository.update(comment);
        if (updated > 0) {
            return Optional.of(commentRepository.findById(commentId));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteComment(Integer commentId) {
        return commentRepository.deleteById(commentId) > 0;
    }

    @Override
    public boolean likeComment(Integer commentId) {
        return commentRepository.incrementLikes(commentId) > 0;
    }

    @Override
    public boolean unlikeComment(Integer commentId) {
        // SQL Server 没有GREATEST，需用CASE WHEN实现
        // 这里假设已在Repository中处理
        return commentRepository.decrementLikes(commentId) > 0;
    }

    @Override
    public int getCommentCountByCourseId(Integer courseId) {
        return commentRepository.countByCourseId(courseId);
    }

    @Override
    public Double getAverageRatingByCourseId(Integer courseId) {
        return commentRepository.getAverageRating(courseId);
    }

    @Override
    public List<CourseComment> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }
} 