package com.demo.python_demo.repository;

import com.demo.python_demo.entity.CourseClass;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CourseClassRepository {

    @Insert("INSERT INTO class (Name, Description, TeacherID, class_type, max_students, start_date, status, current_students, tags) VALUES (#{name}, #{description}, #{teacherId}, #{classType}, #{maxStudents}, #{startDate}, #{status}, #{currentStudents}, #{tags})")
    @Options(useGeneratedKeys = true, keyProperty = "classId")
    int insert(CourseClass c);

    @Select("SELECT ClassID as classId, Name as name, Description as description, TeacherID as teacherId, class_type as classType, max_students as maxStudents, start_date as startDate, status, current_students as currentStudents, tags, CreatedAt as createdAt, UpdatedAt as updatedAt FROM class WHERE TeacherID = #{teacherId} ORDER BY CreatedAt DESC")
    List<CourseClass> findByTeacher(Integer teacherId);

    @Select("SELECT ClassID as classId, Name as name, Description as description, TeacherID as teacherId, class_type as classType, max_students as maxStudents, start_date as startDate, status, current_students as currentStudents, tags, CreatedAt as createdAt, UpdatedAt as updatedAt FROM class WHERE ClassID = #{classId}")
    CourseClass findById(Integer classId);

    @Update("UPDATE class SET Name = #{name}, Description = #{description}, class_type = #{classType}, max_students = #{maxStudents}, start_date = #{startDate}, status = #{status}, current_students = #{currentStudents}, tags = #{tags}, UpdatedAt = CURRENT_TIMESTAMP WHERE ClassID = #{classId}")
    int update(CourseClass c);

    @Delete("DELETE FROM class WHERE ClassID = #{classId}")
    int deleteById(Integer classId);
}


