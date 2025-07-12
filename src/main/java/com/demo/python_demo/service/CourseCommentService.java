package com.demo.python_demo.service;

import com.demo.python_demo.entity.CourseComment;

import java.util.List;
import java.util.Optional;

/**
 * 课程评论服务接口
 */
public interface CourseCommentService {

    /**
     * 根据课程ID获取评论列表
     */
    List<CourseComment> getCommentsByCourseId(Integer courseId);

    /**
     * 根据ID获取评论
     */
    Optional<CourseComment> getCommentById(Integer commentId);

    /**
     * 创建评论
     */
    CourseComment createComment(CourseComment comment);

    /**
     * 更新评论
     */
    Optional<CourseComment> updateComment(Integer commentId, CourseComment comment);

    /**
     * 删除评论
     */
    boolean deleteComment(Integer commentId);

    /**
     * 点赞评论
     */
    boolean likeComment(Integer commentId);

    /**
     * 取消点赞评论
     */
    boolean unlikeComment(Integer commentId);

    /**
     * 获取课程评论总数
     */
    int getCommentCountByCourseId(Integer courseId);

    /**
     * 获取课程平均评分
     */
    Double getAverageRatingByCourseId(Integer courseId);

    /**
     * 根据用户ID获取评论列表
     */
    List<CourseComment> getCommentsByUserId(Integer userId);
} 