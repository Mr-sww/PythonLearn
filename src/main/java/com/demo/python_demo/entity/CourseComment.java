package com.demo.python_demo.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程评论实体类
 */
public class CourseComment {
    private Integer commentId;
    private Integer courseId;
    private Integer userId;
    private String content;
    private Integer rating;
    private Integer parentId;
    private Integer likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联字段
    private String userName;
    private String userAvatar;
    private List<CourseComment> replies;

    // 构造函数
    public CourseComment() {}

    public CourseComment(Integer courseId, Integer userId, String content, Integer rating) {
        this.courseId = courseId;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
    }

    // Getter和Setter方法
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public List<CourseComment> getReplies() {
        return replies;
    }

    public void setReplies(List<CourseComment> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "CourseComment{" +
                "commentId=" + commentId +
                ", courseId=" + courseId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", parentId=" + parentId +
                ", likes=" + likes +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 