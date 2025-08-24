package com.demo.python_demo.entity;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 知识点学习记录实体类
 */
public class KnowledgeStudyRecord {
    private Integer id;
    private Integer userId;
    private Integer knowledgeId;
    private String knowledgeTitle;
    private String contentType;
    private Integer studyTime;
    private BigDecimal progress;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime lastStudyTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public KnowledgeStudyRecord() {}

    public KnowledgeStudyRecord(Integer userId, Integer knowledgeId, String knowledgeTitle, String contentType) {
        this.userId = userId;
        this.knowledgeId = knowledgeId;
        this.knowledgeTitle = knowledgeTitle;
        this.contentType = contentType;
        this.status = "started";
        this.progress = new BigDecimal("0.00");
        this.studyTime = 0;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Integer knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getKnowledgeTitle() {
        return knowledgeTitle;
    }

    public void setKnowledgeTitle(String knowledgeTitle) {
        this.knowledgeTitle = knowledgeTitle;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return "KnowledgeStudyRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", knowledgeId=" + knowledgeId +
                ", knowledgeTitle='" + knowledgeTitle + '\'' +
                ", contentType='" + contentType + '\'' +
                ", studyTime=" + studyTime +
                ", progress=" + progress +
                ", status='" + status + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastStudyTime=" + lastStudyTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

