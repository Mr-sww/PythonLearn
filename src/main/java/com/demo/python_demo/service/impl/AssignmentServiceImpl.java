package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.Assignment;
import com.demo.python_demo.entity.AssignmentSubmission;
import com.demo.python_demo.repository.AssignmentRepository;
import com.demo.python_demo.repository.AssignmentSubmissionRepository;
import com.demo.python_demo.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作业服务实现类
 */
@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment getAssignmentById(Integer assignmentId) {
        return assignmentRepository.findByIdWithDetails(assignmentId);
    }

    @Override
    public List<Assignment> getAssignmentsByCourseId(Integer courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    @Override
    public List<Assignment> getAssignmentsByTeacherId(Integer teacherId) {
        return assignmentRepository.findByTeacherIdWithCourse(teacherId);
    }

    @Override
    public Assignment createAssignment(Assignment assignment) {
        try {
            // 设置默认值
            if (assignment.getStatus() == null) {
                assignment.setStatus("draft");
            }
            if (assignment.getMaxScore() == null) {
                assignment.setMaxScore(100);
            }
            
            int result = assignmentRepository.insert(assignment);
            if (result > 0) {
                return getAssignmentById(assignment.getAssignmentId());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Assignment updateAssignment(Assignment assignment) {
        try {
            int result = assignmentRepository.update(assignment);
            if (result > 0) {
                return getAssignmentById(assignment.getAssignmentId());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteAssignment(Integer assignmentId, Integer teacherId) {
        try {
            // 检查作业是否属于该教师
            Assignment assignment = assignmentRepository.findById(assignmentId);
            if (assignment == null || !assignment.getTeacherId().equals(teacherId)) {
                return false;
            }
            
            int result = assignmentRepository.deleteById(assignmentId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean publishAssignment(Integer assignmentId, Integer teacherId) {
        try {
            // 检查作业是否属于该教师
            Assignment assignment = assignmentRepository.findById(assignmentId);
            if (assignment == null || !assignment.getTeacherId().equals(teacherId)) {
                return false;
            }
            
            assignment.setStatus("published");
            int result = assignmentRepository.update(assignment);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeAssignment(Integer assignmentId, Integer teacherId) {
        try {
            // 检查作业是否属于该教师
            Assignment assignment = assignmentRepository.findById(assignmentId);
            if (assignment == null || !assignment.getTeacherId().equals(teacherId)) {
                return false;
            }
            
            assignment.setStatus("closed");
            int result = assignmentRepository.update(assignment);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AssignmentSubmission> getSubmissionsByAssignmentId(Integer assignmentId) {
        return submissionRepository.findByAssignmentIdWithStudent(assignmentId);
    }

    @Override
    public AssignmentSubmission getSubmissionByStudentAndAssignment(Integer studentId, Integer assignmentId) {
        return submissionRepository.findByStudentAndAssignment(studentId, assignmentId);
    }

    @Override
    public AssignmentSubmission submitAssignment(AssignmentSubmission submission) {
        try {
            // 检查是否已经提交过
            AssignmentSubmission existingSubmission = getSubmissionByStudentAndAssignment(
                submission.getStudentId(), submission.getAssignmentId());
            
            if (existingSubmission != null) {
                // 更新现有提交
                submission.setSubmissionId(existingSubmission.getSubmissionId());
                submission.setStatus("submitted");
                int result = submissionRepository.update(submission);
                if (result > 0) {
                    return submissionRepository.findByIdWithDetails(submission.getSubmissionId());
                }
            } else {
                // 创建新提交
                submission.setStatus("submitted");
                int result = submissionRepository.insert(submission);
                if (result > 0) {
                    return submissionRepository.findByIdWithDetails(submission.getSubmissionId());
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean gradeSubmission(Integer submissionId, Double score, String feedback, Integer teacherId) {
        try {
            // 检查提交是否存在
            AssignmentSubmission submission = submissionRepository.findById(submissionId);
            if (submission == null) {
                return false;
            }
            
            // 检查作业是否属于该教师
            Assignment assignment = assignmentRepository.findById(submission.getAssignmentId());
            if (assignment == null || !assignment.getTeacherId().equals(teacherId)) {
                return false;
            }
            
            int result = submissionRepository.gradeSubmission(submissionId, score, feedback);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AssignmentSubmission> getSubmissionsByStudentId(Integer studentId) {
        return submissionRepository.findByStudentIdWithAssignment(studentId);
    }

    @Override
    public Object getTeacherAssignmentStatistics(Integer teacherId) {
        Map<String, Object> statistics = new HashMap<>();
        
        List<Assignment> assignments = getAssignmentsByTeacherId(teacherId);
        
        // 总作业数
        statistics.put("totalAssignments", assignments.size());
        
        // 草稿作业数
        long draftCount = assignments.stream()
                .filter(a -> "draft".equals(a.getStatus()))
                .count();
        statistics.put("draftAssignments", draftCount);
        
        // 已发布作业数
        long publishedCount = assignments.stream()
                .filter(a -> "published".equals(a.getStatus()))
                .count();
        statistics.put("publishedAssignments", publishedCount);
        
        // 已关闭作业数
        long closedCount = assignments.stream()
                .filter(a -> "closed".equals(a.getStatus()))
                .count();
        statistics.put("closedAssignments", closedCount);
        
        // 总提交数
        long totalSubmissions = assignments.stream()
                .mapToLong(a -> getSubmissionsByAssignmentId(a.getAssignmentId()).size())
                .sum();
        statistics.put("totalSubmissions", totalSubmissions);
        
        // 已批改数
        long gradedSubmissions = assignments.stream()
                .mapToLong(a -> getSubmissionsByAssignmentId(a.getAssignmentId()).stream()
                        .filter(s -> "graded".equals(s.getStatus()))
                        .count())
                .sum();
        statistics.put("gradedSubmissions", gradedSubmissions);
        
        return statistics;
    }

    @Override
    public Object getStudentAssignmentStatistics(Integer studentId) {
        Map<String, Object> statistics = new HashMap<>();
        
        List<AssignmentSubmission> submissions = getSubmissionsByStudentId(studentId);
        
        // 总提交数
        statistics.put("totalSubmissions", submissions.size());
        
        // 已提交数
        long submittedCount = submissions.stream()
                .filter(s -> "submitted".equals(s.getStatus()))
                .count();
        statistics.put("submittedCount", submittedCount);
        
        // 已批改数
        long gradedCount = submissions.stream()
                .filter(s -> "graded".equals(s.getStatus()))
                .count();
        statistics.put("gradedCount", gradedCount);
        
        // 平均分数
        double averageScore = submissions.stream()
                .filter(s -> s.getScore() != null)
                .mapToDouble(AssignmentSubmission::getScore)
                .average()
                .orElse(0.0);
        statistics.put("averageScore", averageScore);
        
        // 最高分数
        double maxScore = submissions.stream()
                .filter(s -> s.getScore() != null)
                .mapToDouble(AssignmentSubmission::getScore)
                .max()
                .orElse(0.0);
        statistics.put("maxScore", maxScore);
        
        return statistics;
    }
} 