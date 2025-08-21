package com.demo.python_demo.service;

import com.demo.python_demo.entity.StudyRecord;

import java.util.List;
import java.util.Map;

/**
 * 学习记录服务接口
 */
public interface StudyRecordService {
    
    /**
     * 根据用户ID获取学习记录列表
     */
    List<StudyRecord> getStudyRecordsByUserId(Integer userId);
    
    /**
     * 根据用户ID和课程ID获取学习记录
     */
    List<StudyRecord> getStudyRecordsByUserIdAndCourseId(Integer userId, Integer courseId);
    
    /**
     * 保存学习记录
     */
    StudyRecord saveStudyRecord(StudyRecord record);
    
    /**
     * 更新学习记录
     */
    StudyRecord updateStudyRecord(StudyRecord record);
    
    /**
     * 删除学习记录
     */
    boolean deleteStudyRecord(Integer recordId);
    
    /**
     * 获取用户最近的学习记录
     */
    List<StudyRecord> getRecentStudyRecords(Integer userId, Integer limit);
    
    /**
     * 获取用户学习统计
     */
    Map<String, Object> getUserStudyStatistics(Integer userId);
    
    /**
     * 记录学习进度
     */
    boolean recordStudyProgress(Integer userId, Integer courseId, Integer lessonId, 
                               Integer studyTime, Double progress, Boolean completed);
}
