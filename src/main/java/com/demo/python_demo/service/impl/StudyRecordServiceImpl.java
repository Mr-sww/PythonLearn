package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.StudyRecord;
import com.demo.python_demo.repository.StudyRecordRepository;
import com.demo.python_demo.service.StudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习记录服务实现类
 */
@Service
public class StudyRecordServiceImpl implements StudyRecordService {

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Override
    public List<StudyRecord> getStudyRecordsByUserId(Integer userId) {
        return studyRecordRepository.findByUserId(userId);
    }

    @Override
    public List<StudyRecord> getStudyRecordsByUserIdAndCourseId(Integer userId, Integer courseId) {
        return studyRecordRepository.findByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public StudyRecord saveStudyRecord(StudyRecord record) {
        if (record.getStudyDate() == null) {
            record.setStudyDate(LocalDateTime.now());
        }
        int result = studyRecordRepository.insert(record);
        if (result > 0) {
            return record;
        }
        return null;
    }

    @Override
    public StudyRecord updateStudyRecord(StudyRecord record) {
        int result = studyRecordRepository.update(record);
        if (result > 0) {
            return record;
        }
        return null;
    }

    @Override
    public boolean deleteStudyRecord(Integer recordId) {
        int result = studyRecordRepository.deleteById(recordId);
        return result > 0;
    }

    @Override
    public List<StudyRecord> getRecentStudyRecords(Integer userId, Integer limit) {
        return studyRecordRepository.findRecentRecords(userId, limit);
    }

    @Override
    public Map<String, Object> getUserStudyStatistics(Integer userId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取基础统计信息
        Map<String, Object> stats = studyRecordRepository.getUserStatistics(userId);
        if (stats != null) {
            statistics.put("totalCourses", stats.get("totalCourses"));
            statistics.put("totalRecords", stats.get("totalRecords"));
            statistics.put("totalStudyTime", stats.get("totalStudyTime"));
            statistics.put("completedLessons", stats.get("completedLessons"));
        } else {
            statistics.put("totalCourses", 0);
            statistics.put("totalRecords", 0);
            statistics.put("totalStudyTime", 0);
            statistics.put("completedLessons", 0);
        }
        
        // 获取连续学习天数
        int continuousDays = studyRecordRepository.getContinuousDays(userId);
        statistics.put("continuousDays", continuousDays);
        
        // 计算学习时长（小时）
        Integer totalStudyTime = (Integer) statistics.get("totalStudyTime");
        if (totalStudyTime != null) {
            double totalStudyHours = Math.round((totalStudyTime / 3600.0) * 10) / 10.0;
            statistics.put("totalStudyHours", totalStudyHours);
        } else {
            statistics.put("totalStudyHours", 0.0);
        }
        
        return statistics;
    }

    @Override
    public boolean recordStudyProgress(Integer userId, Integer courseId, Integer lessonId, 
                                     Integer studyTime, Double progress, Boolean completed) {
        StudyRecord record = new StudyRecord(userId, courseId, lessonId);
        record.setStudyTime(studyTime);
        record.setProgress(progress);
        record.setCompleted(completed);
        record.setStudyDate(LocalDateTime.now());
        
        StudyRecord savedRecord = saveStudyRecord(record);
        return savedRecord != null;
    }
}
