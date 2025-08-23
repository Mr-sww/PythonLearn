package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.KnowledgeStudyRecord;
import com.demo.python_demo.entity.VideoWatchRecord;
import com.demo.python_demo.repository.KnowledgeStudyRecordRepository;
import com.demo.python_demo.repository.VideoWatchRecordRepository;
import com.demo.python_demo.service.LearningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习记录服务实现类
 */
@Service
public class LearningRecordServiceImpl implements LearningRecordService {

    @Autowired
    private KnowledgeStudyRecordRepository knowledgeStudyRecordRepository;

    @Autowired
    private VideoWatchRecordRepository videoWatchRecordRepository;

    // 知识点学习记录相关方法

    @Override
    public KnowledgeStudyRecord startKnowledgeStudy(Integer userId, Integer knowledgeId, String knowledgeTitle) {
        // 检查是否已有学习记录
        KnowledgeStudyRecord existingRecord = knowledgeStudyRecordRepository.findByUserIdAndKnowledgeId(userId, knowledgeId);
        
        if (existingRecord != null) {
            // 如果已有记录，更新状态为重新开始
            existingRecord.setStatus("started");
            existingRecord.setStartTime(LocalDateTime.now());
            existingRecord.setProgress(new BigDecimal("0.00"));
            existingRecord.setStudyTime(0);
            existingRecord.setEndTime(null); // 清除结束时间
            knowledgeStudyRecordRepository.update(existingRecord);
            return existingRecord;
        } else {
            // 创建新的学习记录
            KnowledgeStudyRecord newRecord = new KnowledgeStudyRecord(userId, knowledgeId, knowledgeTitle);
            knowledgeStudyRecordRepository.insert(newRecord);
            return newRecord;
        }
    }

    @Override
    public boolean updateKnowledgeProgress(Integer userId, Integer knowledgeId, Integer studyTime, Double progress) {
        KnowledgeStudyRecord record = knowledgeStudyRecordRepository.findByUserIdAndKnowledgeId(userId, knowledgeId);
        if (record != null) {
            record.setStudyTime(studyTime);
            record.setProgress(new BigDecimal(progress.toString()));
            record.setStatus("in_progress");
            record.setLastStudyTime(LocalDateTime.now());
            return knowledgeStudyRecordRepository.update(record) > 0;
        }
        return false;
    }

    @Override
    public boolean completeKnowledgeStudy(Integer userId, Integer knowledgeId) {
        KnowledgeStudyRecord record = knowledgeStudyRecordRepository.findByUserIdAndKnowledgeId(userId, knowledgeId);
        if (record != null) {
            record.setStatus("completed");
            record.setProgress(new BigDecimal("100.00"));
            record.setEndTime(LocalDateTime.now());
            record.setLastStudyTime(LocalDateTime.now());
            return knowledgeStudyRecordRepository.update(record) > 0;
        }
        return false;
    }

    @Override
    public List<KnowledgeStudyRecord> getKnowledgeRecords(Integer userId, Integer limit) {
        return knowledgeStudyRecordRepository.findRecentByUserId(userId, limit);
    }

    @Override
    public Object getKnowledgeStudyStats(Integer userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 总知识点数
        int totalKnowledge = knowledgeStudyRecordRepository.countByUserId(userId);
        stats.put("totalKnowledge", totalKnowledge);
        
        // 已完成知识点数
        int completedKnowledge = knowledgeStudyRecordRepository.countCompletedByUserId(userId);
        stats.put("completedKnowledge", completedKnowledge);
        
        // 总学习时长（秒）
        Integer totalStudyTime = knowledgeStudyRecordRepository.sumStudyTimeByUserId(userId);
        stats.put("totalStudyTime", totalStudyTime != null ? totalStudyTime : 0);
        
        // 连续学习天数（简化实现，返回1或0）
        int continuousDays = totalKnowledge > 0 ? 1 : 0;
        stats.put("continuousDays", continuousDays);
        
        return stats;
    }

    // 视频观看记录相关方法

    @Override
    public VideoWatchRecord startVideoWatch(Integer userId, Integer videoId, String videoTitle, String videoUrl, Integer totalDuration) {
        // 检查是否已有观看记录
        VideoWatchRecord existingRecord = videoWatchRecordRepository.findByUserIdAndVideoId(userId, videoId);
        
        if (existingRecord != null) {
            // 如果已有记录，更新状态为重新开始
            existingRecord.setStatus("started");
            existingRecord.setStartTime(LocalDateTime.now());
            existingRecord.setProgress(new BigDecimal("0.00"));
            existingRecord.setWatchTime(0);
            videoWatchRecordRepository.update(existingRecord);
            return existingRecord;
        } else {
            // 创建新的观看记录
            VideoWatchRecord newRecord = new VideoWatchRecord(userId, videoId, videoTitle, videoUrl, totalDuration);
            videoWatchRecordRepository.insert(newRecord);
            return newRecord;
        }
    }

    @Override
    public boolean updateVideoProgress(Integer userId, Integer videoId, Integer watchTime, Double progress) {
        VideoWatchRecord record = videoWatchRecordRepository.findByUserIdAndVideoId(userId, videoId);
        if (record != null) {
            record.setWatchTime(watchTime);
            record.setProgress(new BigDecimal(progress.toString()));
            record.setStatus("in_progress");
            record.setLastWatchTime(LocalDateTime.now());
            return videoWatchRecordRepository.update(record) > 0;
        }
        return false;
    }

    @Override
    public boolean completeVideoWatch(Integer userId, Integer videoId) {
        VideoWatchRecord record = videoWatchRecordRepository.findByUserIdAndVideoId(userId, videoId);
        if (record != null) {
            record.setStatus("completed");
            record.setProgress(new BigDecimal("100.00"));
            record.setEndTime(LocalDateTime.now());
            record.setLastWatchTime(LocalDateTime.now());
            return videoWatchRecordRepository.update(record) > 0;
        }
        return false;
    }

    @Override
    public List<VideoWatchRecord> getVideoRecords(Integer userId, Integer limit) {
        return videoWatchRecordRepository.findRecentByUserId(userId, limit);
    }

    @Override
    public Object getVideoWatchStats(Integer userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", videoWatchRecordRepository.countByUserId(userId));
        stats.put("completedRecords", videoWatchRecordRepository.countCompletedByUserId(userId));
        return stats;
    }
}
