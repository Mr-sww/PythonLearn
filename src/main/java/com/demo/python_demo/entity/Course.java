package com.demo.python_demo.entity;

import java.time.LocalDateTime;

/**
 * 课程实体类
 */
public class Course {
    private Integer articleId;
    private String title;
    private String url;
    private LocalDateTime publicationDate;
    private String content;
    private String author;
    private String category;
    private String tags;
    private Integer views;
    private Double price;
    private Double rating;
    private String duration;
    private Integer lessons;
    private String difficulty;
    private String coverImage;
    private String authorAvatar;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 审核相关字段
    private String reviewComment;
    private LocalDateTime reviewedAt;
    private Integer reviewedBy;

    // 构造函数
    public Course() {}

    public Course(Integer articleId, String title, String url, LocalDateTime publicationDate, 
                  String content, String author, String category, String tags, Integer views) {
        this.articleId = articleId;
        this.title = title;
        this.url = url;
        this.publicationDate = publicationDate;
        this.content = content;
        this.author = author;
        this.category = category;
        this.tags = tags;
        this.views = views;
    }

    // Getter和Setter方法
    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getLessons() {
        return lessons;
    }

    public void setLessons(Integer lessons) {
        this.lessons = lessons;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    // 审核相关字段的getter和setter
    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    @Override
    public String toString() {
        return "Course{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", publicationDate=" + publicationDate +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", tags='" + tags + '\'' +
                ", views=" + views +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", reviewComment='" + reviewComment + '\'' +
                ", reviewedAt=" + reviewedAt +
                ", reviewedBy=" + reviewedBy +
                '}';
    }
} 