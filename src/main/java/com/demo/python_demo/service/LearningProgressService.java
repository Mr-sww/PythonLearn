package com.demo.python_demo.service;

import com.demo.python_demo.entity.LearningProgress;
import java.util.List;
import java.util.Optional;

public interface LearningProgressService {
    Optional<LearningProgress> getProgressByUserAndCourse(Integer userId, Integer courseId);
    List<LearningProgress> getProgressByUserId(Integer userId);
    LearningProgress saveProgress(LearningProgress progress);
    Optional<LearningProgress> updateProgress(Integer progressId, LearningProgress progress);
    boolean deleteProgress(Integer progressId);
    List<LearningProgress> getRecentCourses(Integer userId, Integer limit);
    Object getUserStatistics(Integer userId);
    int getCourseStudentCount(Integer courseId);
    int getCourseCompletedCount(Integer courseId);
    boolean updateLessonProgress(Integer userId, Integer courseId, Integer lessonId, Double progress, Integer timeSpent);
    boolean completeCourse(Integer userId, Integer courseId);
}
