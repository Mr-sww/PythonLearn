package com.demo.python_demo.entity;

import java.time.LocalDateTime;

/**
 * 学习记录实体类
 */
public class StudyRecord {
    private Integer recordId;
    private Integer userId;
    private Integer courseId;
    private Integer lessonId;
    private Integer studyTime;
    private Double progress;
    private Boolean completed;
    private LocalDateTime studyDate;
    
    // 关联字段
    private String courseTitle;
    private String lessonTitle;
    private String courseImage;
    private String chapterTitle;

    // 构造函数
    public StudyRecord() {}

    public StudyRecord(Integer userId, Integer courseId, Integer lessonId) {
        this.userId = userId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.studyTime = 0;
        this.progress = 0.0;
        this.completed = false;
        this.studyDate = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(LocalDateTime studyDate) {
        this.studyDate = studyDate;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    @Override
    public String toString() {
        return "StudyRecord{" +
                "recordId=" + recordId +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", lessonId=" + lessonId +
                ", studyTime=" + studyTime +
                ", progress=" + progress +
                ", completed=" + completed +
                ", studyDate=" + studyDate +
                '}';
    }
}
