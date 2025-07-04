package com.demo.python_demo.entity;

import java.util.Date;

public class KnowledgePoint {
    private String title;
    private String question;
    private String url;
    private String content;
    private String stage;
    private Date createTime;
    private Integer id;

    // getter/setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}
