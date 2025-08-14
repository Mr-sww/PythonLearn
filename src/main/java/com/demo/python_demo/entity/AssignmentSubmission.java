package com.demo.python_demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * 作业提交实体类
 */
public class AssignmentSubmission {
    @JsonProperty("submissionId")
    private Integer submissionId;
    
    @JsonProperty("assignmentId")
    private Integer assignmentId;
    
    @JsonProperty("studentId")
    private Integer studentId;
    
    private String content;
    private Double score;
    private String feedback;
    private Date submitTime;
    private Date gradeTime;
    private String status;
    
    // 非数据库字段，用于前端显示
    private String assignmentTitle;
    private String studentName;
    private String courseTitle;

    // 构造函数
    public AssignmentSubmission() {}

    public AssignmentSubmission(Integer assignmentId, Integer studentId, String content) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.content = content;
    }

    // Getter和Setter方法
    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getGradeTime() {
        return gradeTime;
    }

    public void setGradeTime(Date gradeTime) {
        this.gradeTime = gradeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @Override
    public String toString() {
        return "AssignmentSubmission{" +
                "submissionId=" + submissionId +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", feedback='" + feedback + '\'' +
                ", submitTime=" + submitTime +
                ", gradeTime=" + gradeTime +
                ", status='" + status + '\'' +
                '}';
    }
} 