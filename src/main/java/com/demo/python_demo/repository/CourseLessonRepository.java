package com.demo.python_demo.repository;

import com.demo.python_demo.entity.CourseLesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CourseLessonRepository {
    @Select("SELECT LessonID as lessonId, Title as title, Content as content FROM course_lesson WHERE ChapterID = #{chapterId}")
    List<CourseLesson> findLessonsByChapterId(Integer chapterId);
}
