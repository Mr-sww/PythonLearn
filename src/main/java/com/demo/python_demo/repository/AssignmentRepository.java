package com.demo.python_demo.repository;

import com.demo.python_demo.entity.Assignment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AssignmentRepository {
    
    @Select("SELECT assignment_id AS assignmentId, course_id AS courseId, teacher_id AS teacherId, title, description, content, due_date AS dueDate, max_score AS maxScore, status, create_time AS createTime, update_time AS updateTime FROM `assignment`")
    List<Assignment> findAll();
    
    @Select("SELECT assignment_id AS assignmentId, course_id AS courseId, teacher_id AS teacherId, title, description, content, due_date AS dueDate, max_score AS maxScore, status, create_time AS createTime, update_time AS updateTime FROM `assignment` WHERE assignment_id = #{assignmentId}")
    Assignment findById(Integer assignmentId);
    
    @Select("SELECT assignment_id AS assignmentId, course_id AS courseId, teacher_id AS teacherId, title, description, content, due_date AS dueDate, max_score AS maxScore, status, create_time AS createTime, update_time AS updateTime FROM `assignment` WHERE course_id = #{courseId}")
    List<Assignment> findByCourseId(Integer courseId);
    
    @Select("SELECT assignment_id AS assignmentId, course_id AS courseId, teacher_id AS teacherId, title, description, content, due_date AS dueDate, max_score AS maxScore, status, create_time AS createTime, update_time AS updateTime FROM `assignment` WHERE teacher_id = #{teacherId}")
    List<Assignment> findByTeacherId(Integer teacherId);
    
    @Insert("INSERT INTO `assignment` (course_id, teacher_id, title, description, content, due_date, max_score, status) VALUES (#{courseId}, #{teacherId}, #{title}, #{description}, #{content}, #{dueDate}, #{maxScore}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "assignmentId")
    int insert(Assignment assignment);
    
    @Update("UPDATE `assignment` SET course_id=#{courseId}, teacher_id=#{teacherId}, title=#{title}, description=#{description}, content=#{content}, due_date=#{dueDate}, max_score=#{maxScore}, status=#{status} WHERE assignment_id=#{assignmentId}")
    int update(Assignment assignment);
    
    @Delete("DELETE FROM `assignment` WHERE assignment_id = #{assignmentId}")
    int deleteById(Integer assignmentId);
    
    // 获取作业详情（包含课程和教师信息）
    @Select("SELECT a.assignment_id AS assignmentId, a.course_id AS courseId, a.teacher_id AS teacherId, " +
            "a.title, a.description, a.content, a.due_date AS dueDate, a.max_score AS maxScore, " +
            "a.status, a.create_time AS createTime, a.update_time AS updateTime, " +
            "c.Title AS courseTitle, u.nickname AS teacherName " +
            "FROM `assignment` a " +
            "LEFT JOIN `course` c ON a.course_id = c.ArticleID " +
            "LEFT JOIN `user` u ON a.teacher_id = u.user_id " +
            "WHERE a.assignment_id = #{assignmentId}")
    Assignment findByIdWithDetails(Integer assignmentId);
    
    // 获取教师的所有作业（包含课程信息）
    @Select("SELECT a.assignment_id AS assignmentId, a.course_id AS courseId, a.teacher_id AS teacherId, " +
            "a.title, a.description, a.content, a.due_date AS dueDate, a.max_score AS maxScore, " +
            "a.status, a.create_time AS createTime, a.update_time AS updateTime, " +
            "c.Title AS courseTitle " +
            "FROM `assignment` a " +
            "LEFT JOIN `course` c ON a.course_id = c.ArticleID " +
            "WHERE a.teacher_id = #{teacherId} " +
            "ORDER BY a.create_time DESC")
    List<Assignment> findByTeacherIdWithCourse(Integer teacherId);
} 