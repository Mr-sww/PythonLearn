package com.demo.python_demo.repository;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserCourseRepository {

    @Select("SELECT uc.CourseID as courseId, c.Title as title, c.CoverImage as coverImage, uc.CreatedAt as joinedAt " +
            "FROM user_course uc JOIN course c ON uc.CourseID = c.ArticleID WHERE uc.UserID = #{userId} ORDER BY uc.CreatedAt DESC")
    List<Map<String, Object>> findUserCourses(Integer userId);
}


