package com.demo.python_demo.entity;

import java.util.Date;

/**
 * 用户收藏课程实体类
 */
public class UserFavorite {
    private Integer id;
    private Integer userId;
    private Integer courseId;
    private Date createTime;
    
    // 关联的课程信息
    private Course course;

    // Getter and Setter methods
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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
} 