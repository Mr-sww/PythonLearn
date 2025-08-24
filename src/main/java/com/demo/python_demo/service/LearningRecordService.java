package com.demo.python_demo.service;

import com.demo.python_demo.entity.KnowledgeStudyRecord;
import com.demo.python_demo.entity.VideoWatchRecord;

import java.util.List;

/**
 * 学习记录服务接口
 */
public interface LearningRecordService {

    // 知识点学习记录相关方法

    /**
     * 开始知识点学习
     */
    KnowledgeStudyRecord startKnowledgeStudy(Integer userId, Integer knowledgeId, String knowledgeTitle, String contentType);

    /**
     * 更新知识点学习进度
     */
    boolean updateKnowledgeProgress(Integer userId, Integer knowledgeId, Integer studyTime, Double progress);

    /**
     * 完成知识点学习
     */
    boolean completeKnowledgeStudy(Integer userId, Integer knowledgeId);

    /**
     * 获取用户的知识点学习记录
     */
    List<KnowledgeStudyRecord> getKnowledgeRecords(Integer userId, Integer limit);

    /**
     * 获取用户的知识点学习统计
     */
    Object getKnowledgeStudyStats(Integer userId);

    // 视频观看记录相关方法

    /**
     * 开始视频观看
     */
    VideoWatchRecord startVideoWatch(Integer userId, Integer videoId, String videoTitle, String videoUrl, Integer totalDuration);

    /**
     * 更新视频观看进度
     */
    boolean updateVideoProgress(Integer userId, Integer videoId, Integer watchTime, Double progress);

    /**
     * 完成视频观看
     */
    boolean completeVideoWatch(Integer userId, Integer videoId);

    /**
     * 获取用户的视频观看记录
     */
    List<VideoWatchRecord> getVideoRecords(Integer userId, Integer limit);

    /**
     * 获取用户的视频观看统计
     */
    Object getVideoWatchStats(Integer userId);
}

