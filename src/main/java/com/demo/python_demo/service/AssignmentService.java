package com.demo.python_demo.service;

import com.demo.python_demo.entity.Assignment;
import com.demo.python_demo.entity.AssignmentSubmission;

import java.util.List;

/**
 * 作业服务接口
 */
public interface AssignmentService {

    /**
     * 获取所有作业
     */
    List<Assignment> getAllAssignments();

    /**
     * 根据ID获取作业
     */
    Assignment getAssignmentById(Integer assignmentId);

    /**
     * 根据课程ID获取作业
     */
    List<Assignment> getAssignmentsByCourseId(Integer courseId);

    /**
     * 根据教师ID获取作业
     */
    List<Assignment> getAssignmentsByTeacherId(Integer teacherId);

    /**
     * 创建作业
     */
    Assignment createAssignment(Assignment assignment);

    /**
     * 更新作业
     */
    Assignment updateAssignment(Assignment assignment);

    /**
     * 删除作业
     */
    boolean deleteAssignment(Integer assignmentId, Integer teacherId);

    /**
     * 发布作业
     */
    boolean publishAssignment(Integer assignmentId, Integer teacherId);

    /**
     * 关闭作业
     */
    boolean closeAssignment(Integer assignmentId, Integer teacherId);

    /**
     * 获取作业的所有提交
     */
    List<AssignmentSubmission> getSubmissionsByAssignmentId(Integer assignmentId);

    /**
     * 获取学生的作业提交
     */
    AssignmentSubmission getSubmissionByStudentAndAssignment(Integer studentId, Integer assignmentId);

    /**
     * 学生提交作业
     */
    AssignmentSubmission submitAssignment(AssignmentSubmission submission);

    /**
     * 批改作业
     */
    boolean gradeSubmission(Integer submissionId, Double score, String feedback, Integer teacherId);

    /**
     * 获取学生的所有作业提交
     */
    List<AssignmentSubmission> getSubmissionsByStudentId(Integer studentId);

    /**
     * 获取教师的作业统计
     */
    Object getTeacherAssignmentStatistics(Integer teacherId);

    /**
     * 获取学生的作业统计
     */
    Object getStudentAssignmentStatistics(Integer studentId);
} 