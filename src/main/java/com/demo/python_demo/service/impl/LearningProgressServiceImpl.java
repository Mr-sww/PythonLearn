package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.LearningProgress;
import com.demo.python_demo.repository.LearningProgressRepository;
import com.demo.python_demo.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 学习进度服务实现类
 */
@Service
public class LearningProgressServiceImpl implements LearningProgressService {

    @Autowired
    private LearningProgressRepository progressRepository;

    @Override
    public Optional<LearningProgress> getProgressByUserAndCourse(Integer userId, Integer courseId) {
        LearningProgress progress = progressRepository.findByUserAndCourse(userId, courseId);
        return Optional.ofNullable(progress);
    }

    @Override
    public List<LearningProgress> getProgressByUserId(Integer userId) {
        return progressRepository.findByUserId(userId);
    }

    @Override
    public LearningProgress saveProgress(LearningProgress progress) {
        if (progress == null) {
            throw new IllegalArgumentException("学习进度信息不能为空");
        }
        
        // 设置默认值
        if (progress.getProgress() == null) {
            progress.setProgress(0.0);
        }
        if (progress.getStatus() == null) {
            progress.setStatus("not_started");
        }
        if (progress.getTimeSpent() == null) {
            progress.setTimeSpent(0);
        }
        
        int result = progressRepository.insertOrUpdate(progress);
        if (result > 0) {
            return progress;
        }
        return null;
    }

    @Override
    public Optional<LearningProgress> updateProgress(Integer progressId, LearningProgress progress) {
        if (progressId == null || progress == null) {
            return Optional.empty();
        }
        
        LearningProgress existingProgress = progressRepository.findByUserAndCourse(
            progress.getUserId(), progress.getCourseId());
        if (existingProgress == null) {
            return Optional.empty();
        }
        
        // 更新字段
        if (progress.getChapterId() != null) {
            existingProgress.setChapterId(progress.getChapterId());
        }
        if (progress.getLessonId() != null) {
            existingProgress.setLessonId(progress.getLessonId());
        }
        if (progress.getProgress() != null) {
            existingProgress.setProgress(progress.getProgress());
        }
        if (progress.getStatus() != null) {
            existingProgress.setStatus(progress.getStatus());
        }
        if (progress.getTimeSpent() != null) {
            existingProgress.setTimeSpent(progress.getTimeSpent());
        }
        
        int result = progressRepository.update(existingProgress);
        if (result > 0) {
            return Optional.of(existingProgress);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteProgress(Integer progressId) {
        if (progressId == null) {
            return false;
        }
        int result = progressRepository.deleteById(progressId);
        return result > 0;
    }

    @Override
    public List<LearningProgress> getRecentCourses(Integer userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }
        return progressRepository.findRecentCourses(userId, limit);
    }

    @Override
    public Object getUserStatistics(Integer userId) {
        return progressRepository.getUserStatistics(userId);
    }

    @Override
    public int getCourseStudentCount(Integer courseId) {
        return progressRepository.countByCourseId(courseId);
    }

    @Override
    public int getCourseCompletedCount(Integer courseId) {
        return progressRepository.countCompletedByCourseId(courseId);
    }

    @Override
    public boolean updateLessonProgress(Integer userId, Integer courseId, Integer lessonId, Double progress, Integer timeSpent) {
        LearningProgress learningProgress = progressRepository.findByUserAndCourse(userId, courseId);
        
        if (learningProgress == null) {
            // 创建新的学习进度
            learningProgress = new LearningProgress(userId, courseId);
            learningProgress.setLessonId(lessonId);
            learningProgress.setProgress(progress);
            learningProgress.setTimeSpent(timeSpent);
            learningProgress.setStatus("in_progress");
            
            int result = progressRepository.insert(learningProgress);
            return result > 0;
        } else {
            // 更新现有学习进度
            learningProgress.setLessonId(lessonId);
            learningProgress.setProgress(progress);
            learningProgress.setTimeSpent(learningProgress.getTimeSpent() + timeSpent);
            learningProgress.setStatus("in_progress");
            
            int result = progressRepository.update(learningProgress);
            return result > 0;
        }
    }

    @Override
    public boolean completeCourse(Integer userId, Integer courseId) {
        LearningProgress progress = progressRepository.findByUserAndCourse(userId, courseId);
        if (progress == null) {
            return false;
        }
        
        progress.setProgress(100.0);
        progress.setStatus("completed");
        progress.setLastStudyTime(LocalDateTime.now());
        
        int result = progressRepository.update(progress);
        return result > 0;
    }
} 