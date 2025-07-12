package com.demo.python_demo.entity;

import java.time.LocalDateTime;

/**
 * 学习进度实体类
 */
public class LearningProgress {
    private Integer progressId;
    private Integer userId;
    private Integer courseId;
    private Integer chapterId;
    private Integer lessonId;
    private Double progress;
    private String status;
    private Integer timeSpent;
    private LocalDateTime lastStudyTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联字段
    private String courseTitle;
    private String chapterTitle;
    private String lessonTitle;

    // 构造函数
    public LearningProgress() {}

    public LearningProgress(Integer userId, Integer courseId) {
        this.userId = userId;
        this.courseId = courseId;
        this.progress = 0.0;
        this.status = "not_started";
        this.timeSpent = 0;
    }

    // Getter和Setter方法
    public Integer getProgressId() {
        return progressId;
    }

    public void setProgressId(Integer progressId) {
        this.progressId = progressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public LocalDateTime getLastStudyTime() {
        return lastStudyTime;
    }

    public void setLastStudyTime(LocalDateTime lastStudyTime) {
        this.lastStudyTime = lastStudyTime;
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

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    @Override
    public String toString() {
        return "LearningProgress{" +
                "progressId=" + progressId +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", chapterId=" + chapterId +
                ", lessonId=" + lessonId +
                ", progress=" + progress +
                ", status='" + status + '\'' +
                ", timeSpent=" + timeSpent +
                ", lastStudyTime=" + lastStudyTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 