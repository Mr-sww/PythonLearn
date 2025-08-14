package com.demo.python_demo.repository;

import com.demo.python_demo.entity.AssignmentSubmission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AssignmentSubmissionRepository {
    
    @Select("SELECT submission_id AS submissionId, assignment_id AS assignmentId, student_id AS studentId, " +
            "content, score, feedback, submit_time AS submitTime, grade_time AS gradeTime, status " +
            "FROM `assignment_submission`")
    List<AssignmentSubmission> findAll();
    
    @Select("SELECT submission_id AS submissionId, assignment_id AS assignmentId, student_id AS studentId, " +
            "content, score, feedback, submit_time AS submitTime, grade_time AS gradeTime, status " +
            "FROM `assignment_submission` WHERE submission_id = #{submissionId}")
    AssignmentSubmission findById(Integer submissionId);
    
    @Select("SELECT submission_id AS submissionId, assignment_id AS assignmentId, student_id AS studentId, " +
            "content, score, feedback, submit_time AS submitTime, grade_time AS gradeTime, status " +
            "FROM `assignment_submission` WHERE assignment_id = #{assignmentId}")
    List<AssignmentSubmission> findByAssignmentId(Integer assignmentId);
    
    @Select("SELECT submission_id AS submissionId, assignment_id AS assignmentId, student_id AS studentId, " +
            "content, score, feedback, submit_time AS submitTime, grade_time AS gradeTime, status " +
            "FROM `assignment_submission` WHERE student_id = #{studentId}")
    List<AssignmentSubmission> findByStudentId(Integer studentId);
    
    @Select("SELECT submission_id AS submissionId, assignment_id AS assignmentId, student_id AS studentId, " +
            "content, score, feedback, submit_time AS submitTime, grade_time AS gradeTime, status " +
            "FROM `assignment_submission` WHERE student_id = #{studentId} AND assignment_id = #{assignmentId}")
    AssignmentSubmission findByStudentAndAssignment(@Param("studentId") Integer studentId, @Param("assignmentId") Integer assignmentId);
    
    @Insert("INSERT INTO `assignment_submission` (assignment_id, student_id, content, status) " +
            "VALUES (#{assignmentId}, #{studentId}, #{content}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "submissionId")
    int insert(AssignmentSubmission submission);
    
    @Update("UPDATE `assignment_submission` SET content=#{content}, status=#{status} WHERE submission_id=#{submissionId}")
    int update(AssignmentSubmission submission);
    
    @Update("UPDATE `assignment_submission` SET score=#{score}, feedback=#{feedback}, grade_time=NOW(), status='graded' " +
            "WHERE submission_id=#{submissionId}")
    int gradeSubmission(@Param("submissionId") Integer submissionId, @Param("score") Double score, @Param("feedback") String feedback);
    
    @Delete("DELETE FROM `assignment_submission` WHERE submission_id = #{submissionId}")
    int deleteById(Integer submissionId);
    
    // 获取作业提交详情（包含学生和作业信息）
    @Select("SELECT s.submission_id AS submissionId, s.assignment_id AS assignmentId, s.student_id AS studentId, " +
            "s.content, s.score, s.feedback, s.submit_time AS submitTime, s.grade_time AS gradeTime, s.status, " +
            "a.title AS assignmentTitle, u.nickname AS studentName, c.Title AS courseTitle " +
            "FROM `assignment_submission` s " +
            "LEFT JOIN `assignment` a ON s.assignment_id = a.assignment_id " +
            "LEFT JOIN `user` u ON s.student_id = u.user_id " +
            "LEFT JOIN `course` c ON a.course_id = c.ArticleID " +
            "WHERE s.submission_id = #{submissionId}")
    AssignmentSubmission findByIdWithDetails(Integer submissionId);
    
    // 获取作业的所有提交（包含学生信息）
    @Select("SELECT s.submission_id AS submissionId, s.assignment_id AS assignmentId, s.student_id AS studentId, " +
            "s.content, s.score, s.feedback, s.submit_time AS submitTime, s.grade_time AS gradeTime, s.status, " +
            "u.nickname AS studentName " +
            "FROM `assignment_submission` s " +
            "LEFT JOIN `user` u ON s.student_id = u.user_id " +
            "WHERE s.assignment_id = #{assignmentId} " +
            "ORDER BY s.submit_time DESC")
    List<AssignmentSubmission> findByAssignmentIdWithStudent(Integer assignmentId);
    
    // 获取学生的所有提交（包含作业信息）
    @Select("SELECT s.submission_id AS submissionId, s.assignment_id AS assignmentId, s.student_id AS studentId, " +
            "s.content, s.score, s.feedback, s.submit_time AS submitTime, s.grade_time AS gradeTime, s.status, " +
            "a.title AS assignmentTitle, c.Title AS courseTitle " +
            "FROM `assignment_submission` s " +
            "LEFT JOIN `assignment` a ON s.assignment_id = a.assignment_id " +
            "LEFT JOIN `course` c ON a.course_id = c.ArticleID " +
            "WHERE s.student_id = #{studentId} " +
            "ORDER BY s.submit_time DESC")
    List<AssignmentSubmission> findByStudentIdWithAssignment(Integer studentId);
} 