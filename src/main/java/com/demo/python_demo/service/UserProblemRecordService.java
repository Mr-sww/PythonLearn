package com.demo.python_demo.service;

import com.demo.python_demo.entity.UserProblemRecord;
import java.util.List;

public interface UserProblemRecordService {
    void saveRecord(UserProblemRecord record);
    
    /**
     * 获取用户提交总数
     * @param userId 用户ID，如果为0则获取所有用户的提交数
     * @return 提交总数
     */
    int getTotalSubmissions(Integer userId);
    
    int getPassedProblems(Integer userId);
    double getAccuracy(Integer userId);

    // 统计卡片一站式接口
    java.util.Map<String, Object> getUserStatistics(Integer userId);

    int getContinuousDays(Integer userId);

    // 活跃天数（有提交的天）
    int getActiveDays(Integer userId);
    
    // 获取用户做题记录（分页）
    List<java.util.Map<String, Object>> getUserProblemRecordsWithPagination(Integer userId, int offset, int size);
    
    // 获取用户做题记录总数
    int getUserProblemRecordsCount(Integer userId);
    
    // 根据结果筛选用户做题记录（分页）
    List<java.util.Map<String, Object>> getUserProblemRecordsByResultWithPagination(Integer userId, String result, int offset, int size);
    
    // 根据结果筛选用户做题记录总数
    int getUserProblemRecordsByResultCount(Integer userId, String result);
    
    // 获取每日做题趋势
    List<java.util.Map<String, Object>> getDailySubmissionTrend(Integer userId);
    
    // 获取结果分布
    List<java.util.Map<String, Object>> getResultDistribution(Integer userId);
} 