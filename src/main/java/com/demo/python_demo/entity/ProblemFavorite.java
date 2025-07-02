package com.demo.python_demo.entity;
import java.util.Date;

public class ProblemFavorite {
    private Integer favoriteId;
    private Integer userId;
    private String problemId;
    private Date createdAt;

    public Integer getFavoriteId() { return favoriteId; }
    public void setFavoriteId(Integer favoriteId) { this.favoriteId = favoriteId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getProblemId() { return problemId; }
    public void setProblemId(String problemId) { this.problemId = problemId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
} 