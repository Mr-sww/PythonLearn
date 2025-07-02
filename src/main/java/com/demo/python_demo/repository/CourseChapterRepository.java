package com.demo.python_demo.repository;

import com.demo.python_demo.entity.CourseChapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CourseChapterRepository {
    @Select("SELECT ChapterID as chapterId, Title as title FROM course_chapter")
    List<CourseChapter> findAllChapters();
}
