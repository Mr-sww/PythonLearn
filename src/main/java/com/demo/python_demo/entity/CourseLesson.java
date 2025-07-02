package com.demo.python_demo.entity;

public class CourseLesson {
    private Integer lessonId;
    private Integer chapterId;
    private String title;
    private String content;

    public Integer getLessonId() {
        return lessonId;
    }
    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getChapterId() {
        return chapterId;
    }
    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
