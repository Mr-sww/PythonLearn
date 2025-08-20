package com.demo.python_demo.entity;

import java.util.Date;

/**
 * 知识点评论实体
 */
public class KnowledgeComment {
    private Integer id;
    private Integer knowledgeId;
    private Integer userId;
    private String nickname;
    private String avatar;
    private Integer parentId;       // 直接父评论ID（0为顶级）
    private Integer rootId;         // 根评论ID（0为顶级）
    private Integer replyToUserId;  // 被回复用户ID
    private String content;
    private Long likes;
    private Long replyCount;
    private Date createdAt;
    private Date updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getKnowledgeId() { return knowledgeId; }
    public void setKnowledgeId(Integer knowledgeId) { this.knowledgeId = knowledgeId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getLikes() { return likes; }
    public void setLikes(Long likes) { this.likes = likes; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
    public Integer getRootId() { return rootId; }
    public void setRootId(Integer rootId) { this.rootId = rootId; }
    public Integer getReplyToUserId() { return replyToUserId; }
    public void setReplyToUserId(Integer replyToUserId) { this.replyToUserId = replyToUserId; }
    public Long getReplyCount() { return replyCount; }
    public void setReplyCount(Long replyCount) { this.replyCount = replyCount; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}


