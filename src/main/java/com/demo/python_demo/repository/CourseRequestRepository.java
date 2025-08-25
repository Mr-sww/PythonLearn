package com.demo.python_demo.repository;

import com.demo.python_demo.entity.CourseRequest;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CourseRequestRepository {

    @Insert("INSERT INTO course_request (TeacherID, Title, Description, CoverImage) VALUES (#{teacherId}, #{title}, #{description}, #{coverImage})")
    @Options(useGeneratedKeys = true, keyProperty = "requestId")
    int insert(CourseRequest r);

    @Select("SELECT RequestID as requestId, TeacherID as teacherId, Title as title, Description as description, CoverImage as coverImage, Status as status, ReviewNote as reviewNote, CreatedAt as createdAt, ReviewedAt as reviewedAt FROM course_request WHERE TeacherID = #{teacherId} ORDER BY CreatedAt DESC")
    List<CourseRequest> findByTeacher(Integer teacherId);

    @Select("SELECT RequestID as requestId, TeacherID as teacherId, Title as title, Description as description, CoverImage as coverImage, Status as status, ReviewNote as reviewNote, CreatedAt as createdAt, ReviewedAt as reviewedAt FROM course_request WHERE Status = #{status} ORDER BY CreatedAt ASC")
    List<CourseRequest> findByStatus(String status);

    @Update("UPDATE course_request SET Status = #{status}, ReviewNote = #{reviewNote}, ReviewedAt = NOW() WHERE RequestID = #{requestId}")
    int updateStatus(Integer requestId, String status, String reviewNote);
}


