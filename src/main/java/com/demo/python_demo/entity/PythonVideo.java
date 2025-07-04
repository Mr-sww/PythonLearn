package com.demo.python_demo.entity;

public class PythonVideo {
    private Integer id;
    private String title;
    private String url;
    private String imageUrl;
    private String playCount;
    private String publishDate;
    private String category;
    private String tags;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getPlayCount() { return playCount; }
    public void setPlayCount(String playCount) { this.playCount = playCount; }
    public String getPublishDate() { return publishDate; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
} 