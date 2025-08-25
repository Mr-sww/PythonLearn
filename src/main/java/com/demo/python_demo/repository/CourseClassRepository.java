package com.demo.python_demo.repository;

import com.demo.python_demo.entity.CourseClass;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CourseClassRepository {

    @Insert("INSERT INTO class (Name, Description, TeacherID) VALUES (#{name}, #{description}, #{teacherId})")
    @Options(useGeneratedKeys = true, keyProperty = "classId")
    int insert(CourseClass c);

    @Select("SELECT ClassID as classId, Name as name, Description as description, TeacherID as teacherId, CreatedAt as createdAt, UpdatedAt as updatedAt FROM class WHERE TeacherID = #{teacherId} ORDER BY CreatedAt DESC")
    List<CourseClass> findByTeacher(Integer teacherId);

    @Select("SELECT ClassID as classId, Name as name, Description as description, TeacherID as teacherId, CreatedAt as createdAt, UpdatedAt as updatedAt FROM class WHERE ClassID = #{classId}")
    CourseClass findById(Integer classId);
}


