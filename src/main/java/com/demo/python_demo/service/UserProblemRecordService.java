package com.demo.python_demo.service;

import com.demo.python_demo.entity.UserProblemRecord;
import java.util.List;

public interface UserProblemRecordService {
    void saveRecord(UserProblemRecord record);
    int getTotalSubmissions(Integer userId);
    int getPassedProblems(Integer userId);
    double getAccuracy(Integer userId);

    // 统计卡片一站式接口
    java.util.Map<String, Object> getUserStatistics(Integer userId);

    int getContinuousDays(Integer userId);
} 