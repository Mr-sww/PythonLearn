package com.demo.python_demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * 作业实体类
 */
public class Assignment {
    @JsonProperty("assignmentId")
    private Integer assignmentId;
    
    @JsonProperty("courseId")
    private Integer courseId;
    
    @JsonProperty("teacherId")
    private Integer teacherId;
    
    private String title;
    private String description;
    private String content;
    private Date dueDate;
    private Integer maxScore;
    private String status;
    private Date createTime;
    private Date updateTime;
    
    // 非数据库字段，用于前端显示
    private String courseTitle;
    private String teacherName;
    private Integer submissionCount;

    // 构造函数
    public Assignment() {}

    public Assignment(Integer assignmentId, Integer courseId, Integer teacherId, String title, String content) {
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.title = title;
        this.content = content;
    }

    // Getter和Setter方法
    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(Integer submissionCount) {
        this.submissionCount = submissionCount;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId=" + assignmentId +
                ", courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", dueDate=" + dueDate +
                ", maxScore=" + maxScore +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 